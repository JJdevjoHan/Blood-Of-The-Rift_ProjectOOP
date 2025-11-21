package ui;

import javax.swing.JTextArea;
import java.util.Random;

public class Paladin extends Character {

    private final Random random = new Random();

    public Paladin(String playerName) {
        super(playerName, "Paladin", 220, 120);
    }

    @Override
    public String useSkill(int choice, World1Mob target) {
        int damage;
        String message;
        String critMessage = "";

        switch (choice) {
            case 1: // Shield Bash
                damage = getDamageWithBuff((int)(Math.random() * 9));
                mana += 10;
                if (mana > maxMana) mana = maxMana;

                if (random.nextInt(100) < 20) {
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
                    int healAmount = 20 + (int)(Math.random() * 16);
                    mana -= 30;
                    this.hp += healAmount;
                    if (this.hp > this.maxHp) this.hp = this.maxHp;
                    return name + " used Holy Renewal! You heal for " + healAmount + " HP. HP: " + this.hp;
                } else return "Not enough mana!";

            default:
                return "Invalid skill choice. Turn skipped.";
        }
    }

    /**
     * Display skills in a JTextArea (Swing UI).
     * You can also use this to update buttons dynamically.
     */
    @Override
    protected void displaySkillsSwing(JTextArea battleLog) {
        String[] skills = {
                "[1] Shield Bash (0-8 Dmg, +10 Mana)",
                "[2] Radiant Guard (Reduce damage, 20 Mana)",
                "[3] Holy Renewal (Heal 20-35 HP, 30 Mana)"
        };
        battleLog.append(name + "'s Skills:\n");
        for (String skill : skills) {
            battleLog.append(skill + "\n");
        }
        battleLog.append("\n");
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }

  
}
