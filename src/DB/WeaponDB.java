package DB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeaponDB {

    private static List<WeaponData> weapons = new ArrayList<WeaponData>();

    public static void load(String filePath) {
        try {
            List<String> lines = FileUtils.readLines(new File(filePath));

            for (String line : lines)
                weapons.add(new WeaponData(line.split(",")));

            print();
        } catch (IOException e) {
        }
    }

    public static WeaponData getWeapon(String name) {
        for (WeaponData weaponData : weapons)
            if (weaponData.getUniqueName().equals(name))
                return weaponData;
        return null;
    }

    public static WeaponData getWeapon(int id) {
        return weapons.get(id);
    }

    public static void print() {
        for (WeaponData weaponData : weapons)
            System.out.println(weaponData.toString());
    }
}
