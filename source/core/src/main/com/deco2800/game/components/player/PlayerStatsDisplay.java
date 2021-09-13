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
import com.deco2800.game.components.SprintComponent;
import com.deco2800.game.components.*;

import com.deco2800.game.components.SprintComponent;

import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.rendering.TextureRenderComponent;

import com.deco2800.game.screens.MainGameScreen;

import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

import java.util.Collection;



/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  Table sprintTable;
  Table progressTable;
  Table livesTable;
  
  private Image heartImage;
  private Image levelStatus;
  Table healthTable;
  private Image livesImage;

  private Label healthLabel;
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
    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
    entity.getEvents().addListener("updateProgress", this::updatePlayerProgressUI);
    entity.getEvents().addListener("updateLives", this::updateLivesUI);
  }

  /**
   * Creates actors and positions them on the stage using a healthTable.
   * @see Table for positioning options
   */
  private void addActors() {
    // Heart image
    float heartSideLength = 30f;

    // HUD icon images
    float iconSideLength = 30f;

    /* Tables for UI Elements */
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f).padLeft(5f);

    healthTable = new Table();
    healthTable.top().left();
    healthTable.setFillParent(true);
    healthTable.padTop(45f).padLeft(5f);


    sprintTable = new Table();
    sprintTable.top().left();
    sprintTable.setFillParent(true);
    sprintTable.padTop(75f).padLeft(5f);

    progressTable = new Table();
    progressTable.top();
    progressTable.setFillParent(true);
    progressTable.padTop(25f);

    
    livesTable = new Table();
    livesTable.top();
    livesTable.setFillParent(true);
    livesTable.padTop(25f).padLeft(5f);

    livesTable = new Table();
    livesTable.top().left();
    livesTable.setFillParent(true);
    livesTable.padTop(115f).padLeft(5f);

    Table buffTable = new Table();
    buffTable.top().left();
    buffTable.setFillParent(true);
    buffTable.padTop(150f).padLeft(5f);

    /* Images for UI */
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));
    livesImage = new Image(ServiceLocator.getResourceService().getAsset("images/ghost_1.png", Texture.class));
    livesImage = new Image(ServiceLocator.getResourceService().getAsset("images/ghost_1.png", Texture.class));

    // Health text
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("Health: %d", health);
    healthLabel = new Label(healthText, skin, "large");

    //Sprint Text
    int sprint = entity.getComponent(SprintComponent.class).getSprint();
    CharSequence sprintText = String.format("Sprint: %d", sprint);
    sprintLabel = new Label(sprintText, skin, "large");

    // Buff-related Text
    buffText = "Current buffs: \n";
    buffLabel = new Label(buffText, skin, "large");
    buffTable.add(buffLabel);

    //Progress Text
    float progress = entity.getComponent(ProgressComponent.class).getProgress();
    CharSequence progressText = String.format("%.0f %%", progress);
    progressLabel = new Label(progressText, skin, "large");

    //Lives text
    int lives = entity.getComponent(LivesComponent.class).getLives();
    CharSequence livesText = String.format("x%d", lives);
    livesLabel = new Label(livesText, skin, "large");

    //Create textures to be changed on update
    Texture levelStart = new Texture("images/00.png");
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


    //Adding elements to each table, subsequently adding them to the stage
    table.add(heartImage).size(heartSideLength).pad(5);
    table.add(healthLabel).pad(5);
    stage.addActor(table);

    sprintTable.add(sprintLabel).pad(5);
    stage.addActor(sprintTable);

    //Adding elements to tables
    healthTable.add(heartImage).size(iconSideLength).pad(5);
    healthTable.add(healthLabel).pad(5);

    sprintTable.add(sprintLabel).pad(5);


    levelStatus = new Image(levelStart);
    progressTable.add(levelStatus).size(600, 250);
    progressTable.add(progressLabel);

    stage.addActor(progressTable);

    livesTable.add(livesImage).size(iconSideLength).pad(5);
    livesTable.add(livesLabel);

    //adding tables to stages
    stage.addActor(healthTable);
    stage.addActor(sprintTable);
    stage.addActor(progressTable);
    stage.addActor(livesTable);
    stage.addActor(buffTable);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    double health = entity.getComponent(CombatStatsComponent.class).getHealth();
    double hp = health / entity.getComponent(CombatStatsComponent.class).getMaxHealth();
    if (hp>0.9){
      textureRegion.setTexture(manager.get("images/100.png", Texture.class));
      batch.draw(textureRegion,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.8 && hp <=0.9){
      textureRegion.setTexture(manager.get("images/90.png", Texture.class));
      batch.draw(textureRegion,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.7 && hp <=0.8){
      textureRegion.setTexture(manager.get("images/80.png", Texture.class));
      batch.draw(textureRegion,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.6 && hp <=0.7){
      textureRegion.setTexture(manager.get("images/70.png", Texture.class));
      batch.draw(textureRegion,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.5 && hp <=0.6){
      textureRegion.setTexture(manager.get("images/60.png", Texture.class));
      batch.draw(textureRegion,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.4 && hp <=0.5){
      textureRegion.setTexture(manager.get("images/50.png", Texture.class));
      batch.draw(textureRegion,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.3 && hp <=0.4){
      textureRegion.setTexture(manager.get("images/40.png", Texture.class));
      batch.draw(textureRegion,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.2 && hp <=0.3){
      textureRegion.setTexture(manager.get("images/30.png", Texture.class));
      batch.draw(textureRegion,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.1 && hp <=0.2){
      textureRegion.setTexture(manager.get("images/20.png", Texture.class));
      batch.draw(textureRegion,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.0 && hp <=0.1){
      textureRegion.setTexture(manager.get("images/10.png", Texture.class));
      batch.draw(textureRegion,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp <=0){
      textureRegion.setTexture(manager.get("images/00.png", Texture.class));
      batch.draw(textureRegion,entity.getPosition().x-1, entity.getPosition().y+1);
    }
  }

  public Vector2 getPlayerPosition() {
    return entity.getPosition();
  }


  /**
   * Updates the player's health on the ui.
   * @param health player health
   */
  public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
    entity.getEvents().trigger("updatePlayerStatusAnimation", health);
    healthLabel.setText(text);
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

      text = text.concat(buffName + " " + timeRemaining + "..." + "\n");
    }

    /* Update */
    buffLabel.setText(text);
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


  public void updateLivesUI(int lives) {
    CharSequence text = String.format("x%d", lives);
    livesLabel.setText(text);
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
