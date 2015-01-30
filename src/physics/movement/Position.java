package physics.movement;

public class Position {

    private int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    /**
     * sets the X value of the position
     * @param x delta X
     * @param relative whether or not to add X to current position or set it absolute
     */
    public void dx(int x, boolean relative) {
        this.x = x + (relative ? this.x : 0);
    }

    public int y() {
        return y;
    }

    /**
     * sets the Y value of the position
     * @param y delta Y
     * @param relative whether or not to add Y to current position or set it absolute
     */
    public void dy(int y, boolean relative) {
        this.y = y + (relative ? this.y : 0);

    }
}
