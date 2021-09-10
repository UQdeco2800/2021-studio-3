package com.deco2800.game.components.maingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the actions to take when buttons on the 'Loss' pop-up menu are
 * clicked on.
 * */
public class PlayerLossActions extends Component {
    /* Debugging */
    private static final Logger logger =
            LoggerFactory.getLogger(PlayerLossActions.class);

    /* Lets the loss menu change the game screen */
    private GdxGame game;

    /* Lets the loss menu access the PopupMenuActions */
    private Entity mainMenuUI;

    /**
     * Constructor for the PlayerLossActions.
     *
     * @param game the current game.
     * */
    public PlayerLossActions(GdxGame game, Entity mainMenuUI) {
        this.game = game;
        this.mainMenuUI = mainMenuUI;
    }

    /**
     * Instantiates the PlayerLossActions. Adds listeners for button pushes on
     * the loss pop-up menu.
     * */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("homeMenu",
                this.mainMenuUI.getComponent(PopupMenuActions.class)::onHome);
        entity.getEvents().addListener("replayLevelLoss",
                this.mainMenuUI.getComponent(PopupMenuActions.class)::onReplayLoss);
    }
}