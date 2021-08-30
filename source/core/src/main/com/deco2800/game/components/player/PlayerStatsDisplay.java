package com.deco2800.game.components.player;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.CombatStatsComponent;

import com.deco2800.game.components.SprintComponent;

import com.deco2800.game.components.SprintComponent;

import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.rendering.TextureRenderComponent;

import com.deco2800.game.screens.MainGameScreen;

import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  Table table2;
  private Image heartImage;
  private Label healthLabel;

  private Label sprintLabel;

  AssetManager manager;
  TextureRegion  textureRegion;

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


    table.add(heartImage).size(heartSideLength).pad(5);
    table.add(healthLabel);
    stage.addActor(table);

    //new table to add sprint label
    //not sure how to add sprint label below health label in same table
    table2 = new Table();
    table2.top().left();
    table2.setFillParent(true);
    table2.padTop(100f).padLeft(5f);
    table2.add(sprintLabel);
    stage.addActor(table2);
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
   * updates the players sprint on the UI
   * @param sprintLevel player's sprint
   */
  public void updateSprintLevelUI(int sprintLevel){
    CharSequence text = String.format("Sprint: %d", sprintLevel);
    sprintLabel.setText(text);
  }

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
    sprintLabel.remove();
  }
}
