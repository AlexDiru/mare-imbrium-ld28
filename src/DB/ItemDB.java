package DB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class ItemDB {

    private static List<ItemData> items = new ArrayList<ItemData>();

    public static void load(String filePath) {
        try {
            List<String> lines = FileUtils.readLines(new File(filePath));

            for (String line : lines)
                items.add(new ItemData(line.split(",")));

            print();
        } catch (IOException e) {
        }
    }

    public static ItemData getItem(String name) {
        for (ItemData itemData : items)
            if (itemData.getUniqueName().equals(name))
                return itemData;
        return null;
    }

    public static ItemData getItem(int id) {
        return items.get(id);
    }

    public static void print() {
        for (ItemData itemData : items)
            System.out.println(itemData.toString());
    }

    public static class ItemData {
        private String uniqueName;
        private String name;
        private int healthBonus;
        private int armourBonus;
        private int ammoBonus;
        private int weaponIndex;
        private Texture texture;
        private boolean isWeapon;
        private int uniqueID;
        private boolean usuable;

        public String toString() {
            if (weaponIndex == -1)
                return name + " Health: " + healthBonus + " Armour: " + armourBonus + " Ammo: " + ammoBonus + " WeaponIndex: " + weaponIndex;
            else
                return name + WeaponDB.getWeapon(weaponIndex).toString();
        }

        public ItemData(String[] cells) {
            this(cells[0], cells[1], Integer.parseInt(cells[2]), Integer.parseInt(cells[3]), Integer.parseInt(cells[4]), Integer.parseInt(cells[5]), Boolean.parseBoolean(cells[6]),Boolean.parseBoolean(cells[7]), cells[8]);
        }

        public ItemData(String uniqueName, String name, int healthBonus, int armourBonus, int ammoBonus, int weaponIndex, boolean isWeapon, boolean usuable, String filePath) {
            this.uniqueName = uniqueName;
            this.name = name;
            this.healthBonus = healthBonus;
            this.armourBonus = armourBonus;
            this.ammoBonus = ammoBonus;
            this.weaponIndex = weaponIndex;
            this.isWeapon = isWeapon;
            this.usuable = usuable;
            this.texture = new Texture(Gdx.files.internal(filePath));
        }

        public String getUniqueName() {
            return uniqueName;
        }

        public Texture getTexture() {
            return texture;
        }

        public int getHealthBonus() {
            return healthBonus;
        }

        public int getArmourBonus() {
            return armourBonus;
        }

        public boolean isWeapon() {
            return isWeapon;
        }

        public int getWeaponIndex() {
            return weaponIndex;
        }

        public int getUniqueID() {
            return uniqueID;
        }

        public boolean isUsuable() {
            return usuable;
        }
    }
}
