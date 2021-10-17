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

public class PlayerWinDisplay extends UIComponent {
    /* Debugging */
    private static final Logger logger = LoggerFactory.getLogger(PlayerWinDisplay.class);

    /* Handler to set up the UI elements of the win screen */
    private PopupUIHandler handler;
    private int score;
    private int progress;
    private int steps;
    private int health;
    private static final String CLICK_SOUND_FILE_PATH = "sounds/click.mp3";
    public PlayerWinDisplay(PopupUIHandler handler, Entity player) {
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

        // create score
        Table scoreTable = new Table();
        scoreTable.center();
        scoreTable.setFillParent(true);
        scoreTable.padBottom(50f).padRight(350f);
        CharSequence scoreText = String.format("SCORE: %d", score);
        Label scoreLabel = new Label(scoreText, skin, "large");
        scoreTable.add(scoreLabel);

        // create progress
        Table progressTable = new Table();
        progressTable.center();
        progressTable.setFillParent(true);
        progressTable.padTop(50f).padRight(350f);
        CharSequence progressText = String.format("PROGRESS: %d%%", progress);
        Label progressLabel = new Label(progressText, skin, "large");
        progressTable.add(progressLabel);

        // create steps
        Table stepsTable = new Table();
        stepsTable.center();
        stepsTable.setFillParent(true);
        stepsTable.padTop(150f).padRight(350f);
        CharSequence stepsText = String.format("STEPS: %d", steps);
        Label stepsLabel = new Label(stepsText, skin, "large");
        stepsTable.add(stepsLabel);

        // create health
        Table healthTable = new Table();
        healthTable.center();
        healthTable.setFillParent(true);
        healthTable.padTop(250f).padRight(350f);
        CharSequence healthText = String.format("HEALTH: %d", health);
        Label healthLabel = new Label(healthText, skin, "large");
        healthTable.add(healthLabel);

        // Create buttons from the images
        Table buttonHolder = new Table();
        buttonHolder.setX(100f);
        ArrayList<Image> buttons =
                handler.setupButtons(buttonHolder, 125, 15, false);
        // Set up actions to trigger for this menu.
        // These must be in order of the buttons on the menu.
        final String[] actions = {"replayLevelWin", "homeMenu", "continue"};
        handler.setupButtonClicks(buttons, actions, entity);

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