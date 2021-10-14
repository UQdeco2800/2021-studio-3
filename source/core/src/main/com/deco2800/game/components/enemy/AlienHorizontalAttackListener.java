package com.deco2800.game.components.enemy;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.EnemyFactory;


    public class AlienHorizontalAttackListener extends Component {
        private Entity target;
        private GameArea gameArea;

        public AlienHorizontalAttackListener(Entity target, GameArea gameArea) {
            this.target = target;
            this.gameArea = gameArea;
        }

        public void create() {
            super.create();
            entity.getEvents().addListener("attack", this::attack);
        }

        void attack() {
            EnemyFactory.createAlienSoldierHorizontalWeapon(this.entity,target, gameArea);
        }
    }


