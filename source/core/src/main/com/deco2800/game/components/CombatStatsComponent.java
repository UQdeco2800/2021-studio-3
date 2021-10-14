package com.deco2800.game.components;

import com.deco2800.game.SaveData.SaveData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private int health;
  private int maxHealth;
  private int baseAttack;

  /* Whether or not the player is currently invincible (timed buff) */
  private boolean invincible = false;

  /* Whether or not the player is currently taking double damage (timed buff) */
  private boolean doubleHurt = false;

  public CombatStatsComponent(int health, int baseAttack) {
    setMaxHealth(health);
    setHealth(health);
    setBaseAttack(baseAttack);
  }

  /**
   * Returns true if the entity's has 0 health, otherwise false.
   *
   * @return is player dead
   */
  public Boolean isDead() {
    return health == 0;
  }

  /**
   * Returns the entity's health.
   *
   * @return entity's health
   */
  public int getHealth() {
    return health;
  }

  /**
   * Returns the Max health.
   *
   * @return entity's Max health
   */
  public int getMaxHealth() {
    return this.maxHealth;
  }

  /**
   * Returns the Max health.
   *
   * @return entity's Max health
   */
  public int setMaxHealth(int health) {
    return this.maxHealth = health;
  }

  /**
   * Triggers' the players invincibility. When invincible, the player will
   * not take damage.
   *
   * @param invincible whether to turn invincibility on or off. 'True' is on,
   *                   'False' is off.
   * */
  public void setInvincibility(boolean invincible) {
    this.invincible = invincible;
  }

  /**
   * Triggers the player taking double damage upon being hit.
   *
   * @param hurtStatus whether to turn double hurt on or off. 'True' is on,
   *                   'False' is off
   * */
  public void setDoubleHurt(boolean hurtStatus) {
    this.doubleHurt = hurtStatus;
  }

  /**
   * Sets the players health to the maximum.
   * */
  public void setFullHeal() {
    this.health = getMaxHealth();
    setHealth(this.health);
  }

  /**
   * Sets the entity's health. Health has a minimum bound of 0.
   *
   * If the players' health reaches 0, a loss pop-up menu is triggered.
   * If the player is Invincible (timed buff), then the player's health will
   *    not decrease.
   * If the player is under the effects of Double Hurt (timed buff), then the
   *    player will take double damage when hit.
   *
   * @param health health
   */
  public void setHealth(int health) {
    if (!invincible) {
      if (health >= 0) {
        this.health = (doubleHurt && health < this.health) ?
                (this.health - (2 * (this.health - health))) : health;

        // If the double hurt caused negative health, set to 0.
        this.health = Math.max(this.health, 0);
      } else {
        this.health = 0;
      }
      if (entity != null) {
        // Updates the player state and animations when healed
        entity.getEvents().trigger("playerStatusAnimation");
        if (isDead()) {
          if (entity.getComponent(LivesComponent.class) != null) {
            if (entity.getComponent(LivesComponent.class).getLives() == 0) {
              entity.getEvents().trigger("playerFinalDeath");
            }
            entity.getEvents().trigger("playerDeath");
          }
        }
      }
    }
  }

  /**
   * Adds to the player's health. The amount added can be negative.
   *
   * @param health health to add
   */
  public void addHealth(int health) {
    setHealth(this.health + health);
  }

  /**
   * Decrease the player's health. The amount added can be negative.
   *
   * @param health health to add
   */
  public void decreaseHealth(int health) {
    setHealth(this.health - health);
  }

  /**
   * Returns the entity's base attack damage.
   *
   * @return base attack damage
   */
  public int getBaseAttack() {
    return baseAttack;
  }

  /**
   * Sets the entity's attack damage. Attack damage has a minimum bound of 0.
   *
   * @param attack Attack damage
   */
  public void setBaseAttack(int attack) {
    if (attack >= 0) {
      this.baseAttack = attack;
    } else {
      logger.error("Can not set base attack to a negative attack value");
    }
  }

  public void hit(CombatStatsComponent attacker) {
    int newHealth = getHealth() - attacker.getBaseAttack();
    setHealth(newHealth);
  }
}
