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

/**
 * The different directions a player can be moving
 */
enum Direction {
    LEFT,
    RIGHT
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
     * The players current state
     */
    public Direction direction;

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
        direction = Direction.RIGHT;
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
                    switch (direction) {
                        case LEFT:
                            return "normal-walk-left";
                        case RIGHT:
                            return "normal-walk-right";
                    }
                case SPRINT:
                    switch (direction) {
                        case LEFT:
                            return "normal-sprint-left";
                        case RIGHT:
                            return "normal-sprint-right";
                    }
                case JUMP:
                    switch (direction) {
                        case LEFT:
                            return "normal-jump-left";
                        case RIGHT:
                            return "normal-jump-right";
                    }
                case SPRINT_JUMP:
                    switch (direction) {
                        case LEFT:
                            return "normal-jump-left";
                        case RIGHT:
                            return "normal-jump-right";
                    }
                default:
                    throw new IllegalArgumentException();
            }
        }
        else if (health == Health.ROUGH){
            switch (state) {
                case STATIONARY:
                    return "rough-stationary";
                case WALK:
                    switch (direction) {
                        case LEFT:
                            return "rough-walk-left";
                        case RIGHT:
                            return "rough-walk-right";
                    }
                case SPRINT:
                    switch (direction) {
                        case LEFT:
                            return "rough-sprint-left";
                        case RIGHT:
                            return "rough-sprint-right";
                    }
                case JUMP:
                    switch (direction) {
                        case LEFT:
                            return "rough-jump-left";
                        case RIGHT:
                            return "rough-jump-right";
                    }
                case SPRINT_JUMP:
                    switch (direction) {
                        case LEFT:
                            return "rough-jump-left";
                        case RIGHT:
                            return "rough-jump-right";
                    }
                default:
                    throw new IllegalArgumentException();
            }
        }
        else if (health == Health.DAMAGED){
            switch (state) {
                case STATIONARY:
                    return "damaged-stationary";
                case WALK:
                    switch (direction) {
                        case LEFT:
                            return "damaged-walk-left";
                        case RIGHT:
                            return "damaged-walk-right";
                    }
                case SPRINT:
                    switch (direction) {
                        case LEFT:
                            return "damaged-sprint-left";
                        case RIGHT:
                            return "damaged-sprint-right";
                    }
                case JUMP:
                    switch (direction) {
                        case LEFT:
                            return "damaged-jump-left";
                        case RIGHT:
                            return "damaged-jump-right";
                    }
                case SPRINT_JUMP:
                    switch (direction) {
                        case LEFT:
                            return "damaged-jump-left";
                        case RIGHT:
                            return "damaged-jump-right";
                    }
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
    public void manage(boolean isJumping, boolean isSprinting, boolean movingRight, boolean movingLeft, boolean isStationary){
        if (isJumping && !isSprinting){
            updateState(State.JUMP);
        }
        else if (!isJumping && isSprinting){
            updateState(State.SPRINT);
        }
        else if (isJumping){
            updateState(State.SPRINT_JUMP);
        } else {
            updateState(State.WALK);
        }

        if (movingRight && movingLeft) {
            updateState(State.STATIONARY);
        } else if (movingRight) {
            updateDirection(Direction.RIGHT);
        } else if (movingLeft) {
            updateDirection(Direction.LEFT);
        } else if (isStationary && !isJumping) {
            updateState(State.STATIONARY);
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
     * update the player's current direction state
     * @param direction the new direction the player is moving
     */
    public void updateDirection(Direction direction){
        this.direction = direction;
    }

    /**
     * Gets the player's current Direction state.
     * @return the player's current Direction state
     */
    public Direction getDirection(){
        return direction;
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
