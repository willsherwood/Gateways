package physics.gravity;

public class Gravity {

    public static final float DEFAULT_MAGNITUDE = 0.6f;
    public static final float DEFAULT_DIRECTION = (float) (3 * Math.PI / 2);

    private float amount;
    private float direction;

    public Gravity(float amount, float direction) {
        this.amount = amount;
        this.direction = direction;
    }

    public float getXComponent() {
        return (float) (amount * Math.cos(direction));
    }

    public float getYComponent() {
        return (float) (amount * Math.sin(direction));
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }
}
