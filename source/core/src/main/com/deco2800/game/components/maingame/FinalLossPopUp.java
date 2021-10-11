package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.GdxGame;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinalLossPopUp extends UIComponent {
    /* Debugging */
    private static final Logger logger =
            LoggerFactory.getLogger(FinalLossPopUp.class);

    /* Allows the game-state to be changed from the pop-up menu */
    private GdxGame game;

    /* Allows the pop-up menu to listen for the players' death */
    private Entity player;

    /* Handler to set up the UI elements of the loss screen */
    private PopupUIHandler handler;

    /**
     * Constructor for the PlayerLossPopup
     *
     * @param game the current game.
     * @param player the player entity this pop-up pertains to
     * @param lossHandler a UI handler which sets up UI elements for the loss
     *                    pop-up menu.
     * */
    public FinalLossPopUp(GdxGame game, Entity player,
                           PopupUIHandler lossHandler) {
        this.game = game;
        this.player = player;
        this.handler = lossHandler;
    }

    /**
     * Sets up listener to take action when the player dies
     * */
    @Override
    public void create() {
        super.create();
        player.getEvents().addListener("playerFinalDeath", this::onFinalDeath);

    }

    /**
     * Creates the loss pop-up menu when the players' health drops to 0. The
     * game state is set to OVER to cease combat.
     * */
    public void onFinalDeath() {
        createUI();
        game.setState(GdxGame.GameState.OVER);
    }

    /**
     * Creates the UI for the PlayerLossPopup menu.
     * */
    public void createUI() {
        logger.debug("Creating player loss ui");
        Entity ui = new Entity();
        ui.addComponent(new FinalLossActions(game, entity))
                .addComponent(new FinalLossDisplay(handler));
        ServiceLocator.getEntityService().register(ui);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        //
    }
}
