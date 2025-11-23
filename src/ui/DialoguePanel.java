package ui;

import javax.swing.JPanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

class DialoguePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private MainFrame mainFrame;
    private JTextPane storyPane;
    private int index = 0;

    private String[] storyLines = {
        "In sooth, a thousand years ago....",
        "A mighty hero was chosen to save the Kingdom of Senthra from the ire of the gods.",
        "Betrayed by his own kin, he was made a scapegoat and sacrificed unto the gods to seal the calamities.",
        "Yet, the world didst still fall asunder, a great rift tore reality into divers realms.",
        "The champion didst not perish; nay, he was cursed, left less than a mortal, and forgotten in the shadows of the rift.",
        "Eight hundred years later....",
        "New heroes rise from the divided worlds.",
        "Believing the fallen Hero to be the cause of the chaos, they set out to defeat him.",
        "But as they uncover the truth, they learn he was never the villain, only the first victim.",
        "In the final battle, the champion accepts his fate.",
        "With the help of the heroes, he offers himself as the true sacrifice, closing the rift and restoring the world at last."
    };

    public DialoguePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/images/backgroundpic/storyline1.png"));
        Image bgImage = bgIcon.getImage();

        JPanel contentPane = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(contentPane, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        storyPane = new JTextPane();
        storyPane.setEditable(false);
        storyPane.setFocusable(false);
        storyPane.setOpaque(false);
        storyPane.setForeground(Color.WHITE);
        storyPane.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 22));

        StyledDocument doc = storyPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        GridBagConstraints gbcStory = new GridBagConstraints();
        gbcStory.gridx = 0;
        gbcStory.gridy = 0;
        gbcStory.fill = GridBagConstraints.HORIZONTAL;
        gbcStory.weightx = 1;
        gbcStory.weighty = 1;
        gbcStory.insets = new Insets(20, 20, 20, 20);
        centerPanel.add(storyPane, gbcStory);

        Timer timer = new Timer(7000, e -> {
            if (index < storyLines.length) {
                storyPane.setText(storyLines[index]);
                index++;
            } else {
                ((Timer) e.getSource()).stop();
                storyPane.setText("To be Continued...");
                mainFrame.showPanel("home");
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }
}

