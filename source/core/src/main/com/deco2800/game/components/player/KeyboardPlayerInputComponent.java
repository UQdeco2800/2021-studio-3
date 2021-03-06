package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.components.CombatStatsComponent;
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
   * Change this value to control how fast the player moves whilst under the
   * effect of sprint
   */
  public static int SPRINT_MODIFIER = 2;

  public final Vector2 gravity = new Vector2(0, -1f); // Value of gravity on player for comparing
  public Vector2 walkDirection = new Vector2(0, -1f); // Sets gravity on player

  private static final Logger logger = LoggerFactory.getLogger(KeyboardPlayerInputComponent.class);

  /* Player movement-state variables */

  // Sprinting and walking
  private boolean isSprinting = false; // true if player is currently sprinting
  private boolean firstSprint = true; // used for starting timer-related stuff
  private boolean movingRight = false; // true if player is moving right
  private boolean movingLeft = false; // true if player is moving left
  private boolean isStationary = true; // true if player is not moving (Just gravity affecting movement)

  // Jumping
  private boolean isJumping = false; // true if player is jumping

  // Rolling
  private boolean isRolling = false; // true if the player is rolling
  private boolean processingRoll = false; // true if a roll is currently being processed
  private boolean onRollWalked = false; // true if the player attempted to walk while mid-roll

  public Timer sprintTimer = new Timer();

  /**
   * Handles jump-related behaviour every frame. Ensures the player loses if
   * they fall off the map.
   *
   * @see DoubleJumpComponent#checkJumpOnUpdate()
   * */
  @Override
  public void update() {
    entity.getComponent(DoubleJumpComponent.class).checkJumpOnUpdate();
    // The player loses if they fall off the map
    if (entity.getCenterPosition().y < 0.5) {
      entity.getComponent(CombatStatsComponent.class).setHealth(0);
    }
  }

  /**
   * Sets the player's walking direction, for usage in calls within this class.
   *
   * @param newDirection the new direction the player is moving.
   * */
  public void setWalkDirection(Vector2 newDirection) {
    this.walkDirection = newDirection;
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
    entity.getComponent(PlayerStateComponent.class).manage(isJumping,
            isSprinting, movingRight, movingLeft, isStationary);
    triggerMovementEvent();
  }

  /**
   * Applies change in players' movement direction when jumping or falling.
   *
   * @param direction the new direction for the player to go in.
   * */
  public void applyMovement(Vector2 direction) {
    int height = entity.getComponent(DoubleJumpComponent.class).JUMP_HEIGHT;

    for (int i = 0; i < height; i++) {
      walkDirection.add(direction);
    }
  }

  public Timer.Task removeSprint = new Timer.Task() {
    @Override
    public void run() {
      if (!isStationary) {
        entity.getComponent(SprintComponent.class).removeSprint(1);
      }
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
        entity.getComponent(PlayerStateComponent.class).manage(isJumping,
                isSprinting, movingRight, movingLeft, isStationary);
      }
    }
  };


  /**
   * Sets the players 'rolling' status to the status passed in.
   *
   * @param rolling true if the player is rolling, else false.
   * */
  public void setRolling(boolean rolling) {
    this.isRolling = rolling;
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
   * Checks if a walk input was entered while the player was mid-roll. If the
   * player tries to walk while mid-roll, the onRollWalked variable ensures the
   * movements don't clash.
   * */
  private void checkOnRollWalked() {
    if ((processingRoll | isRolling)) {
      this.onRollWalked = true;
    }
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
        // The player can only walk if they are not currently rolling
        checkOnRollWalked();
        return (processingRoll | isRolling) || handleWalk('A', "DOWN");
      case Keys.D:
        checkOnRollWalked();
        return (processingRoll | isRolling) || handleWalk('D', "DOWN");
      case Keys.SHIFT_LEFT:
          return handleSprint(true);
      case Keys.Q:
        // The player can only roll if they're not currently walking or rolling
        return handleRollKeyInput(Vector2Utils.LEFT);
      case Keys.E:
        return handleRollKeyInput(Vector2Utils.RIGHT);
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
        return handleWalkKeyInput('A', "UP");
      case Keys.D:
        return handleWalkKeyInput('D', "UP");
      case Keys.SHIFT_LEFT:
        return handleSprint(false);
      default:
        return false;
    }
  }

  /**
   * Handles the key input when a player attempts to roll left (Q) or right
   * (E).
   *
   * The player can't roll if they are currently rolling, processing a
   * roll or moving. If these conditions are met, the roll is handled.
   *
   * @param direction the direction the player rolls in.
   * @return true to indicate that the input is processed.
   * */
  private boolean handleRollKeyInput(Vector2 direction) {
    if (processingRoll | isRolling) {
      return true;
    }
    return !isStationary || setupRoll(direction);
  }

  /**
   * Ensures that if the player tried to walk while mid-roll, the player does
   * not attempt to 'un-walk' when the roll ends (ie, walk backwards)
   *
   * @param key the key that was pressed (A or D)
   * @param keyState whether the key was released or pressed. Currently, this
   *                 function only handles release, ie "UP".
   * */
  private boolean handleWalkKeyInput(char key, String keyState) {
    if (onRollWalked) {
      this.onRollWalked = false;
      return true;
    }
    return (processingRoll | isRolling) || handleWalk(key, keyState);
  }

  /**
   * Handles the player rolling. Ensures that the player only rolls if they
   * are not mid-jump, and triggers the roll event for the given direction.
   *
   * @param direction the direction the player rolled in
   * @return true when the input is processed.
   * */
  private boolean setupRoll(Vector2 direction) {
    processingRoll = true;
    if (entity.getComponent(DoubleJumpComponent.class).isLanded()) {
      isRolling = true;
      entity.getEvents().trigger("roll", direction);
    }
    processingRoll = false;
    return true;
  }


  /**
   * After an input of 'A' or 'D' has been detected, decide to move left or
   * right.
   *
   * @return true if walk was processed
   */
  private boolean handleWalk(char Key, String keyState){
    Vector2 direction = Key == 'A' ? Vector2Utils.LEFT : Vector2Utils.RIGHT;
    int scalar = entity.getComponent(SprintComponent.class).getSprint() > 0 && isSprinting ? SPRINT_MODIFIER : 1;
    if (scalar == 1) {
      entity.getComponent(PlayerStateComponent.class).updateState(State.WALK);
    }
    // Updates the player state direction for which way they are moving
    if (keyState.equals("DOWN")){
      if (Key == 'A') {
        movingLeft = true;
      } else if (Key == 'D') {
        movingRight = true;
      }
      isStationary = false;
      walkDirection.add(direction.cpy().scl(scalar));
    } else {
      if (keyState.equals("UP")) {
        if (Key == 'A') {
          movingLeft = false;
        } else if (Key == 'D') {
          movingRight = false;
        }
      }
      //on KeyUp
      walkDirection.sub(direction.cpy().scl(scalar));
      if (!movingRight && !movingLeft){
        isStationary = true;
      }
    }
    triggerMovementEvent();
    return true;
  }

  /**
   * After an input of 'LEFT_SHIFT' has been detected, decide which way to
   * apply sprint to if sprint is left
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
      isSprinting = true;
      triggerSprintEvent(true);
      return true;
    }
    sprintTimer.stop();
    isSprinting = false;
    triggerSprintEvent(false);
    return true;
  }

  /**
   * Determines the players current state based on several state-tracking
   * variables, and updates their physical representation to reflect that.
   * */
  public void updatePlayerStateAnimation() {
    entity.getComponent(PlayerStateComponent.class).manage(isJumping,
            isSprinting, movingRight, movingLeft, isStationary);
    entity.getEvents().trigger("playerStatusAnimation");
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
      entity.getComponent(PlayerStateComponent.class).manage(isJumping,
              isSprinting, movingRight, movingLeft, isStationary);
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
    return (!isJumping && !isRolling &&
            entity.getComponent(DoubleJumpComponent.class).notDoubleJumping());
  }

  /**
   * After a walk or jump has been processed, apply the speed and animations
   * to the player.
   */
  private void triggerMovementEvent() {
    entity.getComponent(PlayerStateComponent.class).manage(isJumping,
            isSprinting, movingRight, movingLeft, isStationary);
    entity.getEvents().trigger("walk", walkDirection);
    entity.getEvents().trigger("playerStatusAnimation");
  }

  /**
   * After a sprint has been processed, apply the sprinting speed and
   * animations to the player
   *
   * @param sprinting: true if the player is sprinting
   */
  private void triggerSprintEvent(boolean sprinting) {
    if (entity.getComponent(SprintComponent.class).getSprint() == 0) {
      return;
    }
    entity.getComponent(PlayerStateComponent.class).manage(isJumping,
            isSprinting, movingRight, movingLeft, isStationary);
    entity.getEvents().trigger("sprint", walkDirection, sprinting,
            SPRINT_MODIFIER);
    entity.getEvents().trigger("playerStatusAnimation");
  }

}
