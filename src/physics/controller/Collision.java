package physics.controller;

import physics.*;
import util.UnorderedPair;

/**
* Created by s506571 on 2/5/2015.
*/
public class Collision {
    public UnorderedPair<PhysicsObject> objects;
    public Vector normal;
    public Vector intersection;
    public double distance;
    public double time; // make the program go twice as fast
    public boolean valid;

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
