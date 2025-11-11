public class Intro {
    private ClearScreen screen;
    private GoToXY go;

    public Intro(DrawBox box, ClearScreen screen, GoToXY go) {
        this.screen = screen;
        this.go = go;
    }

    public void play() {

        screen.clear(0);

        go.move(96, 27);
        System.out.print("A thousand years ago...");
        screen.clear(3);

        go.move(68, 27);
        System.out.print("a mighty hero was chosen to save the Kingdom of Sethra from the wrath of gods...");
        screen.clear(6);

        go.move(56, 27);
        System.out.print("betrayed by his own people, he was used as a scapegoat and sacrificed to seal the calamities...");
        screen.clear(8);

        go.move(64, 27);
        System.out.print("but the world still fell apart, a great rift tore reality into multiple realms...");
        screen.clear(6);

        go.move(87, 27);
        System.out.print("although the champion did not die...");
        screen.clear(5);

        go.move(64, 27);
        System.out.print("instead, he was cursed, left less than human, and forgotten in the shadows of the rift...");
        screen.clear(8);

        go.move(72, 27);
        System.out.print("Eight hundred years later, new heroes rise from the divided worlds...");
        screen.clear(6);

        go.move(63, 27);
        System.out.print("Believing the fallen Hero to be the cause of the chaos, they set out to defeat him...");
        screen.clear(8);


    }
}