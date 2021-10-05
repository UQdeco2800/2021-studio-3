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

    private static final String[] forestTextures = {
            "images/box_boy_leaf.png",
            "images/tree.png",
            "images/ghost_king.png",
            "images/ghost_1.png",
            "images/lives_icon.png",
            "images/lives_icon2.png",
            "images/grass_1.png",
            "images/grass_2.png",
            "images/grass_3.png",
            "images/hex_grass_1.png",
            "images/hex_grass_2.png",
            "images/hex_grass_3.png",
            "images/iso_grass_1.png",
            "images/iso_grass_2.png",
            "images/iso_grass_3.png",
            "images/box_boy.png",
            "images/underground.png",
            "images/sky.png",
            "images/untouchedCheckpoint.png",
            "images/longBackground.png",
            "images/broken_asteriod.png",
            "images/asteroid_fire1.png",
            "images/robot1.png",
            "images/rock1.png",
            "images/rock2.png",
            "images/rock3.png",
            "images/rock4.png",
            "images/asteroid.png",
            "images/asteroid_2.png",
            "images/platform1.png",
            "images/platform2.png",
            "images/platform3.png",
            "images/platform4.png",
            "images/platform5.png",
            "images/building_1.png",
            "images/planet1.png",
            "images/ufo_2.png",
            "images/rock_platform.png",
            "images/Walking.png",
            "images/WalkingDamage90-50.png",
            "images/WalkingDamage50-10.png",
            "images/Sprint.png",
            "images/SprintDamage(50-90).png",
            "images/SprintDamage(10-50).png",
            "images/Jump.png",
            "images/JumpDamage(50-90).png",
            "images/JumpDamage(10-50).png",
            "images/IdleCharacters.png",
            "images/0percent.png",
            "images/10percent.png",
            "images/20percent.png",
            "images/30percent.png",
            "images/40percent.png",
            "images/50percent.png",
            "images/60percent.png",
            "images/70percent.png",
            "images/80percent.png",
            "images/90percent.png",
            "images/100percent.png",
            "images/background_stars.png",
            "images/background_sky.png",
            "images/background_rock.png",
            "images/background_star.png",
            "images/background_surface.png",
            "images/surface.png",
            "images/vikings_in_space.png",
            "images/main_screens-02.png"
    };

    private static final String[] screenTextures = {
            "images/screen1.png",
            "images/screen2.png",
            "images/screen3.png",
            "images/screen4.png",
            "images/screen5.png"
    };

    private static final String[] forestTextureAtlases = {

            "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas",
            "images/boxBoy.atlas", "images/robot.atlas", "images/asteroidFire.atlas",
            "images/ufo_animation.atlas", "images/PlayerMovementAnimations.atlas"
    };

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

        // If all assets are loaded, then switch to start screen
        /*if (resourceService.getAssetManager().update()) {
            if (game.getScreenType() == GdxGame.ScreenType.MAIN_GAME) {
                this.game.setScreen(GdxGame.ScreenType.LOADING);
            }

        }*/
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
        resourceService.loadTextures(forestTextures);
        resourceService.loadTextures(screenTextures);
        resourceService.loadTextureAtlases(forestTextureAtlases);
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
        ui.addComponent(new IntroDisplay(game));
        //ui.addComponent(new LoadingDisplay(game)).addComponent(new InputDecorator(stage, 10));;
        ServiceLocator.getEntityService().register(ui);
    }
}
