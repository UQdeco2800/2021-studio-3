package com.deco2800.game.components.maingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the actions to take when buttons on the 'Win' pop-up menu are
 * clicked on.
 * */
public class PlayerWinActions extends Component {
    /* Debugging */
    private static final Logger logger =
            LoggerFactory.getLogger(PlayerWinActions.class);

    /* Lets the win menu change the game screen */
    private GdxGame game;

    /* Lets the win menu access the PopupMenuActions */
    private Entity mainGameUI;

    /**
     * Constructor for the PlayerWinActions
     *
     * @param game the current game.
     * */
    public PlayerWinActions(GdxGame game, Entity mainGameUI) {
        this.game = game;
        this.mainGameUI = mainGameUI;
    }

    /**
     * Instantiates the PlayerWinActions. Adds listeners for button pushes on
     * the win pop-up menu.
     * */
    @Override
    public void create() {
        super.create();

        entity.getEvents().addListener("replayLevelWin",
                this.mainGameUI.getComponent(PopupMenuActions.class)::onReplayWin);
        entity.getEvents().addListener("homeMenu",
                this.mainGameUI.getComponent(PopupMenuActions.class)::onHome);

        /* Currently, there is only one level. 'continue' repeats the level. */

        // jump into the next level
        entity.getEvents().addListener("continue",
                this.mainGameUI.getComponent(PopupMenuActions.class)::onNextLevel);
    }

    /**
     * Called when the user clicks on the Continue button on the Win pop-up
     * screen. Takes the player to the next level.
     *
     * (Currently, there is only one level, just repeats the level)
     * */
    public void onContinue() {
        this.mainGameUI.getComponent(PopupMenuActions.class).onReplay();
    }
}