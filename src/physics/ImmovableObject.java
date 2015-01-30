package physics;

import java.awt.*;

public class ImmovableObject {

    private Rectangle bounds;

    public ImmovableObject(int x, int y, int width, int height) {
        bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getRect() {
        return (Rectangle) bounds.clone();
    }
}
