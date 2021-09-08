package com.deco2800.game.components;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.deco2800.game.GdxGame;

public class LivesComponent extends Component {

    private int lives;
    private GdxGame game;

    public LivesComponent(int lives) {
            this.lives = lives;
        }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        //Will need to create a condition where lives can be added
        //this.lives += lives;

        if (entity != null) {
            entity.getEvents().trigger("updateLives", lives);
        }
    }

    public void addLives(int lives) {
        if (lives < 0) {

        } else {
            this.lives += lives;
        }
    }

}
