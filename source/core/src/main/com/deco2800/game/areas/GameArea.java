package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.*;
import com.deco2800.game.components.maingame.BuffManager;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.BuffFactory;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents an area in the game, such as a level, indoor area, etc. An area has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>Support for enabling/disabling game areas could be added by making this a Component instead.
 */
public abstract class GameArea implements Disposable {
  protected TerrainComponent terrain;
  protected List<Entity> areaEntities;
  protected GameArea() {
    areaEntities = new ArrayList<>();
  }
  protected Entity player;

  /** Create the game area in the world. */
  public abstract void create();

  /** Dispose of all internal entities in the area */
  public void dispose() {
    for (Entity entity : areaEntities) {
      entity.dispose();
    }

    if (player != null) {
      player.dispose();
    }
  }

  /**
   * Spawn entity at its current position
   *
   * @param entity Entity (not yet registered)
   */
  public void spawnEntity(Entity entity) {
    areaEntities.add(entity);
    ServiceLocator.getEntityService().register(entity);
  }

  /**
   * Spawn entity on a given tile. Requires the terrain to be set first.
   *
   * @param entity Entity (not yet registered)
   * @param tilePos tile position to spawn at
   * @param centerX true to center entity X on the tile, false to align the bottom left corner
   * @param centerY true to center entity Y on the tile, false to align the bottom left corner
   */
  protected void spawnEntityAt(
      Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
    Vector2 worldPos = terrain.tileToWorldPosition(tilePos);
    float tileSize = terrain.getTileSize();

    if (centerX) {
      worldPos.x += (tileSize / 2) - entity.getCenterPosition().x;
    }
    if (centerY) {
      worldPos.y += (tileSize / 2) - entity.getCenterPosition().y;
    }

    entity.setPosition(worldPos);
    spawnEntity(entity);
  }

  protected void loadSave(Entity player, String saveState) {
    int x = 18, y = 12;
    try(BufferedReader br = new BufferedReader(new FileReader(saveState))) {
      String line = br.readLine();
      // parse file to load the floor
      while (line != null) {
        String[] values = line.split(":");
        switch (values[0]) {
          case "SCORE":
            player.getComponent(ScoreComponent.class).setScore(Integer.parseInt(values[1]));
          case "LIVES":
            player.getComponent(LivesComponent.class).setLives(Integer.parseInt(values[1]));
          case "HEALTH":
            player.getComponent(CombatStatsComponent.class).setHealth(Integer.parseInt(values[1]));
          case "SPRINT":
            player.getComponent(SprintComponent.class).setSprint(Integer.parseInt(values[1]));
          case "X":
            x = Integer.parseInt(values[1]);
          case "Y":
            y = Integer.parseInt(values[1]);
        }
        line = br.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    GridPoint2 spawnPoint = new GridPoint2(x, y);
    spawnEntityAt(player, spawnPoint, true, true);
    player.getComponent(ProgressComponent.class).setProgress();
  }
  /**
   * Spawn entity at a position vector.
   * @param entity Entity (not yet registered)
   * @param pos Vector position to spawn at
   */
  protected void spawnEntityAtVector(Entity entity, Vector2 pos) {
    entity.setPosition(pos);
    spawnEntity(entity);
  }
}
