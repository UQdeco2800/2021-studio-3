package com.deco2800.game.components.player;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.assets.AssetManager;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(GameExtension.class)
class playerHpImageTest {
    @Test
    void shouldLoadImage() {
        AssetManager manager= PlayerFactory.load();
        List<String> hpList = Arrays.asList("00","10","20","30","40","50","60","70","80",
                "90", "100");
        for(String string : hpList){
            assertTrue(manager.isLoaded("images/" + string + ".png"));
        }
    }
}
