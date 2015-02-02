package physics.demo;

import physics.*;
import sherwood.gameScreen.FPSUpdateAlgorithm;
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

        for (int i=0; i<3; i++) {
            MovingObject o = new MovingObject(400+i*100, 300, 50, 50);
            o.setVelocity(new Vector(-20.4, 0.2));
            movingObjects.add(o);
        }

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
            insertNewObject();
        }
    }

    public void insertNewObject() {
        MovingObject o = new MovingObject(Entropy.next(0, GameScreen.WIDTH-50),
                                          Entropy.next(0, GameScreen.HEIGHT-50),
                                          Entropy.next(5, 15), Entropy.next(5, 15));
        o.setVelocity(new Vector(Entropy.next(-3, 3), Entropy.next(-3, 3)));
        movingObjects.add(o);
        physicsController.add(o);
    }

    @Override
    public void init() {
        super.init();
        GameScreen.get().requestUpdateAlgorithm(new FPSUpdateAlgorithm(60));
        GameScreen.get().requestKeyInputMechanism(new DiscreteControlKeyboardInput());
    }
}
