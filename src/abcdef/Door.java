package abcdef;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 13/12/13
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 */
public class Door extends TileBasedObject {
    public boolean isClosed() {
        return closed;
    }

    public enum DoorType {
        REGULAR,
        BLUE,
        RED,
        GREEN
    }

    private DoorType type;
    private boolean closed;

    public Door(int x, int y, DoorType type) {
        super(x,y);
        this.type = type;
        closed = true;

        if (type == DoorType.REGULAR)
            create(Assets.doors.regularDoorTexture);
        else if (type == DoorType.BLUE)
            create(Assets.doors.blueDoorTexture);
        else if (type == DoorType.GREEN)
            create(Assets.doors.greenDoorTexture);
        else if (type == DoorType.RED)
            create(Assets.doors.redDoorTexture);
    }

    public void open(Player player) {
        if (type == DoorType.RED && !player.hasRedKey())
            return;
        solid = false;
        closed = false;
    }

    private void create(Texture texture) {
        super.create(texture, true);
    }

    @Override
    public void render(SpriteBatch spriteBatch, int playerX, int playerY) {
        if (closed)
            super.render(spriteBatch, playerX, playerY);
    }

}
