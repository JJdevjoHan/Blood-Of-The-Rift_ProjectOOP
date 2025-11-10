import java.util.Scanner;

public class Battle {
    private Scanner input;
    private Character player;
    private World1Mob mob;
    private ClearScreen screen;
    private GoToXY go;
    private DrawBox box;

    public Battle(Scanner input, Character player, World1Mob mob, ClearScreen screen, GoToXY go, DrawBox box) {
        this.input = input;
        this.player = player;
        this.mob = mob;
        this.screen = screen;
        this.go = go;
        this.box = box;
    }

public boolean start() {
    screen.clear(0);
    go.move(0, 37);
    box.draw(209, 18);
    go.move(92, 45);
    System.out.println("A wild " + mob.name + " has appeared!");
    screen.clear(3);

    while (player.isAlive() && mob.isAlive()) {
        playerTurn();
        if (!mob.isAlive()) break;
        mobTurn();
    }

    if (player.isAlive()) {
        box.draw(209, 18);
        go.move(92, 45);
        System.out.println("You have defeated the " + mob.name + "!");
        player.decrementDamageBuffDuration();
        screen.clear(3);
        return true;
    }
    else {
        return false;
    }
}

private void playerTurn() {
    displayBattleStatus();
    go.move(90, 41);
    System.out.println("Your turn. Choose your skill:");
    player.displaySkills();
    go.move(106, 48);

    int choice = input.nextInt();
    screen.clear(0);
    go.move(0, 37);
    box.draw(209, 18);
    go.move(80, 48);
    player.useSkill(choice, mob);
    screen.clear(4);
}

private void mobTurn() {
    displayBattleStatus();


    // Simple AI >> 30% chance to use special skill, otherwise normal attack
    if (Math.random() < 0.3) {
        mob.specialSkill(player);
    } else {
        mob.attack(player);
    }
    screen.clear(4);
}

private void displayBattleStatus() {
    screen.clear(0);
    go.move(0, 37);
    box.draw(209, 18);

    switch (player.className) {
        case "Warrior" -> {
            go.move(46, 18);
            System.out.printf("%s", player.name);
            CharacterIcon.Warrior(45, 20);
        }
        case "Paladin" -> {
            go.move(46, 18);
            System.out.printf("%s", player.name);
            CharacterIcon.Paladin(48, 20);
        }
        case "Mage" -> {
            go.move(46, 17);
            System.out.printf("%s", player.name);
            CharacterIcon.Mage(47, 19);
        }
    }

    go.move(36, 24);
    System.out.printf("HP: \u001B[1;92m%d/%d\u001B[0m   MP: \u001B[36m%d/%d\u001B[0m", player.hp, player.maxHp, player.mana, player.maxMana);


    go.move(160, 18);
    System.out.printf("%s", mob.name);
    go.move(157, 24);
    System.out.printf("HP: \u001B[1;91m%d\u001B[0m", mob.hp);
    switch (mob.name) {
        case "Slime": CharacterIcon.Slime(162, 20); break;
        case "Wild Bull": CharacterIcon.WildBull(162, 20); break;
        case "Dire Wolf": CharacterIcon.Wolf(162, 20); break;
        case "Minotaur": CharacterIcon.Minotaur(162, 20); break;
    }
}
}