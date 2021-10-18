package com.deco2800.game.components.maingame;

import com.badlogic.gdx.audio.Sound;
import com.deco2800.game.GdxGame;
import com.deco2800.game.SaveData.SaveData;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.LevelFourArea;
import com.deco2800.game.areas.LevelTwoArea;
import com.deco2800.game.areas.LevelThreeArea;
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
    private static final String lossMusic = "sounds/loss.mp3";
    /* Allows the pop-up menus to change the game state */
    private GdxGame game;

    /* The current Area */
    private ForestGameArea area = null;
    private LevelTwoArea areaTwo = null;
    private LevelThreeArea areaThree = null;
    private LevelFourArea areaFour = null;

    /*Player savae file*/
    private SaveData saveData;

    /* The current level */
    private int currentLevel;

    public PopupMenuActions(GdxGame game) {
        this.game = game;
    }

    public PopupMenuActions(GdxGame game, ForestGameArea area) {
        this.game = game;
        switch (area.getAreaType()) {
            case ONE:
                this.area = area;
                this.currentLevel = 1;
                saveData = new SaveData(game, area.getPlayer());
                break;
            case TWO:
                this.areaTwo = (LevelTwoArea) area;
                this.currentLevel = 2;
                saveData = new SaveData(game, area.getPlayer());
                break;
            case THREE:
                this.areaThree = (LevelThreeArea) area;
                this.currentLevel = 3;
                saveData = new SaveData(game, area.getPlayer());
                break;
            case FOUR:
                this.areaFour = (LevelFourArea) area;
                this.currentLevel = 4;
                saveData = new SaveData(game, area.getPlayer());
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
     * Method actives when user clicks the replay button after winning. This
     * method will return the player to the beginning of the same level.
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
        saveData.savePlayerData();
    }

    /**
     * Method actives when user clicks the next level button after winning.
     *
     * If the player is on levels 1-3, this method will change the screen to
     * the next level.
     *
     * If the player is on level 4, this method will change the screen to the
     * Main Menu.
     */
    public void onNextLevel() {
//        Sound buttonClickSound = ServiceLocator.getResourceService().getAsset(CLICK_SOUND_FILE_PATH, Sound.class);
//        buttonClickSound.play();
        switch (this.currentLevel) {
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
        //game.setScreen(GdxGame.ScreenType.LEVEL_TWO_GAME);
        saveData.savePlayerData();
    }

    /**
     * Returns the current level.
     *
     * @return an integer between 1 to 4 inclusive representing the current
     *         game level.
     */
    public int getCurrentLevel() {
        return this.currentLevel;
    }

    /**
     * Returns the current 'game' for this PopupMenuActions.
     *
     * @return the game associated with this PopupMenuActions.
     * */
    public GdxGame getGame() {
        return this.game;
    }

    /**
     * Returns the current area.
     *
     * @return the current game area.
     * */
    public ForestGameArea getCurrentArea() {
        return (area != null) ? area
                : (areaTwo != null) ? areaTwo
                : (areaThree != null) ? areaThree
                : areaFour;
    }
}