package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.LevelTwoArea;
import com.deco2800.game.areas.LevelThreeArea;
import com.deco2800.game.components.BulletHitPlayer;
import com.deco2800.game.components.CheckPointComponent;
import com.deco2800.game.components.TouchAttackComponent;

import com.deco2800.game.components.obstacle.AttackListener;
import com.deco2800.game.components.obstacle.ObstacleAnimationController;

import com.deco2800.game.components.obstacle.UfoAnimationController;

import com.deco2800.game.components.tasks.AttackTask;
import com.deco2800.game.components.tasks.ChaseTask;

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
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class EnemyFactory {
    private static final EnemyConfig configs =
            FileLoader.readClass(EnemyConfig.class, "configs/enemy.json");

    public static Entity createAlienMonster(Entity target, GameArea gameArea) {
        AlienMonsterConfig config = configs.alienMonster;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        //.addTask(new FallTask(5f));
                        .addTask(new WanderTask(new Vector2(3f, 2f), 0f))
                        .addTask(new AttackTask(target, 2, 10, 6f));

        Entity alienMonster = new Entity()
                .addComponent(new TextureRenderComponent("images/ufo_2.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(aiComponent)
                .addComponent(new AttackListener(target, gameArea));

//    AnimationRenderComponent animator =
//            new AnimationRenderComponent(
//                    ServiceLocator.getResourceService().getAsset("images/ufo_animation.atlas", TextureAtlas.class));
//    animator.addAnimation("hit_ufo", 0.5f, Animation.PlayMode.LOOP_REVERSED);
//    animator.addAnimation("ufo", 0.5f, Animation.PlayMode.LOOP);
//
//    ufo.addComponent(animator);
//    ufo.addComponent(new UfoAnimationController());
//
//    ufo.getComponent(AnimationRenderComponent.class).scaleEntity();
        PhysicsUtils.setScaledCollider(alienMonster, 1f,1f);
        alienMonster.scaleHeight(2f);
        return alienMonster;
    }

    public static Entity createAlienMonsterWeapon(Entity from, Entity target, GameArea gameArea) {

        AlienMonsterWeaponConfig config = configs.alienMonsterWeapon;
        float x1 = from.getPosition().x;
        float y1 = from.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;

        Vector2 newTarget = new Vector2(x2 - x1, y2 - y1);

        newTarget = newTarget.scl(100);
        newTarget = newTarget.add(from.getPosition());

        float rotation = (MathUtils.radiansToDegrees * MathUtils.atan2(newTarget.y - y1, newTarget.x - x1));

        Entity alienMonsterWeapon =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/ufo_2.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new BulletHitPlayer(target, gameArea));
                      //  .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
                       // .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                       // .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
                        //.addComponent(new CombatStatsComponent(config.health, config.baseAttack));

        alienMonsterWeapon.getComponent(TextureRenderComponent.class).scaleEntity();
        alienMonsterWeapon.scaleHeight(0.7f);
        PhysicsUtils.setScaledCollider(alienMonsterWeapon, 0.5f, 0.3f);

        alienMonsterWeapon.setPosition(x1 - alienMonsterWeapon.getScale().x / 2 + from.getScale().x / 2,
                y1 - alienMonsterWeapon.getScale().y / 2 + from.getScale().y / 2);

        alienMonsterWeapon.getComponent(PhysicsMovementComponent.class).setTarget(newTarget);
        alienMonsterWeapon.getComponent(PhysicsMovementComponent.class).setMoving(true);
        alienMonsterWeapon.getComponent(ColliderComponent.class).setSensor(true);
        return alienMonsterWeapon;
    }

}



