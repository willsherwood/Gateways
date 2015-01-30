package physics.movement;

/**
 * represents a game object's velocities
 */
public class Velocity {

    private float hspeed, vspeed;
    private float maxHspeed, maxVspeed;

    public float hspeed() {
        return hspeed;
    }

    public float vspeed() {
        return vspeed;
    }

    public void setHspeed(float hspeed) {
        if (hspeed >= -maxHspeed && hspeed <= maxHspeed)
            this.hspeed = hspeed;
    }

    public void setVspeed(float vspeed) {
        if (vspeed >= -maxVspeed && vspeed <= maxVspeed)
            this.vspeed = vspeed;
    }

    public Velocity(float hspeed, float vspeed, float maxHspeed, float maxVspeed) {
        this.hspeed = hspeed;
        this.vspeed = vspeed;
        this.maxHspeed = maxHspeed;
        this.maxVspeed = maxVspeed;
    }
}
