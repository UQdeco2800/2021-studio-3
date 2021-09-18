package com.deco2800.game.components.maingame;

import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.BuffInformation;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class BuffManagerTest {
    @Mock MainGameScreen mainGame;
    @Mock ForestGameArea currentMap;
    @Mock BuffManager manager;
    @Mock BuffInformation buffInfo;
    @Mock Entity buff;

    @BeforeEach
    void setupServices() {
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerTimeSource(new GameTime());
    }

    @Test
    void registerBuffTest() {
        when(buffInfo.getBuff()).thenReturn(buff);
        manager = new BuffManager(mainGame, currentMap);

        // There are no buffs spawned
        assertEquals(0, manager.getCurrentBuffs().keySet().size());

        manager.registerBuff(buffInfo);

        // A new buff has been added
        assertEquals(1, manager.getCurrentBuffs().keySet().size());
        assertEquals(buffInfo, manager.getCurrentBuffs().get(buff));
    }

}