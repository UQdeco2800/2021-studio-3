package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.*;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;

/**
 * Action component for interacting with the player. Player events should be
 * initialised in create() and when triggered should call methods within this
 * class.
 */
public class PlayerActions extends Component {
  private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second

  /* The entity's physics component */
  private PhysicsComponent physicsComponent;

  // Sets gravity to 1 m/s for comparing
  private static final Vector2 gravity = new Vector2 (0, -1f);

  // Sets player movement and adds gravity of 1 m/s
  public Vector2 walkDirection = new Vector2 (0, -1f);

  /* True if the player is currently moving */
  private boolean moving = false;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("sprint", this::sprint);
    entity.getEvents().addListener("newProgress", this::upgradeProgress);
    entity.getEvents().addListener("newScore", this::upgradeScore);
    entity.getEvents().addListener("roll", this::roll);
    entity.getEvents().addListener("stopRoll", this::stopRolling);
  }

  @Override
  public void update() {
    if (moving) {
      updateSpeed();
      upgradeProgress();
    }
    upgradeScore();
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  /**
   * Rolls the player in a given direction.
   *
   * @param direction the direction for the player to roll in.
   * */
  public void roll(Vector2 direction) {
    if (entity.getComponent(RollComponent.class).cantRoll()) {
      entity.getComponent(KeyboardPlayerInputComponent.class).setRolling(false);
      return;
    }
    entity.getComponent(RollComponent.class).handleRolling(direction);
  }

  /**
   * Stops the player rolling.
   * */
  public void stopRolling() {
    stopWalking();
    entity.getComponent(KeyboardPlayerInputComponent.class).setWalkDirection(this.walkDirection.cpy()); // Update the walking direction
  }

  /**
   * Moves the player towards a given direction.
   *
   * @param direction direction to move in
   */
  void walk(Vector2 direction) {
    this.walkDirection = direction;
    moving = true;
  }

  /**
   * Handles player sprinting
   * @param direction the direction the player is moving in
   * @param sprinting true if the player is beginning a sprint, false otherwise
   */
  void sprint(Vector2 direction, boolean sprinting, int sprintModifier) {
      if (direction.x > 0) {
        //if the player is moving right
        if (sprinting){
          //if sprint was called on keyDown, increase speed
          this.walkDirection.add(Vector2Utils.RIGHT.cpy().scl(sprintModifier-1));
        } else {
          //player has stopped sprinting, subtract speed
          this.walkDirection.sub(Vector2Utils.RIGHT.cpy().scl(sprintModifier-1));
        }
      }
      if (direction.x < 0){
        //if the player is moving left
        if (sprinting){
          this.walkDirection.add(Vector2Utils.LEFT.cpy().scl(sprintModifier-1));
        } else {
          this.walkDirection.sub(Vector2Utils.LEFT.cpy().scl(sprintModifier-1));
        }
      }
  }

  /**
   * Stops the player from walking.
   */
  void stopWalking() {
    this.walkDirection = gravity;
    updateSpeed();
    moving = false;
  }

  /**
   * Makes the player attack.
   */
  void attack() {
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
  }


  /**
   * Upgrades the players level completion progress
   */
  void upgradeProgress() {
    if (walkDirection.x > 0) {
      entity.getComponent(ProgressComponent.class).updateProgress(entity.getPosition().x);
    }
    entity.getComponent(InformPlayerComponent.class).setPosition(entity.getPosition().x);
  }

  /**
   * Upgrades the players level completion progress
   */
  void upgradeScore() {
    entity.getComponent(ScoreComponent.class).updateScore();
  }

}
