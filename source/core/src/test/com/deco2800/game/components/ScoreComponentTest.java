package com.deco2800.game.components;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class ScoreComponentTest {

    @Test
    void setScoreTest() {
        ScoreComponent scoreComponent = new ScoreComponent();
        scoreComponent.setScore(100);
        assertEquals(100, scoreComponent.getScore());

        scoreComponent.setScore(20);
        assertEquals(20, scoreComponent.getScore());

        scoreComponent.setScore(0);
        assertEquals(0, scoreComponent.getScore());
    }


}