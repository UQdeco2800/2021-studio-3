package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.AsteroidFireConfig;
import com.deco2800.game.entities.configs.GhostKingConfig;
import com.deco2800.game.entities.configs.NPCConfigs;
import com.deco2800.game.entities.configs.ObstacleConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
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
