package com.deco2800.game.components.maingame;

import static org.mockito.Mockito.verify;
import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class PopupMenuActionsTest {
    @Mock GdxGame game;

    @Test
    /**
     * Tests that the onHome function changes the screen to the Main Menu
     * screen
     * */
    void testOnHomeChanges() {
        // Setup
        PopupMenuActions popup = new PopupMenuActions(game);
        popup.onHome();

        // Verify that the game screen did change
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    @Test
    /**
     * Tests that the onReplay function changes the screen back to the Main
     * Game screen.
     * */
    void testOnReplayChanges() {
        // Setup
        PopupMenuActions popup = new PopupMenuActions(game);
        popup.onReplay();

        // Verify that the game screen did change
        verify(game).setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

}