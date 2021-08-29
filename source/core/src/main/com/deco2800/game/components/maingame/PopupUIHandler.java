package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Handles setting up the UI for the win, loss and pause menus.
 *
 * One PopupUIHandler is created for a pop-up menu. This class allows for the
 * visuals on the pop-up menus to be easily modified or changed
 * */
public class PopupUIHandler {
    /* Debugging */
    private static final Logger logger =
            LoggerFactory.getLogger(PopupUIHandler.class);

    /* The background image for the menu */
    private String background;

    /* The buttons on the menu, in order of appearance */
    private String[] buttons;

    /**
     * Constructor for the PopupUIHandler
     *
     * @param textures the textures to be used on the menu. Must be ordered as:
     *                 1: the background texture of the menu
     *                 2: the buttons you would like to use on the menu, in
     *                    order of their appearance
     * */
    public PopupUIHandler(String[] textures) {
        this.background = textures[0];

        this.buttons = new String[textures.length];
        for (int i = 1; i < textures.length; i++) {
            this.buttons[i] = textures[i];
        }
    }

    /**
     * Places the background texture of the menu onto the menu.
     *
     * @param backgroundFrame the Table holding the image for the background
     * @return the background image
     * */
    public Image setupBackground(Table backgroundFrame) {
        backgroundFrame.center();
        backgroundFrame.setFillParent(true);
        Image background = new Image(ServiceLocator.getResourceService()
                .getAsset(this.background, Texture.class));
        backgroundFrame.add(background);
        return background;
    }


    /**
     * Creates the buttons for the pop-up menu, and places them on the
     * background in order
     *
     * @param buttonHolder the Table holding the buttons for the menu
     * @param padding the amount of padding to put above the uppermost button.
     *                This will vary depending on the spacing you want on your
     *                background, or how large the background header text is.
     * @return a list of the buttons to which actions will be added
     * */
    public ArrayList<Image> setupButtons(Table buttonHolder, float padding) {
        buttonHolder.center();
        buttonHolder.setFillParent(true);

        ArrayList<Image> buttons = new ArrayList<>();
        for (int i = 1; i < this.buttons.length; i++) {
            Image newButton = new Image(ServiceLocator.getResourceService()
                    .getAsset(this.buttons[i], Texture.class));
            buttons.add(newButton);
        }

        // Add padding to push buttons down
        buttonHolder.padTop(padding);
        addButtons(buttonHolder, buttons);
        return buttons;
    }

    /**
     * Adds the buttons to the pop-up menu.
     *
     * @param buttonHolder the Table holding the buttons for the menu
     * @param buttons the buttons to be added to the menu
     * */
    public void addButtons(Table buttonHolder, ArrayList<Image> buttons) {
        for (Image image : buttons) {
            buttonHolder.add(image).padBottom(5f);
            buttonHolder.row();
        }
    }

    /**
     * Handles setting up the on-click actions for the buttons on the menu.
     *
     * @param buttons the buttons which are on the menu, in order of appearance
     * @param actions the events to trigger when the particular button is
     *                pressed. Must be in the same order as the buttons
     *                placement on the menu itself.
     * @param entity the entity on which Action classes are listening for
     *               push-button events.
     * */
    public void setupButtonClicks(ArrayList<Image> buttons, String[] actions,
            Entity entity) {

        // Add listeners to buttons
        for (int i = 0; i < actions.length; i++) {
            final int index = i;

            buttons.get(i).addListener(new ClickListener() {
                public void clicked (InputEvent event, float x, float y) {
                    entity.getEvents().trigger(actions[index]);
                }
            } );
        }
    }
}
