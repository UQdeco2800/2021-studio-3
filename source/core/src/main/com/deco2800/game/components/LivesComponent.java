package com.deco2800.game.components;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.deco2800.game.GdxGame;
import com.deco2800.game.physics.components.HitboxComponent;

public class LivesComponent extends Component {

    private int lives;
    private GdxGame game;

    public LivesComponent(int lives) {
            this.lives = lives;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("updateLives", this::addLives);

    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;

        if (entity != null) {
            entity.getEvents().trigger("updateLives", lives);
        }
    }

    public void addLives(int lives) {
        if (this.lives + lives < 0) {

        } else {
            setLives(this.lives += lives);
        }
    }

}
