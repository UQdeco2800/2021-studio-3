package com.deco2800.game.components;

import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.physics.components.HitboxComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Component used to store information related to the player's information sending.
 */
public class InformPlayerComponent extends Component {

    /**variable used for the users informer*/
    private String information;
    private Map<Integer, String> triggers;
    private float position;
    /**
     * Constructor class for the InformPlayer Component.
     */
    public InformPlayerComponent(Map<Integer, String> triggers, float position) {
        this.information = "";
        this.triggers = triggers;
        this.position = position;
    }

    public InformPlayerComponent() {
        this.information = "";
        this.position = 0;
        this.triggers = new HashMap<>();
    }
    @Override
    public void create() {

    }

    /**
     * Set's position
     *
     * @param position position to set; if the position is greater than 0 or
     *                  the current position, it will be updated, otherwise it will be reset to 0
     *                 if the position is 0. Will not change otherwise
     */
    public void setPosition(float position) {
        this.position = position;
        int temp = (int) this.position;
        if (this.triggers.get(temp) != null) {
            boolean isKeyPresent = this.triggers.containsKey(temp);
            if (isKeyPresent) {
                setInformation(triggers.get(temp));
            }
        }

    }

    /**
     * Set's triggers
     *
     * @param triggers position to set; if the position is greater than 0 or
     *                  the current position, it will be updated, otherwise it will be reset to 0
     *                 if the position is 0. Will not change otherwise
     */
    public void setTriggers(Map<Integer, String> triggers) {
        this.triggers = triggers;
    }
    /**
     * Getter method for the text sent to player.
     * @return the text sent to player
     */
    public String getText() {
        return information;
    }

    /**
     * Set's the new text on the UI once the updateInformation event has been triggered. This occurs
     * only if the entity has been created (i.e. it is not null)
     */
    public void setInformation(String information) {
        this.information = information;
        if (entity != null) {
            entity.getEvents().trigger("updateInformation", this.information);
        }
    }


}
