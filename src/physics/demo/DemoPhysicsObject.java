package physics.demo;

import physics.MovingObject;
import physics.PhysicsObject;
import physics.Vector;
import sherwood.gameScreen.GameScreen;
import util.Entropy;

public class DemoPhysicsObject extends MovingObject {

    private boolean colliding;

    public DemoPhysicsObject(double x, double y, double width, double height) {
        super(x, y, width, height);
        setVelocity(new Vector(Entropy.next(-1, 2), Entropy.next(-1, 2)));
    }

    public DemoPhysicsObject(Vector position, Vector size) {
        super(position, size);
    }

    public DemoPhysicsObject(double width, double height) {
        super(width, height);
    }

    public boolean isColliding() {
        boolean out = colliding;
        colliding = false;
        return out;
    }

    @Override
    public void step() {
        super.step();
        position = position.plus(velocity);
        if (position.x() + size.x() >= GameScreen.WIDTH || position.x() < 0)
            velocity = new Vector(-velocity.x(), velocity.y());
        if (position.y() + size.y() >= GameScreen.HEIGHT || position.y() < 0)
            velocity = new Vector(velocity.x(), -velocity.y());
    }

    @Override
    public void collide(PhysicsObject o) {
        colliding = true;
    }
}
