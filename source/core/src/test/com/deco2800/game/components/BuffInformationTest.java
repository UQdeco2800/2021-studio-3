package com.deco2800.game.components;

import com.deco2800.game.components.maingame.BuffManager;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class BuffInformationTest {
    @Mock BuffInformation buffInfo;
    @Mock Entity buff;
    @Mock GameTime time;

    /**
     * Test that the Double Hurt debuff is setup properly
     * */
    @Test
    void testDoubleHurt() {
        buffInfo = new BuffInformation(buff, BuffManager.BuffTypes.DT_DOUBLE_DMG, 0);
        assertEquals("Double Hurt!", buffInfo.getBuffName());
        assertEquals( 5000, buffInfo.getEffectTimeOut());
        assertEquals(buff, buffInfo.getBuff());
        assertEquals(0, buffInfo.getTimeOfCreation());
        assertEquals(BuffManager.BuffTypes.DT_DOUBLE_DMG, buffInfo.getType());
    }

    /**
     * Test that the invincibility buff is setup properly
     * */
    @Test
    void testInvincibility() {
        buffInfo = new BuffInformation(buff, BuffManager.BuffTypes.BT_INVIN, 0);
        assertEquals("Invincibility!", buffInfo.getBuffName());
        assertEquals( 5000, buffInfo.getEffectTimeOut());
        assertEquals(buff, buffInfo.getBuff());
        assertEquals(0, buffInfo.getTimeOfCreation());
        assertEquals(BuffManager.BuffTypes.BT_INVIN, buffInfo.getType());
    }

    /**
     * Test that the infinite sprint buff is setup properly
     * */
    @Test
    void testInfiniteSprint() {
        buffInfo = new BuffInformation(buff, BuffManager.BuffTypes.BT_INF_SPRINT, 0);
        assertEquals("Infinite Sprint!", buffInfo.getBuffName());
        assertEquals( 5000, buffInfo.getEffectTimeOut());
        assertEquals(buff, buffInfo.getBuff());
        assertEquals(0, buffInfo.getTimeOfCreation());
        assertEquals(BuffManager.BuffTypes.BT_INF_SPRINT, buffInfo.getType());
    }


    /**
     * Test that setting an effect timeout works
     * */
    @Test
    void testSetTimeout() {
        buffInfo = new BuffInformation(buff, BuffManager.BuffTypes.BT_INVIN, 0);
        assertEquals( 5000, buffInfo.getEffectTimeOut());
        buffInfo.setEffectTimeout(10000);
        assertEquals( 10000, buffInfo.getEffectTimeOut());
    }

    /**
     * Test that increasing an effect timeout works
     * */
    @Test
    void testIncreaseTimeout() {
        buffInfo = new BuffInformation(buff, BuffManager.BuffTypes.BT_INVIN, 0);
        assertEquals( 5000, buffInfo.getEffectTimeOut());
        buffInfo.increaseTimeout(5000);
        assertEquals( 10000, buffInfo.getEffectTimeOut());
    }


    /**
     * Test that setting a time applied works
     * */
    @Test
    void testSetTimeApplied() {
        buffInfo = new BuffInformation(buff, BuffManager.BuffTypes.BT_INVIN, 0);
        buffInfo.setTimeApplied(10);
        assertEquals( 10, buffInfo.getTimeApplied());
    }

    /**
     * Test that getting the time remaining on the buff works
     * */
    @Test
    void testTimeRemaining() {
        buffInfo = new BuffInformation(buff, BuffManager.BuffTypes.BT_INVIN, 0);
        ServiceLocator.registerTimeSource(time);

        // this buff lasts 5 seconds, and is applied 9 seconds from game start
        buffInfo.setEffectTimeout(5000);
        buffInfo.setTimeApplied(9000);

        // set the time to 10 seconds since game start
        when(time.getTime()).thenReturn((long) 10000);

        // buff should have 4 seconds left
        assertEquals(4000, buffInfo.getTimeLeft());
    }
}