package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.InformPlayerComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.EnemyFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/** Game area for the tutorial */
public class TutorialArea extends ForestGameArea {
    private static final Logger logger = LoggerFactory.getLogger(LevelTwoArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(12, 6);
    private ArrayList<GridPoint2> PLATFORM_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ASTEROID_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ASTEROID_FIRE_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ROBOT_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> ALIEN_MONSTER_SPAWNS = new ArrayList<>();
    private ArrayList<GridPoint2> CHECKPOINT_SPAWNS = new ArrayList<>();
    private Map<Integer, String> triggerPoints;
    private boolean hasSave;
    //private int lives = 5;

    /* Music specific to this level */
    private static final String backgroundMusic = "sounds/maingame.mp3";

    public TutorialArea(TerrainFactory terrainFactory, int checkpoint, boolean hasDied) {
        super(terrainFactory, checkpoint, hasDied);
        this.hasSave = false;
        triggerPoints = new HashMap<>();
        setupSpawns();
    }

    public TutorialArea(TerrainFactory terrainFactory, String saveState) {
        super(terrainFactory, saveState);
        this.hasSave = true;
        triggerPoints = new HashMap<>();
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
//        setupAlienMonsterSpawns();
//        setupCheckPointSpawns();
    }

    /**
     * Sets up platform one spawn locations
     * */
    private void setupPlatformSpawns() {
        this.PLATFORM_SPAWNS.add(new GridPoint2(95,13));
        this.PLATFORM_SPAWNS.add(new GridPoint2(105, 13));
    }

    /**
     * Sets up Asteroid spawn locations
     * */
    private void setupAsteroidSpawns() {
        this.ASTEROID_SPAWNS.add(new GridPoint2(38, 7));
        this.ASTEROID_SPAWNS.add(new GridPoint2(46, 7));
        this.ASTEROID_SPAWNS.add(new GridPoint2(55, 14));
        this.ASTEROID_SPAWNS.add(new GridPoint2(87, 8));
        this.ASTEROID_SPAWNS.add(new GridPoint2(104, 8));
        this.ASTEROID_SPAWNS.add(new GridPoint2(112, 8));
        this.ASTEROID_SPAWNS.add(new GridPoint2(132, 10));
        this.ASTEROID_SPAWNS.add(new GridPoint2(150, 8));
        this.ASTEROID_SPAWNS.add(new GridPoint2(173, 6));
    }

    /**
     * Sets up Asteroid Fire spawn locations
     * */
    private void setupAsteroidFireSpawns() {
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(1,5));
        this.ASTEROID_FIRE_SPAWNS.add(new GridPoint2(3,5));
    }

    /**
     * Sets up robot spawn locations
     * */
    private void setupRobotSpawns() {
        this.ROBOT_SPAWNS.add(new GridPoint2(100, 8));
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
        displayUI("Level Tutorial");
        fillTriggerMessages();
        spawnTerrain(TerrainType.TUTORIAL_TERRAIN, "level-floors/TutorialGround.txt", "level-floors/TutorialFloat.txt");
        player = spawnPlayer(PLAYER_SPAWN, TerrainType.SIDE_SCROLL_ER, hasSave);
        player.getComponent(InformPlayerComponent.class).setInformation("Press A or D to move");
        player.getComponent(InformPlayerComponent.class).setTriggers(triggerPoints);
        if (hasSave) {
            loadSave(player, this.saveState);
        }
        GridPoint2 pos = new GridPoint2(20, 18);
        spawnEntityAt(ObstacleFactory.createBuffDebuffInformation(), pos, true, true);
        spawnPlatformsTypeTwo(this.PLATFORM_SPAWNS);
        spawnRobots(this.ROBOT_SPAWNS);
        spawnAsteroids(this.ASTEROID_SPAWNS);
        spawnAsteroidFires(this.ASTEROID_FIRE_SPAWNS);
        spawnEgg(MainGameScreen.Level.TUTORIAL);
        // createCheckpoints(this.CHECKPOINT_SPAWNS, this); No checkpoints on this map

        playMusic(backgroundMusic);
    }

    public void fillTriggerMessages() {
        triggerPoints.put(12, "Press Space to Jump");
        triggerPoints.put(19, "Press Space Midair to Double Jump");
        triggerPoints.put(23, "Watch Out For Pit Falls");
        triggerPoints.put(35, "Avoid The Deadly Aliens");
        triggerPoints.put(48, "Press Q or E to Roll");
        triggerPoints.put(60, "Press Shift to Sprint");
        triggerPoints.put(85, "CONGRATULATIONS!!! collect your prize");
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
        return MainGameScreen.Level.TUTORIAL;
    }
}
