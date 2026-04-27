package main.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

import main.config.Constants;

public class Renderer extends JPanel {
    private final Screen screen;

    public Renderer(Screen screen) {
        this.screen = screen;

        // setPreferredSize(Constants.SCREEN_DIMENSION);
        // setPreferredSize();
        setBackground(Constants.BACKGROUND_COLOR);
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                screen.handleKeyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                screen.handleKeyReleased(e);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                screen.handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                screen.handleMouseReleased(e);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                screen.handleMouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                screen.handleMouseMoved(e);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (Constants.ANTI_ALIASING) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
        }
        screen.draw(g);
    }
}
