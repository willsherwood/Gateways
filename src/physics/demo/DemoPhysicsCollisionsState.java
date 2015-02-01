package physics.demo;

import physics.*;
import sherwood.gameScreen.GameScreen;
import sherwood.inputs.keyboard.control.Control;
import sherwood.inputs.keyboard.control.discrete.DiscreteControlKeyboardInput;
import sherwood.screenStates.ScreenState;
import util.Entropy;

import java.awt.*;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public class DemoPhysicsCollisionsState extends ScreenState {

    private PhysicsController physicsController;
    private Set<MovingObject> movingObjects;
    private Set<StaticObject> staticObjects;

    public DemoPhysicsCollisionsState() {
        this.physicsController = new PhysicsController();
        this.movingObjects = new HashSet<>();
        this.staticObjects = new HashSet<>();

        staticObjects.add(new StaticObject(100, 100, 10, 400));

        MovingObject a = new MovingObject(300, 300, 64, 64);
        MovingObject b = new MovingObject(400, 200, 64, 64);
        a.setVelocity(new Vector(-1, 0.2));
        b.setVelocity(new Vector(-2, 1.4));
        movingObjects.add(a);
        movingObjects.add(b);

        staticObjects.forEach(physicsController::add);
        movingObjects.forEach(physicsController::add);

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.GREEN);
        for (PhysicsObject p : movingObjects)
            g.drawRect((int) p.getAxisAlignedBoundingBox().x1, (int) p.getAxisAlignedBoundingBox().y1, (int) p.getWidth(), (int) p.getHeight());
        g.setColor(Color.RED);
        for (PhysicsObject p : staticObjects)
            g.drawRect((int) p.getAxisAlignedBoundingBox().x1, (int) p.getAxisAlignedBoundingBox().y1, (int) p.getWidth(), (int) p.getHeight());
    }

    @Override
    public void step(BitSet keys) {
        physicsController.step();
        if (keys.get(Control.START.ordinal())) {
            MovingObject o = new MovingObject(500, 300, 40, 40);
            o.setVelocity(new Vector(Entropy.next(-5, -1), Entropy.next(-2, 2)));
            movingObjects.add(o);
        }
    }

    @Override
    public void init() {
        super.init();
        GameScreen.get().requestKeyInputMechanism(new DiscreteControlKeyboardInput());
    }
}
