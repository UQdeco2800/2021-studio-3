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
    entity.getEvents().addListener("updatePlayerStatusAnimation", this::updatePlayerStatusAnimation);
  }

  void startAnimateWalk() {animator.startAnimation("angry_float");}

  void stopAnimateWalk() {animator.startAnimation("angry_float");}

  void updatePlayerStatusAnimation(int health) {
    if (health <= 90 && health > 50) {
      System.out.println("i'm good");
    } else if (health <= 50 && health > 10) {
      System.out.println("i'm ok");
    } else if (health == 0){
      System.out.println("help me!!!!!!!!!");
    }

  }

}
