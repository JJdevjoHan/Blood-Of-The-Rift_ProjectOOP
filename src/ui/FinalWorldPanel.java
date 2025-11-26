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

public class FinalWorldPanel extends JPanel {
    private final MainFrame mainFrame;
    private Character player;

    // LAYOUT MANAGERS
    private CardLayout mainCardLayout;
    private JPanel containerPanel;
    private CardLayout bottomCardLayout;
    private JPanel bottomPanel;

    // EXPLORE UI COMPONENTS
    private JLabel viewportLabel;
    private JTextPane dialoguePane;
    // ButtonPanel removed as story is now automatic

    private RiftBar playerHpBar;
    private RiftBar playerMpBar;

    // VERSUS (VS) SCREEN COMPONENTS
    private JPanel versusPanel;
    private JLabel vsPlayerLabel, vsMobLabel, vsTextLabel, vsStatusLabel;

    // BATTLE UI COMPONENTS
    private JPanel battlePanel;
    private JLabel playerPortrait, mobPortrait;
    private JLabel playerNameLabel, mobNameLabel;
    private RiftBar battleHpBar, battleMpBar, mobHpBar;
    private RiftButton btnSkill1, btnSkill2, btnSkill3;

    // BATTLE LOG
    private JTextPane battleLogPane;

    // STATE
    private World5Boss currentBoss;
    private final Random rng = new Random();

    private Image bgImage;

    // STYLING
    private final Color NEON_CYAN = new Color(0, 255, 255);
    private final Color RIFT_PURPLE = new Color(138, 43, 226); // Theme Color
    private final Color BG_BLACK = Color.BLACK;
    private final Font RETRO_FONT = new Font("Consolas", Font.BOLD, 22);
    private final Font HEADER_FONT = new Font("Impact", Font.PLAIN, 28);
    private final Font CHAR_FONT = new Font("Consolas", Font.BOLD, 110);
    private final Font VS_FONT = new Font("Impact", Font.ITALIC | Font.BOLD, 80);
    private final Border RIFT_BORDER = new LineBorder(RIFT_PURPLE, 3);

    public FinalWorldPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        mainCardLayout = new CardLayout();
        containerPanel = new JPanel(mainCardLayout);
        add(containerPanel, BorderLayout.CENTER);

        // tinted to purple para chuy
        URL bgUrl = getClass().getResource("/images/backgroundpic/volcano.png");
        if (bgUrl != null) {
            bgImage = new ImageIcon(bgUrl).getImage();
        }

        containerPanel.add(createRetroLayout(), "EXPLORE");
        containerPanel.add(createVersusPanel(), "VERSUS");
        containerPanel.add(createBattlePanel(), "BATTLE");
    }

    // --- 1. EXPLORE LAYOUT ---
    private JPanel createRetroLayout() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(BG_BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // TOP BOX
        JPanel headerBox = createBoxPanel();
        JLabel title = new JLabel("THE RIFT", SwingConstants.CENTER);
        title.setFont(HEADER_FONT);
        title.setForeground(Color.WHITE);
        headerBox.add(title, BorderLayout.CENTER);
        gbc.gridy = 0; gbc.weighty = 0.1;
        main.add(headerBox, gbc);

        // MIDDLE BOX (VIEWPORT)
        JPanel viewportBox = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                    // RIFT TINT: Dark Purple overlay
                    g.setColor(new Color(50, 0, 80, 180));
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        viewportBox.setBorder(RIFT_BORDER);

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

        // BOTTOM BOX
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

        gbc.gridy = 2; gbc.weighty = 0.2; main.add(bottomPanel, gbc);
        return main;
    }

    private JPanel createBoxPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_BLACK);
        p.setBorder(RIFT_BORDER);
        return p;
    }

    private JPanel createVersusPanel() {
        JPanel mainVSPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(50, 0, 80, 200)); // Deep Purple Tint
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else { g.setColor(Color.BLACK); g.fillRect(0, 0, getWidth(), getHeight()); }
            }
        };
        mainVSPanel.setOpaque(false);

        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 0, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        // LEFT: PLAYER PORTRAIT
        vsPlayerLabel = new JLabel("", SwingConstants.CENTER);
        vsPlayerLabel.setBorder(new LineBorder(Color.CYAN, 4));
        vsPlayerLabel.setPreferredSize(new Dimension(300, 300));
        gbc.gridx = 0; gbc.weightx = 0.4;
        centerContainer.add(vsPlayerLabel, gbc);

        // CENTER: VS LOGO
        vsTextLabel = new JLabel("VS", SwingConstants.CENTER);
        vsTextLabel.setFont(VS_FONT);
        vsTextLabel.setForeground(RIFT_PURPLE);
        vsTextLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        gbc.gridx = 1; gbc.weightx = 0.2;
        centerContainer.add(vsTextLabel, gbc);

        // RIGHT: BOSS PORTRAIT
        vsMobLabel = new JLabel("", SwingConstants.CENTER);
        vsMobLabel.setBorder(new LineBorder(Color.RED, 4));
        vsMobLabel.setPreferredSize(new Dimension(300, 300));
        gbc.gridx = 2; gbc.weightx = 0.4;
        centerContainer.add(vsMobLabel, gbc);

        mainVSPanel.add(centerContainer, BorderLayout.CENTER);

        // Bottom Status Text Label
        vsStatusLabel = new JLabel("", SwingConstants.CENTER);
        vsStatusLabel.setFont(RETRO_FONT);
        vsStatusLabel.setForeground(Color.WHITE);
        vsStatusLabel.setBorder(new EmptyBorder(20, 0, 30, 0));
        mainVSPanel.add(vsStatusLabel, BorderLayout.SOUTH);

        return mainVSPanel;
    }

    // --- 3. BATTLE LAYOUT ---
    private JPanel createBattlePanel() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 220));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        p.setOpaque(false);

        JPanel arena = new JPanel(new GridLayout(1, 2, 20, 0));
        arena.setOpaque(false);
        arena.setBorder(new EmptyBorder(20, 40, 0, 40));

        // LEFT HERO
        JPanel heroPane = new JPanel(new GridBagLayout()); heroPane.setOpaque(false);
        GridBagConstraints hGbc = new GridBagConstraints();
        hGbc.gridx = 0; hGbc.fill = GridBagConstraints.HORIZONTAL; hGbc.anchor = GridBagConstraints.CENTER;

        playerPortrait = new JLabel("", SwingConstants.CENTER);
        playerPortrait.setBorder(new LineBorder(Color.DARK_GRAY, 2)); playerPortrait.setPreferredSize(new Dimension(300, 300));
        hGbc.gridy = 0; hGbc.insets = new Insets(0, 0, 15, 0); heroPane.add(playerPortrait, hGbc);

        playerNameLabel = new JLabel("HERO", SwingConstants.CENTER);
        playerNameLabel.setForeground(Color.CYAN); playerNameLabel.setFont(HEADER_FONT);
        hGbc.gridy = 1; hGbc.insets = new Insets(0, 0, 5, 0); heroPane.add(playerNameLabel, hGbc);

        battleHpBar = new RiftBar(100, Color.GREEN); battleHpBar.setPreferredSize(new Dimension(300, 30));
        hGbc.gridy = 2; hGbc.insets = new Insets(0, 0, 5, 0); heroPane.add(battleHpBar, hGbc);

        battleMpBar = new RiftBar(100, Color.BLUE); battleMpBar.setPreferredSize(new Dimension(200, 15));
        hGbc.gridy = 3; heroPane.add(battleMpBar, hGbc);
        arena.add(heroPane);

        // RIGHT BOSS
        JPanel mobPane = new JPanel(new GridBagLayout()); mobPane.setOpaque(false);
        GridBagConstraints mGbc = new GridBagConstraints();
        mGbc.gridx = 0; mGbc.fill = GridBagConstraints.HORIZONTAL; mGbc.anchor = GridBagConstraints.CENTER;

        mobPortrait = new JLabel("", SwingConstants.CENTER);
        mobPortrait.setBorder(new LineBorder(Color.DARK_GRAY, 2)); mobPortrait.setPreferredSize(new Dimension(300, 300));
        mGbc.gridy = 0; mGbc.insets = new Insets(0, 0, 15, 0); mobPane.add(mobPortrait, mGbc);

        mobNameLabel = new JLabel("BOSS", SwingConstants.CENTER);
        mobNameLabel.setForeground(Color.RED); mobNameLabel.setFont(HEADER_FONT);
        mGbc.gridy = 1; mGbc.insets = new Insets(0, 0, 5, 0); mobPane.add(mobNameLabel, mGbc);

        mobHpBar = new RiftBar(100, Color.RED); mobHpBar.setPreferredSize(new Dimension(300, 30));
        mGbc.gridy = 2; mobPane.add(mobHpBar, mGbc);
        arena.add(mobPane);
        p.add(arena, BorderLayout.CENTER);

        // BOTTOM
        JPanel southWrapper = new JPanel(new BorderLayout()); southWrapper.setOpaque(false);
        battleLogPane = new JTextPane(); battleLogPane.setOpaque(false);
        battleLogPane.setEditable(false); battleLogPane.setFont(RETRO_FONT);
        battleLogPane.setForeground(Color.WHITE); battleLogPane.setPreferredSize(new Dimension(800, 80));

        StyledDocument doc = battleLogPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        JPanel logContainer = new JPanel(new BorderLayout()); logContainer.setOpaque(false);
        logContainer.setBorder(new EmptyBorder(10, 100, 10, 100));
        logContainer.add(battleLogPane, BorderLayout.CENTER);
        southWrapper.add(logContainer, BorderLayout.NORTH);

        JPanel skills = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20)); skills.setOpaque(false);
        skills.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.WHITE));

        btnSkill1 = new RiftButton("SKILL 1"); btnSkill2 = new RiftButton("SKILL 2"); btnSkill3 = new RiftButton("SKILL 3");
        Dimension skillDim = new Dimension(220, 50);
        btnSkill1.setPreferredSize(skillDim); btnSkill2.setPreferredSize(skillDim); btnSkill3.setPreferredSize(skillDim);
        btnSkill1.addActionListener(e -> doSkill(1)); btnSkill2.addActionListener(e -> doSkill(2)); btnSkill3.addActionListener(e -> doSkill(3));
        skills.add(btnSkill1); skills.add(btnSkill2); skills.add(btnSkill3);

        southWrapper.add(skills, BorderLayout.SOUTH);
        p.add(southWrapper, BorderLayout.SOUTH);
        return p;
    }

    // logic

    public void setPlayerObject(Character p) {
        this.player = p;
        updateSkillButtons();
        updateViewport();

        mainCardLayout.show(containerPanel, "EXPLORE");
        startStorySequence(); // AUTOMATIC START
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

    //story sequence
    private void startStorySequence() {
        bottomCardLayout.show(bottomPanel, "TEXT");

        String[] storyLines = {
                "You step into The Rift...",
                "This place is not of the world.",
                "It is a wound in reality, echoing with shadow and sorrow.",
                "A faint, golden echo shimmers in the darkness...",
                "'Our Champion! He has saved Senthra from the Calamity!'",
                "The echo shifts... a king and his advisors in a dark room.",
                "'His power is too great. He is a threat to the throne.'",
                "'The gods demand a sacrifice... the Champion will be our offering.'",
                "The vision sharpens. The hero kneels at an altar.",
                "His own allies surround him with chains.",
                "'It is the only way, my friend. Forgive us.'",
                "You hear a scream that transcends time...",
                "'LIARS! BETRAYERS! YOU USED ME!'",
                "The world shatters. The vision goes dark.",
                "FACE THE GUARDIAN..."
        };

        playStoryLine(storyLines, 0, () -> triggerBoss(new World5Boss.Kyros()));
    }

    private void playStoryLine(String[] lines, int index, Runnable onComplete) {
        if (index >= lines.length) {
            onComplete.run();
            return;
        }

        dialoguePane.setText(lines[index]);

        new Timer().schedule(new TimerTask() {
            @Override public void run() {
                SwingUtilities.invokeLater(() -> playStoryLine(lines, index + 1, onComplete));
            }
        }, 4000); // 4 seconds per line
    }

    //BOSS LOGIC

    private void triggerBoss(World5Boss boss) {
        this.currentBoss = boss;
        startVersusSequence();
    }

    private void startVersusSequence() {
        mainCardLayout.show(containerPanel, "VERSUS");

        if (currentBoss instanceof World5Boss.DemonLord) {
            vsStatusLabel.setText("The Demon Lord unleashes his full power!");
            vsStatusLabel.setForeground(Color.RED);
        } else {
            vsStatusLabel.setText("General Kyros blocks the path!");
            vsStatusLabel.setForeground(RIFT_PURPLE);
        }

        // Load Player Image
        String pPath = "/images/playable/" + player.className.toLowerCase() + ".png";
        URL pUrl = getClass().getResource(pPath);
        if (pUrl != null) {
            ImageIcon pIcon = new ImageIcon(pUrl);
            vsPlayerLabel.setIcon(new ImageIcon(pIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
        }

        // Load Boss Image
        String bossFile = (currentBoss instanceof World5Boss.Kyros) ? "Kyros.png" : "Demon Lord.png";
        String mPath = "/images/Boss/" + bossFile;

        try {
            URL mUrl = getClass().getResource(mPath);
            if(mUrl != null) {
                ImageIcon mIcon = new ImageIcon(mUrl);
                Image sc = mIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                vsMobLabel.setIcon(new ImageIcon(sc)); vsMobLabel.setText("");
                mobPortrait.setIcon(new ImageIcon(sc)); mobPortrait.setText("");
            } else {
                vsMobLabel.setIcon(null); vsMobLabel.setText(currentBoss.name);
            }
        } catch (Exception e) { vsMobLabel.setText(currentBoss.name); }

        new Timer().schedule(new TimerTask() {
            @Override public void run() { SwingUtilities.invokeLater(() -> startBattle()); }
        }, 4000);
    }

    private void startBattle() {
        mainCardLayout.show(containerPanel, "BATTLE");
        battleLogPane.setText("It is your turn! Choose a skill.");

        mobNameLabel.setText(currentBoss.name.toUpperCase());
        mobHpBar.setMax(currentBoss.maxHp); mobHpBar.updateValue(currentBoss.hp);

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
        btnSkill1.setEnabled(enabled); btnSkill2.setEnabled(enabled); btnSkill3.setEnabled(enabled);
    }

    private void doSkill(int choice) {
        if (currentBoss == null) return;
        setButtonsEnabled(false);

        String res = player.useSkill(choice, currentBoss);
        mobHpBar.updateValue(currentBoss.hp);
        battleMpBar.updateValue(player.mana);
        battleLogPane.setText(res);

        if (!currentBoss.isAlive()) {
            new Timer().schedule(new TimerTask() {
                @Override public void run() { SwingUtilities.invokeLater(() -> endBattle(true)); }
            }, 1500);
            return;
        }

        // Boss Turn
        new java.util.Timer().schedule(new TimerTask() {
            @Override public void run() { SwingUtilities.invokeLater(() -> {

                String enemyAtk;
                if (rng.nextInt(100) < 40) enemyAtk = currentBoss.specialSkill(player);
                else enemyAtk = currentBoss.attack(player);

                battleHpBar.updateValue(player.hp);
                battleLogPane.setText(enemyAtk);

                if (player.hp <= 0) {
                    new Timer().schedule(new TimerTask() { @Override public void run() { SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(FinalWorldPanel.this, "YOUR JOURNEY ENDS HERE.", "GAME OVER", JOptionPane.ERROR_MESSAGE);
                        mainFrame.showPanel("intro");
                    }); } }, 1500);
                } else {
                    new java.util.Timer().schedule(new TimerTask() { @Override public void run() { SwingUtilities.invokeLater(() -> {
                        battleLogPane.setText("It is your turn.");
                        setButtonsEnabled(true);
                    }); } }, 1500);
                }
            });
            }
        }, 1500);
    }

    private void endBattle(boolean win) {
        if (win) {
            battleLogPane.setText("VICTORY!");
            new Timer().schedule(new TimerTask() {
                @Override public void run() {
                    SwingUtilities.invokeLater(() -> {
                        if (currentBoss instanceof World5Boss.Kyros) {
                            openRewardChest();
                        } else {
                            playEndingSequence();
                        }
                    });
                }
            }, 2000);
        }
    }

    private void openRewardChest() {
        mainCardLayout.show(containerPanel, "EXPLORE");
        bottomCardLayout.show(bottomPanel, "TEXT");
        updateViewport();

        Object[] opts = {"HEAL", "MANA", "DMG UP"};

        // USE CUSTOM RIFT DIALOG FOR REWARD
        int c = RiftDialog.showOptionDialog(this,
                "Kyros fades... Choose your boon:", "REWARD", opts);

        String msg = "";
        if (c == 0) {
            player.maxHp += 50; player.hp = player.maxHp;
            msg = "Vitality surges through you!";
        } else if (c == 1) {
            player.maxMana += 30; player.mana = player.maxMana;
            msg = "Your mind expands with cosmic energy.";
        } else {
            player.tempDamage += 15;
            msg = "Your weapon hums with ancient power.";
        }

        dialoguePane.setText(msg);

        String[] demonLordIntro = {
                "You approach the broken throne. The Demon Lord rises.",
                "He sees the last of Kyros's shadow fade from your weapon.",
                "'KYROS... NO! YOU TOOK HIM FROM ME! NOW... YOU DIE!'",
                "FIGHT THE DEMON LORD!"
        };

        new Timer().schedule(new TimerTask() {
            @Override public void run() { SwingUtilities.invokeLater(() ->
                    playStoryLine(demonLordIntro, 0, () -> triggerBoss(new World5Boss.DemonLord()))
            ); }
        }, 3000);
    }

    private void playEndingSequence() {
        mainCardLayout.show(containerPanel, "EXPLORE");
        bottomCardLayout.show(bottomPanel, "TEXT");

        updateViewport();

        String[] endingLines = {
                "With the final blow, the Demon Lord does not scream.",
                "The demonic shadows flee, revealing the human hero he once was.",
                "He looks at you. A single tear cuts through the grime.",
                "'Kyros.. I am coming. Thank you...'",
                "You have brought peace to the Betrayed Champion.",
                "The Rift seals. The world is saved.",
                "But you alone carry the truth: The world you saved was built on a lie."
        };

        showEndingLine(endingLines, 0);
    }

    private void showEndingLine(String[] lines, int index) {
        if (index >= lines.length) {
            // USE CUSTOM RIFT DIALOG FOR ENDING
            Object[] endOptions = {"EXIT GAME"};
            RiftDialog.showOptionDialog(this,
                    "CONGRATULATIONS!\nYOU HAVE CLEARED BLOOD OF THE RIFT.",
                    "LEGENDARY VICTORY", endOptions);
            System.exit(0);
            return;
        }

        dialoguePane.setText(lines[index]);

        new Timer().schedule(new TimerTask() {
            @Override public void run() {
                SwingUtilities.invokeLater(() -> showEndingLine(lines, index + 1));
            }
        }, 4000);
    }

    private void updateStatus() {
        if(player != null) {
            playerHpBar.setMax(player.maxHp); playerHpBar.updateValue(player.hp);
            playerMpBar.setMax(player.maxMana); playerMpBar.updateValue(player.mana);
        }
    }
}