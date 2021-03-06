package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.LevelTwoArea;
import com.deco2800.game.areas.LevelThreeArea;
import com.deco2800.game.components.CheckPointComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.obstacle.ObstacleAnimationController;
import com.deco2800.game.components.obstacle.UfoAnimationController;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.components.CombatStatsComponent;


import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {
  private static final ObstacleConfig configs =
          FileLoader.readClass(ObstacleConfig.class, "configs/obstacle.json");
  /**
   * Creates a tree entity.
   * @return entity
   */
  public static Entity createTree() {
    Entity tree =
        new Entity()
            .addComponent(new TextureRenderComponent("images/tree.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.DynamicBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(tree, 0.5f, 1f);
    return tree;
  }

  /**
   * Creates a portal entity.
   * @return entity
   */
  public static Entity createPortal() {
    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/PortalAnimation.atlas",
                            TextureAtlas.class));
    animator.addAnimation("Portal", 0.2f, Animation.PlayMode.LOOP);
    // Starts the idle animation
    animator.startAnimation("Portal");
    Entity portal =
            new Entity()
                    //.addComponent(new TextureRenderComponent("images/portal.png"))
                    .addComponent(animator)
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    portal.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    //portal.getComponent(TextureRenderComponent.class).scaleEntity();
    portal.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(portal, 0.5f, 1f);
    return portal;
  }

  /**
   * Creates a spaceship entity.
   * @return entity
   */
  public static Entity createSpaceship() {
    Entity portal =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/Spaceship.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    portal.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    portal.getComponent(TextureRenderComponent.class).scaleEntity();
    portal.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(portal, 0.5f, 1f);
    return portal;
  }

  /**
   * Creates a asteroid entity.
   * @return entity
   */
  public static Entity createAsteroid() {
    Entity asteroid =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/broken_asteriod.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    asteroid.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    asteroid.getComponent(TextureRenderComponent.class).scaleEntity();
    asteroid.scaleHeight(0.7f);
    PhysicsUtils.setScaledCollider(asteroid, 0.7f, 0.5f);
    return asteroid;
  }

  /**
   * Creates a asteroid fire entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createAsteroidFire(Entity target) {
    //Entity attackObstacle = createBaseNPC(target);
    AsteroidFireConfig config = configs.asteroidFire;


    Entity asteroidFire =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
                    .addComponent(new TextureRenderComponent("images/asteroid_fire1.png"))
                    .addComponent(new CombatStatsComponent(config.health, config.baseAttack));
    asteroidFire.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);

    return asteroidFire;
  }

  /**
   * Creates an animated asteroid fire entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createAsteroidAnimatedFire(Entity target) {

    AsteroidFireConfig config = configs.asteroidFire;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(0f, 0f), 0f));
    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService()
                            .getAsset("images/asteroidFireNew.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.2f, Animation.PlayMode.LOOP);
    Entity asteroidFire =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
                    .addComponent(aiComponent);
    asteroidFire.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new ObstacleAnimationController());
    asteroidFire.getComponent(PhysicsComponent.class).setBodyType(BodyType.DynamicBody);
    asteroidFire.scaleHeight(1.5f);
    asteroidFire.getComponent(HitboxComponent.class).setAsBox(new Vector2(0.3f, 1.5f));

    // Allows player to pass through fire while taking damage
    asteroidFire.getComponent(ColliderComponent.class).setSensor(true);
    asteroidFire.getComponent(ColliderComponent.class).setAsBox(new Vector2(0,0),asteroidFire.getCenterPosition());
    return asteroidFire;
  }

  /**
   * Creates a robot entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createRobot(Entity target) {
    RobotConfig config = configs.robot;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(10f, 0f), 0f));

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService()
                            .getAsset("images/robot.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.5f, Animation.PlayMode.LOOP);

    Entity robot =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
                    .addComponent(aiComponent);

    robot.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new ObstacleAnimationController());
    robot.getComponent(PhysicsComponent.class).setBodyType(BodyType.DynamicBody);
    robot.scaleHeight(1f);

    return robot;
  }

  public static Entity createUfo(Entity target) {
    UfoConfig config = configs.ufo;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(3f, 2f), 0f))
                    .addTask(new ChaseTask(target, 2,2f,2.5f));

    Entity ufo = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(aiComponent);

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/ufo_animation.atlas", TextureAtlas.class));
    animator.addAnimation("hit_ufo", 1f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("ufo", 1f, Animation.PlayMode.LOOP);

    ufo.addComponent(animator);
    ufo.addComponent(new UfoAnimationController());

    ufo.getComponent(AnimationRenderComponent.class).scaleEntity();
    PhysicsUtils.setScaledCollider(ufo, 0.5f,0.3f);
    ufo.scaleHeight(2f);
    return ufo;
  }

  /**
   * Creates a rock entity.
   *
   * @return entity
   */
  public static Entity createRock2() {
    Entity rock2 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/rock2.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    rock2.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    rock2.getComponent(TextureRenderComponent.class).scaleEntity();
    rock2.scaleHeight(0.8f);
    PhysicsUtils.setScaledCollider(rock2, 0.5f, 0.3f);
    return rock2;
  }

  /**
   * Creates a rock entity.
   *
   * @return entity
   */
  public static Entity createRock3() {
    Entity rock3 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/rock3.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    rock3.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    rock3.getComponent(TextureRenderComponent.class).scaleEntity();
    rock3.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(rock3, 0.5f, 0.3f);
    return rock3;
  }

  /**
   * Creates a rock entity.
   *
   * @return entity
   */
  public static Entity createRock4() {
    Entity rock4 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/rock4.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC));

    rock4.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    rock4.getComponent(TextureRenderComponent.class).scaleEntity();
    rock4.scaleHeight(0.9f);
    PhysicsUtils.setScaledCollider(rock4, 0.5f, 0.3f);
    return rock4;
  }

  /**
   * Creates a rock entity.
   *
   * @return entity
   */
  public static Entity createPlanet1() {
    Entity planet1 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/planet1.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE));

    planet1.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    planet1.getComponent(TextureRenderComponent.class).scaleEntity();
    planet1.scaleHeight(1.3f);
    PhysicsUtils.setScaledCollider(planet1, 0.5f, 0.5f);
    return planet1;
  }

  /**
   * Creates a asteroid entity.
   *
   * @return entity
   */
  public static Entity createAsteroid1() {
    Entity asteroid1 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/asteroid.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE));

    asteroid1.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    asteroid1.getComponent(TextureRenderComponent.class).scaleEntity();
    asteroid1.scaleHeight(1.2f);
    PhysicsUtils.setScaledCollider(asteroid1, 0.5f, 0.5f);
    return asteroid1;
  }

  /**
   * Creates a asteroid entity.
   *
   * @return entity
   */
  public static Entity createAsteroid2() {
    Entity asteroid2 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/asteroid_2.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE));

    asteroid2.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    asteroid2.getComponent(TextureRenderComponent.class).scaleEntity();
    asteroid2.scaleHeight(0.8f);
    PhysicsUtils.setScaledCollider(asteroid2, 0.5f, 0.5f);
    return asteroid2;
  }

  /**
   * Creates a platform entity.
   *
   * @return entity
   */
  public static Entity createPlatform1() {
    Entity platform1 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/platform1.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    platform1.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    platform1.getComponent(TextureRenderComponent.class).scaleEntity();
    platform1.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platform1, 0.5f, 0.5f);
    return platform1;
  }

  /**
   * Creates a platform entity.
   *
   * @return entity
   */
  public static Entity createPlatform2() {
    Entity platform2 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/platform2.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    platform2.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    platform2.getComponent(TextureRenderComponent.class).scaleEntity();
    platform2.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platform2, 0.8f, 0.3f);
    return platform2;
  }

  /**
   * Creates a moving platform entity which moves horizontally in a fixed area at a constant speed.
   *
   * @return entity
   */
  public static Entity createHorizontalMovingPlatform() {
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new Platform_x_Task(3f,1));
                    //.addTask(new Platform_y_Task(3f,1));

    Entity platform3 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/platform3.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                    .addComponent(aiComponent);



    platform3.getComponent(PhysicsComponent.class).setBodyType(BodyType.KinematicBody);
    platform3.getComponent(TextureRenderComponent.class).scaleEntity();
    platform3.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platform3, 0.7f, 0.5f);
    return platform3;
  }

  /**
   * Creates a moving platform entity which moves vertically in a fixed area at a constant speed.
   *
   * @return entity
   */
  public static Entity createVerticalMovingPlatform() {

    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new Platform_y_Task(3f,1));

    Entity platform4 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/platform4.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                    .addComponent(aiComponent);

    platform4.getComponent(PhysicsComponent.class).setBodyType(BodyType.KinematicBody);
    platform4.getComponent(TextureRenderComponent.class).scaleEntity();
    platform4.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platform4, 0.7f, 0.5f);
    return platform4;
  }

  /**
   * Creates a platform entity.
   *
   * @return entity
   */
  public static Entity createPlatform5() {
    Entity platform5 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/platform5.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    platform5.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    platform5.getComponent(TextureRenderComponent.class).scaleEntity();
    platform5.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platform5, 0.5f, 0.3f);
    return platform5;
  }

  /**
   * Creates a building entity.
   *
   * @return entity
   */
  public static Entity createBuilding1() {
    Entity building1 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/building_1.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE));

    building1.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    building1.getComponent(TextureRenderComponent.class).scaleEntity();
    building1.scaleHeight(4f);
    PhysicsUtils.setScaledCollider(building1, 0.5f, 0.5f);
    return building1;

  }

  /**
   * Creates a harmless egg.
   *
   * @return entity
   */
  public static Entity createDragonEgg() {
    Entity egg =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/harmless_egg.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    egg.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    egg.getComponent(TextureRenderComponent.class).scaleEntity();
    egg.scaleHeight(1.5f);
    PhysicsUtils.setScaledCollider(egg, 0.5f, 0.9f);
    return egg;
  }

  /**
   * Creates the empty nest.
   *
   * @return entity
   */
  public static Entity createEmptyNest() {
    Entity egg =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/empty_nest.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    egg.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    egg.getComponent(TextureRenderComponent.class).scaleEntity();
    egg.scaleHeight(0.75f);
    return egg;
  }

  /**
   * Creates a table of information for the buffs and debuffs.
   *
   * @return entity
   */
  public static Entity createBuffDebuffInformation() {
    Entity info =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/buff_debuff_info.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE));

    info.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    info.getComponent(TextureRenderComponent.class).scaleEntity();
    info.scaleHeight(6f);
    return info;
  }

  /**
   * Creates an invisible physics wall.
   * @param width Wall width in world units
   * @param height Wall height in world units
   * @return Wall entity of given width and height
   */
  public static Entity createWall(float width, float height) {
    Entity wall = new Entity()
        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    wall.setScale(width, height);
    return wall;
  }

  public static Entity createDeathFloor(float width, float height) {
    DeathFloorConfig deathFloor = configs.deathFloor;

    Entity floor = new Entity()
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
            .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    floor.addComponent(new CombatStatsComponent(1000, deathFloor.baseAttack));
    floor.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    floor.setScale(width, height);
    return floor;
  }

  /**
   * Creates a checkpoint entity.
   * @return entity
   */
  public static Entity createCheckpoint(Entity target, ForestGameArea area) {

    Entity checkpoint =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE))
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new CheckPointComponent(PhysicsLayer.PLAYER, area))
                    .addComponent(new TextureRenderComponent("images/untouchedCheckpoint.png"));



    PhysicsUtils.setScaledCollider(checkpoint, 0.9f, 0.4f);
    return checkpoint;
  }


  /**
   * Create a new death wall and start to move from left to end of map
   * @param target the target that the death wall toward with.
   *               Normally the target should be the right air wall of
   *               the game
   * @param speed the speed that a death wall should move
   * @return A new Entity death wall
   */
  public static Entity createDeathWall(Vector2 target, float speed, int levelNumber) {
    DeathWallConfig config = configs.deathWall;
    String atlas = "";
    String animation = "";

    switch (levelNumber){
      case 1:
        atlas = "images/SerpentLevel1.atlas";
        animation = "Serpent1.1";
        break;
      case 2:
        atlas = "images/Lv2SerpentAnimation.atlas";
        animation = "Serpent2.1";
        break;
      case 3:
        atlas = "images/Lv3SerpentAnimation.atlas";
        animation = "Serpent3.1";
        break;
      case 4:
        atlas = "images/Lv4SerpentAnimation.atlas";
        animation = "Serpent4.1";
        break;
    }

    AnimationRenderComponent animator;

    animator = new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset(atlas,
                            TextureAtlas.class));
    animator.addAnimation(animation, 0.15f, Animation.PlayMode.LOOP);
    animator.startAnimation(animation);

    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new MovingTask(target, speed));

    return new Entity()
            .addComponent(new PhysicsComponent().setBodyType(BodyType.DynamicBody))
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE))
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
            .addComponent(new CombatStatsComponent(config.health, 100))
            .addComponent(aiComponent)
            .addComponent(animator);
  }

  /**
   * Creates a checkpoint entity on level 2.
   * @return entity
   */
  public static Entity createCheckpoint(Entity target, LevelTwoArea area) {

    Entity checkpoint =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE))
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new CheckPointComponent(PhysicsLayer.PLAYER, area))
                    .addComponent(new TextureRenderComponent("images/untouchedCheckpoint.png"));



    PhysicsUtils.setScaledCollider(checkpoint, 0.9f, 0.4f);
    return checkpoint;
  }

  public static Entity createCheckpoint(Entity target, LevelThreeArea area) {

    Entity checkpoint =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE))
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new CheckPointComponent(PhysicsLayer.PLAYER, area))
                    .addComponent(new TextureRenderComponent("images/untouchedCheckpoint.png"));



    PhysicsUtils.setScaledCollider(checkpoint, 0.9f, 0.4f);
    return checkpoint;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}

