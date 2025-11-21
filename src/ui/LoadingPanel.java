package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class LoadingPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private MainFrame mainFrame;
    private JTextPane storyPane;
    
    private final String[] storyLines = {
        "You wake up from a deep sleep,",
        "You look around your home for equipment..."
    };
    
    private int lineIndex = 0;
    private int charIndex = 0;
    private String selectedClass, playerName;
    public LoadingPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Background image setup
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/images/backgroundpic/loading1.jpg"));
        URL url = getClass().getResource("/images/backgroundpic/loading1.jpg");
        System.out.println("Resource URL: " + url);
        Image bgImage = bgIcon.getImage();

        // Main content panel with background
        JPanel contentPane = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setOpaque(false); // Allow background to show

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        storyPane = new JTextPane();
        storyPane.setEditable(false);
        storyPane.setOpaque(false);
        storyPane.setForeground(Color.WHITE);
        storyPane.setFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD | java.awt.Font.ITALIC, 22));

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

        add(contentPane, BorderLayout.CENTER); 
        
        /*
        
        JButton testBtn = new JButton("Go to Grassy Plains");
        testBtn.addActionListener(e -> mainFrame.showPanel("grassyPlains"));
        add(testBtn, BorderLayout.SOUTH); // Add to the panel
        
        */
    }

    public void startLoading(String playerName, String selectedClass) {
    	this.playerName = playerName;
    	this.selectedClass = selectedClass;
    	
        lineIndex = 0; 
        charIndex = 0;
        storyPane.setText(""); 

        Timer timer = new Timer(50, null); // 50ms per character
        timer.addActionListener(e -> {
            if (lineIndex >= storyLines.length) {
            	
            	System.out.println("Loading complete, switching to grassyPlains");
                ((Timer) e.getSource()).stop();
                
                // Delay before switching panels
                Timer delay = new Timer(500, ev -> {
                    mainFrame.showPanel("grassyPlains");
                    mainFrame.revalidate();
                    mainFrame.repaint();
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
                
                ((Timer) e.getSource()).stop();
                Timer pause = new Timer(500, ev -> timer.start()); 
                pause.setRepeats(false);
                pause.start();
            }
        });
        timer.start();
    }
}
