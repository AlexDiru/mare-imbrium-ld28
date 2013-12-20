package abcdef;

import com.badlogic.gdx.Input;

import java.util.Arrays;
import java.util.List;

public class InputController {

    private static List<KeyPressTimer> timers = Arrays.asList(new KeyPressTimer[] { new KeyPressTimer(Input.Keys.W),
            new KeyPressTimer(Input.Keys.A),new KeyPressTimer(Input.Keys.S),new KeyPressTimer(Input.Keys.D)});

    public static void preRenderUpdate() {
        for (KeyPressTimer timer : timers)
            timer.update();
    }

    public static boolean isKey(int key) {
        for (KeyPressTimer timer : timers)
            if (timer.getKey() == key) {
                return timer.isHeld();
            }
        return false;
    }
}
