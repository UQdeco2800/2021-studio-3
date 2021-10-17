package com.deco2800.game.areas.terrain;

import static org.junit.jupiter.api.Assertions.*;
import com.badlogic.gdx.Gdx;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.screens.MainGameScreen;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class TerrainFactoryTest {
    @Mock CameraComponent cameraComponent;
    @Mock GdxGame game;

    @Test
    void getYOfSurfaceTest() {
        TerrainFactory terrainFactory = new TerrainFactory(cameraComponent);
        int y = terrainFactory.getYOfSurface(2, MainGameScreen.Level.ONE);
        assertEquals(5, y);
    }

}