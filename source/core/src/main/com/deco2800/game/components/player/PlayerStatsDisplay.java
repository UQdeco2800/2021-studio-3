package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.deco2800.game.components.CombatStatsComponent;

import com.deco2800.game.components.SprintComponent;

import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.rendering.TextureRenderComponent;

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
    Pixmap pixmap = new Pixmap(3, 1, Pixmap.Format.RGBA8888);
    Texture pixmaptex = new Texture(pixmap);
    double health = entity.getComponent(CombatStatsComponent.class).getHealth();
    double hp = health / entity.getComponent(CombatStatsComponent.class).getMaxHealth();
    System.out.println(health);
    System.out.println(entity.getComponent(CombatStatsComponent.class).getMaxHealth());
    System.out.println(hp);

    if (hp>0.9){
      TextureRegion h100 = new TextureRegion(pixmaptex);
      Texture t100 = new Texture("images/100.png");
      h100.setTexture(t100);
      batch.draw(h100,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.8 && hp <=0.9){
      TextureRegion h90 = new TextureRegion(pixmaptex);
      Texture t90 = new Texture("images/90.png");
      h90.setTexture(t90);
      batch.draw(h90,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.7 && hp <=0.8){
      TextureRegion h80 = new TextureRegion(pixmaptex);
      Texture t80 = new Texture("images/80.png");
      h80.setTexture(t80);
      batch.draw(h80,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.6 && hp <=0.7){
      TextureRegion h70 = new TextureRegion(pixmaptex);
      Texture t70 = new Texture("images/70.png");
      h70.setTexture(t70);
      batch.draw(h70,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.5 && hp <=0.6){
      TextureRegion h10 = new TextureRegion(pixmaptex);
      Texture t10 = new Texture("images/60.png");
      h10.setTexture(t10);
      batch.draw(h10,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.4 && hp <=0.5){
      TextureRegion h60 = new TextureRegion(pixmaptex);
      Texture t60 = new Texture("images/50.png");
      h60.setTexture(t60);
      batch.draw(h60,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.3 && hp <=0.4){
      TextureRegion h50 = new TextureRegion(pixmaptex);
      Texture t50 = new Texture("images/40.png");
      h50.setTexture(t50);
      batch.draw(h50,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.2 && hp <=0.3){
      TextureRegion h40 = new TextureRegion(pixmaptex);
      Texture t40 = new Texture("images/30.png");
      h40.setTexture(t40);
      batch.draw(h40,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.1 && hp <=0.2){
      TextureRegion h30 = new TextureRegion(pixmaptex);
      Texture t30 = new Texture("images/20.png");
      h30.setTexture(t30);
      batch.draw(h30,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp>0.0 && hp <=0.1){
      TextureRegion h20 = new TextureRegion(pixmaptex);
      Texture t20 = new Texture("images/10.png");
      h20.setTexture(t20);
      batch.draw(h20,entity.getPosition().x-1, entity.getPosition().y+1);
    }
    if (hp <=0){
      TextureRegion h00 = new TextureRegion(pixmaptex);
      Texture t00 = new Texture("images/00.png");
      h00.setTexture(t00);
      batch.draw(h00,entity.getPosition().x-1, entity.getPosition().y+1);
    }
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

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
    sprintLabel.remove();
  }
}
