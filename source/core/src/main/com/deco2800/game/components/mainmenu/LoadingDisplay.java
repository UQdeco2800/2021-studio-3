package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the loading screen.
 */
public class LoadingDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoadingDisplay.class);
    private static final float Z_INDEX = 2f;

    /* The background for the loading screen */
    private Table background;

    /* Holds the loading bar */
    private Table loadTable;

    /* The loading bar image that reflects the current loading progress */
    private Image loadingStatus;

    /* The resource service that holds all the assets required for the UI */
    private ResourceService resourceService;

    private static final String MUSIC_FILE_PATH = "sounds/loading_background_music_new.mp3";

    public LoadingDisplay(){
        resourceService = ServiceLocator.getResourceService();
    }

    @Override
    public void create() {
        super.create();

        addActors();
    }

    /**
     * Creates the visualisation for the menu, including the background text and
     * initialising the loading bar.
     */
    private void addActors() {
        background = new Table();
        background.setFillParent(true);

        Label loadingLabel = new Label("Loading...", skin, "font_large", "black");
        loadingLabel.setFontScale(4,4);
        background.add(loadingLabel).padBottom(275);

        loadTable = new Table();
        loadTable.center();
        loadTable.setFillParent(true);
        Texture loadingStart = resourceService.getAsset("images/bar1.png", Texture.class);
        loadingStatus = new Image(loadingStart);
        loadTable.add(loadingStatus).width(2500).height(1250);

        stage.addActor(background);
        stage.addActor(loadTable);

        playBackgroundMusic();
    }

    /**
     * Updates the loading bar to reflect the percentage of assets that have been loaded
     * @param currentLoad percentage of assets that have been loaded
     */
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
            case 90:
                loadingStatus.setDrawable(new SpriteDrawable
                        (new Sprite(resourceService.getAsset("images/bar10.png", Texture.class))));
                break;
            }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        Gdx.gl.glClearColor(195/255f,206/255f,224/255f,1);
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

    private void playBackgroundMusic() {
        Music menuSong = ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class);
        menuSong.setLooping(true);
        menuSong.setVolume(0.5f);
        menuSong.play();
    }
}
