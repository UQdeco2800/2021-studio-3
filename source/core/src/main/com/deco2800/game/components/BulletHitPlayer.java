package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;

public class BulletHitPlayer extends Component {
    private Entity target;
    private HitboxComponent hitboxComponent;
    private GameArea GameArea;
    private short Layer;

    public BulletHitPlayer(Entity target, GameArea GameArea) {
        this.target = target;
        this.GameArea = GameArea;

    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("collisionStart", this::Hit);
    }

    private void Hit(Fixture attack, Fixture player) {
        Entity bullet = ((BodyUserData) attack.getBody().getUserData()).entity;
        Entity target = ((BodyUserData) player.getBody().getUserData()).entity;
        if(target == this.target) {
            bullet.getComponent(PhysicsComponent.class).getPhysics().addToDestroy(bullet);
            target.getComponent(CombatStatsComponent.class).decreaseHealth(20);
        }
    }
}
