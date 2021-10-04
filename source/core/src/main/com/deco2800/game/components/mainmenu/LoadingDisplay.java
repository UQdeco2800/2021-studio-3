package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
        //2256x1504


    }

    private void addActors() {
        background = new Table();
        background.setFillParent(true);
        Texture backgroundImage = resourceService.getAsset("images/loadingF1.png", Texture.class);
        //background.background(new TextureRegionDrawable(new TextureRegion(backgroundImage)));
        //background.setWidth(1000);
        //background.add(new Image(backgroundImage)).width(2256);
        
        loadTable = new Table();
        loadTable.center();
        loadTable.setFillParent(true);
        Texture loadingStart = resourceService.getAsset("images/bar1.png", Texture.class);
        loadingStatus = new Image(loadingStart);
        loadTable.add(loadingStatus).width(2500).height(1500);

        stage.addActor(background);
        stage.addActor(loadTable);

    }

    private void updateLoadingBar(int currentLoad) {
        switch (currentLoad) {
            case 0:
                loadingStatus.setDrawable(new SpriteDrawable
                        (new Sprite(resourceService.getAsset("images/bar1.png", Texture.class))));
            case 10:
                loadingStatus.setDrawable(new SpriteDrawable
                        (new Sprite(resourceService.getAsset("images/bar2.png", Texture.class))));
                break;
            case 20:
                loadingStatus.setDrawable(new SpriteDrawable
                        (new Sprite(resourceService.getAsset("images/bar3.png", Texture.class))));
                break;
            case 30:
                loadingStatus.setDrawable(new SpriteDrawable
                        (new Sprite(resourceService.getAsset("images/bar4.png", Texture.class))));
                break;
            case 40:
                loadingStatus.setDrawable(new SpriteDrawable
                        (new Sprite(resourceService.getAsset("images/bar5.png", Texture.class))));
                break;
            case 50:
                loadingStatus.setDrawable(new SpriteDrawable
                        (new Sprite(resourceService.getAsset("images/bar6.png", Texture.class))));
                break;
            case 60:
                loadingStatus.setDrawable(new SpriteDrawable
                        (new Sprite(resourceService.getAsset("images/bar7.png", Texture.class))));
                break;
            case 70:
                loadingStatus.setDrawable(new SpriteDrawable
                        (new Sprite(resourceService.getAsset("images/bar8.png", Texture.class))));
                break;
            case 80:
                loadingStatus.setDrawable(new SpriteDrawable
                        (new Sprite(resourceService.getAsset("images/bar9.png", Texture.class))));
                break;
            case 95:
                loadingStatus.setDrawable(new SpriteDrawable
                        (new Sprite(resourceService.getAsset("images/bar10.png", Texture.class))));
                break;
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
