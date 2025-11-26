package ui;

import javax.swing.JTextArea;
import java.util.Random;

public abstract class Character {
    public String name;
    public String className;
    public int hp;
    public int mana;
    public int maxHp;
    public int maxMana;

    // FIX 1: Added tempDamage so chests work
    public int tempDamage = 0;

    // FIX 2: Added Random to parent
    protected Random rng;

    public Character(String name, String className, int hp, int mana, Random rng) {
        this.name = name;
        this.className = className;
        this.hp = this.maxHp = hp;
        this.mana = this.maxMana = mana;
        this.rng = rng;
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) this.hp = 0;
    }

    public int getDamageWithBuff(int baseDamage) {
        return baseDamage + this.tempDamage;
    }

    // Used by console version, keeping for compatibility
    public void addTemporaryDamage(int amount) {
        this.tempDamage += amount;
    }

    public abstract String useSkill(int choice, World1Mob target);

    // Added for compatibility if needed
    protected void displaySkillsSwing(JTextArea battleLog) {}
}