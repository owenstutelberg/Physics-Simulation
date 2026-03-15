package main.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import main.config.Constants;

public class Renderer extends JPanel {
    private final Screen screen;

    public Renderer(Screen screen) {
        this.screen = screen;

        setPreferredSize(Constants.SCREEN_DIMENSION);
        setBackground(Constants.BACKGROUND_COLOR);
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
