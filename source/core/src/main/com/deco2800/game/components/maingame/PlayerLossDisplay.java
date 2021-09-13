package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PlayerLossDisplay extends UIComponent {
    /* Debugging */
    private static final Logger logger =
            LoggerFactory.getLogger(PlayerLossDisplay.class);

    /* Handler to handle the UI elements of the loss menu */
    private PopupUIHandler handler;
    private int score;
    public PlayerLossDisplay(PopupUIHandler handler, int score) {
        this.handler = handler;
        this.score = score;
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
        handler.setupBackground(backgroundFrame);

        // Create buttons from the images
        Table buttonHolder = new Table();
        ArrayList<Image> buttons =
                handler.setupButtons(buttonHolder, 115, 35);

        // Set up actions to trigger for this menu.
        // These must be in order of the buttons on the menu.
        final String[] actions = {"homeMenu", "replayLevelLoss"};
        handler.setupButtonClicks(buttons, actions, entity);

        Table scoreTable = new Table();
        scoreTable.top().left();
        scoreTable.setFillParent(true);
        scoreTable.padTop(400f).padLeft(350f);
        // Create Score
        CharSequence scoreText = String.format("SCORE: %d", score);
        Label scoreLabel = new Label(scoreText, skin, "large");
        scoreTable.add(scoreLabel);
        // Add to the stage
        stage.addActor(backgroundFrame);
        stage.addActor(buttonHolder);
        stage.addActor(scoreTable);
    }

    @Override
    public void draw(SpriteBatch batch) {
        //
    }
}