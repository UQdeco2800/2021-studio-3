package com.deco2800.game.components.maingame;

import static org.mockito.Mockito.verify;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.areas.terrain.TerrainFactory;
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

        final Renderer renderer;
        final PhysicsEngine physicsEngine;
        ServiceLocator.registerEntityService(new EntityService());
        Entity camera = new Entity().addComponent(new CameraComponent());
        CameraComponent camComponent = camera.getComponent(CameraComponent.class);

        TerrainFactory terrainFactory = new TerrainFactory(camComponent);
        ForestGameArea forestGameArea = new ForestGameArea(terrainFactory, 0);
        forestGameArea.setCheckPointStatus(0);
        PopupMenuActions popup = new PopupMenuActions(game, forestGameArea);
        popup.onReplay();

        // Verify that the game screen did change
        verify(game).setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

}