package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.maingame.BuffManager;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.ObstacleConfig;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class ObstacleFactoryTest {
    @Mock ResourceService resourceService;
    private static final String[] forestTextureAtlases = {
            "images/SerpentLevel1.atlas"
    };

    /**
     * Checking if death wall can be created
     */
    @Test
    void checkDeathWall() {
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.getResourceService().loadTextureAtlases(forestTextureAtlases);
        TextureAtlas atlas = createMockAtlas("Serpent1.1", 1);

        when(ServiceLocator.getResourceService().getAsset("images/SerpentLevel1.atlas",
                TextureAtlas.class)).thenReturn(atlas);

        ObstacleFactory.createDeathWall(new Vector2(6f, 6f));
        verify(resourceService).getAsset("images/SerpentLevel1.atlas",
                TextureAtlas.class);
    }


    static TextureAtlas createMockAtlas(String animationName, int numRegions) {
        TextureAtlas atlas = mock(TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = new Array<>(numRegions);
        for (int i = 0; i < numRegions; i++) {
            regions.add(mock(TextureAtlas.AtlasRegion.class));
        }
        when(atlas.findRegions(animationName)).thenReturn(regions);
        return atlas;
    }
}
