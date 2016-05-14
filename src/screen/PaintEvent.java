package screen;

import java.awt.*;

interface PaintEvent {
    void event(PaintEvent e, int z, int y, int x, Graphics2D g2d);
}
