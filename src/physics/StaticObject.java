package physics;

public class StaticObject extends PhysicsObject {

    public StaticObject(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public StaticObject(Vector position, Vector size) {
        super(position, size);
    }

    public StaticObject(double width, double height) {
        super(width, height);
    }
}
