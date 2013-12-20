package abcdef;

import DB.WeaponData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    protected int tileX = 1;
    protected int tileY = 1;
    private int actualX;
    private int actualY;
    private boolean moving = false;
    protected Map map;

    private Direction queuedMove = Direction.NONE;

    private float movementTimer = 0;
    private float movementTimerMax = 0.1f;
    private Direction moved = Direction.NONE;

    private boolean mapTakenTurn = false;

    protected int health = 10;
    protected int armour = 0;


    private float projectileTimer = 0;

    private boolean isDead = false;

    protected boolean isShooting = false;
    private int shootingIntersectionIndex = 0;
    private List<Point> shootingIntersections;

    private List<Projectile> projectiles = new ArrayList<Projectile>();


    public boolean isDead() {
        return isDead;
    }

    public Character(Map map) {
        this.map = map;
    }

    private void applyDamage(int damage) {
        int offsetDamage = armour > (int)(damage/3) ? (int)(damage/3) : armour;
        armour -= offsetDamage;
        damage -= offsetDamage;
        health -= damage;
    }

    protected void shoot(Character other) {
        Logger.shot(this, other);

        if (this instanceof Enemy && other instanceof Enemy)
            if (((Enemy)other).getData().canBeHurtByOtherEnemies())
                other.applyDamage(getWeapon().calculateDamage());

        if (this instanceof Player || other instanceof Player)
            other.applyDamage(getWeapon().calculateDamage());

        Logger.health(other);

    }

    public int getHealth() {
        return health;
    }

    protected abstract WeaponData getWeapon();

    private void handleDoorCollision(int destinationX, int destinationY) {
        //Enemies can't open doors
        if (this instanceof Enemy)
            return;

        Door door = map.getDoor(destinationX,destinationY);

        if (door != null)
            if (door.isClosed()) {
                door.open((Player)this);
            }
    }

    protected boolean hasLineOfSight(Character other) {
        List<Point> points = BresenhamsLineAlgorithm.getPoints(tileX,tileY, other.tileX, other.tileY);
        for (Point point : points)
            if (!map.isShootThroughable(point.x,point.y))
                return false;
        return true;
    }

    private void updateMovementTimer() {
        if (movementTimer > 0) {
            movementTimer -= Gdx.graphics.getDeltaTime();
            if (movementTimer <= 0) {
                movementTimer = 0;
                //The turn has the ended otherwise the player can tap the movement key lots and the enemies
                //won't get a turn
                endPlayersTurn();
            }
        }

        if (projectileTimer > 0) {
            projectileTimer -= Gdx.graphics.getDeltaTime();
            if (projectileTimer < 0)
                projectileTimer = 0;
        }
    }

    public int getActualX() {
        return actualX;
    }

    public int getActualY() {
        return actualY;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void shootAt(int tileX, int tileY) {
        if (isShooting)
            return;

        shootingIntersections = BresenhamsLineAlgorithm.getPoints(this.tileX, this.tileY, tileX, tileY);
        isShooting = true;

        List<Projectile> localProjectiles = new ArrayList<Projectile>();
        double startAngleOffset;
        if (getWeapon().getNumberOfProjectiles() == 0)
            startAngleOffset = 0;
        else if (getWeapon().getNumberOfProjectiles() % 2 == 0)
            //Even
            startAngleOffset =  (getWeapon().getAngleBetweenProjectiles() * (getWeapon().getNumberOfProjectiles()/2));
        else
            //Odd
            startAngleOffset = (getWeapon().getAngleBetweenProjectiles() * (getWeapon().getNumberOfProjectiles() - 1)/2);

        double currentAngle =  Math.atan2(tileY - this.tileY, tileX - this.tileX) - startAngleOffset;

        for (int i = 0; i < getWeapon().getNumberOfProjectiles(); i++) {
            localProjectiles.add(new Projectile(this.tileX,this.tileY, currentAngle, this, shootingIntersections));
            currentAngle += getWeapon().getAngleBetweenProjectiles();
        }

        projectiles.addAll(localProjectiles);

        startPlayersTurn();
    }

    public String getName() {
        if (this instanceof Player)
            return "PLAYER";
        else
            return "ENEMY";
    }

    public Map getMap() {
        return map;
    }

    public int getArmour() {
        return armour;
    }

    public enum Direction {
        LEFT, RIGHT, UP, DOWN, NONE
    }

    protected void move(Direction direction, boolean queuePriority) {

        if (moved != Direction.NONE) {
            if (queuePriority || !map.haveEnemiesFinishedTurn())
                queuedMove = direction;
            return;
        }

        startPlayersTurn();

        //Create the destination coordinates to be used for collision checks
        int tempX = tileX;
        int tempY = tileY;

        moved = direction;
        if (direction == Direction.UP)
            tempY++;
        else if (direction == Direction.DOWN)
            tempY--;
        else if (direction == Direction.LEFT)
            tempX--;
        else if (direction == Direction.RIGHT)
            tempX++;

        if (!map.isWalkable(tempX, tempY)) {
            handleDoorCollision(tempX, tempY);
            return;
        }

        tileX = tempX;
        tileY = tempY;

        //Check tile damage
        if (this instanceof Player) {
            Tile tile = map.getTile(tileX, tileY);
            if (tile != null)
                if (tile.getTileData().getDamageOnTouch() > 0)
                    applyDamage(tile.getTileData().getDamageOnTouch());
        }

        resetMovementTimer();
    }

    private void startPlayersTurn() {
        if (this instanceof Player)
            mapTakenTurn = false;
    }

    private void endPlayersTurn() {
        if (this instanceof Player)
            if (!mapTakenTurn) {
                map.takeTurn();
                mapTakenTurn = true;
            }
    }

    public void update() {
        //Check for death
        if (this instanceof Player) {
            if (health < 1)
                Engine.switchMenu(Engine.MenuState.DEATH);
        } else if (this instanceof Enemy)
            if (!isDead && health < 1) {
                isDead = true;
                map.getPlayer().increaseExperience(((Enemy)this).getData().getExperience());
            }

        //Smooth tile based movement
        if (movementTimer > 0) {
            float movementDistance = ((float) movementTimer / (float) movementTimerMax) * Map.getTileSize();
            if (moved == Direction.DOWN)
                actualY = (int)((float)(tileY) * (float)Map.getTileSize() + movementDistance);
            if (moved == Direction.UP)
                actualY = (int)((float)(tileY) * (float)Map.getTileSize() - movementDistance);
            if (moved == Direction.LEFT)
                actualX = (int)((float)(tileX) * (float)Map.getTileSize() + movementDistance);
            if (moved == Direction.RIGHT)
                actualX = (int)((float)(tileX) * (float)Map.getTileSize() - movementDistance);
        } else {

            handleEndLevelCollision();

            actualX = Map.getTileSize() * tileX;
            actualY = Map.getTileSize() * tileY;
            moved = Direction.NONE;

            if (queuedMove != Direction.NONE) {
                move(queuedMove, false);
                queuedMove = Direction.NONE;
            }
        }


        for (int p = 0; p < projectiles.size(); p++) {
            projectiles.get(p).update();
            if (projectiles.get(p).isDestroyed())
                projectiles.remove(p--);
        }

        //Movement and shooting end's player's turn
        if (projectiles.size() == 0 && isShooting) {
            endPlayersTurn();
            isShooting = false;
        }

        updateMovementTimer();
    }

    private void handleEndLevelCollision() {
        if (this instanceof Player)
            if (tileX == map.getEndX() && tileY == map.getEndY())
                map.nextMap();
    }

    public void render(SpriteBatch spriteBatch) {

        for (Projectile projectile : projectiles)
            projectile.render(spriteBatch, map.getPlayer().getActualX(), map.getPlayer().getActualY());

    }


    public void moveDown(boolean queuePriority) {
        move(Direction.DOWN, queuePriority);
    }

    public void moveUp(boolean queuePriority) {
        move(Direction.UP, queuePriority);
    }

    public void moveLeft(boolean queuePriority) {
        move(Direction.LEFT, queuePriority);
    }

    public void moveRight(boolean queuePriority) {
        move(Direction.RIGHT, queuePriority);
    }

    private void resetMovementTimer() {
        movementTimer = movementTimerMax;
    }
}
