package studentgradetracker;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private Color bg;
    private int radius;

    public RoundedPanel(Color bg, int radius) {
        this.bg = bg;
        this.radius = radius;
        setOpaque(false);
    }

    public RoundedPanel(Color bg) { this(bg, 16); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}
