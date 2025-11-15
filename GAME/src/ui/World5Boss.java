package ui;

import java.util.Random;

public class World5Boss extends World1Mob {
    
    protected Random random = new Random();

    public World5Boss(String name, int hp, int damage) {
        super(name, hp, damage);
    }

    @Override
    public String attack(Character target) {
        int damage = (int)(Math.random() * this.damage) + 7; //added critic for boss effects-chrisnel
        String critMessage = "";

        if (random.nextInt(100) < 20) { // 20% crit chance
            damage = (int)(damage * 1.5);
            critMessage = " >> CRITICAL HIT! <<";
        }
        target.takeDamage(damage); 
        
        return name + " attacks " + target.name + "!" + critMessage + " Deals " + damage + " damage. ";
    }

    @Override
    public String specialSkill(Character target) {
        int damage = (int)(this.damage * 1.8) + 15; 
        String critMessage = "";
        
        if (random.nextInt(100) < 20) {
            damage = (int)(damage * 1.5);
            critMessage = " >> CRITICAL HIT! <<";
        }
        
        target.takeDamage(damage); 
        
        return name + " uses a special attack!" + critMessage + " Deals " + damage + " damage. ";
    }
    
    public static class DemonLord extends World5Boss {
        public DemonLord() {
            super("Demon Lord", 1, 35); //300
        }
        @Override
        public String specialSkill(Character target) {
            int specialDamage = 60;
            target.takeDamage(specialDamage);
            return "Demon Lord" + " uses 'Hellfire Blast'! Deals " + specialDamage + " damage. ";
        }
    }

    public static class Kyros extends World5Boss {
        public Kyros() {
            super("General | Kyros", 1, 25); //160
        }

        @Override
        public String specialSkill(Character target) {
            int specialDamage = 50;
            target.takeDamage(specialDamage);
            return "Kyros" + " uses 'Shadow Flare'! Deals " + specialDamage + " damage. ";
        }
    }
}