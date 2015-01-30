package physics;

import sherwood.gameScreen.GameScreen;
import sherwood.inputs.keyboard.control.Control;
import sherwood.inputs.keyboard.control.MixedControlKeyboardInput;
import sherwood.screenStates.ScreenState;

import java.awt.*;
import java.util.ArrayList;
import java.util.BitSet;

public class DemoPhysicsState extends ScreenState {

    private Iterable<ImmovableObject> immovableObjets;

    public DemoPhysicsState() {
        immovableObjets = new ArrayList<>();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);
        for (ImmovableObject i : immovableObjets)
            g.fill(i.getRect());

    }

    @Override
    public void step(BitSet keys) {

    }

    @Override
    public void init() {
        super.init();
        BitSet continuousKeys = new BitSet(Control.values().length);
        for (Control c : new Control[]{Control.UP, Control.DOWN, Control.LEFT, Control.RIGHT})
            continuousKeys.set(Control.getCondensed(c));
        GameScreen.get().requestKeyInputMechanism(new MixedControlKeyboardInput(continuousKeys));
    }
}
