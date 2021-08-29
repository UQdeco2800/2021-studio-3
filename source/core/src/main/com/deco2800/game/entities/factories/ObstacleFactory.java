package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.ai.tasks.AITaskComponent;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;

import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

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

  public static Entity createAsteroidFree(Entity target) {
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
    //asteroidFire.getComponent(TextureRenderComponent.class).scaleEntity();
    //asteroidFire.scaleHeight(1.5f);
    //PhysicsUtils.setScaledCollider(asteroidFire, 1.5f, 0.5f);
    return asteroidFire;
  }

  public static Entity createRobot(Entity target) {
    RobotConfig config = configs.robot;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(10f, 0f), 0f));
    Entity robot =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
                    .addComponent(new TextureRenderComponent("images/robot1.png"))
                    .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                    .addComponent(aiComponent);
    robot.getComponent(PhysicsComponent.class).setBodyType(BodyType.DynamicBody);

    return robot;
  }


  public static Entity createRock1() {
    Entity rock1 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/rock1.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    rock1.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    rock1.getComponent(TextureRenderComponent.class).scaleEntity();
    rock1.scaleHeight(0.7f);
    PhysicsUtils.setScaledCollider(rock1, 0.5f, 0.3f);
    return rock1;
  }

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

  public static Entity createRock4() {
    Entity rock4 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/rock4.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    rock4.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    rock4.getComponent(TextureRenderComponent.class).scaleEntity();
    rock4.scaleHeight(0.9f);
    PhysicsUtils.setScaledCollider(rock4, 0.5f, 0.3f);
    return rock4;
  }

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

  public static Entity createPlatform1() {
    Entity platform1 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/platform1.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    platform1.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    platform1.getComponent(TextureRenderComponent.class).scaleEntity();
    platform1.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platform1, 0.5f, 0.3f);
    return platform1;
  }

  public static Entity createPlatform2() {
    Entity platform2 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/platform2.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    platform2.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    platform2.getComponent(TextureRenderComponent.class).scaleEntity();
    platform2.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platform2, 0.5f, 0.3f);
    return platform2;
  }

  public static Entity createPlatform3() {
    Entity platform3 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/platform3.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    platform3.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    platform3.getComponent(TextureRenderComponent.class).scaleEntity();
    platform3.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platform3, 0.5f, 0.3f);
    return platform3;
  }

  public static Entity createPlatform4() {
    Entity platform4 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/platform4.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    platform4.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    platform4.getComponent(TextureRenderComponent.class).scaleEntity();
    platform4.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platform4, 0.5f, 0.3f);
    return platform4;
  }

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

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}

