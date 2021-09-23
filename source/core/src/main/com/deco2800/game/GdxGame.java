package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.screens.LevelTwoScreen;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.screens.MainMenuScreen;
import com.deco2800.game.screens.SettingsScreen;
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

  @Override
  public void create() {
    logger.info("Creating game");
    loadSettings();

    // Sets background to light yellow
    Gdx.gl.glClearColor(248f/255f, 249/255f, 178/255f, 1);

    setScreen(ScreenType.MAIN_MENU);
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
        return new MainGameScreen(this);
      case LEVEL_TWO_GAME:
        return new LevelTwoScreen(this);
      case RESPAWN:
        return new MainGameScreen(this, true);
      case SETTINGS:
        return new SettingsScreen(this);
      case CHECKPOINT:
        return new MainGameScreen(this, 1, true);
      case CHECKPOINT_REPLAY:
        return new MainGameScreen(this, 1, false);
        default:
        return null;
    }
  }

  public void setState(GameState gameState) {
    this.gameState = gameState;
  }

  public GameState getState() {
    return this.gameState;
  }

  public enum ScreenType {
    MAIN_MENU, MAIN_GAME, RESPAWN, SETTINGS, CHECKPOINT, CHECKPOINT_REPLAY, LEVEL_TWO_GAME
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
