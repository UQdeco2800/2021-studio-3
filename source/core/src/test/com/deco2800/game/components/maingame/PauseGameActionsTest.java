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
class PauseGameActionsTest {

    @Test
    void shouldResume() {
        GdxGame game = new GdxGame();
        game.setState(GdxGame.GameState.PAUSED);
        Entity mainMenuUI = new Entity();
        mainMenuUI.addComponent(new PopupMenuActions(game));

        Entity ui = new Entity();
        ui.addComponent(new PauseGameDisplay(new PopupUIHandler(new String[]{"images/pauseMenuBackground.png",
                "images/pauseRestart.png"})));
        ui.addComponent(new PauseGameActions(game, ui, mainMenuUI));
        ui.getComponent(PauseGameActions.class).onResume();
        assertTrue(game.getState() == GdxGame.GameState.RUNNING);

    }
}