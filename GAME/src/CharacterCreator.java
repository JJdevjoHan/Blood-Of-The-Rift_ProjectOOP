import java.util.Scanner;

//warrior
// high dmg output
// normal defense
// low mana reserves

//paladin
// low dmg output
// high defense (less dmg receive)
// normal mana reserves

//wizard
// normal dmg output
// low defense (high dmg receive)
// high mana reserves

public class CharacterCreator {

    public static Character create(Scanner input, ClearScreen screen, GoToXY go, DrawBox box) {
        final String GREEN = "\u001B[1;92m";
        final String BLUE = "\u001B[36m";
        final String RESET = "\u001B[0m";

        String playerName = "";

        while (true) {
            screen.clear(0);
            go.move(0, 37);
            box.draw(209, 18);

            go.move(98, 43);
            System.out.print("What is your name?");

            go.move(104, 46);
            System.out.print("_____");

            go.move(104, 46);
            playerName = input.next();

            if (playerName.length() != 5) {
                screen.clear(0);
                go.move(0, 37);
                box.draw(209, 18);
                go.move(93, 44);
                System.out.println("Name must be 5 characters long!");
                go.move(102, 46);
                System.out.println("Try Again ...");
                screen.clear(3);
                continue;
            }

            screen.clear(0);
            go.move(0, 37);
            box.draw(209, 18);
            go.move(96, 44);
            System.out.printf("Is your name, %s?", playerName);
            go.move(94, 50);
            System.out.println("[1] - Yes       [2] - No");
            go.move(106, 48);
            int confirm = input.nextInt();

            if (confirm == 1) break;
        }

        Character player;

        while (true) {
            screen.clear(0);
            go.move(0, 37);
            box.draw(209, 18);

            go.move(97, 44);
            System.out.println("Choose your class!");

            go.move(60, 24);
            System.out.println("Paladin");
            go.move(62, 25);
            System.out.println("[1]");

            go.move(104, 24);
            System.out.println("Mage");
            go.move(104, 25);
            System.out.println("[2]");

            go.move(146, 24);
            System.out.println("Warrior");
            go.move(148, 25);
            System.out.println("[3]");

            CharacterIcon.Paladin(63, 20);
            CharacterIcon.Mage(104, 19);
            CharacterIcon.Warrior(146, 20);


            go.move(111, 48);
            System.out.println("[3]-Warrior");
            go.move(102, 50);
            System.out.println("[2]-Mage");
            go.move(89, 48);
            System.out.println("[1]-Paladin");
            go.move(106, 49);

            go.move(106, 48);
            int classChoice = input.nextInt();

            String className;
            switch (classChoice) {
                case 1 -> className = "Paladin";
                case 2 -> className = "Mage";
                case 3 -> className = "Warrior";
                default -> {
                    screen.clear(0);
                    go.move(0, 37);
                    box.draw(209, 18);
                    go.move(88, 45);
                    System.out.println("Invalid choice! Please choose [1] to [3].");
                    screen.clear(3);
                    continue;
                }
            }

            switch (className) {
                case "Paladin" -> player = new Paladin(playerName);
                case "Mage" -> player = new Mage(playerName);
                case "Warrior" -> player = new Warrior(playerName);
                default -> throw new IllegalStateException("Unexpected class: " + className);
            }

            screen.clear(0);

            switch (player.className) {
                case "Paladin" -> {
                    box.moveable(52, 18, 20,11);
                    box.moveable(52, 18, 140,11);
                    CharacterIcon.Paladin(105, 20);
                    go.move(102, 24);
                    System.out.println("Paladin");
                    go.move(104, 25);
                    System.out.println("[1]");
                    go.move(45, 13);
                    System.out.println("Info");
                    go.move(30,16);
                    System.out.println("A holy knight devoted to justice and\n");
                    go.move(23,17);
                    System.out.println("righteousness, combining martial prowess with");
                    go.move(30,18);
                    System.out.println("divine magic to protect and heal");
                    go.move(36,20);
                    System.out.print( "HP: " + GREEN + "+120" + RESET + "     ");
                    System.out.println( "MP: " + BLUE + "+70" + RESET);


                    go.move(163, 12);
                    System.out.println("Skills");
                }
                case "Mage" -> {
                    box.moveable(52, 18, 20,11);
                    box.moveable(52, 18, 140,11);
                    CharacterIcon.Mage(104, 19);
                    go.move(104, 24);
                    System.out.println("Mage");
                    go.move(104, 25);
                    System.out.println("[2]");
                    go.move(40, 12);
                    System.out.println("Class Info");
                    go.move(30,16);
                    System.out.println(" ");
                    go.move(23,17);
                    System.out.println(" ");
                    go.move(30,18);
                    System.out.println(" ");
                    go.move(36,20);
                    System.out.print( "HP: " + GREEN + "+n" + RESET + "     ");
                    System.out.println( "MP: " + BLUE + "+n" + RESET);
                }
                case "Warrior" -> {
                    box.moveable(52, 18, 20,11);
                    box.moveable(52, 18, 140,11);
                    CharacterIcon.Warrior(102, 20);
                    go.move(102, 24);
                    System.out.println("Warrior");
                    go.move(104, 25);
                    System.out.println("[3]");
                    go.move(40, 12);
                    System.out.println("Class Info");
                    go.move(30,16);
                    System.out.println(" ");
                    go.move(23,17);
                    System.out.println(" ");
                    go.move(30,18);
                    System.out.println(" ");
                    go.move(36,20);
                    System.out.print( "HP: " + GREEN + "+n" + RESET + "     ");
                    System.out.println( "MP: " + BLUE + "+n" + RESET);
                }
            }

            go.move(0, 37);
            box.draw(209, 18);

            go.move(88, 44);
            System.out.printf("Would you like %s to be your class?", className);
            go.move(94, 50);
            System.out.println("[1] - Yes       [2] - No");
            go.move(106, 48);
            int confirmClass = input.nextInt();

            if (confirmClass == 1) break;
        }

        screen.clear(0);
        switch (player.className) {
            case "Paladin" -> {
                go.move(103, 18);
                System.out.printf("%s", player.name);
                go.move(97, 24);
                System.out.print("HP: ");
                System.out.print(GREEN + player.hp + RESET);
                System.out.print("   MP: ");
                System.out.println(BLUE + player.mana + RESET);
                CharacterIcon.Paladin(105, 20);
            }
            case "Mage" -> {
                go.move(103, 17);
                System.out.printf("%s", player.name);
                go.move(97, 24);
                System.out.print("HP: ");
                System.out.print(GREEN + player.hp + RESET);
                System.out.print("   MP: ");
                System.out.println(BLUE + player.mana + RESET);
                CharacterIcon.Mage(104, 19);
            }
            case "Warrior" -> {
                go.move(103, 18);
                System.out.printf("%s", player.name);
                go.move(97, 24);
                System.out.print("HP: ");
                System.out.print(GREEN + player.hp + RESET);
                System.out.print("   MP: ");
                System.out.println(BLUE + player.mana + RESET);
                CharacterIcon.Warrior(102, 20);
            }
        }


        go.move(0, 37);
        box.draw(209, 18);

        go.move(97, 43);
        System.out.print("Welcome to the world,");
        go.move(104, 45);
        System.out.printf("%s!%n", player.name);

        go.move(95, 48);
        System.out.println("Enter [1] to continue ...");
        go.move(106, 50);


        return player;
    }
}