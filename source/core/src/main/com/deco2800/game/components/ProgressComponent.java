package com.deco2800.game.components;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.ui.UIComponent;

/**
 * Component used to store information related to the player's level completion progress.
 */
public class ProgressComponent extends Component {


  private float progress;

  private float position = 0;

  private final float levelSize;



  public ProgressComponent(float position, float levelSize) {
    this.levelSize = levelSize;
    setPosition(position);
    setProgress();
  }

  /**
   * Set's position
   *
   * @param position position to set; if the position is greater than 0 or
   *                  the current position, it will be updated, otherwise it will be reset to 0
   *                 if the position is 0. Will not change otherwise
   */
  public void setPosition(float position) {
    //Will need to create specific case where player has respawned either at 0 or at the level's checkpoint

    if (position > 0 && position >= this.position) {
      this.position = position;
    } else if (position < 0) {
      this.position = 0;

    }

    if (entity != null) {
      entity.getEvents().trigger("updateProgress", this.progress);

    }
  }

  public float getPosition() {
    return position;
  }

  public void setProgress() {

    float percentProgress = (float) Math.round((getPosition() / levelSize)*100);

    if (percentProgress >= 95) {
      this.progress = 100;
    } else {
      this.progress = percentProgress;
    }

  }


  public float getProgress() {
    setProgress();
    return this.progress;
  }

  public void updateProgress(float position) {

    if (position / levelSize < 1) {
      setPosition(position);
    } else {
      setPosition(100);
    }
    setProgress();
  }

  //Class to return to start
  //Class to return to checkpoint

}
