package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadingDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoadingDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table background;
    private Table loadTable;

    private Image loadingStatus;

    private ResourceService resourceService;

    public LoadingDisplay(){
        resourceService = ServiceLocator.getResourceService();
    }


    @Override
    public void create() {
        super.create();

        addActors();

    }

    private void addActors() {
        background = new Table();
        background.setFillParent(true);
        //Texture backgroundImage = resourceService.getAsset("images/loading3.png", Texture.class);
        //background.background(new TextureRegionDrawable(new TextureRegion(backgroundImage)));


        loadTable = new Table();
        loadTable.center();
        loadTable.setFillParent(true);
        Texture loadingStart = resourceService.getAsset("images/0percent.png", Texture.class);
        loadingStatus = new Image(loadingStart);
        loadTable.add(loadingStatus).width(850);

        stage.addActor(background);
        stage.addActor(loadTable);

    }

    private void updateLoadingBar(int currentLoad) {
        if (currentLoad % 10 == 0 && currentLoad > 0) {
            switch (currentLoad) {
                case 10:
                    loadingStatus.setDrawable(new SpriteDrawable
                            (new Sprite(resourceService.getAsset("images/10percent.png", Texture.class))));
                    break;
                case 20:
                    loadingStatus.setDrawable(new SpriteDrawable
                            (new Sprite(resourceService.getAsset("images/20percent.png", Texture.class))));
                    break;
                case 30:
                    loadingStatus.setDrawable(new SpriteDrawable
                            (new Sprite(resourceService.getAsset("images/30percent.png", Texture.class))));
                    break;
                case 40:
                    loadingStatus.setDrawable(new SpriteDrawable
                            (new Sprite(resourceService.getAsset("images/40percent.png", Texture.class))));
                    break;
                case 50:
                    loadingStatus.setDrawable(new SpriteDrawable
                            (new Sprite(resourceService.getAsset("images/50percent.png", Texture.class))));
                    break;
                case 60:
                    loadingStatus.setDrawable(new SpriteDrawable
                            (new Sprite(resourceService.getAsset("images/60percent.png", Texture.class))));
                    break;
                case 70:
                    loadingStatus.setDrawable(new SpriteDrawable
                            (new Sprite(resourceService.getAsset("images/70percent.png", Texture.class))));
                    break;
                case 80:
                    loadingStatus.setDrawable(new SpriteDrawable
                            (new Sprite(resourceService.getAsset("images/80percent.png", Texture.class))));
                    break;
                case 90:
                    loadingStatus.setDrawable(new SpriteDrawable
                            (new Sprite(resourceService.getAsset("images/90percent.png", Texture.class))));
                    break;
                case 100:
                    loadingStatus.setDrawable(new SpriteDrawable
                            (new Sprite(resourceService.getAsset("images/100percent.png", Texture.class))));
                    break;
            }
        }
    }


    @Override
    protected void draw(SpriteBatch batch) {
        updateLoadingBar((int) (ServiceLocator.getResourceService().getAssetManager().getProgress() * 100));
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        background.clear();
        loadingStatus.clear();
        loadTable.clear();
        super.dispose();
    }
}
