import java.util.Random;

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
        this.damageBuffDuration = 3;
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
    
    public abstract void displaySkills(GoToXY go, int boxStartX, int boxWidth, int yStart);
    
    public abstract String useSkill(int choice, World1Mob target);
}

class Warrior extends Character {
    public Warrior(String playerName) {
        super(playerName, "Warrior", 180, 80);
    }

    @Override
    public void displaySkills(GoToXY go, int boxStartX, int boxWidth, int yStart) {
        String[] skills = {
            "[1] Stone Slash (0-12 Dmg, +10 Mana)",
            "[2] Flame Strike (13-22 Dmg, 20 Mana)",
            "[3] Earthquake Blade (23-35 Dmg, 30 Mana)"
        };
        for (int i = 0; i < skills.length; i++) {
            int centerX = boxStartX + (boxWidth - skills[i].length()) / 2;
            go.move(centerX, yStart + i);
            System.out.print(skills[i]);
        }
    }

    @Override
    public String useSkill(int choice, World1Mob target) {
        int damage;
        String message;
        String critMessage = "";

        switch (choice) {
            case 1:
                damage = getDamageWithBuff((int)(Math.random() * 13));
                mana += 10;
                if (mana > maxMana) mana = maxMana;
                
                if (random.nextInt(100) < 20) { // 20% chance
                    damage = (int)(damage * 1.5); // 1.5x damage
                    critMessage = " >> CRITICAL HIT! <<";
                }
                
                target.takeDamage(damage);
                message = name + " used Stone Slash!" + critMessage + " Deals " + damage + " damage. ";
                return message;
            case 2:
                if (mana >= 20) {
                    damage = getDamageWithBuff(13 + (int)(Math.random() * 10));
                    mana -= 20;

                    if (random.nextInt(100) < 20) {
                        damage = (int)(damage * 1.5);
                        critMessage = " >> CRITICAL HIT! <<";
                    }

                    target.takeDamage(damage);
                    message = name + " used Flame Strike!" + critMessage + " Deals " + damage + " damage. ";
                    return message;
                } else {
                    return "Not enough mana!";
                }
            case 3:
                if (mana >= 30) {
                    damage = getDamageWithBuff(23 + (int)(Math.random() * 13));
                    mana -= 30;

                    if (random.nextInt(100) < 20) {
                        damage = (int)(damage * 1.5);
                        critMessage = " >> CRITICAL HIT! <<";
                    }

                    target.takeDamage(damage);
                    message = name + " used Earthquake Blade!" + critMessage + " Deals " + damage + " damage. ";
                    return message;
                } else {
                    return "Not enough mana!";
                }
            default:
                return "Invalid skill choice.";
        }
    }
}

class Mage extends Character {
    public Mage(String playerName) {
        super(playerName, "Mage", 120, 150);
    }

    @Override
    public void displaySkills(GoToXY go, int boxStartX, int boxWidth, int yStart) {
        String[] skills = {
            "[1] Frost Bolt (0-10 Dmg, +10 Mana)",
            "[2] Rune Burst (11-20 Dmg, 20 Mana)",
            "[3] Lightstorm (21-35 Dmg, 30 Mana)"
        };
        for (int i = 0; i < skills.length; i++) {
            int centerX = boxStartX + (boxWidth - skills[i].length()) / 2;
            go.move(centerX, yStart + i);
            System.out.print(skills[i]);
        }
    }

    @Override
    public String useSkill(int choice, World1Mob target) {
        int damage;
        String message;
        String critMessage = "";

        switch (choice) {
            case 1:
                damage = getDamageWithBuff((int)(Math.random() * 11));
                mana += 10;
                if (mana > maxMana) mana = maxMana;

                if (random.nextInt(100) < 20) {
                    damage = (int)(damage * 1.5);
                    critMessage = " >> CRITICAL HIT! <<";
                }

                target.takeDamage(damage);
                message = name + " used Frost Bolt!" + critMessage + " Deals " + damage + " damage. ";
                return message;
            case 2:
                if (mana >= 20) {
                    damage = getDamageWithBuff(11 + (int)(Math.random() * 10));
                    mana -= 20;

                    if (random.nextInt(100) < 20) {
                        damage = (int)(damage * 1.5);
                        critMessage = " >> CRITICAL HIT! <<";
                    }

                    target.takeDamage(damage);
                    message = name + " used Rune Burst!" + critMessage + " Deals " + damage + " damage. ";
                    return message;
                } else {
                    return "Not enough mana!";
                }
            case 3:
                if (mana >= 30) {
                    damage = getDamageWithBuff(21 + (int)(Math.random() * 15));
                    mana -= 30;

                    if (random.nextInt(100) < 20) {
                        damage = (int)(damage * 1.5);
                        critMessage = " >> CRITICAL HIT! <<";
                    }

                    target.takeDamage(damage);
                    message = name + " used Lightstorm!" + critMessage + " Deals " + damage + " damage. ";
                    return message;
                } else {
                    return "Not enough mana!";
                }
            default:
                return "Invalid skill choice. Turn skipped.";
        }
    }
}

class Paladin extends Character {
    public Paladin(String playerName) {
        super(playerName, "Paladin", 220, 120);
    }

    @Override
    public void displaySkills(GoToXY go, int boxStartX, int boxWidth, int yStart) {
        String[] skills = {
            "[1] Shield Bash (0-8 Dmg, +10 Mana)",
            "[2] Radiant Guard (Reduces damage taken, 20 Mana)",
            "[3] Holy Renewal (Heal 20-35 HP, 30 Mana)"
        };
        for (int i = 0; i < skills.length; i++) {
            int centerX = boxStartX + (boxWidth - skills[i].length()) / 2;
            go.move(centerX, yStart + i);
            System.out.print(skills[i]);
        }
    }

    @Override
    public String useSkill(int choice, World1Mob target) {
        int damage;
        String message;
        String critMessage = "";
        
        switch (choice) {
            case 1:
                damage = getDamageWithBuff((int)(Math.random() * 9));
                mana += 10;
                if (mana > maxMana) mana = maxMana;

                if (random.nextInt(100) < 20) {
                    damage = (int)(damage * 1.5);
                    critMessage = " >> CRITICAL HIT! <<";
                }

                target.takeDamage(damage);
                message = name + " used Shield Bash!" + critMessage + " Deals " + damage + " damage. ";
                return message;
            case 2:
                if (mana >= 20) {
                    mana -= 20;
                    return name + " used Radiant Guard! Damage taken will be reduced.";
                } else {
                    return "Not enough mana!";
                }
            case 3:
                if (mana >= 30) {
                    int healAmount = 20 + (int)(Math.random() * 16);
                    mana -= 30;
                    this.hp += healAmount;
                    if (this.hp > this.maxHp) this.hp = this.maxHp;
                    return name + " used Holy Renewal! You heal for " + healAmount + " HP. HP: " + this.hp;
                } else {
                    return "Not enough mana!";
                }
            default:
                return "Invalid skill choice. Turn skipped.";
        }
    }
}