package ui;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;

public class LoadingPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private MainFrame mainFrame;
    private JTextPane storyPane;
    private Image bgImage;

    // Used a class-level timer para stop
    private Timer typeTimer;

    private final String[] storyLines = {
            "You wake up from a deep sleep...",
            "The world has changed..."
    };//dungangan pani

    private int lineIndex = 0;
    private int charIndex = 0;

    public LoadingPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.BLACK);

        URL imgUrl = getClass().getResource("/images/backgroundpic/loading1.jpg");
        if (imgUrl != null) bgImage = new ImageIcon(imgUrl).getImage();

        JPanel contentPane = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setOpaque(false);
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        storyPane = new JTextPane();
        storyPane.setEditable(false);
        storyPane.setOpaque(false);
        storyPane.setForeground(Color.WHITE);
        storyPane.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 22));

        StyledDocument doc = storyPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        GridBagConstraints gbcStory = new GridBagConstraints();
        gbcStory.gridx = 0; gbcStory.gridy = 0;
        gbcStory.fill = GridBagConstraints.HORIZONTAL;
        gbcStory.weightx = 1; gbcStory.weighty = 1;
        gbcStory.insets = new Insets(20, 20, 20, 20);
        centerPanel.add(storyPane, gbcStory);

        add(contentPane, BorderLayout.CENTER);
    }

    public void startLoading(String playerName, String selectedClass) {
        // Reset State
        lineIndex = 0;
        charIndex = 0;
        storyPane.setText("");

        // Kill any existing timer to prevent ghosts
        if (typeTimer != null && typeTimer.isRunning()) {
            typeTimer.stop();
        }

        // Create new Timer
        typeTimer = new Timer(50, e -> {
            if (lineIndex >= storyLines.length) {
                ((Timer) e.getSource()).stop(); // Stop typing

                // --- REDIRECT TO HOME WORLD ---
                Timer delay = new Timer(1000, ev -> {
                    ((Timer) ev.getSource()).stop(); // Stop the delay timer
                    mainFrame.showPanel("homeWorld");
                });

                delay.setRepeats(false);
                delay.start();
                return;
            }

            String currentLine = storyLines[lineIndex];
            if (charIndex < currentLine.length()) {
                storyPane.setText(storyPane.getText() + currentLine.charAt(charIndex));
                charIndex++;
            } else {
                charIndex = 0;
                lineIndex++;
                storyPane.setText(storyPane.getText() + "\n");
            }
        });

        typeTimer.setRepeats(true);
        typeTimer.start();
    }
}