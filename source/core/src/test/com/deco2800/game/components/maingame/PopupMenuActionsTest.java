package com.deco2800.game.components.maingame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.LevelFourArea;
import com.deco2800.game.areas.LevelThreeArea;
import com.deco2800.game.areas.LevelTwoArea;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.LivesComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.areas.terrain.TerrainFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class PopupMenuActionsTest {
    @Mock GdxGame game;
    @Mock TerrainFactory terrainMock;

    private ForestGameArea forest;
    private LevelTwoArea levelTwo;
    private LevelThreeArea levelThree;
    private LevelFourArea levelFour;

    private Entity player;
    private LivesComponent lives;

    /**
     * Called before every test to instantiate the test environment
     * */
    @BeforeEach
    void setupTestEnvironment() {
        forest = new ForestGameArea(terrainMock, 0, false);
        levelTwo = new LevelTwoArea(terrainMock, 0, false);
        levelThree = new LevelThreeArea(terrainMock, 0, false);
        levelFour = new LevelFourArea(terrainMock, 0, false);

        lives = new LivesComponent(0);

        player = new Entity().addComponent(lives);

        forest.setPlayer(player);
        levelTwo.setPlayer(player);
        levelThree.setPlayer(player);
        levelFour.setPlayer(player);
    }

    /**
     * Tests that the GdxGame attribute is set properly on initialisation of
     * the class (first constructor)
     * */
    @Test
    void shouldSetGame() {
        PopupMenuActions p = new PopupMenuActions(game);
        assertEquals(p.getGame(), game);
    }

    /**
     * Tests that the correct area and level value is set on instantiation of
     * the class (second constructor)
     * */
    @Test
    void shouldSetAreaAndLevelCorrectly() {
        PopupMenuActions menuForest = new PopupMenuActions(game, forest);
        assertEquals(menuForest.getCurrentLevel(), 1);
        assertEquals(menuForest.getCurrentArea(), forest);

        PopupMenuActions menuTwo = new PopupMenuActions(game, levelTwo);
        assertEquals(menuTwo.getCurrentLevel(), 2);
        assertEquals(menuTwo.getCurrentArea(), levelTwo);

        PopupMenuActions menuThree = new PopupMenuActions(game, levelThree);
        assertEquals(menuThree.getCurrentLevel(), 3);
        assertEquals(menuThree.getCurrentArea(), levelThree);

        PopupMenuActions menuFour = new PopupMenuActions(game, levelFour);
        assertEquals(menuFour.getCurrentLevel(), 4);
        assertEquals(menuFour.getCurrentArea(), levelFour);
    }


    /**
     * Tests that the onReplay function works for level one
     * */
    @Test
    void shouldReplayToCorrectLevelOne() {
        /* Level One */
        PopupMenuActions popupOne = new PopupMenuActions(game, forest);
        popupOne.onReplay();

        // Verify that we went back to level one
        verify(game).setScreenType(GdxGame.ScreenType.MAIN_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onReplay function works for level two
     * */
    @Test
    void shouldReplayToCorrectLevelTwo() {
        /* Level Two */
        PopupMenuActions popupTwo = new PopupMenuActions(game, levelTwo);
        popupTwo.onReplay();

        // Verify that we went back to level two
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_TWO_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onReplay function works for level three
     * */
    @Test
    void shouldReplayToCorrectLevelThree() {
        /* Level Three */
        PopupMenuActions popupThree = new PopupMenuActions(game, levelThree);
        popupThree.onReplay();

        // Verify that we went back to level three
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_THREE_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onReplay function works for level four
     * */
    @Test
    void shouldReplayToCorrectLevelFour() {
        /* Level Four */
        PopupMenuActions popupFour = new PopupMenuActions(game, levelFour);
        popupFour.onReplay();

        // Verify that we went back to level four
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_FOUR_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onReplayLoss function works for level one
     * */
    @Test
    void shouldReplayToCorrectRespawnLevelOne() {
        /* Level One */
        PopupMenuActions popupOne = new PopupMenuActions(game, forest);
        popupOne.onReplayLoss();

        // Verify that we went back to level one
        verify(game).setScreenType(GdxGame.ScreenType.RESPAWN1);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onReplayLoss function works for level two
     * */
    @Test
    void shouldReplayToCorrectRespawnLevelTwo() {
        /* Level Two */
        PopupMenuActions popup = new PopupMenuActions(game, levelTwo);
        popup.onReplayLoss();

        // Verify that we went back to level two
        verify(game).setScreenType(GdxGame.ScreenType.RESPAWN2);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onReplayLoss function works for level three
     * */
    @Test
    void shouldReplayToCorrectRespawnLevelThree() {
        /* Level Three */
        PopupMenuActions popup = new PopupMenuActions(game, levelThree);
        popup.onReplayLoss();

        // Verify that we went back to level three
        verify(game).setScreenType(GdxGame.ScreenType.RESPAWN3);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onReplayLossFinal function works for level one
     * */
    @Test
    void shouldReplayToCorrectAndSetLivesOne() {
        /* Level One */
        PopupMenuActions popup = new PopupMenuActions(game, forest);
        popup.onReplayLossFinal();

        // Verify that the screen changed
        verify(game).setScreenType(GdxGame.ScreenType.MAIN_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
        assertEquals( 3, forest.getPlayer().getComponent(LivesComponent.class).getLives());
    }

    /**
     * Tests that the onReplayLossFinal function works for level two
     * */
    @Test
    void shouldReplayToCorrectAndSetLivesTwo() {
        /* Level Two */
        PopupMenuActions popup = new PopupMenuActions(game, levelTwo);
        popup.onReplayLossFinal();

        // Verify that the screen changed
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_TWO_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
        assertEquals( 3, levelTwo.getPlayer().getComponent(LivesComponent.class).getLives());
    }

    /**
     * Tests that the onReplayLossFinal function works for level three
     * */
    @Test
    void shouldReplayToCorrectAndSetLivesThree() {
        /* Level Three */
        PopupMenuActions popup = new PopupMenuActions(game, levelThree);
        popup.onReplayLossFinal();

        // Verify that the screen changed
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_THREE_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
        assertEquals( 3, levelThree.getPlayer().getComponent(LivesComponent.class).getLives());
    }

    /**
     * Tests that the onReplayLossFinal function works for level four
     * */
    @Test
    void shouldReplayToCorrectAndSetLivesFour() {
        /* Level Four */
        PopupMenuActions popup = new PopupMenuActions(game, levelFour);
        popup.onReplayLossFinal();

        // Verify that the screen changed
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_FOUR_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
        assertEquals( 3, levelFour.getPlayer().getComponent(LivesComponent.class).getLives());
    }

    /**
     * Tests that the onReplayWin function works for level one.
     * */
    @Test
    void shouldReplayLevelOne() {
        /* Level One */
        PopupMenuActions popup = new PopupMenuActions(game, forest);
        popup.onReplayWin();

        // Verify that the screen changed
        verify(game).setScreenType(GdxGame.ScreenType.MAIN_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onReplayWin function works for level two.
     * */
    @Test
    void shouldReplayLevelTwo() {
        /* Level Two */
        PopupMenuActions popup = new PopupMenuActions(game, levelTwo);
        popup.onReplayWin();

        // Verify that the screen changed
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_TWO_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onReplayWin function works for level three.
     * */
    @Test
    void shouldReplayLevelThree() {
        /* Level Three */
        PopupMenuActions popup = new PopupMenuActions(game, levelThree);
        popup.onReplayWin();

        // Verify that the screen changed
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_THREE_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onReplayWin function works for level four.
     * */
    @Test
    void shouldReplayLevelFour() {
        /* Level Four */
        PopupMenuActions popup = new PopupMenuActions(game, levelFour);
        popup.onReplayWin();

        // Verify that the screen changed
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_FOUR_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onNextLevel function works for level one
     * */
    @Test
    void shouldChangeToLevelTwo() {
        /* Level One */
        PopupMenuActions popup = new PopupMenuActions(game, forest);
        popup.onNextLevel();

        // Verify that the screen changed
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_TWO_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onNextLevel function works for level two
     * */
    @Test
    void shouldChangeToLevelThree() {
        /* Level Two */
        PopupMenuActions popup = new PopupMenuActions(game, levelTwo);
        popup.onNextLevel();

        // Verify that the screen changed
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_THREE_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onNextLevel function works for level three
     * */
    @Test
    void shouldChangeToLevelFour() {
        /* Level Three */
        PopupMenuActions popup = new PopupMenuActions(game, levelThree);
        popup.onNextLevel();

        // Verify that the screen changed
        verify(game).setScreenType(GdxGame.ScreenType.LEVEL_FOUR_GAME);
        verify(game).setScreen(GdxGame.ScreenType.LOADING);
    }

    /**
     * Tests that the onNextLevel function works for level four. When the
     * player hits 'next level' on the final level, they return to the
     * Main Menu.
     * */
    @Test
    void shouldChangeToMainMenu() {
        /* Level Four */
        PopupMenuActions popup = new PopupMenuActions(game, levelFour);
        popup.onNextLevel();

        // Verify that the screen changed
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Tests that the onHome function changes the screen to the Main Menu
     * screen
     * */
    @Test
    void testOnHomeChanges() {
        // Setup
        PopupMenuActions popup = new PopupMenuActions(game);
        popup.onHome();

        // Verify that the game screen did change
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Tests that the onReplay function changes the screen back to the Main
     * Game screen.
     * */
    @Test
    void testOnReplayChanges() {
        // Setup
        ServiceLocator.registerEntityService(new EntityService());
        Entity camera = new Entity().addComponent(new CameraComponent());
        CameraComponent camComponent = camera.getComponent(CameraComponent.class);

        TerrainFactory terrainFactory = new TerrainFactory(camComponent);
        ForestGameArea forestGameArea = new ForestGameArea(terrainFactory, 0, false);
        forestGameArea.setCheckPointStatus(0);
        PopupMenuActions popup = new PopupMenuActions(game, forestGameArea);
        popup.onReplay();

        // Verify that the game screen did change
        verify(game).setScreenType(GdxGame.ScreenType.MAIN_GAME);
    }

}