package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Screen;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.*;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
  private GdxGame game;

  public MainMenuActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("start", this::onStart);
    entity.getEvents().addListener("load", this::onLoad);
    entity.getEvents().addListener("loadData", this::loadData);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("settings", this::onSettings);
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    logger.info("Start game");
    //Will need to create a condition for intro screen
    game.setScreenType(GdxGame.ScreenType.TUTORIAL);
    game.setScreen(GdxGame.ScreenType.INTRO);
    //game.setScreen(GdxGame.ScreenType.LOADING);
  }

  private void onLoad() {
    game.setScreenType(GdxGame.ScreenType.MAIN_GAME);
    game.setScreen(GdxGame.ScreenType.LOAD);
  }

  /**
   * Intended for loading a saved game state.
   * Load functionality is not actually implemented.
   */
  private void loadData() {
    GdxGame.ScreenType screenType = null;
    logger.info("Load game");
    try(BufferedReader br = new BufferedReader(new FileReader("saves/saveOne.txt"))) {
      String line = br.readLine();

      if (line == null) {
        onStart();
      } else {

        String[] values = line.split(":");
        switch (values[1]) {
          case "Tutorial":
            screenType = GdxGame.ScreenType.TUTORIAL;
            break;
          case "levelOne":
            screenType = GdxGame.ScreenType.MAIN_GAME;
            break;
          case "levelTwo":
            screenType = GdxGame.ScreenType.LEVEL_TWO_GAME;
            break;
          case "levelThree":
            screenType = GdxGame.ScreenType.LEVEL_THREE_GAME;
            break;
          case "levelFour":
            screenType = GdxGame.ScreenType.LEVEL_FOUR_GAME;
          default:
            onStart();
        }

        game.setScreenType(screenType);
        assert screenType != null;
        logger.info(screenType.toString());
        game.setScreen(GdxGame.ScreenType.LOADING);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }



  /**
   * Exits the game.
   */
  private void onExit() {
    logger.info("Exit game");
    game.exit();
  }

  /**
   * Swaps to the Settings screen.
   */
  private void onSettings() {
    logger.info("Launching settings screen");
    game.setScreen(GdxGame.ScreenType.SETTINGS);
  }
}
