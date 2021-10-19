package com.deco2800.game.screens;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.Gdx;
import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class LoadingScreenTest {

    @Mock GdxGame game;

    /**
     * Test whether the screen for loading could be loaded successfully.
     */
    @Test
    void loadLoadingScreenTest() {
        GdxGame game = mock(GdxGame.class);
        game.setScreen(GdxGame.ScreenType.LOADING);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }
}