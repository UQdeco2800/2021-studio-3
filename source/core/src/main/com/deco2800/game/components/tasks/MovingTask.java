package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.physics.components.PhysicsComponent;

/**
 * A class that move toward a target in constant speed
 */
public class MovingTask extends DefaultTask implements PriorityTask {
    private final Vector2 target;
    private MovementTask movementTask;
    private final float speed;

    public MovingTask(Vector2 target, float speed) {
        this.target = target;
        this.speed = speed;
    }

    @Override
    public int getPriority() {
        return 1;
    }


    @Override
    public void start() {
        super.start();
        movementTask = new MovementTask(this.target);
        movementTask.create(owner);

        movementTask.start();
    }

    @Override
    public void update() {
        updateSpeed();
        movementTask.update();
    }

    /**
     * To update the death position by the speed
     */
    private void updateSpeed () {
        Body body = this.owner.getEntity().getComponent(PhysicsComponent.class).getBody();
        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = new Vector2(speed, 0).scl(new Vector2(3f,
                3f));
        // impulse = (desiredVel - currentVel) * mass
        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }
}
