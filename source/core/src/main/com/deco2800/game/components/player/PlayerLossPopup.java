package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.maingame.PlayerLossActions;
import com.deco2800.game.components.maingame.PlayerLossDisplay;
import com.deco2800.game.components.maingame.PopupUIHandler;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class controlling the pop-up menu which is triggered upon the players' death
 * */
public class PlayerLossPopup extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlayerLossPopup.class);

    /* Allows the game-state to be changed from the pop-up menu */
    private GdxGame game;

    /* Allows the pop-up menu to listen for the players' death */
    private Entity player;

    /* Handler to set up the UI elements of the loss screen */
    private PopupUIHandler handler;

    public PlayerLossPopup(GdxGame game, Entity player,
            PopupUIHandler lossHandler) {
        this.game = game;
        this.player = player;
        this.handler = lossHandler;
    }

    /**
     * Set up listener to take action when the player dies
     * */
    @Override
    public void create() {
        super.create();
        player.getEvents().addListener("playerDeath", this::onDeath);
    }

    /**
     * Creates the pop-up menu when the player's health drops to 0
     * */
    public void onDeath() {
        createUI();
        game.setState(GdxGame.GameState.OVER);
    }

    /**
     * Creates the UI for the PlayerLossPopup menu.
     * */
    public void createUI() {
        logger.debug("Creating player loss ui");
        Entity ui = new Entity();

        ui.addComponent(new PlayerLossActions(game))
                .addComponent(new PlayerLossDisplay(handler));

        ServiceLocator.getEntityService().register(ui);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        //
    }
}