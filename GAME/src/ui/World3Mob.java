package ui;

public class World3Mob extends World1Mob {
    public World3Mob(String name, int hp, int damage) {
        super(name, hp, damage);
    }


    public static class GiantFrostWolves extends World3Mob {//boss
        public GiantFrostWolves() {
            super("GiantFrostWolves", 120, 18);
        }
        
        @Override
        public String specialSkill(Character target) {
            int damage = (int)(this.damage * 1.8) + 10; // Increased damage for World 3 mobs
            target.takeDamage(damage); 
            
            return name + " unleashes a powerful special attack! Deals " + damage + " damage. ";
        }
    }

    public static class SnowGolem extends World3Mob {
        public SnowGolem() {
            super("SnowGolem", 50, 25);
        }
    }

    public static class WitchGnome extends World3Mob {
        public WitchGnome() {
            super("WitchGnome", 40, 23);
        }
    }

    public static class Yeti extends World3Mob {
        public Yeti() {
            super("Yeti", 70, 22);
        }
    }
}
