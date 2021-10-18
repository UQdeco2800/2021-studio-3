package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.LivesComponent;
import com.deco2800.game.components.maingame.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class controlling the pop-up menu which appears when a player dies. A
 * player is 'dead' if their health reaches 0.
 * */
public class PlayerLossPopup extends UIComponent {
    /* Debugging */
    private static final Logger logger =
            LoggerFactory.getLogger(PlayerLossPopup.class);

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
    public PlayerLossPopup(GdxGame game, Entity player,
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
        player.getEvents().addListener("playerDeath", this::onDeath);

    }

    /**
     * Creates the loss pop-up menu when the players' health drops to 0. The
     * game state is set to OVER to cease combat.
     * */
    public void onDeath() {
        createUI();
        if (player.getComponent(LivesComponent.class).getLives() > 0) {
            player.getComponent(LivesComponent.class).addLives(-1);
        }
            game.setState(GdxGame.GameState.OVER);
        }
    /**
     * Creates the UI for the PlayerLossPopup menu.
     * */
    public void createUI() {
        logger.debug("Creating player loss ui");
        Entity ui = new Entity();
        if (player.getComponent(LivesComponent.class).getLives() < 1) {
            ui.addComponent(new FinalLossActions(game, entity))
                    .addComponent(new FinalLossDisplay(handler));
        } else {
            ui.addComponent(new PlayerLossActions(game, entity))
                    .addComponent(new PlayerLossDisplay(handler, player));
            ServiceLocator.getEntityService().register(ui);
        }
    }

    @Override
    public void draw(SpriteBatch batch)  {
        //
    }
}