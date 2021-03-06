package com.deco2800.game.components;

/**
 * Component used to handle player lives. Handles adding and subtracting lives,
 * and is initialised in the ForestGameArea class.
 */
public class LivesComponent extends Component {

    /**Variable for player lives */
    private int lives;

    /**
     * Constructor for LivesComponent class. Takes in the number of lives the player has
     * @param lives player lives
     */
    public LivesComponent(int lives) {
            this.lives = lives;
    }

    /**
     * Getter method for the lives variable
     * @return player lives
     */
    public int getLives() {
        return lives;
    }


    /**
     * Sets the number of lives the player has and updates the UI as required
     * if a player exists
     * @param lives new number of lives left.
     */
    public void setLives(int lives) {
        if (lives >= 0) {
            this.lives = lives;
        } else {
            this.lives = 3;
        }
        if (entity != null) {
            entity.getEvents().trigger("updateLives", lives);
        }
    }

    /**
     * Method adds lives to the player's total.
     * @param lives lives to add to the players total.
     */
    public void addLives(int lives) {
        setLives(this.lives += lives);
    }

}
