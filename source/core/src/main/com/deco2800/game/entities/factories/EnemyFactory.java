package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.BulletHitPlayer;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.enemy.AlienBossAttackListener;
import com.deco2800.game.components.enemy.AlienSoldierAttackListener;
import com.deco2800.game.components.obstacle.AttackListener;
import com.deco2800.game.components.tasks.AttackTask;
import com.deco2800.game.components.CombatStatsComponent;
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
 * Factory to create enemy entities that could have different methods to attack the player.
 *
 * <p>Each enemy entity type should have a creation method that returns a corresponding entity.
 */
public class EnemyFactory {
    private static final EnemyConfig configs =
            FileLoader.readClass(EnemyConfig.class, "configs/enemy.json");

    /**
     * Creates an alien monster enemy.
     * @param target the target that the enemy aim to attack
     * @param gameArea the game area
     * @return the alien monster enemy entity
     */
    public static Entity createAlienMonster(Entity target, GameArea gameArea) {
        AlienMonsterConfig config = configs.alienMonster;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(3f, 2f), 0f))
                        .addTask(new AttackTask(target, 2, 10, 6f));

        Entity alienMonster = new Entity()
                .addComponent(new TextureRenderComponent("images/alien_monster.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(aiComponent)
                .addComponent(new AttackListener(target, gameArea));

        PhysicsUtils.setScaledCollider(alienMonster, 1f,1f);
        alienMonster.scaleHeight(2f);
        return alienMonster;
    }

    /**
     * Creates the weapon/bullet for the alien monster enemy.
     * @param from the position of the alien monster that the weapon will be created from
     * @param target the player character that the weapon aim to attack
     * @param gameArea the game area
     * @return the weapon/bullet entity of alien monster
     */
    public static Entity createAlienMonsterWeapon(Entity from, Entity target, GameArea gameArea) {

        float x1 = from.getPosition().x;
        float y1 = from.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;

        Vector2 newTarget = new Vector2(x2 - x1, y2 - y1);

        newTarget = newTarget.scl(1000).add(from.getPosition());
        //newTarget = newTarget.add(from.getPosition());

        Entity alienMonsterWeapon =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/alien_monster_weapon_02.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new BulletHitPlayer(target, gameArea));

        alienMonsterWeapon.getComponent(TextureRenderComponent.class).scaleEntity();
        alienMonsterWeapon.scaleHeight(0.5f);
        PhysicsUtils.setScaledCollider(alienMonsterWeapon, 0.5f, 0.3f);

        alienMonsterWeapon.setPosition(x1 - alienMonsterWeapon.getScale().x / 2 + from.getScale().x / 2,
                y1 - alienMonsterWeapon.getScale().y / 2 + from.getScale().y / 2);

        alienMonsterWeapon.getComponent(PhysicsMovementComponent.class).setTarget(newTarget);
        alienMonsterWeapon.getComponent(PhysicsMovementComponent.class).setMoving(true);
        alienMonsterWeapon.getComponent(ColliderComponent.class).setSensor(true);
        return alienMonsterWeapon;
    }

    /**
     * Creates an alien soldier enemy.
     * @param target the target that the enemy aim to attack
     * @param gameArea the game area
     * @return the alien soldier enemy entity
     */
    public static Entity createAlienSoldier(Entity target, GameArea gameArea) {
        AlienSoldierConfig config = configs.alienSolider;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new AttackTask(target, 3, 10, 6f));

        Entity alienSoldier =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/alien_solider.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
                        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                        .addComponent(aiComponent)
                .addComponent(new AlienSoldierAttackListener(target, gameArea));

        PhysicsUtils.setScaledCollider(alienSoldier, 1f,1f);
        alienSoldier.scaleHeight(1.5f);
        return alienSoldier;
    }

    /**
     * Creates the weapon/bullet for the alien soldier enemy.
     * @param from the position of the alien soldier that the weapon will be created from
     * @param target the player character that the weapon aim to attack
     * @param gameArea the game area
     */
    public static void createAlienSoldierWeapon(Entity from, Entity target, GameArea gameArea) {
        float x1 = from.getPosition().x;
        float y1 = from.getPosition().y;

        Vector2 target1 = new Vector2(x1 + 1, 0);
        Vector2 target2 = new Vector2(x1 - 10, 0);
        Vector2 target3 = new Vector2(x1 + 10, 0);
        Vector2 target4 = new Vector2(x1 - 30, 0);
        Vector2 target5 = new Vector2(x1 + 30, 0);

        Entity alienSoldierWeapon1 =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/alien_solider_weapon_02.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new BulletHitPlayer(target, gameArea));

        Entity alienSoldierWeapon2 =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/alien_solider_weapon_02.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new BulletHitPlayer(target, gameArea));

        Entity alienSoldierWeapon3 =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/alien_solider_weapon_02.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new BulletHitPlayer(target, gameArea));

        Entity alienSoldierWeapon4 =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/alien_solider_weapon_02.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new BulletHitPlayer(target, gameArea));

        Entity alienSoldierWeapon5 =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/alien_solider_weapon_02.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new BulletHitPlayer(target, gameArea));

        alienSoldierWeapon1.getComponent(TextureRenderComponent.class).scaleEntity();
        alienSoldierWeapon1.scaleHeight(0.3f);
        PhysicsUtils.setScaledCollider(alienSoldierWeapon1, 0.3f, 0.3f);

        alienSoldierWeapon2.getComponent(TextureRenderComponent.class).scaleEntity();
        alienSoldierWeapon2.scaleHeight(0.3f);
        PhysicsUtils.setScaledCollider(alienSoldierWeapon2, 0.3f, 0.3f);

        alienSoldierWeapon3.getComponent(TextureRenderComponent.class).scaleEntity();
        alienSoldierWeapon3.scaleHeight(0.3f);
        PhysicsUtils.setScaledCollider(alienSoldierWeapon3, 0.3f, 0.3f);

        alienSoldierWeapon4.getComponent(TextureRenderComponent.class).scaleEntity();
        alienSoldierWeapon4.scaleHeight(0.3f);
        PhysicsUtils.setScaledCollider(alienSoldierWeapon4, 0.3f, 0.3f);

        alienSoldierWeapon5.getComponent(TextureRenderComponent.class).scaleEntity();
        alienSoldierWeapon5.scaleHeight(0.3f);
        PhysicsUtils.setScaledCollider(alienSoldierWeapon5, 0.3f, 0.3f);

        alienSoldierWeapon1.setPosition(x1 - alienSoldierWeapon1.getScale().x / 2 + from.getScale().x / 2,
                y1 - alienSoldierWeapon1.getScale().y / 2 + from.getScale().y / 2);

        alienSoldierWeapon2.setPosition(x1 - alienSoldierWeapon2.getScale().x / 2 + from.getScale().x / 2,
                y1 - alienSoldierWeapon2.getScale().y / 2 + from.getScale().y / 2);

        alienSoldierWeapon3.setPosition(x1 - alienSoldierWeapon3.getScale().x / 2 + from.getScale().x / 2,
                y1 - alienSoldierWeapon3.getScale().y / 2 + from.getScale().y / 2);

        alienSoldierWeapon4.setPosition(x1 - alienSoldierWeapon4.getScale().x / 2 + from.getScale().x / 2,
                y1 - alienSoldierWeapon4.getScale().y / 2 + from.getScale().y / 2);

        alienSoldierWeapon5.setPosition(x1 - alienSoldierWeapon5.getScale().x / 2 + from.getScale().x / 2,
                y1 - alienSoldierWeapon5.getScale().y / 2 + from.getScale().y / 2);

        alienSoldierWeapon1.getComponent(PhysicsMovementComponent.class).setTarget(target1);
        alienSoldierWeapon1.getComponent(PhysicsMovementComponent.class).setMoving(true);
        alienSoldierWeapon1.getComponent(ColliderComponent.class).setSensor(true);

        alienSoldierWeapon2.getComponent(PhysicsMovementComponent.class).setTarget(target2);
        alienSoldierWeapon2.getComponent(PhysicsMovementComponent.class).setMoving(true);
        alienSoldierWeapon2.getComponent(ColliderComponent.class).setSensor(true);

        alienSoldierWeapon3.getComponent(PhysicsMovementComponent.class).setTarget(target3);
        alienSoldierWeapon3.getComponent(PhysicsMovementComponent.class).setMoving(true);
        alienSoldierWeapon3.getComponent(ColliderComponent.class).setSensor(true);

        alienSoldierWeapon4.getComponent(PhysicsMovementComponent.class).setTarget(target4);
        alienSoldierWeapon4.getComponent(PhysicsMovementComponent.class).setMoving(true);
        alienSoldierWeapon4.getComponent(ColliderComponent.class).setSensor(true);

        alienSoldierWeapon5.getComponent(PhysicsMovementComponent.class).setTarget(target5);
        alienSoldierWeapon5.getComponent(PhysicsMovementComponent.class).setMoving(true);
        alienSoldierWeapon5.getComponent(ColliderComponent.class).setSensor(true);

        gameArea.spawnEntity(alienSoldierWeapon1);
        gameArea.spawnEntity(alienSoldierWeapon2);
        gameArea.spawnEntity(alienSoldierWeapon3);
        gameArea.spawnEntity(alienSoldierWeapon4);
        gameArea.spawnEntity(alienSoldierWeapon5);
    }

    /**
     * Creates an alien boss enemy.
     * @param target the target that the enemy aim to attack
     * @param gameArea the game area
     * @return the alien boss enemy entity
     */
    public static Entity createAlienBoss(Entity target, GameArea gameArea) {
        AlienBossConfig config = configs.alienBoss;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(3f, 2f), 0f))
                        .addTask(new AttackTask(target, 1, 10, 6f));

        Entity alienBoss =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/alien_boss.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
                        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                        .addComponent(aiComponent)
                        .addComponent(new AlienBossAttackListener(target, gameArea));

        PhysicsUtils.setScaledCollider(alienBoss, 1f,1f);
        alienBoss.scaleHeight(1.5f);
        return alienBoss;
    }

    /**
     * Creates the weapon/bullet for the alien boss enemy.
     * @param from the position of the alien boss that the weapon will be created from
     * @param target the player character that the weapon aim to attack
     * @param gameArea the game area
     */
    public static void createAlienBossWeapon(Entity from, Entity target, GameArea gameArea) {
        float x1 = from.getPosition().x;
        float y1 = from.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;

        Vector2 straightTarget = new Vector2(x2 - x1, y2 - y1);

        double upX = (Math.cos((Math.PI) / 4) * straightTarget.x) - (Math.sin((Math.PI) / 4) * straightTarget.y);
        double upY = (Math.sin((Math.PI) / 4) * straightTarget.x) + (Math.cos((Math.PI) / 4) * straightTarget.y);
        Vector2 upRotate = new Vector2((float) upX, (float) upY);

        double downX = (Math.cos(-(Math.PI) / 4) * straightTarget.x) - (Math.sin(-(Math.PI) / 4) * straightTarget.y);
        double downY = (Math.sin(-(Math.PI) / 4) * straightTarget.x) + (Math.cos(-(Math.PI) / 4) * straightTarget.y);
        Vector2 downRotate = new Vector2((float) downX, (float) downY);

        straightTarget = straightTarget.scl(100);
        straightTarget = straightTarget.add(from.getPosition());

        downRotate = downRotate.scl(100);
        downRotate = downRotate.add(from.getPosition());

        upRotate = upRotate.scl(100);
        upRotate = upRotate.add(from.getPosition());

        float rotation = (MathUtils.radiansToDegrees * MathUtils.atan2(straightTarget.y - y1, straightTarget.x - x1));
        float rotationUp = (MathUtils.radiansToDegrees * MathUtils.atan2(upRotate.y - y1, upRotate.x - x1));
        float rotationDown = (MathUtils.radiansToDegrees * MathUtils.atan2(downRotate.y - y1, downRotate.x - x1));

        Entity alienBossWeapon1 =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/alien_boss_weapon_01.png", rotation))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new BulletHitPlayer(target, gameArea));

        Entity alienBossWeapon2 =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/alien_boss_weapon_01.png", rotationUp))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new BulletHitPlayer(target, gameArea));

        Entity alienBossWeapon3 =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/alien_boss_weapon_01.png", rotationDown))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new BulletHitPlayer(target, gameArea));

        alienBossWeapon1.getComponent(TextureRenderComponent.class).scaleEntity();
        alienBossWeapon1.scaleHeight(0.3f);
        PhysicsUtils.setScaledCollider(alienBossWeapon1, 0.3f, 0.3f);

        alienBossWeapon2.getComponent(TextureRenderComponent.class).scaleEntity();
        alienBossWeapon2.scaleHeight(0.3f);
        PhysicsUtils.setScaledCollider(alienBossWeapon2, 0.3f, 0.3f);

        alienBossWeapon3.getComponent(TextureRenderComponent.class).scaleEntity();
        alienBossWeapon3.scaleHeight(0.3f);
        PhysicsUtils.setScaledCollider(alienBossWeapon3, 0.3f, 0.3f);

        alienBossWeapon1.setPosition(x1 - alienBossWeapon1.getScale().x / 2 + from.getScale().x / 2,
                y1 - alienBossWeapon1.getScale().y / 2 + from.getScale().y / 2);

        alienBossWeapon2.setPosition(x1 - alienBossWeapon2.getScale().x / 2 + from.getScale().x / 2,
                y1 - alienBossWeapon2.getScale().y / 2 + from.getScale().y / 2);

        alienBossWeapon3.setPosition(x1 - alienBossWeapon3.getScale().x / 2 + from.getScale().x / 2,
                y1 - alienBossWeapon3.getScale().y / 2 + from.getScale().y / 2);

        alienBossWeapon1.getComponent(PhysicsMovementComponent.class).setTarget(straightTarget);
        alienBossWeapon1.getComponent(PhysicsMovementComponent.class).setMoving(true);
        alienBossWeapon1.getComponent(ColliderComponent.class).setSensor(true);

        alienBossWeapon2.getComponent(PhysicsMovementComponent.class).setTarget(upRotate);
        alienBossWeapon2.getComponent(PhysicsMovementComponent.class).setMoving(true);
        alienBossWeapon2.getComponent(ColliderComponent.class).setSensor(true);

        alienBossWeapon3.getComponent(PhysicsMovementComponent.class).setTarget(downRotate);
        alienBossWeapon3.getComponent(PhysicsMovementComponent.class).setMoving(true);
        alienBossWeapon3.getComponent(ColliderComponent.class).setSensor(true);

        gameArea.spawnEntity(alienBossWeapon1);
        gameArea.spawnEntity(alienBossWeapon2);
        gameArea.spawnEntity(alienBossWeapon3);
    }
}



