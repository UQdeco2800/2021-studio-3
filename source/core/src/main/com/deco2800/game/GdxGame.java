package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

    // Sets background to light yellow
    Gdx.gl.glClearColor(0f/255f, 0/255f, 75/255f, 0);
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
        return new MainGameScreen(this, resourceService, MainGameScreen.Level.ONE);
      case LEVEL_TWO_GAME:
        return new LevelTwoScreen(this, resourceService, MainGameScreen.Level.TWO);
      case LEVEL_THREE_GAME:
        return new LevelThreeScreen(this, resourceService, MainGameScreen.Level.THREE);
      case RESPAWN1:
        return new MainGameScreen(this, true, resourceService, MainGameScreen.Level.ONE);
      case RESPAWN2:
        return new LevelTwoScreen(this, true, resourceService, MainGameScreen.Level.TWO);
      case RESPAWN3:
        return new LevelThreeScreen(this, true, resourceService, MainGameScreen.Level.THREE);
      case SETTINGS:
        return new SettingsScreen(this);
      case LOADING:
        return new LoadingScreen(this, resourceService);
      case CHECKPOINT:
        return new MainGameScreen(this, 1, true, resourceService, MainGameScreen.Level.ONE);
      case CHECKPOINT_REPLAY:
        return new MainGameScreen(this, 1, false, resourceService, MainGameScreen.Level.ONE);
      case INTRO:
        return new IntroScreen(this, resourceService);
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
    MAIN_MENU, MAIN_GAME, RESPAWN1, RESPAWN2, RESPAWN3, SETTINGS, CHECKPOINT,
    CHECKPOINT_REPLAY, LEVEL_TWO_GAME, LEVEL_THREE_GAME, LEVEL_FOUR_GAME,
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
