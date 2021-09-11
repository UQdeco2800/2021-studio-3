package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.BuffTaskComponent;
import com.deco2800.game.components.ProgressComponent;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.components.maingame.BuffManager;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.tasks.MovementTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.BuffFactory;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(0, 11);
  private static final GridPoint2 CHECKPOINT = new GridPoint2(20, 11);
  private static final GridPoint2 PLATFORM_SPAWN = new GridPoint2(7,14);
  private static final float WALL_WIDTH = 0.1f;
  private static final String[] forestTextures = {
    "images/box_boy_leaf.png",
    "images/tree.png",
    "images/ghost_king.png",
    "images/ghost_1.png",
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
          "images/surface.png",
          "images/underground.png",
          "images/sky.png",
          "images/untouchedCheckpoint.png",

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
          "images/ufo_2.png"

  };
  private static final String[] forestTextureAtlases = {
    "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas", "images/boxBoy.atlas"
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;

  /* Player on the map */
  private Entity player;

  /* End of this map */
  private Entity endOfMap;

  private int checkpoint;
  public ForestGameArea(TerrainFactory terrainFactory, int checkpoint) {
    super();
    this.terrainFactory = terrainFactory;
    this.checkpoint = checkpoint;
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
    loadAssets();

    displayUI();

    spawnTerrain();
    player = spawnPlayer();
    //spawnTrees();

    spawnGhosts();

    //spawnTrees();
    spawnAsteriod();
    spawnAsteroidFire();
    spawnRobot();


    //spawnBuilding();
    //spawnTrees();
    //spawnRocks();
    spawnPlatform1();
    //spawnPlanet1();
    spawnUFO();
    //spawnBuffDebuffPickup();
    //spawnAsteroids();

    //spawnGhosts();
    //spawnGhostKing();
    createCheckpoint();
//    playMusic();
    //spawnAttackObstacle();
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
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
    spawnEntityAt(
            //change a wall with high:10
        ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), new GridPoint2(0, 10), false, false);
  }

  private void spawnUFO() {
    GridPoint2 minPos = new GridPoint2(2, 20);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 10);
    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    //Entity ufo = NPCFactory.createUFO(player);
    Entity ufo = ObstacleFactory.createUfo(player);
    spawnEntityAt(ufo, randomPos, true, true);
  }

  //private void spawnTrees() {
  //  //need to change it to the horizon view
  //  GridPoint2 minPos = new GridPoint2(2, 10);
  //  GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 20);
  //  for (int i = 0; i < NUM_TREES; i++) {
  //    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
  //    Entity tree = ObstacleFactory.createTree();
  //    spawnEntityAt(tree, randomPos, true, false);
  //  }
  //}
//
  //private void spawnPlanet1() {
  //  GridPoint2 minPos = new GridPoint2(2, 20);
  //  GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 5);
  //  for (int i = 0; i < NUM_PLANETS; i++) {
  //    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
  //    Entity planet1 = ObstacleFactory.createPlanet1();
  //    spawnEntityAt(planet1, randomPos, true, false);
  //  }
  //}
//
//
  //private void spawnBuilding() {
  //  GridPoint2 minPos = new GridPoint2(2, 10);
  //  GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 20);
  //  for (int i = 0; i < NUM_BUILDINGS; i++) {
  //    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
  //    Entity building = ObstacleFactory.createBuilding1();
  //    spawnEntityAt(building, randomPos, true, false);
  //  }
  //}
//
  //private void spawnRocks() {
  //  GridPoint2 minPos = new GridPoint2(5, 10);
  //  GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 20);
  //  Random r = new Random();
//
  //  for (int i = 0; i < NUM_ROCKS; i++) {
  //    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
  //    Entity rock1 = ObstacleFactory.createRock1();
  //    Entity rock2 = ObstacleFactory.createRock2();
  //    Entity rock3 = ObstacleFactory.createRock3();
  //    Entity rock4 = ObstacleFactory.createRock4();
//
  //    if(r.nextInt(4) == 0) {
  //      spawnEntityAt(rock1, randomPos, true, false);
  //    } else if(r.nextInt(4) == 1) {
  //      spawnEntityAt(rock2, randomPos, true, false);
  //    } else if(r.nextInt(4) == 2) {
  //      spawnEntityAt(rock3, randomPos, true, false);
  //    } else {
  //      spawnEntityAt(rock4, randomPos, true, false);
  //    }
  //  }
  //}
//
  private void spawnPlatform1() {
    Entity platform1 = ObstacleFactory.createPlatform1();
    spawnEntityAt(platform1, PLATFORM_SPAWN, true, false);

    GridPoint2 pos2 = new GridPoint2(20, 13);
    Entity platform2 = ObstacleFactory.createPlatform2();
    spawnEntityAt(platform2, pos2, true, false);
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

  private void spawnAsteriod() {
    //need to change it to the horizon view
    //GridPoint2 minPos = new GridPoint2(2, 10);
    //GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 20);

    GridPoint2 asteriodPosition1 = new GridPoint2(5, 10);
    Entity asteriod1 = ObstacleFactory.createAsteroid();
    spawnEntityAt(asteriod1, asteriodPosition1, true, false);

    GridPoint2 asteriodPosition2 = new GridPoint2(9, 10);
    Entity asteriod2 = ObstacleFactory.createAsteroid();
    spawnEntityAt(asteriod2, asteriodPosition2, true, false);

    GridPoint2 asteriodPosition3 = new GridPoint2(14, 10);
    Entity asteriod3 = ObstacleFactory.createAsteroid();
    spawnEntityAt(asteriod3, asteriodPosition3, true, false);
//    for (int i = 0; i < NUM_ASTERIODS; i++) {
//      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
//      Entity asteriod = ObstacleFactory.createAsteriod();
//      spawnEntityAt(asteriod, randomPos, true, false);
//    }
  }

  private void spawnAsteroidFire() {
    GridPoint2 pos1 = new GridPoint2(12,10);
    Entity attackObstacle1 = ObstacleFactory.createAsteroidFree(player);
    spawnEntityAt(attackObstacle1, pos1, true, false);

    GridPoint2 pos2 = new GridPoint2(19,10);
    Entity attackObstacle2 = ObstacleFactory.createAsteroidFree(player);
    spawnEntityAt(attackObstacle2, pos2, true, false);

    GridPoint2 pos3 = new GridPoint2(25,10);
    Entity attackObstacle3 = ObstacleFactory.createAsteroidFree(player);
    spawnEntityAt(attackObstacle3, pos3, true, false);
  }

  private void spawnRobot() {
    GridPoint2 pos1 = new GridPoint2(12, 16);
    Entity robot1 = ObstacleFactory.createRobot(player);
    spawnEntityAt(robot1, pos1, true, true);
  }


  private Entity spawnPlayer() {
    //need to change it to the horizon view
    float tileSize = terrain.getTileSize();
    Entity newPlayer = PlayerFactory.createPlayer();
    //Adds the progress component for a new created player
    newPlayer.addComponent(new ProgressComponent(0,
            (terrain.getMapBounds(0).x)* tileSize));
    //spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    if (this.checkpoint == 1) {
      spawnEntityAt(newPlayer, CHECKPOINT, true, true);
    } else {
      spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    }
    return newPlayer;
  }

  private void createCheckpoint() {

    GridPoint2 checkPoint = new GridPoint2(20, 10);
    Entity checkpoint = ObstacleFactory.createCheckpoint(player, this);
    spawnEntityAt(checkpoint, checkPoint, true, false);

  }

  private void spawnGhosts() {
    //need to change it to the horizon view
//    GridPoint2 minPos = new GridPoint2(0, 0);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
//
//    for (int i = 0; i < NUM_GHOSTS; i++) {
//      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
//      Entity ghost = NPCFactory.createGhost(player);
//      spawnEntityAt(ghost, randomPos, true, true);
//    }

    GridPoint2 pos = new GridPoint2(12,10);
    Entity ghost = NPCFactory.createGhost(player);
    spawnEntityAt(ghost, pos, true, true);
  }



  private void spawnGhostKing() {
    //need to change it to the horizon view
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity ghostKing = NPCFactory.createGhostKing(player);
    spawnEntityAt(ghostKing, randomPos, true, true);
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
    logger.info("Just created and spawned a new buff!");
  }

  public void spawnBuffDebuffPickup(BuffManager.BuffPickup pickup, BuffManager manager) {
    Vector2 target = new Vector2(player.getPosition().x, player.getPosition().y + 3);
    //Vector2 target = new Vector2(3f, 0f);
    Entity buffPickup = BuffFactory.createBuffAnimation();

    spawnEntityAt(buffPickup, new GridPoint2(3, 3), true, true);
    /*spawnEntityAt(buffPickup, new GridPoint2((int) player.getComponent(PlayerStatsDisplay.class).getPlayerPosition().x,
            (int) player.getComponent(PlayerStatsDisplay.class).getPlayerPosition().y), true, true);*/
    logger.info("Just released a buff pickup");

  }

  /*public void spawnBuffDebuffPickup() {
    Vector2 target = new Vector2(player.getPosition().x, player.getPosition().y + 3);
    //Vector2 target = new Vector2(3f, 0f);
    Entity buffPickup = BuffFactory.createBuffAnimation();
    spawnEntityAt(buffPickup, new GridPoint2((int) player.getComponent(PlayerStatsDisplay.class).getPlayerPosition().x,
            (int) player.getComponent(PlayerStatsDisplay.class).getPlayerPosition().y), true, true);
    logger.info("Just released a buff pickup");

  }*/

  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  //private void spawnGhosts() {
  //  //need to change it to the horizon view
  //  GridPoint2 minPos = new GridPoint2(0, 0);
  //  GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
//
  //  for (int i = 0; i < NUM_GHOSTS; i++) {
  //    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
  //    Entity ghost = NPCFactory.createGhost(player);
  //    spawnEntityAt(ghost, randomPos, true, true);
  //  }
  //}
//
  //private void spawnGhostKing() {
  //  //need to change it to the horizon view
  //  GridPoint2 minPos = new GridPoint2(0, 0);
  //  GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
//
  //  GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
  //  Entity ghostKing = NPCFactory.createGhostKing(player);
  //  spawnEntityAt(ghostKing, randomPos, true, true);
  //}
//
  //private void playMusic() {
  //  Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
  //  music.setLooping(true);
  //  music.setVolume(0.3f);
  //  music.play();
  //}


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
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }

}
