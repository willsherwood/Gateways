package physics.demo;

import physics.*;
import sherwood.screenStates.ScreenState;

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

        movingObjects.add(new MovingObject(500, 300, 40, 40));
        movingObjects.forEach(a -> a.setVelocity(new Vector(-1.7, 0.4)));

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
    }

    @Override
    public void init() {
        super.init();
    }
}
