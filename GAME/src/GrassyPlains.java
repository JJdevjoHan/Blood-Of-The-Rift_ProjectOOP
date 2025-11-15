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

    private DesertWorld desertWorld; 

    public GrassyPlains(Scanner input, Character player, ClearScreen screen, GoToXY go, DrawBox box, DesertWorld desertWorld) {
        this.input = input;
        this.player = player;
        this.screen = screen;
        this.go = go;
        this.box = box;
        this.desertWorld = desertWorld; 
    }

    public void explore() {
       
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
        //added intro 
        String msg1 = "You step out of your home. The world you once knew is gone.";
        go.move(105 - (msg1.length() / 2), 44);
        System.out.println(msg1);
        screen.clear(3);

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
        
        String msg2 = "The Grassy Plains, once a vibrant field of green, are now a dying yellow.";
        String msg3 = "A dark energy radiates from a hulking shape in the distance...";
        go.move(105 - (msg2.length() / 2), 44);
        System.out.println(msg2);
        go.move(105 - (msg3.length() / 2), 46);
        System.out.println(msg3);
        screen.clear(5);
        

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
                if (random.nextInt(100) < 80) { // 80% chance of encounter
                    triggerEncounter(direction); 
                    if (!player.isAlive()) {
                        inWorld = false; 
                    }
                } else {
                    // No encounter logic
                    screen.clear(0);
                    go.move(0, 37);
                    box.draw(209, 18);
                    displayPlayerStatus();
                    
                    String directionString = "";
                    switch (direction) {//added twist
                        case 1: directionString = "You head north. The trees are twisted and leafless."; break;
                        case 2: directionString = "You walk east. You hear strange echoes from the cliffs."; break;
                        case 3: directionString = "You push south, the tall grass rustling around you."; break;
                        case 4: directionString = "You follow the dried riverbed, but it's just cracked earth."; break;
                    }
                    
                    int centerXDir = 105 - (directionString.length() / 2);
                    go.move(centerXDir, 45); 
                    System.out.println(directionString);
                    screen.clear(1); // 2

                    screen.clear(0);
                    go.move(0, 37);
                    box.draw(209, 18);
                    displayPlayerStatus();
                    
                    String wanderString = "The air is still... you find nothing of interest.";
                    int centerXWander = 105 - (wanderString.length() / 2);
                    go.move(centerXWander, 45); 
                    System.out.println(wanderString);

                    screen.clear(1); // 2
                }
            } else { 
                screen.clear(0);
                go.move(0, 37);
                box.draw(209, 18);
                displayPlayerStatus();

                go.move(84, 45);
                System.out.println("Invalid direction! Please choose [1] to [4]");
                screen.clear(3); // 3
            }
        }
    }

    private void triggerEncounter(int direction) { 
        World1Mob mob;

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        String directionString = "";
        switch (direction) {
            case 1: directionString = "You head north into the dying woods..."; break;
            case 2: directionString = "You walk east towards the cliffs..."; break;
            case 3: directionString = "You push south through the tall grass..."; break;
            case 4: directionString = "You follow the dried riverbed west..."; break;
        }
        
        int centerXDir = 105 - (directionString.length() / 2);
        go.move(centerXDir, 45); 
        System.out.println(directionString);
        
        screen.clear(1); // 2

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        if (mobsDefeated >= MOBS_UNTIL_BOSS) {
            mob = new Minotaur();
            String msg = "A hulking Minotaur blocks your path! It roars, its eyes glowing with the rift's energy.";
            int centerXMsg = 105 - (msg.length() / 2);
            go.move(centerXMsg, 45);
            System.out.println(msg);
        } else {
            int mobType = random.nextInt(3);
            if (mobType == 0) {
                mob = new Slime();
            } else if (mobType == 1) {
                mob = new Bull();
            } else {
                mob = new Wolf();
            }
            
            String msg = "A " + mob.name + " leaps from the shadows!";
            int centerXMsg = 105 - (msg.length() / 2);
            go.move(centerXMsg, 45);
            System.out.println(msg);
        }
        
        screen.clear(1); // 3

        Battle battle = new Battle(input, player, mob, screen, go, box);
        boolean playerWon = battle.start();

        if (playerWon) {
            if (mob instanceof Minotaur) {
                
                playWorld1Outro(); 

                screen.clear(0);
                go.move(0, 37);
                box.draw(209, 18);
                displayPlayerStatus();

                String msg1 = "The portal to the Desert World is open.";
                String msg2 = "What will you do?";
                String opt1 = "[1] Enter the portal";
                String opt2 = "[2] Return home to rest";

                go.move(105 - (msg1.length() / 2), 43);
                System.out.println(msg1);
                go.move(105 - (msg2.length() / 2), 45);
                System.out.println(msg2);

                go.move(105 - (opt1.length() / 2), 48);
                System.out.println(opt1);
                go.move(105 - (opt2.length() / 2), 49);
                System.out.println(opt2);
                
                go.move(106, 51); 

                int choice = input.nextInt();

                if (choice == 1) {
                    desertWorld.explore(); 
                } else {
                    boolean isResting = true;
                    while (isResting) {
                        
                        screen.clear(0);
                        go.move(0, 37);
                        box.draw(209, 18);
                        displayPlayerStatus(); 

                        String restMsg1 = "You are resting at home.";
                        String restMsg2 = "Are you ready for your next adventure?";
                        String restOpt = "[1] Yes, proceed to the Desert World";

                        go.move(105 - (restMsg1.length() / 2), 44);
                        System.out.println(restMsg1);
                        go.move(105 - (restMsg2.length() / 2), 46);
                        System.out.println(restMsg2);
                        go.move(105 - (restOpt.length() / 2), 49);
                        System.out.println(restOpt);
                        
                        go.move(106, 51); 
                        int restChoice = input.nextInt();
                        
                        if (restChoice == 1) {
                            isResting = false; 
                            desertWorld.explore(); 
                        } else {
                            screen.clear(0);
                            go.move(0, 37);
                            box.draw(209, 18);
                            displayPlayerStatus();
                            String waitMsg = "You take a little more time to rest...";
                            go.move(105 - (waitMsg.length() / 2), 45);
                            System.out.println(waitMsg);
                            screen.clear(1); // 3
                        }
                    }
                }
            } else {
                mobsDefeated++;
                openRewardChest();
            }
        } else {
            screen.clear(0);
            go.move(98, 27);
            System.out.println("You have been defeated...");
            screen.clear(1); // 5
            System.exit(0);
        }
    }

    private void openRewardChest() {
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus(); 
        
        
        String title = "You defeated the enemy! Choose your reward:";
        go.move(105 - (title.length() / 2), 43); 
        System.out.println(title);

        //instead of random reward, let player choose(if mas nice ang random ingna lang ko)- chrisnel
        String opt1 = "[1] Healing Potion (Restore all HP)";
        String opt2 = "[2] Mana Elixir (Restore all Mana)";
        String opt3 = "[3] Whetstone (Gain +15 Temp. Damage for 3 battles)";

        go.move(105 - (opt1.length() / 2), 46);
        System.out.println(opt1);
        go.move(105 - (opt2.length() / 2), 47);
        System.out.println(opt2);
        go.move(105 - (opt3.length() / 2), 48);
        System.out.println(opt3);

        go.move(106, 50); // Cursor position
        int choice = input.nextInt();

        screen.clearLine(go, 70, 43, 80); // Clear title
        screen.clearLine(go, 70, 46, 80); // Clear opt 1
        screen.clearLine(go, 70, 47, 80); // Clear opt 2
        screen.clearLine(go, 70, 48, 80); // Clear opt 3

        String msg = "";
        switch (choice) {
            case 1:
                player.hp = player.maxHp;
                msg = "You feel invigorated! HP restored to full!";
                break;
            case 2:
                player.mana = player.maxMana;
                msg = "Your mind clears! Mana restored to full!";
                break;
            case 3:
                player.addTemporaryDamage(15);
                msg = "Your weapon glows! Damage increased for the next 3 battles!";
                break;
            default:
                player.hp = player.maxHp;
                msg = "You fumbled but found a Healing Potion! HP restored to full!";
                break;
        }

        go.move(105 - (msg.length() / 2), 45);
        System.out.println(msg);
        screen.clear(1); //4
    }

    private void displayPlayerStatus() {
        final String GREEN = "\u001B[1;92m";
        final String BLUE = "\u001B[36m";
        final String RESET = "\u001B[0m";

        go.move(93, 24);
        System.out.print("HP: ");
        System.out.print(GREEN + player.hp + "/" + player.maxHp + RESET);
        System.out.print("   MP: ");
        System.out.println(BLUE + player.mana + "/" + player.maxMana + RESET);

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

    private void playWorld1Outro() {
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
    
        String msg1 = "With the Minotaur defeated, its dark energy evaporates.";
        String msg2 = "A faint, healthy green returns to the grass around you.";
        go.move(105 - (msg1.length() / 2), 44);
        System.out.println(msg1);
        go.move(105 - (msg2.length() / 2), 46);
        System.out.println(msg2);
        screen.clear(5); 

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
        
        String msg3 = "The ground shakes violently as the beast's power is drawn away...";
        String msg4 = "...pulled towards a new focal point: a swirling portal of sand and heat.";
        
        go.move(105 - (msg3.length() / 2), 44);
        System.out.println(msg3);
        go.move(105 - (msg4.length() / 2), 46);
        System.out.println(msg4);
        
        screen.clear(1);//6
    }
}