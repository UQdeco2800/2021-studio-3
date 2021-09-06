package com.deco2800.game.components.maingame;

import static org.mockito.Mockito.verify;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.areas.terrain.TerrainFactory;
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
        final Renderer renderer;
        final PhysicsEngine physicsEngine;
        //game.setState(GdxGame.GameState.RUNNING);

        ServiceLocator.registerEntityService(new EntityService());

        PhysicsService physicsService = new PhysicsService();


        Entity camera = new Entity().addComponent(new CameraComponent());
        CameraComponent camComponent = camera.getComponent(CameraComponent.class);

        TerrainFactory terrainFactory = new TerrainFactory(camComponent);
        ForestGameArea forestGameArea = new ForestGameArea(terrainFactory, 0);
        forestGameArea.setCheckPointStatus(0);
        Entity ui = new Entity();
        ui.addComponent(new PopupMenuActions(game, forestGameArea));

        PlayerWinActions popup = new PlayerWinActions(game, ui);
        popup.onContinue();

        // Verify that the game screen did change
        verify(game).setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

}