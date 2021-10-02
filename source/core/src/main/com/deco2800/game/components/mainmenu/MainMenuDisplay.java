package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private Table table2;
  Texture background;
  SpriteBatch batch;
  Sprite sprite;
  private static final String MUSIC_FILE_PATH = "sounds/background.mp3";

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
      batch = new SpriteBatch();
      sprite = new Sprite();
      playBackgroundMusic();
    table = new Table();
    table2 = new Table();
    table.setFillParent(true);
    table2.setFillParent(true);
    background = new Texture(Gdx.files.internal("images/main_screens-02.png"));
    TextureRegion textureRegion =
            new TextureRegion(background, 0, 0,
                    Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Image title = new Image(textureRegion);
        /*new Image(
            ServiceLocator.getResourceService()
                .getAsset("images/main_screens-02.png", Texture.class));*/

    TextButton startBtn = new TextButton("START", skin);
    TextButton loadBtn = new TextButton("LOAD", skin);
    TextButton settingsBtn = new TextButton("SETTINGS", skin);
    TextButton exitBtn = new TextButton("EXIT", skin);

    // Triggers an event when the button is pressed
    startBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Start button clicked");
            Entity ui = new Entity();
            /*ui.addComponent(new LoadingDisplay());
            ui.getComponent(LoadingDisplay.class).create();*/
            entity.getEvents().trigger("start");
          }
        });

    loadBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Load button clicked");
            entity.getEvents().trigger("load");
          }
        });

    settingsBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Settings button clicked");
            entity.getEvents().trigger("settings");
          }
        });

    exitBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {

            logger.debug("Exit button clicked");
            entity.getEvents().trigger("exit");
          }
        });

    table2.add(title).center();
    table.row();
    table.add(startBtn).padTop(30f);
    table.row();
    table.add(loadBtn).padTop(15f);
    table.row();
    table.add(settingsBtn).padTop(15f);
    table.row();
    table.add(exitBtn).padTop(15f);

    stage.draw();

    stage.addActor(table2);

    stage.addActor(table);

  }

  @Override
  public void draw(SpriteBatch batch) {
    // draw is handled by the stage
      background = new Texture(Gdx.files.internal("images/main_screens-02.png"));
      batch.draw(background, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      background.dispose();
  }

  private void playBackgroundMusic() {
      Music menuSong = ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class);
      menuSong.setLooping(true);
      menuSong.setVolume(0.5f);
      menuSong.play();
  }
  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  @Override
  public void dispose() {
    table.clear();
    ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class).stop();
    super.dispose();
  }

}
