import java.util.Scanner;
import java.util.Random;

public class FinalWorld {
    private Scanner input;
    private Character player;
    private ClearScreen screen;
    private GoToXY go;
    private DrawBox box;
    public int storyStep = 0; // Tracks story progress
    public Random random = new Random();
    
    public FinalWorld(Scanner input, Character player, ClearScreen screen, GoToXY go, DrawBox box) {
        this.input = input;
        this.player = player;
        this.screen = screen;
        this.go = go;
        this.box = box;
    }   

    public void explore(){
        playStoryScene(0); 

        // Story Beats 1-4 (The Backstory)
        for (int i = 1; i <= 4; i++) {
            showNavigation(i);
            playStoryScene(i);
        }

        // Mini-Boss: Kyros
        showNavigation(1);//5
        boolean kyrosDefeated = triggerEncounter(false); // false = not final boss
        
        if (!kyrosDefeated) { 
            screen.clear(0);
            go.move(98, 27);
            System.out.println("Your journey ends in the darkness of the rift...");
            screen.clear(1);    //5
            System.exit(0);
        }

        openRewardChest();
        storyStep++; // This moves storyStep to 6
        
        playStoryScene(6); 

        boolean demonLordDefeated = triggerEncounter(true); 
        
        if (!demonLordDefeated) { 
            screen.clear(0);
            go.move(98, 27);
            System.out.println("Your quest fails at the final step...");
            screen.clear(1);    //5
            System.exit(0);
        }
        
        playStoryScene(7); 
    }

    private void showNavigation(int storyStep) {
        screen.clear(0);
        go.move(0, 37); 
        box.draw(209, 18);   
        displayPlayerStatus();

        String pathText = "";
        switch(storyStep) {
            case 1: pathText = "You follow the narrow path into the oppressive darkness."; break;
            case 2: pathText = "A ghostly whisper brushes past your ear. You press on."; break;
            case 3: pathText = "The air grows cold, thick with an 800-year-old sorrow."; break;
            case 4: pathText = "You see a lone, demonic figure guarding a broken throne ahead."; break;
            case 5: pathText = "Beyond the fallen General, the throne of the Betrayed Champion awaits."; break;
            default: pathText = "The path before you is dark and narrow."; break;
        }
        
        go.move(105 - (pathText.length() / 2), 43);
        System.out.println(pathText);

        go.move(105 - ("[1] Move Forward".length() / 2), 47);
        System.out.println("[1] Move Forward");
        go.move(105 - ("[2] Steel Yourself".length() / 2), 48);
        System.out.println("[2] Steel Yourself");
        go.move(106, 50);

        int direction = input.nextInt();

        if (direction != 1) {
            screen.clear(0);
            go.move(0, 37);
            box.draw(209, 18);
            displayPlayerStatus();
            String msg = "You take a deep breath, steadying your nerves for what's to come.";
            go.move(105 - (msg.length() / 2), 45);
            System.out.println(msg);     
            screen.clear(1);//3
        }
    }

    private boolean triggerEncounter(boolean isFinalBoss){
        World1Mob mob; 
        
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        if(isFinalBoss){
            mob = new World5Boss.DemonLord();

            String msg = "The Demon Lord, the Betrayed Champion, turns to you. His eyes are hollow voids of hatred.";
            String msg2 = "Another hero... sent by the liars who made me this. Come. Finish what your ancestors started.";
            go.move(105 - (msg.length() / 2), 44);
            System.out.println(msg); 
            go.move(105 - (msg2.length() / 2), 46);
            System.out.println(msg2); 
        }else{
            mob = new World5Boss.Kyros();
           
            String msg = "A powerful demonic knight, " + mob.name + ", blocks your path.";
            String msg2 = "You will not harm my master. He has suffered enough at the hands of your kind!";
            go.move(105 - (msg.length() / 2), 44);
            System.out.println(msg);
            go.move(105 - (msg2.length() / 2), 46);
            System.out.println(msg2);
        }

        screen.clear(1); //6

        Battle battle = new Battle(input, player, mob, screen, go, box);
        boolean playerWon = battle.start();

        if(!playerWon){
            return false; 
        } else {
            return true;
        }
    }

    private void playStoryScene(int scene) {
        String[] lines = {};
        int delay = 4; 

        switch(scene) {
            case 0: 
                lines = new String[]{
                    "You step into The Rift.",
                    "This place is not of the world.",
                    "It is a wound in reality, echoing with shadow and sorrow."
                };
                delay = 3;
                break;
            case 1: //celeb
                lines = new String[]{
                    "A faint, golden echo shimmers in the darkness...",
                    "Our Champion! He has saved Senthra from the Calamity!",
                    "You see a vision of a hero, celebrated by thousands."
                };
                delay = 4;
                break;
            case 2: // consiparacy
                lines = new String[]{
                    "The echo shifts... you now see a king and his advisors in a dark room.",
                    "His power is too great. He is a threat to the throne.",
                    "The gods demand a sacrifice to seal the rift...",
                    "...the Champion will be our offering."
                };
                delay = 5;
                break;
            case 3: // betrayal
                lines = new String[]{
                    "The vision sharpens. The hero kneels at an altar, his weapon laid down.",
                    "His own king... his allies... they surround him, not with praise, but with chains.",
                    "It is the only way, my friend. Forgive us."
                };
                delay = 6;
                break;
            case 4: // the fall
                lines = new String[]{
                    "You hear a scream that transcends time, an agony of body and soul.",
                    "LIARS! BETRAYERS! YOU USED ME!",
                    "YOU... YOU WILL ALL... SUFFER!",
                    "The world shatters. The vision goes dark. You feel an immense hatred ahead."
                };
                delay = 5;
                break;
            case 6: 
                lines = new String[]{
                    "You approach the broken throne. The Demon Lord, the Betrayed Champion, rises.",
                    "He sees the last of Kyros's shadow fade from your weapon.",
                    "A terrible, guttural roar of pure grief echoes through the rift.",
                    "KYROS... NO! YOU... WHAT HAVE YOU DONE TO MY ONLY ALLY?!",
                    "HE WAS ALL I HAD LEFT... AND YOU... YOU TOOK HIM FROM ME!",
                    "NOW... YOU. WILL. PAY!"
                };
                delay = 6;
                break;
            case 7: 
                playEnding();
                return;
        }
        
        // Loop to print each line one by one
        for (String line : lines) {
            screen.clear(0);
            go.move(0, 37); 
            box.draw(209, 18);   
            displayPlayerStatus();
            
            go.move(105 - (line.length() / 2), 45); // Center each line
            System.out.println(line);
            screen.clear(delay); // Wait
        }
    }
  
    private void playEnding() {

        String msg1 = "With the final blow, the Demon Lord does not scream. He stumbles.";
        String msg2 = "The demonic shadows flee from his body, revealing the hero he once was.";
        
        screen.clear(0);
        go.move(0, 37); 
        box.draw(209, 18);   
        displayPlayerStatus();
        go.move(105 - (msg1.length() / 2), 43);
        System.out.println(msg1);
        go.move(105 - (msg2.length() / 2), 45);
        System.out.println(msg2);
        screen.clear(7);
        
        String msg3 = "He looks at his hands... then at you. A single, human tear cuts through the grime.";
        String msg4 = "Kyros... I am coming... Thank... you...";

        screen.clear(0);
        go.move(0, 37); 
        box.draw(209, 18);   
        displayPlayerStatus();
        go.move(105 - (msg3.length() / 2), 43);
        System.out.println(msg3);
        go.move(105 - (msg4.length() / 2), 45);
        System.out.println(msg4);
        screen.clear(1);//8
        
        screen.clear(0);
        go.move(0, 37); 
        box.draw(209, 18);   
        
        String end1 = "You have brought peace to the Betrayed Champion.";
        String end2 = "The rift seals. The world is saved.";
        
        go.move(105 - (end1.length() / 2), 43);
        System.out.println(end1);
        go.move(105 - (end2.length() / 2), 45);
        System.out.println(end2);
        screen.clear(1);//7
        
        screen.clear(0);
        go.move(0, 37); 
        box.draw(209, 18);
        
        String end3 = "But as you return, you see the statues of the treacherous king who saved Senthra.";
        String end4 = "You alone carry the terrible truth: the world you saved was built on a lie.";
        String end5 = "Your adventure is over.";
        
        go.move(105 - (end3.length() / 2), 43);
        System.out.println(end3);
        go.move(105 - (end4.length() / 2), 45);
        System.out.println(end4);
        screen.clear(1);//8

        go.move(105 - (end5.length() / 2), 48);
        System.out.println(end5);
        screen.clear(1);//8
        System.exit(0); 
    }

    private void openRewardChest() {
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        String title = "Kyros fades, leaving a chest of shadow and light. Choose your reward:";
        go.move(105 - (title.length() / 2), 43); 
        System.out.println(title);

        String opt1 = "[1] Blessing of the General (+50 Max HP & Full Heal)";
        String opt2 = "[2] Legacy of the General (+15 Temp. Damage for 5 battles)";
        String opt3 = "[3] Echo of the General (+30 Max Mana & Full Mana)";

        go.move(105 - (opt1.length() / 2), 46);
        System.out.println(opt1);
        go.move(105 - (opt2.length() / 2), 47);
        System.out.println(opt2);
        go.move(105 - (opt3.length() / 2), 48);
        System.out.println(opt3);

        go.move(106, 50); 
        int choice = input.nextInt();

        screen.clearLine(go, 70, 43, 80); 
        screen.clearLine(go, 70, 46, 80); 
        screen.clearLine(go, 70, 47, 80); 
        screen.clearLine(go, 70, 48, 80); 

        String msg = "";
        switch (choice) {
            case 1:
                player.maxHp += 50;
                player.hp = player.maxHp;
                msg = "You feel a surge of vitality! Max HP increased and restored to full!";
                break;
            case 2:
                player.addTemporaryDamage(15, 5); 
                msg = "The General's power flows into your weapon. Damage increased!";
                break;
            case 3:
                player.maxMana += 30;
                player.mana = player.maxMana;
                msg = "Your mind expands! Max Mana increased and restored to full!";
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
}