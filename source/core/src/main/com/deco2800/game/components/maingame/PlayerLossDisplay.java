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
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerLossDisplay extends UIComponent {
    /* Debugging */
    private static final Logger logger = LoggerFactory.getLogger(PlayerLossDisplay.class);

    @Override
    public void create() {
        super.create();
        addActors1(); // Testing the new menus
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
                .getAsset("images/lossMenuBackground.png", Texture.class));
        backgroundFrame.add(background);
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
        Image replay = new Image(ServiceLocator.getResourceService()
                .getAsset("images/lossReplay.png", Texture.class));

        Image mainMenu = new Image(ServiceLocator.getResourceService()
                .getAsset("images/lossMainMenu.png", Texture.class));

        // Add padding to push buttons down
        buttonHolder.padTop(200f);

        addButtons(buttonHolder,
                new ArrayList<>(Arrays.asList(mainMenu, replay)));

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
     * */
    private void addActors() {
        /* Create initial table */
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        // Placeholder text for now
        Label popupLabel = new Label("Better luck next time!", skin,
                "large");
        table.add(popupLabel);
        table.row();

        // Placeholder image / buttons for now
        float menuSize = 100f;
        Image popupMenu = new Image(ServiceLocator.getResourceService()
                .getAsset("images/ghost_king.png", Texture.class));
        table.add(popupMenu).size(menuSize).padTop(5f);

        /* Create the buttons for the menu */
        TextButton replayButton = new TextButton("Replay", skin);
        TextButton homeMenuButton = new TextButton("Return to Main Menu",
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

        /* Clean up menu & add the buttons*/
        table.row();
        table.add(replayButton).padTop(15f);
        table.row();
        table.add(homeMenuButton).padTop(15f);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        //
    }
}