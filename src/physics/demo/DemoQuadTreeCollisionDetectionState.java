package physics.demo;

import map.QuadTree;
import physics.AxisAlignedBoundingBox;
import physics.PhysicsObject;
import sherwood.gameScreen.GameScreen;
import sherwood.inputs.keyboard.control.Control;
import sherwood.inputs.keyboard.control.MixedControlKeyboardInput;
import sherwood.screenStates.ScreenState;
import util.Entropy;
import util.UnorderedPair;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * A testing class to test the functionality of the AABB collision tester
 */
public class DemoQuadTreeCollisionDetectionState extends ScreenState {

    private int index;
    private List<DemoPhysicsObject> boxes;
    private QuadTree lastQuadTree;

    private boolean drawGridLines;

    /**
     * once Javange is refactored to give control states
     * an enum set, Integer will be replaced with enum
     * and ordinal will no longer be necessary
     */
    private Map<Integer, physics.Vector> directions;

    public DemoQuadTreeCollisionDetectionState() {
        boxes = new ArrayList<>();
        directions = new HashMap<>();

        for (int i = 0; i < 2; i++)
            addNewBox();

        directions.put(Control.UP.ordinal(), new physics.Vector(0, -3));
        directions.put(Control.DOWN.ordinal(), new physics.Vector(0, 3));
        directions.put(Control.LEFT.ordinal(), new physics.Vector(-3, 0));
        directions.put(Control.RIGHT.ordinal(), new physics.Vector(3, 0));
    }

    @Override
    public void draw(Graphics2D g) {

        String[] instructions = new String[]{
                "A - go to next block",
                "B - go to previous block",
                "Start - add another block",
                "Select - toggle gridlines",
                "colliding  blocks are shown in red",
                "number of boxes: " + boxes.size()};
        for (int i = 0; i < instructions.length; i++)
            g.drawString(instructions[i], 30, 30 + i * 40);

        for (DemoPhysicsObject box : boxes) {
            if (box.isColliding())
                g.setColor(Color.RED);
            else
                g.setColor(Color.LIGHT_GRAY);
            g.fillRect((int) box.getPosition().x, (int) box.getPosition().y, (int) box.getWidth(), (int) box.getHeight());
        }
        if (drawGridLines) {
            g.setColor(Color.GREEN);
            for (AxisAlignedBoundingBox box : lastQuadTree.getBoundingBoxes())
                g.drawRect((int) box.x1, (int) box.y1, (int) box.width(), (int) box.height());
        }
    }

    @Override
    public void step(BitSet keys) {
        for (int i = keys.nextSetBit(0); i >= 0; i = keys.nextSetBit(i + 1))
            if (directions.containsKey(i)) {
                physics.Vector delta = directions.get(i);
                DemoPhysicsObject box = boxes.get(index);
                box.setPosition(box.getPosition().plus(delta));
            }
        if (keys.get(Control.A.ordinal()))
            index = (index + 1) % boxes.size();
        if (keys.get(Control.B.ordinal()))
            index = (index + boxes.size() - 1) % boxes.size();
        if (keys.get(Control.START.ordinal()))
            addNewBox();
        if (keys.get(Control.SELECT.ordinal()))
            drawGridLines = !drawGridLines;
        boxes.forEach(DemoPhysicsObject::step);
        // calculate collisions
        lastQuadTree = new QuadTree(new AxisAlignedBoundingBox(0, 0, GameScreen.WIDTH, GameScreen.HEIGHT));
        boxes.forEach(lastQuadTree::insert);
        for (UnorderedPair<PhysicsObject> q : lastQuadTree.getAllCollidingPairs()) {
            q.getA().collide(q.getB());
        }
    }

    private void addNewBox() {
        for (int i = 0; i < 16; i++)
            boxes.add(new DemoPhysicsObject(Entropy.nextInt(10, GameScreen.WIDTH - 20), Entropy.nextInt(10, GameScreen.HEIGHT - 20), Entropy.nextInt(1, 10), Entropy.nextInt(1, 10)));
    }

    @Override
    public void init() {
        super.init();
        BitSet continuousKeys = new BitSet();
        for (Control c : new Control[]{Control.UP, Control.DOWN, Control.LEFT, Control.RIGHT, Control.START})
            continuousKeys.set(c.ordinal());
        GameScreen.get().requestKeyInputMechanism(new MixedControlKeyboardInput(continuousKeys));
    }
}
