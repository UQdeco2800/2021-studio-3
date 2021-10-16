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
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private static final Logger logger = LoggerFactory.getLogger(TerrainFactory.class);
  private static final GridPoint2 MAP_SIZE = new GridPoint2(210, 30);

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
        // Placeholder : uses Level 1 Terrain and Layout.
        TextureRegion surfaceFour =
                new TextureRegion(resourceService.getAsset("images/background_surface.png", Texture.class));
        TextureRegion undergroundFour =
                new TextureRegion(resourceService.getAsset("images/background_rock.png", Texture.class));
        TextureRegion skyFour =
                new TextureRegion(resourceService.getAsset("images/background_sky.png", Texture.class));
        TextureRegion starFour =
                new TextureRegion(resourceService.getAsset("images/background_star.png", Texture.class));
        return createLevelFourTerrain(0.5f, surfaceFour, undergroundFour, skyFour, starFour);
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

  /**
   * Creates the terrain for the Level Four Area.
   *
   * @param tileWorldSize the size of the tiles within the world
   * @param surface the texture for the Surface of the map
   * @param underground the texture for the Underground of the map
   * @param sky the texture for the Sky of the map
   * @param star the texture for the Stars in the Sky of the map.
   *
   * @return a new terrain with these textures and orientation.
   * */
  private TerrainComponent createLevelFourTerrain(float tileWorldSize, TextureRegion surface, TextureRegion underground, TextureRegion sky, TextureRegion star) {
    GridPoint2 tilePixelSize = new GridPoint2(surface.getRegionWidth(), surface.getRegionHeight());
    TiledMap tiledMap = createLevelFourTiles(tilePixelSize, surface, underground, sky, star);
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
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    addSkyTiles(layer, sky, star, "level-floors/levelOneSky.txt");
    addGroundTiles(layer, underground, surface, "level-floors/levelOneGround.txt");
    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  /**
   * Adds the ground tiles to the map based on the values given inside the text file.
   * @param layer the TiledMap layer
   * @param underground the underground tile TextureRegion
   * @param surface the surface tile TextureRegion
   * @param filename the name of the text file containing information regarding tile placement.
   */
  private void addGroundTiles(TiledMapTileLayer layer, TextureRegion underground,
                              TextureRegion surface, String filename) {
    TerrainTile surfaceTile = new TerrainTile(surface);
    TerrainTile undergroundTile = new TerrainTile(underground);
    ArrayList<String> terrainLayout = readFile(filename);

    float width, height;
    int x, y, distanceX, distanceY;
    for (String s : terrainLayout) {
      String[] values = s.split(" ");
      width = Float.parseFloat(values[0]);
      height = Float.parseFloat(values[1]);
      x = Integer.parseInt(values[2]);
      y = Integer.parseInt(values[3]);
      distanceX = (int) (( width * 2) + x);
      distanceY = (int) (( height * 2) + y);
      // Fills underground tiles, leaves one layer on top for surface tiles
      fillTilesAt(layer, new GridPoint2(x, 0), new GridPoint2(distanceX, distanceY - 1), undergroundTile);
      // Fills surface tiles
      fillTilesAt(layer, new GridPoint2(x, distanceY - 1), new GridPoint2(distanceX, distanceY), surfaceTile);
    }
  }

  /**
   * Adds the sky tiles to the map based on the values given inside the text file.
   * @param layer the TiledMap layer
   * @param sky the sky tile TextureRegion
   * @param star the star tile TextureRegion
   * @param filename the name of the text file containing information regarding tile placement.
   */
  private void addSkyTiles(TiledMapTileLayer layer, TextureRegion sky,
                           TextureRegion star, String filename) {
    TerrainTile skyTile = new TerrainTile(sky);
    TerrainTile starTile = new TerrainTile(star);
    ArrayList<String> terrainLayout = readFile(filename);

    float width, height;
    int x, y, distanceX, distanceY;
    for (String s : terrainLayout) {
      String[] values = s.split(" ");
      width = Float.parseFloat(values[1]);
      height = Float.parseFloat(values[2]);
      x = Integer.parseInt(values[3]);
      y = Integer.parseInt(values[4]);
      distanceX = (int) (( width * 2) + x);
      distanceY = (int) (( height * 2) + y);
      if (values[0].equals("#")) {
        logger.debug("creating sky tile at {}, {}", x, y);
        fillTilesAt(layer, new GridPoint2(x, y), new GridPoint2(distanceX, distanceY), skyTile);
      } else if (values[0].equals("*")) {
        logger.debug("Creating star tile at {}, {}", x, y);
        fillTilesAt(layer, new GridPoint2(x, y), new GridPoint2(distanceX, distanceY), starTile);
      }
    }
  }

  /**
   * Reads the text file given and returns each line of text as an arrayList of strings.
   * @param filename the name of the text file
   * @return a string array with each element containing a line from the text file
   */
  private ArrayList<String> readFile(String filename) {
    ArrayList<String> terrainLayout = new ArrayList<>();
    try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line = br.readLine();
      terrainLayout.add(line);
      while (line != null) {
        terrainLayout.add(line);
        line = br.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return terrainLayout;
  }

  private TiledMap createLevelTwoTiles(GridPoint2 tileSize, TextureRegion surface, TextureRegion underground, TextureRegion sky, TextureRegion star) {
    TiledMap tiledMap = new TiledMap();
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // parses the level files
    addSkyTiles(layer, sky, star, "level-floors/levelTwoSky.txt");
    addGroundTiles(layer, underground, surface, "level-floors/levelTwoGround.txt");
    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private TiledMap createLevelThreeTiles(GridPoint2 tileSize, TextureRegion surface, TextureRegion underground, TextureRegion sky, TextureRegion star) {
    TiledMap tiledMap = new TiledMap();
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // parses the level files
    addSkyTiles(layer, sky, star, "level-floors/levelThreeSky.txt");
    addGroundTiles(layer, underground, surface, "level-floors/levelThreeGround.txt");
    tiledMap.getLayers().add(layer);

    return tiledMap;
  }

  /**
   * Spawns in the tiles for the Level Four game area.
   *
   * @param tileSize the size of the tiles
   * @param surface the texture for the Surface of the map
   * @param underground the texture for the Underground of the map
   * @param sky the texture for the Sky of the map
   * @param star the texture for the Stars in the sky on the map.
   *
   * @return the new tiled area.
   * */
  private TiledMap createLevelFourTiles(GridPoint2 tileSize,
          TextureRegion surface, TextureRegion underground, TextureRegion sky,
          TextureRegion star) {

    TiledMap tiledMap = new TiledMap();
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // Create the terrain and sky
    addSkyTiles(layer, sky, star, "level-floors/levelFourSky.txt");
    addGroundTiles(layer, underground, surface, "level-floors/levelFourGround.txt");
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
  public int getYOfSurface(int x, MainGameScreen.Level screenType) {
    int y = 0;
    String filename = null;
    if (screenType == MainGameScreen.Level.ONE) {
      filename = "level-floors/levelOneGround.txt";
    } else if (screenType == MainGameScreen.Level.TWO) {
      filename = "level-floors/levelTwoGround.txt";
    } else if (screenType == MainGameScreen.Level.THREE) {
      filename = "level-floors/levelThreeGround.txt";
    } else if (screenType == MainGameScreen.Level.FOUR) {
      filename = "level-floors/levelFourGround.txt";
    }
    try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line = br.readLine();
      int referenceX = 0, referenceY = 0, distanceX = 0, distanceY = 0;
      float width = 0, height = 0;
      while (line != null) {
        String[] values = line.split(" ");
        width = Float.parseFloat(values[0]);
        height = Float.parseFloat(values[1]);
        referenceX = Integer.parseInt(values[2]);
        referenceY = Integer.parseInt(values[3]);
        distanceX = (int) (width * 2) + referenceX;
        distanceY = (int) (height * 2) + referenceY;

        if (x >= referenceX && x <= distanceX) {
          logger.debug("this is distance {}", distanceX);
          logger.debug("this is reference x {}", referenceX);
          y = distanceY;
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
