package DB;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnemyDB {
    private static List<EnemyData> enemies = new ArrayList<EnemyData>();

    public static void load(String filePath) {
        try {
            List<String> lines = FileUtils.readLines(new File(filePath));

            for (String line : lines)
                enemies.add(new EnemyData(line.split(",")));

            print();
        } catch (IOException e) {
        }
    }

    public static EnemyData getEnemy(String name) {
        for (EnemyData enemyData : enemies)
            if (enemyData.getUniqueName().equals(name))
                return enemyData;
        return null;
    }

    public static EnemyData getEnemy(int id) {
        return enemies.get(id);
    }

    public static void print() {
        for (EnemyData enemyData : enemies)
            System.out.println(enemyData.toString());
    }

}
