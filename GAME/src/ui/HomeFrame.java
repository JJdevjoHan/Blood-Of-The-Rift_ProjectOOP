package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;  

public class HomeFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField name;
    private JComboBox<String> classbox;
    private JTable table;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomeFrame().setVisible(true));
    }

    public HomeFrame() {
        setTitle("Home Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);

        // MAIN CONTAINER
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(Color.BLACK);
        setContentPane(contentPane);

        // ============================
        //  TOP PANEL: Title + Table
        // ============================
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        contentPane.add(topPanel, BorderLayout.NORTH);

        // Title
        JLabel lblTitle = new JLabel("Blood Of The Rift", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 60));
        topPanel.add(lblTitle);
        topPanel.add(Box.createVerticalStrut(20));

        // ============================
        //  Table with Images
        // ============================
        String[] columnNames = {"Paladin", "Mage", "Warrior"};

        // Load images (update paths accordingly)
        ImageIcon paladinIcon = new ImageIcon("C:\\Users\\JOHAN\\eclipse-workspace\\AGAINUI\\src\\ui\\images\\playable\\paladin.png");
        ImageIcon mageIcon = new ImageIcon("C:\\Users\\JOHAN\\eclipse-workspace\\AGAINUI\\src\\ui\\images\\playable\\mage.png");
        ImageIcon warriorIcon = new ImageIcon("C:\\Users\\JOHAN\\eclipse-workspace\\AGAINUI\\src\\ui\\images\\playable\\warrior.png");

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

        // Header styling
        table.getTableHeader().setBackground(Color.BLACK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 16));
        table.setBackground(Color.BLACK);
        table.setForeground(Color.WHITE);
        table.setBorder(new LineBorder(Color.BLACK));

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.BLACK);
        header.setForeground(Color.WHITE);
        header.setBorder(new LineBorder(Color.BLACK));
        topPanel.add(header);
        topPanel.add(table);
        
        
        // ============================
        //  CENTER PANEL: Character Creation
        // ============================
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new LineBorder(Color.WHITE, 1, true));
        contentPane.add(centerPanel, BorderLayout.CENTER);

        GridBagConstraints gbc;

        // Name Label
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel labelName = new JLabel("What is your Name?");
        labelName.setForeground(Color.WHITE);
        centerPanel.add(labelName, gbc);

        // Name Field
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        name = new JTextField(15);
        centerPanel.add(name, gbc);

        // Class Label
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel labelClass = new JLabel("Choose your class:");
        labelClass.setForeground(Color.WHITE);
        centerPanel.add(labelClass, gbc);

        // Class Box
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        classbox = new JComboBox<>(new String[]{"Warrior", "Mage", "Paladin"});
        centerPanel.add(classbox, gbc);

        // Confirm Button
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JToggleButton confirmBtn = new JToggleButton("Confirm");
        centerPanel.add(confirmBtn, gbc);
        

     // Confirm Button Logic
        confirmBtn.addActionListener(e -> {
            String playerName = name.getText().trim();
            String selectedClass = (String) classbox.getSelectedItem();

            // Basic validations
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
            
            
            // ================================
            // CONFIRMATION MESSAGE ADDED HERE
            // ================================
            
            // Show class info BEFORE confirmation (now using real data from Character classes)
            showClassInfo(selectedClass);

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to take the " + selectedClass + " class?",
                    "Confirm Class",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) {
                return; // STOP if player selects NO
            }

            // If YES → proceed
            JOptionPane.showMessageDialog(this,
                    "Character created!\nName: " + playerName + "\nClass: " + selectedClass);

            if (confirmBtn.isSelected()) {
                new Loading(playerName, selectedClass).setVisible(true);
                dispose();
            }
            
            new GrassyPlains(playerName, selectedClass);
        });
        

        
    }
    
    // Updated to dynamically pull stats from Character subclasses
    private void showClassInfo(String className) {
        Character tempChar = null;  // Temporary instance to get stats (using dummy name)
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
            classLabel.setFont(new Font("Serif", Font.BOLD, 16));
            classLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Use real maxHp and maxMana from the Character instance
            JLabel statsLabel = new JLabel("HP: " + tempChar.maxHp + "   Mana: " + tempChar.maxMana);
            statsLabel.setFont(new Font("Serif", Font.PLAIN, 14));
            statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextArea descArea = new JTextArea(description);
            descArea.setFont(new Font("Serif", Font.ITALIC, 14));
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
