package physics.movement;

public class Gravity {

    public static final float DEFAULT_MAGNITUDE = 0.6f;
    public static final float DEFAULT_DIRECTION = (float) (3 * Math.PI / 2);

    private float magnitude;
    private float direction;

    public Gravity(float magnitude, float direction) {
        this.magnitude = magnitude;
        this.direction = direction;
    }

    public float getXComponent() {
        return (float) (magnitude * Math.cos(direction));
    }

    public float getYComponent() {
        return (float) (magnitude * Math.sin(direction));
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    /**
     * @return a gravity object with direction down and magnitude 0.6 p/s^2
     */
    public static final Gravity defaultGravity() {
        return DEFAULT_GRAVITY;
    }

    private static final Gravity DEFAULT_GRAVITY = new Gravity(DEFAULT_MAGNITUDE, DEFAULT_DIRECTION);
}
