package ui;

public class World4Mob extends World1Mob {
    public World4Mob(String name, int hp, int damage) {
       super(name, hp, damage); 
    }

    @Override
    public String specialSkill(Character target) {
        int damage = (int)(this.damage * 2.0) + 8; // Increased damage for World 4 mobs
        target.takeDamage(damage); 
        
        return name + " performs an ultimate special attack! Deals " + damage + " damage. ";
    }

    public static class LavaImp extends World4Mob {
        public LavaImp() {
            super("Lava Imp", 18, 20);
        }
    }

    public static class MagmaBeast extends World4Mob{
        public MagmaBeast(){
            super("Magma Beast", 20, 18);
        }
    }   

    public static class SkeletonHead extends World4Mob{
        public SkeletonHead(){
            super("Skeleton Head", 120, 10);
        }
    }

    public static class Golem extends World4Mob{
        public Golem(){
            super("Golem", 19, 20);
        }
    }  
}

//LavaImp
//MagmaBeast
//SkeletonHead
//Golem

