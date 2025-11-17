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
        "In sooth, a thousand years ago....",
        "A mighty hero was chosen to save the Kingdom of Senthra from the ire of the gods.",
        "Betrayed by his own kin, he was made a scapegoat and sacrificed unto the gods to seal the calamities.",
        "But...",
        "The world didst still fall asunder, and a great rift tore reality into multiple realms.",
        "Thee once mighty hero did not perish; nay, he was cursed, left less than a mortal, and forgotten in the rift's shadows.",
        "Eight hundred winters anon....",
        "New heroes arose from the sundered worlds.",
        "And believing the fallen hero to be the cause of the chaos, they set forth to vanquish him.",
        "But as they uncover the truth, they learned that he was never the villain, yet only the first victim.",
        "In the final battle, the fallen hero accepts his fate...",
        "And with the help of the heroes, he didst offer himself as thee true sacrifice, closing the rift and restoring the world at last."
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
