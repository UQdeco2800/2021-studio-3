package com.deco2800.game.components;

import com.badlogic.gdx.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Component used to store information related to the players sprint
 */
public class SprintComponent extends Component {

    private static final Logger logger = LoggerFactory.getLogger(SprintComponent.class);

    /** The amount of sprint the player has */
    public int sprint;


    public SprintComponent(int sprint) {
        setSprint(sprint);
    }

    /**
     * Returns true if the entity has 0 sprint, otherwise false.
     *
     * @return true if player has no sprint, false otherwise
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
        return sprint;
    }

    /**
     * Sets the entity's sprint. Sprint has a minimum bound of 0.
     *
     * @param sprint sprint
     */
    public void setSprint(int sprint) {
        this.sprint = Math.max(sprint, 0);
        if (entity != null) {
            entity.getEvents().trigger("updateSprint", this.sprint);
        }
    }


    /**
     * removes sprint.
     *
     * @param sprint sprint to remove
     */
    public void removeSprint(int sprint) {
        setSprint(this.sprint - sprint);
    }

    /**
     * adds sprint
     * @param sprint sprint to add
     */
    public void addSprint(int sprint) {
        setSprint(this.sprint + sprint);
    }

}
