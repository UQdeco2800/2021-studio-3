package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class IntroDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoadingDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table background;
    private Table buttons;
    private ResourceService resourceService;


    public IntroDisplay() {
        resourceService = ServiceLocator.getResourceService();
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }

    @Override
    public void create() {
        super.create();
        addActors();
    }


    private void addActors() {
        background = new Table();
        buttons = new Table();
        background.setFillParent(true);
        buttons.setFillParent(true);
        Texture scene1 = new Texture(Gdx.files.internal("images/screen1.png"));
        background.setBackground(new TextureRegionDrawable(new TextureRegion(scene1)));

        TextButton nextBtn = new TextButton("Next", skin);
        TextButton skipBtn = new TextButton("Skip", skin);

        buttons.add(nextBtn);
        buttons.add(skipBtn);

        stage.addActor(background);
    }

    private void nextScene() {

    }

    private void skipScene() {

    }
}
