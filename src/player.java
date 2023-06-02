public class player {
    int x;
    int y;
    int hp;
    int dmg;
    int speed = 3;

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
