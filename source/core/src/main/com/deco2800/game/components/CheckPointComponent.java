package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.LevelTwoArea;
import com.deco2800.game.areas.LevelThreeArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.areas.ForestGameArea;

/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and apply a knockback.
 *
 * <p>Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>Damage is only applied if target entity has a CombatStatsComponent.
 */
public class CheckPointComponent extends Component {
    private short targetLayer;
    private HitboxComponent hitboxComponent;
    private GdxGame game;
    private ForestGameArea area;
    private LevelTwoArea area2;
    private LevelThreeArea area3;
    /**
     *
     * @param targetLayer The physics layer of the target's collider.
     */
    public CheckPointComponent(short targetLayer, ForestGameArea area) {
        this.targetLayer = targetLayer;
        this.area = area;
    }

    public CheckPointComponent(short targetLayer, LevelTwoArea area) {
        this.targetLayer = targetLayer;
        this.area2 = area;
    }

    public CheckPointComponent(short targetLayer, LevelThreeArea area) {
        this.targetLayer = targetLayer;
        this.area3 = area;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Try to attack target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
            area.setCheckPointStatus(1);
        }

    }
}