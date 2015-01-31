package physics;

/**
 * An object that interacts with the world.
 */
public class PhysicsObject {
    protected Vector position;
    protected Vector size;

    public PhysicsObject(double x, double y, double width, double height) {
        position = new Vector();
        position.x = x;
        position.y = y;
        size = new Vector();
        size.x = width;
        size.y = height;
    }

    public PhysicsObject(Vector position, Vector size) {
        this(position.x, position.y, size.x, size.y);
    }

    public PhysicsObject(double width, double height) {
        this(0, 0, width, height);
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getSize() {
        return size;
    }

    public double getWidth() {
        return size.x;
    }

    public double getHeight() {
        return size.y;
    }

    // unsure on method signature
    public void step() {
    }
}
