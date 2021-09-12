package com.deco2800.game.components;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class LivesComponentTest {

    int lives;
    Entity player;

    @Test
    void getSetLivesTest() {
        lives = 5;
        LivesComponent livesComponent = new LivesComponent(lives);
        LivesComponent livesComponent2 = new LivesComponent(16);

        assertEquals(5,  livesComponent.getLives());
        assertEquals(16,  livesComponent2.getLives());
    }

    @Test
    void addLives() {
        lives = 7;
        LivesComponent livesComponent = new LivesComponent(lives);
        livesComponent.addLives(4);
        assertNotEquals(7,  livesComponent.getLives());
        assertEquals(11, livesComponent.getLives());

    }

}
