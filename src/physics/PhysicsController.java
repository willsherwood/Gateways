package physics;

import map.QuadTree;
import physics.controller.Collision;
import sherwood.gameScreen.GameScreen;
import util.UnorderedPair;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Comparator<Collision> timeComparator = (Collision a, Collision b) -> Double.compare(a.time, b.time);;
        PriorityQueue<Collision> collisions = new PriorityQueue<>(timeComparator);
        HashMap<PhysicsObject, Double> time = new HashMap<>();
        QuadTree quadTree = new QuadTree(new AxisAlignedBoundingBox(0, 0, GameScreen.WIDTH, GameScreen.HEIGHT));
        // initialize the collisions PriorityQueue
        collisions.addAll(quadTree.getAllCollidingPairs().stream().map(Collision::resolve).collect(Collectors.toList()));
        while (!collisions.isEmpty()) {
            Collision collision = collisions.remove();
            // move both objects
            if (collision.objects.getA() instanceof MovingObject) {
                collision.objects.getA().addPosition(((MovingObject) collision.objects.getA()).getVelocity().times(collision.time));
            }
            if (collision.objects.getB() instanceof MovingObject) {
                collision.objects.getB().addPosition(((MovingObject) collision.objects.getB()).getVelocity().times(collision.time));
            }
            if (collision.objects.getA() instanceof MovingObject) {
                if (collision.objects.getB() instanceof MovingObject) {
                    doCollisionMoving2((MovingObject) collision.objects.getA(), (MovingObject) collision.objects.getB(), collision);
                } else {
                    doCollisionMoving1((MovingObject) collision.objects.getA(), collision.objects.getB(), collision);
                }
            } else if (collision.objects.getB() instanceof MovingObject) {
                doCollisionMoving1((MovingObject) collision.objects.getB(), collision.objects.getA(), collision);
            }
            // remove collisions involving either
            PriorityQueue<Collision> newQueue = new PriorityQueue<>(timeComparator);
            newQueue.addAll(collisions.stream().filter((Collision test) ->
                    !(test.objects.getA() == collision.objects.getA() || test.objects.getB() == collision.objects.getA()
                   || test.objects.getA() == collision.objects.getB() || test.objects.getB() == collision.objects.getB())
            ).collect(Collectors.toList()));
            // TODO: recalculate AABBs and add
            // make sure to pass max time to Collision.resolve()
        }
    }

    // moving and static
    private void doCollisionMoving1(MovingObject a, PhysicsObject b,
                                    Collision collision) {
        a.collide(b);
        b.collide(a);
        // TODO: real code
        System.out.println("Collision!");
        a.setVelocity(new Vector());
    }

    // moving and moving
    private void doCollisionMoving2(MovingObject a, MovingObject b,
                                    Collision collision) {
        a.collide(b);
        b.collide(a);
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
