package main.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.core.Loop;
import main.physics.Engine;

public class Screen {
    private final Engine engine;

    public Screen() {
        engine = new Engine(getWidth(), getHeight());
    }

    public void update() {
        engine.update();
    }

    public void handleMousePressed(MouseEvent e) {
        engine.handleMousePressed(e.getX(), e.getY());
    }

    public void handleMouseDragged(MouseEvent e) {
        engine.handleMouseDragged(e.getX(), e.getY());
    }

    public void handleMouseReleased(MouseEvent e) {
        engine.handleMouseReleased(e);
    }

    public void handleMouseMoved(MouseEvent e) {}
    
    public void handleKeyPressed(KeyEvent e) {}

    public void handleKeyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (Loop.isPaused()) {
                Loop.resume();
            } else {
                Loop.pause();
            }
        }
    }

    public void draw(Graphics g) {
        engine.draw(g);
        g.setColor(Color.WHITE);
        if (Loop.isPaused()) {
            g.fillRect(5, 5, 10, 32);
            g.fillRect(20, 5, 10, 32);
        } else {
            g.fillPolygon(new int[] {5, 37, 5}, new int[] {5, 21, 37}, 3);
        }
    }

    public static int getWidth() {
        return Toolkit.getDefaultToolkit().getScreenSize().width;
    }

    public static int getHeight() {
        return Toolkit.getDefaultToolkit().getScreenSize().height;
    }
}
