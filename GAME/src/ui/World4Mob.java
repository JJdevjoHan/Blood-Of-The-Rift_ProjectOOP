package ui;

public class World4Mob extends World1Mob {
    public World4Mob(String name, int hp, int damage) {
       super(name, hp, damage); 
    }


    public static class LavaImp extends World4Mob {
        public LavaImp() {
            super("LavaImp", 5, 20);
        }
    }

    public static class MagmaBeast extends World4Mob{
        public MagmaBeast(){
            super("MagmaBeast", 5, 18);
        }
    }   

    public static class SkeletonHead extends World4Mob{
        public SkeletonHead(){
            super("SkeletonHead", 5, 10);
        }
        
        @Override
        public String specialSkill(Character target) {
            int damage = (int)(this.damage * 2.0) + 8; // Increased damage for World 4 mobs
            target.takeDamage(damage); 
            
            return name + " performs an ultimate special attack! Deals " + damage + " damage. ";
        }
    }

    public static class Golem extends World4Mob{
        public Golem(){
            super("Golem", 5, 20);
        }
    }   
}

//LavaImp
//MagmaBeast
//SkeletonHead
//Golem

