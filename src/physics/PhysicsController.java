package physics;

import map.QuadTree;
import sherwood.gameScreen.GameScreen;
import util.UnorderedPair;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class PhysicsController {
    public static final double ELASTICITY = 0.9;

    public static class Collision {
        public UnorderedPair<PhysicsObject> objects;
        Vector normal;
        double distance;

        public Collision(UnorderedPair<PhysicsObject> objects) {
            this.objects = objects;
            normal = new Vector();
            distance = Double.NaN; // not yet calculated
        }

        public static Collision resolve(UnorderedPair<PhysicsObject> o) {
            Collision col = new Collision(o);
            PhysicsObject A = o.getA(), B = o.getB();
            Vector vA = new Vector(), vB = new Vector();
            if (A instanceof MovingObject) {
                vA = ((MovingObject) A).getVelocity();
            }
            if (B instanceof MovingObject) {
                vB = ((MovingObject) B).getVelocity();
            }
            Vector vel = vA.minus(vB);
            AxisAlignedBoundingBox bA = o.getA().getAxisAlignedBoundingBox(),
                                   bB = o.getB().getAxisAlignedBoundingBox();
            double xInvEntry, yInvEntry, xInvExit, yInvExit;
            if (vel.x > 0) {
                xInvEntry = bB.x1 - bA.x2;
                xInvExit  = bB.x2 - bA.x1;
            } else {
                xInvEntry = bB.x2 - bA.x1;
                xInvExit  = bB.x1 - bA.x2;
            }
            if (vel.y > 0) {
                yInvEntry = bB.y1 - bA.y2;
                yInvExit  = bB.y2 - bA.y1;
            } else {
                yInvEntry = bB.y2 - bA.y1;
                yInvExit  = bB.y1 - bA.y2;
            }
            double xEntry, yEntry, xExit, yExit;
            if (vel.x == 0) {
                xEntry = Double.NEGATIVE_INFINITY;
                xExit  = Double.POSITIVE_INFINITY;
            } else {
                xEntry = xInvEntry / vel.x;
                xExit  = xInvExit  / vel.x;
            }
            if (vel.y == 0) {
                yEntry = Double.NEGATIVE_INFINITY;
                yExit  = Double.POSITIVE_INFINITY;
            } else {
                yEntry = yInvEntry / vel.y;
                yExit  = yInvExit  / vel.y;
            }
            double entry = Math.max(xEntry, yEntry);
            double exit  = Math.min(xExit,  yExit);
            if (entry > exit || xEntry < 0 && yEntry < 0 || xEntry > 1 || yEntry > 1)
                return null;
            if (xEntry > yEntry) {
                if (xInvEntry < 0) {
                    col.normal.x = 1;
                    col.normal.y = 0;
                } else {
                    col.normal.x = -1;
                    col.normal.y = 0;
                }
            } else {
                if (yInvEntry < 0) {
                    col.normal.x = 0;
                    col.normal.y = 1;
                } else {
                    col.normal.x = 0;
                    col.normal.y = -1;
                }
            }
            col.distance = entry * vel.magnitude();
            return col;
        }
    }

    protected HashSet<PhysicsObject> objects;

    public PhysicsController() {
        objects = new HashSet<>();
    }
    public void step() {
        // first, do collisions
        QuadTree quadTree = new QuadTree(new AxisAlignedBoundingBox(0, 0, GameScreen.WIDTH, GameScreen.HEIGHT));
        for (PhysicsObject obj : objects)
            quadTree.insert(obj);
        // not guaranteed, just broad-phase
        Set<UnorderedPair<PhysicsObject>> collidingObjects = quadTree.getCollidingPairs();
        quadTree = null; // this is going to be memory-intensive
        TreeSet<Collision> collisions = new TreeSet<>(new Comparator<Collision>() {
            @Override
            public int compare(Collision o1, Collision o2) {
                return Double.compare(o1.distance, o2.distance);
            }
        });
        for (UnorderedPair<PhysicsObject> pair : collidingObjects) {
            Collision col = Collision.resolve(pair);
            if (col != null)
                collisions.add(col);
        }
        HashSet<PhysicsObject> seen = new HashSet<>();
        for (Collision collision : collisions) {
            PhysicsObject A = collision.objects.getA(),
                          B = collision.objects.getB();
            if (!(A instanceof MovingObject)) {
                if (!(B instanceof MovingObject)) {
                    // do nothing
                    continue;
                } else {
                    if (seen.contains(B))
                        continue;
                    doCollisionMoving1((MovingObject)B, A);
                    seen.add(B);
                    continue;
                }
            } else if (!(B instanceof MovingObject)) {
                if (seen.contains(A))
                    continue;
                doCollisionMoving1((MovingObject)A, B);
                seen.add(A);
                continue;
            }
            if (seen.contains(A) || seen.contains(B))
                continue;
            doCollisionMoving2((MovingObject)A, (MovingObject)B);
            seen.add(A);
            seen.add(B);
        }
    }

    // moving and static
    private void doCollisionMoving1(MovingObject a, PhysicsObject b) {
        a.collide(b);
        b.collide(a);
        // stub
    }

    // moving and moving
    private void doCollisionMoving2(MovingObject a, MovingObject b) {
        a.collide(b);
        b.collide(a);
        // stub
    }
}
