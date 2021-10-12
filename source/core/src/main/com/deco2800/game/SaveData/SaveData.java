package com.deco2800.game.SaveData;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.LivesComponent;
import com.deco2800.game.components.ProgressComponent;
import com.deco2800.game.components.ScoreComponent;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveData {

    private static final Logger logger = LoggerFactory.getLogger(SaveData.class);
    public Entity player;
    protected File saveFile;

    public SaveData(Entity player, int saveFileNumber) {
        this.player = player;
        saveFile = new File(String.format("save/File %d.txt", saveFileNumber));
        savePlayerData(player);
    }

    public SaveData(Entity player) {
        this.player = player;
        saveFile = new File("save/save.txt");
        savePlayerData(player);
    }

    public Entity getPlayer() {
        return player;
    }

    public void savePlayerData(Entity player) {
            if (player != null) {
                try {
                    if (saveFile.createNewFile()) {
                        logger.debug("New Save created!");
                    } else {
                        logger.debug("File already exists");
                    }

                    FileWriter fileWriter = new FileWriter(saveFile);
                    fileWriter.write(String.valueOf(player.getComponent(ScoreComponent.class).getScore()));
                    fileWriter.write(String.valueOf(player.getComponent(ProgressComponent.class).getProgress()));
                    fileWriter.write(String.valueOf(player.getComponent(LivesComponent.class).getLives()));
                    fileWriter.write(String.valueOf(player.getComponent(CombatStatsComponent.class).getHealth()));
                    fileWriter.close();
                    logger.debug("Successfully wrote to file");
                    System.out.println("Successfully write to file");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }
}