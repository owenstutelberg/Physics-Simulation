package main.math;

public class Vec2 {
    private double x;
    private double y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(Vec2 v) {
        this.x = v.getX();
        this.y = v.getY();
    }

    public Vec2() {
        this.x = 0;
        this.y = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vec2 plus(Vec2 v) {
        x += v.getX();
        y += v.getY();
        return this;
    }

    public Vec2 plus(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vec2 minus(Vec2 v) {
        x -= v.getX();
        y -= v.getY();
        return this;
    }

    public Vec2 minus(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vec2 scale(double scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public double distanceTo(Vec2 other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    public double dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    public Vec2 normalize() {
        double mag = magnitude();
        return mag == 0 ? new Vec2() : new Vec2(x / mag, y / mag);
    }
}
