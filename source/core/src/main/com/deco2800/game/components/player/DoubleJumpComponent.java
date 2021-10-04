package com.deco2800.game.components.player;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.services.ServiceLocator;

import java.util.LinkedHashMap;

/**
 * Class to handle the player double jumping. This class controls;
 *
 * - the players jumping state
 * - triggering of descent after JUMP_TIME is up, and
 * - determining if collisions which result in the reset of the jumping occur.
 *
 * Standard jumping functionality is still within the
 * KeyboardPlayerInputComponent (@see) class.
 *
 * @see KeyboardPlayerInputComponent
 * */
public class DoubleJumpComponent extends Component {

    /* Improves readability when making time-based calls */
    private static final int SECONDS = 1000;

    /* Total time before player begins to fall */
    private static final double JUMP_TIME = 0.28 * SECONDS;

    /* Magnitude of the jump */
    public final int JUMP_HEIGHT = 4;

    /* Time that the player last jumped */
    private long jumpStartTime;

    /* Whether the player is currently jumping */
    private boolean isJumping;

    /* The players' jumping state */
    private JumpingState jumpState;

    /* The edges of the current game map */
    private LinkedHashMap<String, Entity> mapFixtures;

    /**
     * The players' potential jumping states.
     * */
    public enum JumpingState {
        LANDED, JUMPING, DOUBLE_JUMPING
    }

    public DoubleJumpComponent() {
        this.jumpState = JumpingState.LANDED;
        this.isJumping = false;
    }

    /**
     * Sets up listener for players' collisions with other physics objects.
     * */
    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart",
                this::onCollision);
    }

    /**
     * Handles actions to take when the player collides with something. If the
     * player collides with something beneath them, such as the world or a
     * platform, then the player is put into the 'LANDED' state. In the
     * 'LANDED' state, the player is able to jump (and double jump) again.
     *
     * @param player the player
     * @param object the physics object the player collided with
     * */
    private void onCollision(Fixture player, Fixture object) {
        // We are only concerned with mid-air collisions
        if (isLanded()) {
            return;
        }

        // The player hit something
        BodyUserData collidedWithData = (BodyUserData) object.getBody().getUserData();
        if (collidedWithData != null && collidedWithData.entity != null) {
            Entity collidedWith = collidedWithData.entity;

            /* Check that the player didn't hit the walls or roof */
            if (this.mapFixtures.values().contains(collidedWith)) {
                return;
            }

            // Player should only jump again if they have landed on something
            boolean isBeneath =
                    ((collidedWith.getCenterPosition().y -
                            entity.getCenterPosition().y) <= 0);

            if (isBeneath) {
                setLanded();
            }
        }
    }

    /**
     * Called on every frame. Handles checking if the player should begin
     * falling based on their JUMP_TIME, and triggers the fall if their
     * jumping duration is up.
     * */
    public void checkJumpOnUpdate() {
        long timeJumping = ServiceLocator.getTimeSource().getTimeSince(this.jumpStartTime);

        if (this.isJumping) {
            if (timeJumping >= JUMP_TIME) {
                // Begin to fall
                entity.getComponent(KeyboardPlayerInputComponent.class).handleFalling();
            }
        }
    }

    /**
     * Sets the edges of the current game map. This allows for finer control
     * of the player on collision with a map edge.
     *
     * @param mapFixtures the edges of the map; FLOOR, RIGHT_WALL, LEFT_WALL,
     *                    ROOF.
     * */
    public void setMapEdges(LinkedHashMap<String, Entity> mapFixtures) {
        mapFixtures.remove("FLOOR");
        this.mapFixtures = mapFixtures;
    }

    /**
     * Sets whether or not the player is currently in a 'jumping' state (either
     * single or double jump)
     *
     * @param jumping true if the player is currently jumping, else false.
     * */
    public void setIsJumping(boolean jumping) {
        this.isJumping = jumping;
    }

    /**
     * Return whether or not the player is currently jumping
     *
     * @return true if the player is currently jumping, else false.
     * */
    public boolean getIsJumping() {
        return this.isJumping;
    }

    /**
     * Returns whether the player is double-jumping or not. Used to control
     * whether the player should be allowed to jump again.
     *
     * @return true if the player is not double-jumped, else false.
     * */
    public boolean notDoubleJumping() {
        return this.jumpState != JumpingState.DOUBLE_JUMPING;
    }

    /**
     * Sets the player to the next jumping state, where the ordering is
     * LANDED, JUMPING, DOUBLE_JUMPING.
     * */
    public void nextJumpState() {
        if (this.jumpState == JumpingState.JUMPING) {
            this.jumpState = JumpingState.DOUBLE_JUMPING;
        } else if (this.jumpState == JumpingState.LANDED) {
            this.jumpState = JumpingState.JUMPING;
        }
    }

    /**
     * Returns the players' current jumping state.
     *
     * @return the current jump state of the player. This will either be
     * LANDED, JUMPING or DOUBLE_JUMPING.
     * */
    public JumpingState getJumpState() {
        return this.jumpState;
    }

    /**
     * Sets the start time of the players' current jump. Called when a player
     * jump is processed successfully.
     *
     * @param time the time that the player stated jumping, in absolute
     *             milliseconds since game start.
     * */
    public void setJumpStartTime(long time) {
        this.jumpStartTime = time;
    }

    /**
     * Sets the player to the 'LANDED' jumping state.
     * */
    public void setLanded() {
        this.jumpState = JumpingState.LANDED;
    }

    /**
     * Returns whether or not the player is in the 'LANDED' jumping state.
     *
     * @return true if the player is currently LANDED, else false.
     * */
    public boolean isLanded() {
        return jumpState == JumpingState.LANDED;
    }
}
