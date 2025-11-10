import java.util.Scanner;

public class Home {
    private Scanner input;
    private Character player;
    private ClearScreen screen;
    private GoToXY go;
    private DrawBox box;
    private int steps = 0;
    private boolean hasEquipment = false;
    private boolean leftHome = false;

    public Home(Scanner input, Character player, ClearScreen screen, GoToXY go, DrawBox box) {
        this.input = input;
        this.player = player;
        this.screen = screen;
        this.go = go;
        this.box = box;
    }

    public void enter() {
        final String GREEN = "\u001B[1;92m";
        final String BLUE = "\u001B[36m";
        final String RESET = "\u001B[0m";

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        go.move(90, 44);
        System.out.println("You wake up from a deep sleep,");
        go.move(84, 46);
        System.out.println("You look around your home for equipment ...");
        screen.clear(5);

        while (!hasEquipment) {
            screen.clear(0);
            go.move(0, 37);
            box.draw(209, 18);


            go.move(103, 18);
            System.out.printf("%s", player.name);
            CharacterIcon.Normal(104, 19);
            go.move(97, 24);
            System.out.print("HP: ");
            System.out.print(GREEN + "100" + RESET);
            System.out.print("   MP: ");
            System.out.println(BLUE + "50" + RESET);

            go.move(95, 43);
            System.out.println("Where do you want to go?");
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

            switch (direction) {
                case 1, 2, 3, 4 -> {
                    steps++;
                    screen.clear(0);
                    go.move(0, 37);
                    box.draw(209, 18);

                    go.move(103, 18);
                    System.out.printf("%s", player.name);
                    CharacterIcon.Normal(104, 19);
                    go.move(97, 24);
                    System.out.print("HP: ");
                    System.out.print(GREEN + "100" + RESET);
                    System.out.print("   MP: ");
                    System.out.println(BLUE + "50" + RESET);

                    go.move(98, 45);
                    System.out.println("You walk " + (direction == 1 ? "north." : direction == 2 ? "east." : direction == 3 ? "south." : "west."));

                    screen.clear(3);
                }
                default -> {
                    screen.clear(0);
                    go.move(0, 37);
                    box.draw(209, 18);
                    go.move(87, 45);
                    System.out.println("Invalid direction! Please choose [1] to [4]");
                    screen.clear(3);
                }
            }

            if (steps == 2) {
                go.move(0, 37);
                box.draw(209, 18);
                go.move(90, 43);
                System.out.println("You found a chest with equipment!");

                switch (player.className) {
                    case "Warrior" -> {
                        go.move(103, 18);
                        System.out.printf("%s", player.name);
                        go.move(93, 45);
                        System.out.println("You obtained Dual War Axes.");
                        CharacterIcon.Warrior(102, 20);
                        go.move(96, 48);
                        System.out.print( "HP: " + GREEN + "+80" + RESET + "     ");
                        System.out.println( "MP: " + BLUE + "+30" + RESET);
                    }
                    case "Paladin" -> {
                        go.move(103, 18);
                        System.out.printf("%s", player.name);
                        go.move(92, 45);
                        System.out.println("You obtained Sword and Shield.");
                        CharacterIcon.Paladin(105, 20);
                        go.move(97, 48);
                        System.out.print( "HP: " + GREEN + "+120" + RESET + "     ");
                        System.out.println( "MP: " + BLUE + "+70" + RESET);
                    }
                    case "Mage" -> {
                        go.move(103, 17);
                        System.out.printf("%s", player.name);
                        go.move(90, 45);
                        System.out.println("You obtained a Wand and Magic Hat.");
                        CharacterIcon.Mage(104, 19);
                        go.move(97, 48);
                        System.out.print( "HP: " + GREEN + "+20" + RESET + "     ");
                        System.out.println( "MP: " + BLUE + "+100" + RESET);
                    }
                }

                go.move(97, 24);
                System.out.print("HP: ");
                System.out.print(GREEN + player.hp + RESET);
                System.out.print("   MP: ");
                System.out.println(BLUE + player.mana + RESET);
                screen.clear(7);
                hasEquipment = true;
            }
        }

        while (!leftHome) {
            screen.clear(0);
            go.move(0, 37);
            box.draw(209, 18);

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

            go.move(97, 24);
            System.out.print("HP: ");
            System.out.print(GREEN + player.hp + RESET);
            System.out.print("   MP: ");
            System.out.println(BLUE + player.mana + RESET);

            go.move(101, 44);
            System.out.println("Leave home?");
            go.move(95, 46);
            System.out.println("[1] - Yes     [2] - No");
            go.move(106, 48);
            int choice = input.nextInt();

            if (choice == 1) {

                screen.clear(0);

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

                go.move(97, 24);
                System.out.print("HP: ");
                System.out.print(GREEN + player.hp + RESET);
                System.out.print("   MP: ");
                System.out.println(BLUE + player.mana + RESET);

                leftHome = true;
                go.move(0, 37);
                box.draw(209, 18);
                go.move(87, 45);
                System.out.println("You step outside, ready for your adventure...");
                screen.clear(4);

            } else {

                screen.clear(0);

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

                go.move(97, 24);
                System.out.print("HP: ");
                System.out.print(GREEN + player.hp + RESET);
                System.out.print("   MP: ");
                System.out.println(BLUE + player.mana + RESET);


                go.move(0, 37);
                box.draw(209, 18);
                go.move(78, 45);
                System.out.println("You're now equipped and ready. It's time to begin your journey.");
                screen.clear(5);
            }
        }
    }
}