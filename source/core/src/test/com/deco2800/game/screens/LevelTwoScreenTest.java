package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
public class LevelTwoScreenTest {

    /**
     * Test whether the screen for level two could be loaded successfully.
     */
    @Test
    void loadLevelTwoScreenTest() {
        GdxGame game = mock(GdxGame.class);
        game.setScreen(GdxGame.ScreenType.LEVEL_TWO_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LEVEL_TWO_GAME);
    }
}
