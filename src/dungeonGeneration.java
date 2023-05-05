import java.util.ArrayList;
import java.util.Random;

public class dungeonGeneration {
    int X, Y, STEPS;
    ArrayList<Vector2> MAP = new ArrayList<>();
    public dungeonGeneration(int steps) {
        this.STEPS = steps;
    }

    public void createDungeon() {
        X = Y = 0;
        MAP.add(new Vector2(X, Y));
        for (int i = 0; i < STEPS; i++) {
            Random random_ = new Random();
            switch (random_.nextInt(4)) {
                case 0: //UP
                    if(Y > 0)
                    {
                        Y--;
                    }
                    break;
                case 1: //RIGHT
                    X++;
                    break;
                case 2: //DOWN
                    Y++;
                    break;
                case 3: //LEFT
                    if(X > 0)
                    {
                        X--;
                    }
                    break;
            }
            Vector2 v = new Vector2(X, Y);
            if(!MAP.contains(v)) {
                MAP.add(v);
            }
        }
    }

    public ArrayList<Vector2> getDungeon() {
        return MAP;
    }

    public void setProperties(int steps) {
        this.STEPS = steps;
    }

    public static void main(String[] args) {
        dungeonGeneration d = new dungeonGeneration(60);
        d.createDungeon();

        for (int y = 0; y < 10; y++) {
            String s = "";
            for (int x = 0; x < 20; x++) {
                if(d.getDungeon().contains(new Vector2(x, y))) {
                    s += ".";
                }
                else {
                    s += "#";
                }
            }
            System.out.println(s);
        }
    }
}
