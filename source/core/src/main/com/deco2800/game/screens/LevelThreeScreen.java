package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.services.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the level three game area.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class LevelThreeScreen extends MainGameScreen {
    private static final Logger logger = LoggerFactory.getLogger(LevelTwoScreen.class);

    /**
     * Load the game screen for level three.
     */
    public LevelThreeScreen(GdxGame game, ResourceService resourceService,
            MainGameScreen.Level level) {
        super(game, resourceService, level);
    }

    public LevelThreeScreen(GdxGame game, boolean hasDied,
            ResourceService resourceService, MainGameScreen.Level level) {
        super(game, hasDied, resourceService, level);
    }

    public LevelThreeScreen(GdxGame game, String saveState,
            ResourceService resourceService) {
        super(game, saveState, resourceService);
    }

}
