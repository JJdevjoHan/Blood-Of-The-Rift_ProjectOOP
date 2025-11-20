package ui;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private IntroPanel introPanel;
    private DialoguePanel dialoguePanel;
    private HomePanel homePanel;
    private LoadingPanel loadingPanel;
    private GrassyPlainsPanel grassyPlainsPanel;
    private DesertWorldPanel desertWorldPanel;
    private SnowyIslandPanel snowyIslandPanel;
    private LavaWorldPanel lavaWorldPanel;
    private FinalWorldPanel finalWorldPanel;

    public MainFrame() {
        setTitle("Blood Of The Rift");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600); // Adjusted for worlds; can be dynamic
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(0, 0, 0));
        mainPanel.setBorder(null); // Remove border to fit content snugly

        // Initialize panels with reference to this frame for navigation
        introPanel = new IntroPanel(this);
        introPanel.setBackground(new Color(0, 0, 0));
        dialoguePanel = new DialoguePanel(this);
        dialoguePanel.setBackground(new Color(0, 0, 0));
        homePanel = new HomePanel(this);
        homePanel.setBackground(new Color(0, 0, 0));
        loadingPanel = new LoadingPanel(this);
        loadingPanel.setBackground(new Color(0, 0, 0));
        grassyPlainsPanel = new GrassyPlainsPanel(this);
        grassyPlainsPanel.setBackground(new Color(0, 0, 0));
        desertWorldPanel = new DesertWorldPanel(this);
        desertWorldPanel.setBackground(new Color(0, 0, 0)); // Fixed: Was incorrectly set on grassyPlainsPanel
        snowyIslandPanel = new SnowyIslandPanel(this);
        snowyIslandPanel.setBackground(new Color(0, 0, 0));
        lavaWorldPanel = new LavaWorldPanel(this);
        lavaWorldPanel.setBackground(new Color(0, 0, 0));
        finalWorldPanel = new FinalWorldPanel(this);
        finalWorldPanel.setBackground(new Color(0, 0, 0));

        // Add panels to CardLayout
        mainPanel.add(introPanel, "intro");
        mainPanel.add(dialoguePanel, "dialogue");
        mainPanel.add(homePanel, "home");
        mainPanel.add(loadingPanel, "loading");
        mainPanel.add(grassyPlainsPanel, "grassyPlains");
        mainPanel.add(desertWorldPanel, "desertWorld");
        mainPanel.add(snowyIslandPanel, "snowyIsland");
        mainPanel.add(lavaWorldPanel, "lavaWorld");
        mainPanel.add(finalWorldPanel, "finalWorld");

        setContentPane(mainPanel);
        showPanel("intro"); // Start with intro
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    // Methods to pass data between panels
    public void startGame(String playerName, String selectedClass) {
        // Initialize worlds with player data
    	showPanel("loading");
        loadingPanel.startLoading(playerName, selectedClass);
        grassyPlainsPanel.setPlayer(playerName, selectedClass);
        desertWorldPanel.setPlayer(playerName, selectedClass);
        snowyIslandPanel.setPlayer(playerName, selectedClass);
        lavaWorldPanel.setPlayer(playerName, selectedClass);
        finalWorldPanel.setPlayer(playerName, selectedClass);
       
    }
   

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
