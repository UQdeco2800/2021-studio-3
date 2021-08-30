package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.components.SprintComponent;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.utils.math.Vector2Utils;


/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  //OLD VARIABLE - private final Vector2 walkDirection = Vector2.Zero.cpy();
  public final Vector2 gravity = new Vector2(0, -1f); // Value of gravity on player for comparing
  public final Vector2 walkDirection = new Vector2(0, -1f); // Sets gravity on player
  private boolean isSprinting = false; //true if player is currently sprinting
  private boolean firstSprint = true; //used for starting timer-related stuff
  private boolean isJumping = false; //true if player is jumping

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
          walkDirection.sub(Vector2Utils.RIGHT);
        }
        if (walkDirection.x < -1) {
          walkDirection.sub(Vector2Utils.LEFT);
        }
        sprintTimer.stop();
        isSprinting = false;
      }
    }
  };

  // Makes player fall for 1 second
  public Timer.Task startFalling = new Timer.Task() {
    @Override
    public void run(){
      // Subtracts 4 m/s to upwards movement
      for (int i = 0; i < 4; i++) {
        walkDirection.sub(Vector2Utils.UP);
      }
      jumpingTimer.stop();
      // Schedules to stop falling and allow user to jump again
      fallingTimer.start();
      fallingTimer.scheduleTask(stopFalling, 1f);
      startFalling.cancel();
    }
  };

  // Stops falling and allows user to jump again by setting isJumping to false
  public Timer.Task stopFalling = new Timer.Task() {
    @Override
    public void run(){
      fallingTimer.stop();
      isJumping = false;
      triggerWalkEvent();
      stopFalling.cancel();
    }
  };

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
        if (!isJumping && !startFalling.isScheduled() && !stopFalling.isScheduled()) {
          isJumping = true;
          // Adds 4 m/s to upwards movement
          for (int i = 0; i < 4; i++) {
            walkDirection.add(Vector2Utils.UP);
          }
          triggerWalkEvent();
          jumpingTimer.start();
          // Schedules to stop jumping and start falling
          jumpingTimer.scheduleTask(startFalling, 0.3f);
        }
        return true;
      case Keys.A:
        if (entity.getComponent(SprintComponent.class).getSprint() > 0 && isSprinting) {
          //if sprint is active before moving, add twice the speed
          walkDirection.add(Vector2Utils.LEFT);
          walkDirection.add(Vector2Utils.LEFT);
          triggerWalkEvent();
          return true;
        }
        walkDirection.add(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Keys.D:
        if (entity.getComponent(SprintComponent.class).getSprint() > 0 && isSprinting) {
          //if sprint is active before moving, add twice the speed
          walkDirection.add(Vector2Utils.RIGHT);
          walkDirection.add(Vector2Utils.RIGHT);
          triggerWalkEvent();
          return true;
        }
        walkDirection.add(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      case Keys.SHIFT_LEFT:
        // Cannot be jumping and sprinting at the same time
        //if (!isJumping) {
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
        //}
        return true;
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
        walkDirection.sub(Vector2Utils.LEFT);
        //if sprinting, remove additional speed on key up
        if (entity.getComponent(SprintComponent.class).getSprint() > 0 && isSprinting) {
          walkDirection.sub(Vector2Utils.LEFT);
        }
        triggerWalkEvent();
        return true;
      case Keys.D:
        walkDirection.sub(Vector2Utils.RIGHT);
        //if sprinting, remove additional speed on key up
        if (entity.getComponent(SprintComponent.class).getSprint() > 0 && isSprinting) {
          walkDirection.sub(Vector2Utils.RIGHT);
        }
        triggerWalkEvent();
        return true;
      case Keys.SHIFT_LEFT:
        //if (removeSprint.isScheduled()) {
        sprintTimer.stop();
        isSprinting = false;
        triggerSprintEvent(false);
        //}
        return true;
      default:
        return false;
    }
  }

  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(gravity) && !isJumping) {
      entity.getEvents().trigger("stopWalkAnimation");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
      entity.getEvents().trigger("startWalkAnimation");
    }
  }

  private void triggerSprintEvent(boolean sprinting) {
    if (entity.getComponent(SprintComponent.class).getSprint() == 0) {
      return;
    }
    // Cannot be jumping and sprinting at the same time
    //if (!isJumping) {
      entity.getEvents().trigger("sprint", walkDirection, sprinting);
    //}
  }
}


