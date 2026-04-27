package main.physics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import main.config.Constants;
import main.math.MathUtils;
import main.math.Vec2;

public class Engine {
    private final int gridCols;
    private final int gridRows;
    private final int width;
    private final int height;
    private final List<Atom>[] grid;
    private final List<Atom> atoms;
    private boolean dragging;
    private double dragX;
    private double dragY;

    public Engine(int width, int height) {
        gridCols = (int) Math.ceil(width / (double) Constants.CELL_SIZE);
        gridRows = (int) Math.ceil(height / (double) Constants.CELL_SIZE);

        this.width = width;
        this.height = height;
        
        grid = (List<Atom>[]) new ArrayList[gridCols * gridRows];
        for (int i = 0; i < grid.length; i++) {
            grid[i] = new ArrayList<>();
        }

        atoms = new ArrayList<>();

        for (int i = 0; i < Constants.NUMBER_OF_ATOMS; i++) {
            Atom atom = new Atom(
                new Vec2(
                    MathUtils.randomInt(0, width),
                    MathUtils.randomInt(0, height)
                ),
                MathUtils.randomInt(Constants.MIN_BALL_RADIUS, Constants.MAX_BALL_RADIUS),
                MathUtils.randomColor()
            );

            atom.setVelocity(new Vec2(
                MathUtils.randomInt(-3, 3),
                MathUtils.randomInt(-3, 3)
            ));

            atoms.add(atom);
        }
    }

    public void update() {
        double dt = 0.016;
        buildSpatialGrid();

        if (dragging) {
            applyMouseRepulsion(dragX, dragY);
        }

        if (Constants.USE_CENTER_ATTRACTION) {
            applyCenterAttraction();
        }
        // applyMutualAttraction();

        for (Atom atom : atoms) {
            atom.update(dt);
            if (Constants.BORDERS) {
                handleBoundaryCollision(atom);
            }
        }

        handleCollisions();
    }

    public void handleMousePressed(double x, double y) {
        dragging = true;
        dragX = x;
        dragY = y;
    }

    public void handleMouseDragged(double x, double y) {
        dragX = x;
        dragY = y;
    }

    public void handleMouseReleased(MouseEvent e) {
        dragging = false;
        applyMouseRepulsion(e.getX(), e.getY());
    }

    public void draw(Graphics g) {
        for (Atom atom : atoms) {
            atom.draw(g);
        }
        if (Constants.DRAW_HEATMAP) drawHeatmap(g);
        if (Constants.DRAW_GRID) drawGrid(g);
    }

    private void drawHeatmap(Graphics g) {
        for (int y = 0; y < gridRows; y++) {
            for (int x = 0; x < gridCols; x++) {
                List<Atom> cell = getCell(x, y);
                if (cell.isEmpty()) continue;

                double totalPressure = 0;
                for (Atom atom : cell) {
                    totalPressure += atom.getPressure();
                }
                double avgPressure = totalPressure / cell.size();

                double normalized = Math.min(avgPressure / 10.0, 1.0);

                int red = (int) (normalized * 255);
                int blue = (int) ((1 - normalized) * 255);
                Color color = new Color(red, 0, blue, 100);

                g.setColor(color);
                g.fillRect(x * Constants.CELL_SIZE, y * Constants.CELL_SIZE,
                           Constants.CELL_SIZE, Constants.CELL_SIZE);
            }
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(new Color(200, 200, 200)); // Light gray
        int cellSize = Constants.CELL_SIZE;
        
        // Draw vertical lines
        for (int x = 0; x <= gridCols; x++) {
            g.drawLine(x * cellSize, 0, x * cellSize, gridRows * cellSize);
        }
        
        // Draw horizontal lines
        for (int y = 0; y <= gridRows; y++) {
            g.drawLine(0, y * cellSize, gridCols * cellSize, y * cellSize);
        }
    }

    private void handleBoundaryCollision(Atom atom) {
        double radius = atom.getRadius();
        Vec2 position = atom.getPosition();
        Vec2 velocity = atom.getVelocity();

        boolean bounced = false;

        if (position.getX() - radius < 0) {
            position = new Vec2(radius, position.getY());
            velocity = new Vec2(-velocity.getX(), velocity.getY());
            bounced = true;
        } else if (position.getX() + radius > width) {
            position = new Vec2(width - radius, position.getY());
            velocity = new Vec2(-velocity.getX(), velocity.getY());
            bounced = true;
        }

        if (position.getY() - radius < 0) {
            position = new Vec2(position.getX(), radius);
            velocity = new Vec2(velocity.getX(), -velocity.getY());
            bounced = true;
        } else if (position.getY() + radius > height) {
            position = new Vec2(position.getX(), height - radius);
            velocity = new Vec2(velocity.getX(), -velocity.getY());
            bounced = true;
        }

        if (bounced) {
            atom.setPosition(position);
            atom.setVelocity(velocity);
        }
    }

    private void handleCollisions() {
        for (int y = 0; y < gridRows; y++) {
            for (int x = 0; x < gridCols; x++) {
                List<Atom> cell = getCell(x, y);

                for (int i = 0; i < cell.size(); i++) {
                    Atom a = cell.get(i);

                    for (int j = i + 1; j < cell.size(); j++) {
                        resolveCollision(a, cell.get(j));
                    }

                    for (int[] offset : Constants.NEIGHBORS) {
                        int neighborX = x + offset[0];
                        int neighborY = y + offset[1];
                        if (!isValidCell(neighborX, neighborY)) {
                            continue;
                        }

                        List<Atom> neighborCell = getCell(neighborX, neighborY);
                        for (Atom b : neighborCell) {
                            resolveCollision(a, b);
                        }
                    }
                }
            }
        }
    }

    private void buildSpatialGrid() {
        for (List<Atom> cell : grid) {
            cell.clear();
        }

        for (Atom atom : atoms) {
            int cellX = clamp((int) (atom.getPosition().getX() / Constants.CELL_SIZE), 0, gridCols - 1);
            int cellY = clamp((int) (atom.getPosition().getY() / Constants.CELL_SIZE), 0, gridRows - 1);
            getCell(cellX, cellY).add(atom);
        }
    }

    private void applyCenterAttraction() {
        double centerX = width / 2.0;
        double centerY = height / 2.0;
        double strength = Constants.CENTER_ATTRACTION_STRENGTH;

        for (Atom atom : atoms) {
            Vec2 position = atom.getPosition();
            double dx = centerX - position.getX();
            double dy = centerY - position.getY();
            atom.applyForce(dx * strength, dy * strength);
        }
    }

    private void applyMutualAttraction() {
        double radiusSq = Constants.ATTRACTION_RADIUS * Constants.ATTRACTION_RADIUS;

        for (int y = 0; y < gridRows; y++) {
            for (int x = 0; x < gridCols; x++) {
                List<Atom> cell = getCell(x, y);

                for (int i = 0; i < cell.size(); i++) {
                    Atom a = cell.get(i);
                    Vec2 aPos = a.getPosition();

                    for (int j = i + 1; j < cell.size(); j++) {
                        applyAttraction(a, cell.get(j), aPos, radiusSq);
                    }

                    for (int[] offset : Constants.NEIGHBORS) {
                        int neighborX = x + offset[0];
                        int neighborY = y + offset[1];
                        if (!isValidCell(neighborX, neighborY)) {
                            continue;
                        }

                        List<Atom> neighborCell = getCell(neighborX, neighborY);
                        for (Atom b : neighborCell) {
                            applyAttraction(a, b, aPos, radiusSq);
                        }
                    }
                }
            }
        }
    }

    private void applyAttraction(Atom a, Atom b, Vec2 aPos, double radiusSq) {
        Vec2 bPos = b.getPosition();
        double dx = bPos.getX() - aPos.getX();
        double dy = bPos.getY() - aPos.getY();
        double distanceSq = dx * dx + dy * dy;
        if (distanceSq <= 0 || distanceSq >= radiusSq) {
            return;
        }

        double distance = Math.sqrt(distanceSq);
        double normalized = Constants.ATTRACTION_STRENGTH * (1.0 - distance / Constants.ATTRACTION_RADIUS);
        double forceX = dx / distance * normalized;
        double forceY = dy / distance * normalized;

        a.applyForce(forceX, forceY);
        b.applyForce(-forceX, -forceY);
    }

    private List<Atom> getCell(int x, int y) {
        return grid[y * gridCols + x];
    }

    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < gridCols && y >= 0 && y < gridRows;
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private void resolveCollision(Atom a, Atom b) {
        Vec2 aPos = a.getPosition();
        Vec2 bPos = b.getPosition();

        double dx = bPos.getX() - aPos.getX();
        double dy = bPos.getY() - aPos.getY();
        double minDistance = a.getRadius() + b.getRadius();
        double distanceSq = dx * dx + dy * dy;

        if (distanceSq <= 0 || distanceSq >= minDistance * minDistance) {
            return;
        }

        double distance = Math.sqrt(distanceSq);
        double nx = dx / distance;
        double ny = dy / distance;

        Vec2 velocityA = a.getVelocity();
        Vec2 velocityB = b.getVelocity();
        double relativeVelocity = (velocityB.getX() * nx + velocityB.getY() * ny)
            - (velocityA.getX() * nx + velocityA.getY() * ny);

        if (relativeVelocity >= 0) {
            return;
        }

        double overlap = minDistance - distance;
        double delta = Constants.COLLISION_RESPONSE * 0.5 * overlap;

        a.setPosition(new Vec2(aPos.getX() - nx * delta, aPos.getY() - ny * delta));
        b.setPosition(new Vec2(bPos.getX() + nx * delta, bPos.getY() + ny * delta));

        a.addPressure(delta);
        b.addPressure(delta);

        double adjustX = (velocityA.getX() - velocityB.getX()) * Constants.VELOCITY_RESPONSE;
        double adjustY = (velocityA.getY() - velocityB.getY()) * Constants.VELOCITY_RESPONSE;

        a.addVelocity(-adjustX, -adjustY);
        b.addVelocity(adjustX, adjustY);
    }

    private void applyMouseRepulsion(double centerX, double centerY) {
        for (Atom atom : atoms) {
            Vec2 position = atom.getPosition();
            double dx = position.getX() - centerX;
            double dy = position.getY() - centerY;
            double distanceSq = dx * dx + dy * dy;
            if (distanceSq <= 0 || distanceSq >= Constants.REPULSION_RADIUS * Constants.REPULSION_RADIUS) {
                continue;
            }

            double distance = Math.sqrt(distanceSq);
            double forceMagnitude = Constants.REPULSION_STRENGTH * (1.0 - distance / Constants.REPULSION_RADIUS);
            atom.applyForce(dx / distance * forceMagnitude, dy / distance * forceMagnitude);
        }
    }
}
