package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ufo state and plays the animation when one
 * of the events is triggered.
 */
public class UfoAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("wanderStart", this::animateWander);
        entity.getEvents().addListener("chaseStart", this::animateChase);
        //entity.getEvents().addListener("FallStart", this::animateFall);
    }

   void animateWander() {
       animator.startAnimation("ufo");
    }

   void animateChase() {
       animator.startAnimation("hit_ufo");
   }

    //void animateFall() {
    //    animator.startAnimation("ufo");}
}
