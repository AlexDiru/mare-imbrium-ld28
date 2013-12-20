package abcdef;

import DB.TileDB;

public class Tile extends TileBasedObject {

    private TileDB.TileData tileData;

    public Tile(int x, int y, int uniqueId) {
        super(x,y);
        tileData = TileDB.getTile(uniqueId);
        texture = tileData.getTexture();
        solid = tileData.isSolid();
    }

    public void destroy(Map map) {
        solid = false;
        texture = map.getUndergroundTexture();
    }

    public TileDB.TileData getTileData() {
        return tileData;
    }
}