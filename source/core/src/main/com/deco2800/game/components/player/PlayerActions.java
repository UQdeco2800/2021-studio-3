package com.deco2800.game.components.player;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.*;
import com.deco2800.game.components.maingame.MainGameActions;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import com.sun.tools.javac.Main;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second
  private PhysicsComponent physicsComponent;
  // OLD VARIABLE - private Vector2 walkDirection = Vector2.Zero.cpy();
  // Sets gravity to 1 m/s for comparing
  private static final Vector2 gravity = new Vector2 (0, -1f);
  // Sets player movement and adds gravity of 1 m/s
  private Vector2 walkDirection = new Vector2 (0, -1f);
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
  void sprint(Vector2 direction, boolean sprinting){
      if (direction.x > 0){
        //if the player is moving right
        if (sprinting){
          //if sprint was called on keyDown, increase speed
          this.walkDirection.add(Vector2Utils.RIGHT);
        } else {
          //player has stopped sprinting, subtract speed
          this.walkDirection.sub(Vector2Utils.RIGHT);
        }
      }
      if (direction.x < 0){
        //if the player is moving left
        if (sprinting){
          this.walkDirection.add(Vector2Utils.LEFT);
        } else {
          this.walkDirection.sub(Vector2Utils.LEFT);
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
  }

  /**
   * Upgrades the players level completion progress
   */
  void upgradeScore() {
    entity.getComponent(ScoreComponent.class).updateScore();
  }

}
