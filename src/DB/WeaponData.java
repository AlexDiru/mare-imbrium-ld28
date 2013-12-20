package DB;

import abcdef.Engine;

public class WeaponData {
    private String name;
    private int baseDamage;
    private int randomDamage;
    private int magazineSize;
    private int maxAmmo;
    private int ammoPerShot;
    private boolean canDestroyMap;
    private int numberOfProjectiles;
    private double angleBetweenProjectiles;

    public WeaponData(String[] cells) {
        this(cells[0], Integer.parseInt(cells[1]), Integer.parseInt(cells[2]), Integer.parseInt(cells[3]), Integer.parseInt(cells[4]), Integer.parseInt(cells[5]), Boolean.parseBoolean(cells[6]), Integer.parseInt(cells[7]), Double.parseDouble(cells[8]));
    }

    public WeaponData(String name, int baseDamage, int randomDamage, int magazineSize, int maxAmmo, int ammoPerShot, boolean canDestroyMap, int numberOfProjectiles, double angleBetweenProjectiles) {
        this.name = name;
        this.baseDamage = baseDamage;
        this.randomDamage = randomDamage;
        this.magazineSize = magazineSize;
        this.maxAmmo = maxAmmo;
        this.ammoPerShot = ammoPerShot;
        this.canDestroyMap = canDestroyMap;
        this.numberOfProjectiles = numberOfProjectiles;
        this.angleBetweenProjectiles = angleBetweenProjectiles;
    }

    public String getUniqueName() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int calculateDamage() {
        return baseDamage + Engine.random.nextInt(randomDamage) + 1;
    }

    public String getDamageString() {
        return baseDamage + "d" + randomDamage;
    }

    public boolean canDestroyMap() {
        return canDestroyMap;
    }

    public int getNumberOfProjectiles() {
        return numberOfProjectiles;
    }

    public double getAngleBetweenProjectiles() {
        return angleBetweenProjectiles;
    }
}