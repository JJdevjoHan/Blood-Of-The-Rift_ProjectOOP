package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.net.URL;
import javax.swing.ImageIcon;

public class Loading extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextPane storyPane;

    private final String[] storyLines = {
        "You wake up from a deep sleep,",
        "You look around your home for equipment..."
    };

    private int lineIndex = 0;
    private int charIndex = 0;

    private final String playerName;
    private final String selectedClass;

    public Loading(String playerName, String selectedClass) {
        this.playerName = playerName;
        this.selectedClass = selectedClass;

        setTitle("Loading...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500); 
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/images/backgroundpic/loading1.jpg"));
        URL url = getClass().getResource("/images/backgroundpic/loading1.jpg");
        System.out.println("Resource URL: " + url);
        Image bgImage = bgIcon.getImage();
        
        //sa container
        JPanel contentPane = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

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
        gbcStory.gridx = 0;
        gbcStory.gridy = 0;
        gbcStory.fill = GridBagConstraints.HORIZONTAL;
        gbcStory.weightx = 1;
        gbcStory.weighty = 1;
        gbcStory.insets = new Insets(20, 20, 20, 20);
        centerPanel.add(storyPane, gbcStory);

        startTyping();
    }

    private void startTyping() {
        Timer timer = new Timer(50, null); // 50ms per character
        timer.addActionListener(e -> {
            if (lineIndex >= storyLines.length) {
                ((Timer) e.getSource()).stop();
                
                Timer delay = new Timer(500, ev -> {
                    new GrassyPlains(playerName, selectedClass).setVisible(true);
                    dispose();
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
                storyPane.setText(storyPane.getText() + " ");
                // Pause before next line
                ((Timer)e.getSource()).stop();
                Timer pause = new Timer(500, ev -> startTyping());
                pause.setRepeats(false);
                pause.start();
            }
        });
        timer.start();
    }
    
 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Loading("Hero", "Mage").setVisible(true));
    }
}
