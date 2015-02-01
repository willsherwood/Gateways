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
    protected HashSet<PhysicsObject> objects;

    public PhysicsController() {
        objects = new HashSet<>();
    }

    public void add(PhysicsObject physicsObject) {
        objects.add(physicsObject);
    }

    public void step() {
        // first, do collisions
        QuadTree quadTree = new QuadTree(new AxisAlignedBoundingBox(0, 0, GameScreen.WIDTH, GameScreen.HEIGHT));
        objects.forEach(quadTree::insert);
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
            if (col != null && col.valid)
                collisions.add(col);
            else if (col != null) {
                // invalid collision, try to separate
                PhysicsObject A = col.objects.getA(), B = col.objects.getB();
                if (!(A instanceof MovingObject)) {
                    if (!(B instanceof MovingObject)) {
                        // do nothing
                    } else {
                        separateMoving1((MovingObject) B, A, col);
                    }
                } else {
                    if (!(B instanceof MovingObject)) {
                        separateMoving1((MovingObject) A, B, col);
                    } else {
                        separateMoving2((MovingObject) A, (MovingObject) B, col);
                    }
                }
            }
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
                    doCollisionMoving1((MovingObject) B, A, collision);
                    seen.add(B);
                    continue;
                }
            } else if (!(B instanceof MovingObject)) {
                if (seen.contains(A))
                    continue;
                doCollisionMoving1((MovingObject) A, B, collision);
                seen.add(A);
                continue;
            }
            if (seen.contains(A) || seen.contains(B))
                continue;
            doCollisionMoving2((MovingObject) A, (MovingObject) B, collision);
            seen.add(A);
            seen.add(B);
        }
        // move everything
        for (PhysicsObject object : objects) {
            if (object instanceof MovingObject) {
                object.setPosition(object.position.plus(((MovingObject)
                    object).getVelocity()));
            }
        }
    }

    private void separateMoving2(MovingObject A, MovingObject B, Collision collision) {
        // know that normal is pointing toward A
    }

    private void separateMoving1(MovingObject A, PhysicsObject B, Collision collision) {
        AxisAlignedBoundingBox bA = A.getAxisAlignedBoundingBox(),
                               bB = B.getAxisAlignedBoundingBox();
        System.out.println(collision.normal);
        Vector delta = new Vector();
        if (collision.normal.x < 0) {
            delta.x = bB.x1 - bA.x2;
        } else if (collision.normal.x > 0) {
            delta.x = bB.x2 - bA.x1;
        }
        if (collision.normal.y < 0) {
            delta.y = bB.y1 - bA.y2;
        } else if (collision.normal.y > 0) {
            delta.y = bB.y2 - bA.y1;
        }
        System.out.println("Separated by " + delta.magnitude());
        A.setPosition(A.getPosition().plus(delta));
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
        // stub
    }

    public static class Collision {
        public UnorderedPair<PhysicsObject> objects;
        Vector normal;
        double distance;
        double time; // make the program go twice as fast
        boolean valid;

        public Collision(UnorderedPair<PhysicsObject> objects) {
            this.objects = objects;
            normal = new Vector();
            distance = Double.NaN; // not yet calculated
            time = Double.NaN;
            valid = false;
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
            if (!(A instanceof MovingObject) && B instanceof MovingObject) {
                // swap them
                Vector temp = vA;
                vA = vB;
                vB = temp;
                col.objects = new UnorderedPair<>(B, A);
                A = col.objects.getA();
                B = col.objects.getB();
            }
            Vector vel = vA.minus(vB);
            AxisAlignedBoundingBox bA = A.getAxisAlignedBoundingBox(),
                    bB = B.getAxisAlignedBoundingBox();
            double xInvEntry, yInvEntry, xInvExit, yInvExit;
            if (vel.x > 0) {
                xInvEntry = bB.x1 - bA.x2;
                xInvExit = bB.x2 - bA.x1;
            } else {
                xInvEntry = bB.x2 - bA.x1;
                xInvExit = bB.x1 - bA.x2;
            }
            if (vel.y > 0) {
                yInvEntry = bB.y1 - bA.y2;
                yInvExit = bB.y2 - bA.y1;
            } else {
                yInvEntry = bB.y2 - bA.y1;
                yInvExit = bB.y1 - bA.y2;
            }
            double xEntry, yEntry, xExit, yExit;
            if (vel.x == 0) {
                xEntry = Double.NEGATIVE_INFINITY;
                xExit = Double.POSITIVE_INFINITY;
            } else {
                xEntry = xInvEntry / vel.x;
                xExit = xInvExit / vel.x;
            }
            if (vel.y == 0) {
                yEntry = Double.NEGATIVE_INFINITY;
                yExit = Double.POSITIVE_INFINITY;
            } else {
                yEntry = yInvEntry / vel.y;
                yExit = yInvExit / vel.y;
            }
            double entry = Math.max(xEntry, yEntry);
            double exit = Math.min(xExit, yExit);
            col.time = entry;
            col.distance = entry * vel.magnitude();
            if (entry > exit || xEntry > 1 || yEntry > 1) {
                // no collision
                return null;
            }
            // this means they haven't already collided
            if (xEntry > 0 || yEntry > 0) {
                col.valid = true;
            }
            if (!col.valid) {
                // find the closest distance
                // also, fudge the numbers so that we can choose which
                // side to put the normal on
                if (Math.abs(bB.x1 - bA.x2) > Math.abs(bB.x2 - bA.x1)) {
                    xInvEntry = bB.x2 - bA.x1 - 1;
                } else {
                    xInvEntry = bB.x1 - bA.x2 + 1;
                }
                if (Math.abs(bB.y1 - bA.y2) > Math.abs(bB.y2 - bA.y1)) {
                    yInvEntry = bB.y2 - bA.y1 - 1;
                } else {
                    yInvEntry = bB.y1 - bA.y2 + 1;
                }
            }
            if (col.valid ? xEntry > yEntry : Math.abs(xInvEntry) < Math.abs(yInvEntry)) {
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
            return col;
        }
    }
}
