package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.ProgressComponent;
import com.deco2800.game.components.SprintComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;


/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  Table table2;
  Table table3;
  private Image heartImage;
  private Image progressBar;
  private Label healthLabel;
  private Label sprintLabel;
  private Label progressLabel;

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();
    entity.getEvents().addListener("updateSprint", this::updateSprintLevelUI);
    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
    entity.getEvents().addListener("updateProgress", this::updatePlayerProgressUI);

  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f).padLeft(5f);

    table2 = new Table();
    table2.top().left();
    table2.setFillParent(true);
    table2.padTop(75f).padLeft(5f);

    table3 = new Table();
    table3.top().left();
    table3.setFillParent(true);
    table3.padTop(110f).padLeft(5f);

    // Heart image
    float heartSideLength = 30f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));
    // Health text
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("Health: %d", health);
    healthLabel = new Label(healthText, skin, "large");

    int sprint = entity.getComponent(SprintComponent.class).getSprint();
    CharSequence sprintText = String.format("Sprint: %d", sprint);
    sprintLabel = new Label(sprintText, skin, "large");

    float position = entity.getComponent(ProgressComponent.class).getProgress();
    CharSequence progressText = String.format("Progress: %.0f %%", position);
    progressLabel = new Label(progressText, skin, "large");
    progressBar = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));
    table.add(heartImage).size(heartSideLength).pad(5);
    table.add(healthLabel).pad(5);
    stage.addActor(table);

    //new table to add sprint label
    //not sure how to add sprint label below health label in same table
    table2.add(sprintLabel).pad(5);
    stage.addActor(table2);

    table3.add(progressLabel);
    stage.addActor(table3);


  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's health on the ui.
   * @param health player health
   */
  public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
    healthLabel.setText(text);
  }

  /**
   * updates the players sprint on the UI
   * @param sprintLevel player's sprint
   */
  public void updateSprintLevelUI(int sprintLevel){
    CharSequence text = String.format("Sprint: %d", sprintLevel);
    sprintLabel.setText(text);
  }

  /**
   * Updates the player's level progress on the UI
   * @param progress player's progress
   */
  public void updatePlayerProgressUI(float progress) {
    CharSequence text = String.format("Progress: %.0f %%", progress);
    progressLabel.setText(text);
  }

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
    sprintLabel.remove();
    progressLabel.remove();
  }
}

