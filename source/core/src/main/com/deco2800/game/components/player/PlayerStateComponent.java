package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;

enum State {
    STATIONARY,
    WALK,
    SPRINT,
    JUMP,
    SPRINT_JUMP,
    DEAD
}

public class PlayerStateComponent extends Component {
    public State state;

    public PlayerStateComponent(){
        //at the start of the game
        this.state = State.STATIONARY;
    }

    public void updateState(State state){
        this.state = state;
    }

    public State getState(){
        return this.state;
    }

}
