public class World2Mob extends World1Mob {
    public World2Mob(String name, int hp, int damage) {
        super(name, hp, damage);
    }

    @Override
    public String specialSkill(Character target) {
        int damage = (int)(this.damage * 1.7) + 7; //did make the mobs stronger :>>
        target.takeDamage(damage); 
        
        return name + " uses a devastating special attack! Deals " + damage + " damage. ";
    }

    public static class Spider extends World2Mob {
        public Spider() {
            super("Spider", 5, 12);//40
        }
    }

    public static class Snake extends World2Mob {
        public Snake() {
            super("Snake", 5, 15); //30
        }
    }

    public static class GiantWorm extends World2Mob {
        public GiantWorm() {
            super("Giant Worm", 5, 13); //60
        }
    }

    public static class Mummy extends World2Mob {
        public Mummy() {
            super("Mummy", 5, 15); //100
        }

        @Override
        public String specialSkill(Character player) {
            int specialDamage = 40; 
            player.takeDamage(specialDamage); // Silent
            
            return name + " uses its cursed touch! Deals " + specialDamage + " damage. ";
        }
    }
}