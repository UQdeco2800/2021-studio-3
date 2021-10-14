package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.deco2800.game.GdxGame;
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

/**
 * The game screen containing the main menu.
 */
public class LoadingScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(LoadingScreen.class);

    private final Renderer renderer;
    private final GdxGame game;
    private ResourceService resourceService;

    /* Textures of loading screen background and loading bar */
    private static final String[] LoadingTextures = {"images/bar1.png",
            "images/bar2.png", "images/bar3.png", "images/bar4.png",
            "images/bar5.png", "images/bar6.png", "images/bar7.png",
            "images/bar8.png", "images/bar9.png", "images/bar10.png"};

    private static final String[] forestTextures = {
            "images/box_boy_leaf.png",
            "images/tree.png",
            "images/ghost_king.png",
            "images/ghost_1.png",
            "images/lives_icon.png",
            "images/lives_icon2.png",
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
            "images/Death.png",
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
            "images/alien_monster.png",
            "images/alien_monster.png",
            "images/lives_icon2.png",
            "images/roll.png",
            "images/alien_monster_weapon_01.png",
            "images/alien_monster_weapon_02.png",
            "images/alien_solider.png",
            "images/alien_solider_weapon_01.png",
            "images/alien_solider_weapon_02.png",
            "images/alien_boss.png",
            "images/alien_boss_weapon_01.png",
            "images/lives_icon2.png",
            "images/instence_fall.png",
            "images/double_jump.png",
            "images/portal.png",
            "images/Spaceship.png"
    };

    /* Textures only needed for level 2*/
    private static final String[] level2Textures = {"images/background_mars.png",
            "images/background_mars_ground.png",
            "images/background_mars_surface.png",
            "images/background_mars_star.png"};

    /* Textures only needed for level 3 */
    private static final String[] level3Textures = {"images/background_europa.png",
            "images/background_europa_ground.png",
            "images/background_europa_surface.png",
            "images/background_europa_star.png"};

    private static final String[] forestTextureAtlases = {

            "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas",
            "images/boxBoy.atlas", "images/robot.atlas", "images/asteroidFire.atlas",
            "images/ufo_animation.atlas", "images/PlayerMovementAnimations.atlas",
            "images/SerpentLevel1.atlas"
    };

    private static final String[] forestSounds = {"sounds/Impact4.ogg","sounds/buff.mp3","sounds/debuff.mp3"};

    private static String[] forestMusic = {"sounds/maingame.mp3", "sounds/level2.mp3", "sounds/BGM_03_mp3.mp3","sounds/level3.mp3",};


    /**
     * The game screen displayed when the level is loading.
     */
    public LoadingScreen(GdxGame game, ResourceService resourceService) {
        this.game = game;

        logger.debug("Initialising main menu screen services");
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

        // If all assets are loaded, then switch to the correct screen of the game.
        if (resourceService.getAssetManager().update()) {
            switch (game.getScreenType()) {
                case MAIN_GAME:
                    logger.info("Setting screen to MAIN_GAME");
                    this.game.setScreen(GdxGame.ScreenType.MAIN_GAME);
                    break;
                case CHECKPOINT_REPLAY:
                    logger.info("Setting screen to CHECKPOINT_REPLAY");
                    this.game.setScreen(GdxGame.ScreenType.CHECKPOINT_REPLAY);
                    break;
                case CHECKPOINT:
                    logger.info("Setting screen to CHECKPOINT");
                    this.game.setScreen(GdxGame.ScreenType.CHECKPOINT);
                    break;
                case RESPAWN:
                    logger.info("Setting screen to RESPAWN");
                    this.game.setScreen(GdxGame.ScreenType.RESPAWN);
                    break;
                case LEVEL_TWO_GAME:
                    logger.info("Setting screen to LEVEL_TWO_GAME");
                    this.game.setScreen(GdxGame.ScreenType.LEVEL_TWO_GAME);
                    break;
                case LEVEL_THREE_GAME:
                    logger.info("Setting screen to LEVEL_THREE_GAME");
                    this.game.setScreen(GdxGame.ScreenType.LEVEL_THREE_GAME);
                    break;
            }
        } else {
            logger.info("Loading... {}%", (int) (resourceService.getAssetManager().getProgress() * 100));
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

        resourceService.loadTextures(LoadingTextures);
        ServiceLocator.getResourceService().loadAll();

        resourceService.loadTextures(forestTextures);
        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadSounds(forestSounds);
        resourceService.loadMusic(forestMusic);
        if (game.getScreenType() == GdxGame.ScreenType.LEVEL_TWO_GAME) {
            logger.info("loading level2 assets");
            resourceService.loadTextures(level2Textures);

        } else if (game.getScreenType() == GdxGame.ScreenType.LEVEL_THREE_GAME) {
            logger.info("loading level3 assets");
            resourceService.loadTextures(level3Textures);
        }
    }

    /**
     * Unloads the assets required for the loading screen only.
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        resourceService.unloadAssets(LoadingTextures);
    }

    /**
     * Creates the loading screen's ui
     */
    private void createUI() {
        logger.debug("Creating ui");
        Entity ui = new Entity();
        ui.addComponent(new LoadingDisplay());
        ServiceLocator.getEntityService().register(ui);
    }
}
