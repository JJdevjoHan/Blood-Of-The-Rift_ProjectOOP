import java.util.Random;
import java.util.Scanner;

public class GrassyPlains {
    private Scanner input;
    private Character player;
    private ClearScreen screen;
    private GoToXY go;
    private DrawBox box;
    private Random random = new Random();
    private int mobsDefeated = 0;
    private final int MOBS_UNTIL_BOSS = 3;

    public GrassyPlains(Scanner input, Character player, ClearScreen screen, GoToXY go, DrawBox box) {
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
        System.out.println("You enter the vast, windy Grassy Plains.");
        screen.clear(4);

        boolean inWorld = true;
        while (inWorld) {
            screen.clear(0);
            go.move(0, 37);
            box.draw(209, 18);

            // Display player stats and icon
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
                // Every move has a chance for an encounter
                if (random.nextInt(100) < 65) { // 65% chance of encounter
                    triggerEncounter();
                    if (!player.isAlive()) {
                        inWorld = false; // Player was defeated
                    }
                }

                else {
                    go.move(98, 45);
                    System.out.println("You wander the plains but find nothing of interest.");
                    screen.clear(3);
                }
            }

            else {
                go.move(87, 45);
                System.out.println("Invalid direction! Please choose [1] to [4]");
                screen.clear(3);
            }
        }
    }

    private void triggerEncounter() {
        World1Mob mob;
        // Check if it's time for the boss fight
        if (mobsDefeated >= MOBS_UNTIL_BOSS) {
            mob = new Minotaur();
            System.out.println("A hulking Minotaur blocks your path! It's the guardian of these plains!");
        } else {
            // Spawn a random regular mob
            int mobType = random.nextInt(3);
            if (mobType == 0) {
                mob = new Slime();
            } else if (mobType == 1) {
                mob = new Bull();
            } else {
                mob = new Wolf();
            }
        }

        screen.clear(3);

        Battle battle = new Battle(input, player, mob, screen, go, box);
        boolean playerWon = battle.start();

        if (playerWon) {
            if (mob instanceof Minotaur) {
                // Boss defeated sequence
                screen.clear(0);
                go.move(0, 37);
                box.draw(209, 18);
                go.move(80, 44);
                System.out.println("With the Minotaur defeated, the ground shakes and a portal appears!");
                go.move(85, 46);
                System.out.println("This is your path to the next world...");
                screen.clear(8);
                // Here you would transition to the next world. For now, we'll end the demo.
                System.out.println("TO BE CONTINUED...");
                System.exit(0);
            } else {
                // Regular mob defeated sequence
                mobsDefeated++;
                openRewardChest();
            }
        } else {
            // Player lost
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
        go.move(92, 44);
        System.out.println("You found a reward chest!");

        int rewardType = random.nextInt(3);

        if (rewardType == 0) {
            player.hp = player.maxHp; // Full heal
            go.move(94, 46);
            System.out.println("You found a healing potion! HP restored to full!");
        }
        else if (rewardType == 1) {
            player.mana = player.maxMana; // Full mana
            go.move(94, 46);
            System.out.println("You found a mana elixir! Mana restored to full!");
        }
        else {
            player.addTemporaryDamage(5); // Damage buff
            go.move(88, 46);
            System.out.println("You found a whetstone! Your attacks will deal +5 damage for 3 battles.");
        }
        screen.clear(5);
    }

    private void displayPlayerStatus() {
        final String GREEN = "\u001B[1;92m";
        final String BLUE = "\u001B[36m";
        final String RESET = "\u001B[0m";

//        go.move(103, 18);
//        System.out.printf("%s", player.name);

        go.move(93, 24);
        System.out.print("HP: ");
        System.out.print(GREEN + player.hp + "/" + player.maxHp + RESET);
        System.out.print("   MP: ");
        System.out.println(BLUE + player.mana + "/" + player.maxMana + RESET);

//        switch (player.className) {
//            case "Warrior": CharacterIcon.Warrior(102, 20); break;
//            case "Paladin": CharacterIcon.Paladin(105, 20); break;
//            case "Mage": CharacterIcon.Mage(104, 19); break;
//        }

        switch (player.className) {
            case "Warrior" -> {
                go.move(103, 18);
                System.out.printf("%s", player.name);
                CharacterIcon.Warrior(102, 20);
            }
            case "Paladin" -> {
                go.move(103, 18);
                System.out.printf("%s", player.name);
                CharacterIcon.Paladin(105, 20);
            }
            case "Mage" -> {
                go.move(103, 17);
                System.out.printf("%s", player.name);
                CharacterIcon.Mage(104, 19);
            }
        }


    }
}