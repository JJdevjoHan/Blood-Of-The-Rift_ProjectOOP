package ui;

public abstract class World1Mob {
    public String name;
    public int hp;
    public int maxHp;
    public int damage;

    public World1Mob(String name, int hp, int damage) {
        this.name = name;
        this.hp = this.maxHp = hp;
        this.damage = damage;
    }

    public boolean isAlive() {
        return hp > 0;
    }
 
    public void takeDamage(int dmg) {
        this.hp -= dmg;
        if(this.hp < 0) this.hp = 0;
    }

    public String attack(Character target) {
        int damage = (int)(Math.random() * this.damage) + 1;
        target.takeDamage(damage); // Silent
        
        // Return the full message string
        return name + " attacks " + target.name + " for " + damage + " damage. ";
    }

    public String specialSkill(Character target) {
        int damage = (int)(this.damage * 1.5) + 5; 
        target.takeDamage(damage); // Silent
        
        // Return the full message stng
        return name + " uses a special attack! Deals " + damage + " damage. ";
    }
}

class Slime extends World1Mob {
    public Slime() {
        super("Slime", 20, 5);
    }
}

class Bull extends World1Mob {
    public Bull() {
        super("Wild Bull", 30, 8);
    }
}

class Wolf extends World1Mob {
    public Wolf() {
        super("Dire Wolf", 40, 10);
    }
}

class Minotaur extends World1Mob {
    public Minotaur() {
        super("Minotaur", 80, 12);
    } 

    @Override
    public String specialSkill(Character player) {
        int specialDamage = 35; 
        player.takeDamage(specialDamage);
        
        return "Minotaur uses Earth Shatter! Deals " + specialDamage + " damage. ";
    }
}
