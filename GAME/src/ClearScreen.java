public class ClearScreen {
    public void clear(int seconds) {
        try {
            Thread.sleep(seconds * 1000);

            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("Failed to clear screen: " + e.getMessage());
        }
    }

    public void clearLine(GoToXY go, int x, int y, int length) {
        go.move(x, y);
        for (int i = 0; i < length; i++) {
            System.out.print(" ");
        }
        go.move(x, y); 
    }
}

