package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.deco2800.game.GdxGame;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.screens.MainGameScreen;
import net.dermetfan.gdx.physics.box2d.PositionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class PauseGamePopUpTest {

    @Test
    void shouldPause() {
        GdxGame game = new GdxGame();
        game.setState(GdxGame.GameState.RUNNING);
        PopupUIHandler handler = new PopupUIHandler(new String[]{"images/pauseMenuBackground.png",
                "images/pauseRestart.png"});
        PauseGamePopUp popUp = new PauseGamePopUp(game, handler);
        popUp.onPause();

        assertTrue(game.getState() == GdxGame.GameState.PAUSED);
    }
}