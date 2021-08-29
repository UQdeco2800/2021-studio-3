package com.deco2800.game.entities.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class PlayerFactory {
  private static final PlayerConfig stats =
      FileLoader.readClass(PlayerConfig.class, "configs/player.json");

  /**
   * Create a player entity.
   * @return entity
   */
  public static Entity createPlayer() {
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForPlayer();
    Pixmap pixmap = new Pixmap(3,1, Pixmap.Format.RGBA8888);
    Texture pixmaptex = new Texture(pixmap);
    TextureRegion h= new TextureRegion(pixmaptex);
    AssetManager manager =  new  AssetManager ();
    manager.load("images/100.png", Texture.class);
    manager.load("images/90.png", Texture.class);
    manager.load("images/80.png", Texture.class);
    manager.load("images/70.png", Texture.class);
    manager.load("images/60.png", Texture.class);
    manager.load("images/50.png", Texture.class);
    manager.load("images/40.png", Texture.class);
    manager.load("images/30.png", Texture.class);
    manager.load("images/20.png", Texture.class);
    manager.load("images/10.png", Texture.class);
    manager.load("images/00.png", Texture.class);
    manager.finishLoading();

    Entity player =
        new Entity()
            .addComponent(new TextureRenderComponent("images/box_boy_leaf.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
            .addComponent(new InventoryComponent(stats.gold))
            .addComponent(inputComponent)
            .addComponent(new PlayerStatsDisplay(manager,h));

    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    player.getComponent(TextureRenderComponent.class).scaleEntity();
    return player;
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
