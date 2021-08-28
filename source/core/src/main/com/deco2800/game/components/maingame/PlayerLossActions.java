package com.deco2800.game.components.maingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerLossActions extends Component {
    /* Debugging */
    private static final Logger logger = LoggerFactory.getLogger(PlayerLossActions.class);

    /* Lets the loss menu change the game screen */
    private GdxGame game;

    public PlayerLossActions(GdxGame game) {
        this.game = game;
    }

    /**
     * Listens for button pushes on the menu
     * */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("homeMenu", this::onHome);
        entity.getEvents().addListener("replayLevel", this::onReplay);
    }

    /**
     * Changes the screen to be the main menu screen
     * */
    public void onHome(){
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Refreshes the main game screen. Old screen is disposed of.
     * */
    public void onReplay() {
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }
}