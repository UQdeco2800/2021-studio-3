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
import java.util.*;

/** Game area for the level two */
public class LevelTwoArea extends ForestGameArea {
    private static final Logger logger = LoggerFactory.getLogger(LevelTwoArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(5, 11);
    private ArrayList<GridPoint2> PLATFORM_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ASTEROID_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ASTEROID_FIRE_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ROBOT_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ALIEN_MONSTER_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> CHECKPOINT_SPAWNS = new ArrayList<>();
    private boolean hasSave;
    //private int lives = 5;

    /* Music specific to this level */
    private static final String backgroundMusic = "sounds/level2.mp3";

    public LevelTwoArea(TerrainFactory terrainFactory, int checkpoint, boolean hasDied) {
        super(terrainFactory, checkpoint, hasDied);
        this.hasSave = false;
        setupSpawns();
    }

    public LevelTwoArea(TerrainFactory terrainFactory, String saveState) {
        super(terrainFactory, saveState);
        this.hasSave = true;
        setupSpawns();
    }

    /**
     * Sets up the spawn locations for the different entities in the area
     * */
    private void setupSpawns() {
        setupPlatformSpawns();
        setupAsteroidSpawns();
        setupAsteroidFireSpawns();
        setupRobotSpawns();
        setupAlienMonsterSpawns();
        setupCheckPointSpawns();
    }

    /**
     * Sets up platform one spawn locations
     * */
    private void setupPlatformSpawns() {
        this.PLATFORM_SPAWNS.add(new GridPoint2(7,14));
        this.PLATFORM_SPAWNS.add(new GridPoint2(20, 13));
        this.PLATFORM_SPAWNS.add(new GridPoint2(24, 10));
        this.PLATFORM_SPAWNS.add(new GridPoint2(27, 12));
        this.PLATFORM_SPAWNS.add(new GridPoint2(34, 6));
    }

    /**
     * Sets up Asteroid spawn locations
     * */
    private void setupAsteroidSpawns() {
        this.ASTEROID_SPAWNS.add(new GridPoint2(38, 5));
        this.ASTEROID_SPAWNS.add(new GridPoint2(46, 7));
        this.ASTEROID_SPAWNS.add(new GridPoint2(55, 8));
        this.ASTEROID_SPAWNS.add(new GridPoint2(87, 5));
    }

    /**
     * Sets up Asteroid Fire spawn locations
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
     * Sets up robot spawn locations
     * */
    private void setupRobotSpawns() {
        this.ROBOT_SPAWNS.add(new GridPoint2(12, 16));
    }

    /**
     * Sets up Alien Monster spawn locations
     * */
    private void setupAlienMonsterSpawns() {
        this.ALIEN_MONSTER_SPAWNS.add(new GridPoint2(86, 20));
    }

    /**
     * Sets up checkpoint spawn locations
     * */
    private void setupCheckPointSpawns() {
        this.CHECKPOINT_SPAWNS.add(new GridPoint2(20, 10));
    }

    /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
    @Override
    public void create() {
        // UI
        displayUI("Level Two");

        // Spawning Terrain and player
        spawnTerrain(TerrainType.LEVEL_TWO_TERRAIN, "level-floors/levelTwo.txt");
        setPlayer(spawnPlayer(PLAYER_SPAWN, TerrainType.LEVEL_TWO_TERRAIN, this.hasSave));
        if (this.hasSave) {
            loadSave(getPlayer(), this.saveState);
        }
        spawnDeathWall();
        spawnAsteroids(this.ASTEROID_SPAWNS);
        spawnAsteroidFires(this.ASTEROID_FIRE_SPAWNS);
        spawnRobots(this.ROBOT_SPAWNS);
        spawnAlienMonsters(this.ALIEN_MONSTER_SPAWNS, this);
        spawnPlatformsTypeTwo(this.PLATFORM_SPAWNS);
        // createCheckpoints(this.CHECKPOINT_SPAWNS, this); No checkpoints yet

        // Music
        playMusic(backgroundMusic);
    }

    /**
     * reset the camera position when refresh every frame
     * @param camera the CameraComponent of the map
     */
    public void resetCam(CameraComponent camera) {
        super.resetCam(camera, TerrainType.LEVEL_TWO_TERRAIN);
    }

    /**
     * Method signature to ensure a call to the superclass isn't made
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
        return MainGameScreen.Level.TWO;
    }
}
