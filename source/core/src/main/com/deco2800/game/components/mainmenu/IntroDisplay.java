package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.GdxGame;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles the display of the intro scenes.
 */
public class IntroDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(IntroDisplay.class);
    private static final float Z_INDEX = 2f;
    GdxGame game;
    private Table background;
    private Table buttons;

    /* Drawable texture regions for each scene */
    private TextureRegionDrawable scene1;
    private TextureRegionDrawable scene2;
    private TextureRegionDrawable scene3;
    private TextureRegionDrawable scene4;
    private TextureRegionDrawable scene5;

    /* Main menu intro music */
    private static final String MUSIC_FILE_PATH = "sounds/intro_story_background_music.mp3";


    /**
     * Constructor for the display of the game intro scenes. Takes the current
     * GdxGame as a parameter, as defined in the IntroScreen.java class.
     * @param game game defined by IntroScreen.java
     */
    public IntroDisplay(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("next", this::nextScene);
        entity.getEvents().addListener("skip", this::skipScene);
    }

    /**
     * Creates actors and positions them on the stage using background table
     * for th background and buttons table for the buttons.
     * @see Table for positioning options
     */
    private void addActors() {
        //Background and buttons tables
        background = new Table();
        buttons = new Table();
        buttons.bottom().right();
        background.setFillParent(true);
        buttons.setFillParent(true);

        playBackgroundMusic();

        //Creating Drawable texture regions of the intro scenes
        scene1 = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture(Gdx.files.internal("images/screen1.png"))));

        scene2 = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture(Gdx.files.internal("images/screen2.png"))));

        scene3 = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture(Gdx.files.internal("images/screen3.png"))));

        scene4 = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture(Gdx.files.internal("images/screen4.png"))));

        scene5 = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture(Gdx.files.internal("images/screen5.png"))));

        background.setBackground(scene1);

        //Next and skip buttons; next continues slideshow while skip heads to main game.
        TextButton nextBtn = new TextButton("Next", skin);
        TextButton skipBtn = new TextButton("Skip", skin);

        buttons.add(nextBtn).padBottom(15f).padRight(10f);
        buttons.add(skipBtn).padBottom(15f).padRight(15f);;

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
        Gdx.input.setInputProcessor(stage);
        stage.addActor(background);
        stage.addActor(buttons);
    }


    @Override
    protected void draw(SpriteBatch batch) {
        //
    }

    /**
     * Method call actives the next scene button, to trigger the scene change.
     * Once all scenes have been played, starts the main game.
     */
    public void nextScene() {
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

    /**
     * Method call actives the skip scene button, which immediately starts the game.
     */
    public void skipScene() {
        game.setScreen(GdxGame.ScreenType.LOADING);
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    private void playBackgroundMusic() {
        Music menuSong = ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class);
        menuSong.setLooping(true);
        menuSong.setVolume(0.5f);
        menuSong.play();
    }
    @Override
    public void dispose() {
        background.clear();
        buttons.clear();
        stage.dispose();
        super.dispose();
    }

}
