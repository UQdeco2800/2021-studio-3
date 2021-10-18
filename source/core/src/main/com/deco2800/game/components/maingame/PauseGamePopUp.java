package com.deco2800.game.components.maingame;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.GdxGame;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class controlling the pop-up menu which is triggered upon the pressing pause
 * */
public class PauseGamePopUp extends UIComponent {
    private static final String CLICK_SOUND_FILE_PATH = "sounds/click.mp3";
    private static final Logger logger =
            LoggerFactory.getLogger(PauseGamePopUp.class);

    /* Allows the game-state to be changed from the pop-up menu */
    private GdxGame game;
    private Entity ui;

    /* Handler to set up the UI elements of the pause screen */
    private PopupUIHandler handler;

    public PauseGamePopUp(GdxGame game, PopupUIHandler pauseHandler) {
        this.game = game;
        this.handler = pauseHandler;
    }

    /**
     * Set up listener to take action when the pause button is pressed
     * */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("pause", this::onPause);
        entity.getEvents().addListener("pause", this::createUI);
    }

    /**
     * Creates the pop-up menu when the pause button is pressed
     * */
    public void onPause() {
        logger.info("pausing game");
//        Sound buttonClickSound = ServiceLocator.getResourceService().getAsset(CLICK_SOUND_FILE_PATH, Sound.class);
//        buttonClickSound.play();
        if (game.getState() == GdxGame.GameState.RUNNING) {
            game.setState(GdxGame.GameState.PAUSED);
        }
    }

    /**
     * Creates the UI for the Pause menu.
     * */
    public void createUI() {
        logger.debug("Creating pause game ui");
        if (game.getState() != GdxGame.GameState.OVER) {
            ui = new Entity();
            ui.addComponent(new PauseGameActions(game, ui, entity))
                    .addComponent(new PauseGameDisplay(handler));

            ServiceLocator.getEntityService().register(ui);
        }

    }

    @Override
    public void draw(SpriteBatch batch)  {
        //
    }
}