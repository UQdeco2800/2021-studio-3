package com.deco2800.game.components.player;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class RollComponentTest {
    @Mock RollComponent mockRoll;
    @Mock GameTime time;
    @Mock KeyboardPlayerInputComponent keyboardMock;
    @Mock PlayerActions actionsMock;
    @Mock PhysicsComponent physicsMock;
    @Mock PhysicsService physicsServiceMock;
    @Mock AnimationRenderComponent mockAnimation;
    @Mock CombatStatsComponent combatStatsMock;

    private RollComponent rollComponent;

    @BeforeEach
    void setup() {
        // Setup physics before everything else
        physicsServiceMock = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsServiceMock);

        rollComponent = new RollComponent();
        physicsMock = new PhysicsComponent();
        mockRoll = new RollComponent();
    }

    /**
     * Tests that the 'roll end' time is set correctly when called.
     * */
    @Test
    void shouldSetRollEndCorrectly() {
        double rollTime = rollComponent.ROLL_TIME;

        // Tell the player they've begun rolling, at time 0.
        rollComponent.setLastRollStarted(0);

        // Update the roll end-time
        rollComponent.updateRollEnd();

        // See if the time the roll ends is correct
        assertEquals(rollTime, rollComponent.getLastRollEnded());
    }

    /**
     * Tests that the player begins to handle the stopping of a roll, if they
     * are currently rolling and the 'ROLL_TIME' is up.
     * */
    @Test
    void shouldCallHandleStopRolling() {
        // Register the time source
        ServiceLocator.registerTimeSource(time);

        // Create the player
        Entity player = new Entity()
                .addComponent(mockRoll)
                .addComponent(keyboardMock)
                .addComponent(actionsMock)
                .addComponent(combatStatsMock);

        // Tell the player they started rolling at time 0
        mockRoll.setLastRollStarted(0);
        mockRoll.setRolling(true);

        // Set the time to be when the player should begin stopping
        double rollStopTime = mockRoll.ROLL_TIME + 1;
        when(time.getTimeSince(anyLong())).thenReturn((long) rollStopTime);

        // This will handleStopRolling if the ROLL_TIME is up
        mockRoll.checkRollStatus();

        // Verify the call was made
        assertFalse(mockRoll.getRolling());
        verify(keyboardMock).setRolling(false);
        assertTrue(mockRoll.cantRoll());
    }

    /**
     * Tests that the cool-down is removed after the COOL_DOWN_TIME has
     * elapsed after the player has stopped rolling.
     * */
    @Test
    void shouldRemoveRollCoolDownAfterCoolDownPeriod() {
        // Register the time source
        ServiceLocator.registerTimeSource(time);

        // Tell the player they started rolling at time 0
        mockRoll.setLastRollStarted(0);
        mockRoll.updateRollEnd();

        // Tell the player they are no longer rolling, and are on cool-down.
        mockRoll.setRolling(false);
        mockRoll.setCoolDown(true);

        // Set the time to be when the player should be able to roll again
        double ableToRollAgain = mockRoll.ROLL_COOL_DOWN + 1;
        when(time.getTimeSince(anyLong())).thenReturn((long) ableToRollAgain);

        // This will set the cool-down to false
        mockRoll.checkRollStatus();

        // Verify the cool-down was set to false
        assertFalse(mockRoll.cantRoll());

    }

    /**
     * Checks that the setRolling and getRolling functionality works
     * */
    @Test
    void shouldChangeRollingStatus() {
        // False by default
        assertFalse(mockRoll.getRolling());

        // Set to true and check
        mockRoll.setRolling(true);
        assertTrue(mockRoll.getRolling());

        // And back to false
        mockRoll.setRolling(false);
        assertFalse(mockRoll.getRolling());
    }

    /**
     * Checks that the roll-state changes and function calls are made
     * correctly in the handleRolling function.
     * */
    @Test
    void shouldMakeChangesInHandleRolling() {
        // Register the time source
        ServiceLocator.registerTimeSource(time);

        // Create the player with mocked components
        Entity player = new Entity()
                .addComponent(rollComponent)
                .addComponent(keyboardMock)
                .addComponent(actionsMock)
                .addComponent(mockAnimation)
                .addComponent(combatStatsMock);

        // Control the time calls
        when(time.getTime()).thenReturn(1000L);

        // Make the call
        rollComponent.handleRolling(Vector2Utils.LEFT);

        // Check everything was set correctly
        assertEquals(1000L, rollComponent.getLastRollStarted());
        verify(keyboardMock).setRolling(true);
        assertTrue(rollComponent.getRolling());
        assertEquals(1000L + rollComponent.ROLL_TIME, rollComponent.getLastRollEnded());
        verify(actionsMock).walk(Vector2Utils.LEFT.scl(rollComponent.ROLL_LENGTH));
    }

    /**
     * Checks that the 'onCoolDown' variable is set and retrieved properly
     * */
    @Test
    void shouldChangeCoolDownVariable() {
        // False by default
        assertFalse(rollComponent.cantRoll());

        // Set to true and check
        rollComponent.setCoolDown(true);
        assertTrue(rollComponent.cantRoll());

        // And back to false
        rollComponent.setCoolDown(false);
        assertFalse(rollComponent.cantRoll());
    }

    /**
     * Checks that the cool-down remaining time is working correctly.
     * */
    @Test
    void shouldReturnCorrectCoolDownRemainingTime() {
        // Register the time source
        ServiceLocator.registerTimeSource(time);

        // Say that 2ms has passed from 'anything'
        when(time.getTimeSince(anyLong())).thenReturn(2000L);

        // The player's cool-down is 4ms (4000)
        // If we are 2ms (2000) since then, the time remaining should be 2000
        assertEquals(2000, rollComponent.getCoolDownRemaining());
    }

}