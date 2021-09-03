package com.deco2800.game.components.maingame;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.BuffInformation;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.PlayerBuffs;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Oversees the creation and management of buffs and debuffs in the game.
 * Checks buff-related information on each frame. Handles the creation,
 * spawning, despawning and activation of buffs and debuffs on the map.
 *
 * Currently, timed buffs do not stack.
 *
 * - Keeps track of the buffs currently spawned on the map
 * - Keeps track of the time-based buffs which are currently affecting the
 *   player.
 * - Handles despawning timed-out buffs.
 * - Handles removing timed-out buffs' effects from the player when their
 *   timer is up.
 * - Handles spawning new buffs periodically
 *
 * Read the documentation above BuffTypes or the wiki page for how to create
 * new buffs
 * */
public class BuffManager extends Component {
    /* Debugging */
    private static final Logger logger = LoggerFactory.getLogger(BuffManager.class);

    /* Improves readability when making time-based calls */
    private static final int SECONDS = 1000;

    /* Allows new buffs to be created from here */
    private MainGameScreen mainGame;

    /* Allows the BuffManager to check for collisions with buffs */
    private Entity player;

    /* Keep track of when the last buff was spawned */
    private long lastBuffSpawn;

    /**
     * Tracks the different buff types in the game. Add new buff types here.
     *
     * Buffs preceded by a B are buffs that aren't timed.
     * Buffs preceded by a D are debuffs that aren't timed.
     * Buffs preceded by a BT are buffs that are timed
     * Buffs preceded by a DT are debuffs that are timed.
     *
     * If a new buff is added, ensure you update:
     *  - getTexture() method: the filepath to the sprite for this new buff
     *    type (this file must have been loaded by the MainGameScreen prior)
     *  - selectBuffFunctionality() method: append the relevant switch
     *    statement to call the PlayerBuffs method which controls the behaviour
     *    of the buff
     *
     * When any new buff is added, the PlayerBuff class must have a method
     * added to control that buff
     *
     * If a new timed buff is added, the PlayerBuff class must have a method
     * added to control the removal of the effects of the buff
     *
     * If a new timed buff is added, the PlayerBuff's removeTimedBuff() method
     * must also be updated to call the removal function.
     *
     * If a new timed buff is added, the BuffInformation constructor must
     * also be updated with the buff's UI name and effect time.
     * */
    public enum BuffTypes {
        B_HP_UP, B_FULL_HEAL,
        D_HP_DOWN,
        BT_INVIN, BT_INF_SPRINT,
        DT_GIANT, DT_NO_JUMP, DT_DOUBLE_DMG
    }

    /* The buffs which are currently sitting on the map */
    private LinkedHashMap<Entity, BuffInformation> currentBuffs;

    /* The buffs which are currently applied to the player, on a timer */
    private LinkedHashMap<Entity, BuffInformation> timedBuffs;


    /**
     * Default constructor for the BuffManager. Sets up rudimentary
     * information.
     *
     * @param mainGame the MainGameScreen that the map is currently in.
     * @param currentMap the current map on which the game is playing.
     * */
    public BuffManager(MainGameScreen mainGame, GameArea currentMap) {
        this.mainGame = mainGame;
        this.currentBuffs = new LinkedHashMap<>();
        this.timedBuffs = new LinkedHashMap<>();
        this.player = ((ForestGameArea) currentMap).getPlayer();
    }

    /**
     * Returns the texture associated with a particular buff.
     *
     * This method must be updated when a new buff is added, otherwise a
     * default box_boy.png sprite will be used when the buff spawns.
     *
     * @param type the type of buff the texture is being returned for.
     * @return the filepath to the texture for this buff. This filepath
     *         must be in the MainGameScreen's buffsAndDebuffs String[], or
     *         otherwise loaded by the MainGameScreen, to work.
     * */
    public String getTexture(BuffTypes type) {
        switch (type) {
            case BT_INVIN:
                return "images/ghostKing.png";
            case DT_GIANT:
                return "images/winReplay.png";
            case B_HP_UP:
                return "images/winMainMenu.png";
            case D_HP_DOWN:
                return "images/winContinue.png";
            case DT_NO_JUMP:
                return "images/box_boy.png";
            case BT_INF_SPRINT:
                return "images/box_boy.png";
            case DT_DOUBLE_DMG:
                return "images/pauseRestart.png";
            case B_FULL_HEAL:
                return "images/heart.png";
        }
        return "images/box_boy.png"; // Default behaviour
    }

    /**
     * Controls which action functions to call upon the player colliding with
     * a buff.
     *
     * This method must be updated when a new buff is added, otherwise the
     * buff will not have any functionality when collided with.
     *
     * @param type the type of buff that was hit
     * @param buff the buff which was hit
     * @param buffInfo information about the buff which was hit
     * */
    private void selectBuffFunctionality(BuffTypes type, Entity buff,
            BuffInformation buffInfo) {

        /* PlayerBuffs functions to call when there is a new instant buff */
        switch (type) {
            case B_HP_UP:
                PlayerBuffs.increasePlayerHP(this.player);
                return;
            case D_HP_DOWN:
                PlayerBuffs.reducePlayerHP(this.player);
                return;
            case B_FULL_HEAL:
                PlayerBuffs.setHealthFull(this.player);
                return;
        }

        // Timed buff stacking is not supported.
        for (BuffInformation currentTimed : this.timedBuffs.values()) {
            if (currentTimed.getType() == buffInfo.getType()) {
                logger.info("A buff of this type is already applied! Ignoring...");
                return;
            }
        }

        /* PlayerBuffs functions to call when there is a new time-based buff */
        switch (type) {
            case DT_GIANT:
                PlayerBuffs.makePlayerGiant(this.player);
                break;
            case BT_INVIN:
                PlayerBuffs.applyInvincibility(this.player);
                break;
            case DT_NO_JUMP:
                PlayerBuffs.noJumping(this.player);
                break;
            case BT_INF_SPRINT:
                PlayerBuffs.setInfiniteStamina(this.player);
                break;
            case DT_DOUBLE_DMG:
                PlayerBuffs.setDoubleHurt(this.player);
                break;
        }

        logger.info("Setting up new timed buff...");
        setupTimedBuff(buff, buffInfo);
    }

    /**
     * Sets up listener to take action when the player collides with an object.
     * */
    @Override
    public void create() {
        super.create();
        player.getEvents().addListener("collisionStart",
                this::onCollision);
    }

    /**
     * Activates when the player collides with something. Checks if that
     * 'something' was a buff or debuff, and if so, applies the effects to
     * the player.
     *
     * @param player the player the object collided with
     * @param object the object (possibly a buff) that the player collided with
     * */
    public void onCollision(Fixture player, Fixture object) {
        // logger.info("Player has collided with something!");
        Entity buff = null;
        BuffTypes type = null;
        BuffInformation buffInfo = null;

        /* Check if the player hit a buff */
        for (Entity current : this.currentBuffs.keySet()) {
            if (current.getComponent(ColliderComponent.class).getFixture()
                    == object) {
                buffInfo = this.currentBuffs.get(current);
                buff = current;
                type = buffInfo.getType();
            }
        }

        /* Apply the effects of the buff, if one was hit. */
        if (type != null) {
            logger.info("Player has hit a buff!");
            selectBuffFunctionality(type, buff, buffInfo);

            // Remove the buff from the map
            this.currentBuffs.remove(buff);
            removeBuff(buff);
        }
    }

    /**
     * Helper method to set up timed buffs. The timed buff is added to the
     * list of currently active timed buffs.
     *
     * @param buff the buff that is being applied
     * @param buffInfo information about the buff being applied, so that
     *                 timedBuffs may check the timeout later
     * */
    private void setupTimedBuff(Entity buff, BuffInformation buffInfo) {
        this.timedBuffs.put(buff, buffInfo);
        buffInfo.setTimeApplied(ServiceLocator.getTimeSource().getTime());
        this.player.getComponent(PlayerStatsDisplay.class).updateBuffDisplay(this.timedBuffs.values());
    }

    /**
     * Returns whether or not a spawned buff has timed out (ie, should be
     * removed from the map as the player did not pick it up in time).
     *
     * Currently, buffs time out after 10 seconds.
     *
     * @param buff the buff we are checking the timeout of
     * @return 'True' if the buff has been sitting on the map for 10 or more
     *         seconds, and needs to be despawned. 'False' if not.
     * */
    public boolean hasTimedOut(Entity buff) {
        long timeOfCreation = this.currentBuffs.get(buff).getTimeOfCreation();
        return (ServiceLocator.getTimeSource().getTimeSince(timeOfCreation) >= 10 * SECONDS);
    }

    @Override
    /**
     * Controls time-sensitive buff behaviour.
     *
     * - Checks if buffs on the map should be removed due to not being picked
     *   up in time by the player, and removes them.
     * - Checks if there are buff effects currently applied to the player that
     *   have timed out, and removes them.
     * - Checks if a new buff should be placed onto the map, based on when the
     *   most recent one was placed, and places one.
     * */
    public void update() {
        /* Determine if stationary buffs should be removed from the map */
        List<Entity> timedOutBuffs = new ArrayList<>();
        for (Entity buff : this.currentBuffs.keySet()) {
            if (hasTimedOut(buff)) {
                // No longer on the map
                timedOutBuffs.add(buff);

                // Remove the buff from the map
                removeBuff(buff);
            }
        }

        for (Entity buff : timedOutBuffs) {
            this.currentBuffs.remove(buff);
        }

        /* Check if any timed buff effects on the player have timed out */
        List<Entity> effectTimeOutBuffs = new ArrayList<>();

        for (Entity buff : this.timedBuffs.keySet()) {
            BuffInformation buffInfo = this.timedBuffs.get(buff);
            long timeOut = buffInfo.getEffectTimeOut();
            long applied = buffInfo.getTimeApplied();

            if (ServiceLocator.getTimeSource().getTimeSince(applied) >= timeOut) {
                // The buff should have timed out by now
                logger.info("A timed buff has timed out!");
                PlayerBuffs.removeTimedBuff(buffInfo, this.player);

                // Remove it from the active timed buffs
                effectTimeOutBuffs.add(buff);
            }
        }

        for (Entity buff : effectTimeOutBuffs) {
            this.timedBuffs.remove(buff);
        }

        // Update the timed buffs UI to reflect any changes
        this.player.getComponent(PlayerStatsDisplay.class).updateBuffDisplay(this.timedBuffs.values());

        /* Check if a new buff should be spawned. Buffs spawn every 3 seconds. */
        if (ServiceLocator.getTimeSource().getTimeSince(lastBuffSpawn) >= 3 * SECONDS) {
            addNewBuff();
        }
    }

    /**
     * Spawns a new, random buff and registers it with the BuffManager
     * */
    private void addNewBuff() {
        mainGame.getCurrentMap().spawnBuffDebuff(this);
    }


    /**
     * Queues a buff to be destroyed and unregistered on the next world step.
     *
     * @param buff the buff to dispose of.
     * */
    private void removeBuff(Entity buff) {
        buff.getComponent(PhysicsComponent.class).getPhysics().addToDestroy(buff);
    }

    /**
     * Registers a newly-created buff with this BuffManager. Also updates the
     * lastBuffSpawn time to the time this new buff was added.
     *
     * @param info information about the buff which is used to define the
     *             actions to take when a player collides with it
     * */
    public void registerBuff(BuffInformation info) {
        this.currentBuffs.put(info.getBuff(), info);
        this.lastBuffSpawn = info.getTimeOfCreation();
    }
}
