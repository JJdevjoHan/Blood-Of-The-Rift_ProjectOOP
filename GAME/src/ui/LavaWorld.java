package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


public class LavaWorld extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JTextArea battleLog = new JTextArea(10, 40);
    private final JLabel playerLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel mobLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel("", SwingConstants.LEFT);

    private final JButton northBtn = new JButton("North");
    private final JButton eastBtn  = new JButton("East");
    private final JButton southBtn = new JButton("South");
    private final JButton westBtn  = new JButton("West");
    private final JButton inspectBtn = new JButton("Inspect Class");

    private final JButton skill1Btn = new JButton();
    private final JButton skill2Btn = new JButton();
    private final JButton skill3Btn = new JButton();

    private Character player;
    private World4Mob currentMob;
    private boolean inWorld = true;
    private final Random rng = new Random();
    private String reservedSkeletonHeadDirection;
    private int mobsDefeated = 0;
    private int stepsTaken = 0;
    private boolean chestFound = false;
    private final int STEPS_FOR_CHEST = 2;
    private final Set<String> clearedDirections = new HashSet<>();
    private final Set<String> availableDirections = new HashSet<>();
    private final String[] directions = {"North","East","South","West"};
    private final Map<String, World4Mob> directionMobs = new HashMap<>();
    private String currentDirection;

    public LavaWorld (String playerName, String selectedClass) {
        setTitle("Lava World");
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
        battleLog.append("You step from the freezing cold directly into a furnace.\n\n");
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


        JPanel dpad = new JPanel(new GridBagLayout());
        dpad.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 1; gbc.gridy = 0; northBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
 dpad.add(northBtn, gbc);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 0; gbc.gridy = 1; westBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
 dpad.add(westBtn, gbc);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 2; gbc.gridy = 1; eastBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
 dpad.add(eastBtn, gbc);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 1; gbc.gridy = 2; southBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
 dpad.add(southBtn, gbc);

        JPanel skillPanel = new JPanel(new GridLayout(1,3,8,0));
        skillPanel.setBackground(Color.BLACK);
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
        southPanel.add(skillPanel, BorderLayout.NORTH);
        southPanel.add(dpad, BorderLayout.SOUTH);

        main.add(left, BorderLayout.WEST);
        main.add(centerWrapper, BorderLayout.CENTER);
        main.add(southPanel, BorderLayout.SOUTH);

        setContentPane(main);

        setupSkillButtons();

        ActionListener moveListener = e -> {
            if (inWorld) explore(((JButton)e.getSource()).getText());
        };
        northBtn.addActionListener(moveListener);
        eastBtn.addActionListener(moveListener);
        southBtn.addActionListener(moveListener);
        westBtn.addActionListener(moveListener);

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
            
     
            if(currentMob instanceof SkeletonHead) {
            	Object[] options = {"Enter Portal", "Return Home"};
            	int ch = JOptionPane.showOptionDialog(
            	        this,
            	        "✨ The portal to the Rist is open.\n\nWhat will you do?",
            	        "Portal Opened",
            	        JOptionPane.YES_NO_OPTION,
            	        JOptionPane.QUESTION_MESSAGE,
            	        null,
            	        options,
            	        options[0]
            	);

            	if (ch == 0) {
            	    battleLog.append("You step into the portal... The Desert World awaits!\n\n");
            	    new DesertWorld("Hero", "Warrior").setVisible(true);
            	    dispose();
            	} else {
            	    battleLog.append("You decide to return home to rest.\n\n");
            	    new Intro().setVisible(true);
            	    dispose();
            	}

            }

            clearedDirections.add(currentDirection);
            availableDirections.remove(currentDirection);
            
            //ahhh
            openRewardChest();

            currentMob = null;

        } else {
            int mobDmg = currentMob.damage + rng.nextInt(6);
            player.hp -= mobDmg;
            if (player.hp < 0) player.hp = 0;
            battleLog.append(currentMob.name + " attacks for " + mobDmg + " damage!\n\n");
        }

        updateStatus();

        if (player.hp <= 0) {
            battleLog.append("You have been defeated...\n");
            disableMovement();
            JOptionPane.showMessageDialog(this, "Game Over", "Defeat", JOptionPane.PLAIN_MESSAGE);
            dispose();
        }
    }

    private void explore(String direction) {

        if (clearedDirections.contains(direction)) {
            JOptionPane.showMessageDialog(this,
                    "There is nothing in that direction! Try another path.",
                    "Empty Path", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        stepsTaken++;
        battleLog.append("You walk " + direction.toLowerCase() + ".\n\n");

        if (!chestFound && stepsTaken >= STEPS_FOR_CHEST) {
 	
        	battleLog.append("You found nothing of interest as of the moment....\n\n");
            		
            chestFound = true;

            List<World4Mob> mobs = Arrays.asList(
                    new LavaImp(),
                    new MagmaBeast(),
                    new Golem()
            );

            List<String> dirList = new ArrayList<>(Arrays.asList(directions));
            Collections.shuffle(dirList);

            for (int i = 0; i < 3; i++) {
                directionMobs.put(dirList.get(i), mobs.get(i));
                availableDirections.add(dirList.get(i));
            }

            reservedSkeletonHeadDirection = dirList.get(3);

            updateStatus();
            return;
        }

        if (chestFound && currentMob == null) {

            currentDirection = direction;

            if (mobsDefeated == 3 && direction.equals(reservedSkeletonHeadDirection)) {
                currentMob = new SkeletonHead();
                battleLog.append("The Snowy Giant Frost Wolves has appeared!\n\n");
                battleLog.append("The Minancing Growl of a Wolf...Awooooooooo!!\n\n");
                JOptionPane.showMessageDialog(null,"GIANT FROST WOLF INCOMING!!!", "WARNING! MINIBOSS", JOptionPane.ERROR_MESSAGE);
            }
            else {
                currentMob = directionMobs.get(direction);
            }

   
            if (currentMob != null) {
            	 battleLog.append("A wild " + currentMob.name + " appears!\n\n");
            } else {
                
                battleLog.append("You wander the tundra but find nothing of interest..\n\n");
            }
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

    private void disableMovement() {
        northBtn.setEnabled(false);
        southBtn.setEnabled(false);
        eastBtn.setEnabled(false);
        westBtn.setEnabled(false);
        skill1Btn.setEnabled(false);
        skill2Btn.setEnabled(false);
        skill3Btn.setEnabled(false);
    }

    static abstract class Character {
        String name, className;
        int hp, maxHp, mana, maxMana;
        int tempDamage = 0;


        Character(String name, String className, int hp, int mana) {
            this.name = name; this.className = className;
            this.hp = this.maxHp = hp;
            this.mana = this.maxMana = mana;
        }

        boolean isAlive() { return hp > 0; }
        abstract String useSkill(int choice, World4Mob currentMob);
    }

    static class Warrior extends Character {
    	
        Warrior(String n) {
        	super(n,"Warrior",180,80); 
        	}
        @Override
        String useSkill(int choice, World4Mob target) {
        	
            int dmg = 0; String msg = "";
           //?here
            switch(choice) {
                case 1: dmg = 5 + new Random().nextInt(6); target.hp -= dmg; mana += 10; msg="Stone Slash deals "+dmg; break;
                case 2: if(mana>=20){ dmg = 12+new Random().nextInt(8); target.hp -= dmg; mana-=20; msg="Flame Strike deals "+dmg; } else msg="Not enough mana!"; break;
                case 3: if(mana>=30){ dmg = 20+new Random().nextInt(15); target.hp -= dmg; mana-=30; msg="Earthquake Blade deals "+dmg; } else msg="Not enough mana!"; break;
            }
            if(mana<0) mana=0;
            return msg;
        }
    }

    static class Mage extends Character 
    {
        Mage(String n) 
        {
        	super(n,"Mage",120,150); 
        }
        @Override
        String useSkill(int choice, World4Mob target) {
            int dmg=0; String msg="";
            switch(choice) {
                case 1: dmg=5+new Random().nextInt(6); target.hp-=dmg; mana+=10; if(mana>maxMana) mana=maxMana; msg="Frost Bolt deals "+dmg; break;
                case 2: if(mana>=20){ dmg=11+new Random().nextInt(10); target.hp-=dmg; mana-=20; msg="Rune Burst deals "+dmg; } else msg="Not enough mana!"; break;
                case 3: if(mana>=30){ dmg=21+new Random().nextInt(15); target.hp-=dmg; mana-=30; msg="Lightstorm deals "+dmg; } else msg="Not enough mana!"; break;
            }
            if(mana<0) mana=0;
            return msg;
        }
    }

    static class Paladin extends Character 
    {
        Paladin(String n) 
        { 
        	super(n,"Paladin",220,120); 
        }
        @Override
        String useSkill(int choice, World4Mob target) 
        {
            int dmg=0; String msg="";
            switch(choice) 
            {
                case 1: dmg=5+new Random().nextInt(8); target.hp-=dmg; mana+=10; if(mana>maxMana) mana=maxMana; msg="Shield Bash deals "+dmg; break;
                case 2: if(mana>=20){ mana-=20; msg="Radiant Guard! Damage reduced."; } else msg="Not enough mana!"; break;
                case 3: if(mana>=30){ int heal=20+new Random().nextInt(16); hp+=heal; if(hp>maxHp) hp=maxHp; mana-=30; msg="Holy Renewal heals "+heal; } else msg="Not enough mana!"; break;
            }
            if(mana<0) mana=0;
            return msg;
        }
    }
    
    private void openRewardChest() {
        battleLog.append("You found a reward chest!\n\n");

        int rewardType = rng.nextInt(5);

        switch (rewardType) {
            case 0:
                int hpBoost = 30;
                player.maxHp += hpBoost;
                player.hp += hpBoost;
                if (player.hp > player.maxHp) player.hp = player.maxHp;
                battleLog.append("Your maximum HP increased by " + hpBoost + "!\n\n");
                JOptionPane.showMessageDialog(null, "HP INCREASED!", "Reward", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 1: 
                int dmgBoost = 15;
                player.tempDamage += dmgBoost;
                battleLog.append("Your damage increased by " + dmgBoost + " for the next battle!\n\n");
                JOptionPane.showMessageDialog(null, "DAMAGE INCREASED!", "Reward", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 2: 
                int manaBoost = 20;
                player.maxMana += manaBoost;
                player.mana += manaBoost;
                if (player.mana > player.maxMana) player.mana = player.maxMana;
                battleLog.append("Your maximum Mana increased by " + manaBoost + "!\n\n");
                JOptionPane.showMessageDialog(null, "MANA INCREASED!", "Reward", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 3:
                player.hp = player.maxHp;
                battleLog.append("You found a healing potion! HP restored to full!\n\n");
                JOptionPane.showMessageDialog(null, "HP RESTORED!", "Reward", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 4:
                player.mana = player.maxMana;
                battleLog.append("You found a mana elixir! Mana restored to full!\n\n");
                JOptionPane.showMessageDialog(null, "MANA RESTORED!", "Reward", JOptionPane.INFORMATION_MESSAGE);
                break;
        }

        updateStatus();
    }


    static abstract class World4Mob 
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

    static class LavaImp extends World4Mob 
    {
    	LavaImp() 
    	{
    		super("LavaImp", 5, 20); 
    	} 
    }
    static class MagmaBeast extends World4Mob 
    { 
    	MagmaBeast() 
    	{ 
    		super("MagmaBeast", 5, 18); 
    	} 
    }
    static class Golem  extends World4Mob 
    { 
    	Golem() 
    	{ 
    		super("Golem", 5, 20); 
    	} 
    }
    static class SkeletonHead extends World4Mob 
    { 
    	SkeletonHead() 
    	{ 
    		super("SkeletonHead", 5, 10); 
    	} 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LavaWorld("Hero","Warrior").setVisible(true));
    }
}