package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.services.ServiceLocator;

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
    entity.getEvents().addListener("ghostKingAngry", this::startGhostKingAngry);
    entity.getEvents().addListener("ghostKingFloat", this::startGhostKingFloat);
    entity.getEvents().addListener("walkAnimation", this::walkAnimation);
    entity.getEvents().addListener("updatePlayerStatusAnimation", this::updatePlayerStatusAnimation);
  }

  void startGhostKingAngry() {animator.startAnimation("angry_float");}

  void startGhostKingFloat() {animator.startAnimation("float");}

  void walkAnimation() {animator.startAnimation("walkRight");}

  void updatePlayerStatusAnimation(int health) {
    if (health <= 90 && health > 50) {
      this.entity.getComponent(PlayerStateComponent.class).updateHealth(Health.ROUGH);
    } else if (health <= 50 && health > 10) {
      this.entity.getComponent(PlayerStateComponent.class).updateHealth(Health.DAMAGED);
    } else if (health == 0){
      this.entity.getComponent(PlayerStateComponent.class).updateHealth(Health.DEAD);
    }

  }

}
