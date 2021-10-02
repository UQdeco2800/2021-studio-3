package com.deco2800.game.components.obstacle;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.EnemyFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.NPCFactory;



public class AttackListener extends Component {
    private Entity target;
    private GameArea gameArea;

    public AttackListener(Entity target, GameArea gameArea) {
        this.target = target;
        this.gameArea = gameArea;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("attack", this::attack);
    }

    void attack() {
        gameArea.spawnEntity(EnemyFactory.createAlienMonsterWeapon(this.entity, target, gameArea));

    }
}
