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
public class Assets {
    public static TileAssets tiles;
    public static ItemAssets items;
    public static GUIAssets gui;
    public static DoorAssets doors;

    public static Texture projectileTexture = new Texture(Gdx.files.internal("assets/weapons/projectile.png"));
}
