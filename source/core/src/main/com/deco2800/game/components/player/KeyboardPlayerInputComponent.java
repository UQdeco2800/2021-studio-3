package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.components.SprintComponent;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {

  /**
   * SPRINT MODIFIER - This can be changed (when buffed, etc.)
   * Change this value to control how fast the player moves whilst under the effect of sprint
   */
  public static int SPRINT_MODIFIER = 2;

  public final Vector2 gravity = new Vector2(0, -1f); // Value of gravity on player for comparing
  public final Vector2 walkDirection = new Vector2(0, -1f); // Sets gravity on player

  private static final Logger logger = LoggerFactory.getLogger(KeyboardPlayerInputComponent.class);

  private boolean isSprinting = false; //true if player is currently sprinting
  private boolean firstSprint = true; //used for starting timer-related stuff
  private boolean isJumping = false; //true if player is jumping
  private boolean noJumping = false;

  public Timer sprintTimer = new Timer();

  /**
   * Handles jump-related behaviour every frame.
   *
   * @see DoubleJumpComponent#checkJumpOnUpdate()
   * */
  @Override
  public void update() {
    entity.getComponent(DoubleJumpComponent.class).checkJumpOnUpdate();
  }

  /**
   * Handles the player falling after their total JUMP_TIME is up. The player's
   * jumping-related variables and new state are set.
   *
   * JUMP_TIME is defined in the DoubleJumpComponent class.
   *
   * @see DoubleJumpComponent
   * */
  public void handleFalling() {
    // Subtracts 4 m/s to upwards movement
    applyMovement(Vector2Utils.DOWN);

    isJumping = false;
    entity.getComponent(DoubleJumpComponent.class).setIsJumping(isJumping);
    entity.getComponent(PlayerStateComponent.class).manage(isJumping, isSprinting);
    triggerMovementEvent();
  }

  /**
   * Applies change in players' movement direction when jumping or falling.
   *
   * @param direction the new direction for the player to go in.
   * */
  private void applyMovement(Vector2 direction) {
    int height = entity.getComponent(DoubleJumpComponent.class).JUMP_HEIGHT;

    for (int i = 0; i < height; i++) {
      walkDirection.add(direction);
    }
  }

  public Timer.Task removeSprint = new Timer.Task() {

    @Override
    public void run() {
      entity.getComponent(SprintComponent.class).removeSprint(1);
      if (entity.getComponent(SprintComponent.class).getSprint() == 0) {
        //if sprint has fully depleted
        if (walkDirection.x > 1) {
          walkDirection.sub(Vector2Utils.RIGHT.cpy().scl(SPRINT_MODIFIER - 1));
        }
        if (walkDirection.x < -1) {
          walkDirection.sub(Vector2Utils.LEFT.cpy().scl(SPRINT_MODIFIER - 1));
        }
        sprintTimer.stop();
        isSprinting = false;
        entity.getComponent(PlayerStateComponent.class).manage(isJumping, isSprinting);
      }
    }
  };

  /**
   * Sets whether or not the player is under the effects of a No Jumping debuff
   *
   * @param noJumping whether or not the player is able to jump.
   * */
  public void setNoJumping(boolean noJumping) {
    this.noJumping = noJumping;
  }

  /**
   * Returns whether or not the player is currently jumping
   *
   * @return true if the player is currently jumping, else false.
   * */
  public boolean getIsJumping() {
    return this.isJumping;
  }

  public KeyboardPlayerInputComponent() {
    super(5);
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Keys.SPACE:
        return jump();
      case Keys.A:
        return handleWalk('A', "DOWN");
      case Keys.D:
        return handleWalk('D', "DOWN");
      case Keys.SHIFT_LEFT:
          return handleSprint(true);
      default:
        return false;
    }
  }
  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyUp(int)
   */
  @Override
  public boolean keyUp(int keycode) {
    switch (keycode) {
      case Keys.A:
        return handleWalk('A', "UP");
      case Keys.D:
        return handleWalk('D', "UP");
      case Keys.SHIFT_LEFT:
        return handleSprint(false);
      default:
        return false;
    }
  }

  /**
   * After an input of 'A' or 'D' has been detected, decide to move left or right.
   *
   * @return true if walk was processed
   */
  private boolean handleWalk(char Key, String keyState){
    Vector2 direction = Key == 'A' ? Vector2Utils.LEFT : Vector2Utils.RIGHT;
    int scalar = entity.getComponent(SprintComponent.class).getSprint() > 0 && isSprinting ? SPRINT_MODIFIER : 1;
    if (scalar == 1){
      entity.getComponent(PlayerStateComponent.class).updateState(State.WALK);
    }
    if (keyState.equals("DOWN")){
      //on KeyDown
      walkDirection.add(direction.cpy().scl(scalar));
    } else {
      //on KeyUp
      walkDirection.sub(direction.cpy().scl(scalar));
      entity.getComponent(PlayerStateComponent.class).updateState(State.STATIONARY);
    }
    triggerMovementEvent();
    return true;
  }

  /**
   * After an input of 'LEFT_SHIFT' has been detected, decide which way to apply sprint to if sprint is left
   *
   * @return true if sprint was processed
   */
  private boolean handleSprint(boolean keyDown){
    if (keyDown){
      if (entity.getComponent(SprintComponent.class).getSprint() == 0) {
        return true;
      }
      sprintTimer.start();
      if (firstSprint) {
        firstSprint = false;
        sprintTimer.scheduleTask(removeSprint, 0.1f, 0.03f);
      }
      triggerSprintEvent(true);
      isSprinting = true;
      entity.getComponent(PlayerStateComponent.class).manage(isJumping, isSprinting);
      return true;
    }
    sprintTimer.stop();
    isSprinting = false;
    entity.getComponent(PlayerStateComponent.class).manage(isJumping, isSprinting);
    triggerSprintEvent(false);
    return true;
  }

  /**
   * Handles setup and performance of player jumping when the SPACE_BAR is hit.
   * The player's jumping-related variables and new state are set.
   *
   * @return true when the jump is processed.
   */
  private boolean jump() {
    if (canJump()) {
      isJumping = true;
      entity.getComponent(DoubleJumpComponent.class).setIsJumping(isJumping);
      entity.getComponent(DoubleJumpComponent.class).nextJumpState();
      entity.getComponent(PlayerStateComponent.class).manage(isJumping, isSprinting);
      performJump();
    }
    return true;
  }

  /**
   * Performs the player jump movement and begins timing to handle player
   * descent. The duration of a players' jump is defined in the
   * DoubleJumpComponent class.
   *
   * @see DoubleJumpComponent
   * */
  private void performJump() {
    // Adds 4 m/s to upwards movement
    applyMovement(Vector2Utils.UP);
    triggerMovementEvent();

    // Begin timing
    long time = ServiceLocator.getTimeSource().getTime();
    entity.getComponent(DoubleJumpComponent.class).setJumpStartTime(time);
  }

  /**
   * Returns whether or not the player can jump based on:
   * - whether they are currently jumping,
   * - whether they are currently double jumping, and
   * - whether they are under the effects of a debuff which disallows them to
   *   jump.
   *
   * @return true if the player is able to jump, else false.
   * */
  private boolean canJump() {
    return (!isJumping && !noJumping &&
            entity.getComponent(DoubleJumpComponent.class).notDoubleJumping());
  }

  /** After a walk or jump has been processed, apply the speed and animations to the player. */
  private void triggerMovementEvent() {
    entity.getEvents().trigger("walk", walkDirection);
    entity.getEvents().trigger("playerStatusAnimation");
  }

  /**
   * After a sprint has been processed, apply the sprinting speed and animations to the player
   *
   * @param sprinting: true if the player is sprinting
   */
  private void triggerSprintEvent(boolean sprinting) {
    if (entity.getComponent(SprintComponent.class).getSprint() == 0) {
      return;
    }
    entity.getEvents().trigger("sprint", walkDirection, sprinting, SPRINT_MODIFIER);
    entity.getEvents().trigger("playerStatusAnimation");
  }

}
