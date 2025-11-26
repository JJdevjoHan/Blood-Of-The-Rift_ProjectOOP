package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.net.URL;

class IntroPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private MainFrame mainFrame;
    private Image bgImage;

    public IntroPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.BLACK);

        // SAFE IMAGE LOADING
        URL imgUrl = getClass().getResource("/images/backgroundpic/intro.png");
        if (imgUrl != null) {
            bgImage = new ImageIcon(imgUrl).getImage();
        } else {
            System.err.println("Warning: Intro background not found.");
        }

        JPanel contentPane = new JPanel(new BorderLayout()) {
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
        contentPane.setOpaque(false);
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(contentPane, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        JLabel lblTitle = new JLabel("Blood Of The Rift", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 60));

        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.insets = new Insets(20, 10, 20, 10);
        centerPanel.add(lblTitle, gbcTitle);

        JLabel lblSkip = new JLabel("Skip intro?", SwingConstants.CENTER);
        lblSkip.setForeground(Color.WHITE);
        lblSkip.setFont(new Font("Tahoma", Font.PLAIN, 16));

        GridBagConstraints gbcSkip = new GridBagConstraints();
        gbcSkip.gridx = 0;
        gbcSkip.gridy = 1;
        gbcSkip.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(lblSkip, gbcSkip);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        buttonPanel.setOpaque(false);

        JToggleButton yesButton = new JToggleButton("Yes");
        yesButton.addActionListener(e -> { if (yesButton.isSelected()) mainFrame.showPanel("home"); });

        JToggleButton noButton = new JToggleButton("No");
        noButton.addActionListener(e -> { if (noButton.isSelected()) mainFrame.showPanel("dialogue"); });

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridx = 0;
        gbcButtons.gridy = 2;
        gbcButtons.insets = new Insets(20, 10, 20, 10);
        centerPanel.add(buttonPanel, gbcButtons);

        fadeIn();
    }

    private void fadeIn() {
        // Simple fade simulation
        new Thread(() -> {
            try { Thread.sleep(100); repaint(); } catch (Exception e) {}
        }).start();
    }
}