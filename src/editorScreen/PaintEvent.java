package editorScreen;

import java.awt.*;

interface PaintEvent {
    void event(PaintEvent e, int z, int y, Graphics2D g2d);
}
