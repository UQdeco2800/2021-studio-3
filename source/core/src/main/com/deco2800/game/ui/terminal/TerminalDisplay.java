package com.deco2800.game.ui.terminal;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.BuffInformation;
import com.deco2800.game.components.maingame.BuffManager;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.ui.UIComponent;

import java.util.*;

/**
 * A ui component for displaying the debug terminal. The terminal is positioned
 * at the bottom of the screen.
 */
public class TerminalDisplay extends UIComponent {
  private static final float Z_INDEX = 10f;
  private Terminal terminal;
  private Label label;
  AssetManager manager;
  private Entity player;
  //public static TextureRegion h= new TextureRegion(null,0f,0f,5f,5f);
  private Collection<BuffManager.BuffTypes> buffTypes;
  public TerminalDisplay(AssetManager manager, GameArea currentMap){
    this.manager = manager;
    //this.player = ((ForestGameArea) currentMap).getPlayer();
    this.player = ((ForestGameArea) currentMap).getPlayer();
    this.buffTypes = new ArrayList<>();
  }

  @Override
  public void create() {
    super.create();
    addActors();
    terminal = entity.getComponent(Terminal.class);
  }

  private void addActors() {
    String message = "";
    label = new Label("> " + message, skin);
    label.setPosition(5f, 0);
    stage.addActor(label);
  }

  @Override
  public void draw(SpriteBatch batch) {
    if (terminal.isOpen()) {
      label.setVisible(true);
      String message = terminal.getEnteredMessage();
      label.setText("> " + message);
    } else {
      label.setVisible(false);
    }
    Collection<BuffInformation> buffs = entity.getComponent(BuffManager.class).getTimedBuffs();
    float i = 0f;
    for (BuffInformation buff:buffs) {
      if(Objects.equals(buff.getType().toString(), "BT_INVIN")){
        batch.draw(manager.get("images/invincible.png", Texture.class),player.getPosition().x+1.2f-i, player.getPosition().y+1.7f,0.4f,0.4f);
      }
      if (Objects.equals(buff.getType().toString(), "DT_NO_JUMP")){
        batch.draw(manager.get("images/noJumping.png", Texture.class),player.getPosition().x+1.2f-i, player.getPosition().y+1.7f,0.4f,0.4f);
      }
      if (Objects.equals(buff.getType().toString(), "BT_INF_SPRINT")){
        batch.draw(manager.get("images/infiniteSprint.png", Texture.class),player.getPosition().x+1.2f-i, player.getPosition().y+1.7f,0.4f,0.4f);
      }
      if (Objects.equals(buff.getType().toString(), "DT_DOUBLE_DMG")){
        batch.draw(manager.get("images/doubleHurt.png", Texture.class),player.getPosition().x+1.2f-i, player.getPosition().y+1.7f,0.4f,0.4f);
      }
      i+=0.4f;
    }
  }

  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  @Override
  public void dispose() {
    super.dispose();
    label.remove();
  }
}
