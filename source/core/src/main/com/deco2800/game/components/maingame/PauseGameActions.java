package com.deco2800.game.components.maingame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PauseGameActions extends Component {
    private static final String CLICK_SOUND_FILE_PATH = "sounds/click.mp3";
    private static final Logger logger =
            LoggerFactory.getLogger(PauseGameActions.class);
    private GdxGame game;
    private Entity ui;
    private Entity mainMenuUI;

    public PauseGameActions(GdxGame game, Entity ui, Entity mainMenuUI) {
        this.game = game;
        this.ui = ui;
        this.mainMenuUI = mainMenuUI;
    }

    /**
     * Set up listener to take action when buttons on the pause menu are pressed
     * */
    @Override
    public void create() {
        entity.getEvents().addListener("resume", this::onResume);
        entity.getEvents().addListener("homeMenu",
                this.mainMenuUI.getComponent(PopupMenuActions.class)::onHome);
        entity.getEvents().addListener("replayLevel",
                this.mainMenuUI.getComponent(PopupMenuActions.class)::onReplay);
    }

    /**
     * Removes the pop up menu and resumed the game
     */
    public void onResume() {
        logger.info("resuming game");
        if (game.getState() == GdxGame.GameState.PAUSED) {
            game.setState(GdxGame.GameState.RUNNING);
        }

        Array<Image> screenElements =
                ui.getComponent(PauseGameDisplay.class).getScreenElements();
        for (Image image: screenElements) {
            image.remove();
        }
    }
}