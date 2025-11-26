package ui;

import java.util.Random;

public class Mage extends Character {

    public Mage(String name, Random rng) {
        super(name, "Mage", 120, 150, rng);
    }

    @Override
    public String useSkill(int choice, World1Mob target) {
        int damage;
        String message;
        String critMessage = "";

        switch (choice) {
            case 1: // Frost Bolt
                damage = getDamageWithBuff(rng.nextInt(11)); // 0-10
                mana += 10;
                if (mana > maxMana) mana = maxMana;

                if (rng.nextInt(100) < 20) {
                    damage = (int)(damage * 1.5);
                    critMessage = " >> CRITICAL HIT! <<";
                }

                target.takeDamage(damage);
                message = name + " used Frost Bolt!" + critMessage + " Deals " + damage + " damage.";
                return message;

            case 2: // Rune Burst
                if (mana >= 20) {
                    damage = getDamageWithBuff(11 + rng.nextInt(10)); // 11-20
                    mana -= 20;

                    if (rng.nextInt(100) < 20) {
                        damage = (int)(damage * 1.5);
                        critMessage = " >> CRITICAL HIT! <<";
                    }

                    target.takeDamage(damage);
                    message = name + " used Rune Burst!" + critMessage + " Deals " + damage + " damage.";
                    return message;
                } else return "Not enough mana!";

            case 3: // Lightstorm
                if (mana >= 30) {
                    damage = getDamageWithBuff(21 + rng.nextInt(15)); // 21-35
                    mana -= 30;

                    if (rng.nextInt(100) < 20) {
                        damage = (int)(damage * 1.5);
                        critMessage = " >> CRITICAL HIT! <<";
                    }

                    target.takeDamage(damage);
                    message = name + " used Lightstorm!" + critMessage + " Deals " + damage + " damage.";
                    return message;
                } else return "Not enough mana!";

            default:
                return "Invalid skill choice.";
        }
    }
}