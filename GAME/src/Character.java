public abstract class Character {
    private GoToXY go;
    public String name;
    public String className;
    public int hp;
    public int mana;
    public int maxHp;
    public int maxMana;

    private int temporaryDamageBuff = 0;
    private int damageBuffDuration = 0;

    public Character(String name, String className, int hp, int mana) {
        this.name = name;
        this.className = className;
        this.hp = this.maxHp = hp;
        this.mana = this.maxMana = mana;
        this.go = go;
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) this.hp = 0;
        System.out.println(this.name + " takes " + damage + " damage! HP is now " + this.hp);
    }

    public int getDamageWithBuff(int baseDamage) {
        return baseDamage + this.temporaryDamageBuff;
    }

    public void addTemporaryDamage(int amount) {
        this.temporaryDamageBuff = amount;
        this.damageBuffDuration = 3; // Lasts for 3 battles
    }

    public void decrementDamageBuffDuration() {
        if (this.damageBuffDuration > 0) {
            this.damageBuffDuration--;
            if (this.damageBuffDuration == 0) {
                this.temporaryDamageBuff = 0;
                System.out.println("Your temporary damage buff has worn off.");
            }
        }
    }

    // Abstract methods to be implemented by each class
    public abstract void displaySkills();
    public abstract void useSkill(int choice, World1Mob target);
}

class Warrior extends Character {
    public Warrior(String playerName) {
        super(playerName, "Warrior", 180, 80);
    }

    @Override
    public void displaySkills() {
        go.move();
        System.out.println("[1] Stone Slash (0-12 Dmg, +10 Mana)");
        System.out.println("[2] Flame Strike (13-22 Dmg, 20 Mana)");
        System.out.println("[3] Earthquake Blade (23-35 Dmg, 30 Mana)");
    }

    @Override
    public void useSkill(int choice, World1Mob target) {
        int damage;
        switch (choice) {
            case 1:
                damage = getDamageWithBuff((int)(Math.random() * 13));
                mana += 10;
                System.out.println(name + " used Stone Slash! Deals " + damage + " damage.");
                target.takeDamage(damage);
                break;
            case 2:
                if (mana >= 20) {
                    damage = getDamageWithBuff(13 + (int)(Math.random() * 10));
                    mana -= 20;
                    System.out.println(name + " used Flame Strike! Deals " + damage + " damage.");
                    target.takeDamage(damage);
                } else {
                    System.out.println("Not enough mana!");
                }
                break;
            case 3:
                if (mana >= 30) {
                    damage = getDamageWithBuff(23 + (int)(Math.random() * 13));
                    mana -= 30;
                    System.out.println(name + " used Earthquake Blade! Deals " + damage + " damage.");
                    target.takeDamage(damage);
                } else {
                    System.out.println("Not enough mana!");
                }
                break;
            default:
                System.out.println("Invalid skill choice.");
                break;
        }
    }
}

class Mage extends Character {
    public Mage(String playerName) {
        super(playerName, "Mage", 120, 150);
    }

    @Override
    public void displaySkills() {
        System.out.println("[1] Frost Bolt (0-10 Dmg, +10 Mana)");
        System.out.println("[2] Rune Burst (11-20 Dmg, 20 Mana)");
        System.out.println("[3] Lightstorm (21-35 Dmg, 30 Mana)");
    }

    @Override
    public void useSkill(int choice, World1Mob target) {
        int damage;
        switch (choice) {
            case 1:
                damage = getDamageWithBuff((int)(Math.random() * 11)); // 0-10 damage
                mana += 10;
                if (mana > maxMana) mana = maxMana; // Prevent mana overflow
                System.out.println(name + " used Frost Bolt! It deals " + damage + " damage.");
                target.takeDamage(damage);
                break;
            case 2:
                if (mana >= 20) {
                    damage = getDamageWithBuff(11 + (int)(Math.random() * 10)); // 11-20 damage
                    mana -= 20;
                    System.out.println(name + " used Rune Burst! It deals " + damage + " damage.");
                    target.takeDamage(damage);
                } else {
                    System.out.println("Not enough mana!");
                }
                break;
            case 3:
                if (mana >= 30) {
                    damage = getDamageWithBuff(21 + (int)(Math.random() * 15)); // 21-35 damage
                    mana -= 30;
                    System.out.println(name + " used Lightstorm! It deals " + damage + " damage.");
                    target.takeDamage(damage);
                } else {
                    System.out.println("Not enough mana!");
                }
                break;
            default:
                System.out.println("Invalid skill choice. Turn skipped.");
                break;
        }
    }
}

class Paladin extends Character {
    public Paladin(String playerName) {
        super(playerName, "Paladin", 220, 120);
    }

    @Override
    public void displaySkills() {
        System.out.println("[1] Shield Bash (0-8 Dmg, +10 Mana)");
        System.out.println("[2] Radiant Guard (Reduces damage taken, 20 Mana)");
        System.out.println("[3] Holy Renewal (Heal 20-35 HP, 30 Mana)");
    }

    @Override
    public void useSkill(int choice, World1Mob target) {
        int damage;
        switch (choice) {
            case 1:
                damage = getDamageWithBuff((int)(Math.random() * 9)); // 0-8 damage
                mana += 10;
                if (mana > maxMana) mana = maxMana; // Prevent mana overflow
                System.out.println(name + " used Shield Bash! It deals " + damage + " damage.");
                target.takeDamage(damage);
                break;
            case 2:
                if (mana >= 20) {
                    mana -= 20;
                    System.out.println(name + " used Radiant Guard! Damage taken will be reduced.");
                } else {
                    System.out.println("Not enough mana!");
                }
                break;
            case 3:
                if (mana >= 30) {
                    int healAmount = 20 + (int)(Math.random() * 16); // 20-35 heal
                    mana -= 30;
                    this.hp += healAmount;
                    if (this.hp > this.maxHp) {
                        this.hp = this.maxHp; // Prevent overhealing
                    }
                    System.out.println(name + " used Holy Renewal! You heal for " + healAmount + " HP.");
                } else {
                    System.out.println("Not enough mana!");
                }
                break;
            default:
                System.out.println("Invalid skill choice. Turn skipped.");
                break;
        }
    }
}