package com.deco2800.game.components.player;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.deco2800.game.components.*;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

import java.util.Collection;


/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {

  private Table scoreTable;
  private Table sprintTable;
  private Table progressTable;
  private Table livesTable;
  private Table buffTable;

  private Image levelStatus;

  private Label scoreLabel;
  private Label sprintLabel;
  private Label progressLabel;
  private Label livesLabel;

  private Texture level10percent;
  private Texture level20percent;
  private Texture level30percent;
  private Texture level40percent;
  private Texture level50percent;
  private Texture level60percent;
  private Texture level70percent;
  private Texture level80percent;
  private Texture level90percent;
  private Texture levelComplete;

  AssetManager manager;
  TextureRegion  textureRegion;

  /* Buff-related UI elements */
  private CharSequence buffText;
  private Label buffLabel;

  /**
   * Defines potential positioning for Tables holding UI elements to be
   * displayed.
   * */
  public enum UIPosition {
    LEFT, CENTRE, RIGHT
  }

  public PlayerStatsDisplay(AssetManager manager,TextureRegion textureRegion){
    this.manager = manager;
    this.textureRegion = textureRegion;
  }

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();
    entity.getEvents().addListener("updateSprint", this::updateSprintLevelUI);
    entity.getEvents().addListener("updateProgress", this::updatePlayerProgressUI);
    entity.getEvents().addListener("updateScore", this::updateScoreUI);
    entity.getEvents().addListener("updateLives", this::updateLivesUI);
  }

  /**
   * Handles setting up a new UI Table to hold elements to be displayed on the
   * main game UI.
   *
   * @param topPadding the amount of padding to put above the table.
   * @param sidePadding the amount of padding to put to the side of the table.
   *                    The side the padding is on is chosen based on the side
   *                    of the screen the table is on (LEFT or RIGHT)
   * @param position the horizontal positioning of the table, either LEFT,
   *                 RIGHT or CENTRE.
   *
   * @return an empty table in the specified position with the specified
   *         padding.
   * */
  private Table setupUITable(float topPadding, float sidePadding,
          UIPosition position) {
    Table temporaryTable = new Table();

    /* Table positioning */
    switch (position) {
      case LEFT:
        temporaryTable.top().left();
        break;
      case RIGHT:
        temporaryTable.top().right();
        break;
      case CENTRE:
        temporaryTable.top();
        break;
    }

    temporaryTable.setFillParent(true);

    /* Table padding */
    switch (position) {
      case LEFT:
        temporaryTable.padTop(topPadding).padLeft(sidePadding);
        return temporaryTable;
      case RIGHT:
        temporaryTable.padTop(topPadding).padRight(sidePadding);
        return temporaryTable;
      case CENTRE:
        temporaryTable.padTop(topPadding);
        return temporaryTable;
    }

    return temporaryTable;
  }

  /**
   * Creates actors and positions them on the stage using a healthTable.
   * @see Table for positioning options
   */
  private void addActors() {
    /* Tables for UI Elements */
    livesTable = setupUITable(25f, 5f, UIPosition.LEFT);
    sprintTable = setupUITable(60f, 5f, UIPosition.LEFT);
    scoreTable = setupUITable(60f, 10f, UIPosition.CENTRE);
    progressTable = setupUITable(25f, 0f, UIPosition.CENTRE);
    buffTable = setupUITable(95f, 5f, UIPosition.CENTRE);

    /* Images for UI */

    Image heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));
    Image livesImage = new Image(ServiceLocator.getResourceService().getAsset("images/lives_icon2.png", Texture.class));

    // Sprint Text
    int sprint = entity.getComponent(SprintComponent.class).getSprint();
    CharSequence sprintText = String.format("Sprint: %d", sprint);
    sprintLabel = new Label(sprintText, skin, "font", "white");

    // Score Text
    int score = entity.getComponent(ScoreComponent.class).getScore();
    CharSequence scoreText = String.format("score: %d", score);
    scoreLabel = new Label(scoreText, skin, "font_large", "white");

    // Buff / Debuff Text
    buffText = "Current buffs: \n";
    buffLabel = new Label(buffText, skin, "font", "gray");
    buffTable.add(buffLabel);

    // Progress Text
    float progress = entity.getComponent(ProgressComponent.class).getProgress();
    CharSequence progressText = String.format("%.0f %%", progress);
    progressLabel = new Label(progressText, skin, "font_large", "white");

    // Lives Text
    int lives = entity.getComponent(LivesComponent.class).getLives();
    CharSequence livesText = String.format("x%d", lives);
    livesLabel = new Label(livesText, skin, "font", "white");

    // Create textures to be changed on update
    Texture levelStart = new Texture("images/0percent.png");
    level10percent = new Texture("images/10percent.png");
    level20percent = new Texture("images/20percent.png");
    level30percent = new Texture("images/30percent.png");
    level40percent = new Texture("images/40percent.png");
    level50percent = new Texture("images/50percent.png");
    level60percent = new Texture("images/60percent.png");
    level70percent = new Texture("images/70percent.png");
    level80percent = new Texture("images/80percent.png");
    level90percent = new Texture("images/90percent.png");
    levelComplete = new Texture("images/100percent.png");

    // Adding elements to each table, subsequently adding them to the stage
    sprintTable.add(sprintLabel).pad(5);
    scoreTable.add(scoreLabel).pad(5);
    levelStatus = new Image(levelStart);
    progressTable.add(levelStatus).width(850).height(40).padRight(10f);
    progressTable.add(progressLabel);
    livesTable.add(livesImage).size(40f).pad(5);
    livesTable.add(livesLabel);

    // Adding tables to stages
    stage.addActor(sprintTable);
    stage.addActor(progressTable);
    stage.addActor(livesTable);
    stage.addActor(scoreTable);
    stage.addActor(buffTable);
  }

    /**
     * Returns the new Health Bar image, corresponding to the player's current
     * health.
     *
     * @return the filepath to the new image to use for the health bar
     * */
    public String getHealthBarImage(double currentHealthPercentage) {
      /* Switch is using int one-off decimal representation.
         '9' is 91-99, '8' is 81-89, etc. */
      switch ((int) currentHealthPercentage) {
        case 10:  // Same behaviour as 9
        case 9:
          return "images/100.png";
        case 8:
          return "images/90.png";
        case 7:
          return "images/80.png";
        case 6:
          return "images/70.png";
        case 5:
          return "images/60.png";
        case 4:
          return "images/50.png";
        case 3:
          return "images/40.png";
        case 2:
          return "images/30.png";
        case 1:
          return "images/20.png";
        case 0:
          // Health could be 0 to 9 inclusive
          double newHealth = currentHealthPercentage * 10;

          // If it's 0, return so, else return the 1-9 image.
          return (newHealth == 0) ? "images/00.png" : "images/10.png";
      }
      return "images/00.png"; // Unreachable
    }

    /**
     * Handles updating the Health Bar display. The image representing the
     * entity's current health is retrieved and placed above the player.
     *
     * @param currentHealth the players' current health, as a percentage.
     * */
    private void updateHealthBarDisplay(double currentHealth, SpriteBatch batch) {
      /* Change percentage to decimal, 1 place off. */
      double healthPercentage = (currentHealth * 10);

      /* Get the new texture */
      String healthImage = getHealthBarImage(healthPercentage);

      /* Update the display */
      textureRegion.setTexture(manager.get(healthImage, Texture.class));
      batch.draw(textureRegion,entity.getPosition().x - 1,
              entity.getPosition().y + 1);
    }


    /**
     * Handles drawing the health bar.
     * */
    @Override
    public void draw(SpriteBatch batch)  {
      double health = entity.getComponent(CombatStatsComponent.class).getHealth();
      double healthPercentage = health / entity.getComponent(CombatStatsComponent.class).getMaxHealth();
      updateHealthBarDisplay(healthPercentage, batch);
    }


    /**
     * Returns the position of the current entity.
     *
     * @return Vector2 representation of the entity's position.
     * */
    public Vector2 getPlayerPosition() {
      return entity.getPosition();
    }

  /**
   * Updates the players' currently active timed buffs and debuffs on the UI.
   *
   * @param buffInfo a collection of BuffInfo's for the currently active timed
   *                 buffs. Gives the UI access to the name of the buff.
   * */
  public void updateBuffDisplay(Collection<BuffInformation> buffInfo) {
    String text = ((String) this.buffText);

    /* Add the names & time remaining of currently active buffs */
    for (BuffInformation info : buffInfo) {
      String buffName = info.getBuffName();
      double timeRemaining = Math.ceil(info.getTimeLeft() * 0.001);

      text = text.concat(buffName + " " + (int) timeRemaining + "..." + "\n");
    }

    /* Update */
    buffLabel.setText(text);
  }

  /**
   * updates the players sprint on the UI
   * @param sprintLevel player's sprint
   */
  public void updateSprintLevelUI(int sprintLevel) {
    CharSequence text = String.format("Sprint: %d", sprintLevel);
    sprintLabel.setText(text);
  }

  /**
   * Updates the player's level progress on the UI
   * @param progress player's progress
   */
  public void updatePlayerProgressUI(float progress) {

    if (progress % 10 == 0 && progress > 0) {
      CharSequence text = String.format("%.0f %%", progress);
      progressLabel.setText(text);

      //Switch statement changes the levelStatus image based on the progress
      switch ((int) progress) {
        case 10:
          levelStatus.setDrawable(new SpriteDrawable(new Sprite(level10percent)));
          break;
        case 20:
          levelStatus.setDrawable(new SpriteDrawable(new Sprite(level20percent)));
          break;
        case 30:
          levelStatus.setDrawable(new SpriteDrawable(new Sprite(level30percent)));
          break;
        case 40:
          levelStatus.setDrawable(new SpriteDrawable(new Sprite(level40percent)));
          break;
        case 50:
          levelStatus.setDrawable(new SpriteDrawable(new Sprite(level50percent)));
          break;
        case 60:
          levelStatus.setDrawable(new SpriteDrawable(new Sprite(level60percent)));
          break;
        case 70:
          levelStatus.setDrawable(new SpriteDrawable(new Sprite(level70percent)));
          break;
        case 80:
          levelStatus.setDrawable(new SpriteDrawable(new Sprite(level80percent)));
          break;
        case 90:
          levelStatus.setDrawable(new SpriteDrawable(new Sprite(level90percent)));
          break;
        case 100:
          levelStatus.setDrawable(new SpriteDrawable(new Sprite(levelComplete)));
          break;
      }
    }
  }

  /**
   * Updates the player's score on the ui.
   * @param score player score
   */
  public void updateScoreUI(int score) {
    CharSequence text = String.format("Score: %d", score);
    scoreLabel.setText(text);
  }

  /**
   * Updates the player's score on the ui.
   * @param lives player lives
   */
  public void updateLivesUI(int lives) {
    CharSequence text = String.format("x%d", lives);
    livesLabel.setText(text);
  }

  @Override
  public void dispose() {
    super.dispose();
    sprintLabel.remove();
    progressLabel.remove();
    scoreLabel.remove();
  }
}
