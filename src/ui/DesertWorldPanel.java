package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class DesertWorldPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final MainFrame mainFrame;
    private Character player;
    private final JTextArea battleLog = new JTextArea(10, 40);
    private final JLabel playerLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel mobLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel("", SwingConstants.LEFT);

    private final JButton northBtn = new JButton("↑");
    private final JButton eastBtn = new JButton("→");
    private final JButton southBtn = new JButton("↓");
    private final JButton westBtn = new JButton("←");

    private final JButton skill1Btn = new JButton();
    private final JButton skill2Btn = new JButton();
    private final JButton skill3Btn = new JButton();

    private World2Mob currentMob;
    private boolean inWorld = true;
    private final Random rng = new Random();
    private String reservedGiantWormDirection;
    private int mobsDefeated = 0;
    private int stepsTaken = 0;
    private boolean chestFound = false;
    private final int STEPS_FOR_CHEST = 2;
    private final Set<String> clearedDirections = new HashSet<>();
    private final Set<String> availableDirections = new HashSet<>();
    private final String[] directions = {"North", "East", "South", "West"};
    private final Map<String, World2Mob> directionMobs = new HashMap<>();
    private String currentDirection;

    public DesertWorldPanel(MainFrame mainFrame) {
        this.mainFrame = Objects.requireNonNull(mainFrame, "mainFrame must not be null");
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setOpaque(true);
        statusBar.setBackground(new Color(30, 30, 30));
        statusBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusBar.add(statusLabel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.NORTH);

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

        JPanel dpad = new JPanel(new GridBagLayout());
        dpad.setOpaque(false);
        GridBagConstraints d = new GridBagConstraints();
        d.insets = new Insets(4, 4, 4, 4);

        northBtn.setActionCommand("North");
        eastBtn.setActionCommand("East");
        southBtn.setActionCommand("South");
        westBtn.setActionCommand("West");
        northBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        eastBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        southBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        westBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));

        ActionListener moveListener = e -> {
            String dir = ((AbstractButton) e.getSource()).getActionCommand();
            if (inWorld) explore(dir);
        };

        Stream.of(northBtn, eastBtn, southBtn, westBtn).forEach(b -> {
            for (ActionListener al : b.getActionListeners()) b.removeActionListener(al);
            b.addActionListener(moveListener);
        });

        d.gridx = 1; d.gridy = 0; dpad.add(northBtn, d);
        d.gridx = 0; d.gridy = 1; dpad.add(westBtn, d);
        d.gridx = 2; d.gridy = 1; dpad.add(eastBtn, d);
        d.gridx = 1; d.gridy = 2; dpad.add(southBtn, d);

        bottom.add(dpad);
        add(bottom, BorderLayout.SOUTH);

        setSkillButtonsEnabled(false);
    }

    public void setPlayer(String name, String className) {
        if (player != null) {
            player.name = name;
            if (!Objects.equals(player.className, className)) {
                player = createCharacter(name, className);
            }
        } else {
            player = createCharacter(name, className);
        }

        configureSkillButtons();
        updateStatus();
        appendToLog("You enter the scorching Desert World.\n\n");
    }
    
    private Character createCharacter(String name, String className) {
        switch (className) {
            case "Mage": 
            	return new Mage(name, rng);
            case "Paladin": 
            	return new Paladin(name, rng);
            default: 
            	return new Warrior(name, rng);
        }
    }


    private void configureSkillButtons() {
        for (ActionListener al : skill1Btn.getActionListeners()) skill1Btn.removeActionListener(al);
        for (ActionListener al : skill2Btn.getActionListeners()) skill2Btn.removeActionListener(al);
        for (ActionListener al : skill3Btn.getActionListeners()) skill3Btn.removeActionListener(al);

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
        if (player == null || currentMob == null) return;

        String skillResult = player.useSkill(choice, currentMob);
        appendToLog(skillResult + "\n");

        updateStatus();

        if (!currentMob.isAlive()) {
            mobsDefeated++;
            appendToLog("You defeated " + currentMob.name + "!\n\n");

            if (currentMob instanceof GiantWorm) {
                appendToLog("The portal to the Snowy Island is open.\n");

                SwingUtilities.invokeLater(() -> {
                    Object[] options = {"Enter Portal", "Return Home"};
                    int choicePortal = JOptionPane.showOptionDialog(
                            this,
                            "The portal to the Snowy Island is open.\n\nWhat will you do?",
                            "Portal Opened",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

                    if (choicePortal == 0) {
                    	mainFrame.showPanel("snowyisland");
                    }
                    else {
                    	mainFrame.showPanel("home");
                    }
                });

                currentMob = null;
                setSkillButtonsEnabled(false);
                updateStatus();
                return;
            }

            if (currentDirection != null) {
                clearedDirections.add(currentDirection);
                availableDirections.remove(currentDirection);
            }

            openRewardChest();
            currentMob = null;
            setSkillButtonsEnabled(false);
            updateStatus();
            return;
        }

        int mobDmg = currentMob.damage + rng.nextInt(6);
        player.hp -= mobDmg;
        if (player.hp < 0) player.hp = 0;
        appendToLog(currentMob.name + " attacks for " + mobDmg + " damage!\n\n");

        updateStatus();

        if (player.hp <= 0) {
            appendToLog("You have been defeated...\n");
            disableMovement();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Game Over", "Defeat", JOptionPane.PLAIN_MESSAGE);
                mainFrame.showPanel("intro");
            });
        }
    }


    private void explore(String direction) {
    if (direction == null) return;

    if (clearedDirections.contains(direction)) {
        appendToLog("There is nothing in that direction! Try another path.\n\n");
        return;
    }

    stepsTaken++;
    appendToLog("You walk " + direction.toLowerCase() + ".\n\n");

    if (!chestFound && stepsTaken >= STEPS_FOR_CHEST) {
        battleLog.append("You wander through the desert and find nothing of interest...\n\n");
        chestFound = true;

        List<World2Mob> mobs = Arrays.asList(new Spider(), new Snake(), new Mummy());
        List<String> dirList = new ArrayList<>(Arrays.asList(directions));
        Collections.shuffle(dirList);

        directionMobs.clear();
        availableDirections.clear();
        for (int i = 0; i < Math.min(3, dirList.size()); i++) {
            directionMobs.put(dirList.get(i), mobs.get(i));
            availableDirections.add(dirList.get(i));
        }

        reservedGiantWormDirection = (dirList.size() >= 4) ? dirList.get(3) : dirList.get(0);

        updateStatus();
        return;
    }

    if (chestFound && currentMob == null) {
        currentDirection = direction;

        boolean spawnGiantWorm = mobsDefeated >= 3 && direction.equals(reservedGiantWormDirection);

        if (spawnGiantWorm) {
            currentMob = new GiantWorm();
            appendToLog("The sands shift... The Giant Worm emerges!!!\n\n");
            JOptionPane.showMessageDialog(this,
                    "GIANT WORM INCOMING!!!", "WARNING! MINIBOSS",
                    JOptionPane.ERROR_MESSAGE);
        } else if (directionMobs.containsKey(direction)){
        	currentMob = directionMobs.get(direction);
        }

        if (currentMob != null) {
            appendToLog("A wild " + currentMob.name + " appears!\n\n");
            setSkillButtonsEnabled(true);
        }
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

        if (currentMob != null) {
            mobLabel.setText("<html>" + currentMob.name +
                    "<br/>HP: <span style='color:red;'>" + currentMob.hp + "</span></html>");
        } else {
            mobLabel.setText("");
        }
    }

    private void disableMovement() {
        northBtn.setEnabled(false);
        southBtn.setEnabled(false);
        eastBtn.setEnabled(false);
        westBtn.setEnabled(false);
        setSkillButtonsEnabled(false);
    }

    private void openRewardChest() {
        appendToLog("You found a reward chest!\n\n");

        int rewardType = rng.nextInt(5);
        switch (rewardType) {
            case 0 -> {
                int hpBoost = 30;
                player.maxHp += hpBoost;
                player.hp += hpBoost;
                if (player.hp > player.maxHp) player.hp = player.maxHp;
                appendToLog("Your maximum HP increased by " + hpBoost + "!\n\n");
                JOptionPane.showMessageDialog(this, "HP INCREASED!", "Reward", JOptionPane.INFORMATION_MESSAGE);
            }
            case 1 -> {
                int dmgBoost = 15;
                player.tempDamage += dmgBoost;
                appendToLog("Your damage increased by " + dmgBoost + " for the next battle!\n\n");
            }
            case 2 -> {
                int manaBoost = 20;
                player.maxMana += manaBoost;
                player.mana += manaBoost;
                if (player.mana > player.maxMana) player.mana = player.maxMana;
                appendToLog("Your maximum Mana increased by " + manaBoost + "!\n\n");
                JOptionPane.showMessageDialog(this, "MANA INCREASED!", "Reward", JOptionPane.INFORMATION_MESSAGE);
            }
            case 3 -> {
                player.hp = player.maxHp;
                appendToLog("You found a healing potion! HP restored to full!\n\n");
                JOptionPane.showMessageDialog(this, "HP RESTORED!", "Reward", JOptionPane.INFORMATION_MESSAGE);
            }
            case 4 -> {
                player.mana = player.maxMana;
                appendToLog("You found a mana elixir! Mana restored to full!\n\n");
                JOptionPane.showMessageDialog(this, "MANA RESTORED!", "Reward", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        updateStatus();
    }

    private void appendToLog(String text) {
        battleLog.append(text);
        // auto-scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollPane sp = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, battleLog);
            if (sp != null) {
                JViewport vp = sp.getViewport();
                if (vp != null) vp.setViewPosition(new Point(0, battleLog.getHeight()));
            }
        });
    }

    public static abstract class Character {
        int tempDamage = 0;
        String name, className;
        int hp, maxHp, mana, maxMana;
        Random rng; 

        Character(String name, String className, int hp, int mana, Random rng) {
            this.name = name;
            this.className = className;
            this.hp = this.maxHp = hp;
            this.mana = this.maxMana = mana;
            this.rng = rng; 
        }

        boolean isAlive() { return hp > 0; }
        abstract String useSkill(int choice, World2Mob target);
    }


    public static class Warrior extends Character {
        Warrior(String n, Random rng) { super(n, "Warrior", 180, 80, rng); }

        @Override
        String useSkill(int choice, World2Mob target) {
            if (target == null) return "No target!";
            int dmg; String msg;
            switch (choice) {
                case 1 -> {
                    dmg = 5 + rng.nextInt(6); dmg += tempDamage;
                    target.hp -= dmg; 
                    mana = Math.min(maxMana, mana + 10);
                    msg = "Stone Slash deals " + dmg;
                }
                case 2 -> {
                    if (mana >= 20) {
                        dmg = 12 + rng.nextInt(8); dmg += tempDamage;
                        target.hp -= dmg; mana -= 20;
                        msg = "Flame Strike deals " + dmg;
                    } else msg = "Not enough mana!";
                }
                case 3 -> {
                    if (mana >= 30) {
                        dmg = 20 + rng.nextInt(15); dmg += tempDamage;
                        target.hp -= dmg; mana -= 30;
                        msg = "Earthquake Blade deals " + dmg;
                    } else msg = "Not enough mana!";
                }
                default -> msg = "Unknown skill.";
            }
            return msg;
        }
    }

    public static class Mage extends Character {
        Mage(String n, Random rng) { super(n, "Mage", 120, 150, rng); }

        @Override
        String useSkill(int choice, World2Mob target) {
            if (target == null) return "No target!";
            int dmg; String msg;
            switch (choice) {
                case 1 -> { 
                	dmg = 5 + rng.nextInt(6); 
                	dmg += tempDamage; 
                	target.hp -= dmg; 
                	mana = Math.min(maxMana, mana + 10); 
                	msg = "Frost Bolt deals " + dmg; 
                	}
                case 2 -> { 
                	if (mana >= 20) { 
                		dmg = 11 + rng.nextInt(10); 
                		dmg += tempDamage; 
                		target.hp -= dmg; 
                		mana -= 20; 
                		msg = "Rune Burst deals " + dmg; 
                	} else 
                		msg = "Not enough mana!"; 
                }
                case 3 -> { 
                	if (mana >= 30) { 
                		dmg = 21 + rng.nextInt(15); 
                		dmg += tempDamage; 
                		target.hp -= dmg; 
                		mana -= 30; 
                		msg = "Lightstorm deals " + dmg; 
                	} else 
                		msg = "Not enough mana!"; 
                }
                default -> msg = "Unknown skill.";
            }
            return msg;
        }
    }

    public static class Paladin extends Character {
        Paladin(String n, Random rng) { super(n, "Paladin", 220, 120, rng); }

        @Override
        String useSkill(int choice, World2Mob target) {
            if (target == null) return "No target!";
            int dmg; String msg;
            switch (choice) {
                case 1 -> { 
                	dmg = 5 + rng.nextInt(8); 
                	dmg += tempDamage; 
                	target.hp -= dmg; 
                	mana = Math.min(maxMana, mana + 10); 
                	msg = "Shield Bash deals " + dmg; 
                }
                case 2 -> { 
                	if (mana >= 20) { 
                		mana -= 20; 
                		msg = "Radiant Guard! Damage reduced."; 
                		} else 
                			msg = "Not enough mana!"; 
                	}
                case 3 -> { 
                	if (mana >= 30) { 
                		int heal = 20 + rng.nextInt(16); 
                		mana -= 30; 
                		hp = Math.min(maxHp, hp + heal); 
                		msg = "Holy Renewal heals " + heal; 
                		} else 
                			msg = "Not enough mana!"; 
                	}
                default -> msg = "Unknown skill.";
            }
            return msg;
        }
    }
    public static abstract class World2Mob 
    {
        String name; int hp, damage;
        World2Mob(String name, int hp, int dmg)
        { 
        	this.name=name; this.hp=hp; this.damage=dmg; 
        }
        boolean isAlive()
        { 
        	return hp>0; 
        }
    }

    public static class Spider extends World2Mob 
    {
    	Spider() 
    	{
    		super("Spider", 40, 12); 
    	} 
    }
    public static class Snake extends World2Mob 
    { 
    	Snake() 
    	{ 
    		super("Snake", 30, 15); 
    	} 
    }
    public static class Mummy extends World2Mob 
    { 
    	Mummy() 
    	{ 
    		super("Mummy", 60, 15); 
    	} 
    }
    public static class GiantWorm extends World2Mob 
    { 
    	GiantWorm() 
    	{ 
    		super("Giant Worm", 100, 13); 
    	} 
    }
         
}