package com.deco2800.game.areas;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

//private method tests



@ExtendWith(GameExtension.class)
class GameAreaTest {
  @Mock ResourceService resourceService;
  @Mock TiledMap tiledMap;


  @Test
  void shouldSpawnEntities() {
    TerrainFactory factory = mock(TerrainFactory.class);

    GameArea gameArea =
        new GameArea() {
          @Override
          public void create() {}
        };

    ServiceLocator.registerEntityService(new EntityService());
    Entity entity = mock(Entity.class);

    gameArea.spawnEntity(entity);
    verify(entity).create();

    gameArea.dispose();
    verify(entity).dispose();
  }

//    @Test
//    void shouldCreate() {
//      TerrainComponent mock = org.mockito.Mockito.mock(TerrainComponent.class);
//      ServiceLocator.registerResourceService(resourceService);
//      ServiceLocator.registerEntityService(new EntityService());
//      ServiceLocator.registerRenderService(new RenderService());
//      ServiceLocator.getRenderService().setStage(mock(Stage.class));
//      TerrainComponent terrainComponent = makeComponent(TerrainComponent.TerrainOrientation.ORTHOGONAL, 3f);
//      TerrainFactory terrainFactory = mock(TerrainFactory.class);
//      when(terrainFactory.createTerrain(TerrainFactory.TerrainType.SIDE_SCROLL_ER))
//              .thenReturn(terrainComponent);
//      when(mock.getMapBounds(0)).thenReturn(new GridPoint2(30,30));
//      ForestGameArea forestGameArea = new ForestGameArea(terrainFactory, 0, false);
//
//      forestGameArea.create();
//
//    }
//
//  private static TerrainComponent makeComponent(TerrainComponent.TerrainOrientation orientation, float tileSize) {
//    OrthographicCamera camera = mock(OrthographicCamera.class);
//    TiledMap map = mock(TiledMap.class);
//    TiledMapRenderer mapRenderer = mock(TiledMapRenderer.class);
//    return new TerrainComponent(camera, map, mapRenderer, orientation, tileSize);
//  }

}
