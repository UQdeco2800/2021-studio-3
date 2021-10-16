package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.SaveData.SaveData;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.LevelTwoArea;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.maingame.*;
import com.deco2800.game.components.player.PlayerLossPopup;
import com.deco2800.game.components.player.PlayerWinPopup;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the level two game area.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class LevelTwoScreen extends MainGameScreen {
    private static final Logger logger = LoggerFactory.getLogger(LevelTwoScreen.class);

    /**
     * Load the game screen for level two.
     */
    public LevelTwoScreen(GdxGame game, ResourceService resourceService,
            MainGameScreen.Level level) {
        super(game, resourceService, level);
        saveData = new SaveData(game, level.getPlayer());
        saveData.savePlayerData();
    }

    public LevelTwoScreen(GdxGame game, boolean hasDied,
            ResourceService resourceService, MainGameScreen.Level level) {
        super(game, hasDied, resourceService, level);
        saveData = new SaveData(game, level.getPlayer());
        saveData.savePlayerData();

    }


    public LevelTwoScreen(GdxGame game, String saveState,
            ResourceService resourceService) {
        super(game, saveState, resourceService);
    
    }
}
