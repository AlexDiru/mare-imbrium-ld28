package abcdef;

import com.badlogic.gdx.Gdx;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 13/12/13
 * Time: 19:52
 * To change this template use File | Settings | File Templates.
 */
public class KeyPressTimer {

    private static final float threshold = 0.8f;
    private float timer = 0.12f;
    private int key;

    public KeyPressTimer(int key) {
        this.key = key;
    }

    public void increaseTime() {
        timer += Gdx.graphics.getDeltaTime();
    }

    public void resetTime() {
        timer = 0;
    }

    public void update() {
        if (Gdx.input.isKeyPressed(key))
            increaseTime();
        else
            resetTime();
    }

    public boolean isHeld() {
       return timer > threshold;
    }

    public int getKey() {
        return key;
    }
}
