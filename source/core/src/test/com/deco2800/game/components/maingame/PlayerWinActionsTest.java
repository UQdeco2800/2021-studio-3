package com.deco2800.game.components.maingame;

import static org.mockito.Mockito.verify;
import com.deco2800.game.GdxGame;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)

class PlayerWinActionsTest {
    @Mock GdxGame game;

    /**
     * Tests that the on continue button action replays the level; ie
     * refreshes the Main Game screen.
     * */
    @Test
    void testOnContinue() {
        // Setup
        Entity ui = new Entity();
        ui.addComponent(new PopupMenuActions(game));

        PlayerWinActions popup = new PlayerWinActions(game, ui);
        popup.onContinue();

        // Verify that the game screen did change
        verify(game).setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

}