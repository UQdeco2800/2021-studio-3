package com.deco2800.game.components.player;


import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(GameExtension.class)
public class PlayerStateComponentTest {
    @Test
    void getStateAnimationTest(){
        PlayerStateComponent playerState = new PlayerStateComponent();
        playerState.updateHealth(Health.ROUGH);
        playerState.updateState(State.SPRINT);

        assertEquals("rough-sprint", playerState.getStateAnimation());

        playerState.updateHealth(Health.DAMAGED);

        assertEquals("damaged-sprint", playerState.getStateAnimation());

        playerState.updateState(State.STATIONARY);

        assertEquals("damaged-stationary", playerState.getStateAnimation());
    }
    @Test
    void manageTest(){
        PlayerStateComponent playerState = new PlayerStateComponent();

        playerState.updateState(State.WALK);

        playerState.manage(true, true);
        assertEquals(State.SPRINT_JUMP, playerState.getState());

        playerState.manage(false, true);
        assertEquals(State.SPRINT, playerState.getState());

        playerState.manage(true, false);
        assertEquals(State.JUMP, playerState.getState());

        playerState.updateState(State.WALK);

        playerState.manage(false, false);
        assertEquals(State.WALK, playerState.getState());
    }

    @Test
    void updateStateTest(){
        PlayerStateComponent playerState = new PlayerStateComponent();
        assertEquals(State.STATIONARY, playerState.getState());

        playerState.updateState(State.WALK);

        assertEquals(State.WALK, playerState.getState());

        playerState.updateState(State.SPRINT_JUMP);

        assertEquals(State.SPRINT_JUMP, playerState.getState());
    }

    @Test
    void getStateTest(){
        PlayerStateComponent playerState = new PlayerStateComponent();
        assertEquals(State.STATIONARY, playerState.getState());

        playerState.state = State.SPRINT;

        assertEquals(State.SPRINT, playerState.getState());
    }

    @Test
    void updateHealthTest(){
        PlayerStateComponent playerState = new PlayerStateComponent();
        assertEquals(Health.NORMAL, playerState.getHealth());

        playerState.updateHealth(Health.DAMAGED);

        assertEquals(Health.DAMAGED, playerState.getHealth());
    }

    @Test
    void getHealthTest(){
        PlayerStateComponent playerState = new PlayerStateComponent();
        assertEquals(Health.NORMAL, playerState.getHealth());

        playerState.health = Health.DEAD;

        assertEquals(Health.DEAD, playerState.getHealth());
    }
}
