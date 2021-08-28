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

public class PlayerLossDisplay extends UIComponent {
    /* Debugging */
    private static final Logger logger = LoggerFactory.getLogger(PlayerLossDisplay.class);

    @Override
    public void create() {
        super.create();
        addActors();
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