public class SplashScreen {
    private ClearScreen screen;
    private GoToXY go;

    public SplashScreen(ClearScreen screen, GoToXY go) {
        this.screen = screen;
        this.go = go;
    }

    public void play() {

        screen.clear(0);
        go.move(56,23);
        System.out.print("__________.__                    .___         _____    __  .__             __________.__  _____  __   ");
        go.move(56, 24);
        System.out.println("\\______   \\  |   ____   ____   __| _/   _____/ ____\\ _/  |_|  |__  ____    \\______   \\__|/ ____\\/  |_ ");
        go.move(56, 25);
        System.out.println(" |    |  _/  |  /  _ \\ /  _ \\ / __ |   /  _ \\   __\\  \\   __\\  |  \\/ __ \\    |       _/  \\   __\\\\   __\\");
        go.move(56, 26);
        System.out.println(" |    |   \\  |_|  (_) |  (_) | /_/ |  |  (_) |  |     |  | |   Y  \\ ___/    |    |   \\  ||  |   |  |   ");
        go.move(56, 27);
        System.out.println(" |______  /____/\\____/ \\____/\\____ |   \\____/|__|     |__| |___|  /____)    |____|_  /__||__|   |__|   ");
        go.move(56, 28);
        System.out.println("        \\/                        \\/                            \\/                 \\/                  ");


        go.move(100,29);
        System.out.print("version 0.4.3");

        go.move(92,32);
        System.out.print("[1] - Play        [2] - Exit");
    }

}
