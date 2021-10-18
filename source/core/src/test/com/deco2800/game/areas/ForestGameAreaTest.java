package com.deco2800.game.areas;

import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.screens.MainGameScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class ForestGameAreaTest {
    @Mock TerrainFactory terrainMock;
    @Mock Entity playerMock;

    private ForestGameArea forest;

    /**
     * Handles set up code before every test
     * */
    @BeforeEach
    void setupTestEnvironment() {
        forest = new ForestGameArea(terrainMock,0,false);
    }

    /**
     * Checks that the checkpoint is set correctly
     * */
    @Test
    void shouldSetAndGetCheckPoint() {
        assertEquals(0, forest.getCheckPointStatus());
        forest.setCheckPointStatus(50);
        assertEquals(50, forest.getCheckPointStatus());
    }

    /**
     * Checks that the player is set correctly
     * */
    @Test
    void shouldSetAndGetPlayer() {
        forest.setPlayer(playerMock);
        assertEquals(playerMock, forest.getPlayer());
    }

    /**
     * Checks that the correct area type is returned
     * */
    @Test
    void shouldReturnAreaTypeOne() {
        assertEquals(MainGameScreen.Level.ONE, forest.getAreaType());
    }

}