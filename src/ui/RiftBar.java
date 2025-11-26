package ui;

import javax.swing.*;
import java.awt.*;

public class RiftBar extends JPanel {

    private int maxVal;
    private int currentVal;
    private Color barColor;

    public RiftBar(int max, Color color) {
        this.maxVal = max;
        this.currentVal = max; // Start full
        this.barColor = color;

        setOpaque(false);
        setPreferredSize(new Dimension(200, 20)); // Default size
    }

    // Call this method whenever damage/healing happens
    public void updateValue(int current) {
        this.currentVal = current;
        // repaint() triggers the paintComponent() method again to redraw the graphics
        repaint();
    }

    // Update the max limit (e.g., upon leveling up)
    public void setMax(int max) {
        this.maxVal = max;
    }

     //It runs every time the screen refreshes or repaint() is called.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Prevent dvision by zero errors
        if (maxVal <= 0) maxVal = 1;

        double percent = (double) currentVal / maxVal;
        if (percent < 0) percent = 0; // Prevent negative bars

        int fillWidth = (int) (w * percent);

        g2.setColor(new Color(50, 50, 50));

        int[] xPointsBg = {0, w, w - 15, -15};
        int[] yPointsBg = {0, 0, h, h};

        g2.fillPolygon(xPointsBg, yPointsBg, 4);

        g2.setColor(barColor);

        if (fillWidth > 0) {
            // width is limited by 'fillWidth'
            int[] xPointsFill = {0, fillWidth, fillWidth - 15, -15};
            int[] yPointsFill = {0, 0, h, h};

            g2.fillPolygon(xPointsFill, yPointsFill, 4);
        }

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2)); // Thickness of 2 pixels

        // Re-using the background coordinates to draw the frame
        g2.drawPolygon(xPointsBg, yPointsBg, 4);
    }
}