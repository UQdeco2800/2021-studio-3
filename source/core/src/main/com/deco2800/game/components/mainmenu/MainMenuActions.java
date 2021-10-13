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
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("settings", this::onSettings);
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    logger.info("Start game");
    //Will need to create a condition for intro screen
    game.setScreenType(GdxGame.ScreenType.MAIN_GAME);
    game.setScreen(GdxGame.ScreenType.INTRO);
    //game.setScreen(GdxGame.ScreenType.LOADING);
  }

  /**
   * Intended for loading a saved game state.
   * Load functionality is not actually implemented.
   */
  private void onLoad() {
    GdxGame.ScreenType screenType = GdxGame.ScreenType.MAIN_GAME;;
    logger.info("Load game");
    try(BufferedReader br = new BufferedReader(new FileReader("saves/saveOne.txt"))) {
      String line = br.readLine();
      String[] values = line.split(":");
      if (values[1] == "levelOne") {
        screenType = GdxGame.ScreenType.MAIN_GAME;
      } else if (values[1] == "levelTwo") {
        screenType = GdxGame.ScreenType.LEVEL_TWO_GAME;
      } else if (values[1] == "levelThree") {
        screenType = GdxGame.ScreenType.LEVEL_THREE_GAME;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    game.setScreenType(screenType, "saves/saveOne.txt");
    game.setScreen(GdxGame.ScreenType.LOADING);
    logger.info("Launching load screen");
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
