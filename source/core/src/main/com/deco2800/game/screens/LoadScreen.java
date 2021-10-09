package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.loadmenu.LoadMenuDisplay;
import com.deco2800.game.components.mainmenu.IntroDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(LoadScreen.class);
    private final Renderer renderer;
    GdxGame game;
    ResourceService resourceService;

    public LoadScreen(GdxGame game, ResourceService resourceService) {
        this.game = game;

        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();
        this.resourceService = ServiceLocator.getResourceService();

        createUI();
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    @Override
    public void dispose() {
        logger.debug("Disposing load menu screen");
        renderer.dispose();
        unloadAssets();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.clear();
    }

    /**
     * Loads all the assets required for the game scenes.
     */
    private void loadAssets() {
        logger.debug("Loading assets");

        ServiceLocator.getResourceService().loadAll();
    }

    /**
     * Unloads assets loaded through loadAssets.
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
    }

    /**
     * Creates the ui for the game intro by create a new introDisplay
     * instance for the game in which IntroDisplay will be called.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Entity ui = new Entity();
        ui.addComponent(new LoadMenuDisplay(game));
        ServiceLocator.getEntityService().register(ui);
    }
}

