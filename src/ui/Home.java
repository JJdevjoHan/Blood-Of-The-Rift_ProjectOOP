package ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.net.URL;

public class Home extends JPanel {
    private MainFrame mainFrame;
    private Character player;

    // UI COMPONENTS
    private JLabel charPortraitLabel;
    private JPanel roomPanel;
    private JButton chestButton;
    private JTextPane dialogueArea;

    // BUTTONS
    private RiftButton btnAction1;
    private RiftButton btnAction2;

    // IMAGES
    private ImageIcon chestClosedIcon;
    private ImageIcon chestOpenIcon;
    private ImageIcon npcIcon;
    private ImageIcon heroIcon;
    private Image roomBgImage;

    // STATE
    private int progressStep = 0;

    public Home(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(new Color(15, 15, 20));

        loadImages();

        // CENTER SPLIT: [ CHARACTER ] | [ ROOM ]
        JPanel splitPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        splitPanel.setBorder(new EmptyBorder(20, 20, 0, 20));
        splitPanel.setOpaque(false);

        // LEFT: LARGE CHARACTER ART
        JPanel charPanel = new JPanel(new BorderLayout());
        charPanel.setOpaque(false);
        charPanel.setBorder(new CompoundBorder(
                new LineBorder(Color.WHITE, 2),
                new EmptyBorder(10, 10, 10, 10)
        ));

        charPortraitLabel = new JLabel("", SwingConstants.CENTER);
        charPortraitLabel.setForeground(Color.CYAN);
        charPortraitLabel.setFont(new Font("Impact", Font.PLAIN, 24));

        charPanel.add(charPortraitLabel, BorderLayout.CENTER);
        splitPanel.add(charPanel);

        // RIGHT: ROOM + CHEST
        roomPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (roomBgImage != null) {
                    g.drawImage(roomBgImage, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 100));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        roomPanel.setOpaque(false);
        roomPanel.setBorder(new CompoundBorder(
                new LineBorder(Color.WHITE, 2),
                new EmptyBorder(10, 10, 10, 10)
        ));

        chestButton = new JButton();
        chestButton.setContentAreaFilled(false);
        chestButton.setBorderPainted(false);
        chestButton.setFocusPainted(false);
        chestButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chestButton.setPreferredSize(new Dimension(200, 200));

        if (chestClosedIcon != null) {
            chestButton.setIcon(chestClosedIcon);
            chestButton.setText("");
        } else {
            chestButton.setText("[ CHEST ]");
            chestButton.setForeground(Color.WHITE);
        }

        chestButton.addActionListener(e -> handleChestClick());
        roomPanel.add(chestButton);
        splitPanel.add(roomPanel);

        add(splitPanel, BorderLayout.CENTER);

        // BOTTOM: DIALOGUE & CONTROLS
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(800, 200));
        bottomPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JPanel dialogueContainer = new JPanel(new BorderLayout());
        dialogueContainer.setOpaque(false);
        dialogueContainer.setBorder(new CompoundBorder(
                new LineBorder(Color.WHITE, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        dialogueArea = new JTextPane();
        dialogueArea.setText("You wake up from a deep sleep. You look around your home for equipment...");
        dialogueArea.setFont(new Font("Serif", Font.BOLD, 20));
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setOpaque(false);
        dialogueArea.setEditable(false);

        StyledDocument doc = dialogueArea.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        dialogueContainer.add(dialogueArea, BorderLayout.CENTER);
        bottomPanel.add(dialogueContainer);
        bottomPanel.add(Box.createVerticalStrut(15));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        btnPanel.setOpaque(false);

        btnAction1 = new RiftButton("WALK TO CHEST");
        btnAction1.setPreferredSize(new Dimension(250, 45));
        btnAction1.addActionListener(e -> handleProgress());

        btnAction2 = new RiftButton("LEAVE HOME");
        btnAction2.setPreferredSize(new Dimension(250, 45));
        btnAction2.addActionListener(e -> handleFlavorText());

        btnPanel.add(btnAction1);
        btnPanel.add(btnAction2);
        bottomPanel.add(btnPanel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setPlayer(Character p) {
        this.player = p;
        this.progressStep = 0; // Reset progress when entering again

        // Reset UI Buttons
        btnAction1.setText("WALK TO CHEST");
        btnAction2.setVisible(true);
        chestButton.setEnabled(false);
        if (chestClosedIcon != null) chestButton.setIcon(chestClosedIcon);

        setDialogueText("You wake up from a deep sleep. You look around your home for equipment...");

        // Load NPC Image
        String npcPath = "/images/playable/npc.png";
        npcIcon = loadScaledIcon(npcPath, 350, 400);

        // Load Hero Image (With Safety Check)
        String heroPath = "/images/playable/" + player.className.toLowerCase() + ".png";
        heroIcon = loadScaledIcon(heroPath, 350, 400);

        // Set Icon or Fallback Text
        if (npcIcon != null) {
            charPortraitLabel.setIcon(npcIcon);
            charPortraitLabel.setText("");
        } else {
            charPortraitLabel.setIcon(null);
            charPortraitLabel.setText("<html><center>[ NPC ]</center></html>");
        }
    }

    private void handleProgress() {
        if (progressStep == 0) {
            setDialogueText("You approach the old chest in the corner of the room.");
            btnAction1.setText("OPEN CHEST");
            chestButton.setEnabled(true);
            progressStep = 1;
        }
        else if (progressStep == 1) {
            openChest();
        }
        else if (progressStep == 2) {
            int ch = JOptionPane.showConfirmDialog(this, "Leave home and start your journey?", "Depart", JOptionPane.YES_NO_OPTION);
            if (ch == JOptionPane.YES_OPTION) {
                mainFrame.enterGrassyPlains(player);
            }
        }
    }

    private void handleChestClick() {
        if (progressStep == 1) openChest();
    }

    private void openChest() {
        String msg = "You found a chest with equipment!\n";

        switch (player.className) {
            case "Warrior":
                player.maxHp += 80; player.hp += 80; player.maxMana += 30; player.mana += 30;
                msg += "You obtained DUAL WAR AXES!"; break;
            case "Paladin":
                player.maxHp += 120; player.hp += 120; player.maxMana += 70; player.mana += 70;
                msg += "You obtained SWORD & SHIELD."; break;
            case "Mage":
                player.maxHp += 20; player.hp += 20; player.maxMana += 100; player.mana += 100;
                msg += "You obtained WAND & HAT."; break;
        }

        // Show Hero Image
        if (heroIcon != null) {
            charPortraitLabel.setIcon(heroIcon);
            charPortraitLabel.setText("");
        } else {
            charPortraitLabel.setIcon(null);
            charPortraitLabel.setText("<html><center>[ " + player.className + " ]</center></html>");
        }

        if (chestOpenIcon != null) {
            chestButton.setIcon(chestOpenIcon);
        }

        setDialogueText(msg);
        btnAction1.setText("LEAVE HOME");
        btnAction2.setVisible(false);
        progressStep = 2;
    }

    private void handleFlavorText() {
        setDialogueText("Don't be bida2 okay? Go find your equipment before starting your journey.");
    }

    private void setDialogueText(String text) {
        dialogueArea.setText(text);
        // Re-apply center alignment
        StyledDocument doc = dialogueArea.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

    private void loadImages() {
        chestClosedIcon = loadScaledIcon("/images/items/closeChest.png", 200, 200);
        chestOpenIcon = loadScaledIcon("/images/items/openChest.png", 200, 200);
        // Add room bg if you have it
        URL roomUrl = getClass().getResource("/images/backgroundpic/home_room.png");
        if(roomUrl != null) roomBgImage = new ImageIcon(roomUrl).getImage();
    }

    private ImageIcon loadScaledIcon(String path, int w, int h) {
        URL url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Could not find image: " + path);
            return null;
        }
        ImageIcon icon = new ImageIcon(url);
        return new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }
}