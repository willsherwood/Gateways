package physics;

/**
 * Axis-aligned bounding box.
 */
public class AxisAlignedBoundingBox {
    public double x1, y1, x2, y2;

    public AxisAlignedBoundingBox() {
        this(0, 0, 0, 0);
    }

    public AxisAlignedBoundingBox(double x1, double y1, double x2, double y2) {
        set(x1, y1, x2, y2);
    }

    public AxisAlignedBoundingBox(Vector topLeft, Vector botRight) {
        this(topLeft.x, topLeft.y, botRight.x, botRight.y);
    }

    public void set(double x1, double y1, double x2, double y2) {
        this.x1 = Math.min(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.x2 = Math.max(x1, x2);
        this.y2 = Math.max(y1, y2);
    }

    public void set(Vector topLeft, Vector botRight) {
        set(topLeft.x, topLeft.y, botRight.x, botRight.y);
    }

    // TODO: test this
    public boolean intersects(AxisAlignedBoundingBox other) {
        return !(
            other.x1 > this.x2
         || other.x2 < this.x1
         || other.y1 > this.y2
         || other.y2 < this.y1
            );
    }

    public Vector getTopLeftVector() {
        return new Vector(x1, y1);
    }

    public Vector getBottomRightVector() {
        return new Vector(x2, y2);
    }

    // TODO: intersect moving using time
    // velocity is relative to this (i.e. velocity of this AABB = 0)
    // public boolean intersects(AABB other, Vector velocity)
}
