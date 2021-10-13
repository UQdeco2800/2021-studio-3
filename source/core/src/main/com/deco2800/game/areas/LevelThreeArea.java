package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/** Game area for level three */
public class LevelThreeArea extends ForestGameArea {
    private static final Logger logger = LoggerFactory.getLogger(LevelTwoArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(5, 11);

    private ArrayList<GridPoint2> PLATFORM_ONE_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> PLATFORM_TWO_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ASTEROID_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ASTEROID_FIRE_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ROBOT_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ALIEN_BOSS_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> CHECKPOINT_SPAWNS = new ArrayList<>();

    private static final String backgroundMusic = "sounds/level3.mp3";

    public LevelThreeArea(TerrainFactory terrainFactory, int checkpoint, boolean hasDied) {
        super(terrainFactory, checkpoint, hasDied);
        setupSpawns();
    }

    /**
     * Sets up the spawn locations for the different entities in the area
     * */
    private void setupSpawns() {
        setupPlatformOneSpawns();
        setupPlatformTwoSpawns();
        setupAsteroidSpawns();
        setupAsteroidFireSpawns();
        setupRobotSpawns();
        setupCheckPointSpawns();
        setupAlienBossSpawns();
    }

    /**
     * Sets up the Platform One spawn locations
     * */
    private void setupPlatformOneSpawns() {
        this.PLATFORM_ONE_SPAWNS.add(new GridPoint2(7,14));
    }

    /**
     * Sets up the Platform Two spawn locations
     * */
    private void setupPlatformTwoSpawns() {
        this.PLATFORM_TWO_SPAWNS.add(new GridPoint2(7,14));
        this.PLATFORM_TWO_SPAWNS.add(new GridPoint2(20, 13));
        this.PLATFORM_TWO_SPAWNS.add(new GridPoint2(24, 10));
        this.PLATFORM_TWO_SPAWNS.add(new GridPoint2(27, 12));
        this.PLATFORM_TWO_SPAWNS.add(new GridPoint2(34, 6));
    }

    /**
     * Sets up the Asteroid spawn locations
     * */
    private void setupAsteroidSpawns() {
        this.ASTEROID_SPAWNS.add(new GridPoint2(38, 5));
        this.ASTEROID_SPAWNS.add(new GridPoint2(46, 7));
        this.ASTEROID_SPAWNS.add(new GridPoint2(55, 8));
        this.ASTEROID_SPAWNS.add(new GridPoint2(87, 5));
    }

    /**
     * Sets up the Asteroid Fire spawn locations
     * */
    private void setupAsteroidFireSpawns() {
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(22,3));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(21,3));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(25,4));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(40,5));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(41,5));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(50,7));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(61,11));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(65,15));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(85,5));
    }

    /**
     * Sets up the robot spawn locations
     * */
    private void setupRobotSpawns() {
        this.ROBOT_SPAWNS.add(new GridPoint2(12, 16));
    }

    /**
     * Sets up the checkpoint spawn locations
     * */
    private void setupCheckPointSpawns() {
        this.CHECKPOINT_SPAWNS.add(new GridPoint2(20, 10));
    }

    /**
     * Sets up the Alien Boss spawn locations
     * */
    private void setupAlienBossSpawns() {
        this.ALIEN_BOSS_SPAWNS.add(new GridPoint2(78, 20));
    }

    /** Create the game area, including terrain, static entities (asteroids, asteroid fire), dynamic entities (enemies, robot, ufo) */
    @Override
    public void create() {
        // UI
        displayUI("Level Three");

        // Spawning Terrain and player
        spawnTerrain(TerrainType.LEVEL_THREE_TERRAIN, "level-floors/levelThree.txt");
        setPlayer(spawnPlayer(PLAYER_SPAWN, TerrainType.LEVEL_THREE_TERRAIN));
        spawnDeathWall();
        spawnAsteroids(this.ASTEROID_SPAWNS);
        spawnAsteroidFires(this.ASTEROID_FIRE_SPAWNS);
        spawnRobots(this.ROBOT_SPAWNS);
        spawnPlatformsTypeTwo(this.PLATFORM_TWO_SPAWNS);
        spawnPlatformsTypeOne(this.PLATFORM_ONE_SPAWNS);
        spawnUFO();
        spawnAlienBosses(this.ALIEN_BOSS_SPAWNS, this);
        // createCheckpoints(this.CHECKPOINT_SPAWNS, this); // None spawned yet

        // Music
        playMusic(backgroundMusic);
    }

    /**
     * reset the camera position when refresh every frame
     * @param camera the CameraComponent of the map
     */
    public void resetCam(CameraComponent camera) {
        super.resetCam(camera, TerrainType.LEVEL_THREE_TERRAIN);
    }

    /**
     * Method signature to ensure a call to the superclass isn't made.
     * */
    public void introCam(Vector2 startPos, float distance, float duration,
            CameraComponent camera) {
        // Do nothing
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        this.unloadAssets();
    }

    /**
     * Returns the area type for this area.
     *
     * Allows for differentiation between the four areas.
     * */
    public MainGameScreen.Level getAreaType() {
        return MainGameScreen.Level.THREE;
    }
}
