package com.deco2800.game.components.maingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerWinActions extends Component {
    /* Debugging */
    private static final Logger logger = LoggerFactory.getLogger(PlayerWinActions.class);

    /* Lets the win menu change the game screen */
    private GdxGame game;

    public PlayerWinActions(GdxGame game) {
        this.game = game;
    }

    /**
     * Adds listeners for button pushes on the menu
     * */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("homeMenu", this::onHome);
        entity.getEvents().addListener("replayLevel", this::onReplay);

        /* Currently, there is only one level. 'continuing' repeats the level. */
        entity.getEvents().addListener("continue", this::onContinue);
    }

    /**
     * Changes the screen to be the main menu screen
     * */
    public void onHome() {
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Refreshes the main game screen. Old screen is disposed of.
     * */
    public void onReplay() {
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    /**
     * Takes the player to the next level.
     *
     * (Currently, there is only one level, just repeats the level)
     * */
    public void onContinue() {
        onReplay();
    }
}