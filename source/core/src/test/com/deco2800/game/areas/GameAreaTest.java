package com.deco2800.game.areas;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

//private method tests
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(GameExtension.class)
class GameAreaTest {
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

    /**
     * A test to test the create method in ForestGameArea class
     */
  @Test
    void test() {
      ServiceLocator.registerResourceService(new ResourceService());
      ServiceLocator.registerEntityService(new EntityService());
      ServiceLocator.registerInputService(new InputService());
      ServiceLocator.registerPhysicsService(new PhysicsService());
      ServiceLocator.registerRenderService(new RenderService());
      ServiceLocator.registerTimeSource(new GameTime());
      ForestGameArea gameArea = new ForestGameArea(mock(TerrainFactory.class), 0, false);
      gameArea.create();

      verify(gameArea).create();

  }
}
