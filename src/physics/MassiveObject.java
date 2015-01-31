package physics;

// Massive objects have mass.
// Assume all massive objects have gravity
public class MassiveObject extends MovingObject {

    public static final double GRAVITY = 9.8 * Units.MPS2;

    protected double mass;

    public MassiveObject(double x, double y, double width, double height, double mass) {
        super(x, y, width, height);
        this.mass = mass;
    }

    public MassiveObject(Vector position, Vector size, double mass) {
        super(position, size);
        this.mass = mass;
    }

    public MassiveObject(double width, double height, double mass) {
        super(width, height);
        this.mass = mass;
    }

    @Override
    public void step() {
        applyAcceleration(new Vector(0, GRAVITY));
    }

    public double getMass() {
        return mass;
    }

    public void applyForce(Vector force) {
        // F = ma
        applyAcceleration(force.over(mass));
    }

    public void applyAcceleration(Vector a) {
        velocity = velocity.plus(a);
    }
}
