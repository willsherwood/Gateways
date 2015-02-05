package physics;

import map.QuadTree;
import physics.controller.Collision;
import sherwood.gameScreen.GameScreen;
import util.UnorderedPair;

import java.util.*;

public class PhysicsController {
    public static final double ELASTICITY = 0.9;
    protected HashSet<PhysicsObject> objects;

    public PhysicsController() {
        objects = new HashSet<>();
    }

    public void add(PhysicsObject physicsObject) {
        objects.add(physicsObject);
    }

    public void step() {
        PriorityQueue<Collision> collisions = new PriorityQueue<>(new Comparator<Collision>() {
            @Override
            public int compare(Collision a, Collision b) {
                return Double.compare(a.time, b.time);
            }
        });
        HashMap<PhysicsObject, Double> times = new HashMap<>();
        HashSet<PhysicsObject> invalid = new HashSet<>();
        //
    }

    // moving and static
    private void doCollisionMoving1(MovingObject a, PhysicsObject b,
                                    Collision collision) {
        a.collide(b);
        b.collide(a);
        // TODO: real code
        System.out.println("Collision!");
        a.setPosition(a.getPosition().plus(a.getVelocity().times(collision.time)));
        a.setVelocity(new Vector());
    }

    // moving and moving
    private void doCollisionMoving2(MovingObject a, MovingObject b,
                                    Collision collision) {

        a.collide(b);
        b.collide(a);

        // move to collision
        a.setPosition(a.getPosition().plus(a.velocity.times(collision.time)));
        b.setPosition(b.getPosition().plus(b.velocity.times(collision.time)));

        if (a.velocity.dot(b.velocity) < 0)
            if (collision.normal.x == 0) {
                a.setVelocity(new Vector(a.getVelocity().x, 0));
                b.setVelocity(new Vector(b.getVelocity().x, 0));
            } else {
                a.setVelocity(new Vector(0, a.getVelocity().y));
                b.setVelocity(new Vector(0, b.getVelocity().y));
            }
        else {
//            Vector oldA = a.velocity;
//            a.setVelocity(b.velocity);
//            b.setVelocity(oldA);
            a.setVelocity(new Vector(0, 0));
            b.setVelocity(new Vector(0, 0));
        }
    }

}
