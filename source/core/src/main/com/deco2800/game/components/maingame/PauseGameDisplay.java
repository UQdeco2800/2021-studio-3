package com.deco2800.game.components.maingame;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class PauseGameDisplay extends UIComponent {
    /* Debugging */
    private static final Logger logger = LoggerFactory.getLogger(PauseGameDisplay.class);
    private static final float Z_INDEX = 2f;
    private Array<Image> screenElements = new Array<>();
    private PopupUIHandler handler;
    private static final String MUSIC_FILE_PATH = "sounds/background.mp3";
    private static final String CLICK_SOUND_FILE_PATH = "sounds/click.mp3";
    public PauseGameDisplay(PopupUIHandler handler) {
        this.handler = handler;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Creates the visualisation for the menu, including creating the
     * background texture and adding the buttons.
     * */
    private void addActors() {
        // Create the background image
        Table backgroundFrame = new Table();
        Image background = handler.setupBackground(backgroundFrame);
        //playBackgroundMusic();
        // Create buttons from the images
        Table buttonHolder = new Table();
        ArrayList<Image> buttons =
                handler.setupButtons(buttonHolder, 125, 15, false);

        // Set up actions to trigger for this menu.
        // These must be in order of the buttons on the menu.
        final String[] actions = {"replayLevel", "homeMenu", "resume"};
        handler.setupButtonClicks(buttons, actions, entity);

        // Gather images to close the menu
        screenElements.add(background);
        for (Image image : buttons) {
            screenElements.add(image);
        }

        // Add to the stage
        stage.addActor(backgroundFrame);
        stage.addActor(buttonHolder);
    }

    public Array<Image> getScreenElements() {
        return screenElements;
    }

    @Override
    public void draw(SpriteBatch batch) {
        //
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    private void playBackgroundMusic() {
        Music menuSong = ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class);
        menuSong.setLooping(true);
        menuSong.setVolume(0.3f);
        menuSong.play();
    }
    @Override
    public void dispose() {
        ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class).stop();
        super.dispose();
    }
}
