package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PauseGameActions extends Component {
    private static final Logger logger =
            LoggerFactory.getLogger(PauseGameActions.class);
    private GdxGame game;
    private Entity ui;

    public PauseGameActions(GdxGame game, Entity ui) {
        this.game = game;
        this.ui = ui;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("resume", this::onResume);
        entity.getEvents().addListener("homeMenu", this::onHome);
        entity.getEvents().addListener("replayLevel", this::onReplay);
    }


    private void onResume() {
        logger.info("resuming game");
        if (game.getState() == GdxGame.GameState.PAUSED) {
            game.setState(GdxGame.GameState.RUNNING);
        }

        Array<Object> screenElements =
                ui.getComponent(PauseGameDisplay.class).getScreenElements();
        for (Object element: screenElements) {
            if (element.getClass() == Label.class) {
                ((Label) element).remove();
            } else if (element.getClass() == Image.class) {
                ((Image) element).remove();
            } else {
                ((TextButton) element).remove();
            }
        }


    }

    /**
     * Changes the screen to be the main menu screen
     * */
    private void onHome(){
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Refreshes the main game screen. Old screen is disposed of.
     * */
    private void onReplay() {
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

}
