package com.deco2800.game.components.loadmenu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.game.GdxGame;
import com.deco2800.game.ui.UIComponent;

public class LoadMenuDisplay extends UIComponent {
    GdxGame game;

    public LoadMenuDisplay(GdxGame game) {
        super();
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }


    private void addActors() {
        Label title = new Label("Load Game", skin, "title");
    }

    @Override
    protected void draw(SpriteBatch batch) {
        //
    }
}
