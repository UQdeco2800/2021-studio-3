package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
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
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.BuffFactory;
import com.deco2800.game.entities.factories.EnemyFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  protected static int lives = 2;
  private static final GameTime gameTime = new GameTime();
  private long CAM_START_TIME;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(18, 6);
  private static final float WALL_WIDTH = 0.1f;

  private GdxGame game;
  private static final GridPoint2 CHECKPOINT = new GridPoint2(18, 12);
  private ArrayList<GridPoint2> PLATFORM_SPAWNS = new ArrayList<>();
  private ArrayList<GridPoint2> ASTEROID_SPAWNS = new ArrayList<>();
  private ArrayList<GridPoint2> ASTEROID_FIRE_SPAWNS = new ArrayList<>();
  private ArrayList<GridPoint2> ROBOT_SPAWNS = new ArrayList<>();
  private ArrayList<GridPoint2> ALIEN_SOLDIER_SPAWNS = new ArrayList<>();
  private ArrayList<GridPoint2> CHECKPOINT_SPAWNS = new ArrayList<>();
  private ArrayList<GridPoint2> ALIEN_LASER_SPAWNS = new ArrayList<>();
  private ArrayList<GridPoint2> ALIEN_BARBETTE_SPAWNS = new ArrayList<>();


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
          "images/main_screens-02.png",
          "images/roll.png",
          "images/roll2.png",
          "images/roll3.png"

  };

  private static final String[] forestTextureAtlases = {

    "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas",
          "images/boxBoy.atlas", "images/robot.atlas", "images/asteroidFire.atlas",
          "images/ufo_animation.atlas", "images/PlayerMovementAnimations.atlas","images/roll.atlas"
          , "images/SerpentLevel1.atlas", "images/alienBoss.atlas", "images/alienSoldier.atlas", "images/alienMonster.atlas"
  };

  private static final String[] forestSounds = {"sounds/Impact4.ogg","sounds/buff.mp3","sounds/debuff.mp3"};
  private static final String backgroundMusic = "sounds/maingame.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;

  /* Player on the map */
  //public static Entity player;

  /* End of this map */
  private Entity endOfMap;

  private int checkpoint;

  private boolean hasSave;

  private boolean hasDied;

  protected String saveState;
  /* The edges (Entity objects) of this map.  This region defines the
     space the player is allowed to move within. */
  private LinkedHashMap<String, Entity> mapFixtures = new LinkedHashMap<>();

  public ForestGameArea(TerrainFactory terrainFactory, int checkpoint, boolean hasDied) {
    super();
    this.terrainFactory = terrainFactory;
    this.checkpoint = checkpoint;
    this.hasDied = hasDied;
    this.hasSave = false;
    CAM_START_TIME = gameTime.getTime();
    setupSpawns();
  }

  public ForestGameArea(TerrainFactory terrainFactory, String saveState) {
    super();
    this.terrainFactory = terrainFactory;
    this.saveState = saveState;
    this.hasSave = true;
    CAM_START_TIME = gameTime.getTime();
    setupSpawns();
  }

  /**
   * Calls the appropriate spawn functions for all of the different assets of
   * this level.
   * */
  private void setupSpawns() {
    setupPlatformSpawns();
    setupAlienBarbetteSpawns();
    setupAsteroidFireSpawns();
    setupRobotSpawns();
  }

  /**
   * Defines the Alien Barbette spawns for this level.
   * */
  private void setupAlienBarbetteSpawns() {
    this.ALIEN_BARBETTE_SPAWNS.add(new GridPoint2(89, 14));

    this.ALIEN_BARBETTE_SPAWNS.add(new GridPoint2(143, 23));
  }

  /**
   * Defines the Alien Laser spawns for this level.
   * */
  private void setupAlienLaserSpawns() {
    this.ALIEN_LASER_SPAWNS.add(new GridPoint2(93, 12));
  }

  /**
   * Defines the platform spawns for this level.
   * */
  private void setupPlatformSpawns() {
    this.PLATFORM_SPAWNS.add(new GridPoint2(52, 13));

    this.PLATFORM_SPAWNS.add(new GridPoint2(95,8));
    this.PLATFORM_SPAWNS.add(new GridPoint2(99, 10));
    this.PLATFORM_SPAWNS.add(new GridPoint2(95, 12));
    this.PLATFORM_SPAWNS.add(new GridPoint2(99, 14));
    this.PLATFORM_SPAWNS.add(new GridPoint2(104, 17));

    this.PLATFORM_SPAWNS.add(new GridPoint2(155, 8));
    this.PLATFORM_SPAWNS.add(new GridPoint2(162, 7));
    this.PLATFORM_SPAWNS.add(new GridPoint2(157, 4));
  }

  /**
   * Defines the asteroid spawns for this level.
   * */
  private void setupAsteroidSpawns() {
    this.ASTEROID_SPAWNS.add(new GridPoint2(60, 13));
    this.ASTEROID_SPAWNS.add(new GridPoint2(9, 10));
    this.ASTEROID_SPAWNS.add(new GridPoint2(45, 10));
  }

  /**
   * Defines the asteroid fire spawns for this level.
   * */
  private void setupAsteroidFireSpawns() {
    this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(54,6));
    this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(51,6));
    this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(50,6));
    this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(55,6));
    this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(52,6));
    this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(53,6));

    this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(194,7));
  }

  /**
   * Defines the robot spawns for this level
   * */
  private void setupRobotSpawns() {
    this.ROBOT_SPAWNS.add(new GridPoint2(70, 7));
  }

  /**
   * Defines the Alien Soldier spawns for this level.
   * */
  private void setupAlienSoldierSpawns() {
    this.ALIEN_SOLDIER_SPAWNS.add(new GridPoint2(83,20));
  }

  /**
   * Defines the Checkpoint spawns for this level.
   * */
  private void setupCheckpointSpawns() {
    this.CHECKPOINT_SPAWNS.add(new GridPoint2(36,12));
  }

  /**
   * Sets the player for the area
   * */
  public void setPlayer(Entity player) {
    this.player = player;
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
    displayUI("Level One");

    spawnTerrain(TerrainType.SIDE_SCROLL_ER, "level-floors/levelOneGround.txt", "level-floors/levelOneFloat.txt");
    player = spawnPlayer(PLAYER_SPAWN, TerrainType.SIDE_SCROLL_ER, hasSave);
    if (hasSave) {
      loadSave(player, this.saveState);
    }
    spawnDeathWall(1);
    spawnAsteroids(this.ASTEROID_SPAWNS);
    spawnAsteroidFires(this.ASTEROID_FIRE_SPAWNS);
    spawnRobots(this.ROBOT_SPAWNS);
    spawnPlatformsTypeTwo(this.PLATFORM_SPAWNS);
    spawnAlienSoldiers(this.ALIEN_SOLDIER_SPAWNS, this);
    spawnAlienBarbettes(this.ALIEN_BARBETTE_SPAWNS, this);
    // createCheckpoints(this.CHECKPOINT_SPAWNS, this); No checkpoints on this map

    playMusic(backgroundMusic);
  }

  /**
   * Displays the UI for the level.
   *
   * @param name the name of the current level
   * */
  protected void displayUI(String name) {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay(name));
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

  /**
   * Spawns the terrain for the current level.
   *
   * @param type the type of terrain (terrain types differ between all levels)
   * @param levelFile the file to read the level from.
   * */
  protected void spawnTerrain(TerrainType type, String levelFile, String floatFile) {
    // Background terrain
    terrain = terrainFactory.createTerrain(type);
    spawnEntity(new Entity().addComponent(terrain));

    // Terrain walls
    float tileSize = terrain.getTileSize();
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);
    //TiledMapTileLayer layer = new TiledMapTileLayer(tileBounds.x, tileBounds.y, tileBounds.x, tileBounds.y);

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

    int i = 0, x, y, distanceX, distanceY;
    // opens the levels file

    try(BufferedReader br = new BufferedReader(new FileReader(levelFile))) {
      String line = br.readLine();
      // parse file to load the floor
      while (line != null) {
        String[] values = line.split(" ");
        distanceX = Integer.parseInt(values[0]) * 2;
        distanceY = (int) (Float.parseFloat(values[1]) * 2);
        x = Integer.parseInt(values[2]);
        y = Integer.parseInt(values[3]);
        float height = Float.parseFloat(values[1]);

        // creates the floors wall
        //spawnEntityAt(
                //ObstacleFactory.createWall(Integer.parseInt(values[0]), WALL_WIDTH), new GridPoint2(x, distanceY), false, false);
        //if (i != 0) {
          // Create walls when floor level changes
          //float height = (float) y/2;

          //float endHeight = (float) (previousY - y)/2;
        spawnEntityAt(
                ObstacleFactory.createWall(terrain.getTileSize() * distanceX,
                        distanceY * terrain.getTileSize()), new GridPoint2(x, y), false, false);
          //spawnEntityAt(
                  //ObstacleFactory.createWall(WALL_WIDTH, height), new GridPoint2(x + distanceX, y), false, false);
        //}

        spawnFloatPlatform(floatFile);
        line = br.readLine();

      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    //Kills player upon falling into void
    spawnEntityAt(
            ObstacleFactory.createDeathFloor(worldBounds.x, WALL_WIDTH),
            new GridPoint2(0, -1), false, false);
  }

  protected void spawnFloatPlatform(String floatFile) {
    //float width, height;
    int x, y, distanceX, distanceY;
    try(BufferedReader br = new BufferedReader(new FileReader(floatFile))) {
      String line = br.readLine();
      // parse file to load the floor
      while (line != null) {
        String[] values = line.split(" ");
        distanceX = Integer.parseInt(values[0]) * 2;
        distanceY = (int) (Float.parseFloat(values[1]) * 2);
        x = Integer.parseInt(values[2]);
        y = Integer.parseInt(values[3]);
        float height = Float.parseFloat(values[1]);
        spawnEntityAt(
                ObstacleFactory.createWall(terrain.getTileSize() * distanceX,
                        (terrain.getTileSize() * distanceY) + terrain.getTileSize()), new GridPoint2(x, y - 1), false, false);

        line = br.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * @param levelNumber the current level
   * @return the serpent moving speed for each level
   */
  float serpentLevelSpeed(int levelNumber){
    float movingSpeed = 0.2f;
    switch (levelNumber){
      case 1:
        movingSpeed = 0.4f;
        break;
      case 2:
        movingSpeed = 0.85f;
        break;
      case 3:
        movingSpeed = 1.1f;
        break;
      case 4:
        movingSpeed = 1.2f;
        break;
    }
    return movingSpeed;
  }

  /**
   * Spawns the death wall that moves from left to right
   */
  protected void spawnDeathWall(int levelNumber) {
    float movingSpeed = serpentLevelSpeed(levelNumber);
    Vector2 deathWallEndPos = new Vector2(this.endOfMap.getPosition().x, this.endOfMap.getPosition().y);
    Entity deathWall = ObstacleFactory.createDeathWall(deathWallEndPos, movingSpeed);
    deathWall.getComponent(AnimationRenderComponent.class).scaleEntity();
    deathWall.setScale(3f, terrain.getMapBounds(0).y * terrain.getTileSize());
    int startX;
    startX = levelNumber == 1 ? -5 : -8;
    spawnEntityAt(deathWall, new GridPoint2(startX, 0), false, false);
  }

  /**
   * Spawns a UFO in a random position on the map
   * */
  protected void spawnUFO() {
    GridPoint2 minPos = new GridPoint2(2, 20);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 10);
    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    //Entity ufo = NPCFactory.createUFO(player);
    Entity ufo = ObstacleFactory.createUfo(player);
    spawnEntityAt(ufo, randomPos, true, true);
  }

  /**
   * Spawns the platforms for the level (type two)
   *
   * @param positions the position(s) to place the platforms.
   * */
  protected void spawnPlatformsTypeTwo(ArrayList<GridPoint2> positions) {
    for (GridPoint2 pos : positions) {
      spawnEntityAt(ObstacleFactory.createPlatform2(), pos,
              true, false);
    }
  }

  /**
   * Spawns the platforms for the level (type one)
   *
   * @param positions the position(s) to place the platforms.
   * */
  protected void spawnPlatformsTypeOne(ArrayList<GridPoint2> positions) {
    for (GridPoint2 pos : positions) {
      spawnEntityAt(ObstacleFactory.createPlatform1(), pos,
              true, false);
    }
  }

  /**
   * Spawns the asteroids for the level
   *
   * @param positions the position(s) to spawn the asteroids.
   * */
  protected void spawnAsteroids(ArrayList<GridPoint2> positions) {
    for (GridPoint2 pos : positions) {
      spawnEntityAt(ObstacleFactory.createAsteroid(), pos,
              true, false);
    }
  }

  /**
   * Spawns the Asteroid Fires for the level
   *
   * @param positions the position(s) to spawn the Asteroid Fires.
   * */
  protected void spawnAsteroidFires(ArrayList<GridPoint2> positions) {
    for (GridPoint2 pos : positions) {
      spawnEntityAt(ObstacleFactory.createAsteroidAnimatedFire(player),
              pos, true, false);
    }
  }

  /**
   * Spawns the robots for the level
   *
   * @param positions the position(s) to spawn the robot(s) at
   * */
  protected void spawnRobots(ArrayList<GridPoint2> positions) {
    for (GridPoint2 pos : positions) {
      spawnEntityAt(ObstacleFactory.createRobot(player),
              pos, true, true);
    }
  }

  /**
   * Spawns the Alien Monsters for the level
   *
   * @param positions the position(s) to spawn the Alien Monster(s) at.
   * @param area the current game area.
   * */
  protected void spawnAlienMonsters(ArrayList<GridPoint2> positions,
          GameArea area) {
    for (GridPoint2 pos : positions) {
      spawnEntityAt(EnemyFactory.createAlienMonster(player, area),
              pos, true, true);
    }
  }

  /**
   * Spawns the Alien Soldiers for the level.
   *
   * @param positions the position(s) to spawn the Alien Soldier(s) at.
   * @param area the current game area.
   * */
  protected void spawnAlienSoldiers(ArrayList<GridPoint2> positions,
          GameArea area) {
    for (GridPoint2 pos : positions) {
      spawnEntityAt(EnemyFactory.createAlienSoldier(player, area),
              pos, true, true);
    }
  }

  /**
   * Spawns the Alien Boss(es) for the level.
   *
   * @param positions the position(s) to spawn the Alien Boss(es) at.
   * @param area the current game area.
   * */
  protected void spawnAlienBosses(ArrayList<GridPoint2> positions,
          GameArea area) {
    for (GridPoint2 pos : positions) {
      spawnEntityAt(EnemyFactory.createAlienBoss(player, area),
              pos, true, true);
    }
  }

  /**
   * Spawns the Alien Barbette(s) at the given position(s).
   *
   * @param positions the position(s) to spawn the enemy at.
   * @param area the game area.
   * */
  protected void spawnAlienBarbettes(ArrayList<GridPoint2> positions,
          GameArea area) {
    for (GridPoint2 pos : positions) {
      spawnEntityAt(EnemyFactory.createALienBarbette(player, area),
              pos, true, true);
    }
  }

  /**
   * Spawns the Alien Laser Hole(s) as the given position(s).
   *
   * @param positions the position(s) to spawn the enemy at.
   * @param area the game area.
   * */
  protected void spawnAlienLaserHoles(ArrayList<GridPoint2> positions,
          GameArea area) {
    for (GridPoint2 pos : positions) {
      spawnEntityAt(EnemyFactory.createAlienLaserHole(player, area),
              pos, true, true);
    }
  }

  public boolean isDead() {
    return hasDied;
  }

  /**
   * Defines the correct 'lives' boolean to return based on the current map
   * area. The way in which lives are reset is dependent on whether the player
   * is on level one, or another level.
   *
   * @param area the area for which the conditional is being evaluated
   * @param lives the amount of lives the player currently has
   *
   * @return true if the players lives will be reset to MAX, else false.
   * */
  protected boolean livesCondition(TerrainType area, int lives) {
    switch (area) {
      case SIDE_SCROLL_ER:
        return lives == 0;
      case LEVEL_TWO_TERRAIN:
      case LEVEL_THREE_TERRAIN:
      case LEVEL_FOUR_TERRAIN:
        return lives < 0;
    }
    return false;
  }

  /**
   * Spawns the player on the current terrain
   *
   * @param playerSpawn the position to spawn the player at
   * @param area the current map area.
   *
   * @return the new player entity.
   * */
  protected Entity spawnPlayer(GridPoint2 playerSpawn, TerrainType area,
          boolean save) {
    //need to change it to the horizon view
    Entity newPlayer;
    if (player != null) {
      newPlayer = player;
    } else {
      newPlayer = PlayerFactory.createPlayer();
    }
    float tileSize = terrain.getTileSize();
    //Adds the progress component for a new created player
    newPlayer.addComponent(new ProgressComponent(0,
            (terrain.getMapBounds(0).x)* tileSize));
    newPlayer.addComponent(new ScoreComponent());
    newPlayer.addComponent(new LivesComponent(lives));

    if (isDead()) {
      lives -= 1;
        newPlayer.getComponent(LivesComponent.class).setLives(lives);
    } else {
      if(livesCondition(area, lives) && !isDead()) {
        lives = 3;
        newPlayer.getComponent(LivesComponent.class).setLives(lives);
      }
    }

    //spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    if (this.checkpoint == 1) {
      spawnEntityAt(newPlayer, CHECKPOINT, true, true);
    } else if (!save){
      spawnEntityAt(newPlayer, playerSpawn, true, true);
    }

    /* Inform the player about the map fixtures */
    newPlayer.getComponent(DoubleJumpComponent.class).setMapEdges(this.mapFixtures);
    return newPlayer;
  }

  /**
   * Spawns the Checkpoints for the level.
   *
   * @param positions the position(s) to spawn the Checkpoint(s) at.
   * @param area the current game area.
   * */
  protected void createCheckpoints(ArrayList<GridPoint2> positions,
          ForestGameArea area) {
    for (GridPoint2 pos : positions) {
      spawnEntityAt(ObstacleFactory.createCheckpoint(player, area),
              pos, true, false);
    }
  }


  /**
   * Spawns buffs or debuffs onto the current map in a random position. Buffs
   * are spawned on the ground only (not platforms). Buffs can spawn anywhere
   * across the game map (horizontally). A random buff type is chosen to be
   * spawned.
   *
   * @param manager the BuffManager which will handle the actions, despawning
   *                and timeout-related functionality of this buff.
   * @param screenType the area the buffs are currently spawning on. This
   *                   allows for dynamic spawn placements between the
   *                   different terrain layouts.
   * */
  public void spawnBuffDebuff(BuffManager manager,
          MainGameScreen.Level screenType) {
    /* Get a random x position based on map bounds */
    int maxXPos = terrain.getMapBounds(0).x;
    Random randomXPos = new Random();
    int pos = randomXPos.nextInt(maxXPos);
    logger.debug("this is x {}", pos);

    GridPoint2 randomPos = new GridPoint2(pos - 1,
            terrainFactory.getYOfSurface(pos, screenType));

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

  /**
   * Plays the music for the current level.
   *
   * @param musicName the filepath for the sound file.
   * */
  protected void playMusic(String musicName) {
    Music music = ServiceLocator.getResourceService().getAsset(musicName, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  /**
   * reset the camera position when refresh every frame
   *
   * @param camera the CameraComponent of the map
   * @param type the type of terrain. Different terrain types have different
   *             camera positions.
   */
  public void resetCam(CameraComponent camera, TerrainType type) {
    float playerX = player.getPosition().x;

    int lower = 12;
    switch (type) {
      case SIDE_SCROLL_ER:
        // Level one, do nothing
        break;
      case LEVEL_TWO_TERRAIN:
      case LEVEL_THREE_TERRAIN:
      case LEVEL_FOUR_TERRAIN:
        lower = 5;
        break;
    }

//    logger.info(String.valueOf(playerX));
    if (playerX > lower && playerX <= 90) {
      camera.getCamera().translate(playerX - camera.getCamera().position.x + 5, 0,0);
      camera.getCamera().update();
    }
  }

  /**
   * reset the camera position when refresh every frame
   * @param camera the CameraComponent of the map
   */
  public void resetCam(CameraComponent camera) {
    resetCam(camera, TerrainType.SIDE_SCROLL_ER);
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

  /**
   * Check if the game is pause, and stop the animation playing
   * @param state The game state
   */
  public void isPause(GdxGame.GameState state, List<Entity> areaEntities) {
    if (state != GdxGame.GameState.RUNNING) {
      for (Entity entity : areaEntities) {
        if (entity.getComponent(AnimationRenderComponent.class) != null) {
          entity.getComponent(AnimationRenderComponent.class).setEnabled(false);
        }
        if (entity.getComponent(PlayerAnimationController.class) != null) {
          entity.getComponent(PlayerAnimationController.class).setEnabled(false);
        }
      }
    } else {
      for (Entity entity : areaEntities) {
        if (entity.getComponent(AnimationRenderComponent.class) != null) {
          entity.getComponent(AnimationRenderComponent.class).setEnabled(true);
        }
        if (entity.getComponent(PlayerAnimationController.class) != null) {
          entity.getComponent(PlayerAnimationController.class).setEnabled(true);
        }
      }
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

  protected void unloadAssets() {
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
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();

    System.out.println("forest game area disposed");
  }

  /**
   * Returns the TYPE associated with this area. Allows for differentiation
   * between the four areas.
   * */
  public MainGameScreen.Level getAreaType() {
    return MainGameScreen.Level.ONE;
  }

}
