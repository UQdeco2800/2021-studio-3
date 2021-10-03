package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;

import java.util.LinkedHashMap;

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
    public int ROLL_LENGTH = 8;

    /* Defines how long the player rolls for */
    public double ROLL_TIME = 0.05 * SECONDS;

    /* Counts the amount of collisions the player disengaged from while rolling */
    private int offCollisions;

    /* True if the player is falling (rolled off something) */
    private boolean falling;

    /* The ROOF, LEFT_WALL and RIGHT_WALL of the map */
    private LinkedHashMap<String, Entity> mapFixtures;

    public RollComponent() {
        this.rolling = false;
        this.rollOnCoolDown = false;
        this.offCollisions = 0;
        this.falling = false;
    }

    /**
     * Handles setting up listeners for collision starts and ends. Tracking
     * collisions allows for finer control of the player while rolling between
     * physics objects.
     * */
    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart",
                this::onCollision);
        entity.getEvents().addListener("collisionEnd",
                this::offCollision);
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
     * collisionEnd listener. Increases the offCollisions count if the player
     * disengages from a collision while rolling.
     *
     * @param player the player
     * @param object the object that the player stopped colliding with while
     *               rolling
     * */
    public void offCollision(Fixture player, Fixture object) {
        this.offCollisions += 1;
    }

    /**
     * collisionStart listener. If the player has rolled off something (eg a
     * platform) and hit another entity beneath them, their position will be
     * reset to stationary.
     *
     * @param player the player
     * @param object the object the player collided with
     * */
    private void onCollision(Fixture player, Fixture object) {
        if (falling && collisionFromBeneath(object)) {
            stopFalling();
        }
    }

    /**
     * Helper method to check if a collision the player had was with an object
     * beneath them or not.
     *
     * @param collision the entity that the player collided with
     * @return true if the entity is beneath the player, else false.
     * */
    public boolean collisionFromBeneath(Fixture collision) {
        BodyUserData collidedWithData = (BodyUserData) collision.getBody().getUserData();
        if (collidedWithData != null && collidedWithData.entity != null) {
            Entity collidedWith = collidedWithData.entity;

            // The player should continue to fall if they hit the map walls.
            if (this.mapFixtures.values().contains(collidedWith)) {
                return false;
            }

            // Player hit something beneath them
            return ((collidedWith.getCenterPosition().y -
                           entity.getCenterPosition().y) <= 0);

        }
        return false; // In null cases, safer to return false.
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
        setRolling(false);
        entity.getComponent(KeyboardPlayerInputComponent.class).setRolling(false);
        stopRolling();
        setCoolDown(true);
        handleCheckingFallRequired();
    }

    /**
     * Informs the RollComponent about the entities which are map fixtures,
     * to allow for finer control when the player hits the edge of the map.
     *
     * @param fixtures the map fixtures (ROOF, LEFT_WALL, RIGHT_WALL)
     * */
    void setMapFixtures(LinkedHashMap<String, Entity> fixtures) {
        this.mapFixtures = fixtures;
    }

    /**
     * Returns the player to the ground (or closest physics object beneath
     * them) if they rolled off an object.
     * */
    public void handleCheckingFallRequired() {
        if (offCollisions != 0) {
            this.falling = true;
            // We fell off something when rolling
            move(Vector2Utils.DOWN);
        }
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

        // Reset count for off-collisions during roll
        offCollisions = 0;

        // Set the new roll ending time
        updateRollEnd();

        // Perform the roll
        move(direction.cpy().scl(ROLL_LENGTH));
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
     * Calls the 'stopRolling' function in PlayerActions
     * @see PlayerActions
     * */
    private void stopRolling() {
        entity.getComponent(PlayerActions.class).stopRolling();
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
     * Helper function to make code less verbose.
     *
     * Calls the 'stopWalking' function in PlayerActions. Also sets the player
     * to no longer be falling.
     * @see PlayerActions
     * */
    private void stopFalling() {
        this.falling = false;
        entity.getComponent(PlayerActions.class).stopWalking();
    }

    /**
     * Returns the amount of off-collisions (disengages from collision) the
     * player encountered while rolling
     *
     * @return the amount of off-collisions.
     * */
    public int getOffCollisions() {
        return this.offCollisions;
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
     * Returns whether or not the player is currently 'falling' ie, they
     * rolled off something while rolling.
     *
     * @return true if the player is falling, else false.
     * */
    public boolean getFalling() {
        return this.falling;
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

}
