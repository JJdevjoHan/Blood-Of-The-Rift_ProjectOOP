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
            "In sooth, a thousand years ago....",
            "A mighty hero was chosen to save the Kingdom...",
            "New heroes rise from the divided worlds..."
    };

    public DialoguePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.BLACK);

        URL imgUrl = getClass().getResource("/images/backgroundpic/intro.png");
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
        add(contentPane, BorderLayout.CENTER);

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