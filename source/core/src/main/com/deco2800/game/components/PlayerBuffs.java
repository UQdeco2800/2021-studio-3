package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.maingame.BuffManager;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.HitboxComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which handles the behaviour of the different buffs & debuffs available
 * to a player. Methods of this class are called when the player collides with
 * the corresponding buff or debuff. Methods of this class also handle removing
 * the effects of time-based buffs.
 *
 * This class allows for easy extension and alteration of existing buffs and
 * debuffs.
 * */
public abstract class PlayerBuffs {
    /* Debugging */
    private static final Logger logger = LoggerFactory.getLogger(PlayerBuffs.class);

    /** --- Buffs --- **/

    public static void increasePlayerHP(Entity player) {
        // Implement the player's health increasing once.
        logger.info("Increased the players health!");
    }

    public static void applyInvincibility(Entity player) {
        player.getComponent(CombatStatsComponent.class).setInvincibility(true);
    }

    public static void setInfiniteStamina(Entity player) {
        // Implement the player not losing stamina while sprinting
    }

    public static void setHealthFull(Entity player) {
        player.getComponent(CombatStatsComponent.class).setFullHeal();
    }

    /** --- Debuffs --- **/

    public static void reducePlayerHP(Entity player) {
        // Implement the player's health decreasing once.
        logger.info("Decreased the players' health!");
    }

    public static void makePlayerGiant(Entity player) {
        // Implement making the player giant
        // Works (sort of), but isn't great right now since the map is so small.

        /*
        player.setScale(new Vector2(6,6));
        player.getComponent(HitboxComponent.class).setAsBox(new Vector2(6,6));
        */

    }

    public static void setDoubleHurt(Entity player) {
        player.getComponent(CombatStatsComponent.class).setDoubleHurt(true);
    }

    public static void noJumping(Entity player) {
        // Implement making the player unable to jump
    }

    /**
     * Handler method for determining which buff to remove, depending on the
     * buffs type. The appropriate de/buff-removal function is called to remove
     * the de/buff effects from the player.
     *
     * This method must be updated if a new timed buff is created.
     *
     * @param buffInfo information about the buff being removed from the player
     * @param player the player from which this buff's effects are being removed
     * */
    public static void removeTimedBuff(BuffInformation buffInfo, Entity player) {
        BuffManager.BuffTypes type = buffInfo.getType();
        switch (type) {
            case BT_INVIN:
                removeInvincibility(player);
                break;
            case DT_GIANT:
                makePlayerNormalSize(player);
                break;
            case BT_INF_SPRINT:
                makeStaminaFinite(player);
                break;
            case DT_NO_JUMP:
                removeNoJumping(player);
                break;
            case DT_DOUBLE_DMG:
                removeDoubleHurt(player);
                break;

        }
    }

    /** --- Removing the effects of timed buffs / debuffs --- **/

    public static void removeInvincibility(Entity player) {
        player.getComponent(CombatStatsComponent.class).setInvincibility(false);
    }

    public static void removeDoubleHurt(Entity player) {
        player.getComponent(CombatStatsComponent.class).setDoubleHurt(false);
    }

    public static void makePlayerNormalSize(Entity player) {
        // Implement making the player normal size again (default)
    }

    public static void makeStaminaFinite(Entity player) {
        // Implement the player losing stamina when they sprint (default)
    }

    public static void removeNoJumping(Entity player) {
        // Implement allowing the player to jump again (default)
    }
}