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
        // sort collisions by distance
        TreeSet<Collision> collisions = new TreeSet<>(new Comparator<Collision>() {
            @Override
            public int compare(Collision o1, Collision o2) {
                return Double.compare(o1.distance, o2.distance);
            }
        });
        // make sure each collision is real and separate separate collision from regular collisions
        for (UnorderedPair<PhysicsObject> pair : collidingObjects) {
            // debug
            System.out.println(pair.getA() + " and " + pair.getB());
            // resolve the collision
            Collision col = Collision.resolve(pair);
            // only add non-separating collisions
            if (col != null && col.valid)
                collisions.add(col);
            // this means that the collision is not valid i.e. it is a separating collision
            else if (col != null) {
                System.out.println("Invalid");
                // invalid collision, try to separate
                PhysicsObject A = col.objects.getA(), B = col.objects.getB();
                if (!(A instanceof MovingObject)) {
                    if (!(B instanceof MovingObject)) {
                        // do nothing
                        // if A and B can't move
                    } else {
                        // separate A from B, where A is a wall
                        separateMoving1((MovingObject) B, A, col);
                    }
                } else {
                    if (!(B instanceof MovingObject)) {
                        // separate A from B, there B is a wall
                        separateMoving1((MovingObject) A, B, col);
                    } else {
                        // separate A and B
                        separateMoving2((MovingObject) A, (MovingObject) B, col);
                    }
                }
            }
        }
        HashSet<PhysicsObject> alreadyCollided = new HashSet<>();
        for (Collision collision : collisions) {
            PhysicsObject A = collision.objects.getA(),
                    B = collision.objects.getB();
            if (!(A instanceof MovingObject)) {
                if (!(B instanceof MovingObject)) {
                    // do nothing
                    continue;
                } else {
                    if (alreadyCollided.contains(B))
                        continue;
                    doCollisionMoving1((MovingObject) B, A, collision);
                    alreadyCollided.add(B);
                    continue;
                }
            } else if (!(B instanceof MovingObject)) {
                if (alreadyCollided.contains(A))
                    continue;
                doCollisionMoving1((MovingObject) A, B, collision);
                alreadyCollided.add(A);
                continue;
            }
            if (alreadyCollided.contains(A) || alreadyCollided.contains(B))
                continue;
            doCollisionMoving2((MovingObject) A, (MovingObject) B, collision);
            alreadyCollided.add(A);
            alreadyCollided.add(B);
        }
        // move everything
        objects.stream().filter(a -> a instanceof MovingObject).forEach(b -> b.setPosition(b.position.plus(((MovingObject) b).getVelocity())));
    }

    private void separateMoving2(MovingObject A, MovingObject B, Collision collision) {
//        // know that normal is pointing toward A
//        System.out.println(collision.normal);
        A.setVelocity(new Vector());
        B.setVelocity(new Vector());
//        Vector halfSep = collision.intersection.multiply(collision.normal).times(0.5);
//        A.setPosition(A.getPosition().plus(halfSep));
//        B.setPosition(B.getPosition().plus(halfSep.times(-1)));
    }

    private void separateMoving1(MovingObject A, PhysicsObject B, Collision collision) {
//        AxisAlignedBoundingBox bA = A.getAxisAlignedBoundingBox(),
//                bB = B.getAxisAlignedBoundingBox();
//        System.out.println(collision.normal);
//        Vector delta = new Vector();
//        if (collision.normal.x < 0) {
//            delta.x = bB.x1 - bA.x2;
//        } else if (collision.normal.x > 0) {
//            delta.x = bB.x2 - bA.x1;
//        }
//        if (collision.normal.y < 0) {
//            delta.y = bB.y1 - bA.y2;
//        } else if (collision.normal.y > 0) {
//            delta.y = bB.y2 - bA.y1;
//        }
//        if (collision.normal.x > 0 && A.velocity.x < 0
//                || collision.normal.x < 0 && A.velocity.x > 0)
//            A.velocity.x = 0;
//        if (collision.normal.y > 0 && A.velocity.y < 0
//                || collision.normal.y < 0 && A.velocity.y > 0)
//            A.velocity.y = 0;
//        System.out.println("Separated by " + delta);
//        A.setPosition(A.getPosition().plus(delta));
        A.setVelocity(new Vector());
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

    public static class Collision {
        public UnorderedPair<PhysicsObject> objects;
        Vector normal;
        Vector intersection;
        double distance;
        double time; // make the program go twice as fast
        boolean valid;

        public Collision(UnorderedPair<PhysicsObject> objects) {
            this.objects = objects;
            normal = new Vector();
            intersection = null;
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
                System.out.println("Actually no collision");
                return null;
            }
            if (xEntry >= 0 || yEntry >= 0) {
                col.valid = true;
            }
            if (!col.valid) {
                // find the closest distance
                // also, fudge the numbers so that we can choose which
                // side to put the normal on
                col.intersection = new Vector();
                System.out.println("To go right, " + (bB.x1 - bA.x2));
                System.out.println("To go left , " + (bB.x2 - bA.x1));
                if (Math.abs(bB.x1 - bA.x2) > Math.abs(bB.x2 - bA.x1)) {
                    col.intersection.x = Math.abs(bB.x2 - bA.x1);
                    xInvEntry = bB.x2 - bA.x1 - 100;
                } else {
                    col.intersection.x = Math.abs(bB.x1 - bA.x2);
                    xInvEntry = bB.x1 - bA.x2 + 100;
                }
                if (Math.abs(bB.y1 - bA.y2) < Math.abs(bB.y2 - bA.y1)) {
                    col.intersection.y = Math.abs(bB.y2 - bA.y1);
                    yInvEntry = bB.y2 - bA.y1 - 100;
                } else {
                    col.intersection.y = Math.abs(bB.y1 - bA.y2);
                    yInvEntry = bB.y1 - bA.y2 + 100;
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
