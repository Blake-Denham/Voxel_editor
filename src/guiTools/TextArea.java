package guiTools;

import editorScreen.EditorScreen;
import org.jetbrains.annotations.NotNull;
import util.MU;
import util.PU;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public class TextArea extends GuiComponent {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private boolean typing = false;
    @NotNull
    private String display = "", displayTemp = "";
    private char[] displayChar;
    private String allowedCharacters;

    public TextArea(double x_, double y_, double width, String allowedCharacters) {
        super(x_, y_, width, EditorScreen.s_maxHeight * MU.getPercent(25, 1080));
        this.allowedCharacters = allowedCharacters;
        displayChar = display.toCharArray();
        displayTemp = display;
    }

    @Override
    protected void paintGuiComponent(@NotNull Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        PU.castShadow(g2d, bounds, 8, Color.white);
        g2d.setColor(Color.black);
        g2d.setFont(EditorScreen.font.deriveFont(PU.getOptimalFontsize(g2d, EditorScreen.font, display, (float) height, width)));
        displayChar = displayTemp.toCharArray();
        double displayWidth = 0;
        display = "";
        for (char aDisplayChar : displayChar) {
            displayWidth += g2d.getFontMetrics(EditorScreen.font.deriveFont(PU.getOptimalFontsize(g2d, EditorScreen.font, display, (float) height, width))).stringWidth(aDisplayChar + "");

            if ((displayWidth < width)) {
                display += aDisplayChar + "";

            }
        }
        PU.setTextRenderingQuality(g2d);
        g2d.drawString(display, (int) (x), (int) (y + height - height / 8));

    }

    public void setDisplay(@NotNull String display) {
        displayTemp = display;
    }

    @Override
    protected void update() {

    }

    @Override
    public void hover(MouseEvent e) {

    }

    @Override
    public void drag(MouseEvent e) {

    }

    @Override
    public void scroll(MouseWheelEvent mwe) {

    }

    @Override
    public void keyPress(KeyEvent e) {
        if (typing) {
            if (allowedCharacters.contains(e.getKeyChar() + "") && displayTemp.length() < 7) {
                displayTemp += e.getKeyChar();
            }
            if (displayTemp.length() > 1 && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                displayTemp.trim();
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                typing = false;
            }
        }
    }

    @Override
    public void mousePress(@NotNull MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if (bounds.contains(mx, my)) {
            typing = true;
            displayTemp = "#";
        } else {
            typing = false;
        }
    }

    @Override
    public void mouseRelease(MouseEvent e) {

    }

    public boolean isTyping() {
        return typing;
    }

    public @NotNull String getDisplay() {
        return display;
    }
}
