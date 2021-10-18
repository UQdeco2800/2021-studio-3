package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.physics.components.PhysicsComponent;

public class Platform_y_Task extends DefaultTask implements PriorityTask {
    private final int priority;
    private final float y;
    private float p_y = 0;
    private boolean isMove = true;

    /**
     * @param priority Task priority when chasing (0 when not chasing).
     */
    public Platform_y_Task(float y, int priority) {
        this.y = y;
        this.priority = priority;
    }

    @Override
    public void start() {
        super.start();
        p_y = this.owner.getEntity().getPosition().y;
    }

    @Override
    public void update() {
        float position_y = this.owner.getEntity().getPosition().y;
        if (isMove) {
            if(position_y <= p_y + y/2) {
                this.owner.getEntity().getComponent(PhysicsComponent.class).getBody().setLinearVelocity(new Vector2(0,1));
            } else {
                isMove = false;
            }
        } else {
            if(position_y >= p_y - y/2) {
                this.owner.getEntity().getComponent(PhysicsComponent.class).getBody().setLinearVelocity(new Vector2(0,-1));
            } else {
                isMove = true;
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
