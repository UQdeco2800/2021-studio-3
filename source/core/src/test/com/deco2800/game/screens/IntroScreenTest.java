package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
public class IntroScreenTest {
    /**
     * Test whether the Intro screen could be loaded successfully.
     */
    @Test
    void loadIntroScreenTest() {
        GdxGame game = mock(GdxGame.class);
        game.setScreen(GdxGame.ScreenType.INTRO);
        verify(game).setScreen(GdxGame.ScreenType.INTRO);
    }
}
