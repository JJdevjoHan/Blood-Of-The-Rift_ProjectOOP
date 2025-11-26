package ui;

import java.util.Random;

public class Paladin extends Character {

    public Paladin(String name, Random rng) {
        super(name, "Paladin", 220, 120, rng);
    }

    @Override
    public String useSkill(int choice, World1Mob target) {
        int damage;
        String message;
        String critMessage = "";

        switch (choice) {
            case 1: // Shield Bash
                damage = getDamageWithBuff(rng.nextInt(9)); // 0-8
                mana += 10;
                if (mana > maxMana) mana = maxMana;

                if (rng.nextInt(100) < 20) {
                    damage = (int)(damage * 1.5);
                    critMessage = " >> CRITICAL HIT! <<";
                }

                target.takeDamage(damage);
                message = name + " used Shield Bash!" + critMessage + " Deals " + damage + " damage.";
                return message;

            case 2: // Radiant Guard
                if (mana >= 20) {
                    mana -= 20;
                    return name + " used Radiant Guard! Damage taken will be reduced.";
                } else return "Not enough mana!";

            case 3: // Holy Renewal
                if (mana >= 30) {
                    int healAmount = 20 + rng.nextInt(16); // 20-35
                    mana -= 30;
                    this.hp += healAmount;
                    if (this.hp > this.maxHp) this.hp = this.maxHp;
                    return name + " used Holy Renewal! You heal for " + healAmount + " HP. HP: " + this.hp;
                } else return "Not enough mana!";

            default:
                return "Invalid skill choice.";
        }
    }
}