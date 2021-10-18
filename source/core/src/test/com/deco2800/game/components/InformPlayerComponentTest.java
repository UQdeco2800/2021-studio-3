package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InformPlayerComponentTest {

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void setPosition() {
        InformPlayerComponent test = new InformPlayerComponent();
        test.setPosition(1);
        assertEquals(1, test.getPosition());
    }

    @Test
    void setTriggers() {
        InformPlayerComponent test = new InformPlayerComponent();
        Map<Integer, String> triggers = new HashMap<>();
        triggers.put(1,"HI");
        test.setTriggers(triggers);
        assertEquals(triggers, test.getTriggers());
    }

    @Test
    public void getText() {
        InformPlayerComponent test = new InformPlayerComponent();
        Map<Integer, String> triggers = new HashMap<>();
        triggers.put(1,"HI");
        test.setTriggers(triggers);
        test.setPosition(2);
        test.setInformation("HI");
        assertEquals("HI", test.getText());
    }

    @Test
    public void setInformation() {
        InformPlayerComponent test = new InformPlayerComponent();
        test.setInformation("TEST");
        assertEquals("TEST", test.getText());
    }


}
