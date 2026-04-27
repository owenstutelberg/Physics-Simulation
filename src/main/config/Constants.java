package main.config;

import java.awt.Color;
import java.awt.Dimension;

import main.math.Vec2;

public class Constants {
    // Screen settings
    public static final Dimension SCREEN_DIMENSION = new Dimension(1000, 600);
    public static final Vec2 SCREEN_CENTER = new Vec2(500, 300);
    public static final Color BACKGROUND_COLOR = new Color(0, 0, 0);
    public static final boolean ANTI_ALIASING = true;

    // Sim settings
    public static final boolean USE_CENTER_ATTRACTION = true;
    public static final double CENTER_ATTRACTION_STRENGTH = 0.5;
    public static final boolean BORDERS = true;
    public static final boolean DRAW_HEATMAP = false;
    public static final boolean DRAW_GRID = true;

    public static final int MIN_BALL_RADIUS = 2;
    public static final int MAX_BALL_RADIUS = 4;

    public static final int NUMBER_OF_ATOMS = 2500;

    // Engine settings
    public static final double COLLISION_RESPONSE = 0.8;
    public static final double VELOCITY_RESPONSE = 0.0025;

    public static final int CELL_SIZE = MAX_BALL_RADIUS * 10;
    public static final int[][] NEIGHBORS = {{1, 0}, {0, 1}, {1, 1}, {-1, 1}};
    
    public static final double ATTRACTION_RADIUS = 1000;
    public static final double ATTRACTION_STRENGTH = 1.5;
    public static final double REPULSION_RADIUS = 150;
    public static final double REPULSION_STRENGTH = 5000;
}
