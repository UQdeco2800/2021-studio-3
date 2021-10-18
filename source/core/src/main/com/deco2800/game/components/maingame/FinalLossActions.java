package com.deco2800.game.components.maingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinalLossActions extends Component {
    /* Debugging */
    private static final Logger logger =
            LoggerFactory.getLogger(FinalLossActions.class);

    /* Lets the loss menu change the game screen */
    private GdxGame game;

    /* Lets the loss menu access the PopupMenuActions */
    private Entity mainMenuUI;

    /**
     * Constructor for the FinalLossActions.
     *
     * @param game the current game.
     * */
    public FinalLossActions(GdxGame game, Entity mainMenuUI) {
        this.game = game;
        this.mainMenuUI = mainMenuUI;
    }

    /**
     * Instantiates the FinalLossActions. Adds listeners for button pushes on
     * the loss pop-up menu.
     * */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("homeMenu",
                this.mainMenuUI.getComponent(PopupMenuActions.class)::continueOnHome);
        entity.getEvents().addListener("finalLoss",
                this.mainMenuUI.getComponent(PopupMenuActions.class)::onReplayLossFinal);
    }
}
