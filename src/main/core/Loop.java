package main.core;

import main.graphics.Renderer;
import main.graphics.Screen;

public class Loop {
    private final Screen screen;
    private final Renderer renderer;

    public Loop(Screen screen, Renderer renderer) {
        this.screen = screen;
        this.renderer = renderer;
    }

    public void tick() {
        screen.update();
        renderer.repaint();
    }
}
