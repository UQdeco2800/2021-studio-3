package com.deco2800.game.components.maingame;

import com.badlogic.gdx.audio.Sound;
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
 * - Keeps track of the buffs currently spawned on the map
 * - Keeps track of the time-based buffs which are currently affecting the
 *   player.
 * - Handles despawning timed-out buffs.
 * - Handles removing timed-out buffs' effects from the player when their
 *   timer is up.
 * - Handles spawning new buffs periodically
 *
 * The documentation above BuffTypes and the wiki page gives information on how
 * to create new buffs
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

    /* The current map */
    private ForestGameArea map;

    /* Keep track of when the last buff was spawned */
    private long lastBuffSpawn;

    /* The floating animation that is released */
    private Entity buffPickup;

    /* Creation time of floating animation when player picks up a buff */
    private long pickupCreationTime;

    /* Buff sound */
    private static final String BUFF_SOUND_PATH = "sounds/buff.mp3";

    /* Debuff sound */
    private static final String DEBUFF_SOUND_PATH = "sounds/debuff.mp3";

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
     *    type (this file must have been loaded by the LoadingScreen)
     *  - selectBuffFunctionality() method: append the relevant switch
     *    statement to call the PlayerBuffs method which controls the behaviour
     *    of the buff
     *  - getBuffSound() method: add the buff or debuff into the buff / debuff
     *    switch statement list so that the correct sound is applied upon
     *    pickup.
     *
     * When any new buff is added, the PlayerBuff class must have a method
     * added to define the behaviour of the buff.
     *
     * If a new timed buff is added, the PlayerBuff class must have a method
     * added to control the removal of the effects of the buff. The
     * PlayerBuffs' removeTimedBuff() method must be updated to call the
     * removal function.
     *
     * If a new timed buff is added, the BuffInformation constructor must
     * also be updated with the buff's UI name and effect time.
     * */
    public enum BuffTypes {
        B_HP_UP, B_FULL_HEAL,
        D_HP_DOWN,
        BT_INVIN, BT_INF_SPRINT,
        DT_DOUBLE_DMG
    }

    public enum BuffPickup {
        positive, negative
    }

    /* The buffs which are currently sitting on the map */
    private LinkedHashMap<Entity, BuffInformation> currentBuffs;

    /* The buffs which are currently applied to the player, on a timer */
    private LinkedHashMap<Entity, BuffInformation> timedBuffs;

    /* A list of the possible buff pick up animations.
    * The list contains two integers, the first is whether the buff has been picked up.
    * The second is whether the animation has appeared on screen.
    * 0 represents false, 1 represents true. */
    private LinkedHashMap<BuffPickup, List<Integer>> buffPickups;

    /**
     * Default constructor for the BuffManager. Sets up rudimentary
     * information.
     *
     * @param mainGame the MainGameScreen that the map is currently in.
     * @param currentMap the current map on which the game is playing.
     * */
    public BuffManager(MainGameScreen mainGame, GameArea currentMap) {
        this.mainGame = mainGame;
        this.map = (ForestGameArea) currentMap;
        this.currentBuffs = new LinkedHashMap<>();
        this.timedBuffs = new LinkedHashMap<>();
        this.buffPickups = new LinkedHashMap<>();
        this.player = ((ForestGameArea) currentMap).getPlayer();
        this.buffPickup = new Entity();
        this.buffPickups.put(BuffPickup.positive, Arrays.asList(0, 0));
        this.buffPickups.put(BuffPickup.negative, Arrays.asList(0, 0));
    }

    /**
     * Returns the texture associated with a particular buff.
     *
     * This method must be updated when a new buff is added, otherwise a
     * default box_boy.png sprite will be used when the buff spawns.
     *
     * @param type the type of buff the texture is being returned for.
     * @return the filepath to the texture for this buff. This filepath
     *         must be in the LoadingScreen's buffsAndDebuffs String[], or
     *         otherwise loaded by the game to work.
     * */
    public String getTexture(BuffTypes type) {
        switch (type) {
            case BT_INVIN:
                return "images/Invincibility.png";
            case B_HP_UP:
                return "images/oxygen .png";
            case D_HP_DOWN:
                return "images/decreaseHealth.png";
            case BT_INF_SPRINT:
                return "images/sprint (1).png";
            case DT_DOUBLE_DMG:
                return "images/Double Hurt.png";
            case B_FULL_HEAL:
                return "images/heart.png";
        }
        return "images/box_boy.png"; // Default behaviour
    }

    /**
     * Returns the texture associated with a particular pickup
     *
     * @param pickup the type of pickup the player hit
     * @return the filepath to the texture for this pickup. This filepath must
     *         be in the LoadingScreens' buffsAndDebuffs String[], or otherwise
     *         loaded by game to work.
     * */
    public String getPickupTexture(BuffPickup pickup) {
        switch (pickup) {
            case positive:
                return "images/increase20Pickup.png";
            case negative:
                return "images/decrease20Pickup.png";
        }
        return "images/heart.png"; // Default behaviour
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
    public void selectBuffFunctionality(BuffTypes type, Entity buff,
            BuffInformation buffInfo) {
        /* PlayerBuffs functions to call when there is a new instant buff */
        switch (type) {
            case B_HP_UP:
                this.buffPickups.get(BuffPickup.positive).set(0, 1);
                PlayerBuffs.increasePlayerHP(this.player);
                return;
            case D_HP_DOWN:
                this.buffPickups.get(BuffPickup.negative).set(0, 1);
                PlayerBuffs.reducePlayerHP(this.player);
                return;
            case B_FULL_HEAL:
                PlayerBuffs.setHealthFull(this.player);
                return;
        }

        /* Stack buffs if the same timed buff is collided with */
        for (BuffInformation currentTimed : this.timedBuffs.values()) {
            if (currentTimed.getType() == buffInfo.getType()) {
                /* Increase the duration of the buff */
                currentTimed.increaseTimeout(buffInfo.getEffectTimeOut());
                return;
            }
        }

        /* PlayerBuffs functions to call when there is a new time-based buff */
        switch (type) {
            case BT_INVIN:
                PlayerBuffs.applyInvincibility(this.player);
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

            // Play the sound for the buff
            playBuffSound(type);
        }
    }

    /**
     * Returns the filepath for the buff or debuff sound associated with a
     * particular buff. By default, there are two sounds: one for buffs, one
     * for debuffs.
     *
     * @param type the type of buff that was picked up.
     * @return the filepath for the sound to be played.
     * */
    private String getBuffSound(BuffTypes type) {
        switch (type) {
            case B_FULL_HEAL:
            case B_HP_UP:
            case BT_INF_SPRINT:
            case BT_INVIN:
                return BUFF_SOUND_PATH;
            case D_HP_DOWN:
            case DT_DOUBLE_DMG:
                return DEBUFF_SOUND_PATH;
        }
        return BUFF_SOUND_PATH; // default
    }

    /**
     * Plays the buff or debuff tune when one is picked up by the player.
     *
     * @param type the type of buff that was picked up. The tune that is played
     *             upon pickup will depend on whether a buff or debuff was
     *             retrieved.
     * */
    private void playBuffSound(BuffTypes type) {
        String soundPath = getBuffSound(type);
        Sound sound = ServiceLocator.getResourceService().getAsset(soundPath, Sound.class);
        sound.setVolume(0,0.5f);
        sound.play();
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

    /**
     * Handles the float animation after a player hits a HP instant buff/debuff.
     * If the player collides with the buff, a floating animation is spawned onto the map.
     * Once it's been on the map for 1.5 seconds, it is removed.
     */
    private void spawnPickup() {
        for (BuffPickup pickup: this.buffPickups.keySet()) {
            if (this.buffPickups.get(pickup).get(0) == 1) {
                pickupCreationTime = ServiceLocator.getTimeSource().getTime();
                buffPickup = mainGame.getCurrentMap().spawnBuffDebuffPickup(pickup, this);
                this.buffPickups.get(pickup).set(0, 0);
                this.buffPickups.get(pickup).set(1, 1);
            }
            if (this.buffPickups.get(pickup).get(1) == 1) {
                if (ServiceLocator.getTimeSource().getTimeSince(pickupCreationTime) >= 1 * SECONDS) {
                    removeBuff(buffPickup);
                    this.buffPickups.get(pickup).set(1, 0);
                }
            }
        }
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
        /* Handle spawning pickups when the player hits an instant buff */
        spawnPickup();

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
        mainGame.getCurrentMap().spawnBuffDebuff(this,
                this.map.getAreaType());
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

    /**
     * Returns the buffs which are currently affecting the player
     * */
    public Collection<BuffInformation> getTimedBuffs(){
        return this.timedBuffs.values();
    }

    /**
     * Returns the buffs which are currently sitting on the map.
     * */
    public Map<Entity, BuffInformation> getCurrentBuffs() {
        return this.currentBuffs;
    }

    /**
     * Disposes of all of the buffs currently sitting on the map
     * */
    public void disposeAll() {
        for (Entity buff : this.currentBuffs.keySet()) {
            removeBuff(buff);
        }
        // Reset the current buffs
        this.currentBuffs = new LinkedHashMap<>();
    }

}
