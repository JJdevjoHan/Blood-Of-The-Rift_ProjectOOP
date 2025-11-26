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
    private Home inGameHomePanel; // The Safehouse

    // World Panels
    private GrassyPlainsPanel grassyPlainsPanel;
    private DesertWorldPanel desertWorldPanel;
    private SnowyIslandPanel snowyIslandPanel;
    private LavaWorldPanel lavaWorldPanel; // Now Active
    private FinalWorldPanel finalWorldPanel; // Placeholder for next update

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

        // Initialize UI Panels
        introPanel = new IntroPanel(this);
        dialoguePanel = new DialoguePanel(this);
        homePanel = new HomePanel(this);
        loadingPanel = new LoadingPanel(this);
        inGameHomePanel = new Home(this);

        // Initialize World Panels
        grassyPlainsPanel = new GrassyPlainsPanel(this);
        desertWorldPanel = new DesertWorldPanel(this);
        snowyIslandPanel = new SnowyIslandPanel(this);
        lavaWorldPanel = new LavaWorldPanel(this);
        finalWorldPanel = new FinalWorldPanel(this);

        // Add to CardLayout
        mainPanel.add(introPanel, "intro");
        mainPanel.add(dialoguePanel, "dialogue");
        mainPanel.add(homePanel, "home");
        mainPanel.add(loadingPanel, "loading");
        mainPanel.add(inGameHomePanel, "homeWorld");

        mainPanel.add(grassyPlainsPanel, "grassyPlains");
        mainPanel.add(desertWorldPanel, "desertWorld");
        mainPanel.add(snowyIslandPanel, "snowyIsland");
        mainPanel.add(lavaWorldPanel, "lavaWorld"); // Add to stack
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

        // Pass UNARMED player to the Safehouse
        inGameHomePanel.setPlayer(currentPlayer);

        // Start loading
        showPanel("loading");
        loadingPanel.startLoading(playerName, selectedClass);
    }

    //WORLD TRANSITIONS
    public void enterGrassyPlains(Character buffedPlayer) {
        this.currentPlayer = buffedPlayer;
        grassyPlainsPanel.setPlayerObject(buffedPlayer);
        showPanel("grassyPlains");
    }

    public void enterDesertWorld(Character buffedPlayer) {
        this.currentPlayer = buffedPlayer;
        desertWorldPanel.setPlayerObject(buffedPlayer);
        showPanel("desertWorld");
    }

    public void enterSnowyIsland(Character buffedPlayer) {
        this.currentPlayer = buffedPlayer;
        snowyIslandPanel.setPlayerObject(buffedPlayer);
        showPanel("snowyIsland");
    }

    public void enterLavaWorld(Character buffedPlayer) {
        this.currentPlayer = buffedPlayer;
        lavaWorldPanel.setPlayerObject(buffedPlayer);
        showPanel("lavaWorld");
    }

    public void enterFinalWorld(Character buffedPlayer) {
        this.currentPlayer = buffedPlayer;
        finalWorldPanel.setPlayerObject(buffedPlayer);
        showPanel("finalWorld");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}