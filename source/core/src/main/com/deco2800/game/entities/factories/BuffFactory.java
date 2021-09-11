package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.BuffInformation;

import com.deco2800.game.components.maingame.BuffManager;
import com.deco2800.game.components.tasks.FloatTask;
import com.deco2800.game.components.tasks.MovementTask;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for creating buffs & debuffs. This factory is called by the
 * BuffManager when a new buff is to be spawned in.
 *
 * All buffs have a designated texture which should be defined in the
 * BuffManager's getTexture method. The action of buffs is defined in
 * PlayerBuffs.java and called by the BuffManager if a player collides with a
 * buff.
 * */
public class BuffFactory {
    /* Debugging */
    private static final Logger logger = LoggerFactory.getLogger(BuffFactory.class);

    /**
     * Creates and returns a standard buff. Since buffs are just placed on
     * the map and acquired by the player, it is unlikely that the components
     * on buffs will change.
     *
     * After a buff is created, an information class (BuffInformation) is
     * created to store information about the buff which may be needed later
     * in its usage. The buff is then registered with the BuffManager.
     *
     * @param type the type of buff being created
     * @param manager the BuffManager which will oversee the actions, timeouts,
     *                creation and deletion of buffs.
     * */
    public static Entity createBuff(BuffManager.BuffTypes type,
            BuffManager manager) {
        /* Get the texture for the buff */
        String texture = manager.getTexture(type);

        /* Create the buff */
        Entity buff = new Entity().addComponent(new TextureRenderComponent(texture))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody));

        /* Create the BuffInformation for the buff */
        BuffInformation buffInfo = new BuffInformation(buff, type,
                ServiceLocator.getTimeSource().getTime());

        /* Register this new buff with the buff manager */
        manager.registerBuff(buffInfo);
        logger.info("New buff registered and created!");
        return buff;
    }

    /*public static Entity createBuffAnimation(BuffManager.BuffPickup pickup, BuffManager manager,
                                             Vector2 target) {

        String texture = manager.getPickupTexture(pickup);
        AITaskComponent buffComponent = new AITaskComponent().addTask(new WanderTask(new Vector2(0f, 3f), 0));
        //BuffTaskComponent buffComponent = new BuffTaskComponent().addTask(new FloatTask(new Vector2(0f, 3f), 0));



        Entity buffPickup = new Entity().addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.DynamicBody))
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE))
                .addComponent(new TextureRenderComponent(texture))
                .addComponent(buffComponent);
        //buffPickup.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);


        return buffPickup;

    }*/

    public static Entity createBuffAnimation(BuffManager.BuffPickup pickup, BuffManager manager) {
        /* Get the texture for the pickup */
        String texture = manager.getPickupTexture(pickup);
        AITaskComponent buffComponent = new AITaskComponent().addTask(new FloatTask(new Vector2(0f, 3f)));
        //BuffTaskComponent buffComponent = new BuffTaskComponent().addTask(new FloatTask(new Vector2(0f, 3f)));

        Entity buffPickup = new Entity().addComponent(new TextureRenderComponent(texture))
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.DynamicBody))
                .addComponent(new PhysicsMovementComponent())
                .addComponent(buffComponent);


        /*Entity buffPickup = new Entity().addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.DynamicBody))
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE))
                .addComponent(new TextureRenderComponent(texture))
                .addComponent(buffComponent);*/
        //buffPickup.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);


        return buffPickup;

    }
}
