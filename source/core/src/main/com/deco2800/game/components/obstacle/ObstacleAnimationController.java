package com.deco2800.game.components.obstacle;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a asteroid fire and robot state and plays the animation when one
 * of the events is triggered.
 */
public class ObstacleAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("wanderStart", this::animateWander);
        //entity.getEvents().addListener("chaseStart", this::animateChase);
    }

    void animateWander() {
        animator.startAnimation("float");
    }
}
