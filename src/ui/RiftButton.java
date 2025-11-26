package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

public class RiftButton extends JButton {
    private Color normalColor = new Color(30, 30, 30);
    private Color hoverColor = new Color(50, 50, 50);
    private Color borderColor = new Color(0, 255, 255); // Cyan glow
    private boolean isHovered = false;

    public RiftButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int slant = height / 3; // Adjust slant amount

        // Create the slanted shape
        Path2D.Float shape = new Path2D.Float();
        shape.moveTo(slant, 0);
        shape.lineTo(width, 0);
        shape.lineTo(width - slant, height);
        shape.lineTo(0, height);
        shape.closePath();

        // Fill background
        if (isHovered) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fill(shape);

        // Draw border
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2f));
        g2.draw(shape);

        // Draw text centered
        FontMetrics fm = g2.getFontMetrics();
        int textX = (width - fm.stringWidth(getText())) / 2;
        int textY = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2.setColor(getForeground());
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(180, 40); // Default size
    }
}