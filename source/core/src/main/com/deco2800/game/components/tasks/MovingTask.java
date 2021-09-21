package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;

/**
 * A class that move toward a target in constant speed
 */
public class MovingTask extends DefaultTask implements PriorityTask {
    private final Vector2 target;
    private MovementTask movementTask;


    public MovingTask(Vector2 target) {
        this.target = target;
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
        movementTask.update();
    }
}
