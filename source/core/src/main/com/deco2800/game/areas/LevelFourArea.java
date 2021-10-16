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


/** Game area for level four */
public class LevelFourArea extends ForestGameArea {
    private static final Logger logger = LoggerFactory.getLogger(LevelFourArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(18, 12);

    private ArrayList<GridPoint2> PLATFORM_ONE_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> PLATFORM_TWO_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ASTEROID_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ASTEROID_FIRE_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ROBOT_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ALIEN_BOSS_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> CHECKPOINT_SPAWNS = new ArrayList<>();

    // Placeholder music
    private static final String backgroundMusic = "sounds/level4_background_music_1.mp3";

    public LevelFourArea(TerrainFactory terrainFactory, int checkpoint,
            boolean hasDied) {
        super(terrainFactory, checkpoint, hasDied);
        setupSpawns();
    }

    private void setupSpawns() {
        setupPlatformSpawns();
        setupAsteroidSpawns();
        setupAsteroidFireSpawns();
        setupRobotSpawns();
        setupCheckpointSpawns();
    }

    /**
     * Defines the platform spawns for this level.
     * */
    private void setupPlatformSpawns() {
        this.PLATFORM_ONE_SPAWNS.add(new GridPoint2(7,14));
        this.PLATFORM_ONE_SPAWNS.add(new GridPoint2(22, 15));
        this.PLATFORM_ONE_SPAWNS.add(new GridPoint2(70, 18));
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
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(5,10));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(15,11));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(22,8));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(36,10));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(48,10));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(55,13));
    }

    /**
     * Defines the robot spawns for this level
     * */
    private void setupRobotSpawns() {
        this.ROBOT_SPAWNS.add(new GridPoint2(60, 13));
    }

    /**
     * Defines the Checkpoint spawns for this level.
     * */
    private void setupCheckpointSpawns() {
        this.CHECKPOINT_SPAWNS.add(new GridPoint2(36,12));
    }

    /** Create the game area, including terrain, static entities (asteroids, asteroid fire), dynamic entities (enemies, robot, ufo) */
    @Override
    public void create() {
        // UI
        displayUI("Level Four");

        // Spawning Terrain and player
        spawnTerrain(TerrainType.LEVEL_FOUR_TERRAIN, "level-floors/levelFourGround.txt");
        setPlayer(spawnPlayer(PLAYER_SPAWN, TerrainType.LEVEL_FOUR_TERRAIN, false)); // Placeholder save
        spawnDeathWall(4);
        spawnAsteroids(this.ASTEROID_SPAWNS);
        spawnAsteroidFires(this.ASTEROID_FIRE_SPAWNS);
        spawnRobots(this.ROBOT_SPAWNS);
        spawnPlatformsTypeTwo(this.PLATFORM_TWO_SPAWNS);
        spawnPlatformsTypeOne(this.PLATFORM_ONE_SPAWNS);
        spawnUFO();
        spawnAlienBosses(this.ALIEN_BOSS_SPAWNS, this);
        // createCheckpoints(this.CHECKPOINT_SPAWNS, this); // No checkpoints

        // Music
        playMusic(backgroundMusic);
    }

    /**
     * reset the camera position when refresh every frame
     * @param camera the CameraComponent of the map
     */

    public void resetCam(CameraComponent camera) {
        super.resetCam(camera, TerrainType.LEVEL_FOUR_TERRAIN);
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
     * Returns the type associated with this area.
     *
     * Allows for differentiation between the four areas.
     * */
    public MainGameScreen.Level getAreaType() {
        return MainGameScreen.Level.FOUR;
    }
}


