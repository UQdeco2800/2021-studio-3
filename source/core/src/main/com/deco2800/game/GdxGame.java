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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
  private String saveState;
  private boolean loadState = false;

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
      case TUTORIAL:
        return new TutorialScreen(this, resourceService, MainGameScreen.Level.TUTORIAL);
      case MAIN_MENU:
        return new MainMenuScreen(this);
      case MAIN_GAME:
        if (loadState) {
          loadState = false;
          return new MainGameScreen(this, saveState, resourceService);
        } else {
          return new MainGameScreen(this, resourceService, MainGameScreen.Level.ONE);
        }
      case LEVEL_TWO_GAME:
        if (loadState) {
          loadState = false;
          return new LevelTwoScreen(this, saveState, resourceService);
        } else {
          return new LevelTwoScreen(this, resourceService, MainGameScreen.Level.TWO);
        }
      case LEVEL_THREE_GAME:
        if (loadState) {
          loadState = false;
          return new LevelThreeScreen(this, saveState, resourceService);
        } else {
          return new LevelThreeScreen(this, resourceService, MainGameScreen.Level.THREE);
        }
      case LEVEL_FOUR_GAME:
        return new LevelFourScreen(this, resourceService, MainGameScreen.Level.FOUR);
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
      case LOAD:
        return new LoadScreen(this, resourceService);
        default:
        return null;
    }
  }

  /**
   * Create a new screen of the provided saveState.
   * @param saveState save file
   * @return new screen
   */
  private Screen loadState(String saveState) {
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    setScreen(newScreen(ScreenType.MAIN_GAME));

    try(BufferedReader br = new BufferedReader(new FileReader(saveState))) {
      String line = br.readLine();
      // parse file to load the floor
      String[] values = line.split(":");
      switch(values[1]) {
        case "levelOne":
          setScreenType(ScreenType.MAIN_GAME);
          return new MainGameScreen(this,  resourceService, MainGameScreen.Level.ONE);
        case "levelTwo":
          //return new LevelTwoScreen(this, saveState, resourceService);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void setScreenType(ScreenType screenType) {
    this.screenType = screenType;
  }

  public void setScreenType(ScreenType screenType, String saveState) {
    this.screenType = screenType;
    this.saveState = saveState;
    this.loadState = true;
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
    LOADING, INTRO, SAVE_STATE, TUTORIAL, LOAD
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
