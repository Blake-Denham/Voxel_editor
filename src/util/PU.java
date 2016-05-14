package util;


import org.jetbrains.annotations.Contract;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class PU {

    @Contract("_, _ -> !null")
    public static Color saturateColor(Color c, double percent) {
        return new Color((int) (c.getRed() * percent), (int) (c.getGreen() * percent), (int) (c.getBlue() * percent));
    }

    @Contract("_ -> !null")
    public static Color inverseColor(Color c) {
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }

    public static void castShadow(Graphics2D g2d, Rectangle r, int size, Color c) {
        double x = r.getX();
        double y = r.getY();
        double width = r.getWidth();
        double height = r.getHeight();
        double dxy = 0;
        if (size != 0) {
            if (size > 0) {
                double interval = 1.0 / size;
                for (float i = 1f; i >= 0; i -= interval) {
                    g2d.setColor(new Color(0, 0, 0, i / 3f));
                    g2d.fillRect((int) (x + dxy), (int) (y + dxy), (int) width, (int) height);
                    dxy++;
                }
            }
            if (size < 0) {
                for (int i = 0; i < -size; i++) {
                    g2d.setColor(new Color(255 + i * (c.getRed() - 255) / (-size), 255 + i * (c.getGreen() - 255) / (-size), 255 + i * (c.getBlue() - 255) / (-size)));
                    g2d.drawRect((int) (x + dxy + 1), (int) (y + dxy + 1), (int) width, (int) height);
                    dxy++;
                }
            }
        }
        g2d.setColor(c);
        g2d.fill(r);
    }

    public static void castShadow(Graphics2D g2d, Ellipse2D r, int size, Color c) {
        double x = r.getX();
        double y = r.getY();
        double width = r.getWidth();
        double height = r.getHeight();
        double dxy = 0;
        if (size != 0) {
            if (size > 0) {
                double interval = 1.0 / size;
                for (float i = 1f; i >= 0; i -= interval) {
                    g2d.setColor(new Color(0, 0, 0, i / 3f));
                    g2d.fillOval((int) (x + dxy + 1), (int) (y + dxy + 1), (int) width, (int) height);
                    dxy++;
                }
            }
            if (size < 0) {
                for (int i = 0; i < -2 * size; i++) {
                    g2d.setColor(new Color(255 + (i / 2) * (c.getRed() - 255) / (-size), 255 + (i / 2) * (c.getGreen() - 255) / (-size), 255 + (i / 2) * (c.getBlue() - 255) / (-size)));
                    g2d.drawOval((int) (x + dxy + 1), (int) (y + dxy + 1), (int) width, (int) height);
                    dxy += 0.5;
                }
            }
        }
        g2d.setColor(c);
        g2d.fill(r);
    }

    public static float getOptimalFontsize(Graphics2D g2d, Font f, String str, float stringHeight, double areaWidth) {
        float fontSize = stringHeight;
        do {
            fontSize--;
            g2d.setFont(f.deriveFont(fontSize));
        } while (g2d.getFontMetrics(f).stringWidth(str) >= areaWidth);
        return fontSize;
    }

    public static void setTextRenderingQuality(Graphics2D g2d, boolean flag) {
        if (flag) {
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        } else {
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
    }

    @Contract("_, _, _, _, _ -> !null")
    public static Vector2D getVecotrInBounds(Rectangle bounds, double width, double height, double percentX, double percentY) {
        return new Vector2D(getXInBounds(bounds, width, percentX), getYInBounds(bounds, height, percentY));
    }

    public static double getXInBounds(Rectangle bounds, double width, double percentX) {
        double x = bounds.getX();
        double bw = bounds.getWidth();
        return x + (bw - width) * (percentX);
    }

    public static double getYInBounds(Rectangle bounds, double height, double percentY) {
        double y = bounds.getY();
        double bh = bounds.getHeight();
        return y + (bh - height) * (percentY);
    }
}
