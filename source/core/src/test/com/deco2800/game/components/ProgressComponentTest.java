package com.deco2800.game.components;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
    class ProgressComponentTest {

        @Test
        void setPositionTest() {
            float levelSize = 50;
            ProgressComponent progressComponent = new ProgressComponent(0, levelSize);
            progressComponent.setPosition(20);
            assertEquals(20, progressComponent.getPosition());

            progressComponent.setPosition(15);
            assertNotEquals(30, progressComponent.getPosition());

            progressComponent.setPosition(120);
            assertNotEquals(120, progressComponent.getPosition());

            progressComponent.setPosition(-35);
            assertNotEquals(-35, progressComponent.getPosition());

            progressComponent.setPosition(levelSize);
            assertEquals(levelSize, progressComponent.getPosition());
        }

        @Test
        void getPositionTest() {
            ProgressComponent progressComponent = new ProgressComponent(0, 50);
            assertEquals(0, progressComponent.getPosition());
        }

        @Test
        void getSetProgressTest() {
            ProgressComponent progressComponent = new ProgressComponent(0, 50);

            assertEquals(0, progressComponent.getProgress());

            progressComponent.setPosition(35);
            progressComponent.setProgress();
            assertEquals(70, progressComponent.getProgress());






        }

        @Test
        void upgradeProgressTest() {
            ProgressComponent progressComponent = new ProgressComponent(0, 50);
            progressComponent.updateProgress(25);
            assertEquals(50, progressComponent.getProgress());

            progressComponent.updateProgress(51);
            assertEquals(100, progressComponent.getProgress());
            assertEquals(50, progressComponent.getPosition());


        }



}
