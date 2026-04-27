package main.core;

import javax.swing.Timer;

import main.graphics.Renderer;
import main.graphics.Screen;
import main.graphics.Window;

public class Simulation {
    
    private final Loop loop;

    private static final Screen screen = new Screen();

    private static final Renderer renderer = new Renderer(screen);

    private static final Window window = new Window(renderer);
    
    public Simulation() {
        loop = new Loop(screen, renderer);

        new Timer(16, e -> loop.tick()).start();
    }

    public static int getWidth() {
        return window.getWidth();
    }

    public static int getHeight() {
        return window.getHeight();
    }
}
