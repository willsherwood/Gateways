package map;

import physics.AxisAlignedBoundingBox;
import physics.PhysicsObject;

import java.util.HashSet;
import java.util.Set;

/**
 * A data structure representing a quadtree of physics objects
 */
public class QuadTree {

    public static final int BUCKET_CAPACITY = 4;
    public static final int MINIMUM_WIDTH = 8;

    protected final AxisAlignedBoundingBox box;
    protected QuadTree[] children;
    protected Set<PhysicsObject> elements;

    public QuadTree(AxisAlignedBoundingBox box) {
        this.elements = new HashSet<>(BUCKET_CAPACITY);
        this.box = box;
    }

    /**
     * @param box the physics object to insert
     * @return whether or not the box was added
     */
    public boolean insert(PhysicsObject box) {
        if (!box.getAxisAlignedBoundingBox().intersects(this.box))
            return false;
        //TODO: optimize
        if (children != null) {
            for (QuadTree Q : children)
                Q.insert(box);
            return true;
        }
        if (elements.size() == BUCKET_CAPACITY && this.box.width() >= MINIMUM_WIDTH) {
            split();
            for (QuadTree Q : children)
                Q.insert(box);
            return true;
        }
        return elements.add(box);
    }

    protected void split() {
        children = new QuadTree[4];
        children[0] = new QuadTree(new AxisAlignedBoundingBox(box.x1, box.y1, box.x1 + box.width() / 2, box.y1 + box.height() / 2));
        children[1] = new QuadTree(new AxisAlignedBoundingBox(box.x1 + box.width() / 2, box.y1, box.x1 + box.width(), box.y1 + box.height() / 2));
        children[2] = new QuadTree(new AxisAlignedBoundingBox(box.x1, box.y1 + box.height() / 2, box.x1 + box.width() / 2, box.y1 + box.height()));
        children[3] = new QuadTree(new AxisAlignedBoundingBox(box.x1 + box.width() / 2, box.y1 + box.height() / 2, box.x1 + box.width(), box.y1 + box.height()));
        for (QuadTree Q : children) {
            elements.forEach(Q::insert);
        }
        elements.clear();
    }

    /**
     * this should be called after building the quadtree
     * it will call a.collide(b) for all a that collide with b
     * whether a.collide(b) or b.collide(a) is called first is undefined
     */
    public void regressCollisions() {
        PhysicsObject[] objects = elements.toArray(new PhysicsObject[0]);
        for (int i = 0; i < objects.length; i++)
            for (int j = 0; j < objects.length; j++)
                if (i != j && objects[i].getAxisAlignedBoundingBox().intersects(objects[j].getAxisAlignedBoundingBox()))
                    objects[i].collide(objects[j]);
        if (children == null) return;
        for (QuadTree t : children)
            t.regressCollisions();
    }

    /**
     * @return all the bounding boxes
     */
    @Deprecated
    public Set<AxisAlignedBoundingBox> getBoundingBoxes() {
        Set<AxisAlignedBoundingBox> boxes = new HashSet<>();
        boxes.add(box);
        if (children == null)
            return boxes;
        for (QuadTree p : children)
            boxes.addAll(p.getBoundingBoxes());
        return boxes;
    }
}
