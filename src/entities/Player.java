package entities;

import physics.movement.Gravity;
import physics.movement.Velocity;

import java.util.BitSet;

public class Player {

    private Gravity gravity;
    private Velocity velocity;

    public Player() {
        gravity = Gravity.defaultGravity();
        velocity = new Velocity(0, 0, 7, 9);
    }

    public void step(BitSet b) {

    }
}
