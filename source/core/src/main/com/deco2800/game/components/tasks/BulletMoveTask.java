package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.services.ServiceLocator;

public class BulletMoveTask extends DefaultTask implements PriorityTask {
    private final float waitTime;
    private Vector2 startPos;
    private MovementTask movementTask;
    private WaitTask waitTask;
    private Task currentTask;

    public BulletMoveTask(float waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();
        startPos.set(startPos.x, startPos.y - 5);
        //waitTask = new WaitTask(waitTime);
        //waitTask.create(owner);
        movementTask = new MovementTask(startPos);
        movementTask.create(owner);
        movementTask.start();
        currentTask = movementTask;

        this.owner.getEntity().getEvents().trigger("FallStart");
    }

    @Override
    public void update() {
        movementTask.setTarget(startPos);
        movementTask.update();
        if (currentTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }

    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
    }



    // private void swapTask(Task newTask) {
    //     if (currentTask != null) {
    //         currentTask.stop();
    //     }
    //     currentTask = newTask;
    //     currentTask.start();
    // }

    @Override
    public int getPriority() {
        return 1; // Low priority task
    }
}

