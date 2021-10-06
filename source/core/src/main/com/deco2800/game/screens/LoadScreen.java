package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.deco2800.game.GdxGame;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(LoadScreen.class);
    private final Renderer renderer;
    GdxGame game;
    ResourceService resourceService;

    public LoadScreen(GdxGame game, ResourceService resourceService) {
        this.game = game;

        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();
        this.resourceService = ServiceLocator.getResourceService();
    }

}
