package ui;

import javax.swing.JTextArea;
import java.util.Random;

public class Mage extends Character {

    private final Random random = new Random();

    public Mage(String playerName) {
        super(playerName, "Mage", 120, 150);
    }

 
    @Override
    public String useSkill(int choice, World1Mob target) {
        int damage;
        String message;
        String critMessage = "";

        switch (choice) {
            case 1: // Frost Bolt
                damage = getDamageWithBuff((int)(Math.random() * 11));
                mana += 10;
                if (mana > maxMana) mana = maxMana;

                if (random.nextInt(100) < 20) {
                    damage = (int)(damage * 1.5);
                    critMessage = " >> CRITICAL HIT! <<";
                }

                target.takeDamage(damage);
                message = name + " used Frost Bolt!" + critMessage + " Deals " + damage + " damage.";
                return message;

            case 2: // Rune Burst
                if (mana >= 20) {
                    damage = getDamageWithBuff(11 + (int)(Math.random() * 10));
                    mana -= 20;

                    if (random.nextInt(100) < 20) {
                        damage = (int)(damage * 1.5);
                        critMessage = " >> CRITICAL HIT! <<";
                    }

                    target.takeDamage(damage);
                    message = name + " used Rune Burst!" + critMessage + " Deals " + damage + " damage.";
                    return message;
                } else return "Not enough mana!";

            case 3: // Lightstorm
                if (mana >= 30) {
                    damage = getDamageWithBuff(21 + (int)(Math.random() * 15));
                    mana -= 30;

                    if (random.nextInt(100) < 20) {
                        damage = (int)(damage * 1.5);
                        critMessage = " >> CRITICAL HIT! <<";
                    }

                    target.takeDamage(damage);
                    message = name + " used Lightstorm!" + critMessage + " Deals " + damage + " damage.";
                    return message;
                } else return "Not enough mana!";

            default:
                return "Invalid skill choice. Turn skipped.";
        }
    }

   
    @Override
    protected void displaySkillsSwing(JTextArea battleLog) {
        battleLog.setText(
            "Mage Skills:\n" +
            "1. Frost Bolt (0 Mana) - Deals 0–10 damage. +10 Mana.\n" +
            "2. Rune Burst (20 Mana) - Deals 11–20 damage.\n" +
            "3. Lightstorm (30 Mana) - Deals 21–35 damage.\n"
        );
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }

}
