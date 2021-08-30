package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.ProgressComponent;
import com.deco2800.game.components.SprintComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.w3c.dom.Text;


/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  Table table2;
  Table table3;
  private Image heartImage;
  private Label healthLabel;
  private Label sprintLabel;
  private Label progressLabel;

  private Texture levelStart;
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

  private Image levelStatus;


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
    table3.top();
    table3.setFillParent(true);
    table3.padTop(25f);

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

    //Create textures to be changed on update
    levelStart = new Texture("images/00.png");
    level10percent = new Texture("images/10.png");
    level20percent = new Texture("images/20.png");
    level30percent = new Texture("images/30.png");
    level40percent = new Texture("images/40.png");
    level50percent = new Texture("images/50.png");
    level60percent = new Texture("images/60.png");
    level70percent = new Texture("images/70.png");
    level80percent = new Texture("images/80.png");
    level90percent = new Texture("images/90.png");
    levelComplete = new Texture("images/levelComplete.png");

    //Progress Text
    float progress = entity.getComponent(ProgressComponent.class).getProgress();
    CharSequence progressText = String.format("%.0f %%", progress);
    progressLabel = new Label(progressText, skin, "large");

    table.add(heartImage).size(heartSideLength).pad(5);
    table.add(healthLabel).pad(5);
    stage.addActor(table);

    table2.add(sprintLabel).pad(5);
    stage.addActor(table2);

    levelStatus = new Image(levelStart);
    table3.add(levelStatus).size(600, 250);
    table3.add(progressLabel);
    stage.addActor(table3);
  }


  @Override
  public void draw(SpriteBatch batch) {
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

    if (progress % 10 == 0 && progress > 0 || progress > 95) {
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



  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
    sprintLabel.remove();
    progressLabel.remove();

  }
}

