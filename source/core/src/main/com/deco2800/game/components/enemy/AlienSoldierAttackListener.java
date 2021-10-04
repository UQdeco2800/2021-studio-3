package com.deco2800.game.components.enemy;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.EnemyFactory;

public class AlienSoldierAttackListener extends Component {
    private Entity target;
    private GameArea gameArea;

    public AlienSoldierAttackListener(Entity target, GameArea gameArea) {
        this.target = target;
        this.gameArea = gameArea;
    }

    public void create() {
        super.create();
        entity.getEvents().addListener("attack", this::attack);
    }

    void attack() {
        EnemyFactory.createAlienSoldierWeapon(this.entity,target, gameArea);
    }
}
