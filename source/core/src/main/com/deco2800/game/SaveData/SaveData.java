package com.deco2800.game.SaveData;

import com.badlogic.gdx.Game;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.*;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class which saves player data.
 */
public class SaveData {

    private static final Logger logger = LoggerFactory.getLogger(SaveData.class);
    public Entity player;
    protected File saveFile;
    private GdxGame game;

    public SaveData(GdxGame game, Entity player, int saveFileNumber) {
        this.game = game;
        this.player = player;
        saveFile = new File(String.format("save/File %d.txt", saveFileNumber));
        //savePlayerData(player);
    }

    /**
     * Constructor for SaveData.
     * @param game current game
     * @param player player's entity
     */
    public SaveData(GdxGame game, Entity player) {
        this.game = game;
        this.player = player;
        saveFile = new File("saves/saveOne.txt");
    }

    /**
     * @return the player entity
     */
    public Entity getPlayer() {
        return player;
    }

    /**
     * Saves player data to a file, overwrites previous file save.
     * Writes player data that will later be read from the onLoad method in MainMenuActions.
     */
    public void savePlayerData() {
            if (player != null) {
                try {
                    if (saveFile.createNewFile()) {
                        logger.info("New Save created!");
                    } else {
                        logger.info("File already exists");
                    }

                    FileWriter fileWriter = new FileWriter(saveFile);
                    fileWriter.write("Level:");

                    switch ((game.getScreenType().name())) {
                        case "MAIN_GAME":
                        case "RESPAWN1":
                            fileWriter.write("levelOne");
                            break;
                        case "LEVEL_TWO_GAME":
                        case "RESPAWN2":
                            fileWriter.write("levelTwo");
                            break;
                        case "LEVEL_THREE_GAME":
                        case "RESPAWN3":
                            fileWriter.write("levelThree");
                            break;
                    }
                    fileWriter.write("\nSCORE:");
                    fileWriter.write(String.valueOf(player.getComponent(ScoreComponent.class).getScore()));
                    fileWriter.write("\nLIVES:");
                    fileWriter.write(String.valueOf(player.getComponent(LivesComponent.class).getLives()));
                    fileWriter.write("\nHEALTH:");
                    fileWriter.write(String.valueOf(player.getComponent(CombatStatsComponent.class).getHealth()));
                    fileWriter.write("\nSPRINT:");
                    fileWriter.write(String.valueOf(player.getComponent(SprintComponent.class).getSprint()));
                    fileWriter.write("\nX:");
                    fileWriter.write(String.valueOf((int) Math.floor(player.getCenterPosition().x*2)));
                    fileWriter.write("\nY:");
                    fileWriter.write(String.valueOf((int) Math.floor(player.getCenterPosition().y*2)));
                    fileWriter.close();
                    logger.info("Successfully saved player data");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }
}
