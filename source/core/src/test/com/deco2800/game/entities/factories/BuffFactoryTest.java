package com.deco2800.game.entities.factories;

import com.deco2800.game.components.maingame.BuffManager;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class BuffFactoryTest {
    @Mock BuffManager manager;
    @Mock ResourceService resourceService;
    private String[] textures = {"images/ghostKing.png", "images/heart.png"};

    /**
     * Test that the construction of buffs interacts with the BuffManager
     * */
    @Test
    void checkCreateBuff() {
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerTimeSource(new GameTime());

        // Setup the textures
        ServiceLocator.getResourceService().loadTextures(textures);
        when(manager.getTexture(BuffManager.BuffTypes.BT_INVIN)).thenReturn("images/ghostKing.png");

        // Create the buff
        BuffFactory.createBuff(BuffManager.BuffTypes.BT_INVIN, manager);

        /* Ensure the texture was called upon */
        verify(manager).getTexture(BuffManager.BuffTypes.BT_INVIN);
    }

    @Test
    void checkCreateBuffPickup() {
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerTimeSource(new GameTime());

        ServiceLocator.getResourceService().loadTextures(textures);
        when(manager.getPickupTexture(BuffManager.BuffPickup.positive)).thenReturn("images/heart");

        BuffFactory.createBuffAnimation(BuffManager.BuffPickup.positive, manager);

        verify(manager).getPickupTexture(BuffManager.BuffPickup.positive);
    }
}