package abcdef;

import DB.ItemDB;
import DB.WeaponData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Player extends Character {


    private static Texture texture = new Texture(Gdx.files.internal("assets/player.png"));
    private List<Item> items;

    private WeaponData currentWeapon;
    private Item currentWeaponItem;
    private int experience = 0;
    private int maxItemsInInventory = 10;

    public void increaseExperience(int experience) {
        System.out.println("exp " + experience);
        this.experience += experience;
    }

    public Player(Map map) {
        super(map);
        map.setPlayer(this);

        items = new ArrayList<Item>();

        items.add(Item.fromData(ItemDB.getItem("PISTOL")));
        switchWeapon(items.get(0));

        health = 100;
    }

    public void switchWeapon(Item item) {
        //Unequip other weapons
        if (currentWeaponItem != null)
            currentWeaponItem.unequip();
        currentWeaponItem = item;
        currentWeaponItem.equip();
        currentWeapon = item.toWeapon();
    }

    protected void handleItemCollision() {
        Item item = map.getItem(tileX,tileY);

        if (item != null && items.size() < maxItemsInInventory) {
            map.removeItem(item);
            pickup(item);
        }

    }

    public WeaponData getWeapon() {
        return currentWeapon;
    }


    private void pickup(Item item) {
        Logger.pickup(this, item);
        items.add(item);
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, Gdx.graphics.getWidth()/2 - Map.getTileSize()/2,Gdx.graphics.getHeight()/2 - Map.getTileSize()/2);

        super.render(spriteBatch);

        for (int i = 0; i < items.size(); i++) {
            items.get(i).renderOnGUI(spriteBatch, 0,i*Map.getTileSize());
        }
    }

    public void dropItem(int selectedItem) {
        //Check floor is empty
        if (map.getItem(tileX, tileY) != null)
            return;

        Item item = null;
        try {
            item = items.get(selectedItem);
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        //Can't drop equipped weapon
        if (item.isEquipped())
            return;

        item.setTileX(tileX);
        item.setTileY(tileY);
        map.addItem(item);
        items.remove(item);
    }

    public void useItem(int selectedItem) {
        Item item = null;
        try {
            item = items.get(selectedItem);
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        if (!item.getData().isUsuable())
            return;

        health += item.getData().getHealthBonus();
        armour += item.getData().getArmourBonus();

        //Switch to weapon
        if (item.getData().isWeapon()) {
            switchWeapon(item);
        } else
            //DON'T CONSUME WEAPONS
            items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public int getExperience() {
        return experience;
    }

    public void setPosition(int startPositionX, int startPositionY) {
        tileX = startPositionX;
        tileY = startPositionY;
    }

    public boolean hasRedKey() {
        for (Item item : items)
            if (item.getData().getUniqueName().equals("REDKEY"))
                return true;
        return false;
    }
}
