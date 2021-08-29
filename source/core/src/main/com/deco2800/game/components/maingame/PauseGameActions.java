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

    /**
     * Set up listener to take action when buttons on the pause menu are pressed
     * */
    @Override
    public void create() {
        entity.getEvents().addListener("resume", this::onResume);
        entity.getEvents().addListener("homeMenu",
                entity.getComponent(PopupMenuActions.class)::onHome);
        entity.getEvents().addListener("replayLevel",
                entity.getComponent(PopupMenuActions.class)::onReplay);
    }

    /**
     * Removes the pop up menu and resumed the game
     */
    private void onResume() {
        logger.info("resuming game");
        if (game.getState() == GdxGame.GameState.PAUSED) {
            game.setState(GdxGame.GameState.RUNNING);
        }

        /*Array<Object> screenElements =
                ui.getComponent(PauseGameDisplay.class).getScreenElements();
        for (Object element: screenElements) {
            if (element.getClass() == Label.class) {
                ((Label) element).remove();
            } else if (element.getClass() == Image.class) {
                ((Image) element).remove();
            } else {
                ((TextButton) element).remove();
            }
        }*/
        Array<Image> screenElements =
                ui.getComponent(PauseGameDisplay.class).getScreenElements();
        for (Image image: screenElements) {
            image.remove();
        }
    }
}