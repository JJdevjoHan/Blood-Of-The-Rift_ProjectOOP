package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Intro extends JFrame {

	
	private static final long serialVersionUID = 1L;

	public Intro() {
        setFont(new Font("Tahoma", Font.PLAIN, 12));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500); 
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setUndecorated(true);
        setOpacity(0f);       

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

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
        yesButton.addActionListener(e -> {
            if (yesButton.isSelected()) {
                new HomeFrame().setVisible(true);
                dispose();
            }
        });

        JToggleButton noButton = new JToggleButton("No");
        noButton.addActionListener(e -> {
            if (noButton.isSelected()) {
                new DialogueFrame().setVisible(true);
                dispose();
            }
        });

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridx = 0;
        gbcButtons.gridy = 2;
        gbcButtons.insets = new Insets(20, 10, 20, 10);
        centerPanel.add(buttonPanel, gbcButtons);

        setTitle("Intro Frame");
        fadeIn();
    }

    private void fadeIn() {
        new Thread(() -> {
            try {
                for (float i = 0f; i <= 1f; i += 0.02f) {
                    setOpacity(i);
                    Thread.sleep(30); 
                }
                setOpacity(1f); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Intro().setVisible(true));
    }
}
