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
class LevelFourAreaTest {
    @Mock TerrainFactory terrainMock;

    private LevelFourArea levelFour;

    /**
     * Handles set up code before every test
     * */
    @BeforeEach
    void setupTestEnvironment() {
        levelFour = new LevelFourArea(terrainMock,0,false);
    }

    /**
     * Checks that the correct area type is returned
     * */
    @Test
    void shouldReturnAreaTypeFour() {
        assertEquals(MainGameScreen.Level.FOUR, levelFour.getAreaType());
    }

}