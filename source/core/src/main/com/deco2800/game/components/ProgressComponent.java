package com.deco2800.game.components;

/**
 * Component used to store information related to the player's level completion progress.
 */
public class ProgressComponent extends Component {


  /**variable defining player progress as a percentage*/
  private float progress;

  /**variable defining player position (initially 0)*/
  private float position = 0;

  /**variable used for level size*/
  private final float levelSize;


  /**
   * Constructor class for the Progress Component. Takes in a position and a level size that is determined
   * by the game terrain.
   * @param position position of the player
   * @param levelSize size of the level terrain
   */
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

    if (position > 0 && position/levelSize <= 1 && position >= this.position) {
      this.position = position;
    } else if (position < 0) {
      this.position = 0;
    } else if (position == levelSize) {
        this.position = levelSize;
    }
  }

  /**
   * Getter method for the player position.
   * @return the position of the player
   */
  public float getPosition() {
    return position;
  }

  /**
   * Sets the new progress on the UI once the updateProgress event has been triggered. This occurs
   * only if the entity has been created (i.e. it is not null)
    */
  public void setProgress() {

    float percentProgress = (float) Math.round((getPosition() / levelSize)*100);

    if (percentProgress >= 95) {
      this.progress = 100;
    } else {
      this.progress = percentProgress;
    }

    if (entity != null) {
      entity.getEvents().trigger("updateProgress", this.progress);

    }
  }


  /**
   * Getter method for the progress.
   * @return the progress to be displayed on the UI.
   */
  public float getProgress() {
    setProgress();
    return this.progress;
  }

  /**
   *  function to determine the player's new progress based on an input updated on player movement.
   * @param position new player position used to update the progress.
   */
  public void updateProgress(float position) {

    if (position / levelSize < 1) {
      setPosition(position);
    } else {
      setPosition(levelSize);
    }
    setProgress();
  }

  //Class to return to start
  //Class to return to checkpoint

}
