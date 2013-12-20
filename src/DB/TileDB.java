package DB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TileDB {
    private static List<TileData> tiles = new ArrayList<TileData>();

    public static void load(String filePath) {
        try {
            List<String> lines = FileUtils.readLines(new File(filePath));

            for (String line : lines)
                tiles.add(new TileData(line.split(",")));

            print();
        } catch (IOException e) {
        }
    }

    public static TileData getTile(String name) {
        for (TileData tileData : tiles)
            if (tileData.getUniqueName().equals(name))
                return tileData;
        return null;
    }

    public static TileData getTile(int id) {
        return tiles.get(id);
    }

    public static void print() {
        for (TileData tileData : tiles)
            System.out.println(tileData.toString());
    }

    public static int numberOfTiles() {
        return tiles.size();
    }

    public static class TileData {
        private String uniqueName;
        private boolean solid;
        private Texture texture;
        private int damageOnTouch;

        public String toString() {
            return uniqueName + " Solid: " + solid;
        }

        public TileData(String[] cells) {
            this(cells[0], Boolean.parseBoolean(cells[1]),Integer.parseInt(cells[2]), cells[3]);
        }

        public TileData(String uniqueName,boolean solid, int damageOnTouch, String filePath) {
            this.uniqueName = uniqueName;
            this.solid = solid;
            this.damageOnTouch = damageOnTouch;
            this.texture = new Texture(Gdx.files.internal(filePath));
        }

        public String getUniqueName() {
            return uniqueName;
        }

        public Texture getTexture() {
            return texture;
        }

        public boolean isSolid() {
            return solid;
        }

        public int getDamageOnTouch() {
            return damageOnTouch;
        }
    }
}
