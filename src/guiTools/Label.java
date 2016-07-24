package guiTools;

import editorScreen.EditorScreen;
import org.jetbrains.annotations.NotNull;
import util.MU;
import util.PU;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Blake on 5/13/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Label extends GuiComponent {
    @NotNull
    private String display="";
    private char[] displayChar;


    public Label(double x, double y, double width) {
        super(x, y, width, EditorScreen.s_maxHeight* MU.getPercent(25,1080));
        displayChar = display.toCharArray();
        bgColor = new Color(255, 255, 255);
    }

    @Override
    protected void paintGuiComponent(@NotNull Graphics2D g2d) {
        if (!(width == 0)) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        PU.castShadow(g2d, bounds, 6, bgColor);
        g2d.setColor(Color.black);
        g2d.setFont(EditorScreen.font.deriveFont(PU.getOptimalFontsize(g2d, EditorScreen.font, display, (float) (height-4.6), width)));

        double displayWidth =0;
        display = "";

            for (char aDisplayChar : displayChar) {

                displayWidth += g2d.getFontMetrics(EditorScreen.font.deriveFont(PU.getOptimalFontsize(g2d, EditorScreen.font, display, (float) (height - 4.6), width))).stringWidth(aDisplayChar + "");

                if ((displayWidth < width)) {
                    display += aDisplayChar + "";

                }
            }
            PU.setTextRenderingQuality(g2d);
            g2d.drawString(display, (int) (x + 3), (int) (y + height - (height - 4.6) / 4.0));
        }
    }

    @Override
    protected void update() {

    }

    @Override
    protected void hover(MouseEvent e) {

    }

    @Override
    protected void drag(MouseEvent e) {

    }

    @Override
    protected void scroll(MouseWheelEvent mwe) {

    }

    @Override
    protected void keyPress(KeyEvent e) {

    }

    @Override
    protected void mousePress(MouseEvent e) {

    }

    @Override
    protected void mouseRelease(MouseEvent e) {

    }

    public void setDisplay(@NotNull String display) {
        displayChar = display.toCharArray();
    }

}
