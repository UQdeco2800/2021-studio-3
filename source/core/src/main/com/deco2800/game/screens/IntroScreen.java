package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.mainmenu.IntroDisplay;
import com.deco2800.game.components.mainmenu.LoadingDisplay;
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
    private ResourceService resourceService;
    private Table table;
    private Stage stage;

    private static final String[] screenTextures = {
            "images/screen1.png",
            "images/screen2.png",
            "images/screen3.png",
            "images/screen4.png",
            "images/screen5.png"
    };

    public IntroScreen(GdxGame game, ResourceService resourceService) {
        this.game = game;

        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();
        this.resourceService = ServiceLocator.getResourceService();
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();

        // If all assets are loaded, then switch to start screen
        if (resourceService.getAssetManager().update()) {
            if (game.getScreenType() == GdxGame.ScreenType.MAIN_GAME) {
                this.game.setScreen(GdxGame.ScreenType.MAIN_GAME);
            }

        }
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
     * Loads all the assets required for the loading screen as well as the
     * forest game area.
     */
    private void loadAssets() {
        logger.debug("Loading assets");

        ServiceLocator.getResourceService().loadAll();
        resourceService.loadTextures(screenTextures);
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        //ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(screenTextures);
    }

    /**
     *
     */
    private void createUI() {
        logger.debug("Creating ui");
        Entity ui = new Entity();
        ui.addComponent(new IntroDisplay());
        //ui.addComponent(new LoadingDisplay(game)).addComponent(new InputDecorator(stage, 10));;
        ServiceLocator.getEntityService().register(ui);
    }
}
