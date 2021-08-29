package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.maingame.PlayerWinActions;
import com.deco2800.game.components.maingame.PlayerWinDisplay;
import com.deco2800.game.components.maingame.PopupUIHandler;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class controlling the popup menu that appears when a player wins the level;
 * ie reaches the right-most wall.
 *
 * This class may need to change in future depending on how (and if)
 * right-scroll is added to the game.
 * */
public class PlayerWinPopup extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlayerLossPopup.class);

    /* The player the menu is listening for collisions on */
    private Entity player;

    /* The end of the map. The menu checks for collisions between the player and this */
    private Entity endOfMap;

    /* Lets the buttons change the game screen */
    private GdxGame game;

    /* Handler to set up the UI elements of the win screen */
    private PopupUIHandler handler;

    public PlayerWinPopup(GdxGame game, GameArea currentMap,
            PopupUIHandler winHandler) {
        this.game = game;
        this.player = ((ForestGameArea) currentMap).getPlayer();
        this.endOfMap = ((ForestGameArea) currentMap).getEndMap();
        this.handler = winHandler;
    }

    /**
     * Returns the Fixture associated with the end of the map
     * */
    public Fixture getMapFixture() {
        return endOfMap.getComponent(ColliderComponent.class).getFixture();
    }

    @Override
    public void create() {
        super.create();
        player.getEvents().addListener("collisionStart",
                this::onCollision);
    }

    /**
     * Creates the UI for the PlayerWinPopup menu.
     * */
    public void createUI() {
        logger.debug("Creating player win ui");
        Entity ui = new Entity();

        ui.addComponent(new PlayerWinActions(game))
                .addComponent(new PlayerWinDisplay(handler));

        ServiceLocator.getEntityService().register(ui);
    }

    /**
     * Called when a collision between the player and some other object is
     * detected.
     *
     * @param player the player object that has been hit
     * @param wall the object (possibly the end wall) the player collided with
     * */
    public void onCollision(Fixture player, Fixture wall) {
        if (wall == this.getMapFixture()) {
            createUI();
            game.setState(GdxGame.GameState.OVER);
        }
    }

    @Override
    public void draw(SpriteBatch batch)  {
        //
    }
}