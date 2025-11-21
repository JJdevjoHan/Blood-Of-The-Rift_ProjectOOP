package ui;

import javax.swing.JTextArea;
import java.util.Random;

public class Warrior extends Character {

    private final Random random = new Random();

    public Warrior(String playerName) {
        super(playerName, "Warrior", 180, 80);
    }

    @Override
    public String useSkill(int choice, World1Mob target) {
        int damage;
        String message;
        String critMessage = "";

        switch (choice) {
            case 1: // Stone Slash
                damage = getDamageWithBuff((int)(Math.random() * 13));
                mana += 10;
                if (mana > maxMana) mana = maxMana;

                if (random.nextInt(100) < 20) {
                    damage = (int)(damage * 1.5);
                    critMessage = " >> CRITICAL HIT! <<";
                }

                target.takeDamage(damage);
                message = name + " used Stone Slash!" + critMessage + " Deals " + damage + " damage.";
                return message;

            case 2: // Flame Strike
                if (mana >= 20) {
                    damage = getDamageWithBuff(13 + (int)(Math.random() * 10));
                    mana -= 20;

                    if (random.nextInt(100) < 20) {
                        damage = (int)(damage * 1.5);
                        critMessage = " >> CRITICAL HIT! <<";
                    }

                    target.takeDamage(damage);
                    message = name + " used Flame Strike!" + critMessage + " Deals " + damage + " damage.";
                    return message;
                } else return "Not enough mana!";

            case 3: // Earthquake Blade
                if (mana >= 30) {
                    damage = getDamageWithBuff(23 + (int)(Math.random() * 13));
                    mana -= 30;

                    if (random.nextInt(100) < 20) {
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

    /**
     * Display Warrior skills in the Swing JTextArea
     */
    @Override
    protected void displaySkillsSwing(JTextArea battleLog) {
        battleLog.setText(
            "Warrior Skills:\n" +
            "1. Stone Slash (0 Mana) - Deals 0–12 damage. +10 Mana.\n" +
            "2. Flame Strike (20 Mana) - Deals 13–22 damage.\n" +
            "3. Earthquake Blade (30 Mana) - Deals 23–35 damage.\n"
        );
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }

    
}
