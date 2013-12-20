package abcdef;

import DB.*;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;
import java.awt.Point;
import java.util.Random;

public class Engine implements ApplicationListener, InputProcessor {

    public static Random random = new Random();

    private Map map;
    private SpriteBatch spriteBatch;
    private Player player;
    private Input previousInput;
    private BitmapFont bitmapFont;

    //Hovered tile
    private int hoveredTileX;
    private int hoveredTileY;

    private boolean fireMode = false;

    private static GameState gameState = GameState.PLAY;
    private static MenuState menuState = MenuState.MAIN;

    private int selectedItem = 0;


    public enum GameState {
        PLAY,
        INVENTORY,
        MENU
    }

    public enum MenuState {
        MAIN,
        DEATH,
        WIN
    }

    @Override
    public void create() {
        Texture.setEnforcePotImages(false);
        WeaponDB.load("assets/weapondb.txt");
        ItemDB.load("assets/itemdb.txt");
        TileDB.load("assets/tiledb.txt");
        EnemyDB.load("assets/enemydb.txt");
        MapDB.load("assets/mapdb.txt");
        spriteBatch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        Gdx.input.setInputProcessor(this);

        resetMap();
    }

    private void resetMap() {
        map = new Map();
        map.load(MapDB.getMap(0));
        player = new Player(map);
    }

    @Override
    public void pause() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resume() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dispose() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resize(int i, int i2) {
    }

    private void renderGame() {
        InputController.preRenderUpdate();

        if (gameState == GameState.PLAY) {
            if (map.haveEnemiesFinishedTurn()) {
                if (InputController.isKey(Input.Keys.W))
                    player.moveUp(false);

                if (InputController.isKey(Input.Keys.S))
                    player.moveDown(false);

                if (InputController.isKey(Input.Keys.A))
                    player.moveLeft(false);

                if (InputController.isKey(Input.Keys.D))
                    player.moveRight(false);

                player.update();
            }

            updateHoveredTile();
        }
        map.update();

        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        map.render(spriteBatch,player.getActualX(), player.getActualY());
        player.render(spriteBatch);

        if (fireMode)
            drawLineOfSight();

        if (gameState == GameState.INVENTORY)
            spriteBatch.draw(Assets.gui.selectedItemCursor, Map.getTileSize(),selectedItem * Map.getTileSize());

        TileBasedObject.render(spriteBatch, Assets.gui.hoveredTile, hoveredTileX - player.getTileX(), hoveredTileY - player.getTileY());
        renderGUI(player);
        spriteBatch.end();
    }

    public static void switchMenu(MenuState menuState) {
        Engine.gameState = GameState.MENU;
        Engine.menuState = menuState;
    }

    private void renderMenu() {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();

        if (menuState == MenuState.MAIN)
            spriteBatch.draw(Assets.gui.mainScreen,0,0);
        else if (menuState == MenuState.DEATH)
            spriteBatch.draw(Assets.gui.deathScreen,0,0);
        else
            spriteBatch.draw(Assets.gui.winScreen,0,0);

        String credits = "Alex Spedding / @AlexDiru";
        BitmapFont.TextBounds bounds = bitmapFont.getBounds(credits);
        bitmapFont.draw(spriteBatch, credits, 8, bounds.height + 8);

        spriteBatch.end();
    }

    @Override
    public void render() {

        if (gameState == GameState.MENU)
            renderMenu();
        else
            renderGame();

    }

    private void renderGUI(Player character) {
        String str ="Health: " + character.getHealth() + "    Armour: " + character.getArmour() + "    Damage: " + character.getWeapon().getDamageString();// + "    Exp: " + character.getExperience();
        BitmapFont.TextBounds bounds = bitmapFont.getBounds(str);
        bitmapFont.draw(spriteBatch, str,Gdx.graphics.getWidth() - bounds.width - 8,bounds.height + 8);
    }

    private void drawLineOfSight() {
        List<Point> points = BresenhamsLineAlgorithm.getPoints(player.getTileX(), player.getTileY(), hoveredTileX, hoveredTileY);
        boolean shotAttemptedThroughWall = false;
        for (Point point : points)
            if (!map.isShootThroughable(point.x, point.y) || shotAttemptedThroughWall) {
                shotAttemptedThroughWall = true;
                TileBasedObject.render(spriteBatch, Assets.gui.noLineOfSightTile, point.x - player.getTileX(), point.y - player.getTileY());
            }
            else
                TileBasedObject.render(spriteBatch, Assets.gui.lineOfSightTile, point.x - player.getTileX(), point.y - player.getTileY());
    }

    private void updateHoveredTile() {
        //This code is fucking horrible
        //Sorts out the selected tile
        //I can't even begin to comment this - it works
        int adjustedX = Gdx.input.getX();// + abcdef.Map.getTileSize()/2;
        hoveredTileX =  (adjustedX + adjustedX % Map.getTileSize())/ Map.getTileSize();
        hoveredTileY = (Gdx.graphics.getHeight() - Gdx.input.getY() + Gdx.input.getY()% Map.getTileSize())/ Map.getTileSize() - 1;
        hoveredTileX -= Gdx.graphics.getWidth()/ Map.getTileSize()/2;
        hoveredTileY -= Gdx.graphics.getHeight()/ Map.getTileSize()/2;
        hoveredTileX += player.getTileX();
        hoveredTileY += player.getTileY();
    }

    @Override
    public boolean keyDown(int key) {

        if (gameState == GameState.PLAY) {
            if (key == Input.Keys.W)
                player.moveUp(true);

            if (key == Input.Keys.S)
                player.moveDown(true);

            if (key == Input.Keys.A)
                player.moveLeft(true);

            if (key == Input.Keys.D)
                player.moveRight(true);

            //if (key == Input.Keys.F)
            //    fireMode = !fireMode;

            if (key == Input.Keys.I)
                gameState = GameState.INVENTORY;

            if (key == Input.Keys.G)
                player.handleItemCollision();

        } else if (gameState == GameState.INVENTORY) {
            if (key == Input.Keys.I)
                gameState = GameState.PLAY;

            if (key == Input.Keys.W)
                selectedItem++;

            if (key == Input.Keys.S)
                selectedItem--;

            if (selectedItem > player.getItems().size() - 1)
                selectedItem = player.getItems().size() - 1;
            if (selectedItem < 0)
                selectedItem = 0;

            if (key == Input.Keys.U || key == Input.Keys.ENTER)
                player.useItem(selectedItem);

            if (key == Input.Keys.D || key == Input.Keys.BACKSPACE)
                player.dropItem(selectedItem);
        } else if (gameState == GameState.MENU) {
            if (key == Input.Keys.P) {
                if (menuState == MenuState.DEATH || menuState == MenuState.WIN)
                    resetMap();
                gameState = GameState.PLAY;
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int key) {

        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (fireMode && button == 0 || button == 1)
            player.shootAt(hoveredTileX, hoveredTileY);
        return false;
    }

    @Override
    public boolean touchUp(int i, int i2, int i3, int i4) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean scrolled(int i) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
