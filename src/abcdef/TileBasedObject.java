package abcdef;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class TileBasedObject {

    protected Texture texture;

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    protected int tileX;
    protected int tileY;
    protected boolean solid;

    public TileBasedObject(int tileX, int tileY) {
        this.tileX = tileX;
        this.tileY = tileY;
    }

    public void render(SpriteBatch spriteBatch, int playerX, int playerY) {
        TileBasedObject.render(spriteBatch, texture, tileX, tileY, playerX, playerY);
    }

    public static void render(SpriteBatch spriteBatch, Texture texture, int tileX, int tileY, int playerX, int playerY) {
        spriteBatch.draw(texture, tileX * Map.getTileSize() - playerX + Gdx.graphics.getWidth()/2 - Map.getTileSize()/2, tileY * Map.getTileSize() - playerY + Gdx.graphics.getHeight()/2 - Map.getTileSize()/2);
    }

    public static void renderFree(SpriteBatch spriteBatch, Texture texture, double actualX, double actualY, int playerActualX, int playerActualY) {
        spriteBatch.draw(texture, (int)(actualX - playerActualX + Gdx.graphics.getWidth()/2 - Map.getTileSize()/2), (int)(actualY - playerActualY + Gdx.graphics.getHeight()/2 - Map.getTileSize()/2));
    }

    public static void render(SpriteBatch spriteBatch, Texture texture, int tileX, int tileY) {
        spriteBatch.draw(texture, tileX * Map.getTileSize() + Gdx.graphics.getWidth()/2 - Map.getTileSize()/2, tileY * Map.getTileSize() + Gdx.graphics.getHeight()/2 - Map.getTileSize()/2);
    }


    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public boolean isSolid() {
        return solid;
    }

    protected void create(Texture texture, boolean solid) {
        this.texture = texture;
        this.solid = solid;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
