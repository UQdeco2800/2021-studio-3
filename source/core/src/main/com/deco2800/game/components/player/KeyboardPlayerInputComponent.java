package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;


import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.components.ProgressComponent;
import com.deco2800.game.components.SprintComponent;
import com.deco2800.game.components.maingame.MainGameActions;
import com.deco2800.game.components.maingame.MainGameActions;import com.deco2800.game.components.SprintComponent;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {


  //OLD VARIABLE - private final Vector2 walkDirection = Vector2.Zero.cpy();
  public final Vector2 gravity = new Vector2(0, -1f); // Value of gravity on player for comparing
  public final Vector2 walkDirection = new Vector2(0, -1f); // Sets gravity on player

  //private final Vector2 walkDirection = Vector2.Zero.cpy();
  private static final Logger logger = LoggerFactory.getLogger(KeyboardPlayerInputComponent.class);


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
        return jump();
      case Keys.A:
        return handleKey('A', "DOWN");
      case Keys.D:
        return handleKey('D', "DOWN");
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
        return handleKey('A', "UP");
      case Keys.D:
        return handleKey('D', "UP");
      case Keys.SHIFT_LEFT:
        return handleSprint(false);
      default:
        return false;
    }
  }

  private boolean handleKey(char Key, String keyState){
    Vector2 direction = Key == 'A' ? Vector2Utils.LEFT : Vector2Utils.RIGHT;
    //on KeyDown
    if (keyState.equals("DOWN")){
      walkDirection.add(direction);
      if (entity.getComponent(SprintComponent.class).getSprint() > 0 && isSprinting){
        walkDirection.add(direction);
      }
    } else {
      //on KeyUp
      walkDirection.sub(direction);
      if (entity.getComponent(SprintComponent.class).getSprint() > 0 && isSprinting) {
        walkDirection.sub(direction);
      }
    }
    triggerWalkEvent();
    return true;
  }

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
      return true;
    }
    sprintTimer.stop();
    isSprinting = false;
    triggerSprintEvent(false);
    return true;
  }

  private boolean jump(){
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
    entity.getEvents().trigger("sprint", walkDirection, sprinting);
  }
}
