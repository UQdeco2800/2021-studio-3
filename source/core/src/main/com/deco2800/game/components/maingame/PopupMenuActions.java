package com.deco2800.game.components.maingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;

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

    public PopupMenuActions(GdxGame game) {
        this.game = game;
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
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }
}