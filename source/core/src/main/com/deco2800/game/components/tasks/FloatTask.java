package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class FloatTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(FloatTask.class);

    private final Vector2 wanderRange;

    private Vector2 startPos;
    private MovementTask movementTask;



    /**
     * @param wanderRange Distance in X and Y the entity can move from its position when start() is
     *     called.
     */
    public FloatTask(Vector2 wanderRange) {
        this.wanderRange = wanderRange;

    }

    @Override
    public int getPriority() {
        return 1; // Low priority task
    }

    @Override
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();

        movementTask = new MovementTask(getTarget());
        movementTask.create(owner);

        movementTask.start();



    }

    public Status getMovementStatus() {
        return movementTask.getStatus();
    }

    @Override
    public void update() {


        //System.out.println(movementTask.getStatus());
        movementTask.update();

    }




    private Vector2 getTarget() {

        return new Vector2(startPos.x + wanderRange.x, startPos.y + wanderRange.y);

    }
}

