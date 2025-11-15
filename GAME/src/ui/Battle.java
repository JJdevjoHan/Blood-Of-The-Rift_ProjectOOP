package ui;

import javax.swing.*;
import java.util.Random;

public class Battle {
    private final Character player;
    private final World1Mob mob;
    private final JTextArea battleLog;
    private final JLabel playerLabel;
    private final JLabel mobLabel;
    private final Random rng = new Random();

    Battle(Character player, World1Mob mob,
           JTextArea battleLog, JLabel playerLabel, JLabel mobLabel) {
        this.player = player;
        this.mob = mob;
        this.battleLog = battleLog;
        this.playerLabel = playerLabel;
        this.mobLabel = mobLabel;
    }

    boolean start() {
        while(player.isAlive() && mob.isAlive()) {
            playerTurn();
            if (!mob.isAlive()) break;
            mobTurn();
        }
        return player.isAlive();
    }

    private void playerTurn() {
        appendLog("Your turn. Choose an action:");
        String[] options = {"Attack","Spell","Flee"};
        int choice = JOptionPane.showOptionDialog(null, "Choose action:", "Battle",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if(choice==0) { // Attack
            int dmg = 5 + rng.nextInt(6);
            mob.hp -= dmg;
            appendLog("You attack " + mob.name + " for " + dmg + " damage!");
        } else if(choice==1) { // Spell
            if(player.mana >= 10) {
                player.mana -= 10;
                int dmg = (player.className.equals("Mage") ? 25 : 12) + rng.nextInt(8);
                mob.hp -= dmg;
                appendLog("You cast a spell on " + mob.name + " for " + dmg + " damage!");
            } else appendLog("Not enough mana!");
        } else { // Flee
            if(rng.nextInt(100)<40) { appendLog("You fled!"); player.hp = 0; }
            else appendLog("Failed to flee!");
        }

        updateStatus();
    }

    private void mobTurn() {
        int dmg = mob.damage + rng.nextInt(4);
        player.hp -= dmg;
        appendLog(mob.name + " attacks you for " + dmg + " damage!");
        updateStatus();
    }

    private void updateStatus() {
        playerLabel.setText(player.className + " HP: " + player.hp + "/" + player.maxHp);
        mobLabel.setText(mob.name + " HP: " + mob.hp + "/" + mob.maxHp);
    }

    private void appendLog(String msg) {
        battleLog.append(msg + "\n");
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }
}
