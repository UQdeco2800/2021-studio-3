package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a player entity's state and plays the animation when one
 * of the events is triggered.
 */
public class PlayerAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("startWalkAnimation", this::startAnimateWalk);
    entity.getEvents().addListener("stopWalkAnimation", this::stopAnimateWalk);
  }

  void startAnimateWalk() {animator.startAnimation("angry_float");}

  void stopAnimateWalk() {animator.startAnimation("float");}

}
