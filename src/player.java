public class player {
    int x;
    int y;
    int hp;
    int dmg;

    int speed = 5;
    int cooldown = 25;
    int attackTimer = 0;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getAttackTimer() {
        return attackTimer;
    }

    public void setAttackTimer(int attackTimer) {
        this.attackTimer = attackTimer;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }
    public player(int x, int y, int hp, int dmg) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.dmg = dmg;
    }
}
