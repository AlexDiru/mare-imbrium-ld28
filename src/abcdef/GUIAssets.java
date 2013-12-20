package abcdef;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class GUIAssets {
    public static final Texture hoveredTile = new Texture(Gdx.files.internal("assets/hoveredtile.png"));
    public static final Texture lineOfSightTile = new Texture(Gdx.files.internal("assets/lineofsight.png"));
    public static final Texture noLineOfSightTile = new Texture(Gdx.files.internal("assets/nolineofsight.png"));
    public static final Texture selectedItemCursor = new Texture(Gdx.files.internal("assets/selecteditem.png"));

    public static final Texture mainScreen = new Texture(Gdx.files.internal("assets/menus/frontScreen.png"));
    public static final Texture deathScreen = new Texture(Gdx.files.internal("assets/menus/deathScreen.png"));
    public static final Texture winScreen = new Texture(Gdx.files.internal("assets/menus/winScreen.png"));
}
