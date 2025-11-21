package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;

public class HomePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private MainFrame mainFrame;
    private JTextField name;
    private JComboBox<String> classbox;
    private JTable table;

    public HomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Background image setup
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/images/backgroundpic/background3.png"));
        URL url = getClass().getResource("/images/backgroundpic/background3.png");
        System.out.println("Resource URL: " + url);
        Image bgImage = bgIcon.getImage();

        // Main content panel with background
        JPanel contentPane = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setOpaque(false);

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        contentPane.add(topPanel, BorderLayout.NORTH);

        // Title
        JLabel lblTitle = new JLabel("Blood Of The Rift", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 60));
        topPanel.add(lblTitle);
        topPanel.add(Box.createVerticalStrut(20));

        String[] columnNames = {"Paladin", "Mage", "Warrior"};

        // Images for classes
        ImageIcon paladinIcon = new ImageIcon(getClass().getResource("/images/playable/paladin.png"));
        ImageIcon mageIcon = new ImageIcon(getClass().getResource("/images/playable/mage.png"));
        ImageIcon warriorIcon = new ImageIcon(getClass().getResource("/images/playable/warrior.png"));

        Object[][] data = {
            {paladinIcon, mageIcon, warriorIcon}
        };

        table = new JTable(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return ImageIcon.class;
            }
        };

        table.setRowHeight(100);
        table.setBackground(Color.BLACK);
        table.setForeground(Color.WHITE);

        // Styling for header
        table.getTableHeader().setBackground(Color.BLACK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 16));
        table.setBorder(new LineBorder(Color.BLACK));

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.BLACK);
        header.setForeground(Color.WHITE);
        header.setBorder(new LineBorder(Color.BLACK));
        topPanel.add(header);
        topPanel.add(table);

        // Character creation
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new LineBorder(Color.WHITE, 1, true));
        contentPane.add(centerPanel, BorderLayout.CENTER);

        GridBagConstraints gbc;

        // Label for name
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel labelName = new JLabel("What is your Name?");
        labelName.setForeground(Color.WHITE);
        centerPanel.add(labelName, gbc);

        // Name field
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        name = new JTextField(15);
        centerPanel.add(name, gbc);

        // Label for class
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel labelClass = new JLabel("Choose your class:");
        labelClass.setForeground(Color.WHITE);
        centerPanel.add(labelClass, gbc);

        // Class combo box
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        classbox = new JComboBox<>(new String[]{"Warrior", "Mage", "Paladin"});
        centerPanel.add(classbox, gbc);

        // Confirm button
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JToggleButton confirmBtn = new JToggleButton("Confirm");
        centerPanel.add(confirmBtn, gbc);

        // Logic for confirm button
        confirmBtn.addActionListener(e -> {
            String playerName = name.getText().trim();
            String selectedClass = (String) classbox.getSelectedItem();

            // Validation
            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a name.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (playerName.length() < 5) {
                JOptionPane.showMessageDialog(this, "Name must be at least 5 letters.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (playerName.length() > 5) {
                JOptionPane.showMessageDialog(this, "Name must NOT exceed 5 letters.", "Error", JOptionPane.WARNING_MESSAGE);
                name.setText(playerName.substring(0, 5));
                return;
            }

            // Show class info
            showClassInfo(selectedClass);

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to take the " + selectedClass + " class?",
                    "Confirm Class",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) {
                return; // Stop if NO
            }

            // Proceed
            JOptionPane.showMessageDialog(this,
                    "Character created!\nName: " + playerName + "\nClass: " + selectedClass);

            if (confirmBtn.isSelected()) {
                mainFrame.startGame(playerName, selectedClass);
                mainFrame.showPanel("loadingPanel");
            }
        });

        add(contentPane, BorderLayout.CENTER); // Add the content to this panel
    }

    private void showClassInfo(String className) {
        Character tempChar = null; // Temp instance for stats
        String description = "";

        switch (className) {
            case "Warrior":
                tempChar = new Warrior("Temp");
                description = "A battle-hardened fighter who excels in physical combat,\nrelying on strength, armor, and weapon mastery.";
                break;
            case "Mage":
                tempChar = new Mage("Temp");
                description = "A master of arcane arts who casts powerful spells,\nmanipulating elements and reality through magical knowledge.";
                break;
            case "Paladin":
                tempChar = new Paladin("Temp");
                description = "A holy knight devoted to justice and righteousness,\ncombining martial prowess with divine magic to protect and heal.";
                break;
        }

        if (tempChar != null) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Color.WHITE);

            JLabel classLabel = new JLabel("Class Status: " + className);
            classLabel.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 16));
            classLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Instance stats
            JLabel statsLabel = new JLabel("HP: " + tempChar.maxHp + "   Mana: " + tempChar.maxMana);
            statsLabel.setFont(new java.awt.Font("Serif", java.awt.Font.PLAIN, 14));
            statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextPane descArea = new JTextPane();
            descArea.setText(description);
            descArea.setFont(new java.awt.Font("Serif", java.awt.Font.ITALIC, 14));
            descArea.setEditable(false);
            descArea.setOpaque(false);
            descArea.setAlignmentX(Component.LEFT_ALIGNMENT);

            panel.add(classLabel);
            panel.add(Box.createVerticalStrut(5));
            panel.add(statsLabel);
            panel.add(Box.createVerticalStrut(5));
            panel.add(descArea);

            JOptionPane.showMessageDialog(
                this,
                panel,
                "Class Info: " + className,
                JOptionPane.PLAIN_MESSAGE
            );
        }
    }
}