package com.deco2800.game.components;

import com.deco2800.game.components.maingame.BuffManager;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;

/**
 * Stores information about each buff and debuff created. Each buff / debuff will
 * have a BuffInformation attached to them when they are created. This class
 * works with the BuffManager to keep track of the information about current
 * buffs in the game.
 * */
public class BuffInformation extends Component {
    /* Improves readability when defining timed buff's effect timeout */
    private static final int SECONDS = 1000;

    /* The Buff entity */
    private Entity buff;

    /* The type of this buff */
    private BuffManager.BuffTypes type;

    /* The time this buff was created */
    private long timeOfCreation;

    /* How this buff is displayed on the UI */
    private String buffName;

    // Timed buff attributes
    /* The absolute time this buff should affect the player for */
    private long effectTimeOut;

    /* When the buff got applied to the player */
    private long timeApplied;


    /**
     * Constructor for BuffInformation objects. Sets up the rudimentary
     * information about the buff or debuff. For timed buffs, their unique UI
     * display name is also defined, along with their timeout.
     *
     * @param buff the buff entity object
     * @param type the type of this buff
     * @param creationTime the absolute time (in milliseconds, from game start)
     *                     that this buff was created.
     * */
    public BuffInformation(Entity buff, BuffManager.BuffTypes type,
            long creationTime) {
        this.buff = buff;
        this.type = type;
        this.timeOfCreation = creationTime;

        /* Set the UI title and time of effect for the timed buff. */
        switch (type) {
            case DT_DOUBLE_DMG:
                this.buffName = "Double Hurt!";
                setEffectTimeout(5 * SECONDS);
                break;
            case BT_INVIN:
                this.buffName = "Invincibility!";
                setEffectTimeout(5 * SECONDS);
                break;
            case BT_INF_SPRINT:
                this.buffName = "Infinite Sprint!";
                this.setEffectTimeout(5 * SECONDS);
                break;
        }
    }

    /**
     * Returns the string representation of this buff to be displayed on the UI
     * */
    public String getBuffName() {
        return this.buffName;
    }

    /**
     * Returns the buff Entity pertaining to this BuffInformation object.
     * */
    public Entity getBuff() {
        return this.buff;
    }

    /**
     * Returns the type of this buff
     * */
    public BuffManager.BuffTypes getType() {
        return this.type;
    }

    /**
     * Returns the time that this buff was created, ie, placed onto the game
     * map. This time is given as an absolute time since the game start, in
     * milliseconds.
     * */
    public long getTimeOfCreation() {
        return this.timeOfCreation;
    }

    /**
     * Sets how long the effects of a buff should last on a player.
     *
     * @param time the absolute amount of time (in milliseconds) that the buff
     *             effects should last
     * */
    public void setEffectTimeout(long time) {
        this.effectTimeOut = time;
    }

    /**
     * Returns the absolute amount of time that the effects of a buff should
     * last on the player (in milliseconds).
     * */
    public long getEffectTimeOut() {
        return this.effectTimeOut;
    }


    /**
     * Returns the time that a time-based buff was applied to a player. This
     * time is given as an absolute time since game start, in milliseconds.
     * */
    public long getTimeApplied() {
        return this.timeApplied;
    }

    /**
     * Sets the time that a time-based buff was applied to the player.
     *
     * @param time the absolute time (since game start) that this buff was
     *             applied to the player, in milliseconds.
     * */
    public void setTimeApplied(long time) {
        this.timeApplied = time;
    }

    /**
     * Returns the amount of time left before this buffs' effects will be
     * removed from the player.
     *
     * @return the relative time that this buff has left, in milliseconds.
     * */
    public long getTimeLeft() {
        /* Get the current time and how long the buff has been active */
        long currentTime = ServiceLocator.getTimeSource().getTime();
        long activeFor = currentTime - getTimeApplied();

        return getEffectTimeOut() - activeFor;
    }

    /**
     * Increases the amount of time that this buff's effects should last on the
     * player.
     *
     * @param extraTime how much longer the buff should last, in milliseconds.
     * */
    public void increaseTimeout(long extraTime) {
        setEffectTimeout(getEffectTimeOut() + extraTime);
    }
}
