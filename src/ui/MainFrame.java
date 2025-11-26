package ui;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Panels
    private IntroPanel introPanel;
    private DialoguePanel dialoguePanel;
    private HomePanel homePanel; // Main Menu
    private LoadingPanel loadingPanel;
    private Home inGameHomePanel; // The new Safehouse (Home.java)

    private GrassyPlainsPanel grassyPlainsPanel;
    private DesertWorldPanel desertWorldPanel;
    private SnowyIslandPanel snowyIslandPanel;
    private LavaWorldPanel lavaWorldPanel;
    private FinalWorldPanel finalWorldPanel;

    private Character currentPlayer;

    public MainFrame() {
        setTitle("Blood Of The Rift");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.BLACK);

        // Initialize Panels
        introPanel = new IntroPanel(this);
        dialoguePanel = new DialoguePanel(this);
        homePanel = new HomePanel(this);
        loadingPanel = new LoadingPanel(this);

        // Initialize the new Home Room
        inGameHomePanel = new Home(this);

        grassyPlainsPanel = new GrassyPlainsPanel(this);
        desertWorldPanel = new DesertWorldPanel(this);
        snowyIslandPanel = new SnowyIslandPanel(this);
        lavaWorldPanel = new LavaWorldPanel(this);
        finalWorldPanel = new FinalWorldPanel(this);

        // Add to CardLayout
        mainPanel.add(introPanel, "intro");
        mainPanel.add(dialoguePanel, "dialogue");
        mainPanel.add(homePanel, "home"); // Menu
        mainPanel.add(loadingPanel, "loading");
        mainPanel.add(inGameHomePanel, "homeWorld"); // The Room

        mainPanel.add(grassyPlainsPanel, "grassyPlains");
        mainPanel.add(desertWorldPanel, "desertWorld");
        mainPanel.add(snowyIslandPanel, "snowyIsland");
        mainPanel.add(lavaWorldPanel, "lavaWorld");
        mainPanel.add(finalWorldPanel, "finalWorld");

        setContentPane(mainPanel);
        showPanel("intro");
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void startGame(String playerName, String selectedClass) {
        // Create Player Instance Once
        Random rng = new Random();
        switch (selectedClass) {
            case "Mage": currentPlayer = new Mage(playerName, rng); break;
            case "Paladin": currentPlayer = new Paladin(playerName, rng); break;
            default: currentPlayer = new Warrior(playerName, rng); break;
        }

        // Pass UNARMED player to homeboy
        inGameHomePanel.setPlayer(currentPlayer);

        // Start loading
        showPanel("loading");
        loadingPanel.startLoading(playerName, selectedClass);
    }

    // Called by Home.java when leaving
    public void enterGrassyPlains(Character buffedPlayer) {
        // Pass EQUIPPED player to Grassy Plains
        grassyPlainsPanel.setPlayerObject(buffedPlayer);
        showPanel("grassyPlains");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}