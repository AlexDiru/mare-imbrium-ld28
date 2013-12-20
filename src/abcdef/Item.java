package abcdef;

import DB.ItemDB;
import DB.WeaponDB;
import DB.WeaponData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 11/12/13
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
public class Item extends TileBasedObject {

    private ItemDB.ItemData itemData;
    private boolean isEquipped = false;

    public void renderOnGUI(SpriteBatch spriteBatch, int x, int y) {
        spriteBatch.draw(texture, x, y);
    }

    public ItemDB.ItemData getData() {
        return itemData;
    }

    private Item(int x, int y) {
        super(x,y);
    }

    public Item(int x, int y, int uniqueID) {
        this(x, y);
        itemData = ItemDB.getItem(uniqueID);
        texture = itemData.getTexture();
    }

    public Item(int x, int y, String uniqueName) {
        this(x, y);
        itemData = ItemDB.getItem(uniqueName);
        texture = itemData.getTexture();
    }

    public WeaponData toWeapon() {
        return WeaponDB.getWeapon(itemData.getWeaponIndex());
    }

    /**
     * For inserting an item directly into inventory - doesn't need to be picked up
     * @param data
     */
    public static Item fromData(ItemDB.ItemData data) {
        Item item = new Item(0,0);
        item.itemData = data;
        item.texture = data.getTexture();
        return item;
    }

    public boolean isEquipped() {
        return isEquipped;
    }

    public void unequip() {
        isEquipped = false;
    }

    public void equip() {
        isEquipped = true;
    }
}
