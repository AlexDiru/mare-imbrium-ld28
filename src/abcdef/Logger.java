package abcdef;

import java.lang.*;

public class Logger {
    public static void shot(Character shooter, Character recipient) {
        log(shooter.getName() + " shot " + recipient.getName() + " with " + shooter.getWeapon().getName());
    }

    public static void shot(Character shooter, Tile recipient) {
        log(shooter.getName() + " shot " +  (recipient.isSolid() ? "the wall" : "nothing"));
    }

    public static void pickup(Character character,Item item) {
        log(character.getName() + " picked up " + item.getData().toString());
    }

    private static void log(String message) {
        System.out.println(message);
    }

    public static void health(Character character) {
        log(character.getName() + "'s health is " + character.getHealth());
    }
}
