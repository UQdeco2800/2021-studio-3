package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.LevelTwoArea;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.maingame.PopupMenuActions;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
public class LevelChangeTest {

    /**
     * Test whether the game level can be changed successfully from level one to level two.
     */
    @Test
    void changeToLevelTwoTest() {
        GdxGame game = mock(GdxGame.class);
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
        ForestGameArea area = mock(ForestGameArea.class);
        PopupMenuActions winPopMenuActions = new PopupMenuActions(game, area);
        winPopMenuActions.onNextLevel();
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_TWO_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Test whether the game level can be changed successfully from level two to level three.
     */
    @Test
    void changeToLevelThreeTest() {
        GdxGame game = mock(GdxGame.class);
        game.setScreen(GdxGame.ScreenType.LEVEL_TWO_GAME);
        LevelTwoArea area = mock(LevelTwoArea.class);
        PopupMenuActions winPopMenuActions = new PopupMenuActions(game, area);
        winPopMenuActions.onNextLevel();
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_THREE_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }
}
