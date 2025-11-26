package ui;

import java.util.Random;

public class Warrior extends Character {

    public Warrior(String name, Random rng) {
        super(name, "Warrior", 180, 80, rng);
    }

    @Override
    public String useSkill(int choice, World1Mob target) {
        int damage;
        String message;
        String critMessage = "";

        switch (choice) {
            case 1: // Stone Slash
                damage = getDamageWithBuff(rng.nextInt(13)); // 0-12
                mana += 10;
                if (mana > maxMana) mana = maxMana;

                if (rng.nextInt(100) < 20) {
                    damage = (int)(damage * 1.5);
                    critMessage = " >> CRITICAL HIT! <<";
                }

                target.takeDamage(damage);
                message = name + " used Stone Slash!" + critMessage + " Deals " + damage + " damage.";
                return message;

            case 2: // Flame Strike
                if (mana >= 20) {
                    damage = getDamageWithBuff(13 + rng.nextInt(10)); // 13-22
                    mana -= 20;

                    if (rng.nextInt(100) < 20) {
                        damage = (int)(damage * 1.5);
                        critMessage = " >> CRITICAL HIT! <<";
                    }

                    target.takeDamage(damage);
                    message = name + " used Flame Strike!" + critMessage + " Deals " + damage + " damage.";
                    return message;
                } else return "Not enough mana!";

            case 3: // Earthquake Blade
                if (mana >= 30) {
                    damage = getDamageWithBuff(23 + rng.nextInt(13)); // 23-35
                    mana -= 30;

                    if (rng.nextInt(100) < 20) {
                        damage = (int)(damage * 1.5);
                        critMessage = " >> CRITICAL HIT! <<";
                    }

                    target.takeDamage(damage);
                    message = name + " used Earthquake Blade!" + critMessage + " Deals " + damage + " damage.";
                    return message;
                } else return "Not enough mana!";

            default:
                return "Invalid skill choice.";
        }
    }
}