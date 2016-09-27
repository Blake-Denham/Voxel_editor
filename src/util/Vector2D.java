package util;

public class Vector2D {
    private double x, y;


    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
