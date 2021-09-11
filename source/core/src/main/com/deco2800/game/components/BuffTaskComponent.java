package com.deco2800.game.components;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.TaskRunner;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.maingame.BuffManager;
import com.deco2800.game.components.tasks.FloatTask;
import com.deco2800.game.components.tasks.MovementTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BuffTaskComponent extends Component implements TaskRunner {
    private static final Logger logger = LoggerFactory.getLogger(BuffTaskComponent.class);
    private FloatTask task;

    public BuffTaskComponent addTask(FloatTask task) {
        logger.debug("Adding buff task", this, task);
        this.task = task;
        task.create(this);
        System.out.println("i'm here");
        task.start();
        return this;
    }

    /*public MovementTask getTask() {
        return this.task;
    }*/

    @Override
    public void update() {
        System.out.println("hello");
        task.update();
        System.out.println("am i still here");
    }

    @Override
    public void dispose() {
        if (task != null) {
            task.stop();
        }
    }
}
