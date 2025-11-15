package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Random;

public class FinalWorld extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JTextArea battleLog = new JTextArea(10, 40);
    private final JLabel playerLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel mobLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel("", SwingConstants.LEFT);

    private final JButton inspectBtn = new JButton("Inspect Class");
    private final JButton skill1Btn = new JButton();
    private final JButton skill2Btn = new JButton();
    private final JButton skill3Btn = new JButton();

    private Character player;
    private World5Boss currentMob;
    private final Random rng = new Random();
    private int mobsDefeated = 0;

    public FinalWorld(String playerName, String selectedClass) {
        setTitle("Final World");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        switch (selectedClass) {
            case "Mage": player = new Mage(playerName); break;
            case "Paladin": player = new Paladin(playerName); break;
            default: player = new Warrior(playerName); break;
        }

        initUI();
        updateStatus();
        battleLog.append("You face the last horrors of the rift...\n\n");
        spawnNextMob();
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout(8,8));
        main.setBorder(new EmptyBorder(12,12,12,12));
        main.setBackground(Color.BLACK);

        JPanel center = new JPanel(new GridLayout(1,2,10,0));
        center.setBackground(Color.DARK_GRAY);

        playerLabel.setForeground(Color.WHITE);
        playerLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mobLabel.setForeground(Color.WHITE);
        mobLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        mobLabel.setHorizontalAlignment(SwingConstants.CENTER);

        center.add(playerLabel);
        center.add(mobLabel);

        battleLog.setEditable(false);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        battleLog.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        battleLog.setBackground(Color.BLACK);
        battleLog.setForeground(Color.WHITE);

        JPanel centerWrapper = new JPanel(new BorderLayout(6,6));
        centerWrapper.setBackground(Color.DARK_GRAY);
        centerWrapper.add(center, BorderLayout.CENTER);
        centerWrapper.add(new JScrollPane(battleLog), BorderLayout.SOUTH);

        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(Color.BLACK);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        left.add(statusLabel, BorderLayout.NORTH);
        inspectBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        left.add(inspectBtn, BorderLayout.SOUTH);

        JPanel skillPanel = new JPanel(new GridLayout(1,3,8,0));
        skillPanel.setBackground(Color.BLACK);
        skill1Btn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        skill2Btn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        skill3Btn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        skillPanel.add(skill1Btn);
        skillPanel.add(skill2Btn);
        skillPanel.add(skill3Btn);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.BLACK);
        southPanel.add(skillPanel, BorderLayout.NORTH);

        main.add(left, BorderLayout.WEST);
        main.add(centerWrapper, BorderLayout.CENTER);
        main.add(southPanel, BorderLayout.SOUTH);

        setContentPane(main);

        setupSkillButtons();

        inspectBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Class: " + player.className + "\nPlayer: " + player.name));
    }

    private void setupSkillButtons() {
        if (player instanceof Warrior) {
            skill1Btn.setText("Stone Slash (0-12 Dmg, +10 Mana)");
            skill2Btn.setText("Flame Strike (13-22 Dmg, 20 Mana)");
            skill3Btn.setText("Earthquake Blade (23-35 Dmg, 30 Mana)");
        } else if (player instanceof Mage) {
            skill1Btn.setText("Frost Bolt (0-10 Dmg, +10 Mana)");
            skill2Btn.setText("Rune Burst (11-20 Dmg, 20 Mana)");
            skill3Btn.setText("Lightstorm (21-35 Dmg, 30 Mana)");
        } else if (player instanceof Paladin) {
            skill1Btn.setText("Shield Bash (0-8 Dmg, +10 Mana)");
            skill2Btn.setText("Radiant Guard (Reduces damage taken, 20 Mana)");
            skill3Btn.setText("Holy Renewal (Heal 20-35 HP, 30 Mana)");
        }

        skill1Btn.addActionListener(e -> doSkill(1));
        skill2Btn.addActionListener(e -> doSkill(2));
        skill3Btn.addActionListener(e -> doSkill(3));
    }

    private void doSkill(int choice) {
        if (currentMob == null) {
            battleLog.append("No enemy to attack!\n\n");
            return;
        }

        String result = player.useSkill(choice, currentMob);
        battleLog.append(result + "\n\n");

        if (!currentMob.isAlive()) {
            battleLog.append("You defeated " + currentMob.name + "!\n\n");
            mobsDefeated++;
            currentMob = null;
            spawnNextMob();
        } else {
            int mobDmg = currentMob.damage + rng.nextInt(6);
            player.hp -= mobDmg;
            if (player.hp < 0) player.hp = 0;
            battleLog.append(currentMob.name + " attacks for " + mobDmg + " damage!\n\n");
        }

        updateStatus();

        if (player.hp <= 0) {
            battleLog.append("You have been defeated...\n");
            disableCombat();
            JOptionPane.showMessageDialog(this, "Game Over", "Defeat", JOptionPane.PLAIN_MESSAGE);
            dispose();
        }
    }

    private void spawnNextMob() {
        switch (mobsDefeated) {
            case 0:
                currentMob = new Kyros();
                battleLog.append("A Mini Boss appears!\n\n");
                break;
            case 1:
                currentMob = new DemonLord();
                battleLog.append("The Final Boss appears!\n\n");
                break;
            case 2:
                battleLog.append("You have defeated all enemies! Victory!\n");
                disableCombat();
                break;
        }
        updateStatus();
    }

    private void updateStatus() {
        statusLabel.setText("<html><b>Name:</b> " + player.name +
                "<br/><b>Class:</b> " + player.className +
                "<br/><b>HP:</b> <span style='color:green;'>" + player.hp + "</span> / " + player.maxHp +
                "<br/><b>MP:</b> <span style='color:blue;'>" + player.mana + "</span></html>");

        playerLabel.setText("<html>" + player.className +
                "<br/>HP: <span style='color:green;'>" + player.hp + "</span>  " +
                "MP: <span style='color:blue;'>" + player.mana + "</span></html>");

        if (currentMob != null)
            mobLabel.setText("<html>" + currentMob.name +
                    "<br/>HP: <span style='color:red;'>" + currentMob.hp + "</span></html>");
        else
            mobLabel.setText("");
    }

    private void disableCombat() {
        skill1Btn.setEnabled(false);
        skill2Btn.setEnabled(false);
        skill3Btn.setEnabled(false);
    }

    // Define MiniBoss and FinalBoss
    static class Kyros extends World5Boss { Kyros() { super("Kyros", 150, 20); } }
    static class DemonLord extends World5Boss { DemonLord() { super("DemonLord", 300, 30); } }

    static abstract class World4Mob {
        String name; int hp, damage;
        World4Mob(String name, int hp, int dmg) { this.name=name; this.hp=hp; this.damage=dmg; }
        boolean isAlive() { return hp>0; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FinalWorld("Hero","Warrior").setVisible(true));
    }
}
