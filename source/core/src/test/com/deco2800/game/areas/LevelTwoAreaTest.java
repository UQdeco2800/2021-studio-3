package com.deco2800.game.areas;

import com.deco2800.game.areas.terrain.TerrainFactory;
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
class LevelTwoAreaTest {
    @Mock TerrainFactory terrainMock;

    private LevelTwoArea levelTwo;

    /**
     * Handles set up code before every test
     * */
    @BeforeEach
    void setupTestEnvironment() {
        levelTwo = new LevelTwoArea(terrainMock,0,false);
    }

    /**
     * Checks that the correct area type is returned
     * */
    @Test
    void shouldReturnAreaTypeTwo() {
        assertEquals(MainGameScreen.Level.TWO, levelTwo.getAreaType());
    }

}