package com.deco2800.game.components;

import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.physics.components.HitboxComponent;

/**
 * Component used to store information related to the player's score.
 */
public class ScoreComponent extends Component {

    /**variable used for the users score*/
    private int score;
    private int timerTick;
    private long timer;
    private int health;
    private CombatStatsComponent combatStats;
    /**
     * Constructor class for the Score Component.
     */
    public ScoreComponent() {
        //health = combatStats.getHealth();
        score = 1000;
        timerTick = 1;
        timer = TimeUtils.nanoTime();
    }

    @Override
    public void create() {
        combatStats = entity.getComponent(CombatStatsComponent.class);
        health = combatStats.getHealth();
    }

    /**
     * Getter method for the player score.
     * @return the score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * Set's the new score on the UI once the updateScore event has been triggered. This occurs
     * only if the entity has been created (i.e. it is not null)
     */
    public void setScore(int score) {
        this.score = score;
        if (entity != null) {
            entity.getEvents().trigger("updateScore", this.score);
        }
    }


    /**
     *  function to determine the player's new score based on the players health and time passed.
     */
    public void updateScore() {
        if (TimeUtils.timeSinceNanos(timer) > 1000000000) {
            timer = TimeUtils.nanoTime();
            setScore(score - timerTick);
        }
        int healthDifference = 0;
        int currentHealth = combatStats.getHealth();
        if (currentHealth != health) {
            healthDifference = currentHealth - health;
            health = combatStats.getHealth();
        }
        int newScore = getScore() + (7 * healthDifference);

        newScore = newScore < 0 ? 0 : newScore;
        setScore(newScore);
    }


}
