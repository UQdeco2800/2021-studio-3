package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;

/**
 * Stores health interval values for different animations
 */
enum Health {
    NORMAL, // 100 - 90 Health
    ROUGH, // 90 - 50 Health
    DAMAGED, // 50 - 10 Health
    DEAD // 0 Health
}

/**
 * The different states a player can be in
 */
enum State {
    STATIONARY,
    WALK,
    SPRINT,
    JUMP,
    SPRINT_JUMP
}

public class PlayerStateComponent extends Component {

    /**
     * Used for keeping track of how many times the player jumped
     */
    public int jumpCount;

    /**
     * The players current state
     */
    public State state;

    /**
     * The player's current Health state
     */
    public Health health;

    /**
     * Initialises the current State and Health values
     */
    public PlayerStateComponent(){
        //at the start of the game
        state = State.STATIONARY;
        health = Health.NORMAL;
    }

    /**
     * Determines which state the player should be in, at different health intervals
     * @return String: the animation name of the current state
     */
    public String getStateAnimation(){
        String animationName;
        if (health == Health.NORMAL){
            switch (state) {
                case STATIONARY:
                    return "normal-stationary";
                case WALK:
                    return "normal-walk";
                case SPRINT:
                    return "normal-sprint";
                case JUMP:
                case SPRINT_JUMP:
                    return "normal-jump";
                default:
                    throw new IllegalArgumentException();
            }
        }
        else if (health == Health.ROUGH){
            switch (state) {
                case STATIONARY:
                    return "rough-stationary";
                case WALK:
                    return "rough-walk";
                case SPRINT:
                    return "rough-sprint";
                case JUMP:
                case SPRINT_JUMP:
                    return "rough-jump";
                default:
                    throw new IllegalArgumentException();
            }
        }
        else if (health == Health.DAMAGED){
            switch (state) {
                case STATIONARY:
                    return "damaged-stationary";
                case WALK:
                    return "damaged-walk";
                case SPRINT:
                    return "damaged-sprint";
                case JUMP:
                case SPRINT_JUMP:
                    return "damaged-jump";
                default:
                    throw new IllegalArgumentException();
            }
        }
        else if (health == Health.DEAD){
            return "dead";
        }
        return null;
    }

    /**
     * handles situations do with player sprinting and jumping at the same time
     * @param isJumping Whether the player is jumping
     * @param isSprinting Whether the player is sprinting
     */
    public void manage(boolean isJumping, boolean isSprinting){
        if (isJumping && !isSprinting){
            updateState(State.JUMP);
        }
        else if (!isJumping && isSprinting){
            updateState(State.SPRINT);
        }
        else if (isJumping){
            updateState(State.SPRINT_JUMP);
        }
    }

    /**
     * updates the player's current state
     * @param state The new state the player is in.
     */
    public void updateState(State state){
        if (state == State.JUMP){
            jumpCount++;
        }
        this.state = state;
    }

    /**
     * Gets the player's current state.
     * @return the player's current state
     */
    public State getState(){
        return state;
    }

    /**
     * update the player's current health state
     * @param health the new health interval the player is in
     */
    public void updateHealth(Health health){
        this.health = health;
    }

    /**
     * Gets the player's current Health state.
     * @return the player's current Health state
     */
    public Health getHealth(){
        return health;
    }

    /**
     * @return The amount of time the player has jumped in the current game.
     */
    public int getJumpCount(){
        return jumpCount;
    }

}
