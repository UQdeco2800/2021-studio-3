package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.maingame.*;
import com.deco2800.game.components.player.PlayerLossPopup;
import com.deco2800.game.components.player.PlayerWinPopup;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.terminal.Terminal;
import com.deco2800.game.ui.terminal.TerminalDisplay;
import com.deco2800.game.components.gamearea.PerformanceDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private static final String[] mainGameTextures = {"images/heart.png"};

  /* Textures for the pause menu */
  private static final String[] pauseMenuTextures =
          {"images/pauseMenuBackground.png",
                  "images/pauseRestart.png",
                  "images/pauseMainMenu.png",
                  "images/pauseResume.png"};

  /* Textures for the win menu */
  private static final String[] winMenuTextures =
          {"images/winMenuBackground.png",
                  "images/winReplay.png",
                  "images/winMainMenu.png",
                  "images/winContinue.png"};

  /* Textures for the loss menu */
  private static final String[] lossMenuTextures =
          {"images/lossMenuBackground.png",
                  "images/lossMainMenu.png",
                  "images/lossReplay.png"};

  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);

  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;

  // We know the map is a ForestGameArea
  // should make more general when new maps are added
  private ForestGameArea currentMap;

  public MainGameScreen(GdxGame game) {
    this.game = game;
    game.setState(GdxGame.GameState.RUNNING);

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();

    logger.debug("Initialising main game screen entities");
    TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
    ForestGameArea forestGameArea = new ForestGameArea(terrainFactory, 0);
    forestGameArea.create();

    this.currentMap = forestGameArea;
    createUI();
  }

  public MainGameScreen(GdxGame game, int checkpoint) {
    this.game = game;
    game.setState(GdxGame.GameState.RUNNING);

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();

    logger.debug("Initialising main game screen entities");
    TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
    ForestGameArea forestGameArea = new ForestGameArea(terrainFactory, 1);
    forestGameArea.create();

    this.currentMap = forestGameArea;
    createUI();
  }

  @Override
  public void render(float delta) {

    if (game.getState() == GdxGame.GameState.RUNNING) {
      physicsEngine.update();
      ServiceLocator.getEntityService().update();
    }
    renderer.render();

  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  @Override
  public void pause() {
    logger.info("Game paused");

  }

  @Override
  public void resume() {
    logger.info("Game resumed");
  }

  @Override
  public void dispose() {
    logger.debug("Disposing main game screen");

    renderer.dispose();
    unloadAssets();

    ServiceLocator.getEntityService().dispose();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getResourceService().dispose();

    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainGameTextures);

    /* Load the textures for the pop-up menus */
    resourceService.loadTextures(pauseMenuTextures);
    resourceService.loadTextures(winMenuTextures);
    resourceService.loadTextures(lossMenuTextures);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainGameTextures);
    resourceService.unloadAssets(pauseMenuTextures);
    resourceService.unloadAssets(winMenuTextures);
    resourceService.unloadAssets(lossMenuTextures);
  }

  /**
   * Creates the main game's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForTerminal();

    Entity ui = new Entity();
    ui.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(new MainGameActions(this.game))
        .addComponent(new MainGameExitDisplay())
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay())
        .addComponent(new PauseGamePopUp(this.game,
                new PopupUIHandler(pauseMenuTextures)))
        .addComponent(new PlayerWinPopup(this.game, currentMap,
                new PopupUIHandler(winMenuTextures)))
        .addComponent(new PlayerLossPopup(this.game, currentMap.getPlayer(),
                new PopupUIHandler(lossMenuTextures)))
        .addComponent(new PopupMenuActions(this.game, this.currentMap));


    ServiceLocator.getEntityService().register(ui);
  }
}