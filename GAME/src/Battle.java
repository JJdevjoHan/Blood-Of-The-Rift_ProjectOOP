import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Battle {
    private Scanner input;
    private Character player;
    private World1Mob mob;
    private ClearScreen screen;
    private GoToXY go;
    private DrawBox box;

    public Battle(Scanner input, Character player, World1Mob mob, ClearScreen screen, GoToXY go, DrawBox box) {
        this.input = input;
        this.player = player;
        this.mob = mob;
        this.screen = screen;
        this.go = go;
        this.box = box;
    }

    // centerlogic -chrisnel
    private int centerTextX(String text, int boxWidth, int boxStartX) {
        return boxStartX + (boxWidth - text.length()) / 2;
    }

    public boolean start() {
        while (player.isAlive() && mob.isAlive()) {
            playerTurn();
            if (!mob.isAlive()) break;
            mobTurn();
        }
        //furnished -chrisnel
        displayBattleStatus();
        go.move(92, 45);
        screen.clearLine(go, 92, 45, 80);
        if (player.isAlive()) {
            System.out.println("You have defeated the " + mob.name + "!");
            player.decrementDamageBuffDuration();
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) {}
            return true;
        } else {
            System.out.println("You have been defeated by the " + mob.name + "!");
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) {}
            return false;
        }
    }

    private void playerTurn() {
        displayBattleStatus(); 
        
        // Clear previous action messages before new turn promptt
        screen.clearLine(go, 80, 45, 100); 
        screen.clearLine(go, 80, 46, 100);
        screen.clearLine(go, 80, 47, 100);

        String turnText = "Your turn. Choose your skill:";
        int centerX = centerTextX(turnText, 209, 0);
        go.move(centerX, 41);
        System.out.println(turnText);
        
        // Show centered skill list under it
        player.displaySkills(go, 0, 209, 43); 
        
        go.move(106, 48); 
        int choice;
        try {
            choice = Integer.parseInt(input.next());
        } catch (Exception e) {
            choice = -1; 
        }
        
        screen.clearLine(go, 90, 41, 40); 
        screen.clearLine(go, 75, 43, 80); 
        screen.clearLine(go, 75, 44, 80); 
        screen.clearLine(go, 75, 45, 80); 

        String actionMessage = player.useSkill(choice, mob);
        int actionCenterX = centerTextX(actionMessage, 209, 0);
        go.move(actionCenterX, 45); 
        System.out.print(actionMessage); 
   
        updateHpDisplay();
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) {}
    }

    private void mobTurn() {
        displayBattleStatus(); 

        // Clear previous action messages
        screen.clearLine(go, 80, 45, 100);
        screen.clearLine(go, 80, 46, 100);
        screen.clearLine(go, 80, 47, 100);

        String turnText = mob.name + "'s turn.";
        int turnCenterX = centerTextX(turnText, 209, 0);
        go.move(turnCenterX, 43); // Centered the mob turn text
        System.out.println(turnText);
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) {}

        String actionMessage;
        if (Math.random() < 0.3) {
            actionMessage = mob.specialSkill(player);
        } else {
            actionMessage = mob.attack(player);
        }

        screen.clearLine(go, turnCenterX, 43, turnText.length());

        int actionCenterX = centerTextX(actionMessage, 209, 0);
        go.move(actionCenterX, 45); 
        System.out.print(actionMessage); 
        
        updateHpDisplay();
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) {}
    }

    private void displayBattleStatus() {
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);

        // --- Player Side ---
        go.move(46, 18);
        System.out.printf("%s", player.name);
        switch (player.className) {
            case "Warrior":
                CharacterIcon.Warrior(45, 20);
                break;
            case "Paladin":
                CharacterIcon.Paladin(48, 20);
                break;
            case "Mage":
                CharacterIcon.Mage(47, 19);
                break;
        }

        go.move(36, 24);
        System.out.printf("HP: \u001B[1;92m%d/%d\u001B[0m   MP: \u001B[36m%d/%d\u001B[0m",
                player.hp, player.maxHp, player.mana, player.maxMana);

        // --- Mob Side ---
        go.move(160, 18);
        System.out.printf("%s", mob.name);
        switch (mob.name) {
            case "Slime": CharacterIcon.Slime(162, 20); break;
            case "Wild Bull": CharacterIcon.WildBull(162, 20); break;
            case "Dire Wolf": CharacterIcon.Wolf(162, 20); break;
            case "Minotaur": CharacterIcon.Minotaur(162, 20); break;
        }

        go.move(157, 24);
        System.out.printf("HP: \u001B[1;91m%d/%d\u001B[0m", mob.hp, mob.maxHp);
    }

    private void updateHpDisplay() {
        go.move(36, 24);
        System.out.printf("HP: \u001B[1;92m%d/%d\u001B[0m   MP: \u001B[36m%d/%d\u001B[0m",
                player.hp, player.maxHp, player.mana, player.maxMana);
        go.move(157, 24);
        System.out.printf("HP: \u001B[1;91m%d/%d\u001B[0m", mob.hp, mob.maxHp);
    }
}
