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
    public void onHome(){
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Called when the user clicks on the Replay button on pop-up screens.
     * Refreshes the main game screen. Old screen is disposed of.
     * */
    public void onReplay() {

        if (area.getCheckPointStatus() == 1) {
            game.setScreen(GdxGame.ScreenType.CHECKPOINT);
        } else {
            game.setScreen(GdxGame.ScreenType.MAIN_GAME);
        }
    }

    public void onReplayLoss() {
        area.getPlayer().getComponent(LivesComponent.class).addLives(-1);
        if (area.getCheckPointStatus() == 1) {
            game.setScreen(GdxGame.ScreenType.CHECKPOINT);
        } else {
            game.setScreen(GdxGame.ScreenType.MAIN_GAME);
        }
        //area.getPlayer().getEvents().trigger("livesUpdate", -1);

    }

}