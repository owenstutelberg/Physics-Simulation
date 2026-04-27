package main.graphics;

import javax.swing.JFrame;

public class Window extends JFrame {
    public Window(Renderer renderer) {
        setTitle("Physics Simulation");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        add(renderer);
        // pack();
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
    }
}
