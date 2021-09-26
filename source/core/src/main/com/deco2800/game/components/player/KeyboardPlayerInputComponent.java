package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.components.SprintComponent;
import com.deco2800.game.input.InputComponent;
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
  private boolean noJumping = false; //true if player has picked up a no jump debuff
  private boolean movingRight = false; //true if player is moving right
  private boolean movingLeft = false; //true if player is moving left
  private boolean isStationary = true; //true if player is not moving (Just gravity affecting movement)


  public Timer sprintTimer = new Timer();
  public Timer jumpingTimer = new Timer();
  public Timer fallingTimer = new Timer();

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
        entity.getComponent(PlayerStateComponent.class).manage(isJumping, isSprinting, movingRight, movingLeft, isStationary);
      }
    }
  };

  /** Makes player fall for 1 second */
  public Timer.Task startFalling = new Timer.Task() {
    @Override
    public void run(){
      logger.info("Player is falling from a jump");
      // Subtracts 4 m/s to upwards movement
      walkDirection.sub(0, 4);
      jumpingTimer.stop();
      // Schedules to stop falling and allow user to jump again
      fallingTimer.start();
      fallingTimer.scheduleTask(stopFalling, 1f);
      startFalling.cancel();
    }
  };

  /** Stops falling and allows user to jump again by setting isJumping to false */
  public Timer.Task stopFalling = new Timer.Task() {
    @Override
    public void run() {
      logger.info("Player has stopped falling");
      fallingTimer.stop();
      isJumping = false;
      triggerMovementEvent();
      stopFalling.cancel();
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

  /*private boolean instantDrop() {
    if (isJumping) {
      logger.info("Dropped to the ground");
      for (int i = 0; i < 8; i++) {
        walkDirection.sub(Vector2Utils.UP);
      }
      for (int i = 0; i < 4; i++) {
        walkDirection.add(Vector2Utils.UP);
      }
    }
    return true;
  }*/

  /**
   * After an input of 'A' or 'D' has been detected, decide to move left or right.
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
   * After an input of 'SPACE_BAR' been detected, jump if the player is able
   * to jump.
   *
   * @return true if jump was processed
   */
  private boolean jump(){
    if (canJump()) {
      isJumping = true;

      // Adds 4 m/s to upwards movement
      walkDirection.add(0,4);

      triggerMovementEvent();
      jumpingTimer.start();
      // Schedules to stop jumping and start falling
      jumpingTimer.scheduleTask(startFalling, 0.3f);

    }
    return true;
  }

  /**
   * Returns whether the player can jump based on:
   * - whether they are currently jumping, and
   * - whether they are under the effects of a debuff which disallows them to jump.
   *
   * @return true if the player is able to jump, else false.
   * */
  private boolean canJump() {
    return (!isJumping && !startFalling.isScheduled() &&
            !stopFalling.isScheduled() && !noJumping);
  }

  /** After a walk or jump has been processed, apply the speed and animations to the player. */
  private void triggerMovementEvent() {
    entity.getComponent(PlayerStateComponent.class).manage(isJumping, isSprinting, movingRight, movingLeft, isStationary);
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
    entity.getComponent(PlayerStateComponent.class).manage(isJumping, isSprinting, movingRight, movingLeft, isStationary);
    entity.getEvents().trigger("sprint", walkDirection, sprinting, SPRINT_MODIFIER);
    entity.getEvents().trigger("playerStatusAnimation");
  }

}
