package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.Component;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;


public class PauseGameDisplay extends UIComponent {
    /* Debugging */
    private static final Logger logger = LoggerFactory.getLogger(PauseGameDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private TextButton replayButton;
    private TextButton resumeButton;
    private TextButton homeMenuButton;
    private Image popupMenu;
    private Label popupLabel;
    private Array<Image> screenElements = new Array<>();

    @Override
    public void create() {
        super.create();
        /*addActors();
        screenElements.add(popupLabel, popupMenu, replayButton, resumeButton);
        screenElements.add(homeMenuButton);*/
        addActors1();
    }

    /**
     * Places the background texture onto the menu.
     *
     * @param backgroundFrame the Table holding the image for the background
     * */
    public void setupBackground(Table backgroundFrame) {
        backgroundFrame.center();
        backgroundFrame.setFillParent(true);
        Image background = new Image(ServiceLocator.getResourceService()
                .getAsset("images/pauseMenuBackground.png", Texture.class));
        backgroundFrame.add(background);
        screenElements.add(background);
    }

    /**
     * Adds the buttons to the pop-up menu.
     *
     * @param buttonHolder the Table holding the buttons for the menu
     * @param buttons the buttons to be added to the menu
     * */
    public void addButtons(Table buttonHolder, ArrayList<Image> buttons) {
        for (Image image : buttons) {
            buttonHolder.add(image).padBottom(10f);
            buttonHolder.row();
            screenElements.add(image);
        }
    }

    /**
     * Creates the buttons for the pop-up menu, including adding listeners for
     * players clicking on the buttons
     *
     * @param buttonHolder the Table holding the buttons for the menu
     * */
    public void setupButtons(Table buttonHolder) {
        buttonHolder.center();
        buttonHolder.setFillParent(true);

        Image resume = new Image(ServiceLocator.getResourceService()
                .getAsset("images/pauseResume.png", Texture.class));

        Image replay = new Image(ServiceLocator.getResourceService()
                .getAsset("images/pauseRestart.png", Texture.class));

        Image mainMenu = new Image(ServiceLocator.getResourceService()
                .getAsset("images/pauseMainMenu.png", Texture.class));

        // Add padding to push buttons down
        buttonHolder.padTop(125f);

        addButtons(buttonHolder,
                new ArrayList<>(Arrays.asList(resume, replay, mainMenu)));

        // Add listeners to buttons
        mainMenu.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                entity.getEvents().trigger("homeMenu");
            }
        } );

        replay.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                entity.getEvents().trigger("replayLevel");
            }
        } );

        resume.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                /* Currently there is only one level, continue replays the level */
                entity.getEvents().trigger("resume");
            }
        } );

    }

    /**
     * Creates the visualisation for the menu, including creating the
     * background texture and adding the buttons.
     * */
    private void addActors1() {
        // Create the background image
        Table backgroundFrame = new Table();
        setupBackground(backgroundFrame);

        // Create 4 buttons from images
        Table buttonHolder = new Table();
        setupButtons(buttonHolder);

        // Add to the stage
        stage.addActor(backgroundFrame);
        stage.addActor(buttonHolder);
    }

    /**
     * Creates the visualisation for the menu, and triggers the buttons when
     * pushed
     **/
    private void addActors() {
        /* Create initial table */
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        // Placeholder text for now
        popupLabel = new Label("Game Paused", skin,
                "large");
        table.add(popupLabel);
        table.row();

        // Placeholder image / buttons for now
        float menuSize = 100f;
        popupMenu = new Image(ServiceLocator.getResourceService()
                .getAsset("images/ghost_king.png", Texture.class));
        table.add(popupMenu).size(menuSize).padTop(5f);

        /* Create the buttons for the menu */
        resumeButton = new TextButton("Resume", skin);
        replayButton = new TextButton("Replay", skin);
        homeMenuButton = new TextButton("Return to Main Menu",
                skin);

        /* Add triggers when the buttons are pressed */
        homeMenuButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("homeMenu");
                    }
                });

        replayButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("replayLevel");
                    }
                });

        resumeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("resume");
                        //entity.getEvents().trigger("continue");
                    }
                });


        /* Clean up menu & add the buttons*/
        table.row();
        table.add(resumeButton).padTop(15f);
        table.row();
        table.add(replayButton).padTop(15f);
        table.row();
        table.add(homeMenuButton).padTop(15f);
        stage.addActor(table);
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

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}
