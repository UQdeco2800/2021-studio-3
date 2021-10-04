package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;

public class RollComponent extends Component {

    /* Improves readability of time-based calls */
    private static final int SECONDS = 1000;

    /* Last time the player rolled */
    private long lastRollStarted;

    /* Last time the player stopped rolling */
    private double lastRollEnded;

    /* True when the player is rolling, else false */
    private boolean rolling;

    /* Ensure the player doesn't roll infinitely */
    private boolean rollOnCoolDown;

    /* Defines how long the player must wait between rolls */
    public long ROLL_COOL_DOWN = 4 * SECONDS;

    /* Defines how far the player will roll. */
    public int ROLL_LENGTH = 3;

    /* Defines how long the player rolls for */
    public double ROLL_TIME = 0.4 * SECONDS;

    /* True if the player is falling (rolled off something) */
    private boolean falling;

    public RollComponent() {
        this.rolling = false;
        this.rollOnCoolDown = false;
        this.falling = false;
    }

    /**
     * Handles checking the roll status per frame
     * */
    @Override
    public void update() {
        checkRollStatus();
        entity.getComponent(PlayerStatsDisplay.class).updateRollDisplay(getCoolDownRemaining() / 1000);
    }

    /**
     * Updates the time that the player's current roll will end, based on the
     * time the roll started.
     *
     * All roll times are in absolute milliseconds since game start.
     * */
    public void updateRollEnd() {
        this.lastRollEnded = this.lastRollStarted + ROLL_TIME;
    }

    /**
     * Handles checking if the player needs to stop rolling, and whether they
     * can roll again based on the cool down.
     *
     * - If the ROLL_TIME is up, and the player is rolling, they  will stop
     *   rolling.
     * - If the ROLL_COOL_DOWN has worn off since the last roll, the player is
     *   able to roll again.
     * */
    public void checkRollStatus() {
        GameTime currentTime = ServiceLocator.getTimeSource();

        // Check for ending the roll
        if (rolling) {
            long timeSinceStart = currentTime.getTimeSince(lastRollStarted);
            if (timeSinceStart > ROLL_TIME) {
                handleStopRolling();
            }
        }

        // Check for allowing the player to roll again
        if (rollOnCoolDown) {
            long timeSinceRoll = currentTime.getTimeSince((long) lastRollEnded);
            setCoolDown(timeSinceRoll < ROLL_COOL_DOWN);
        }
    }

    /**
     * Calls set-down code for rolling; the player is set to not rolling,
     * and will stop rolling. The roll cool-down begins. If they have fallen
     * off something while rolling, this is handled.
     * */
    public void handleStopRolling() {
        // Reset the player to their old visual representation
        stopRollAnimation();

        // The players roll cool-down begins
        setCoolDown(true);

        // Make the player stop rolling
        entity.getEvents().trigger("walk", Vector2Utils.DOWN);

        // We are no longer rolling
        setRolling(false);
        entity.getComponent(KeyboardPlayerInputComponent.class).setRolling(this.falling); // if we are falling, dont allow walking
    }

    /**
     * Sets the player's 'rolling' status.
     *
     * @param rolling true if the player is not rolling, else false.
     * */
    public void setRolling(boolean rolling) {
        this.rolling = rolling;
    }

    /**
     * Set-up code for rolling. The roll start time is logged, the player is
     * set to rolling and the new roll ending time is updated. The player
     * then rolls in the given direction.
     *
     * @param direction the direction the player will roll in, either LEFT or
     *                  RIGHT.
     * */
    public void handleRolling(Vector2 direction) {
        // Keep track of when the player rolls
        setLastRollStarted(ServiceLocator.getTimeSource().getTime());

        // Inform relevant classes that the player is rolling
        entity.getComponent(KeyboardPlayerInputComponent.class).setRolling(true);
        this.rolling = true;

        // Set the new roll ending time
        updateRollEnd();

        // Perform the roll
        move(direction.cpy().scl(ROLL_LENGTH));

        // Animate the player
        animateRoll();
    }

    /**
     * Returns whether the player can roll based on whether or not they are
     * currently on a roll cool-down
     *
     * @return true if the player is on cool-down, else false.
     * */
    public boolean cantRoll() {
        return this.rollOnCoolDown;
    }

    /**
     * Triggers the roll cool-down on or off. The player cannot roll while
     * they are on a cool-down.
     *
     * @param coolDownStatus true to set the cool-down to on, else false to
     *                       turn it off.
     * */
    public void setCoolDown(boolean coolDownStatus) {
        this.rollOnCoolDown = coolDownStatus;
    }

    /**
     * Returns the time remaining on the roll cool-down. Once this value hits
     * 0, it will continue into negatives until the player rolls again. Hence,
     * 0 is the minimum bound returned.
     *
     * @return the amount of time, in milliseconds, that the player has
     *         remaining on the roll cool-down.
     * */
    public double getCoolDownRemaining() {
        GameTime currentTime = ServiceLocator.getTimeSource();
        double timeSinceLastRollEnded = currentTime.getTimeSince((long) lastRollEnded);
        double coolDownRemaining = ROLL_COOL_DOWN - timeSinceLastRollEnded;

        return (coolDownRemaining < 0) ? 0 : coolDownRemaining;
    }

    /**
     * Helper function to make code less verbose.
     *
     * Calls the 'walk' function in PlayerActions
     * @see PlayerActions
     * */
    private void move(Vector2 direction) {
        entity.getComponent(PlayerActions.class).walk(direction);
    }

    /**
     * Returns the time at which the most recent roll ended, in absolute
     * milliseconds since game start.
     *
     * @return the time (in milliseconds since game start) that the player
     * stopped rolling
     * */
    public double getLastRollEnded() {
        return this.lastRollEnded;
    }

    /**
     * Sets the time at which the player started their roll.
     *
     * @param rollStart the time, in absolute milliseconds since game start,
     *                  that the player began rolling.
     * */
    public void setLastRollStarted(long rollStart) {
        this.lastRollStarted = rollStart;
    }

    /**
     * Returns whether or not the player is currently rolling.
     *
     * @return true if the player is mid-roll, else false.
     * */
    public boolean getRolling() {
        return this.rolling;
    }

    /**
     * Returns the time at which the player started their current, or most
     * recent roll.
     *
     * @return the time, in absolute milliseconds since game start, that the
     *         player started their current or most recent roll.
     * */
    public long getLastRollStarted() {
        return this.lastRollStarted;
    }

    /**
     * Starts the roll animation when the player rolls
     * */
    public void animateRoll() {
        AnimationRenderComponent render = entity.getComponent(AnimationRenderComponent.class);
        render.startAnimation("roll");
    }

    /**
     * Returns the player to their previous state after rolling
     * */
    public void stopRollAnimation() {
        entity.getComponent(KeyboardPlayerInputComponent.class).updatePlayerStateAnimation();
    }

}
