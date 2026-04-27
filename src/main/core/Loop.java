package main.core;

import main.graphics.Renderer;
import main.graphics.Screen;

public class Loop {
    private final Screen screen;
    private final Renderer renderer;

    private static boolean paused = false;

    /**
     * Creates the main game loop. Updates the screen and renderer every tick.
     * @param screen
     * @param renderer
     */
    public Loop(Screen screen, Renderer renderer) {
        this.screen = screen;
        this.renderer = renderer;
    }

    public void tick() {
        if (!paused) {
            screen.update();
        }
        renderer.repaint();
    }

    public static void pause() {
        paused = true;
    }

    public static void resume() {
        paused = false;
    }

    public static boolean isPaused() {
        return paused;
    }


}
