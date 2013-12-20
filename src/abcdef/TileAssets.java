package abcdef;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 11/12/13
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public class TileAssets {
    public static final Texture floorTexture = new Texture(Gdx.files.internal("assets/tile.png"));
    public static final Texture wallTexture = new Texture(Gdx.files.internal("assets/wall.png"));
    public static final Texture enemyTexture = new Texture(Gdx.files.internal("assets/enemy.png"));
}
