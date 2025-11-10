public class DrawBox {
    public void draw(int width, int height) {

        System.out.print("┌");
        for (int i = 0; i < width - 2; i++) {
            System.out.print("─");
        }
        System.out.println("┐");

        for (int i = 0; i < height - 2; i++) {
            System.out.print("│");
            for (int j = 0; j < width - 2; j++) {
                System.out.print(" ");
            }
            System.out.println("│");
        }

        System.out.print("└");
        for (int i = 0; i < width - 2; i++) {
            System.out.print("─");
        }
        System.out.println("┘");
    }

    public void moveable(int width, int height, int x, int y){

        System.out.printf("\033[%d;%dH%s", y, x, "┌");
        for (int i = 0; i < width - 2; i++) {
            System.out.print("─");
        }
        System.out.println("┐");


        int spaces = 0;
        for (int i = 0; i < height - 2; i++) {
            System.out.printf("\033[%d;%dH%s", y+1 +i, x,"│");
            for (int j = 0; j < width - 2; j++) {
                System.out.print(" ");
            }
            System.out.println("│");
            spaces = i;
        }

        System.out.printf("\033[%d;%dH%s", y + spaces + 2, x, "└");
        for (int i = 0; i < width - 2; i++) {
            System.out.print("─");
        }
        System.out.println("┘");
    }

}
