package com.deco2800.game.components.loadmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.mainmenu.LoadingDisplay;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoadMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    GdxGame game;
    Table loadTable;


    public LoadMenuDisplay(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }


    private void addActors() {
        loadTable = new Table();
        loadTable.setFillParent(true);
        loadTable.setBackground(new TextureRegionDrawable(
                new Texture(Gdx.files.internal("images/loadScreen3.png"))));
        /*Label title = new Label("Load Game", skin, "title");
        loadTable.add(title);*/

        stage.addActor(loadTable);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        //
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        loadTable.clear();
        stage.dispose();
        super.dispose();
    }
}
