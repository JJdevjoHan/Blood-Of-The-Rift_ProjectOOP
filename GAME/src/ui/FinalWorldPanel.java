package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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

    private final JButton northBtn = new JButton("North");
    private final JButton eastBtn = new JButton("East");
    private final JButton southBtn = new JButton("South");
    private final JButton westBtn = new JButton("West");
    private final JButton inspectBtn = new JButton("Inspect Class");

    private final JButton skill1Btn = new JButton();
    private final JButton skill2Btn = new JButton();
    private final JButton skill3Btn = new JButton();

    private World5Boss currentMob;
    private final Random rng = new Random();
    private int mobsDefeated = 0;

    public FinalWorldPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
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
        JPanel main = new JPanel(new BorderLayout(8, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        
         
        main.setBorder(new EmptyBorder(12, 12, 12, 12));
        main.setOpaque(false); // Allow background to show

        // Center panel: player & mob
        JPanel center = new JPanel(new GridLayout(1, 2, 10, 0));
        center.setBackground(Color.DARK_GRAY);
        center.setOpaque(false);

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

        JPanel centerWrapper = new JPanel(new BorderLayout(6, 6));
        centerWrapper.setBackground(Color.BLACK);
        centerWrapper.setOpaque(false);
        centerWrapper.add(center, BorderLayout.CENTER);
        centerWrapper.add(new JScrollPane(battleLog), BorderLayout.SOUTH);

        // Status panel
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(Color.BLACK);
        left.setOpaque(false);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        left.add(statusLabel, BorderLayout.NORTH);
        inspectBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        left.add(inspectBtn, BorderLayout.SOUTH);

        // D-pad setup (disabled since no exploration)
        JPanel dpad = new JPanel(new GridBagLayout());
        dpad.setBackground(Color.BLACK);
        dpad.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 1;
        gbc.gridy = 0;
        northBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        northBtn.setEnabled(false); // Disable unused buttons
        dpad.add(northBtn, gbc);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 0;
        gbc.gridy = 1;
        westBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        westBtn.setEnabled(false);
        dpad.add(westBtn, gbc);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 2;
        gbc.gridy = 1;
        eastBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        eastBtn.setEnabled(false);
        dpad.add(eastBtn, gbc);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 1;
        gbc.gridy = 2;
        southBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        southBtn.setEnabled(false);
        dpad.add(southBtn, gbc);

        // Skill panel
        JPanel skillPanel = new JPanel(new GridLayout(1, 3, 8, 0));
        skillPanel.setBackground(Color.BLACK);
        skillPanel.setOpaque(false);
        skill1Btn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        skill1Btn.setForeground(Color.BLACK);
        skill2Btn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        skill2Btn.setForeground(Color.BLACK);
        skill3Btn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        skill3Btn.setForeground(Color.BLACK);
        skillPanel.add(skill1Btn);
        skillPanel.add(skill2Btn);
        skillPanel.add(skill3Btn);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.BLACK);
        southPanel.setOpaque(false);
        southPanel.add(skillPanel, BorderLayout.NORTH);
        southPanel.add(dpad, BorderLayout.SOUTH);

        main.add(left, BorderLayout.WEST);
        main.add(centerWrapper, BorderLayout.CENTER);
        main.add(southPanel, BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER); // Properly add the main panel to this JPanel

        inspectBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Class: " + player.className + "\nPlayer: " + player.name));
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