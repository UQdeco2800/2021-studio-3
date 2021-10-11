package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class FinalLossDisplay extends UIComponent {

    /* Debugging */
    private static final Logger logger =
            LoggerFactory.getLogger(FinalLossDisplay.class);

    /* Handler to handle the UI elements of the loss menu */
    private PopupUIHandler handler;

    public FinalLossDisplay(PopupUIHandler handler) {
            this.handler = handler;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    public void addActors() {
        // Create the background image
        Table background = new Table();
        handler.setupBackground(background);

        // Create buttons from the images
        Table buttonHolder = new Table();
        ArrayList<Image> buttons =
                handler.setupButtons(buttonHolder, 150, 35, true);

        // Set up actions to trigger for this menu.
        // These must be in order of the buttons on the menu.
        final String[] actions = {"homeMenu", "replayLevelLoss"};
        handler.setupButtonClicks(buttons, actions, entity);

        stage.addActor(background);
        stage.addActor(buttonHolder);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        //
    }
}
