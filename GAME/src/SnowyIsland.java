import java.util.Scanner;
import java.util.Random;


public class SnowyIsland {
    private Scanner input;
    private Character player;
    private ClearScreen screen;
    private GoToXY go;
    private DrawBox box;
    private Random random = new Random();
    private int mobsDefeated = 0;
    private final int MOBS_UNTIL_BOSS = 3;
    private LavaWorld lavaWorld;    

    public SnowyIsland(Scanner input, Character player, ClearScreen screen, GoToXY go, DrawBox box, LavaWorld lavaWorld) {
        this.input = input;
        this.player = player;
        this.screen = screen;
        this.go = go;
        this.box = box;
        this.lavaWorld = lavaWorld;
    }

    public void explore() {
    
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
        
        String msg1 = "You step from the searing desert heat into a blinding, freezing blizzard.";
        go.move(105 - (msg1.length() / 2), 44);
        System.out.println(msg1);
        screen.clear(3);

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
        
        String msg2 = "The air is so cold it burns. A voice on the wind whispers: Silence... Stillness... Order.";
        String msg3 = "This is the domain of Kyros's general. You must find the next portal.";
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
                if (random.nextInt(100) < 80) { 
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
                        case 1: directionString = "You climb the Frozen Ridge. The wind nearly throws you off."; break;
                        case 2: directionString = "You enter the Ice Caves. The walls glitter with trapped light."; break;
                        case 3: directionString = "You move south, staring into the bottomless crevasse."; break;
                        case 4: directionString = "You scale the peak. The view is endless, frozen desolation."; break;
                    }

                    int centerXDir = 105 - (directionString.length() / 2);
                    go.move(centerXDir, 45);
                    System.out.println(directionString);
                    screen.clear(1); // 2

                    screen.clear(0);
                    go.move(0, 37);
                    box.draw(209, 18);
                    displayPlayerStatus();

                    String wanderString = "You find nothing but ice and biting wind.";
                    int centerXWander = 105 - (wanderString.length() / 2);
                    go.move(centerXWander, 45); 
                    System.out.println(wanderString);   

                    screen.clear(1); // 3
                }
            } else {
                screen.clear(0);
                go.move(0, 37);
                box.draw(209, 18);
                displayPlayerStatus();

                go.move(84,45);
                System.out.println("Invalid direction! Please try again.");
                screen.clear(3);
            }
        }   
    }

    private void triggerEncounter(int direction){
        World1Mob mob;

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        String directionString = "";
        
        switch (direction) {
            case 1: directionString = "You climb the Frozen Ridge..."; break;
            case 2: directionString = "You enter the Ice Caves..."; break;
            case 3: directionString = "You move south, towards the crevasse..."; break;
            case 4: directionString = "You scale the peak..."; break;
        }

        int centerXDir = 105 - (directionString.length() / 2);
        go.move(centerXDir, 45);
        System.out.println(directionString);
        screen.clear(1); // 2

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
        
        if(mobsDefeated >= MOBS_UNTIL_BOSS) { 
            mob = new World3Mob.GiantFrostWolves();
            String msg = "The guardians of the peak, the " + mob.name + ", have appeared!";
            int centerXMsg = 105 - (msg.length() / 2);
            go.move(centerXMsg, 45);
            System.out.println(msg);
        } else { 
            int mobType = random.nextInt(3);
            if(mobType == 0) {
                mob = new World3Mob.SnowGolem();
            } else if (mobType == 1) {
                mob = new World3Mob.Yeti();
            } else {
                mob = new World3Mob.WitchGnome();
            }

            String msg = "A " + mob.name + " ambushes you from the snow!";
            int centerXMsg = 105 - (msg.length() / 2);
            go.move(centerXMsg, 45);
            System.out.println(msg);
        }

        screen.clear(1); // 2

        Battle battle = new Battle(input, player, mob, screen, go, box);
        boolean playerWon = battle.start();

        if(playerWon) {
            if(mob instanceof World3Mob.GiantFrostWolves){
                
                playerWorld3Outro(); 

                screen.clear(0);
                go.move(0, 37);
                box.draw(209, 18);
                displayPlayerStatus();

                String msg1 = "The portal to the Lava World is open.";
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

                if(choice == 1){
                    lavaWorld.explore();
                } else {
                   boolean isResting = true;
                   while(isResting){
                        
                        screen.clear(0);
                        go.move(0, 37);
                        box.draw(209, 18);
                        displayPlayerStatus();

                        String restMsg1 = "You are resting at home.";
                        String restMsg2 = "Are you ready for your next adventure?";
                        String restOpt = "[1] Yes, proceed to the Lava World";

                        go.move(105 - (restMsg1.length() / 2), 44);
                        System.out.println(restMsg1);
                        go.move(105 - (restMsg2.length() / 2), 46);
                        System.out.println(restMsg2);
                        go.move(105 - (restOpt.length() / 2), 49);
                        System.out.println(restOpt);

                        go.move(106, 51); 
                        int restChoice = input.nextInt();

                        if(restChoice == 1){
                            isResting = false;
                            lavaWorld.explore();
                        }else{
                            screen.clear(0);
                            go.move(0, 37);
                            box.draw(209, 18);
                            displayPlayerStatus();
                            String waitMsg = "You decide to stay longer at home.";  
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
        }else{
            screen.clear(0);
            go.move(98, 27);
            System.out.println("You have been defeated...");
            screen.clear(5);    
            System.exit(0);
        }
    }

    private void openRewardChest(){
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        String title = "You defeated the enemy! Choose your reward:";
        go.move(105 - (title.length() / 2), 43); 
        System.out.println(title);

        String opt1 = "[1] Healing Potion (Restore all HP)";
        String opt2 = "[2] Mana Elixir (Restore all Mana)";
        String opt3 = "[3] Whetstone (Gain +15 Temp. Damage for 3 battles)";

        go.move(105 - (opt1.length() / 2), 46);
        System.out.println(opt1);
        go.move(105 - (opt2.length() / 2), 47);
        System.out.println(opt2);
        go.move(105 - (opt3.length() / 2), 48);
        System.out.println(opt3);

        go.move(106, 50); 
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
        screen.clear(1); // Pause for 4 seconds
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

    private void playerWorld3Outro() {
        // --- Scene 1: The Thaw ---
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
    
        String msg1 = "As the Giant Frost Wolves fade, the unnatural cold begins to recede.";
        String msg2 = "Kyros's oppressive voice fades from your mind, replaced by a new sound...";
        go.move(105 - (msg1.length() / 2), 44);
        System.out.println(msg1);
        go.move(105 - (msg2.length() / 2), 46);
        System.out.println(msg2);
        screen.clear(1); //5

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
        
        String msg3 = "A deep, volcanic rumble. The ice in front of you doesn't just melt, it *boils*.";
        String msg4 = "A portal of fire and brimstone tears open, radiating an intense, dry heat.";
        
        go.move(105 - (msg3.length() / 2), 44);
        System.out.println(msg3);
        go.move(105 - (msg4.length() / 2), 46);
        System.out.println(msg4);
        
        screen.clear(1); //6
    }   
}