package com.deco2800.game.components;


/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class ProgressComponent extends Component {

  private int progress;
  private float position;
  private int levelSize;

  public ProgressComponent(float position) {
    setPosition(position);
    setProgress();
  }
  public void setPosition(float newPosition) {
    //Will need to create specific case where player has respawned either at 0 or at the level's checkpoint

    position = newPosition*0;
    /*if (newPosition > 0 && newPosition >= this.position) {
      position = newPosition;
    } else if (newPosition < 0) {
      position = 0;
    }*/
  }

  public float getPosition() {
    return position;
  }

  public void setProgress() {
    this.progress = (int) (getPosition()/levelSize) * 100;
  }

  public int getProgress() {
    setProgress();
    return this.progress;
  }

  //Class to return to start
  //Class to return to checkpoint

}
