package com.deco2800.game.components.maingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.*;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.LivesComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Handles actions for the buttons pushed on the win, loss and pause pop-up
 * menus when the actions are the same across the screens.
 *
 * This class allows for the functionality of buttons common to all menus to
 * be changed centrally.
 * */
public class PopupMenuActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PopupMenuActions.class);

    /* Allows the pop-up menus to change the game state */
    private GdxGame game;

    /* The current Area */
    private ForestGameArea area = null;
    private LevelTwoArea areaTwo = null;
    private LevelThreeArea areaThree = null;
    private LevelFourArea areaFour = null;
    private TutorialArea areaTutorial = null;
    /* The current level */
    private int currentLevel;

    public PopupMenuActions(GdxGame game) {
        this.game = game;
    }

    public PopupMenuActions(GdxGame game, ForestGameArea area) {
        this.game = game;
        switch (area.getAreaType()) {
            case TUTORIAL:
                this.areaTutorial = (TutorialArea) area;
                this.currentLevel = 0;
                break;
            case ONE:
                this.area = area;
                this.currentLevel = 1;
                break;
            case TWO:
                this.areaTwo = (LevelTwoArea) area;
                this.currentLevel = 2;
                break;
            case THREE:
                this.areaThree = (LevelThreeArea) area;
                this.currentLevel = 3;
                break;
            case FOUR:
                this.areaFour = (LevelFourArea) area;
                this.currentLevel = 4;
                break;
        }
    }

    /**
     * Called when a user clicks on the Main Menu button on pop-up screens.
     * Changes the screen to be the main menu screen
     * */
    public void onHome() {
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Called when the user clicks on the Replay button on pop-up screens.
     * Refreshes the main game screen. Old screen is disposed of.
     * */
    public void onReplay() {
        if (area != null) {
            if (area.getCheckPointStatus() == 1) {
                game.setScreenType(GdxGame.ScreenType.CHECKPOINT_REPLAY);
            } else {
                game.setScreenType(GdxGame.ScreenType.MAIN_GAME);
            }
        } else if (areaTwo != null) {
            game.setScreenType(GdxGame.ScreenType.LEVEL_TWO_GAME);
        } else if (areaThree != null) {
            game.setScreenType(GdxGame.ScreenType.LEVEL_THREE_GAME);
        } else if (areaFour != null) {
            game.setScreenType(GdxGame.ScreenType.LEVEL_FOUR_GAME);
        }
        game.setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Method actives when user clicks the replay button after dying.
     */
    public void onReplayLoss() {
        if (area != null) {
            logger.info("Player has lost and is now replaying level 1");
                if (area.getCheckPointStatus() == 1) {
                    game.setScreenType(GdxGame.ScreenType.CHECKPOINT);
                } else {
                    game.setScreenType(GdxGame.ScreenType.RESPAWN1);
                }

        } else if (areaTwo != null) {
            logger.info("Player has lost and is now replaying level2");
            game.setScreenType(GdxGame.ScreenType.RESPAWN2);
        } else if (areaThree != null) {
            logger.info("Player has lost and is now replaying level3");
            game.setScreenType(GdxGame.ScreenType.RESPAWN3);
        }

        game.setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Method actives when user clicks the replay button after dying with no
     * lives left.
     */
    public void onReplayLossFinal() {
        if (area != null) {
            area.getPlayer().getComponent(LivesComponent.class).setLives(3);
           game.setScreenType(GdxGame.ScreenType.MAIN_GAME);
        } else if (areaTwo != null) {
            areaTwo.getPlayer().getComponent(LivesComponent.class).setLives(3);
          game.setScreenType(GdxGame.ScreenType.LEVEL_TWO_GAME);
        } else if (areaThree != null) {
            areaThree.getPlayer().getComponent(LivesComponent.class).setLives(3);
            game.setScreenType(GdxGame.ScreenType.LEVEL_THREE_GAME);
        } else if (areaFour != null) {
            areaFour.getPlayer().getComponent(LivesComponent.class).setLives(3);
            game.setScreenType(GdxGame.ScreenType.LEVEL_FOUR_GAME);
        }
        logger.info("Player lives reset");
        game.setScreen(GdxGame.ScreenType.LOADING);

    }


    /**
     * Method actives when user clicks the replay button after winning
     */
    public void onReplayWin() {
        switch (this.currentLevel) {
            case 1:
                game.setScreenType(GdxGame.ScreenType.MAIN_GAME);
                break;
            case 2:
                game.setScreenType(GdxGame.ScreenType.LEVEL_TWO_GAME);
                break;
            case 3:
                game.setScreenType(GdxGame.ScreenType.LEVEL_THREE_GAME);
                break;
            case 4:
                game.setScreenType(GdxGame.ScreenType.LEVEL_FOUR_GAME);
                break;
        }
            game.setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Method actives when user clicks the next level button after winning
     */
    public void onNextLevel() {
        switch (this.currentLevel) {
            case 0:
                game.setScreenType(GdxGame.ScreenType.MAIN_GAME);
                game.setScreen(GdxGame.ScreenType.LOADING);
                break;
            case 1:
                game.setScreenType(GdxGame.ScreenType.LEVEL_TWO_GAME);
                game.setScreen(GdxGame.ScreenType.LOADING);
                break;
            case 2:
                game.setScreenType(GdxGame.ScreenType.LEVEL_THREE_GAME);
                game.setScreen(GdxGame.ScreenType.LOADING);
                break;
            case 3:
                game.setScreenType(GdxGame.ScreenType.LEVEL_FOUR_GAME);
                game.setScreen(GdxGame.ScreenType.LOADING);
                break;
            case 4:
                // Return to main menu
                onHome();
                break;
        }

    }

    /**
     * Return the current level.
     * @return int current level num
     */
    public int getCurrentLevel() {
        return currentLevel;
    }
}