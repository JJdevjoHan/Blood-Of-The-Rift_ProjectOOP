import java.util.Scanner;
import java.util.Random;

public class LavaWorld {
    private Scanner input;
    private Character player;
    private ClearScreen screen;
    private GoToXY go;
    private DrawBox box;
    private Random random = new Random();
    private int mobsDefeated = 0;
    private final int MOBS_UNTIL_BOSS = 3;
   
    private FinalWorld finalWorld;

    public LavaWorld(Scanner input, Character player, ClearScreen screen, GoToXY go, DrawBox box, FinalWorld finalWorld) {
        this.input = input;
        this.player = player;
        this.screen = screen;
        this.go = go;
        this.box = box;
        this.finalWorld = finalWorld; 
    }

    public void explore(){
        
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
        
        String msg1 = "You step from the freezing cold directly into a furnace.";
        go.move(105 - (msg1.length() / 2), 44);
        System.out.println(msg1);
        screen.clear(1);//3

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
        
        String msg2 = "This is the heart of the rift's corruption, a land of pure fire and rage.";
        String msg3 = "The Betrayed Champion's hatred has twisted this place into a monument to his pain.";
        go.move(105 - (msg2.length() / 2), 44);
        System.out.println(msg2);
        go.move(105 - (msg3.length() / 2), 46);
        System.out.println(msg3);
        screen.clear(1);//5

        boolean inWorld = true;
        while(inWorld){
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
                        case 1: directionString = "You walk north. The heat from the volcano rim is unbearable."; break;
                        case 2: directionString = "You walk east. A river of molten rock flows nearby."; break;
                        case 3: directionString = "You walk south, kicking up clouds of hot ash."; break;
                        case 4: directionString = "You walk west through a forest of burnt, skeletal trees."; break;
                    }

                    int centerXDir = 105 - (directionString.length() / 2);
                    go.move(centerXDir, 45);
                    System.out.println(directionString);
                    screen.clear(1); // 2

                    screen.clear(0);
                    go.move(0, 37);
                    box.draw(209, 18);
                    displayPlayerStatus();

                    String wanderString = "The air shimmers. You find nothing but rock and fire.";
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
                go.move(84, 45);
                System.out.println("Invalid direction. Please choose 1-4.");    
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
            case 1: directionString = "You climb toward the volcano's rim..."; break;
            case 2: directionString = "You follow the magma river east..."; break;
            case 3: directionString = "You head south into the ash fields..."; break;
            case 4: directionString = "You step into the charred forest..."; break;
        }   
        
        int centerXDir = 105 - (directionString.length() / 2);  
        go.move(centerXDir, 45);    
        System.out.println(directionString); 
        screen.clear(1); // 2

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        if(mobsDefeated >= MOBS_UNTIL_BOSS){
            mob = new World4Mob.LavaImp();
            String msg = "The Lava Boss " + mob.name + " rises from a pool of magma!";
            int centerXMsgg = 105 - (msg.length() / 2);
            go.move(centerXMsgg, 45);
            System.out.println(msg); 
        }else{
            int mobType = random.nextInt(3);
            if(mobType == 0){
                mob = new World4Mob.MagmaBeast();    
            }else if(mobType == 1){
                mob = new World4Mob.SkeletonHead();
            }else{
                mob = new World4Mob.Golem();
            }

            String msg = "A " + mob.name + " erupts from the ground!"; 
            int centerXMsg = 105 - (msg.length() / 2);
            go.move(centerXMsg, 45);    
            System.out.println(msg);
        }

        screen.clear(1);    // 2    

        Battle battle = new Battle(input, player, mob, screen, go, box);
        boolean playerWon = battle.start();

        if(playerWon){
            if(mob instanceof World4Mob.LavaImp){
                playWorld4Outro(); 
                finalWorld.explore(); 
            }else{
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
        
        String title = "You defeated the enemy! Choose your reward:";
        go.move(105 - (title.length() / 2), 43); // Center title
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
        screen.clear(1); //4
    }

    private void displayPlayerStatus(){
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
    
    private void playWorld4Outro() {
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        String msg1 = "As the Lava Imp dissolves, its fire extinguishes into cold ash.";
        String msg2 = "The oppressive heat of the volcano suddenly vanishes...";
        go.move(105 - (msg1.length() / 2), 44);
        System.out.println(msg1);
        go.move(105 - (msg2.length() / 2), 46);
        System.out.println(msg2);
        
        screen.clear(1); //5

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        String msg3 = "A deep, groaning sound echoes from beneath your feet.";
        String msg4 = "RRRUUUMMMBBLLLE..."; // Sound effect unta mahimoan plspls
        
        go.move(105 - (msg3.length() / 2), 44);
        System.out.println(msg3);
        go.move(105 - (msg4.length() / 2), 46);
        System.out.println(msg4);

        screen.clear(1); //4

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        String msg5 = "The ground splits open, but not with fire.";
        String msg6 = "Instead, a portal of pure, terrifying shadow tears a hole in reality.";
        
        go.move(105 - (msg5.length() / 2), 44);
        System.out.println(msg5);
        go.move(105 - (msg6.length() / 2), 46);
        System.out.println(msg6);

        screen.clear(1); //6

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        String msg7 = "You feel the echoes of an 800-year-old hatred calling to you from within.";
        String msg8 = "This is it. The path to the Betrayed Champion.";

        go.move(105 - (msg7.length() / 2), 44);
        System.out.println(msg7);
        go.move(105 - (msg8.length() / 2), 46);
        System.out.println(msg8);
        
        screen.clear(1);//7
    }
}