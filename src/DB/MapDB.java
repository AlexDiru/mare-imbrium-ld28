package DB;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapDB {
    private static List<String> maps = new ArrayList<String>();

    public static void load(String filePath) {
        try {
            List<String> lines = FileUtils.readLines(new File(filePath));

            for (String line : lines)
                maps.add(line);

        } catch (IOException e) {
        }
    }

    public static String getMap(int id) {
        return maps.get(id);
    }

    public static int numberOfMaps() {
        return maps.size();
    }
}
