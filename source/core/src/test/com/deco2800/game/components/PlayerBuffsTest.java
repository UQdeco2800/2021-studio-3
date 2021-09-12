package com.deco2800.game.components;

import com.deco2800.game.components.maingame.BuffManager;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class PlayerBuffsTest {
    //@Mock CombatStatsComponent combat;
    @Mock Entity player;
    @Mock BuffInformation buffInfo;


    @Test
    public void applyInvincibilityTest() {
        CombatStatsComponent combat = new CombatStatsComponent(100, 20);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combat);
        PlayerBuffs.applyInvincibility(player);
        player.getComponent(CombatStatsComponent.class).hit(combat);
        assertEquals(100, player.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    public void setInfiniteStaminaTest() {
        SprintComponent sprint = new SprintComponent(80);
        when(player.getComponent(SprintComponent.class)).thenReturn(sprint);
        PlayerBuffs.setInfiniteStamina(player);
        player.getComponent(SprintComponent.class).removeSprint(20);
        assertEquals(100, player.getComponent(SprintComponent.class).getSprint());
    }

    @Test
    public void setHealthFullTest() {
        CombatStatsComponent combat = new CombatStatsComponent(100, 20);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combat);
        player.getComponent(CombatStatsComponent.class).hit(combat);
        assertEquals(80, player.getComponent(CombatStatsComponent.class).getHealth());
        PlayerBuffs.setHealthFull(player);
        assertEquals(100, player.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    public void setDoubleHurtTest() {
        CombatStatsComponent combat = new CombatStatsComponent(100, 20);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combat);
        PlayerBuffs.setDoubleHurt(player);
        player.getComponent(CombatStatsComponent.class).hit(combat);
        assertEquals(60, player.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    public void setNoJumpingTest() {

    }

    @Test
    public void removeInvincibilityTest() {
        CombatStatsComponent combat = new CombatStatsComponent(100, 20);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combat);
        PlayerBuffs.applyInvincibility(player);
        player.getComponent(CombatStatsComponent.class).hit(combat);
        assertEquals(100, player.getComponent(CombatStatsComponent.class).getHealth());
        when (buffInfo.getType()).thenReturn(BuffManager.BuffTypes.BT_INVIN);
        PlayerBuffs.removeTimedBuff(buffInfo, player);
        player.getComponent(CombatStatsComponent.class).hit(combat);
        assertEquals(80, player.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    public void makeStaminaFiniteTest() {
        SprintComponent sprint = new SprintComponent(80);
        when(player.getComponent(SprintComponent.class)).thenReturn(sprint);
        PlayerBuffs.setInfiniteStamina(player);
        player.getComponent(SprintComponent.class).removeSprint(20);
        assertEquals(100, player.getComponent(SprintComponent.class).getSprint());
        when (buffInfo.getType()).thenReturn(BuffManager.BuffTypes.BT_INF_SPRINT);
        PlayerBuffs.removeTimedBuff(buffInfo, player);
        player.getComponent(SprintComponent.class).removeSprint(20);
        assertEquals(80, player.getComponent(SprintComponent.class).getSprint());
    }

    @Test
    public void removeNoJumpingTest() {

    }

}