package com.deco2800.game.components;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class SprintComponentTest {
    @Test
    void shouldSetGetSprint() {
        SprintComponent sprint = new SprintComponent(100);
        assertEquals(100, sprint.getSprint());

        sprint.setSprint(150);
        assertEquals(150, sprint.getSprint());

        sprint.setSprint(-50);
        assertEquals(0, sprint.getSprint());
    }

    @Test
    void shouldCheckIsDead() {
        SprintComponent sprint = new SprintComponent(100);
        assertFalse(sprint.hasNoSprint());

        sprint.setSprint(0);
        assertTrue(sprint.hasNoSprint());
    }

    @Test
    void shouldAddHealth() {
        SprintComponent sprint = new SprintComponent(100);
        sprint.addSprint(-500);
        assertEquals(0, sprint.getSprint());

        sprint.addSprint(100);
        sprint.addSprint(-20);
        assertEquals(80, sprint.getSprint());
    }
}
