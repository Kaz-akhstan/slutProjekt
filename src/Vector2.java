import java.util.Objects;

public class Vector2 {
    int x, y;
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return x == vector2.x && y == vector2.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "x: " + x + " y; " + y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
