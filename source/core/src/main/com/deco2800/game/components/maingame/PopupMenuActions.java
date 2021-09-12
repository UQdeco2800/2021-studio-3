package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.LivesComponent;

/**
 * Handles actions for the buttons pushed on the win, loss and pause pop-up
 * menus when the actions are the same across the screens.
 *
 * This class allows for the functionality of buttons common to all menus to
 * be changed centrally.
 * */
public class PopupMenuActions extends Component {
    /* Allows the pop-up menus to change the game state */
    private GdxGame game;
    private ForestGameArea area;
    private int checkPointStatus;

    public PopupMenuActions(GdxGame game) {
        this.game = game;
    }

    public PopupMenuActions(GdxGame game, ForestGameArea area) {
        this.game = game;
        this.area = area;
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

        if (area.getCheckPointStatus() == 1) {
            game.setScreen(GdxGame.ScreenType.CHECKPOINT_REPLAY);
        } else {
            game.setScreen(GdxGame.ScreenType.MAIN_GAME);
        }
    }

    /**
     * Method actives when user clicks the replay button after dying.
     */
    public void onReplayLoss() {
        if (area.getPlayer().getComponent(LivesComponent.class).getLives() < 1) {
            onHome();

        } else {
            if (area.getCheckPointStatus() == 1) {
                game.setScreen(GdxGame.ScreenType.CHECKPOINT);
            } else {
                game.setScreen(GdxGame.ScreenType.RESPAWN);
            }
        }
    }

    /**
     * Method actives when user clicks the replay button after dying with no lives left.
     */
    public void onReplayLossFinal() {
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Method actives when user clicks the replay button after winning
     */
    public void onReplayWin() {

            game.setScreen(GdxGame.ScreenType.MAIN_GAME);

    }


}