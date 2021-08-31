package com.deco2800.game.components.maingame;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)

class PopupUIHandlerTest {

    /**
     * Test that the constructor correctly populates the background and
     * buttons variables
     * */
    @Test
    void constructorTest() {
        // Valid input
        String[] textures = {"background", "button1", "button2"};
        PopupUIHandler handler = new PopupUIHandler(textures);

        assertEquals(handler.getBackground(), "background");
        assertEquals(handler.getButtons()[1], "button1");
        assertEquals(handler.getButtons()[2], "button2");

        // No buttons
        String[] textures2 = {"background"};
        PopupUIHandler handler2 = new PopupUIHandler(textures2);
        assertEquals(handler2.getBackground(), "background");
        assertArrayEquals(handler2.getButtons(), new String[textures2.length]);
    }
}