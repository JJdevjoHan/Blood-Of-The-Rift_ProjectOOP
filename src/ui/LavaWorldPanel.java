package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class LavaWorldPanel extends JPanel {
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

    private World4Mob currentMob;
    private boolean inWorld = true;
    private final Random rng = new Random();
    private String reservedGolemDirection;
    private int mobsDefeated = 0;
    private int stepsTaken = 0;
    private boolean chestFound = false;
    private final int STEPS_FOR_CHEST = 2;
    private final Set<String> clearedDirections = new HashSet<>();
    private final Set<String> availableDirections = new HashSet<>();
    private final String[] directions = {"North", "East", "South", "West"};
    private final Map<String, World4Mob> directionMobs = new HashMap<>();
    private String currentDirection;

    public LavaWorldPanel(MainFrame mainFrame) {
        this.mainFrame = Objects.requireNonNull(mainFrame, "mainFrame must not be null");
        initialize();
    }

    private void initialize() {
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

        ActionListener moveListener = e -> {
            String dir = ((AbstractButton) e.getSource()).getActionCommand();
            if (inWorld) explore(dir);
        };

        // Attach movement listeners (no duplicates)
        Stream.of(northBtn, eastBtn, southBtn, westBtn).forEach(b -> {
            // ensure no duplicate listeners
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
        appendToLog("You enter the icy Snowy Island. Get ready for an Adventure!\n\n");
    }
    
    private Character createCharacter(String name, String className) {
        switch (className) {
            case "Mage": return new Mage(name, rng);
            case "Paladin": return new Paladin(name, rng);
            default: return new Warrior(name, rng);
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

            if (currentMob instanceof Golem) {
                appendToLog("You enter the icy Snowy Island. Get ready for an Adventure!.\nWhat will you do?\n\n");

                Object[] options = {"Enter Portal", "Return Home"};
                int choicePortal = JOptionPane.showOptionDialog(
                        this,
                        "The portal to the Rist is open.\n\nWhat will you do?",
                        "Portal Opened",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (choicePortal == 0) { // Enter Portal
                    
                    mainFrame.showPanel("finalWorld");
                } else { 
                    mainFrame.showPanel("intro");
                }
                currentMob = null;
                setSkillButtonsEnabled(false);
                updateStatus();
                return;
            }

            // Clear direction if mob is defeated
            if (currentDirection != null) {
                clearedDirections.add(currentDirection);
                availableDirections.remove(currentDirection);
            }

            // Open reward chest
            openRewardChest();

            // Clear current mob and disable skills
            currentMob = null;
            setSkillButtonsEnabled(false);
            updateStatus();
            return;
        }

        // If mob is still alive, it attacks back
        int mobDmg = currentMob.damage + rng.nextInt(6);
        player.hp -= mobDmg;
        if (player.hp < 0) player.hp = 0;
        appendToLog(currentMob.name + " attacks for " + mobDmg + " damage!\n\n");

        updateStatus();

        // Check if player is defeated
        if (player.hp <= 0) {
            appendToLog("You have been defeated...\n");
            disableMovement();
            JOptionPane.showMessageDialog(this, "Game Over", "Defeat", JOptionPane.PLAIN_MESSAGE);
            //mainFrame.showPanel("intro");
        }
    }

    private void explore(String direction) {
        if (direction == null) return;
        if (clearedDirections.contains(direction)) {
            JOptionPane.showMessageDialog(this,
                    "There is nothing in that direction! Try another path.",
                    "Empty Path", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        stepsTaken++;
        appendToLog("You walk " + direction.toLowerCase() + ".\n\n");

        // find chest and spawn mobs
        if (!chestFound && stepsTaken >= STEPS_FOR_CHEST) {
        	battleLog.append("You wander to the desert but Found nothing...\n\n");
            chestFound = true;
            List<World4Mob> mobs = Arrays.asList(
                    new LavaImp(),
                    new MagmaBeast(),
                    new SkeletonHead()
            );
            
            List<String> dirList = new ArrayList<>(Arrays.asList(directions));
            Collections.shuffle(dirList);

            // put up to 3 mobs into directionMobs safely
            directionMobs.clear();
            availableDirections.clear();
            for (int i = 0; i < Math.min(3, dirList.size()); i++) {
                directionMobs.put(dirList.get(i), mobs.get(i));
                availableDirections.add(dirList.get(i));
            }

            // reserve a direction for Minotaur if possible
            if (dirList.size() >= 4) {
                reservedGolemDirection = dirList.get(3);
            } else {
                reservedGolemDirection = null;
            }

            updateStatus();
            return;
        }

        // If chest has been found and there's currently no mob, spawn one for this direction
        if (chestFound && currentMob == null) {
            currentDirection = direction;

            boolean spawnGolemWolves = mobsDefeated >= 3 && Objects.equals(direction, reservedGolemDirection);

            if (spawnGolemWolves) {
                currentMob = new Golem();
                battleLog.append("The Giant Golem appeared!\n\n");
                battleLog.append("The Minancing Aura of the Golem....skicles!!\n\n");
                JOptionPane.showMessageDialog(null,"GOLEM INCOMING!!!", "WARNING! MINIBOSS", JOptionPane.ERROR_MESSAGE);
            }
            else {
                currentMob = directionMobs.get(direction); // may be null
            }

            if (currentMob != null) {
                appendToLog("A wild " + currentMob.name + " appears!\n\n");
                setSkillButtonsEnabled(true);
            } else {
                appendToLog("You wander the tundra but find nothing of interest....\n\n");
                setSkillButtonsEnabled(false);
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
        Random rng;  // Add this

        Character(String name, String className, int hp, int mana, Random rng) {
            this.name = name;
            this.className = className;
            this.hp = this.maxHp = hp;
            this.mana = this.maxMana = mana;
            this.rng = rng; // assign panel's RNG
        }

        boolean isAlive() { return hp > 0; }
        abstract String useSkill(int choice, World4Mob target);
    }


    public static class Warrior extends Character {
        Warrior(String n, Random rng) { super(n, "Warrior", 180, 80, rng); }

        @Override
        String useSkill(int choice, World4Mob target) {
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
        String useSkill(int choice, World4Mob target) {
            if (target == null) return "No target!";
            int dmg; String msg;
            switch (choice) {
                case 1 -> { dmg = 5 + rng.nextInt(6); dmg += tempDamage; target.hp -= dmg; mana = Math.min(maxMana, mana + 10); msg = "Frost Bolt deals " + dmg; }
                case 2 -> { if (mana >= 20) { dmg = 11 + rng.nextInt(10); dmg += tempDamage; target.hp -= dmg; mana -= 20; msg = "Rune Burst deals " + dmg; } else msg = "Not enough mana!"; }
                case 3 -> { if (mana >= 30) { dmg = 21 + rng.nextInt(15); dmg += tempDamage; target.hp -= dmg; mana -= 30; msg = "Lightstorm deals " + dmg; } else msg = "Not enough mana!"; }
                default -> msg = "Unknown skill.";
            }
            return msg;
        }
    }

    public static class Paladin extends Character {
        Paladin(String n, Random rng) { super(n, "Paladin", 220, 120, rng); }

        @Override
        String useSkill(int choice, World4Mob target) {
            if (target == null) return "No target!";
            int dmg; String msg;
            switch (choice) {
                case 1 -> { dmg = 5 + rng.nextInt(8); dmg += tempDamage; target.hp -= dmg; mana = Math.min(maxMana, mana + 10); msg = "Shield Bash deals " + dmg; }
                case 2 -> { if (mana >= 20) { mana -= 20; msg = "Radiant Guard! Damage reduced."; } else msg = "Not enough mana!"; }
                case 3 -> { if (mana >= 30) { int heal = 20 + rng.nextInt(16); mana -= 30; hp = Math.min(maxHp, hp + heal); msg = "Holy Renewal heals " + heal; } else msg = "Not enough mana!"; }
                default -> msg = "Unknown skill.";
            }
            return msg;
        }
    }
    public static abstract class World4Mob 
    {
        String name; int hp, damage;
        World4Mob(String name, int hp, int dmg)
        { 
        	this.name=name; this.hp=hp; this.damage=dmg; 
        }
        boolean isAlive()
        { 
        	return hp>0; 
        }
    }
        
        public static class LavaImp extends World4Mob {
            public LavaImp() {
                super("Lava Imp", 18, 20);
            }
        }

        public static class MagmaBeast extends World4Mob{
            public MagmaBeast(){
                super("Magma Beast", 20, 18);
            }
        }   

        public static class SkeletonHead extends World4Mob{
            public SkeletonHead(){
                super("Skeleton Head", 19, 10);
            }
        }

        public static class Golem extends World4Mob{
            public Golem(){
                super("Golem", 120, 20);
            }
        }

         
}