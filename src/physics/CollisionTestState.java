package physics;

import sherwood.gameScreen.GameScreen;
import sherwood.inputs.keyboard.control.Control;
import sherwood.inputs.keyboard.control.MixedControlKeyboardInput;
import sherwood.screenStates.ScreenState;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * A testing class to test the functionality of the AABB collision tester
 */
public class CollisionTestState extends ScreenState {

    private int index;
    private List<AxisAlignedBoundingBox> boxes;
    private Set<AxisAlignedBoundingBox> collidingBoxes;

    /**
     * once Javange is refactored to give control states
     * an enum set, Integer will be replaced with enum
     * and ordinal will no longer be necessary
     */
    private Map<Integer, Vector> directions;

    public CollisionTestState() {
        boxes = new ArrayList<>();
        directions = new HashMap<>();
        collidingBoxes = new HashSet<>();

        boxes.add(new AxisAlignedBoundingBox(100, 100, 200, 150));
        boxes.add(new AxisAlignedBoundingBox(400, 425, 300, 450));

        directions.put(Control.UP.ordinal(), new Vector(0, -3));
        directions.put(Control.DOWN.ordinal(), new Vector(0, 3));
        directions.put(Control.LEFT.ordinal(), new Vector(-3, 0));
        directions.put(Control.RIGHT.ordinal(), new Vector(3, 0));
    }

    @Override
    public void draw(Graphics2D g) {

        String[] instructions = new String[]{
                "A - go to next block",
                "B - go to previous block",
                "Start - add another block",
                "colliding  blocks are shown in red"};
        for (int i=0; i<instructions.length; i++)
            g.drawString(instructions[i], 30, 30 + i * 40);

        for (AxisAlignedBoundingBox box : boxes) {
            if (collidingBoxes.contains(box)) {
                g.setColor(Color.RED);
                collidingBoxes.remove(box);
            } else
                g.setColor(Color.WHITE);
            g.fillRect((int) box.x1, (int) box.y1, (int) (box.x2 - box.x1), (int) (box.y2 - box.y1));
        }
    }

    @Override
    public void step(BitSet keys) {
        for (int i = keys.nextSetBit(0); i >= 0; i = keys.nextSetBit(i + 1))
            if (directions.containsKey(i)) {
                Vector delta = directions.get(i);
                AxisAlignedBoundingBox box = boxes.get(index);
                box.set(box.getTopLeftVector().plus(delta), box.getBottomRightVector().plus(delta));
            }

        if (keys.get(Control.A.ordinal()))
            index = (index + 1) % boxes.size();
        if (keys.get(Control.B.ordinal()))
            index = (index + boxes.size() - 1) % boxes.size();
        if (keys.get(Control.START.ordinal()))
            boxes.add(new AxisAlignedBoundingBox(200, 300, 400, 400));
        // calculate collisions
        for (int i = 0; i < boxes.size(); i++)
            for (int j = i + 1; j < boxes.size(); j++)
                if (boxes.get(i).intersects(boxes.get(j))) {
                    collidingBoxes.add(boxes.get(i));
                    collidingBoxes.add(boxes.get(j));
                }
    }

    @Override
    public void init() {
        super.init();
        BitSet continuousKeys = new BitSet();
        for (Control c : new Control[]{Control.UP, Control.DOWN, Control.LEFT, Control.RIGHT})
            continuousKeys.set(c.ordinal());
        GameScreen.get().requestKeyInputMechanism(new MixedControlKeyboardInput(continuousKeys));
    }
}
