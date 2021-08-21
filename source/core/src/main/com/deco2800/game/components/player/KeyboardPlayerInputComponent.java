package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.utils.math.Vector2Utils;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean sprinting = false;


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
      case Keys.A:
        if (sprinting){
          walkDirection.add(Vector2Utils.LEFT);
          walkDirection.add(Vector2Utils.LEFT);
          triggerWalkEvent();
          return true;
        }
        walkDirection.add(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Keys.D:
        if (sprinting){
          walkDirection.add(Vector2Utils.RIGHT);
          walkDirection.add(Vector2Utils.RIGHT);
          triggerWalkEvent();
          return true;
        }
        walkDirection.add(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      case Keys.SHIFT_LEFT:
        triggerSprintEvent(true);
        sprinting = true;
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
        if (sprinting){
          walkDirection.sub(Vector2Utils.LEFT);
        }
        triggerWalkEvent();
        return true;
      case Keys.D:
        walkDirection.sub(Vector2Utils.RIGHT);
        if (sprinting){
          walkDirection.sub(Vector2Utils.RIGHT);
        }
        triggerWalkEvent();
        return true;
      case Keys.SHIFT_LEFT:
        triggerSprintEvent(false);
        sprinting = false;
        return true;
      default:
        return false;
    }
  }

  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }

  private void triggerSprintEvent(boolean sprinting) {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("sprint", walkDirection, sprinting);
    }
  }
}
