package org.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

class Zoom extends JPanel {
    private double zoomFactor = 1.0;
    private BufferedImage image;

    public Zoom(BufferedImage img) {
        this.image = img;
        setupListeners();
    }

    private void setupListeners() {
        this.addMouseWheelListener(e -> {
            double delta = 0.05f * e.getPreciseWheelRotation();
            zoomFactor += delta;
            repaint();
        });

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Implement click to zoom logic
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(zoomFactor, zoomFactor);
        // Draw your image or other content here
        g2.drawImage(image, 0, 0, this);
    }
}
