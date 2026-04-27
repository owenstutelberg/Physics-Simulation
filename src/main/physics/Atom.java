package main.physics;

import main.math.Vec2;
import java.awt.Color;
import java.awt.Graphics;

public class Atom {

    private static final double DAMPING = 0.995;

    private Vec2 position;
    private Vec2 lastPosition;
    private Vec2 acceleration;
    private double radius = 10.0;
    private double pressure;
    private Color color = Color.WHITE;

    public Atom(Vec2 position, double radius, Color color) {
        this.position = new Vec2(position);
        this.lastPosition = new Vec2(position);
        this.acceleration = new Vec2(0, 0);
        this.radius = radius;
        this.pressure = 0;
        this.color = color;
    }

    public double getRadius() {
        return radius;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void addPressure(double amount) {
        this.pressure += amount;
    }

    public Vec2 getVelocity() {
        return new Vec2(position).minus(lastPosition);
    }

    public void setVelocity(Vec2 velocity) {
        lastPosition = new Vec2(position).minus(velocity);
    }

    public void addVelocity(Vec2 deltaVelocity) {
        Vec2 newVelocity = new Vec2(getVelocity()).plus(deltaVelocity);
        setVelocity(newVelocity);
    }

    public void addVelocity(double deltaX, double deltaY) {
        Vec2 newVelocity = new Vec2(getVelocity()).plus(deltaX, deltaY);
        setVelocity(newVelocity);
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public void applyForce(Vec2 force) {
        acceleration.plus(force);
    }

    public void applyForce(double forceX, double forceY) {
        acceleration.plus(forceX, forceY);
    }

    public void update(double dt) {
        Vec2 displacement = new Vec2(position).minus(lastPosition).scale(DAMPING);

        lastPosition = new Vec2(position);

        position = new Vec2(position)
            .plus(displacement)
            .plus(acceleration.scale(dt * dt));

        acceleration = new Vec2();

        pressure *= 0.98;
    }

    public Vec2 getPosition() {
        return position;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(
            (int) (position.getX() - radius),
            (int) (position.getY() - radius),
            (int) (radius * 2),
            (int) (radius * 2)
        );
    }
}