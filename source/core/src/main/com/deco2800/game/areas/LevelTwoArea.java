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
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.BuffFactory;
import com.deco2800.game.entities.factories.EnemyFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deco2800.game.components.player.DoubleJumpComponent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Random;

/** Game area for the level two */
public class LevelTwoArea extends GameArea{
    private static final Logger logger = LoggerFactory.getLogger(LevelTwoArea.class);
    private static int lives = 5;
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(5, 11);
    private static final GridPoint2 CHECKPOINT = new GridPoint2(20, 11);
    private static final GridPoint2 PLATFORM_SPAWN = new GridPoint2(7,14);
    private static final float WALL_WIDTH = 0.1f;
    private static final String[] forestTextures = {
            "images/box_boy_leaf.png",
            "images/tree.png",
            "images/ghost_king.png",
            "images/ghost_1.png",
            "images/lives_icon2.png",
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
            "images/rock_platform.png",
            "images/background_stars.png",
            "images/background_sky.png",
            "images/background_rock.png",
            "images/background_star.png",
            "images/background_surface.png",
            "images/surface.png",
            "images/background_mars.png",
            "images/background_mars_ground.png",
            "images/background_mars_surface.png",
            "images/background_mars_star.png",
            "images/alien_monster_weapon_01.png",
            "images/alien_monster_weapon_02.png",
            "images/alien_solider.png",
            "images/alien_solider_weapon_01.png",
            "images/alien_solider_weapon_02.png",
            "images/alien_boss.png",
            "images/alien_boss_weapon_01.png",
            "images/portal.png",
            "images/Spaceship.png"
    };

    private static final String[] forestTextureAtlases = {

            "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas",
            "images/boxBoy.atlas", "images/robot.atlas", "images/asteroidFire.atlas",
            "images/ufo_animation.atlas", "images/PlayerMovementAnimations.atlas"
    };

    private static final String[] forestSounds = {"sounds/Impact4.ogg","sounds/buff.mp3","sounds/debuff.mp3"};
    private static final String backgroundMusic = "sounds/level2.mp3";
    private static final String[] forestMusic = {backgroundMusic};

    private final TerrainFactory terrainFactory;

    /* Player on the map */
    private Entity player;

    /* End of this map */
    private Entity endOfMap;

    /* The end portal of this map */
    private Entity endPortal;

    private int checkpoint;

    private boolean hasDied;

    private LinkedHashMap<String, Entity> mapFixtures = new LinkedHashMap<>();

    public LevelTwoArea(TerrainFactory terrainFactory, int checkpoint, boolean hasDied) {
        super();
        this.terrainFactory = terrainFactory;
        this.checkpoint = checkpoint;
        this.hasDied = hasDied;
    }

    public LevelTwoArea(TerrainFactory terrainFactory, int checkpoint, int lives) {
        super();
        this.terrainFactory = terrainFactory;
        this.checkpoint = checkpoint;
        LevelTwoArea.lives = lives;
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

    /**
     * Returns the end of the current map.
     * */
    public Entity getEndPortal() {
        return endPortal;
    }

    /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
    @Override
    public void create() {
        //loadAssets();

        displayUI();

        spawnTerrain();
        player = spawnPlayer();
        spawnDeathWall();
        //spawnTrees();
        spawnPortal();

        //spawnGhosts();

        //spawnTrees();
        spawnAsteroids();
        spawnAsteroidFires();
        spawnRobot();
        spawnAlienMonster();

        //spawnBuilding();
        //spawnTrees();
        //spawnRocks();
        spawnPlatforms();
        //spawnPlanet1();
        //spawnUFO();
        //spawnBuffDebuffPickup();

        playMusic();
        //spawnGhosts();
        //spawnGhostKing();
        //createCheckpoint();
//    playMusic();
        //spawnAttackObstacle();
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Level Two"));
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
     * Spawn the terrain onto this area.
     */
    private void spawnTerrain() {
        // Background terrain

        terrain = terrainFactory.createTerrain(TerrainType.LEVEL_TWO_TERRAIN);
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
        // LOGIC to create level terrain
        int i = 0, x, y, distance;
        // opens the levels file
        try(BufferedReader br = new BufferedReader(new FileReader("level-floors/levelTwo.txt"))) {
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
                    // creates walls when floor level changes
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
     * Spawn the dynamic obstacle entity UFO.
     */
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
        spawnPlatform(20, 13);
        spawnPlatform(24, 10);
        spawnPlatform(27, 12);
        spawnPlatform(34, 6);
    }

    /**
     * spawns a platform at a give position
     * param: int x, x position of the platform
     *        int y, y position of the platform
     * */
    private void spawnPlatform(int x, int y) {
        Entity platform1 = ObstacleFactory.createPlatform1();
        spawnEntityAt(platform1, PLATFORM_SPAWN, true, false);

        GridPoint2 pos2 = new GridPoint2(x, y);
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

    /**
     * spawns the asteroids for the level
     * */
    private void spawnAsteroids() {
        //GridPoint2 minPos = new GridPoint2(2, 10);
        //GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 20);
        spawnAsteroidFire(38,5);
        spawnAsteroid(46, 7);
        spawnAsteroid(55, 8);
        spawnAsteroid(87, 5);
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
        spawnAsteroidFire(22,3);
        spawnAsteroidFire(21,3);
        spawnAsteroidFire(25,4);
        spawnAsteroidFire(40,5);
        spawnAsteroidFire(41,5);
        spawnAsteroidFire(50,7);
        spawnAsteroidFire(61,11);
        spawnAsteroidFire(65,15);
        spawnAsteroidFire(85,5);
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
        GridPoint2 pos1 = new GridPoint2(12, 16);
        Entity robot1 = ObstacleFactory.createRobot(player);
        spawnEntityAt(robot1, pos1, true, true);
    }

    private void spawnPortal() {
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        int posY = terrainFactory.getYOfSurface(tileBounds.x - 2, GdxGame.ScreenType.LEVEL_TWO_GAME);
        GridPoint2 pos1 = new GridPoint2(tileBounds.x - 2, posY + 2);
        this.endPortal = ObstacleFactory.createPortal();
        spawnEntityAt(this.endPortal, pos1, true, true);
    }

    /**
     * Spawns an alien monster for this level.
     */
    private void spawnAlienMonster() {
        GridPoint2 pos1 = new GridPoint2(86, 20);
        Entity alienMonster = EnemyFactory.createAlienMonster(player, this);
        spawnEntityAt(alienMonster, pos1, true, true);
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

        newPlayer.getComponent(DoubleJumpComponent.class).setMapEdges(this.mapFixtures);
        return newPlayer;
    }

    private void createCheckpoint() {

        GridPoint2 checkPoint = new GridPoint2(20, 10);
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
        int maxXPos = terrain.getMapBounds(0).x;

        Random randomXPos = new Random();
        int pos = randomXPos.nextInt(maxXPos);
        logger.info("this is x {}", pos);
        GridPoint2 randomPos = new GridPoint2(pos - 1, terrainFactory.getYOfSurface(pos, GdxGame.ScreenType.LEVEL_TWO_GAME));

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

    private void spawnDeathWall() {
        Vector2 deathWallEndPos = new Vector2(this.endOfMap.getPosition().x, this.endOfMap.getPosition().y);
        Entity deathWall = ObstacleFactory.createDeathWall(deathWallEndPos);
        deathWall.getComponent(AnimationRenderComponent.class).scaleEntity();
        deathWall.setScale(3f, terrain.getMapBounds(0).y * terrain.getTileSize());
        spawnEntityAt(deathWall, new GridPoint2(-5, 0), false, false);
    }

    private void playMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
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

        //System.out.println(playerX);
        if (playerX >= 5 && playerX <= 35) {
            camera.getCamera().translate(playerX - camera.getCamera().position.x + 5, 0,0);
            camera.getCamera().update();
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
