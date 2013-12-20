package abcdef;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.Point;
import java.lang.*;
import java.util.List;

public class Projectile {

    private double speed = 1000;

    private double actualX;
    private double actualY;
    private double angle;
    private Character sender;
    private List<Point> unvisitedPoints;
    private boolean destroyed = false;

    public Projectile(int startTileX, int startTileY, int targetTileX, int targetTileY, Character sender, List<Point> bresenhamPoints) {
        this(startTileX, startTileY,  Math.atan2(targetTileY - startTileY, targetTileX - startTileX), sender, bresenhamPoints);
    }

    public Projectile(int startTileX, int startTileY, double angle, Character sender, List<Point> bresenhamPoints) {
        this.sender = sender;
        unvisitedPoints = bresenhamPoints;
        actualX = startTileX * Map.getTileSize() + Map.getTileSize()/2;
        actualY = startTileY * Map.getTileSize() + Map.getTileSize()/2;
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public void update() {

        double verticalMovement = Math.sin(angle) * speed * Gdx.graphics.getDeltaTime();
        double horizontalMovement = Math.cos(angle) * speed * Gdx.graphics.getDeltaTime();
        actualX += horizontalMovement;
        actualY += verticalMovement;

        //Check for tile collision
        System.out.println(actualX % Map.getTileSize() + " ," + actualY % Map.getTileSize() );
        int allowedError = Map.getTileSize();
        int lowerBound = Map.getTileSize()/2 - allowedError/2;
        int upperBound = Map.getTileSize()/2 + allowedError/2;
        //if (actualX % Map.getTileSize() > allowedError && actualY % Map.getTileSize() > allowedError)
        //    return;
        int xGap = (int)actualX % Map.getTileSize();
        int yGap = (int)actualY % Map.getTileSize();

        if (xGap < lowerBound || xGap > upperBound)
            if (yGap < lowerBound || yGap > upperBound)
                return;


        int tileX = (int)((actualX - actualX % Map.getTileSize())/Map.getTileSize());
        int tileY = (int)((actualY - actualY % Map.getTileSize())/Map.getTileSize());

       /* if (unvisitedPoints.size() > 0) {
            if (unvisitedPoints.get(0).x == tileX && unvisitedPoints.get(0).getY() == tileY) {
                destroyed = sender.getMap().handleBulletCollision(sender, tileX, tileY);
                unvisitedPoints.remove(0);
            }
        } else {*/
            destroyed = sender.getMap().handleBulletCollision(sender, tileX, tileY);
        //}
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void render(SpriteBatch spriteBatch, int playerActualX, int playerActualY) {
        if (!destroyed)
            TileBasedObject.renderFree(spriteBatch, Assets.projectileTexture, actualX - Map.getTileSize()/2, actualY - Map.getTileSize()/2, playerActualX, playerActualY);
    }
}
