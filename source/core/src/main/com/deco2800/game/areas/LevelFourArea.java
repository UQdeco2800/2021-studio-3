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
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(8, 11);

    private ArrayList<GridPoint2> PLATFORM_ONE_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> PLATFORM_TWO_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ASTEROID_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ASTEROID_FIRE_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ROBOT_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ALIEN_BOSS_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> CHECKPOINT_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> UFO_SPAWNS = new ArrayList<>();

    // Placeholder music
    private static final String backgroundMusic = "sounds/level4_background_music_1.mp3";

    public LevelFourArea(TerrainFactory terrainFactory, int checkpoint,
            boolean hasDied) {
        super(terrainFactory, checkpoint, hasDied);
        setupSpawns();
    }

    /**
     * Instantiates the spawn locations for all entities on this level.
     * */
    private void setupSpawns() {
        setupAsteroidFireSpawns();
        setupUFOSpawns();
    }

    /**
     * Defines the UFO spawn locations for this level.
     * */
    private void setupUFOSpawns() {
        this.UFO_SPAWNS.add(new GridPoint2(15,20));

        this.UFO_SPAWNS.add(new GridPoint2(119,20));

        this.UFO_SPAWNS.add(new GridPoint2(154,13));
    }

    /**
     * Defines the asteroid fire spawns for this level.
     * */
    private void setupAsteroidFireSpawns() {
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(67,16));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(68,16));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(69,16));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(70,16));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(71,16));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(72,16));

        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(112,8));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(113,8));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(114,8));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(115,8));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(116,8));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(117,8));

        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(152,19));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(153,19));

        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(156,19));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(157,19));

        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(160,19));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(161,19));

        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(164,19));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(165,19));

        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(190,4));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(191,4));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(192,4));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(193,4));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(194,4));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(195,4));

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
        spawnTerrain(TerrainType.LEVEL_FOUR_TERRAIN, "level-floors/levelFourGround.txt", "level-floors/levelFourFloat.txt");
        setPlayer(spawnPlayer(PLAYER_SPAWN, TerrainType.LEVEL_FOUR_TERRAIN, false)); // Placeholder save
        spawnDeathWall(4);
        spawnSpaceship(MainGameScreen.Level.FOUR);
        spawnAsteroids(this.ASTEROID_SPAWNS);
        spawnAsteroidFires(this.ASTEROID_FIRE_SPAWNS);
        spawnPlatformsTypeTwo(this.PLATFORM_TWO_SPAWNS);
        spawnPlatformsTypeOne(this.PLATFORM_ONE_SPAWNS);
        spawnUFOs(this.UFO_SPAWNS);

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


