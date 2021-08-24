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
        entity.getEvents().addListener("pause", this::onPause);
    }

    private void onPause() {
        logger.info("pausing game");
        if (game.getState() == GdxGame.GameState.PAUSED) {
            game.setState(GdxGame.GameState.RUNNING);

        } else {
            game.setState(GdxGame.GameState.PAUSED);
        }
    }

}
