package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.services.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the level four game area.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class LevelFourScreen extends MainGameScreen {
    private static final Logger logger = LoggerFactory.getLogger(LevelFourScreen.class);

    /**
     * Load the game screen for level four.
     */
    public LevelFourScreen(GdxGame game, ResourceService resourceService,
            MainGameScreen.Level level) {
        super(game, resourceService, level);
    }

    public LevelFourScreen(GdxGame game, boolean hasDied,
            ResourceService resourceService, MainGameScreen.Level level) {
        super(game, hasDied, resourceService, level);
    }

    public LevelFourScreen(GdxGame game, String saveState,
            ResourceService resourceService) {
        super(game, saveState, resourceService);
    }

}

