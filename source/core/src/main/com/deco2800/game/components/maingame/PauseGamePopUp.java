package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.maingame.PlayerLossActions;
import com.deco2800.game.components.maingame.PlayerLossDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class controlling the pop-up menu which is triggered upon the pressing pause
 * */
public class PauseGamePopUp extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PauseGamePopUp.class);

    /* Allows the game-state to be changed from the pop-up menu */
    private GdxGame game;



    public PauseGamePopUp(GdxGame game) {
        this.game = game;
    }

    /**
     * Set up listener to take action when the pause button is pressed
     * */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("pause", this::onPause);
    }

    /**
     * Creates the pop-up menu when the pause button is pressed
     * */
    private void onPause() {
        if (game.getState() == GdxGame.GameState.RUNNING) {
            game.setState(GdxGame.GameState.PAUSED);
            createUI();
        }
    }


    /**
     * Creates the UI for the Pause menu.
     * */
    public void createUI() {
        logger.debug("Creating pause game ui");
        Entity ui = new Entity();
        ui.addComponent(new PauseGameActions(game))
                .addComponent(new PauseGameDisplay());

        ServiceLocator.getEntityService().register(ui);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        //
    }
}