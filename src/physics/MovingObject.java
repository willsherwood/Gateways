package physics;

public class MovingObject extends PhysicsObject {
    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    protected Vector velocity;

    public MovingObject(double x, double y, double width, double height) {
        super(x, y, width, height);
        velocity = new Vector();
    }

    public MovingObject(Vector position, Vector size) {
        super(position, size);
        velocity = new Vector();
    }

    public MovingObject(double width, double height) {
        super(width, height);
        velocity = new Vector();
    }
}
