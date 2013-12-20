package DB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class EnemyData {

    private String uniqueName;
    private String name;
    private int weaponIndex;
    private int experience;
    private Texture texture;
    private int health;
    private boolean canBeHurtByOtherEnemies;

    public EnemyData(String uniqueName, String name, int weaponIndex, int health, int experience, boolean canBeHurtByOtherEnemies, String filePath) {
        this.uniqueName = uniqueName;
        this.name = name;
        this.weaponIndex = weaponIndex;
        this.health = health;
        this.experience = experience;
        this.canBeHurtByOtherEnemies = canBeHurtByOtherEnemies;
        texture = new Texture(Gdx.files.internal(filePath));
    }

    public EnemyData(String[] cells) {
        this(cells[0], cells[1], Integer.parseInt(cells[2]), Integer.parseInt(cells[3]),Integer.parseInt(cells[4]),Boolean.parseBoolean(cells[5]), cells[6]);
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getWeaponIndex() {
        return weaponIndex;
    }

    public int getExperience() {
        return experience;
    }

    public int getHealth() {
        return health;
    }

    public boolean canBeHurtByOtherEnemies() {
        return canBeHurtByOtherEnemies;
    }
}
