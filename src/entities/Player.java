package entities;

import physics.MassiveObject;
import physics.Unit;

public class Player extends MassiveObject {
    // TODO: real values
    public static final double PLAYER_WIDTH = 16;
    public static final double PLAYER_HEIGHT = 24;
    // average mass of a human
    public static final double PLAYER_MASS = 74.7 * Unit.KILOGRAM;

    public Player() {
        super(PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_MASS);
    }

    public Player(double x, double y) {
        super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_MASS);
    }
}
