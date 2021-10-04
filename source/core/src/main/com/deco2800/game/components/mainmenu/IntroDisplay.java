package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.GdxGame;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.screens.LoadingScreen;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class IntroDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoadingDisplay.class);
    private static final float Z_INDEX = 2f;
    GdxGame game;
    private Table background;
    private Table buttons;
    private TextureRegionDrawable scene1;
    private TextureRegionDrawable scene2;
    private TextureRegionDrawable scene3;
    private TextureRegionDrawable scene4;
    private TextureRegionDrawable scene5;
    private ResourceService resourceService;

    private static final String MUSIC_FILE_PATH = "sounds/background.mp3";


    public IntroDisplay() {
        resourceService = ServiceLocator.getResourceService();
    }

    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("next", this::nextScene);
        entity.getEvents().addListener("skip", this::skipScene);
    }


    private void addActors() {
        background = new Table();
        buttons = new Table();
        background.setFillParent(true);
        buttons.setFillParent(true);
        scene1 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/screen1.png"))));
        scene2 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/screen2.png"))));
        scene3 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/screen3.png"))));
        scene4 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/screen4.png"))));
        scene5 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/screen5.png"))));

        background.setBackground(scene1);

        TextButton nextBtn = new TextButton("Next", skin);
        TextButton skipBtn = new TextButton("Skip", skin);

        buttons.add(nextBtn).padTop(900f).padLeft(900f);
        buttons.add(skipBtn).padTop(900f).padLeft(30f);

        nextBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("next button clicked");
                        entity.getEvents().trigger("next");
                    }
                });


        skipBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("skip button clicked");
                        entity.getEvents().trigger("skip");
                    }
                });

        stage.addActor(background);
        stage.addActor(buttons);
    }


    @Override
    protected void draw(SpriteBatch batch) {
        //
    }

    private void nextScene() {
        if (background.getBackground().equals(scene1)) {
            background.setBackground(scene2);
        } else if (background.getBackground().equals(scene2)) {
            background.setBackground(scene3);
        } else if (background.getBackground().equals(scene3)) {
            background.setBackground(scene4);
        } else if (background.getBackground().equals(scene4)) {
            background.setBackground(scene5);
        } else if (background.getBackground().equals(scene5)) {
            game.setScreen(GdxGame.ScreenType.LOADING);
        }
    }

    private void skipScene() {
        game.setScreen(GdxGame.ScreenType.LOADING);
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        background.clear();
        buttons.clear();
        ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class).stop();
        stage.dispose();
        super.dispose();
    }

}
