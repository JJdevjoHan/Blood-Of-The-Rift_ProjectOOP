import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        DrawBox box = new DrawBox();
        ClearScreen screen = new ClearScreen();
        GoToXY go = new GoToXY();

        screen.clear(0);

        go.move(100, 26);
        System.out.print("Skip the intro?");
        go.move(95, 28);
        System.out.println("[1] - Yes          [2] - No");

        go.move(106, 50);
        int choice = input.nextInt();

        if (choice == 2) {
            Intro intro = new Intro(box, screen, go);
            intro.play();
        } else {
            screen.clear(0);
        }

        SplashScreen welcome = new SplashScreen(screen, go);
        welcome.play();

        go.move(106, 40);
        int choice2 = input.nextInt();

        if (choice2 == 1) {
            screen.clear(0);

            Character player = CharacterCreator.create(input, screen, go, box);
            Home home = new Home(input, player, screen, go, box);
            home.enter();
            //add world 
            FinalWorld finalWorld = new FinalWorld(input, player, screen, go, box);
            LavaWorld lavaWorld = new LavaWorld(input, player, screen, go, box, finalWorld);
            SnowyIsland snowyIsland = new SnowyIsland(input, player, screen, go, box, lavaWorld);
            DesertWorld desertWorld = new DesertWorld(input, player, screen, go, box, snowyIsland);
            GrassyPlains grassyPlains = new GrassyPlains(input, player, screen, go, box, desertWorld);
            grassyPlains.explore();
        }
        else {
            screen.clear(0);
            go.move(92,27);
            System.exit(0);
        }
    }
}