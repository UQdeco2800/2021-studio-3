package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private static final Logger logger = LoggerFactory.getLogger(TerrainFactory.class);
  private static final GridPoint2 MAP_SIZE = new GridPoint2(100, 30);

  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;

  /**
   * Create a terrain factory with Orthogonal orientation
   *
   * @param cameraComponent Camera to render terrains to. Must be orthographic.
   */
  public TerrainFactory(CameraComponent cameraComponent) {
    this(cameraComponent, TerrainOrientation.ORTHOGONAL);
  }

  /**
   * Create a terrain factory
   *
   * @param cameraComponent Camera to render terrains to. Must be orthographic.
   * @param orientation orientation to render terrain at
   */
  public TerrainFactory(CameraComponent cameraComponent, TerrainOrientation orientation) {
    this.camera = (OrthographicCamera) cameraComponent.getCamera();
    this.orientation = orientation;
  }

  /**
   * Create a terrain of the given type, using the orientation of the factory. This can be extended
   * to add additional game terrains.
   *
   * @param terrainType Terrain to create
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain(TerrainType terrainType) {
    ResourceService resourceService = ServiceLocator.getResourceService();
    switch (terrainType) {
      case SIDE_SCROLL_ER:
        TextureRegion surface = new TextureRegion(resourceService.getAsset("images/background_surface.png", Texture.class));
        TextureRegion underground = new TextureRegion(resourceService.getAsset("images/background_rock.png", Texture.class));
        TextureRegion sky = new TextureRegion(resourceService.getAsset("images/background_sky.png", Texture.class));
        TextureRegion star = new TextureRegion(resourceService.getAsset("images/background_star.png", Texture.class));
        return createSideScrollTerrain(0.5f, surface, underground, sky, star);
      case LEVEL_TWO_TERRAIN:
        TextureRegion mars_surface =
                new TextureRegion(resourceService.getAsset("images/background_mars_surface.png", Texture.class));
        TextureRegion mars_underground =
                new TextureRegion(resourceService.getAsset("images/background_mars_ground.png", Texture.class));
        TextureRegion mars_sky =
                new TextureRegion(resourceService.getAsset("images/background_mars.png", Texture.class));
        TextureRegion mars_star =
                new TextureRegion(resourceService.getAsset("images/background_mars_star.png", Texture.class));
        return createLevelTwoTerrain(0.5f, mars_surface, mars_underground, mars_sky, mars_star);
      case LEVEL_THREE_TERRAIN:
        TextureRegion europa_surface =
                new TextureRegion(resourceService.getAsset("images/background_europa_surface.png", Texture.class));
        TextureRegion europa_underground =
                new TextureRegion(resourceService.getAsset("images/background_europa_ground.png", Texture.class));
        TextureRegion europa_sky =
                new TextureRegion(resourceService.getAsset("images/background_europa.png", Texture.class));
        TextureRegion europa_star =
                new TextureRegion(resourceService.getAsset("images/background_europa_star.png", Texture.class));
        return createLevelThreeTerrain(0.5f, europa_surface, europa_underground, europa_sky, europa_star);
      case LEVEL_FOUR_TERRAIN:
        return createTerrain(TerrainType.LEVEL_THREE_TERRAIN); // Placeholder
      default:
        return null;
    }
  }

  //A new terrain with side sroll-er
  private TerrainComponent createSideScrollTerrain(
          float tileWorldSize, TextureRegion surface, TextureRegion underground, TextureRegion sky, TextureRegion tree) {
    GridPoint2 tilePixelSize = new GridPoint2(surface.getRegionWidth(), surface.getRegionHeight());
    TiledMap tiledMap = createSideScrollTiles(tilePixelSize, surface, underground, sky, tree);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  private TerrainComponent createLevelTwoTerrain(float tileWorldSize, TextureRegion surface, TextureRegion underground, TextureRegion sky, TextureRegion star) {
    GridPoint2 tilePixelSize = new GridPoint2(surface.getRegionWidth(), surface.getRegionHeight());
    TiledMap tiledMap = createLevelTwoTiles(tilePixelSize, surface, underground, sky, star);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  private TerrainComponent createLevelThreeTerrain(float tileWorldSize, TextureRegion surface, TextureRegion underground, TextureRegion sky, TextureRegion star) {
    GridPoint2 tilePixelSize = new GridPoint2(surface.getRegionWidth(), surface.getRegionHeight());
    TiledMap tiledMap = createLevelThreeTiles(tilePixelSize, surface, underground, sky, star);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
    switch (orientation) {
      case ORTHOGONAL:
        return new OrthogonalTiledMapRenderer(tiledMap, tileScale);
      case ISOMETRIC:
        return new IsometricTiledMapRenderer(tiledMap, tileScale);
      case HEXAGONAL:
        return new HexagonalTiledMapRenderer(tiledMap, tileScale);
      default:
        return null;
    }
  }

  //A new tile with side scroll-er
  private TiledMap createSideScrollTiles(
          GridPoint2 tileSize, TextureRegion surface, TextureRegion underground, TextureRegion sky, TextureRegion star) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile surfaceTile = new TerrainTile(surface);
    TerrainTile undergroundTile = new TerrainTile(underground);
    TerrainTile skyTile = new TerrainTile(sky);
    TerrainTile starTile = new TerrainTile(star);
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // Create base grass
    //fillTilesAt(layer, new GridPoint2(0, 0), new GridPoint2(100, 9), undergroundTile);
    //fillTilesAt(layer, new GridPoint2(0, 9), new GridPoint2(100, 10), surfaceTile);
    fillTilesAt(layer, new GridPoint2(0, 0), new GridPoint2(100, 20), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 20), new GridPoint2(10, 21), skyTile);
    fillTilesAt(layer, new GridPoint2(10, 20), new GridPoint2(11, 21), starTile);
    fillTilesAt(layer, new GridPoint2(11, 20), new GridPoint2(57, 21), skyTile);
    fillTilesAt(layer, new GridPoint2(57, 20), new GridPoint2(58, 21), starTile);
    fillTilesAt(layer, new GridPoint2(58, 20), new GridPoint2(98, 21), skyTile);
    fillTilesAt(layer, new GridPoint2(98, 20), new GridPoint2(99, 21), starTile);
    fillTilesAt(layer, new GridPoint2(99, 20), new GridPoint2(100, 21), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 21), new GridPoint2(100, 22), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 22), new GridPoint2(15, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(15, 22), new GridPoint2(16, 23), starTile);
    fillTilesAt(layer, new GridPoint2(16, 22), new GridPoint2(18, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(18, 22), new GridPoint2(19, 23), starTile);
    fillTilesAt(layer, new GridPoint2(19, 22), new GridPoint2(31, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(31, 22), new GridPoint2(32, 23), starTile);
    fillTilesAt(layer, new GridPoint2(32, 22), new GridPoint2(70, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(70, 22), new GridPoint2(72, 23), starTile);
    fillTilesAt(layer, new GridPoint2(72, 22), new GridPoint2(100, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 23), new GridPoint2(100, 24), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 24), new GridPoint2(5, 25), skyTile);
    fillTilesAt(layer, new GridPoint2(5, 24), new GridPoint2(6, 25), starTile);
    fillTilesAt(layer, new GridPoint2(6, 24), new GridPoint2(87, 25), skyTile);
    fillTilesAt(layer, new GridPoint2(87, 24), new GridPoint2(88, 25), skyTile);
    fillTilesAt(layer, new GridPoint2(88, 24), new GridPoint2(100, 25), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 25), new GridPoint2(100, 30), skyTile);
    // parses the level files
    try(BufferedReader br = new BufferedReader(new FileReader("level-floors/levelOne.txt"))) {
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
      int x = 0, y = 0, width = 0, distance = 0, i = 0;
      while (line != null) {
        String[] values = line.split(" ");
        width = Integer.parseInt(values[0]);
        x = Integer.parseInt(values[1]);
        y = Integer.parseInt(values[2]);
        distance = (width * 2) + x;
        fillTilesAt(layer, new GridPoint2(x, 0), new GridPoint2(distance, y - 1), undergroundTile);
        fillTilesAt(layer, new GridPoint2(x, y - 1), new GridPoint2(distance, y), surfaceTile);
        line = br.readLine();
        i++;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private TiledMap createLevelTwoTiles(GridPoint2 tileSize, TextureRegion surface, TextureRegion underground, TextureRegion sky, TextureRegion star) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile surfaceTile = new TerrainTile(surface);
    TerrainTile undergroundTile = new TerrainTile(underground);
    TerrainTile skyTile = new TerrainTile(sky);
    TerrainTile starTile = new TerrainTile(star);
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);


    fillTilesAt(layer, new GridPoint2(0, 0), new GridPoint2(100, 20), skyTile);
    fillTilesAt(layer, new GridPoint2(25, 0), new GridPoint2(50, 10), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 10), new GridPoint2(100, 20), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 20), new GridPoint2(10, 21), skyTile);
    fillTilesAt(layer, new GridPoint2(10, 20), new GridPoint2(11, 21), starTile);
    fillTilesAt(layer, new GridPoint2(11, 20), new GridPoint2(57, 21), skyTile);
    fillTilesAt(layer, new GridPoint2(57, 20), new GridPoint2(58, 21), starTile);
    fillTilesAt(layer, new GridPoint2(58, 20), new GridPoint2(98, 21), skyTile);
    fillTilesAt(layer, new GridPoint2(98, 20), new GridPoint2(99, 21), starTile);
    fillTilesAt(layer, new GridPoint2(99, 20), new GridPoint2(100, 21), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 21), new GridPoint2(100, 22), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 22), new GridPoint2(15, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(15, 22), new GridPoint2(16, 23), starTile);
    fillTilesAt(layer, new GridPoint2(16, 22), new GridPoint2(18, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(18, 22), new GridPoint2(19, 23), starTile);
    fillTilesAt(layer, new GridPoint2(19, 22), new GridPoint2(31, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(31, 22), new GridPoint2(32, 23), starTile);
    fillTilesAt(layer, new GridPoint2(32, 22), new GridPoint2(70, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(70, 22), new GridPoint2(72, 23), starTile);
    fillTilesAt(layer, new GridPoint2(72, 22), new GridPoint2(100, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 23), new GridPoint2(100, 24), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 24), new GridPoint2(5, 25), skyTile);
    fillTilesAt(layer, new GridPoint2(5, 24), new GridPoint2(6, 25), starTile);
    fillTilesAt(layer, new GridPoint2(6, 24), new GridPoint2(87, 25), skyTile);
    fillTilesAt(layer, new GridPoint2(87, 24), new GridPoint2(88, 25), skyTile);
    fillTilesAt(layer, new GridPoint2(88, 24), new GridPoint2(100, 25), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 25), new GridPoint2(100, 30), skyTile);
    // parses the level files
    try(BufferedReader br = new BufferedReader(new FileReader("level-floors/levelTwo.txt"))) {
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
      int x = 0, y = 0, width = 0, distance = 0, i = 0;
      while (line != null) {
        String[] values = line.split(" ");
        width = Integer.parseInt(values[0]);
        x = Integer.parseInt(values[1]);
        y = Integer.parseInt(values[2]);
        distance = (width * 2) + x;
        fillTilesAt(layer, new GridPoint2(x, 0), new GridPoint2(distance, y - 1), undergroundTile);
        fillTilesAt(layer, new GridPoint2(x, y - 1), new GridPoint2(distance, y), surfaceTile);
        line = br.readLine();
        i++;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private TiledMap createLevelThreeTiles(GridPoint2 tileSize, TextureRegion surface, TextureRegion underground, TextureRegion sky, TextureRegion star) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile surfaceTile = new TerrainTile(surface);
    TerrainTile undergroundTile = new TerrainTile(underground);
    TerrainTile skyTile = new TerrainTile(sky);
    TerrainTile starTile = new TerrainTile(star);
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    //fillTilesAt(layer, new GridPoint2(0, 0), new GridPoint2(100, 9), undergroundTile);
    //fillTilesAt(layer, new GridPoint2(0, 9), new GridPoint2(100, 10), surfaceTile);
    fillTilesAt(layer, new GridPoint2(0, 0), new GridPoint2(100, 20), skyTile);
    fillTilesAt(layer, new GridPoint2(25, 0), new GridPoint2(50, 10), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 10), new GridPoint2(100, 20), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 20), new GridPoint2(10, 21), skyTile);
    fillTilesAt(layer, new GridPoint2(10, 20), new GridPoint2(11, 21), starTile);
    fillTilesAt(layer, new GridPoint2(11, 20), new GridPoint2(57, 21), skyTile);
    fillTilesAt(layer, new GridPoint2(57, 20), new GridPoint2(58, 21), starTile);
    fillTilesAt(layer, new GridPoint2(58, 20), new GridPoint2(98, 21), skyTile);
    fillTilesAt(layer, new GridPoint2(98, 20), new GridPoint2(99, 21), starTile);
    fillTilesAt(layer, new GridPoint2(99, 20), new GridPoint2(100, 21), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 21), new GridPoint2(100, 22), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 22), new GridPoint2(15, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(15, 22), new GridPoint2(16, 23), starTile);
    fillTilesAt(layer, new GridPoint2(16, 22), new GridPoint2(18, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(18, 22), new GridPoint2(19, 23), starTile);
    fillTilesAt(layer, new GridPoint2(19, 22), new GridPoint2(31, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(31, 22), new GridPoint2(32, 23), starTile);
    fillTilesAt(layer, new GridPoint2(32, 22), new GridPoint2(70, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(70, 22), new GridPoint2(72, 23), starTile);
    fillTilesAt(layer, new GridPoint2(72, 22), new GridPoint2(100, 23), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 23), new GridPoint2(100, 24), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 24), new GridPoint2(5, 25), skyTile);
    fillTilesAt(layer, new GridPoint2(5, 24), new GridPoint2(6, 25), starTile);
    fillTilesAt(layer, new GridPoint2(6, 24), new GridPoint2(87, 25), skyTile);
    fillTilesAt(layer, new GridPoint2(87, 24), new GridPoint2(88, 25), skyTile);
    fillTilesAt(layer, new GridPoint2(88, 24), new GridPoint2(100, 25), skyTile);
    fillTilesAt(layer, new GridPoint2(0, 25), new GridPoint2(100, 30), skyTile);
    // parses the level files
    try(BufferedReader br = new BufferedReader(new FileReader("level-floors/levelThree.txt"))) {
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
      int x = 0, y = 0, width = 0, distance = 0, i = 0;
      while (line != null) {
        String[] values = line.split(" ");
        width = Integer.parseInt(values[0]);
        x = Integer.parseInt(values[1]);
        y = Integer.parseInt(values[2]);
        distance = (width * 2) + x;
        fillTilesAt(layer, new GridPoint2(x, 0), new GridPoint2(distance, y - 1), undergroundTile);
        fillTilesAt(layer, new GridPoint2(x, y - 1), new GridPoint2(distance, y), surfaceTile);
        line = br.readLine();
        i++;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    tiledMap.getLayers().add(layer);
    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private static void fillTilesAtRandom(
      TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile, int amount) {
    GridPoint2 min = new GridPoint2(0, 0);
    GridPoint2 max = new GridPoint2(mapSize.x - 1, mapSize.y - 1);

    for (int i = 0; i < amount; i++) {
      GridPoint2 tilePos = RandomUtils.random(min, max);
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(tile);
    }
  }

  private static void fillTilesAt(TiledMapTileLayer layer, GridPoint2 minPos, GridPoint2 maxPos, TerrainTile tile) {
    for (int x = minPos.x; x < maxPos.x; x++) {
      for (int y = minPos.y; y < maxPos.y; y++) {
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
      }
    }
  }

  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
      }
    }
  }

  /**
   * If a surface tile exists on the game map with the given x-coordinate, return
   * the corresponding y-coordinate
   * @param x
   * @param screenType the current level screen of the game
   * @return the corresponding y-coordinate of surface tile if it exists wih
   * given x-coordinate
   */
  public int getYOfSurface(int x, GdxGame.ScreenType screenType) {
    int y = 0;
    String filename = null;
    if (screenType == GdxGame.ScreenType.MAIN_GAME) {
      filename = "level-floors/levelOne.txt";
    } else if (screenType == GdxGame.ScreenType.LEVEL_TWO_GAME) {
      filename = "level-floors/levelTwo.txt";
    }
    try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line = br.readLine();
      int referenceX = 0, referenceY = 0, width = 0, distance = 0;

      while (line != null) {
        String[] values = line.split(" ");
        width = Integer.parseInt(values[0]);
        referenceX = Integer.parseInt(values[1]);
        referenceY = Integer.parseInt(values[2]);
        distance = (width * 2) + referenceX;

        if (x >= referenceX && x <= distance) {
          logger.debug("this is distance {}", distance);
          logger.debug("this is reference x {}", referenceX);
          y = referenceY;
          break;
        } else {
          line = br.readLine();
        }
      }
    } catch (FileNotFoundException e) {
      logger.error("No file is found with the name {}", filename);
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    logger.debug("this is y {}", y);
    return y;
  }

  /**
   * This enum should contain the different terrains in your game, e.g. forest,
   * cave, home, all with the same orientation. But for demonstration purposes,
   * the base code has the same level in 3 different orientations.
   */
  public enum TerrainType {
    SIDE_SCROLL_ER,
    LEVEL_TWO_TERRAIN,
    LEVEL_THREE_TERRAIN,
    LEVEL_FOUR_TERRAIN
  }
}
