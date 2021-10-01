package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
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

    private GdxGame game;
    private ForestGameArea forestGameArea;

    public LoadingDisplay(){
        //this.game = game;
        //this.forestGameArea = forestGameArea;
    }


    @Override
    public void create() {
        super.create();
        //entity.getEvents().addListener("updateLoad", this::updateLoad);
        addActors();
        //updateLoad(100);
        //this.forestGameArea.loadAssets();
        //this.forestGameArea.create();

    }

    private void addActors() {
        background = new Table();
        background.setFillParent(true);
        //Table menuBtns = makeMenuBtns();
        //background.add(menuBtns).fillX();

        loadTable = new Table();
        loadTable.center();
        loadTable.setFillParent(true);

        loadingStatus = new Image(ServiceLocator.getResourceService().getAsset("images/0percent.png", Texture.class));
        loadTable.add(loadingStatus).width(850);

        stage.addActor(background);
        stage.addActor(loadTable);

    }

        /*private Table makeMenuBtns() {
        TextButton exitBtn = new TextButton("Exit", skin);

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        exitMenu();
                    }
                });


        Table table = new Table();
        table.add(exitBtn).expandX().left().pad(0f, 15f, 15f, 0f);
        return table;
    }
    private void exitMenu() {
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }*/

    private void updateLoad(int progress) {
        if (progress == 100) {
            game.setScreen(GdxGame.ScreenType.MAIN_MENU);
        }

    }


    @Override
    protected void draw(SpriteBatch batch) {
        //handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        background.clear();
        loadingStatus.clear();
        super.dispose();
    }
}
