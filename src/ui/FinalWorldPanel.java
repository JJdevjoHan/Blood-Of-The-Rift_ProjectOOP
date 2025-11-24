package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import java.awt.Insets;

import java.util.Objects;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class FinalWorldPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private MainFrame mainFrame;
    private Character player;
    private final JTextArea battleLog = new JTextArea(10, 40);
    private final JLabel playerLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel mobLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel("", SwingConstants.LEFT);

    /*
    private boolean inWorld = true;
    private final JButton northBtn = new JButton("North");
    private final JButton eastBtn = new JButton("East");
    private final JButton southBtn = new JButton("South");
    private final JButton westBtn = new JButton("West");
    private final JButton inspectBtn = new JButton("Inspect Class");
*/
    private final JButton skill1Btn = new JButton();
    private final JButton skill2Btn = new JButton();
    private final JButton skill3Btn = new JButton();
    private World5Boss currentMob;
    private final Random rng = new Random();
    private int mobsDefeated = 0;

    public FinalWorldPanel(MainFrame mainFrame) {
    	this.mainFrame = Objects.requireNonNull(mainFrame, "mainFrame must not be null");
        initialize();
    }

	private void initialize() {
		/*
        // Add background image support (update path for FinalWorld)
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/images/backgroundpic/finalworld.png"));
        try {
            URL url = getClass().getResource("/images/backgroundpic/finalworld.png");
            if (url != null) {
                bgIcon = new ImageIcon(url);
            } else {
                // Fallback: Use a solid color or default image
                bgIcon = new ImageIcon(); // Empty icon, or load a default
                System.err.println("Warning: Background image not found. Using fallback.");
            }
        } catch (Exception e) {
            bgIcon = new ImageIcon(); // Fallback
            e.printStackTrace();
        }
        
        Image bgImage = bgIcon.getImage();
*/
		setLayout(new BorderLayout());
        setOpaque(false);

        // Status bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setOpaque(true);
        statusBar.setBackground(new Color(30, 30, 30));
        statusBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusBar.add(statusLabel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.NORTH);

        // Center area: player, log, mob
        JPanel midPanel = new JPanel(new GridBagLayout());
        midPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel playerIconPanel = new JPanel();
        playerIconPanel.setPreferredSize(new Dimension(180, 220));
        playerIconPanel.setBackground(new Color(0, 0, 0, 120));
        playerIconPanel.add(playerLabel);
        gbc.gridx = 0; gbc.gridy = 0;
        midPanel.add(playerIconPanel, gbc);

        JScrollPane battleScroll = new JScrollPane(battleLog);
        battleScroll.setPreferredSize(new Dimension(380, 220));
        battleLog.setBackground(Color.BLACK);
        battleLog.setForeground(Color.WHITE);
        battleLog.setEditable(false);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        gbc.gridx = 1;
        midPanel.add(battleScroll, gbc);

        JPanel mobIconPanel = new JPanel();
        mobIconPanel.setPreferredSize(new Dimension(180, 220));
        mobIconPanel.setBackground(new Color(0, 0, 0, 120));
        mobIconPanel.add(mobLabel);
        gbc.gridx = 2;
        midPanel.add(mobIconPanel, gbc);

        add(midPanel, BorderLayout.CENTER);

        // Bottom area: skills + dpad
        JPanel bottom = new JPanel(new GridLayout(2, 1));
        bottom.setOpaque(false);

        JPanel skillRow = new JPanel(new GridLayout(1, 3, 20, 0));
        skillRow.setOpaque(false);
        skillRow.setBorder(new EmptyBorder(10, 30, 10, 30));
        skill1Btn.setFont(new Font("Times New Roman", Font.BOLD, 14));
        skill2Btn.setFont(new Font("Times New Roman", Font.BOLD, 14));
        skill3Btn.setFont(new Font("Times New Roman", Font.BOLD, 14));
        skillRow.add(skill1Btn); skillRow.add(skill2Btn); skillRow.add(skill3Btn);
        bottom.add(skillRow);

        /*
        JPanel dpad = new JPanel(new GridBagLayout());
        dpad.setOpaque(false);
        GridBagConstraints d = new GridBagConstraints();
        d.insets = new Insets(4, 4, 4, 4);

        // Use action commands to carry the logical direction (safer than name/text)
        northBtn.setActionCommand("North");
        eastBtn.setActionCommand("East");
        southBtn.setActionCommand("South");
        westBtn.setActionCommand("West");
        northBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        eastBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        southBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        westBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        
        northBtn.setEnabled(false);
        southBtn.setEnabled(false);
        eastBtn.setEnabled(false);
        westBtn.setEnabled(false);


        d.gridx = 1; d.gridy = 0; dpad.add(northBtn, d);
        d.gridx = 0; d.gridy = 1; dpad.add(westBtn, d);
        d.gridx = 2; d.gridy = 1; dpad.add(eastBtn, d);
        d.gridx = 1; d.gridy = 2; dpad.add(southBtn, d);
        
        */

        add(bottom, BorderLayout.SOUTH);
        setSkillButtonsEnabled(false);
    }

    public void setPlayer(String name, String className) {
        switch (className) {
            case "Mage":
                player = new Mage(name);
                break;
            case "Paladin":
                player = new Paladin(name);
                break;
            default:
                player = new Warrior(name);
                break;
        }
        setupSkillButtons(); // Now that player is set, configure buttons
        updateStatus();
        battleLog.append("You face the last horrors of the rift...\n\n");
        spawnNextMob(); // Start with the first boss
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
    
    private void setSkillButtonsEnabled(boolean enabled) {
        skill1Btn.setEnabled(enabled);
        skill2Btn.setEnabled(enabled);
        skill3Btn.setEnabled(enabled);
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

            if (currentMob instanceof DemonLord) {
                Object[] options = {"Go Home", "Play Again"};
                int ch = JOptionPane.showOptionDialog(
                        this,
                        "The portal Back to Reality is open.\n\nWhat will you do?",
                        "Portal Opened",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (ch == 0) {
                    battleLog.append("You step into the portal... GoodBye!\n\n");
                    mainFrame.showPanel("lavaWorldPanel"); // Assuming next world; adjust if needed
                } else {
                    battleLog.append("You decide to return home and play again.\n\n");
                    mainFrame.showPanel("introPanel");
                }
            }
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
            mainFrame.showPanel("introPanel"); // Switch to intro on defeat
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
        if (player != null) {
            statusLabel.setText("<html><b>Name:</b> " + player.name +
                    "<br/><b>Class:</b> " + player.className +
                    "<br/><b>HP:</b> <span style='color:green;'>" + player.hp + "</span> / " + player.maxHp +
                    "<br/><b>MP:</b> <span style='color:blue;'>" + player.mana + "</span></html>");

            playerLabel.setText("<html>" + player.className +
                    "<br/>HP: <span style='color:green;'>" + player.hp + "</span>  " +
                    "MP: <span style='color:blue;'>" + player.mana + "</span></html>");
        }

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
}



// Moved outside the panel for reusability
class Kyros extends World5Boss {
    Kyros() {
        super("General | Kyros", 160, 25);
    }
}

class DemonLord extends World5Boss {
    DemonLord() {
        super("Demon Lord", 300, 35);
    }
}