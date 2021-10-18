package com.deco2800.game.components.loadmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.mainmenu.LoadingDisplay;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoadMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    GdxGame game;
    Table loadTable;
    Table backButtonTable;
    Table fileButtonTable;
    Table labelTable;
    private static final String MUSIC_FILE_PATH = "sounds/background.mp3";


    public LoadMenuDisplay(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        super.create();

        entity.getEvents().addListener("back", this::back);
        addActors();
    }

    private void addActors() {
        loadTable = new Table();
        backButtonTable = new Table();
        labelTable = new Table();
        playBackgroundMusic();

        loadTable.setFillParent(true);
        backButtonTable.setFillParent(true);
        labelTable.setFillParent(true);
        labelTable.center().top();
        loadTable.setBackground(new TextureRegionDrawable(
                new Texture(Gdx.files.internal("images/loadScreen3.png"))));
        Label title = new Label("Load Game", skin, "title");
        labelTable.add(title);

        TextButton backBtn = new TextButton("BACK", skin);
        backButtonTable.bottom().right();
        backButtonTable.add(backBtn).padBottom(15f).padRight(15f);;

        backBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Back button clicked");
                        entity.getEvents().trigger("back");
                    }
                });

        Gdx.input.setInputProcessor(stage);
        stage.addActor(loadTable);
        stage.addActor(backButtonTable);
        stage.addActor(labelTable);
    }

    public void back() {
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    private void playBackgroundMusic() {
        Music menuSong = ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class);
        menuSong.setLooping(true);
        menuSong.setVolume(0.5f);
        menuSong.play();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        //
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        loadTable.clear();
        backButtonTable.clear();
        stage.dispose();
        ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class).stop();
        super.dispose();
    }
}
