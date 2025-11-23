package ui;


public class World3Mob extends World1Mob {
    public World3Mob(String name, int hp, int damage) {
        super(name, hp, damage);
    }

    @Override
    public String specialSkill(Character target) {
        int damage = (int)(this.damage * 1.8) + 10; 
        target.takeDamage(damage); 
        
        return name + " unleashes a powerful special attack! Deals " + damage + " damage. ";
    }

    public static class SnowGolem extends World3Mob 
    {
    	SnowGolem() 
    	{
    		super("Snow Golem",50, 25); 
    	} 
    }
    public static class WitchGnome extends World3Mob 
    { 
    	WitchGnome() 
    	{ 
    		super("Witch Gnome",40, 23); 
    	} 
    }
    public static class Yeti  extends World3Mob 
    { 
    	Yeti () 
    	{ 
    		super("Yeti",70, 22); 
    	} 
    }
    public static class GiantFrostWolves extends World3Mob 
    { 
    	GiantFrostWolves() 
    	{ 
    		super("Giant Frost Wolves",120,18); 
    	} 
    }
}
