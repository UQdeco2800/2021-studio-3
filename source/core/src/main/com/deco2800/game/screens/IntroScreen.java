package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.deco2800.game.GdxGame;
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

public class IntroScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(IntroScreen.class);
    private final Renderer renderer;
    private final GdxGame game;
    private final ResourceService resourceService;
    private static final String[] introScreenMusic = {"sounds/intro_story_background_music.mp3"};
    private static final String[] screenTextures = {
            "images/screen1.png",
            "images/screen2.png",
            "images/screen3.png",
            "images/screen4.png",
            "images/screen5.png"
    };
    private static final String[] mainMenuClickSounds = {"sounds/click.mp3"};

    /**
     * Constructor for the intro screen. Takes the current GdxGame
     * as well as the resource Service as inputs, used to generate
     * the sequence of intro scenes.
     * @param game current game
     * @param resourceService asset loading service
     */
    public IntroScreen(GdxGame game, ResourceService resourceService) {
        this.game = game;

        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();
        this.resourceService = ServiceLocator.getResourceService();

        loadAssets();
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
        logger.debug("Disposing main menu screen");
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
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadMusic(introScreenMusic);

        resourceService.loadTextures(screenTextures);
        resourceService.loadSounds(mainMenuClickSounds);
        ServiceLocator.getResourceService().loadAll();
    }

    /**
     * Unloads assets loaded through loadAssets.
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(screenTextures);
        resourceService.unloadAssets(introScreenMusic);
        resourceService.unloadAssets(mainMenuClickSounds);
    }

    /**
     * Creates the ui for the game intro by create a new introDisplay
     * instance for the game in which IntroDisplay will be called.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Entity ui = new Entity();
        ui.addComponent(new IntroDisplay(game));
        ServiceLocator.getEntityService().register(ui);
    }
}
