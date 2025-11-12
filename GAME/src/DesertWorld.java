import java.util.Scanner;
import java.util.Random;

public class DesertWorld {
    private Scanner input;
    private Character player;
    private ClearScreen screen;
    private GoToXY go;
    private DrawBox box;
    private Random random = new Random();
    private int mobsDefeated = 0;
    private final int MOBS_UNTIL_BOSS = 4;

    public DesertWorld(Scanner input, Character player, ClearScreen screen, GoToXY go, DrawBox box) {
        this.input = input;
        this.player = player;
        this.screen = screen;
        this.go = go;
        this.box = box;
    }

    public void explore() {
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        go.move(87, 45);
        System.out.println("You enter the scorching Desert World.");
        screen.clear(4);

        boolean inWorld = true;
        while (inWorld) {
            screen.clear(0);
            go.move(0, 37);
            box.draw(209, 18);
            displayPlayerStatus();

            go.move(95, 43);
            System.out.println("Where will you explore?");
            go.move(102, 47);
            System.out.println("[1]-North");
            go.move(111, 49);
            System.out.println("[2]-East");
            go.move(102, 51);
            System.out.println("[3]-South");
            go.move(94, 49);
            System.out.println("[4]-West");
            go.move(106, 49);

            int direction = input.nextInt();

            if (direction >= 1 && direction <= 4) {
                if (random.nextInt(100) < 65) { 
                    triggerEncounter(direction); 
                    if (!player.isAlive()) {
                        inWorld = false; 
                    }
                } else {
                    screen.clear(0);
                    go.move(0, 37);
                    box.draw(209, 18);
                    displayPlayerStatus();

                    String directionString = "";
                    switch (direction) {
                        case 1: directionString = "You walk north."; break;
                        case 2: directionString = "You walk east."; break;
                        case 3: directionString = "You walk south."; break;
                        case 4: directionString = "You walk west."; break;
                    }

                    int centerXDir = 105 - (directionString.length() / 2);
                    go.move(centerXDir, 45);
                    System.out.println(directionString);

                    screen.clear(2);

                    screen.clear(0);
                    go.move(0, 37);
                    box.draw(209, 18);
                    displayPlayerStatus();

                    String wanderString = "You wander the desert but find nothing interest.";
                    int centerXWander = 105 - (wanderString.length() / 2);
                    go.move(centerXWander, 45);
                    System.out.println(wanderString);

                    screen.clear(3);
                }
            } else {
                screen.clear(0);
                go.move(0, 37);
                box.draw(209, 18);
                displayPlayerStatus();

                go.move(84, 45);
                System.out.println("Invalid direction. Please choose 1-4.");    
                screen.clear(3);
            }
        }
    }

    private void triggerEncounter(int direction) {
        World1Mob mob; //tricky asf (exteded classes)

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        String directionString = "";
        switch (direction) {
            case 1: directionString = "You walk north."; break;
            case 2: directionString = "You walk east."; break;
            case 3: directionString = "You walk south."; break;
            case 4: directionString = "You walk west."; break;
        }

        int centerXDir = 105 - (directionString.length() / 2);
        go.move(centerXDir, 45);
        System.out.println(directionString);

        screen.clear(2);
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        if (mobsDefeated >= MOBS_UNTIL_BOSS) {
            mob = new World2Mob.Mummy();
            String msg = "The Desert Boss " + mob.name + " has appeared!";
            int centerXMsg = 105 - (msg.length() / 2);
            go.move(centerXMsg, 45);
            System.out.println(msg);
        } else {
            int mobType = random.nextInt(3);
            if (mobType == 0) {
                mob = new World2Mob.Spider();
            } else if (mobType == 1) {
                mob = new World2Mob.Snake();
            } else {
                mob = new World2Mob.GiantWorm();
            }

            String msg = "A " + mob.name + " has appeared!";
            int centerXMsg = 105 - (msg.length() / 2);
            go.move(centerXMsg, 45);
            System.out.println(msg);
        }

        screen.clear(2);

        Battle battle = new Battle(input, player, mob, screen, go, box);
        boolean playerWon = battle.start();

        if (playerWon) {
            if(mob instanceof World2Mob.Mummy){
                screen.clear(0);
                go.move(0, 37);
                box.draw(209, 18);
                go.move(80, 44);
                System.out.println("You have defeated the Desert Boss Mummy! You conquer the Desert World!");
                go.move(85, 46);
                System.out.println("A portal opens before you, leading to new adventures...");
                screen.clear(5);
                
                //pwede ra diri na dayon e add ang next world - chrisnel
                
                System.out.println("TO BE CONTINUED...");
                System.exit(0);
            } else {
                mobsDefeated++;
                openRewardChest();
            }
        } else {
            screen.clear(0);
            go.move(98, 27);
            System.out.println("You have been defeated...");
            screen.clear(5);    
            System.exit(0);
        }
    }

    private void openRewardChest() {
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        go.move(92, 44);
        System.out.println("You found a reward chest!");
        int rewardType = random.nextInt(5);

        if (rewardType == 0) {
            int hpBoost = 30;
            int healAmount = random.nextInt(16) + 15; //15-30 HP heal

            player.maxHp += hpBoost;
            player.hp += hpBoost;

            if(player.hp > player.maxHp) {
                player.hp = player.maxHp; //capped
            }

            String msg = "Max HP increased by " + hpBoost + "! You also heal for " + healAmount + " HP!";
            int centerX = 105 - (msg.length() / 2);
            go.move(centerX, 46);
            System.out.println(msg);
            
        } else if (rewardType == 1) {
            player.addTemporaryDamage(15);
            go.move(80, 46);
            System.out.println("Your damage increased by 15 for the next battle!");
        } else if (rewardType == 2) {
            int manaBoost = 20;
            int manaHeal = random.nextInt(16) + 15;

            player.maxMana += manaBoost;
            player.mana += manaBoost;

            if (player.mana > player.maxMana){
                 player.mana = player.maxMana;
            }
            String msg = "Max Mana increased by " + manaBoost + "! You also recover " + manaHeal + " MP!";
            int centerX = 105 - (msg.length() / 2);
            go.move(centerX, 46);
            System.out.println(msg);
            
        } else if (rewardType == 3) {
            player.hp = player.maxHp;
            go.move(80, 46);
            System.out.println("You found a healing potion! HP restored to full!");
        } else {
            player.mana = player.maxMana;
            go.move(81, 46);
            System.out.println("You found a mana elixir! Mana restored to full!");
        }
        screen.clear(5);
    }

    private void displayPlayerStatus() {
        final String GREEN = "\u001B[1;92m";
        final String BLUE = "\u001B[36m";
        final String RESET = "\u001B[0m";

        go.move(93, 24);
        System.out.print("HP: ");
        System.out.print(GREEN + player.hp + "/" + player.maxHp + RESET);
        System.out.print("    MP:"); 
        System.out.println(BLUE + player.mana + "/" + player.maxMana + RESET);

        switch (player.className){
            case "Warrior":
                go.move(103, 18);
                System.out.printf("%s", player.name);
                CharacterIcon.Warrior(102, 20);
                break;
            case "Paladin":
                go.move(103, 18);
                System.out.printf("%s", player.name);
                CharacterIcon.Paladin(105, 20);
                break;
            case "Mage":
                go.move(103, 17);
                System.out.printf("%s", player.name);
                CharacterIcon.Mage(104, 19);
                break;
        }
    }
}