package com.deco2800.game.entities.factories;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.ProgressComponent;
import com.deco2800.game.components.SprintComponent;
import com.deco2800.game.components.maingame.BuffManager;
import com.deco2800.game.components.player.*;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
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
  public static Pixmap pixmap = new Pixmap(3,1, Pixmap.Format.RGBA8888);
  public static Texture pixmaptex = new Texture(pixmap);
  public static TextureRegion h= new TextureRegion(pixmaptex);
  public static AssetManager manager =  new  AssetManager ();

  /**
   * load picture to AssetManager.
   * @return assetManager
   */
  public static AssetManager load(){
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
    manager.load("images/untouchedCheckpoint.png",Texture.class);
    manager.finishLoading();
    return manager;
  }



  /**
   * Create a player entity.
   * @return entity
   */
  public static Entity createPlayer() {
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForPlayer();
    load();
    //Defining and Adding player related animation here
    //---------------------------------
    // Adds Animations that can be used by the player
    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/PlayerMovementAnimations.atlas", TextureAtlas.class));
    animator.addAnimation("normal-stationary", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("normal-walk", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("normal-sprint", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("normal-jump", 0.3f, Animation.PlayMode.LOOP);
    animator.addAnimation("rough-stationary", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("rough-walk", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("rough-sprint", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("rough-jump", 0.3f, Animation.PlayMode.LOOP);
    animator.addAnimation("damaged-stationary", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("damaged-walk", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("damaged-sprint", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("damaged-jump", 0.3f, Animation.PlayMode.LOOP);
    animator.addAnimation("dead", 0.4f, Animation.PlayMode.LOOP);

    // Starts the idle animation
    animator.startAnimation("normal-stationary");

    Entity player =
            new Entity()
//            .addComponent(new TextureRenderComponent("images/box_boy_leaf.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                    .addComponent(new PlayerActions())
                    .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                    .addComponent(new InventoryComponent(stats.gold))
                    .addComponent(new PlayerStateComponent())
                    .addComponent(inputComponent)
                    .addComponent(new SprintComponent(100))
                    .addComponent(animator)
                    .addComponent(new PlayerAnimationController())
                    .addComponent(new PlayerStatsDisplay(manager,h));


    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
//    player.getComponent(TextureRenderComponent.class).scaleEntity();
    player.getComponent(AnimationRenderComponent.class).scaleEntity();
    return player;
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
