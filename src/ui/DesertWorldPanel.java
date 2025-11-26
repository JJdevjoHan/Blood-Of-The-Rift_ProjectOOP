package ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class DesertWorldPanel extends JPanel {
    private final MainFrame mainFrame;
    private Character player;

    private CardLayout mainCardLayout;
    private JPanel containerPanel;
    private CardLayout bottomCardLayout;
    private JPanel bottomPanel;

    private JLabel viewportLabel;
    private JTextPane dialoguePane;
    private JPanel buttonPanel;
    private RiftButton btnNorth, btnSouth, btnEast, btnWest;
    private RiftBar playerHpBar;
    private RiftBar playerMpBar;

    private JPanel versusPanel;
    private JLabel vsPlayerLabel, vsMobLabel, vsTextLabel, vsStatusLabel;

    private JPanel battlePanel;
    private JLabel playerPortrait, mobPortrait;
    private JLabel playerNameLabel, mobNameLabel;
    private RiftBar battleHpBar, battleMpBar, mobHpBar;
    private RiftButton btnSkill1, btnSkill2, btnSkill3;

    private JTextPane battleLogPane;

    private World2Mob currentMob;
    private final Random rng = new Random();
    private int mobsDefeated = 0;
    private final int MOBS_UNTIL_BOSS = 3;
    private int introStep = 0;

    private final String[] introLines = {
            "You step through the portal and are hit by a wave of searing, unnatural heat.",
            "This desert is wrong. The sand is gray with ash, and the sun is a hateful red eye.",
            "Legends speak of a corrupted tomb, a guardian... and a sacred blade lost in the sand.",
    };

    private Image bgImage;

    private final Color NEON_CYAN = new Color(0, 255, 255);
    private final Color DESERT_GOLD = new Color(255, 165, 0);
    private final Color BG_BLACK = Color.BLACK;
    private final Font RETRO_FONT = new Font("Consolas", Font.BOLD, 22);
    private final Font HEADER_FONT = new Font("Impact", Font.PLAIN, 28);
    private final Font CHAR_FONT = new Font("Consolas", Font.BOLD, 110);
    private final Font VS_FONT = new Font("Impact", Font.ITALIC | Font.BOLD, 80);
    private final Border CYAN_BORDER = new LineBorder(NEON_CYAN, 3);

    public DesertWorldPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        mainCardLayout = new CardLayout();
        containerPanel = new JPanel(mainCardLayout);
        add(containerPanel, BorderLayout.CENTER);

        URL bgUrl = getClass().getResource("/images/backgroundpic/desert.png");
        if (bgUrl != null) {
            bgImage = new ImageIcon(bgUrl).getImage();
        }

        containerPanel.add(createRetroLayout(), "EXPLORE");
        containerPanel.add(createVersusPanel(), "VERSUS");
        containerPanel.add(createBattlePanel(), "BATTLE");
    }

    private JPanel createRetroLayout() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(BG_BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        JPanel headerBox = createBoxPanel();
        JLabel title = new JLabel("DESERT WORLD", SwingConstants.CENTER);
        title.setFont(HEADER_FONT);
        title.setForeground(Color.WHITE);
        headerBox.add(title, BorderLayout.CENTER);
        gbc.gridy = 0; gbc.weighty = 0.1;
        main.add(headerBox, gbc);

        JPanel viewportBox = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        viewportBox.setBorder(CYAN_BORDER);

        GridBagConstraints viewGbc = new GridBagConstraints();
        viewGbc.gridx = 0; viewGbc.anchor = GridBagConstraints.CENTER;

        JPanel statsContainer = new JPanel(new GridBagLayout());
        statsContainer.setOpaque(false);
        GridBagConstraints statGbc = new GridBagConstraints();
        statGbc.gridx = 0; statGbc.anchor = GridBagConstraints.WEST;

        playerHpBar = new RiftBar(100, Color.GREEN); playerHpBar.setPreferredSize(new Dimension(300, 25));
        statGbc.gridy = 0; statsContainer.add(playerHpBar, statGbc);

        playerMpBar = new RiftBar(100, Color.BLUE); playerMpBar.setPreferredSize(new Dimension(200, 15));
        statGbc.gridy = 1; statGbc.insets = new Insets(3, 0, 0, 0);
        statsContainer.add(playerMpBar, statGbc);

        viewGbc.gridy = 0; viewportBox.add(statsContainer, viewGbc);

        viewportLabel = new JLabel("", SwingConstants.CENTER);
        viewportLabel.setForeground(Color.WHITE);
        viewportLabel.setFont(CHAR_FONT);
        viewGbc.gridy = 1; viewGbc.insets = new Insets(20, 0, 0, 0);
        viewportBox.add(viewportLabel, viewGbc);

        gbc.gridy = 1; gbc.weighty = 0.7;
        main.add(viewportBox, gbc);

        bottomPanel = createBoxPanel();
        bottomCardLayout = new CardLayout();
        bottomPanel.setLayout(bottomCardLayout);
        bottomPanel.setPreferredSize(new Dimension(900, 150));

        dialoguePane = new JTextPane();
        dialoguePane.setBackground(BG_BLACK); dialoguePane.setForeground(Color.WHITE);
        dialoguePane.setFont(RETRO_FONT); dialoguePane.setEditable(false);

        StyledDocument doc = dialoguePane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        JPanel textWrapper = new JPanel(new GridBagLayout()); textWrapper.setBackground(BG_BLACK);
        textWrapper.add(dialoguePane); bottomPanel.add(textWrapper, "TEXT");

        buttonPanel = new JPanel(new GridBagLayout()); buttonPanel.setBackground(BG_BLACK);
        GridBagConstraints btnGbc = new GridBagConstraints();

        JLabel promptLabel = new JLabel("Where do you want to go?");
        promptLabel.setFont(RETRO_FONT); promptLabel.setForeground(NEON_CYAN);
        btnGbc.gridx = 0; btnGbc.gridy = 0; btnGbc.gridwidth = 4;
        btnGbc.insets = new Insets(0, 0, 15, 0); btnGbc.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(promptLabel, btnGbc);

        btnNorth = new RiftButton("NORTH"); btnSouth = new RiftButton("SOUTH");
        btnEast = new RiftButton("EAST"); btnWest = new RiftButton("WEST");

        btnNorth.addActionListener(e -> explore(1)); btnEast.addActionListener(e -> explore(2));
        btnSouth.addActionListener(e -> explore(3)); btnWest.addActionListener(e -> explore(4));

        btnGbc.gridy = 1; btnGbc.gridwidth = 1; btnGbc.insets = new Insets(0, 10, 0, 10); btnGbc.fill = GridBagConstraints.NONE;
        btnGbc.gridx = 0; buttonPanel.add(btnWest, btnGbc); btnGbc.gridx = 1; buttonPanel.add(btnNorth, btnGbc);
        btnGbc.gridx = 2; buttonPanel.add(btnSouth, btnGbc); btnGbc.gridx = 3; buttonPanel.add(btnEast, btnGbc);
        bottomPanel.add(buttonPanel, "BUTTONS");

        gbc.gridy = 2; gbc.weighty = 0.2; main.add(bottomPanel, gbc);
        return main;
    }

    private JPanel createBoxPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_BLACK);
        p.setBorder(CYAN_BORDER);
        return p;
    }

    private JPanel createVersusPanel() {
        JPanel mainVSPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 180));
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainVSPanel.setOpaque(false);

        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 0, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        vsPlayerLabel = new JLabel("", SwingConstants.CENTER);
        vsPlayerLabel.setBorder(new LineBorder(Color.CYAN, 4));
        vsPlayerLabel.setPreferredSize(new Dimension(300, 300));
        gbc.gridx = 0; gbc.weightx = 0.4;
        centerContainer.add(vsPlayerLabel, gbc);

        vsTextLabel = new JLabel("VS", SwingConstants.CENTER);
        vsTextLabel.setFont(VS_FONT);
        vsTextLabel.setForeground(DESERT_GOLD);
        vsTextLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        gbc.gridx = 1; gbc.weightx = 0.2;
        centerContainer.add(vsTextLabel, gbc);

        vsMobLabel = new JLabel("", SwingConstants.CENTER);
        vsMobLabel.setBorder(new LineBorder(Color.RED, 4));
        vsMobLabel.setPreferredSize(new Dimension(300, 300));
        gbc.gridx = 2; gbc.weightx = 0.4;
        centerContainer.add(vsMobLabel, gbc);

        mainVSPanel.add(centerContainer, BorderLayout.CENTER);

        vsStatusLabel = new JLabel("", SwingConstants.CENTER);
        vsStatusLabel.setFont(RETRO_FONT);
        vsStatusLabel.setForeground(Color.WHITE);
        vsStatusLabel.setBorder(new EmptyBorder(20, 0, 30, 0));
        mainVSPanel.add(vsStatusLabel, BorderLayout.SOUTH);

        return mainVSPanel;
    }

    private JPanel createBattlePanel() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 200));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        p.setOpaque(false);

        JPanel arena = new JPanel(new GridLayout(1, 2, 20, 0));
        arena.setOpaque(false);
        arena.setBorder(new EmptyBorder(20, 40, 0, 40));

        JPanel heroPane = new JPanel(new GridBagLayout());
        heroPane.setOpaque(false);
        GridBagConstraints hGbc = new GridBagConstraints();
        hGbc.gridx = 0; hGbc.fill = GridBagConstraints.HORIZONTAL; hGbc.anchor = GridBagConstraints.CENTER;

        playerPortrait = new JLabel("", SwingConstants.CENTER);
        playerPortrait.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        playerPortrait.setPreferredSize(new Dimension(300, 300));
        hGbc.gridy = 0; hGbc.insets = new Insets(0, 0, 15, 0);
        heroPane.add(playerPortrait, hGbc);

        playerNameLabel = new JLabel("HERO", SwingConstants.CENTER);
        playerNameLabel.setForeground(Color.CYAN);
        playerNameLabel.setFont(HEADER_FONT);
        hGbc.gridy = 1; hGbc.insets = new Insets(0, 0, 5, 0);
        heroPane.add(playerNameLabel, hGbc);

        battleHpBar = new RiftBar(100, Color.GREEN);
        battleHpBar.setPreferredSize(new Dimension(300, 30));
        hGbc.gridy = 2; hGbc.insets = new Insets(0, 0, 5, 0);
        heroPane.add(battleHpBar, hGbc);

        battleMpBar = new RiftBar(100, Color.BLUE);
        battleMpBar.setPreferredSize(new Dimension(200, 15));
        hGbc.gridy = 3;
        heroPane.add(battleMpBar, hGbc);

        arena.add(heroPane);

        JPanel mobPane = new JPanel(new GridBagLayout());
        mobPane.setOpaque(false);
        GridBagConstraints mGbc = new GridBagConstraints();
        mGbc.gridx = 0; mGbc.fill = GridBagConstraints.HORIZONTAL; mGbc.anchor = GridBagConstraints.CENTER;

        mobPortrait = new JLabel("", SwingConstants.CENTER);
        mobPortrait.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        mobPortrait.setPreferredSize(new Dimension(300, 300));
        mGbc.gridy = 0; mGbc.insets = new Insets(0, 0, 15, 0);
        mobPane.add(mobPortrait, mGbc);

        mobNameLabel = new JLabel("ENEMY", SwingConstants.CENTER);
        mobNameLabel.setForeground(Color.RED);
        mobNameLabel.setFont(HEADER_FONT);
        mGbc.gridy = 1; mGbc.insets = new Insets(0, 0, 5, 0);
        mobPane.add(mobNameLabel, mGbc);

        mobHpBar = new RiftBar(100, Color.RED);
        mobHpBar.setPreferredSize(new Dimension(300, 30));
        mGbc.gridy = 2;
        mobPane.add(mobHpBar, mGbc);

        arena.add(mobPane);
        p.add(arena, BorderLayout.CENTER);

        JPanel southWrapper = new JPanel(new BorderLayout());
        southWrapper.setOpaque(false);

        battleLogPane = new JTextPane();
        battleLogPane.setOpaque(false);
        battleLogPane.setEditable(false);
        battleLogPane.setFont(RETRO_FONT);
        battleLogPane.setForeground(Color.WHITE);
        battleLogPane.setPreferredSize(new Dimension(800, 80));

        StyledDocument doc = battleLogPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        JPanel logContainer = new JPanel(new BorderLayout());
        logContainer.setOpaque(false);
        logContainer.setBorder(new EmptyBorder(10, 100, 10, 100));
        logContainer.add(battleLogPane, BorderLayout.CENTER);

        southWrapper.add(logContainer, BorderLayout.NORTH);

        JPanel skills = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        skills.setOpaque(false);
        skills.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.WHITE));

        btnSkill1 = new RiftButton("SKILL 1");
        btnSkill2 = new RiftButton("SKILL 2");
        btnSkill3 = new RiftButton("SKILL 3");

        Dimension skillDim = new Dimension(220, 50);
        btnSkill1.setPreferredSize(skillDim);
        btnSkill2.setPreferredSize(skillDim);
        btnSkill3.setPreferredSize(skillDim);

        btnSkill1.addActionListener(e -> doSkill(1));
        btnSkill2.addActionListener(e -> doSkill(2));
        btnSkill3.addActionListener(e -> doSkill(3));

        skills.add(btnSkill1); skills.add(btnSkill2); skills.add(btnSkill3);

        southWrapper.add(skills, BorderLayout.SOUTH);
        p.add(southWrapper, BorderLayout.SOUTH);

        return p;
    }

    public void setPlayerObject(Character p) {
        this.player = p;
        updateSkillButtons();
        updateViewport();
        mainCardLayout.show(containerPanel, "EXPLORE");
        startIntroSequence();
    }

    private void updateViewport() {
        String path = "/images/playable/" + player.className.toLowerCase() + ".png";
        URL url = getClass().getResource(path);
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
            viewportLabel.setIcon(new ImageIcon(img));
            viewportLabel.setText("");
            playerPortrait.setIcon(new ImageIcon(icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
        }
        updateStatus();
    }

    private void startIntroSequence() {
        introStep = 0;
        bottomCardLayout.show(bottomPanel, "TEXT"); dialoguePane.setText("");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (introStep < introLines.length) {
                        dialoguePane.setText(introLines[introStep]); introStep++;
                    } else {
                        promptNavigation(); timer.cancel();
                    }
                });
            }
        }, 500, 2000);
    }

    private void promptNavigation() {
        bottomCardLayout.show(bottomPanel, "BUTTONS");
    }

    private void explore(int dir) {
        bottomCardLayout.show(bottomPanel, "TEXT");
        String txt = switch(dir) {
            case 1 -> "You climb over the buried ruins of an old city.";
            case 2 -> "You walk east, the sun glinting off sharp, black glass.";
            case 3 -> "You trudge south through endless, choking dunes of ash.";
            case 4 -> "You head west into a field of colossal, sun-bleached bones.";
            default -> "";
        };
        dialoguePane.setText(txt);

        if (rng.nextInt(100) < 80) {
            new Timer().schedule(new TimerTask() {
                @Override public void run() { SwingUtilities.invokeLater(() -> triggerEncounter()); }
            }, 1000);
        } else {
            new Timer().schedule(new TimerTask() {
                @Override public void run() {
                    SwingUtilities.invokeLater(() -> {
                        dialoguePane.setText("You wander the desert but find nothing of interest.");
                        new Timer().schedule(new TimerTask() {
                            @Override public void run() { SwingUtilities.invokeLater(() -> promptNavigation()); }
                        }, 1500);
                    });
                }
            }, 1500);
        }
    }

    private void triggerEncounter() {
        if (mobsDefeated >= MOBS_UNTIL_BOSS) {
            currentMob = new World2Mob.GiantWorm();
        } else {
            int roll = rng.nextInt(3);
            if (roll == 0) currentMob = new World2Mob.Spider();
            else if (roll == 1) currentMob = new World2Mob.Snake();
            else currentMob = new World2Mob.Mummy();
        }
        startVersusSequence();
    }

    private void startVersusSequence() {
        mainCardLayout.show(containerPanel, "VERSUS");

        // Custom Intro Text
        if (currentMob instanceof World2Mob.GiantWorm) {
            vsStatusLabel.setText("A sarcophagus bursts from the sand! The Desert Boss " + currentMob.name + " has appeared!");
            vsStatusLabel.setForeground(new Color(255, 100, 100));
        } else {
            vsStatusLabel.setText("A " + currentMob.name + " ambushes you from the sand!");
            vsStatusLabel.setForeground(Color.WHITE);
        }

        // Load Images
        String pPath = "/images/playable/" + player.className.toLowerCase() + ".png";
        URL pUrl = getClass().getResource(pPath);
        if (pUrl != null) {
            ImageIcon pIcon = new ImageIcon(pUrl);
            vsPlayerLabel.setIcon(new ImageIcon(pIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
        }

        // LOAD WORLD 2 MOB IMAGE
        String mobFileName = "Snake";
        String nameCheck = currentMob.name.toLowerCase();

        if (nameCheck.contains("worm")) mobFileName = "Giant Worm";
        else if (nameCheck.contains("mummy")) mobFileName = "Mummy";
        else if (nameCheck.contains("snake")) mobFileName = "Snake";
        else if (nameCheck.contains("spider")) mobFileName = "Spider";

        String mPath = "/images/World2Mob/" + mobFileName + ".png";

        try {
            URL mUrl = getClass().getResource(mPath);
            if(mUrl != null) {
                ImageIcon mIcon = new ImageIcon(mUrl);
                Image sc = mIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                vsMobLabel.setIcon(new ImageIcon(sc));
                vsMobLabel.setText("");
                mobPortrait.setIcon(new ImageIcon(sc));
                mobPortrait.setText("");
            } else {
                vsMobLabel.setIcon(null); vsMobLabel.setText(currentMob.name);
            }
        } catch (Exception e) { vsMobLabel.setText(currentMob.name); }

        new Timer().schedule(new TimerTask() {
            @Override public void run() { SwingUtilities.invokeLater(() -> startBattle()); }
        }, 4000);
    }

    private void startBattle() {
        mainCardLayout.show(containerPanel, "BATTLE");
        battleLogPane.setText("It is your turn! Choose a skill.");

        mobNameLabel.setText(currentMob.name.toUpperCase());
        mobHpBar.setMax(currentMob.maxHp);
        mobHpBar.updateValue(currentMob.hp);

        playerNameLabel.setText(player.name.toUpperCase() + " [" + player.className + "]");
        battleHpBar.setMax(player.maxHp); battleHpBar.updateValue(player.hp);
        battleMpBar.setMax(player.maxMana); battleMpBar.updateValue(player.mana);

        setButtonsEnabled(true);
    }

    private void updateSkillButtons() {
        if (player instanceof Warrior) {
            btnSkill1.setText("STONE SLASH"); btnSkill2.setText("FLAME STRIKE"); btnSkill3.setText("EARTHQUAKE");
        } else if (player instanceof Mage) {
            btnSkill1.setText("FROST BOLT"); btnSkill2.setText("RUNE BURST"); btnSkill3.setText("LIGHTSTORM");
        } else if (player instanceof Paladin) {
            btnSkill1.setText("SHIELD BASH"); btnSkill2.setText("RADIANT GUARD"); btnSkill3.setText("HOLY RENEWAL");
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        btnSkill1.setEnabled(enabled);
        btnSkill2.setEnabled(enabled);
        btnSkill3.setEnabled(enabled);
    }

    private void doSkill(int choice) {
        if (currentMob == null) return;
        setButtonsEnabled(false);

        // Player Turn
        String res = player.useSkill(choice, currentMob);
        mobHpBar.updateValue(currentMob.hp);
        battleMpBar.updateValue(player.mana);
        battleLogPane.setText(res);

        if (!currentMob.isAlive()) {
            new Timer().schedule(new TimerTask() {
                @Override public void run() {
                    SwingUtilities.invokeLater(() -> endBattle(true));
                }
            }, 1500);
            return;
        }

        // Enemy Turn
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    // Check for special skill logic if boss
                    String enemyAtk;
                    if (currentMob instanceof World2Mob.GiantWorm && rng.nextInt(100) < 30) {
                        enemyAtk = ((World2Mob.GiantWorm) currentMob).specialSkill(player);
                    } else {
                        enemyAtk = currentMob.attack(player);
                    }

                    battleHpBar.updateValue(player.hp);
                    battleLogPane.setText(enemyAtk);

                    if (player.hp <= 0) {
                        new Timer().schedule(new TimerTask() {
                            @Override public void run() {
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(DesertWorldPanel.this, "DEFEATED", "Game Over", JOptionPane.ERROR_MESSAGE);
                                    mainFrame.showPanel("intro");
                                });
                            }
                        }, 1500);
                    } else {
                        // Reset to Player
                        new java.util.Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                SwingUtilities.invokeLater(() -> {
                                    battleLogPane.setText("It is your turn! Choose a skill.");
                                    setButtonsEnabled(true);
                                });
                            }
                        }, 1500);
                    }
                });
            }
        }, 1500);
    }

    private void endBattle(boolean win) {
        if (win) {
            battleLogPane.setText("VICTORY! Enemy defeated.");
            new Timer().schedule(new TimerTask() {
                @Override public void run() {
                    SwingUtilities.invokeLater(() -> {
                        if (currentMob instanceof World2Mob.GiantWorm) {
                            playWorld2Outro();
                        } else {
                            mobsDefeated++;
                            openRewardChest();
                            showExplore();
                        }
                    });
                }
            }, 1500);
        }
    }

    // --- CUSTOM UI IMPLEMENTATION HERE ---
    private void playWorld2Outro() {
        mainCardLayout.show(containerPanel, "EXPLORE");
        bottomCardLayout.show(bottomPanel, "TEXT");

        updateViewport();

        String msg1 = "The Giant Worm crumbles to dust, and the red haze over the desert lifts.";
        dialoguePane.setText(msg1);

        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    String msg2 = "As the cursed sand settles, you see a portal of frost and ice appear.\n" +
                            "You hear a cold voice echo... Silence. Stillness. Order.";
                    dialoguePane.setText(msg2);

                    new java.util.Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(() -> {

                                // --- RIFT DIALOG (YES/NO) ---
                                int c = RiftDialog.showConfirmDialog(DesertWorldPanel.this,
                                        "Enter the Snowy Island?", "Portal Opened");

                                if (c == JOptionPane.YES_OPTION) {
                                    mainFrame.enterSnowyIsland(player);
                                }
                                else mainFrame.showPanel("home");
                                // ----------------------------

                            });
                        }
                    }, 6000);
                });
            }
        }, 5000);
    }

    private void showExplore() {
        mainCardLayout.show(containerPanel, "EXPLORE");
        updateViewport();
        updateStatus();
        promptNavigation();
    }

    private void openRewardChest() {
        Object[] opts = {"HEAL", "MANA", "DMG UP"};

        // --- RIFT OPTION DIALOG ---
        int c = RiftDialog.showOptionDialog(this,
                "You found a chest! Choose your reward:",
                "VICTORY LOOT", opts);
        // --------------------------

        if (c == 0) player.hp = player.maxHp;
        else if (c == 1) player.mana = player.maxMana;
        else player.tempDamage += 15;
    }

    private void updateStatus() {
        if(player != null) {
            playerHpBar.setMax(player.maxHp); playerHpBar.updateValue(player.hp);
            playerMpBar.setMax(player.maxMana); playerMpBar.updateValue(player.mana);
        }
    }
}