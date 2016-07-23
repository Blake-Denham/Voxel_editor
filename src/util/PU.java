package util;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class PU {

    @NotNull
    @Contract("_, _ -> !null")
    public static Color saturateColor(@NotNull Color c, double percent) {
        return new Color((int) (c.getRed() * percent), (int) (c.getGreen() * percent), (int) (c.getBlue() * percent));
    }

    @NotNull
    @Contract("_ -> !null")
    public static Color inverseColor(@NotNull Color c) {
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }

    public static void castShadow(@NotNull Graphics2D g2d, @NotNull Rectangle r, int size, @NotNull Color c) {
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

    public static void castShadow(@NotNull Graphics2D g2d, @NotNull Ellipse2D r, int size, @NotNull Color c) {
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

    public static float getOptimalFontsize(@NotNull Graphics2D g2d, @NotNull Font f, @NotNull String str, float stringHeight, double areaWidth) {
        float fontSize = stringHeight;
        do {
            fontSize--;
            g2d.setFont(f.deriveFont(fontSize));
        } while (g2d.getFontMetrics(f).stringWidth(str) >= areaWidth);
        return fontSize;
    }

    public static void setTextRenderingQuality(@NotNull Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    @NotNull
    @SuppressWarnings("unused")
    public static Vector2D getVectorInBounds(@NotNull Rectangle bounds, double width, double height, double percentX, double percentY) {
        return new Vector2D(getXInBounds(bounds, width, percentX), getYInBounds(bounds, height, percentY));
    }

    public static double getXInBounds(@NotNull Rectangle bounds, double width, double percentX) {
        double x = bounds.getX();
        double bw = bounds.getWidth();
        return x + (bw - width) * (percentX);
    }

    public static double getYInBounds(@NotNull Rectangle bounds, double height, double percentY) {
        double y = bounds.getY();
        double bh = bounds.getHeight();
        return y + (bh - height) * (percentY);
    }

    public Color getColorRGB(int rgb) {
        System.out.println(Integer.toHexString((rgb << 8) | 0xff));
        return new Color((rgb << 8) | 0xff);
    }

}
