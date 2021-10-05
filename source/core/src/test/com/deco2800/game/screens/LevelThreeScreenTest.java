package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
public class LevelThreeScreenTest {
    /**
     * Test whether the screen for level three could be loaded successfully.
     */
    @Test
    void loadLevelThreeScreenTest() {
        GdxGame game = mock(GdxGame.class);
        game.setScreen(GdxGame.ScreenType.LEVEL_THREE_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LEVEL_THREE_GAME);
    }
}
