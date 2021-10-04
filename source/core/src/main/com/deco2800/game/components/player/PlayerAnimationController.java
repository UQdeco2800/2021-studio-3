package com.deco2800.game.components.player;

import com.deco2800.game.components.CombatStatsComponent;
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
    entity.getEvents().addListener("playerStatusAnimation", this::updatePlayerStatusAnimation);
  }

  /**
   * Updates the health of the player then applies the correct animation to the player based on states
   * in the PlayerStateComponent.java
   * @see PlayerStateComponent
   */
  void updatePlayerStatusAnimation() {
    // Updates the health value in PlayerStateComponent
    int health = this.entity.getComponent(CombatStatsComponent.class).getHealth();

    if (health <= 100 && health > 90) {
      this.entity.getComponent(PlayerStateComponent.class).updateHealth(Health.NORMAL);
    } else if (health <= 90 && health > 50) {
      this.entity.getComponent(PlayerStateComponent.class).updateHealth(Health.ROUGH);
    } else if (health <= 50 && health > 10) {
      this.entity.getComponent(PlayerStateComponent.class).updateHealth(Health.DAMAGED);
    } else if (health == 0){
      this.entity.getComponent(PlayerStateComponent.class).updateHealth(Health.DEAD);
    }

    // Applies the correct animation
    if (this.enabled) {
      animator.startAnimation(entity.getComponent(PlayerStateComponent.class).getStateAnimation());
    }
  }

}
