package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.SaveData.SaveData;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.LevelFourArea;
import com.deco2800.game.areas.LevelThreeArea;
import com.deco2800.game.areas.LevelTwoArea;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.gamearea.PerformanceDisplay;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the level one game area.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private static final String[] mainGameTextures = {"images/heart.png",
          "images/lives_icon2.png"};

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

  /* Textures for the continue loss menu*/
  private static final String[] finalLossTextures =
          {"images/continue.png",
                  "images/no.png",
                  "images/yes.png"};

  /* Textures for the buffs and debuffs */
  private static final String[] buffsAndDebuffsTextures =
          {"images/invincible.png",
                  "images/healthDecrease.png",
                  "images/doubleHurt.png",
                  "images/decrease20Pickup.png",
                  "images/increase20Pickup.png",
                  "images/healthIncrease.png",
                  "images/noJumping.png",
                  "images/infiniteSprint.png"};

  private static final Vector2 CAMERA_POSITION = new Vector2(10f, 7.5f);
  /* background and click effect */
  private static final String[] mainMenuMusic = {"sounds/background.mp3"};
  private static final String[] mainMenuClickSounds = {"sounds/click.mp3"};
  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  public static AssetManager manager =  new  AssetManager();
  public SaveData saveData;

  private ForestGameArea currentMap;
  private final TerrainFactory terrainFactory;
  private Entity ui;

  public enum Level {
    ONE, TWO, THREE, FOUR
  }

  /* Manages buffs & debuffs in the game */
  private BuffManager buffManager;

  /**
   * Load the game screen for level one when the game is starting.
   */
  public MainGameScreen(GdxGame game, ResourceService resourceService,
          MainGameScreen.Level level) {
    this.game = game;
    game.setState(GdxGame.GameState.RUNNING);

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(resourceService);

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();

    logger.debug("Initialising main game screen entities");
    this.terrainFactory = new TerrainFactory(renderer.getCamera());

    load();
    setAreaAndUI(selectGameArea(terrainFactory, 0, false, level));
  }

  public static AssetManager load(){

    manager.load("images/invincible.png", Texture.class);
    manager.load("images/winReplay.png", Texture.class);
    manager.load("images/winMainMenu.png", Texture.class);
    manager.load("images/winContinue.png", Texture.class);
    manager.load("images/doubleHurt.png", Texture.class);
    manager.load("images/infiniteSprint.png", Texture.class);
    manager.load("images/heart.png", Texture.class);
    manager.load("images/noJumping.png", Texture.class);
    manager.load("images/lives_icon2.png", Texture.class);
    manager.finishLoading();
    return manager;
  }

  /**
   * Load the game screen for level one when the game is starting.
   */
  public MainGameScreen(GdxGame game, boolean hasDied,
          ResourceService resourceService, MainGameScreen.Level level) {
    this.game = game;
    game.setState(GdxGame.GameState.RUNNING);

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(resourceService);

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();
    load();
    logger.debug("Initialising main game screen entities");
    this.terrainFactory = new TerrainFactory(renderer.getCamera());

    setAreaAndUI(selectGameArea(terrainFactory, 0, hasDied, level));

  }

  /**
   * Load the game screen for level one when the game is starting.
   */
  public MainGameScreen(GdxGame game, int checkpoint, boolean hasDied,
          ResourceService resourceService, MainGameScreen.Level level) {
    this.game = game;
    game.setState(GdxGame.GameState.RUNNING);

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(resourceService);

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();
    load();
    logger.debug("Initialising main game screen entities");
    this.terrainFactory = new TerrainFactory(renderer.getCamera());

    setAreaAndUI(selectGameArea(terrainFactory, 1, hasDied, level));
  }

  /**
   * Determines, creates and returns the correct ForestGameArea class based on
   * the level passed in. For every different level, a different ForestGameArea
   * type is spawned.
   *
   * @param factory the TerrainFactory for the level
   * @param checkpoint the checkpoint the player reached on their last run
   * @param hasDied whether or not the player has died, ie this area
   *                instantiation is actually the player respawning.
   * @param level the level to spawn: ONE, TWO, THREE or FOUR.
   *
   * @return the new area to be created.
   * */
  public ForestGameArea selectGameArea(TerrainFactory factory, int checkpoint,
          boolean hasDied, MainGameScreen.Level level) {
    switch (level) {
      case ONE:
        return new ForestGameArea(factory, checkpoint, hasDied);
      case TWO:
        return new LevelTwoArea(factory, checkpoint, hasDied);
      case THREE:
        return new LevelThreeArea(factory, checkpoint, hasDied);
      case FOUR:
        return new LevelFourArea(factory, checkpoint, hasDied);
    }
    return null; // Unreachable
  }

  /**
   * Instantiates the new player area, creates the UI for the level and begins
   * the buff spawning mechanism.
   *
   * @param area the area which is going to be created.
   * */
  public void setAreaAndUI(ForestGameArea area) {
    area.create();
    this.currentMap = area;
    createUI();
    area.spawnBuffDebuff(this.buffManager, area.getAreaType());
    saveData = new SaveData(game, area.getPlayer());
    saveData.savePlayerData();
  }

  /**
   * Load the game screen for level one when the game is starting.
   */
  public MainGameScreen(GdxGame game, String saveState, ResourceService resourceService) {
    this.game = game;
    game.setState(GdxGame.GameState.RUNNING);

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(resourceService);

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();
    load();
    logger.debug("Initialising main game screen entities");
    //TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
    this.terrainFactory = new TerrainFactory(renderer.getCamera());
    ForestGameArea forestGameArea = new ForestGameArea(terrainFactory, saveState);
    forestGameArea.create();

    this.currentMap = forestGameArea;
    createUI();
    //forestGameArea.spawnBuffDebuff(this.buffManager);

    saveData = new SaveData(game, forestGameArea.getPlayer());
    saveData.savePlayerData();
  }

  @Override
  public void render(float delta) {
    if (game.getState() == GdxGame.GameState.RUNNING) {
      this.currentMap.introCam(CAMERA_POSITION,7, 2.5f, renderer.getCamera());
      this.currentMap.resetCam(renderer.getCamera());
      physicsEngine.update();
      ServiceLocator.getEntityService().update();
    }
    this.currentMap.isPause(game.getState(), this.currentMap.getAllEntities(), 2.5f);

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

    this.buffManager.disposeAll();

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
    resourceService.loadTextures(finalLossTextures);
    resourceService.loadTextures(buffsAndDebuffsTextures);
    resourceService.loadSounds(mainMenuMusic);
    resourceService.loadSounds(mainMenuClickSounds);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainGameTextures);
    resourceService.unloadAssets(pauseMenuTextures);
    resourceService.unloadAssets(winMenuTextures);
    resourceService.unloadAssets(lossMenuTextures);
    resourceService.unloadAssets(finalLossTextures);
    resourceService.unloadAssets(buffsAndDebuffsTextures);
    resourceService.unloadAssets(mainMenuMusic);
    resourceService.unloadAssets(mainMenuClickSounds);
  }

  /**
   * Creates the main game's ui including components for rendering ui elements
   * to the screen and capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForTerminal();

    this.ui = new Entity();
    ui.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(new MainGameActions(this.game))
        .addComponent(new MainGameExitDisplay())
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay(manager, this.currentMap))
        .addComponent(new PauseGamePopUp(this.game,
                new PopupUIHandler(pauseMenuTextures)))
        .addComponent(new PlayerWinPopup(this.game, this.currentMap,
                new PopupUIHandler(winMenuTextures)))
        .addComponent(new PlayerLossPopup(this.game, this.currentMap.getPlayer(),
                new PopupUIHandler(lossMenuTextures)))
        .addComponent(new FinalLossPopUp(this.game, currentMap.getPlayer(),
                new PopupUIHandler(finalLossTextures)))
        .addComponent(new PopupMenuActions(this.game, this.currentMap))
        .addComponent(this.buffManager = new BuffManager(this,
                this.currentMap));

    ServiceLocator.getEntityService().register(ui);
  }

  /**
   * Returns the current game map
   * */
  public ForestGameArea getCurrentMap() {
      return this.currentMap;
  }
}