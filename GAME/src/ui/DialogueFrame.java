package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;

public class DialogueFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextPane storyPane;
    private int index = 0;

    private String[] storyLines = {
        "A thousand years ago....",
        "A Mighty Hero was chosen to save the Kingdom of Senthra from the wrath of the gods.",
        "Betrayed by his own people, he was used as a scapegoat and sacrificed to seal the calamities.",
        "But the world still fell apart, a great rift tore reality into multiple realms.",
        "The champion did not die; instead, he was cursed, left less than human, and forgotten in the shadows of the rift.",
        "Eight hundred years later....",
        "New heroes rise from the divided worlds.",
        "Believing the fallen Hero to be the cause of the chaos, they set out to defeat him.",
        "But as they uncover the truth, they learn he was never the villain, only the first victim.",
        "In the final battle, the champion accepts his fate.",
        "With the help of the heroes, he offers himself as the true sacrifice, closing the rift and restoring the world at last."
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DialogueFrame().setVisible(true));
    }

    public DialogueFrame() {
        setTitle("Dialogue Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);

        // Main container
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(40, 40, 40, 40));
        setContentPane(contentPane);

        // Dynamic center container
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        // STORY AREA (NO SCROLL)
        storyPane = new JTextPane();
        storyPane.setEditable(false);
        storyPane.setFocusable(false);
        storyPane.setOpaque(false);  // Transparent background
        storyPane.setForeground(Color.WHITE);
        storyPane.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 22));

        //center allign
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

        // Timer updates
        Timer timer = new Timer(7000, e -> {
            if (index < storyLines.length) {
                storyPane.setText(storyLines[index]);
                index++;
            } else {
                ((Timer) e.getSource()).stop();
                storyPane.setText("To be Continued...");
                new HomeFrame().setVisible(true);
                dispose();
            }
        });

        timer.setInitialDelay(0);
        timer.start();
    }
}
