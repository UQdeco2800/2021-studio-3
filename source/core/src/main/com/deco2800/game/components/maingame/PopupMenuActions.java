package com.deco2800.game.components.maingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.LevelTwoArea;
import com.deco2800.game.areas.LevelThreeArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.LivesComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

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
    private ForestGameArea area = null;
    private LevelTwoArea area2 = null;
    private LevelThreeArea area3 = null;
    private int currentLevel = 0;

    public PopupMenuActions(GdxGame game) {
        this.game = game;
    }

    public PopupMenuActions(GdxGame game, ForestGameArea area) {
        this.game = game;
        this.area = area;
        this.currentLevel = 1;
    }

    public PopupMenuActions(GdxGame game, LevelTwoArea area) {
        this.game = game;
        this.area2 = area;
        this.currentLevel = 2;
    }

    public PopupMenuActions(GdxGame game, LevelThreeArea area) {
        this.game = game;
        this.area3 = area;
    }

    /**
     * Called when a user clicks on the Main Menu button on pop-up screens.
     * Changes the screen to be the main menu screen
     * */
    public void onHome() {
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
        //area.getPlayer().getComponent(LivesComponent.class).resetLives();
    }

    /**
     * Called when the user clicks on the Replay button on pop-up screens.
     * Refreshes the main game screen. Old screen is disposed of.
     * */
    public void onReplay() {

//        if (area.getCheckPointStatus() == 1) {
//            game.setScreen(GdxGame.ScreenType.CHECKPOINT_REPLAY);
//        } else {
//            game.setScreen(GdxGame.ScreenType.MAIN_GAME);
//        }

        if (area != null) {
            if (area.getCheckPointStatus() == 1) {
                game.setScreenType(GdxGame.ScreenType.CHECKPOINT_REPLAY);
                game.setScreen(GdxGame.ScreenType.LOADING);
            } else {
                game.setScreenType(GdxGame.ScreenType.MAIN_GAME);
                game.setScreen(GdxGame.ScreenType.LOADING);
            }
        } else if (area2 != null) {
            game.setScreenType(GdxGame.ScreenType.LEVEL_TWO_GAME);
            game.setScreen(GdxGame.ScreenType.LOADING);
        } else if (area3 != null) {
            game.setScreenType(GdxGame.ScreenType.LEVEL_THREE_GAME);
            game.setScreen(GdxGame.ScreenType.LOADING);
        }
    }

    /**
     * Method actives when user clicks the replay button after dying.
     */
    public void onReplayLoss() {

        if (area != null) {
            logger.info("Player has lost and is now replaying level 1");
                if (area.getCheckPointStatus() == 1 ) {
                    game.setScreenType(GdxGame.ScreenType.CHECKPOINT);
                } else {
                    game.setScreenType(GdxGame.ScreenType.RESPAWN1);
                }
            game.setScreen(GdxGame.ScreenType.LOADING);
        } else if (area2 != null) {
            logger.info("Player has lost and is now replaying level2");
                    game.setScreenType(GdxGame.ScreenType.RESPAWN2);
                    game.setScreen(GdxGame.ScreenType.LOADING);

        } else if (area3 != null) {
            logger.info("Player has lost and is now replaying level3");
                    game.setScreenType(GdxGame.ScreenType.RESPAWN3);
                    game.setScreen(GdxGame.ScreenType.LOADING);
        }
    }

    /**
     * Method actives when user clicks the replay button after dying with no lives left.
     */
    public void onReplayLossFinal() {
        if (area != null) {
            area.getPlayer().getComponent(LivesComponent.class).setLives(3);
           game.setScreenType(GdxGame.ScreenType.MAIN_GAME);
        } else if (area2 != null) {
            area2.getPlayer().getComponent(LivesComponent.class).setLives(3);
          game.setScreenType(GdxGame.ScreenType.LEVEL_TWO_GAME);
        } else if (area3 != null) {
            area3.getPlayer().getComponent(LivesComponent.class).setLives(3);
            game.setScreenType(GdxGame.ScreenType.LEVEL_THREE_GAME);
        }
        logger.info("Player lives reset");
        game.setScreen(GdxGame.ScreenType.LOADING);

    }


    /**
     * Method actives when user clicks the replay button after winning
     */
    public void onReplayWin() {
            game.setScreenType(GdxGame.ScreenType.MAIN_GAME);
            game.setScreen(GdxGame.ScreenType.LOADING);

    }

    /**
     * Method actives when user clicks the next level button after winning
     */
    public void onNextLevel() {
        if (this.currentLevel == 1) {
            game.setScreenType(GdxGame.ScreenType.LEVEL_TWO_GAME);
            game.setScreen(GdxGame.ScreenType.LOADING);
        } else if (this.currentLevel == 2) {
            game.setScreenType(GdxGame.ScreenType.LEVEL_THREE_GAME);
            game.setScreen(GdxGame.ScreenType.LOADING);
        }
        //game.setScreen(GdxGame.ScreenType.LEVEL_TWO_GAME);
    }

    /**
     * Return the current level.
     * @return int current level num
     */
    public int getCurrentLevel() {
        return currentLevel;
    }
}