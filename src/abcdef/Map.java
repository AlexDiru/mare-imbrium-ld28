package abcdef;

import DB.MapDB;
import DB.TileDB;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;

public class Map {

    private static int tileSize = 32;

    private List<Tile> tiles;
    private List<Item> items;
    private List<Door> doors;
    private List<Enemy> enemies;
    private Player player;
    private Texture undergroundTexture;
    private int startPositionX;
    private int startPositionY;
    private int endPositionX;
    private int endPositionY;
    private int currentMap = 0;
    private int height;
    private int width;

    public Map() {
    }


    public void nextMap() {
        switchMap(++currentMap);
    }

    private void switchMap(int index) {
        if (index >= MapDB.numberOfMaps()) {
            Engine.switchMenu(Engine.MenuState.WIN);
            return;
        }

        load(MapDB.getMap(index));
        setPlayer(player);
    }

    private void clear() {
        tiles = new ArrayList<Tile>();
        items = new ArrayList<Item>();
        doors = new ArrayList<Door>();
        enemies = new ArrayList<Enemy>();
        height = 0;
        width = 0;
    }

    public void load(String filePath) {
        clear();

        try {
            List<String> lines = FileUtils.readLines(new File(filePath));

            int y = 0;
            int x = 0;

            int mode = 0; //0 = tiles, 1 = items, 2 = doors, 3 = enemies, 4 = weapons

            for (String line : lines) {
                if (line.contains("items")) {
                    x = y = 0;
                    mode = 1;
                    continue;
                } else if (line.contains("doors")) {
                    x = y = 0;
                    mode = 2;
                    continue;
                } else if (line.contains("enemies")) {
                    x = y = 0;
                    mode = 3;
                    continue;
                } else if (line.contains("weapons")) {
                    x = y = 0;
                    mode = 4;
                    continue;
                } else if (line.contains("start")) {
                    String[] split = line.split(":")[1].split(",");
                    startPositionX = Integer.parseInt(split[0]);
                    startPositionY = Integer.parseInt(split[1]);
                    continue;
                } else if (line.contains("end")) {
                    String[] split = line.split(":")[1].split(",");
                    endPositionX = Integer.parseInt(split[0]);
                    endPositionY = Integer.parseInt(split[1]);
                    continue;
                } else if (line.contains("underground")) {
                    String[] split = line.split(":");
                    undergroundTexture = TileDB.getTile(split[1]).getTexture();
                    continue;
                }
                String[] split = line.split(",");
                for (String data : split) {
                    if (mode == 0)
                        tiles.add(new Tile(x,y,Integer.parseInt(data)));
                    else if (mode == 1 && Integer.parseInt(data) != 0)
                        items.add(new Item(x,y, Integer.parseInt(data) - 1));
                    else if (mode == 2 && Integer.parseInt(data) != 0)
                        doors.add(new Door(x,y,Door.DoorType.values()[Integer.parseInt(data) - 1]));
                    else if (mode == 3 && Integer.parseInt(data) != 0)
                        enemies.add(new Enemy(this,x,y, Integer.parseInt(data)-1));

                    if (x > width)
                        width = x;

                    x++;
                }
                x = 0;
                if (y > height)
                    height = y;
                y++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(SpriteBatch spriteBatch, int playerX, int playerY) {
        for (Tile tile : tiles)
            tile.render(spriteBatch, playerX, playerY);

        for (Item item : items)
            item.render(spriteBatch, playerX, playerY);

        for (Door door : doors)
            door.render(spriteBatch, playerX, playerY);
        for (Enemy enemy : enemies)
            enemy.render(spriteBatch, playerX, playerY);
    }

    public static int getTileSize() {
        return tileSize;
    }

    public Tile getTile(int tileX, int tileY) {
        for (Tile tile : tiles)
            if (tile.getTileX() == tileX)
                if (tile.getTileY() == tileY)
                    return tile;
        return null;
    }

    public Item getItem(int tileX, int tileY) {
        for (Item item : items)
            if (item.getTileX() == tileX)
                if (item.getTileY() == tileY)
                    return item;
        return null;
    }

    public boolean haveEnemiesFinishedTurn() {
        for (Enemy enemy : enemies)
            if (!enemy.finishedTurn())
                return false;

        return true;
    }

    public boolean isWalkable(int tempX, int tempY) {
        Tile tile = getTile(tempX, tempY);

        if (tile != null)
            if (tile.isSolid())
                return false;

        Item item = getItem(tempX, tempY);

        if (item != null)
            if (item.isSolid())
                return false;

        Door door = getDoor(tempX, tempY);

        if (door != null)
            if (door.isClosed())
                return false;

        return true;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Door getDoor(int tileX, int tileY) {
        for (Door door : doors)
            if (door.getTileX() == tileX)
                if (door.getTileY() == tileY)
                    return door;
        return null;
    }

    public void update() {
        for (int e = 0; e < enemies.size(); e++)
            if (enemies.get(e).isDead())
                enemies.remove(e--);
            else
                enemies.get(e).update();
    }

    public void takeTurn() {
        for (Enemy enemy : enemies)
            enemy.takeTurn();
    }

    public boolean isShootThroughable(int x, int y) {
        return isWalkable(x,y);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        player.setPosition(startPositionX,startPositionY);
    }

    public Enemy getEnemy(int tileX, int tileY) {
        for (Enemy enemy : enemies)
            if (enemy.getTileY() == tileY)
                if (enemy.getTileX() == tileX)
                    return enemy;
        return null;
    }

    private void destroyAt(int x, int y) {
        Tile tile = getTile(x,y);
        if (tile != null)
            if (tile.isSolid())
                tile.destroy(this);
    }

    public boolean handleBulletCollision(Character shooter, int x, int y) {

        System.out.println(width + " " + height);
        if (x == 0 || y == 0 || x >= width || y >= height)
            return true;

        Enemy enemy = getEnemy(x,y);

        if (enemy != null && enemy != shooter) {
            shooter.shoot(enemy);
            return true;
        }

        if (player.getTileX() == x && player.getTileY() == y && shooter != player) {
            shooter.shoot(player);
            return true;
        }

        if (!isShootThroughable(x,y)) {
            if (shooter.getWeapon().canDestroyMap())
                destroyAt(x,y);
            return true;
        }

        return false;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public int getEndX() {
        return endPositionX;
    }

    public int getEndY() {
        return endPositionY;
    }

    public Texture getUndergroundTexture() {
        return undergroundTexture;
    }
}
