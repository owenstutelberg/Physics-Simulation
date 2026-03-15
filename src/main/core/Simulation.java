package main.core;

import javax.swing.Timer;

import main.graphics.Renderer;
import main.graphics.Screen;
import main.graphics.Window;

public class Simulation {
    private final Loop loop;
    
    public Simulation() {
        Screen screen = new Screen();
        Renderer renderer = new Renderer(screen);

        new Window(renderer);

        loop = new Loop(screen, renderer);

        new Timer(16, e -> loop.tick()).start();
    }
}
