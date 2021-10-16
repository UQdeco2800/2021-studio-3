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
    private ArrayList<GridPoint2> ALIEN_BOSS_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> CHECKPOINT_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> UFO_SPAWNS = new ArrayList<>();

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
        setupAsteroidFireSpawns();
        setupRobotSpawns();
        setupUFOSpawns();
    }

    /**
     * Sets up platform one spawn locations
     * */
    private void setupPlatformSpawns() {
        this.PLATFORM_SPAWNS.add(new GridPoint2(12,12));
        this.PLATFORM_SPAWNS.add(new GridPoint2(16, 15));

        this.PLATFORM_SPAWNS.add(new GridPoint2(40, 12));
        this.PLATFORM_SPAWNS.add(new GridPoint2(38, 15));
        this.PLATFORM_SPAWNS.add(new GridPoint2(43, 19));

        this.PLATFORM_SPAWNS.add(new GridPoint2(92, 16));
        this.PLATFORM_SPAWNS.add(new GridPoint2(92, 12));
        this.PLATFORM_SPAWNS.add(new GridPoint2(99, 14));
        this.PLATFORM_SPAWNS.add(new GridPoint2(102, 9));

        this.PLATFORM_SPAWNS.add(new GridPoint2(156, 18));

        this.PLATFORM_SPAWNS.add(new GridPoint2(173, 16));
        this.PLATFORM_SPAWNS.add(new GridPoint2(173, 12));
        this.PLATFORM_SPAWNS.add(new GridPoint2(180, 14));
        this.PLATFORM_SPAWNS.add(new GridPoint2(183, 9));
    }

    /**
     * Sets up the UFO spawn locations
     * */
    private void setupUFOSpawns() {
        this.UFO_SPAWNS.add(new GridPoint2(28, 20));

        this.UFO_SPAWNS.add(new GridPoint2(68,15));

        this.UFO_SPAWNS.add(new GridPoint2(123,17));
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
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(34,10));

        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(66,6));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(67,6));

        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(197,6));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(196,6));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(195,6));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(194,6));
    }

    /**
     * Sets up robot spawn locations
     * */
    private void setupRobotSpawns() {
        this.ROBOT_SPAWNS.add(new GridPoint2(28, 11));
    }

    /**
     * Sets up Alien Boss spawn locations
     * */
    private void setupAlienBossSpawns() {
        this.ALIEN_BOSS_SPAWNS.add(new GridPoint2(45, 14));
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
        spawnTerrain(TerrainType.LEVEL_TWO_TERRAIN, "level-floors/levelTwoGround.txt", "level-floors/levelOneFloat.txt");
        setPlayer(spawnPlayer(PLAYER_SPAWN, TerrainType.LEVEL_TWO_TERRAIN, this.hasSave));
        if (this.hasSave) {
            loadSave(getPlayer(), this.saveState);
        }
        spawnDeathWall(2);
        spawnAsteroids(this.ASTEROID_SPAWNS);
        spawnAsteroidFires(this.ASTEROID_FIRE_SPAWNS);
        spawnRobots(this.ROBOT_SPAWNS);
        spawnAlienBosses(this.ALIEN_BOSS_SPAWNS, this);
        spawnPlatformsTypeTwo(this.PLATFORM_SPAWNS);
        spawnUFOs(this.UFO_SPAWNS);

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
