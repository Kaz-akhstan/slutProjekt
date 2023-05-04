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
            switch (random_.nextInt(5)) {
                case 0: //UP
                    Y--;
                    break;
                case 1: //RIGHT
                    X++;
                    break;
                case 2: //DOWN
                    Y++;
                    break;
                case 3: //LEFT
                    X--;
                    break;
            }
            if(!MAP.contains(new Vector2(X, Y))) {
                MAP.add(new Vector2(X, Y));
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
        dungeonGeneration d = new dungeonGeneration(20);
        d.createDungeon();
    }
}
