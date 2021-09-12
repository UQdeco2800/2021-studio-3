package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;

enum Health {
    NORMAL, // 100 - 90 Health
    ROUGH, // 90 - 50 Health
    DAMAGED, // 50 - 10 Health
    DEAD // 0 Health
}

enum State {
    STATIONARY,
    WALK,
    SPRINT,
    JUMP,
    SPRINT_JUMP
}

public class PlayerStateComponent extends Component {

    public int jumpCount; // Used for keeping track of how many times the player jumped
    public State state;
    public Health health;

    public PlayerStateComponent(){
        //at the start of the game
        state = State.STATIONARY;
        health = Health.NORMAL;
    }

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
                    return "normal-stationary";
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
                    return "normal-stationary";
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

    public void updateState(State state){
        if (state == State.JUMP){
            jumpCount++;
        }
        this.state = state;
        System.out.println(getStateAnimation());
    }

    public State getState(){
        return state;
    }

    public void updateHealth(Health health){
        this.health = health;
    }

    public Health getHealth(){
        return health;
    }

    public int getJumpCount(){
        return jumpCount;
    }

}
