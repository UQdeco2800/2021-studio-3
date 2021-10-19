package com.deco2800.game.services;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
class ResourceServiceTest {
  @Test
  void loadAllShouldLoadUnloadAssets() {
    String texture1 = "test/files/tree.png";
    String texture2 = "test/files/missing.png";
    String texture3 = "test/files/heart.png";
    String[] textures = {texture1, texture2, texture3};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadTextures(textures);
    resourceService.loadAll();

    verify(assetManager).load(texture1, Texture.class);
    verify(assetManager).load(texture2, Texture.class);
    verify(assetManager).load(texture3, Texture.class);

    assertTrue(assetManager.contains(texture1, Texture.class));
    assertFalse(assetManager.contains(texture2, Texture.class));
    assertTrue(assetManager.contains(texture3, Texture.class));

    resourceService.unloadAssets(textures);

    assertFalse(assetManager.contains(texture1, Texture.class));
    assertFalse(assetManager.contains(texture2, Texture.class));
    assertFalse(assetManager.contains(texture3, Texture.class));
  }

  @Test
  void loadAllPlayerAnimationsAssets() {
    String[] forestTextures = {
            "images/Walking.png",
            "images/WalkingDamage90-50.png",
            "images/WalkingDamage50-10.png",
            "images/Sprint.png",
            "images/SprintDamage(50-90).png",
            "images/SprintDamage(10-50).png",
            "images/Jump.png",
            "images/JumpDamage(50-90).png",
            "images/JumpDamage(10-50).png",
            "images/IdleCharacters.png"
    };
    String[] forestTextureAtlases = {
            "images/PlayerMovementAnimations.atlas"
    };
    // Load Textures
    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadTextures(forestTextures);

    verify(assetManager).load("images/PlayerMovementAnimations.atlas", TextureAtlas.class);
    verify(assetManager).load("images/Walking.png", Texture.class);
    verify(assetManager).load("images/WalkingDamage90-50.png", Texture.class);
    verify(assetManager).load("images/WalkingDamage50-10.png", Texture.class);
    verify(assetManager).load("images/Sprint.png", Texture.class);
    verify(assetManager).load("images/SprintDamage(50-90).png", Texture.class);
    verify(assetManager).load("images/SprintDamage(10-50).png", Texture.class);
    verify(assetManager).load("images/Jump.png", Texture.class);
    verify(assetManager).load("images/JumpDamage(50-90).png", Texture.class);
    verify(assetManager).load("images/JumpDamage(10-50).png", Texture.class);
    verify(assetManager).load("images/IdleCharacters.png", Texture.class);

    assertTrue(assetManager.contains("images/PlayerMovementAnimations.atlas", TextureAtlas.class));
    assertTrue(assetManager.contains("images/Walking.png", Texture.class));
    assertTrue(assetManager.contains("images/WalkingDamage90-50.png", Texture.class));
    assertTrue(assetManager.contains("images/WalkingDamage50-10.png", Texture.class));
    assertTrue(assetManager.contains("images/Sprint.png", Texture.class));
    assertTrue(assetManager.contains("images/SprintDamage(50-90).png", Texture.class));
    assertTrue(assetManager.contains("images/SprintDamage(10-50).png", Texture.class));
    assertTrue(assetManager.contains("images/Jump.png", Texture.class));
    assertTrue(assetManager.contains("images/JumpDamage(50-90).png", Texture.class));
    assertTrue(assetManager.contains("images/JumpDamage(10-50).png", Texture.class));
    assertTrue(assetManager.contains("images/IdleCharacters.png", Texture.class));

    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(forestTextureAtlases);

    assertFalse(assetManager.contains("images/PlayerMovementAnimations.atlas", TextureAtlas.class));
    assertFalse(assetManager.contains("images/Walking.png", Texture.class));
    assertFalse(assetManager.contains("images/WalkingDamage90-50.png", Texture.class));
    assertFalse(assetManager.contains("images/WalkingDamage50-10.png", Texture.class));
    assertFalse(assetManager.contains("images/Sprint.png", Texture.class));
    assertFalse(assetManager.contains("images/SprintDamage(50-90).png", Texture.class));
    assertFalse(assetManager.contains("images/SprintDamage(10-50).png", Texture.class));
    assertFalse(assetManager.contains("images/Jump.png", Texture.class));
    assertFalse(assetManager.contains("images/JumpDamage(50-90).png", Texture.class));
    assertFalse(assetManager.contains("images/JumpDamage(10-50).png", Texture.class));
    assertFalse(assetManager.contains("images/IdleCharacters.png", Texture.class));
  }

  @Test
  void loadForMillisShouldLoadAssets() {
    String texture1 = "test/files/tree.png";
    String texture2 = "test/files/missing.png";
    String texture3 = "test/files/heart.png";
    String[] textures = {texture1, texture2, texture3};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadTextures(textures);
    while (!resourceService.loadForMillis(1)) {
      ;
    }

    verify(assetManager).load(texture1, Texture.class);
    verify(assetManager).load(texture2, Texture.class);
    verify(assetManager).load(texture3, Texture.class);

    assertTrue(assetManager.contains(texture1, Texture.class));
    assertFalse(assetManager.contains(texture2, Texture.class));
    assertTrue(assetManager.contains(texture3, Texture.class));
  }

  @Test
  void shouldContainAndGetAssets() {
    String texture1 = "test/files/tree.png";
    String texture2 = "test/files/missing.png";
    String[] textures = {texture1, texture2};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadTextures(textures);
    resourceService.loadAll();

    assertTrue(resourceService.containsAsset(texture1, Texture.class));
    assertFalse(resourceService.containsAsset(texture2, Texture.class));

    verify(assetManager).contains(texture1, Texture.class);
    verify(assetManager).contains(texture2, Texture.class);

    assertNotNull(resourceService.getAsset(texture1, Texture.class));
  }

  @Test
  void shouldLoadTextures() {
    String asset1 = "test/files/tree.png";
    String asset2 = "test/files/heart.png";
    String[] textures = {asset1, asset2};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadTextures(textures);
    verify(assetManager).load(asset1, Texture.class);
    verify(assetManager).load(asset2, Texture.class);
  }

  @Test
  void shouldLoadTextureAtlases() {
    String asset1 = "test/files/test.atlas";
    String asset2 = "test/files/test2.atlas";
    String[] textures = {asset1, asset2};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadTextureAtlases(textures);
    verify(assetManager).load(asset1, TextureAtlas.class);
    verify(assetManager).load(asset2, TextureAtlas.class);
  }

  @Test
  void shouldLoadSounds() {
    String asset1 = "test/files/sound1.ogg";
    String asset2 = "test/files/sound2.ogg";
    String[] textures = {asset1, asset2};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadSounds(textures);
    verify(assetManager).load(asset1, Sound.class);
    verify(assetManager).load(asset2, Sound.class);
  }

  @Test
  void shouldLoadMusic() {
    String asset1 = "test/files/sound1.ogg";
    String asset2 = "test/files/sound2.ogg";
    String[] textures = {asset1, asset2};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadMusic(textures);
    verify(assetManager).load(asset1, Music.class);
    verify(assetManager).load(asset2, Music.class);
  }

  @Test
  void shouldLoadSound() {
    String asset1 = "sounds/win.mp3";
    String asset2 = "sounds/lose.mp3";
    String[] textures = {asset1, asset2};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadMusic(textures);
    verify(assetManager).load(asset1, Music.class);
    verify(assetManager).load(asset2, Music.class);
  }

  @Test
  void shouldLoadClickSound() {
    String asset1 = "sounds/click.mp3";
    String[] textures = {asset1};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadMusic(textures);
    verify(assetManager).load(asset1, Music.class);
  }

  /**
   * Test whether the background music for the loading screens, intro screen and level four area can be successfully loaded.
   */
  @Test
  void shouldLoadBackgroundMusic() {
    String asset1 = "sounds/loading_background_music.mp3";
    String asset2 = "sounds/intro_story_background_music.mp3";
    String asset3 = "sounds/level4_background_music_1.mp3";
    String[] textures = {asset1, asset2, asset3};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadMusic(textures);
    verify(assetManager).load(asset1, Music.class);
    verify(assetManager).load(asset2, Music.class);
    verify(assetManager).load(asset3, Music.class);
  }
}
