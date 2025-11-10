public class World1Mob {
    protected String name;
    protected int hp;
    protected int damage;

    public World1Mob(String name, int hp, int damage) {
        this.name = name;
        this.hp = hp;
        this.damage = damage;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
        System.out.println(name + " takes " + dmg + " damage. HP: " + hp);
    }

    public void attack(Character player) {
        System.out.println(name + " attacks " + player.name + " for " + damage + " damage.");
        player.hp -= damage;
        System.out.println(player.name + " HP: " + player.hp);
    }

    public void specialSkill(Character player) {
        // Default: no skill
    }
}

class Slime extends World1Mob {
    public Slime() {
        super("Slime", 20, 5);
    }           // lessen hp & dmg -erlo
}

class Bull extends World1Mob {
    public Bull() {
        super("Wild Bull", 30, 8);
    }       // lessen hp & dmg -erlo
}

class Wolf extends World1Mob {
    public Wolf() {
        super("Dire Wolf", 40, 10);
    }       // lessen hp & dmg-erlo
}

class Minotaur extends World1Mob {
    public Minotaur() {
        super("Minotaur", 80, 15);
    }   // lessen hp & dmg-erlo

    @Override
    public void specialSkill(Character player) {
        System.out.println("Minotaur uses Earth Shatter! Deals 35 damage.");
        player.hp -= 35;
        System.out.println(player.name + " HP: " + player.hp);
    }
}