package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("continue", this::onContinue);

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

    /**
     * Removes the pause button menu when game is resumed.
     */
    private void onContinue() {
        replayButton.remove();
        resumeButton.remove();
        homeMenuButton.remove();
        popupMenu.remove();
        popupLabel.remove();
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
