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
            return switch (state) {
                case STATIONARY -> "normal-stationary";
                case WALK -> "normal-walk";
                case SPRINT -> "normal-sprint";
                case JUMP -> "normal-jump";
                case SPRINT_JUMP -> "normal-sprint-jump";
            };
        }
        else if (health == Health.ROUGH){
            return switch (state) {
                case STATIONARY -> "rough-stationary";
                case WALK -> "rough-walk";
                case SPRINT -> "rough-sprint";
                case JUMP -> "rough-jump";
                case SPRINT_JUMP -> "rough-sprint-jump";
            };
        }
        else if (health == Health.DAMAGED){
            return switch (state) {
                case STATIONARY -> "damaged-stationary";
                case WALK -> "damaged-walk";
                case SPRINT -> "damaged-sprint";
                case JUMP -> "damaged-jump";
                case SPRINT_JUMP -> "damaged-sprint-jump";
            };
        }
        else if (health == Health.DEAD){
            return "dead";
        }
        return null;
    }

    public void updateState(State state){
        this.state = state;
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



}
