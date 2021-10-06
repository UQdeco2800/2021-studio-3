package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.files.UserSettings;

import com.deco2800.game.screens.*;

import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.Gdx.app;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {
  private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);
  private GameState gameState;
  private ScreenType screenType;
  private ResourceService resourceService;

  @Override
  public void create() {
    logger.info("Creating game");
    loadSettings();

    // Sets background to light blue
    Gdx.gl.glClearColor(195/255f,206/255f,224/255f,1);
    ServiceLocator.registerResourceService(new ResourceService());
    resourceService = ServiceLocator.getResourceService();
    setScreen(ScreenType.MAIN_MENU);
  }

  @Override
  public void render() {
    super.render();

  }
  /**
   * Loads the game's settings.
   */
  private void loadSettings() {
    logger.debug("Loading game settings");
    UserSettings.Settings settings = UserSettings.get();
    UserSettings.applySettings(settings);
  }

  /**
   * Sets the game's screen to a new screen of the provided type.
   * @param screenType screen type
   */
  public void setScreen(ScreenType screenType) {
    logger.info("Setting game screen to {}", screenType);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    setScreen(newScreen(screenType));
  }

  /**
   * Sets the game's screen to a new screen of the provided type.
   * @param screenType screen type
   */
  public void loadCheckPoint(ScreenType screenType) {
    logger.info("Setting game screen to {}", screenType);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    setScreen(newScreen(screenType));
  }

  @Override
  public void dispose() {
    logger.debug("Disposing of current screen");
    getScreen().dispose();
  }

  /**
   * Create a new screen of the provided type.
   * @param screenType screen type
   * @return new screen
   */
  private Screen newScreen(ScreenType screenType) {
    switch (screenType) {
      case MAIN_MENU:
        return new MainMenuScreen(this);
      case MAIN_GAME:
        return new MainGameScreen(this, resourceService);
      case LEVEL_TWO_GAME:
        return new LevelTwoScreen(this, resourceService);
      case LEVEL_THREE_GAME:
        return new LevelThreeScreen(this, resourceService);
      case RESPAWN:
        return new MainGameScreen(this, true, resourceService);
      case SETTINGS:
        return new SettingsScreen(this);
      case LOADING:
        return new LoadingScreen(this, resourceService);
      case CHECKPOINT:
        return new MainGameScreen(this, 1, true, resourceService);
      case CHECKPOINT_REPLAY:
        return new MainGameScreen(this, 1, false, resourceService);
      case INTRO:
        return new IntroScreen(this, resourceService);
      case LOAD:
        return new LoadScreen(this, resourceService);
        default:
        return null;
    }
  }

  public void setScreenType(ScreenType screenType) {
    this.screenType = screenType;
  }

  public ScreenType getScreenType() {
    return this.screenType;
  }

  public void setState(GameState gameState) {
    this.gameState = gameState;
  }

  public GameState getState() {
    return this.gameState;
  }

  public enum ScreenType {
    MAIN_MENU, MAIN_GAME, RESPAWN, SETTINGS, LOAD, CHECKPOINT,
    CHECKPOINT_REPLAY, LEVEL_TWO_GAME, LEVEL_THREE_GAME,
    LOADING, INTRO

  }

  public enum GameState {
    RUNNING, PAUSED, OVER
  }

  /**
   * Exit the game.
   */
  public void exit() {
    app.exit();
  }
}
