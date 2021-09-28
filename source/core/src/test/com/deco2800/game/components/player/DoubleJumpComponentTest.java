package com.deco2800.game.components.player;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class DoubleJumpComponentTest {
    @Mock KeyboardPlayerInputComponent keyboardMock;
    @Mock GameTime time;
    private DoubleJumpComponent jumpComponent;

    @BeforeEach
    void setup() {
        jumpComponent = new DoubleJumpComponent();
    }

    /**
     * Tests that the player begins to fall after the JUMP_TIME is up.
     * */
    @Test
    void shouldFallAfterTime() {
        // Register time source
        ServiceLocator.registerTimeSource(time);

        // Create a player with the jump & keyboard components
        Entity player = new Entity()
                .addComponent(jumpComponent)
                .addComponent(keyboardMock);

        // The player jumped at time 0
        jumpComponent.setJumpStartTime(0);
        jumpComponent.setIsJumping(true);

        // Set the time to be when the player should fall
        double currentTime = (0.28 * 1000) + 1;
        when(time.getTimeSince(0)).thenReturn((long) currentTime);

        // Call the check
        jumpComponent.checkJumpOnUpdate();

        // The player should have fallen.
        verify(keyboardMock).handleFalling();
    }

    /**
     * Tests that the isJumping setters and getters are working correctly
     * */
    @Test
    void shouldBeJumping() {
        // Player is not jumping by default
        assertFalse(jumpComponent.getIsJumping());
        jumpComponent.setIsJumping(true);

        // Player should be set to jumping now
        assertTrue(jumpComponent.getIsJumping());
    }

    /**
     * Tests that the notDoubleJumping & nextJumpState function is working
     * correctly
     * */
    @Test
    void checkNotDoubleJumping() {
        // Player is LANDED
        assertTrue(jumpComponent.notDoubleJumping());
        jumpComponent.nextJumpState();

        // Player is JUMPING
        assertTrue(jumpComponent.notDoubleJumping());
        jumpComponent.nextJumpState();

        // Player is DOUBLE_JUMPING
        assertFalse(jumpComponent.notDoubleJumping());
    }

    /**
     * Tests that the nextJumpState function and getJumpState getter is
     * working correctly.
     * */
    @Test
    void checkNextJumpState() {
        // Player is LANDED by default
        assertEquals(jumpComponent.getJumpState(),
                DoubleJumpComponent.JumpingState.LANDED);
        jumpComponent.nextJumpState();

        // Player is now JUMPING
        assertEquals(jumpComponent.getJumpState(),
                DoubleJumpComponent.JumpingState.JUMPING);
        jumpComponent.nextJumpState();

        // Player is now DOUBLE_JUMPING
        assertEquals(jumpComponent.getJumpState(),
                DoubleJumpComponent.JumpingState.DOUBLE_JUMPING);
    }

    /**
     * Tests that the isLanded and setLanded functions are working correctly
     * */
    @Test
    void testGetAndSetLanded() {
        // Player is LANDED by default
        assertTrue(jumpComponent.isLanded());
        jumpComponent.nextJumpState();

        // Player should now be JUMPING
        assertEquals(jumpComponent.getJumpState(),
                DoubleJumpComponent.JumpingState.JUMPING);
        jumpComponent.setLanded();

        // Player should now be LANDED
        assertTrue(jumpComponent.isLanded());
        assertEquals(jumpComponent.getJumpState(),
                DoubleJumpComponent.JumpingState.LANDED);
    }

}