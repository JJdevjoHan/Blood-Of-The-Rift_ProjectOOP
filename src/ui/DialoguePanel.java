package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.net.URL;

class DialoguePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private MainFrame mainFrame;
    private JTextPane storyPane;
    private int index = 0;
    private Image bgImage;

    private String[] storyLines = {
            "A thousand years ago...",
            "A mighty hero was chosen to save the Kingdom of Sethra from the wrath of gods",
            "Betrayed by his own people, he was used as a scapegoat and sacrificed to seal the calamities",
            "But the world still fell apart, a great rift tore reality into multiple realms",
            "Although the champion did not die",
            "Instead, he was cursed, left less than human, and forgotten in the shadows of the rift",
            "Eight hundred years later, new heroes rise from the divided worlds...",
            "Believing the fallen Hero to be the cause of the chaos, they set out to defeat him"
    };

    public DialoguePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        URL imgUrl = getClass().getResource("/images/backgroundpic/storyline.png");
        if (imgUrl != null) {
            bgImage = new ImageIcon(imgUrl).getImage();
        }

        // 1. Main Background Panel
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setOpaque(false);
        add(backgroundPanel, BorderLayout.CENTER);

        // 2. Center Container
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false);
        backgroundPanel.add(centerContainer, BorderLayout.CENTER);

        // 3. The Text Box Panel (FIXED TRANSPARENCY)
        JPanel textBoxPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                // Manually draw the semi-transparent background
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        textBoxPanel.setOpaque(false); // Vital for the fix!
        textBoxPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        storyPane = new JTextPane();
        storyPane.setEditable(false);
        storyPane.setOpaque(false);
        storyPane.setForeground(Color.WHITE);
        storyPane.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 32));

        // Center text alignment
        StyledDocument doc = storyPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        textBoxPanel.add(storyPane, BorderLayout.CENTER);

        // Add Constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(50, 50, 50, 50);
        centerContainer.add(textBoxPanel, gbc);

        // Timer Logic
        Timer timer = new Timer(4000, e -> {
            if (index < storyLines.length) {
                storyPane.setText(storyLines[index]);
                index++;
            } else {
                ((Timer) e.getSource()).stop();
                mainFrame.showPanel("home");
            }
        });
        timer.setInitialDelay(500);
        timer.start();
    }
}