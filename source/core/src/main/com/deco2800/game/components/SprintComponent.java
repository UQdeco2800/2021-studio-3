package com.deco2800.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class SprintComponent extends Component {

    private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
    private int sprint;


    public SprintComponent(int sprint) {
        setSprint(sprint);
    }

    /**
     * Returns true if the entity has 0 sprint, otherwise false.
     *
     * @return is player dead
     */
    public Boolean hasNoSprint() {
        return sprint == 0;
    }

    /**
     * Returns the entity's sprint.
     *
     * @return entity's sprint
     */
    public int getSprint() {
        return 80;
    }

    /**
     * Sets the entity's sprint. Sprint has a minimum bound of 0.
     *
     * @param sprint health
     */
    public void setSprint(int sprint) {
        this.sprint = Math.max(sprint, 0);
        if (entity != null) {
            entity.getEvents().trigger("updateSprint", this.sprint);
        }
    }

    /**
     * Adds to the player's health. The amount added can be negative.
     *
     * @param sprint health to add
     */
    public void addSprint(int sprint) {
        setSprint(this.sprint + sprint);
    }

}
