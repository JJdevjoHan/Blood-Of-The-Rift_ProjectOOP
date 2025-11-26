package ui;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;

public class HomePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private MainFrame mainFrame;
    private JTextField name;
    private JComboBox<String> classbox;
    private Image bgImage;

    public HomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.BLACK);

        // SAFE IMAGE LOADING
        URL imgUrl = getClass().getResource("/images/backgroundpic/home.png");
        if (imgUrl != null) {
            bgImage = new ImageIcon(imgUrl).getImage();
        }

        JPanel contentPane = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPane.setOpaque(false);
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        contentPane.add(topPanel, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel("Blood Of The Rift", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 60));
        topPanel.add(lblTitle);
        topPanel.add(Box.createVerticalStrut(20));

        // Safe Icon Loading
        ImageIcon paladinIcon = loadIcon("/images/playable/paladin.png");
        ImageIcon mageIcon = loadIcon("/images/playable/mage.png");
        ImageIcon warriorIcon = loadIcon("/images/playable/warrior.png");

        String[] columnNames = {"Paladin", "Mage", "Warrior"};
        Object[][] data = {{paladinIcon, mageIcon, warriorIcon}};

        JTable table = new JTable(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) { return ImageIcon.class; }
        };

        table.setRowHeight(100);
        table.setOpaque(false);
        table.setBackground(new Color(0,0,0,0));
        table.setShowGrid(false);

        JTableHeader header = table.getTableHeader();
        header.setOpaque(false);
        header.setBackground(new Color(0,0,0,0));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Times New Roman", Font.BOLD, 16));

        topPanel.add(header);
        topPanel.add(table);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new LineBorder(Color.WHITE, 1, true));
        contentPane.add(centerPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel labelName = new JLabel("What is your Name?");
        labelName.setForeground(Color.WHITE);
        centerPanel.add(labelName, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        name = new JTextField(15);
        centerPanel.add(name, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel labelClass = new JLabel("Choose your class:");
        labelClass.setForeground(Color.WHITE);
        centerPanel.add(labelClass, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        classbox = new JComboBox<>(new String[]{"Warrior", "Mage", "Paladin"});
        centerPanel.add(classbox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        JToggleButton confirmBtn = new JToggleButton("Confirm");
        centerPanel.add(confirmBtn, gbc);

        confirmBtn.addActionListener(e -> {
            String playerName = name.getText().trim();
            String selectedClass = (String) classbox.getSelectedItem();

            if (playerName.isEmpty() || playerName.length() < 3) {
                JOptionPane.showMessageDialog(this, "Name must be at least 3 chars.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (confirmBtn.isSelected()) {
                mainFrame.startGame(playerName, selectedClass);
            }
        });

        add(contentPane, BorderLayout.CENTER);
    }

    private ImageIcon loadIcon(String path) {
        URL url = getClass().getResource(path);
        if (url != null) return new ImageIcon(url);
        return new ImageIcon(); // Empty icon if not found
    }
}