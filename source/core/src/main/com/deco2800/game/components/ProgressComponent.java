package com.deco2800.game.components;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.components.gamearea.GameAreaDisplay;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class ProgressComponent extends Component {

  private float progress;
  private float position = 0;

  public ProgressComponent(float position) {
    setPosition(position);
    setProgress();
  }
  public void setPosition(float position) {
    //Will need to create specific case where player has respawned either at 0 or at the level's checkpoint

    if (position > 0 && position >= this.position) {
      this.position = position;
    } else if (position < 0) {
      this.position = 0;
    }

    if (entity != null) {
      entity.getEvents().trigger("updateProgress", this.position);
    }
  }

  public float getPosition() {
    return position;
  }

  public void setProgress() {

    int levelSize = 14;
    float percentProgress = (float) Math.round((getPosition() / levelSize) * 100);

    if (percentProgress > 100) {
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
    if (position < 100) {
      setPosition(position);
    } else {
      setPosition(100);
    }
    setProgress();
  }

  //Class to return to start
  //Class to return to checkpoint

}
