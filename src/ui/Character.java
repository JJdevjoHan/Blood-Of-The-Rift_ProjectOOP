package ui;

import java.util.Random;
import javax.swing.JTextArea;

public abstract class Character {
    public String name;
    public String className;
    public int hp;
    public int mana;
    public int maxHp;
    public int maxMana;

    protected Random random = new Random();
    private int temporaryDamageBuff = 0;
    private int damageBuffDuration = 0;

    public Character(String name, String className, int hp, int mana) {
        this.name = name;
        this.className = className;
        this.hp = this.maxHp = hp;
        this.mana = this.maxMana = mana;
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) this.hp = 0;
    }

    public int getDamageWithBuff(int baseDamage) {
        return baseDamage + this.temporaryDamageBuff;
    }
    
    public void addTemporaryDamage(int amount) {
        this.temporaryDamageBuff = amount;
        this.damageBuffDuration = 3; // lasts 3 turns
    }

    public void decrementDamageBuffDuration() {
        if (this.damageBuffDuration > 0) {
            this.damageBuffDuration--;
            if (this.damageBuffDuration == 0) {
                this.temporaryDamageBuff = 0;
            }
        }
    }

    public abstract String useSkill(int choice, World1Mob target);
    protected abstract void displaySkillsSwing(JTextArea battleLog);

	public void resetTemporaryDamage() {
		
	}
}
