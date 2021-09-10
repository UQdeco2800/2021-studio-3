package com.deco2800.game.components;

import com.deco2800.game.areas.ForestGameArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private ForestGameArea area;

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
/*
 * Returns the Max health.
 *
 * @return entity's Max health
 */
public int getMaxHealth() {
  return this.maxHealth;
}

  /*
   * Returns the Max health.
   *
   * @return entity's Max health
   */
  public int setMaxHealth(int health) {
    return this.maxHealth = health;
  }

  /**
   * Sets the entity's health. Health has a minimum bound of 0. If the
   * players' health reaches 0, a loss pop-up menu is triggered.
   *
   * @param health health
   */
  public void setHealth(int health) {
    this.health = Math.max(health, 0);
    if (entity != null) {
      entity.getEvents().trigger("updateHealth", this.health);
      if (isDead()) {
        entity.getEvents().trigger("playerDeath");

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
