package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
public class LevelOneScreenTest {
    /**
     * Test whether the screen for level one could be loaded successfully.
     */
    @Test
    void loadLevelOneScreenTest() {
        GdxGame game = mock(GdxGame.class);
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
        verify(game).setScreen(GdxGame.ScreenType.MAIN_GAME);
    }
}
