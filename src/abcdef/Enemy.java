package abcdef;

import DB.EnemyDB;
import DB.EnemyData;
import DB.WeaponDB;
import DB.WeaponData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.lang.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 13/12/13
 * Time: 20:33
 * To change this template use File | Settings | File Templates.
 */
public class Enemy extends Character {

    private TileBasedObject tileBasedObject;
    private boolean finishedTurn = true;
    private WeaponData weapon;
    private EnemyData enemyData;
    private int numberOfTurnsHasHadLineOfSight = 0;

    public Enemy(Map map, int x, int y, int enemyDBIndex) {
        super(map);
        tileBasedObject = new TileBasedObject(x, y);
        tileX = x;
        tileY = y;
        enemyData = EnemyDB.getEnemy(enemyDBIndex);
        tileBasedObject.setTexture(enemyData.getTexture());
        weapon = WeaponDB.getWeapon(enemyData.getWeaponIndex());
        health = enemyData.getHealth();
    }

    @Override
    protected WeaponData getWeapon() {
        return weapon;
    }

    public void takeTurn() {
        finishedTurn = false;

        if (hasLineOfSight(map.getPlayer()) || getWeapon().canDestroyMap()) {
            numberOfTurnsHasHadLineOfSight++;
            if (numberOfTurnsHasHadLineOfSight > 1)
                shootAt(map.getPlayer().getTileX(), map.getPlayer().getTileY());
            return;
        } else
            numberOfTurnsHasHadLineOfSight = 0;

        Character.Direction direction = Character.Direction.values()[(int)(Math.random() * Direction.values().length - 1)];
        move(direction, false);
        finishedTurn = true;
    }

    public void update() {
        super.update();

        if (!finishedTurn && !isShooting)
            finishedTurn = true;
    }

    public void render(SpriteBatch spriteBatch, int playerX, int playerY) {
        tileBasedObject.tileX = tileX;
        tileBasedObject.tileY = tileY;
        tileBasedObject.render(spriteBatch, playerX, playerY);
        super.render(spriteBatch);
    }

    public boolean finishedTurn() {
        return finishedTurn;
    }

    public EnemyData getData() {
        return enemyData;
    }
}