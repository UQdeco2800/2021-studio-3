package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.LivesComponent;
import com.deco2800.game.components.ProgressComponent;
import com.deco2800.game.components.ScoreComponent;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.components.maingame.BuffManager;
import com.deco2800.game.components.player.DoubleJumpComponent;
import com.deco2800.game.components.player.PlayerAnimationController;
import com.deco2800.game.components.player.PlayerStateComponent;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.BuffFactory;
import com.deco2800.game.entities.factories.EnemyFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final int NUM_TREES = 1;
  private static final int NUM_BUILDINGS = 3;
  private static final int NUM_ROCKS = 8;
  private static final int NUM_PLANETS = 3;
  private static final int NUM_ASTEROIDS = 5;
  private static final int NUM_GHOSTS = 2;
  private static final int NUM_ASTERIODS = 5;
  private static int lives = 5;

  private static final GameTime gameTime = new GameTime();
  private long CAM_START_TIME;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(18, 12);
  private static final float WALL_WIDTH = 0.1f;

  private GdxGame game;
  private static final GridPoint2 CHECKPOINT = new GridPoint2(18, 12);

  private static final GridPoint2 PLATFORM_SPAWN = new GridPoint2(7,14);

  /**
   * Asset loading is now handled in the LoadingScreen class.
   *
   * Add any new textures into it's forestTextures String[].
   * */
  private static final String[] forestTextures = {
          "images/box_boy_leaf.png",
          "images/tree.png",
          "images/ghost_king.png",
          "images/ghost_1.png",
          "images/lives_icon.png",
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
          "images/roll.png",
          "images/roll2.png",
          "images/roll3.png"
  };

  private static final String[] forestTextureAtlases = {

    "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas",
          "images/boxBoy.atlas", "images/robot.atlas", "images/asteroidFire.atlas",
          "images/ufo_animation.atlas", "images/PlayerMovementAnimations.atlas","images/roll.atlas"
          , "images/SerpentLevel1.atlas"
  };

  private static final String[] forestSounds = {"sounds/Impact4.ogg","sounds/buff.mp3","sounds/debuff.mp3"};
  private static final String backgroundMusic = "sounds/maingame.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;

  /* Player on the map */
  private Entity player;

  /* End of this map */
  private Entity endOfMap;

  private int checkpoint;

  private boolean hasDied;

  /* The edges (Entity objects) of this map.  This region defines the
     space the player is allowed to move within. */
  private LinkedHashMap<String, Entity> mapFixtures = new LinkedHashMap<>();


  public ForestGameArea(TerrainFactory terrainFactory, int checkpoint, boolean hasDied) {
    super();
    this.terrainFactory = terrainFactory;
    this.checkpoint = checkpoint;
    this.hasDied = hasDied;
    CAM_START_TIME = gameTime.getTime();
  }

  public ForestGameArea(TerrainFactory terrainFactory, int checkpoint, int lives) {
    super();
    this.terrainFactory = terrainFactory;
    this.checkpoint = checkpoint;
    ForestGameArea.lives = lives;
    CAM_START_TIME = gameTime.getTime();
  }

  /**
   * Returns the player spawned into this area. Allows upper menus to access
   * the players' status.
   * */
  public Entity getPlayer() {
    return player;
  }

  /**
   * Returns the end of the current map.
   * */
  public Entity getEndMap() {
    return endOfMap;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    //loadAssets();

    displayUI();

    spawnTerrain();
    player = spawnPlayer();
    //spawnTrees();


    //spawnGhosts();
    spawnDeathWall();


    //spawnTrees();

    spawnAsteroids();
    spawnAsteroidFires();
    spawnRobot();
    //spawnBuilding();
    //spawnTrees();
    //spawnRocks();
    spawnPlatforms();
    //spawnPlanet1();
    spawnUFO();
    //spawnBuffDebuffPickup();

    //spawnGhosts();
    //spawnGhostKing();
    //createCheckpoint();
    playMusic();

    //createCheckpoint();
//    playMusic();

    //spawnAttackObstacle();
    spawnAlienMonster();
    spawnAlienSoldier();
    spawnAlienBoss();

  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Level One"));
    spawnEntity(ui);
  }

  /**
   * Returns the check point status.
   * */
  public int getCheckPointStatus() {
    return checkpoint;
  }

  /**
   * sets the check point status.
   * */
  public void setCheckPointStatus(int status) {
    checkpoint = status;
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.SIDE_SCROLL_ER);
    spawnEntity(new Entity().addComponent(terrain));

    // Terrain walls
    float tileSize = terrain.getTileSize();
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);
    TiledMapTileLayer layer = new TiledMapTileLayer(tileBounds.x, tileBounds.y, tileBounds.x, tileBounds.y);

    // Left
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y), GridPoint2Utils.ZERO, false, false);
    // Right
    spawnEntityAt(
            this.endOfMap = ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            new GridPoint2(tileBounds.x, 0),
            false,
            false);
    // Top
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
            new GridPoint2(0, tileBounds.y),
            false,
            false);
    // Bottom
    // LOGIC to create level terrain
    int i = 0, x, y, distance;
    // opens the levels file
    try(BufferedReader br = new BufferedReader(new FileReader("level-floors/levelOne.txt"))) {
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
      // parse file to load the floor
      while (line != null) {
        String[] values = line.split(" ");
        distance = Integer.parseInt(values[0]) * 2;
        x = Integer.parseInt(values[1]);
        y = Integer.parseInt(values[2]);

        // creates the floors wall
        spawnEntityAt(
                ObstacleFactory.createWall(Integer.parseInt(values[0]), WALL_WIDTH), new GridPoint2(x, y), false, false);
        if (i != 0) {
          // Create walls when floor level changes
          float height = (float) y/2;
          //float endHeight = (float) (previousY - y)/2;
          spawnEntityAt(
                  ObstacleFactory.createWall(WALL_WIDTH, height), new GridPoint2(x, 0), false, false);
          spawnEntityAt(
                  ObstacleFactory.createWall(WALL_WIDTH, height), new GridPoint2(x + distance, 0), false, false);
        }

        line = br.readLine();
        i++;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    //Kills player upon falling into void
    spawnEntityAt(
            ObstacleFactory.createDeathFloor(worldBounds.x, WALL_WIDTH),
            new GridPoint2(0, -1), false, false);

  }

  /**
   * spawn a death wall that move from left to end
   */
  private void spawnDeathWall() {
    // this.endOfMap.getPosition() causes the death wall to slowly traverse downwards, hence the
    // target's y position is offset 4.5 upwards to remove the bug
    Vector2 deathWallEndPos = new Vector2(this.endOfMap.getPosition().x, this.endOfMap.getPosition().y);
    Entity deathWall = ObstacleFactory.createDeathWall(deathWallEndPos);
    deathWall.getComponent(AnimationRenderComponent.class).scaleEntity();
    deathWall.setScale(3f, terrain.getMapBounds(0).y * terrain.getTileSize());
    spawnEntityAt(deathWall, new GridPoint2(-5, 0), false, false);
  }

  private void spawnUFO() {
    GridPoint2 minPos = new GridPoint2(2, 20);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 10);
    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    //Entity ufo = NPCFactory.createUFO(player);
    Entity ufo = ObstacleFactory.createUfo(player);
    spawnEntityAt(ufo, randomPos, true, true);
  }

  /**
   * spawns the platforms for the level
   * */
  private void spawnPlatforms() {
    spawnPlatform(22, 15);
    spawnPlatform(70, 18);
  }

  /**
   * spawns a platform at a give position
   * param: int x, x position of the platform
   *        int y, y position of the platform
   * */
  private void spawnPlatform(int x, int y) {
    GridPoint2 pos = new GridPoint2(x, y);
    Entity platform = ObstacleFactory.createPlatform2();
    spawnEntityAt(platform, pos, true, false);
  }

  /**private void spawnAsteroids() {
    GridPoint2 minPos = new GridPoint2(2, 20);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 5);
    Random r = new Random();

    for (int i = 0; i < NUM_ASTEROIDS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity asteroid1 = ObstacleFactory.createAsteroid1();
      Entity asteroid2 = ObstacleFactory.createAsteroid2();

      if(r.nextInt(2) == 0) {
        spawnEntityAt(asteroid1, randomPos, true, false);
      } else {
        spawnEntityAt(asteroid2, randomPos, true, false);
      }
    }
  }*/

  /**
   * spawns the asteroids for the level
   * */
  private void spawnAsteroids() {
    //GridPoint2 minPos = new GridPoint2(2, 10);
    //GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 20);

    spawnAsteroid(60, 13);
    spawnAsteroid(9, 10);
    spawnAsteroid(45, 10);

//    for (int i = 0; i < NUM_ASTERIODS; i++) {
//      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
//      Entity asteriod = ObstacleFactory.createAsteriod();
//      spawnEntityAt(asteriod, randomPos, true, false);
//    }
  }

  /**
   * spawns an asteroid at a give position
   * param: int x, x position of the asteroid
   *        int y, y position of the asteroid
   * */
  private void spawnAsteroid(int x, int y) {
    GridPoint2 asteroidPosition1 = new GridPoint2(x, y);
    Entity asteroid1 = ObstacleFactory.createAsteroid();
    spawnEntityAt(asteroid1, asteroidPosition1, true, false);
  }

  /**
   * spawns the asteroidFires for the level
   * */
  private void spawnAsteroidFires() {
    spawnAsteroidFire(5,10);
    spawnAsteroidFire(15,11);
    spawnAsteroidFire(22,8);
    spawnAsteroidFire(36,10);
    spawnAsteroidFire(48,10);
    spawnAsteroidFire(55,13);
  }

  /**
   * spawns an asteroidFire at a give position
   * param: int x, x position of the asteroidFire
   *        int y, y position of the asteroidFire
   * */
  private void spawnAsteroidFire(int x, int y) {
    GridPoint2 pos = new GridPoint2(x,y);
    Entity attackObstacle = ObstacleFactory.createAsteroidAnimatedFire(player);
    spawnEntityAt(attackObstacle, pos, true, false);
  }

  private void spawnRobot() {
    GridPoint2 pos1 = new GridPoint2(60, 13);
    Entity robot1 = ObstacleFactory.createRobot(player);
    spawnEntityAt(robot1, pos1, true, true);
  }

  private void spawnAlienMonster() {
//    GridPoint2 minPos = new GridPoint2(2, 20);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 10);
//    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    GridPoint2 pos1 = new GridPoint2(63, 20);
    Entity alienMonster = EnemyFactory.createAlienMonster(player, this);
    spawnEntityAt(alienMonster, pos1, true, true);
  }

  private void spawnAlienSoldier() {
//    GridPoint2 minPos = new GridPoint2(2, 20);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 10);
//    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    GridPoint2 pos1 = new GridPoint2(83, 20);
    Entity alienSolider = EnemyFactory.createAlienSoldier(player, this);
    spawnEntityAt(alienSolider, pos1, true, true);
  }

  private void spawnAlienBoss() {
//    GridPoint2 minPos = new GridPoint2(2, 20);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 10);
//    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    GridPoint2 pos1 = new GridPoint2(33, 20);
    Entity alienBoss = EnemyFactory.createAlienBoss(player, this);
    spawnEntityAt(alienBoss, pos1, true, true);
  }

  public boolean isDead() {
    return hasDied;
  }

  private Entity spawnPlayer() {
    //need to change it to the horizon view
    float tileSize = terrain.getTileSize();
    Entity newPlayer = PlayerFactory.createPlayer();
    //Adds the progress component for a new created player
    newPlayer.addComponent(new ProgressComponent(0,
            (terrain.getMapBounds(0).x)* tileSize));
    newPlayer.addComponent(new ScoreComponent());
    newPlayer.addComponent(new LivesComponent(lives));

    if (isDead()) {
      lives -= 1;
        newPlayer.getComponent(LivesComponent.class).setLives(lives);
    } else {
      if(lives < 5 && !isDead()) {
        lives = 5;
        newPlayer.getComponent(LivesComponent.class).setLives(lives);
      }
    }

    //spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    if (this.checkpoint == 1) {
      spawnEntityAt(newPlayer, CHECKPOINT, true, true);
    } else {
      spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    }

    /* Inform the player about the map fixtures */
    newPlayer.getComponent(DoubleJumpComponent.class).setMapEdges(this.mapFixtures);
    return newPlayer;
  }

  private void createCheckpoint() {

    GridPoint2 checkPoint = new GridPoint2(36, 12);
    Entity checkpoint = ObstacleFactory.createCheckpoint(player, this);
    spawnEntityAt(checkpoint, checkPoint, true, false);

  }


  /**
   * Spawns buffs or debuffs onto the current map in a random position. Buffs
   * are spawned on the ground only (not platforms). Buffs can spawn anywhere
   * across the game map (horizontally). A random buff type is chosen to be
   * spawned.
   *
   * @param manager the BuffManager which will handle the actions, despawning
   *                and timeout-related functionality of this buff.
   * */
  public void spawnBuffDebuff(BuffManager manager) {
    /* Get a random position based on map bounds */
    GridPoint2 maxPos = new GridPoint2(terrain.getMapBounds(0).x,
            PLAYER_SPAWN.y);
    GridPoint2 randomPos = RandomUtils.random(PLAYER_SPAWN, maxPos);

    /* Pick a random buff */
    Random randomNumber = new Random();
    int pick = randomNumber.nextInt(BuffManager.BuffTypes.values().length);

    /* Create and spawn the buff */
    Entity buff = BuffFactory.createBuff(BuffManager.BuffTypes.values()[pick],
            manager);
    spawnEntityAt(buff, randomPos, true, true);
    //logger.info("Just created and spawned a new buff!");
  }

  /**
   * Spawns a floating animation when a HP buff/debuff is picked up.
   * Spawns at the player and floats up
   * @param pickup the type of animation that will be released based on which
   *               buff is picked up
   * @param manager the Buff Manger which will handle the actions and despawning
   *                functionality of the animation
   * @return the floating animation that is spawned.
   */
  public Entity spawnBuffDebuffPickup(BuffManager.BuffPickup pickup, BuffManager manager) {
    Entity buffPickup = BuffFactory.createBuffAnimation(pickup, manager);
    Vector2 playerPos =
            new Vector2(player.getComponent(PlayerStatsDisplay.class).getPlayerPosition().x + 1f,
                    player.getComponent(PlayerStatsDisplay.class).getPlayerPosition().y + 1f);
    spawnEntityAtVector(buffPickup, playerPos);
    logger.info("Just released a buff pickup");
    return buffPickup;
  }

  private void playMusic() {
    /*Changed*/
    Music music = ServiceLocator.getResourceService().getAsset("sounds/maingame.mp3", Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  /**
   * reset the camera position when refresh every frame
   * @param camera the CameraComponent of the map
   */
  public void resetCam(CameraComponent camera) {
    float playerX = player.getPosition().x;

//    logger.info(String.valueOf(playerX));
    if (playerX > 12 && playerX <= 35) {
      camera.getCamera().translate(playerX - camera.getCamera().position.x + 5, 0,0);
      camera.getCamera().update();
    }
  }

  /**
   * introducing the death wall to player by making camera stay at the start with 3.5 second and move to target with
   * constant speed
   * @param startPos the position of the camera spawn
   * @param distance the distance that the camera is going to move from start point
   * @param duration the total time when the camera is moving
   * @param camera the CameraComponent of the map
   */
  public void introCam(Vector2 startPos, float distance, float duration, CameraComponent camera) {
    long DEATH_WALL_SHOW_DUR = 3500;
    float REFRESH_RATE = 60;
    player.setEnabled(gameTime.getTimeSince(CAM_START_TIME) >= DEATH_WALL_SHOW_DUR + duration * 1000);
    player.getComponent(PlayerAnimationController.class).setEnabled(gameTime.getTimeSince(CAM_START_TIME) >= DEATH_WALL_SHOW_DUR + duration * 1000);

    if (camera.getCamera().position.x - startPos.x < distance
            && gameTime.getTimeSince(CAM_START_TIME) > DEATH_WALL_SHOW_DUR) {
      camera.getCamera().translate((distance/duration)/ REFRESH_RATE, 0,0);
      camera.getCamera().update();
    }
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(forestTextureAtlases);
    resourceService.unloadAssets(forestSounds);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    /*Change back to backgroundMusic*/
    ServiceLocator.getResourceService().getAsset("sounds/maingame.mp3", Music.class).stop();
    this.unloadAssets();

    System.out.println("forest game area disposed");
  }

}
