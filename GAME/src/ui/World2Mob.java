package ui;

public abstract class World2Mob extends World1Mob {
    public World2Mob(String name, int hp, int damage) {
        super(name, hp, damage);
    }

    @Override
    public String specialSkill(Character target) {
        int damage = (int)(this.damage * 1.7) + 7; //did make the mobs stronger :>>
        target.takeDamage(damage); 
        
        return name + " uses a devastating special attack! Deals " + damage + " damage. ";
    }
}

class Spider extends World2Mob {
    public Spider() {
        super("Spider", 40, 12);
    }
}

class Snake extends World2Mob {
    public Snake() {
        super("Snake", 30, 15);
    }
}

class GiantWorm extends World2Mob {
    public GiantWorm() {
        super("Giant Worm", 60, 13);
    }
    
    @Override
    public String specialSkill(Character player) {
        int specialDamage = 40; 
        player.takeDamage(specialDamage); // Silent
        
        return name + " uses its venom touch! Deals " + specialDamage + " damage. ";
    }
}

class Mummy extends World2Mob {
    public Mummy() {
        super("Mummy", 100, 15);
    }

}

