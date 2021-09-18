package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.ProgressComponent;
import com.deco2800.game.components.ScoreComponent;
import com.deco2800.game.entities.Entity;
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
    private int progress;
    private int steps;
    private int health;

    public PlayerLossDisplay(PopupUIHandler handler, Entity player) {
        this.handler = handler;
        this.score = player.getComponent(ScoreComponent.class).getScore();
        this.progress = Math.round(player.getComponent(ProgressComponent.class).getProgress());
        this.steps =  Math.round(player.getPosition().x * 10);
        this.health = player.getComponent(CombatStatsComponent.class).getHealth();
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
                handler.setupButtons(buttonHolder, 410, 35, true);

        // Set up actions to trigger for this menu.
        // These must be in order of the buttons on the menu.
        final String[] actions = {"homeMenu", "replayLevelLoss"};
        handler.setupButtonClicks(buttons, actions, entity);

        // crete score
        Table scoreTable = new Table();
        scoreTable.center();
        scoreTable.setFillParent(true);
        scoreTable.padBottom(50f).padRight(300f);
        CharSequence scoreText = String.format("SCORE: %d", score);
        Label scoreLabel = new Label(scoreText, skin, "large");
        scoreTable.add(scoreLabel);

        // create progress
        Table progressTable = new Table();
        progressTable.center();
        progressTable.setFillParent(true);
        progressTable.padRight(300f).padTop(50f);
        CharSequence progressText = String.format("PROGRESS: %d%%", progress);
        Label progressLabel = new Label(progressText, skin, "large");
        progressTable.add(progressLabel);

        // create steps
        Table stepsTable = new Table();
        stepsTable.center();
        stepsTable.setFillParent(true);
        stepsTable.padTop(150f).padRight(300f);
        CharSequence stepsText = String.format("STEPS: %d", steps);
        Label stepsLabel = new Label(stepsText, skin, "large");
        stepsTable.add(stepsLabel);

        // create health
        Table healthTable = new Table();
        healthTable.center();
        healthTable.setFillParent(true);
        healthTable.padRight(300f).padTop(250f);
        CharSequence healthText = String.format("HEALTH: %d", health);
        Label healthLabel = new Label(healthText, skin, "large");
        healthTable.add(healthLabel);

        // Add to the stage
        stage.addActor(backgroundFrame);
        stage.addActor(buttonHolder);
        stage.addActor(scoreTable);
        stage.addActor(progressTable);
        stage.addActor(stepsTable);
        stage.addActor(healthTable);
    }

    @Override
    public void draw(SpriteBatch batch) {
        //
    }
}