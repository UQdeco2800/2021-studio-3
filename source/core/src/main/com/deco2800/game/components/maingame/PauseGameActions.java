package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.services.GameTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PauseGameActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PauseGameActions.class);
    private GdxGame game;

    public PauseGameActions(GdxGame game) {
        this.game = game;
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
        entity.getEvents().trigger("continue");
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
