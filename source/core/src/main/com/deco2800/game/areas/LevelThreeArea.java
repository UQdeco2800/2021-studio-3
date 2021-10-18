package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.GdxGame;
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
    private ArrayList<GridPoint2> UFO_SPAWNS = new ArrayList<>();

    private static final String backgroundMusic = "sounds/level3.mp3";

    private boolean hasSave;

    public LevelThreeArea(TerrainFactory terrainFactory, int checkpoint, boolean hasDied) {
        super(terrainFactory, checkpoint, hasDied);
        this.hasSave = false;
        setupSpawns();
    }

    public LevelThreeArea(TerrainFactory terrainFactory, String saveState) {
        super(terrainFactory, saveState);
        this.hasSave = true;
        setupSpawns();
    }

    /**
     * Sets up the spawn locations for the different entities in the area
     * */
    private void setupSpawns() {
        setupPlatformOneSpawns();
        setupPlatformTwoSpawns();
        setupAsteroidFireSpawns();
        setupRobotSpawns();
        setupUFOSpawns();
    }

    /**
     * Sets up the UFO spawn locations.
     * */
    private void setupUFOSpawns() {
        this.UFO_SPAWNS.add(new GridPoint2(8, 17));
        this.UFO_SPAWNS.add(new GridPoint2(48, 17));
        this.UFO_SPAWNS.add(new GridPoint2(90, 22));
        this.UFO_SPAWNS.add(new GridPoint2(115, 20));
       // this.UFO_SPAWNS.add(new GridPoint2(162, 20));
    }

    /**
     * Sets up the Platform One spawn locations
     * */
    private void setupPlatformOneSpawns() {
        this.PLATFORM_ONE_SPAWNS.add(new GridPoint2(47,7));
        this.PLATFORM_ONE_SPAWNS.add(new GridPoint2(50,5));

        this.PLATFORM_ONE_SPAWNS.add(new GridPoint2(99, 10));
        this.PLATFORM_ONE_SPAWNS.add(new GridPoint2(99, 6));
        this.PLATFORM_ONE_SPAWNS.add(new GridPoint2(106, 8));
        this.PLATFORM_ONE_SPAWNS.add(new GridPoint2(109, 4));
    }

    /**
     * Sets up the Platform Two spawn locations
     * */
    private void setupPlatformTwoSpawns() {
        this.PLATFORM_TWO_SPAWNS.add(new GridPoint2(23,12));
        this.PLATFORM_TWO_SPAWNS.add(new GridPoint2(26, 16));

        this.PLATFORM_TWO_SPAWNS.add(new GridPoint2(143, 18));
    }

    /**
     * Sets up the Asteroid spawn locations
     * */
    private void setupAsteroidSpawns() {
        this.ASTEROID_SPAWNS.add(new GridPoint2(188, 17));
    }

    /**
     * Sets up the Asteroid Fire spawn locations
     * */
    private void setupAsteroidFireSpawns() {
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(196,14));

        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(162,12));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(163,12));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(164,12));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(165,12));

        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(157,14));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(156,14));

        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(149,15));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(148,15));

        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(90,10));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(89,10));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(88,10));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(87,10));
    }

    /**
     * Sets up the robot spawn locations
     * */
    private void setupRobotSpawns() {
        this.ROBOT_SPAWNS.add(new GridPoint2(22, 9));
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
        spawnTerrain(TerrainType.LEVEL_THREE_TERRAIN, "level-floors/levelThreeGround.txt", "level-floors/levelThreeFloat.txt");
        setPlayer(spawnPlayer(PLAYER_SPAWN, TerrainType.LEVEL_THREE_TERRAIN, hasSave));
        if (hasSave) {
            loadSave(getPlayer(), this.saveState);
        }
        spawnDeathWall(3);
        spawnPortal(MainGameScreen.Level.THREE);
        spawnAsteroids(this.ASTEROID_SPAWNS);
        spawnAsteroidFires(this.ASTEROID_FIRE_SPAWNS);
        spawnRobots(this.ROBOT_SPAWNS);
        spawnPlatformsTypeTwo(this.PLATFORM_TWO_SPAWNS);
        spawnPlatformsTypeOne(this.PLATFORM_ONE_SPAWNS);
        spawnUFOs(this.UFO_SPAWNS);
        spawnAlienBosses(this.ALIEN_BOSS_SPAWNS, this);

        // Music
        playMusic(backgroundMusic);

        spawnAlienBossLevelThree(this);
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
