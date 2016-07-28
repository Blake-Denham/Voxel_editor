package editorScreen;

import guiTools.GuiComponent;
import guiTools.Slider;
import guiTools.TextArea;
import org.jetbrains.annotations.NotNull;
import util.MU;
import util.PU;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Ellipse2D;


public class ColorWheel extends GuiComponent {
    private double r;
    private double xc;
    private double yc;
    private int mb;
    @NotNull
    private final Slider saturation;
    @NotNull
    private final TextArea ta;
    @NotNull
    private final Ellipse2D colorWheel;
    private Color selectedC1, selectedC2;
    @NotNull
    private final Rectangle pt;
    @NotNull
    private final Rectangle displayC1, displayC2;
    @NotNull
    private final Rectangle selected;
    private Rectangle[][] colorButtons;
    private Color[][] colors;
    private Rectangle hover, section;

    public ColorWheel(double x_, double y_, double width, Color bgColor) {
        super(x_, y_, width, width * 1.5, bgColor, 14, true);
        hover = new Rectangle(0, 0, 16, 16);
        colorButtons = new Rectangle[16][2];
        colors = new Color[16][2];
        section = new Rectangle((int) x, (int) y, (int) width - 32 - 6, (int) height);
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 2; j++) {
                colorButtons[i][j] = new Rectangle((int) (x + 2 + j * 8), (int) (y + 2 + i * 2), 8, 8);
            }
        }
        colors[0][0] = new Color(0);
        colors[1][0] = new Color(0x404040);
        colors[2][0] = new Color(0xff0000);
        colors[3][0] = new Color(0xff6a00);
        colors[4][0] = new Color(0xffd800);
        colors[5][0] = new Color(0xb6ff00);
        colors[6][0] = new Color(0x4cff00);
        colors[7][0] = new Color(0x00ff21);
        colors[8][0] = new Color(0x00ff90);
        colors[9][0] = new Color(0x00ffff);
        colors[10][0] = new Color(0x0094ff);
        colors[11][0] = new Color(0x0026ff);
        colors[12][0] = new Color(0x4800ff);
        colors[13][0] = new Color(0xb200ff);
        colors[14][0] = new Color(0xff00dc);
        colors[15][0] = new Color(0xff006e);
        colors[0][1] = new Color(0xffffff);
        colors[1][1] = new Color(0x808080);
        colors[2][1] = PU.saturateColor(new Color(0xff0000), 0.5);
        colors[3][1] = PU.saturateColor(new Color(0xff6a00), 0.5);
        colors[4][1] = PU.saturateColor(new Color(0xffd800), 0.5);
        colors[5][1] = PU.saturateColor(new Color(0xb6ff00), 0.5);
        colors[6][1] = PU.saturateColor(new Color(0x4cff00), 0.5);
        colors[7][1] = PU.saturateColor(new Color(0x00ff21), 0.5);
        colors[8][1] = PU.saturateColor(new Color(0x00ff90), 0.5);
        colors[9][1] = PU.saturateColor(new Color(0x00ffff), 0.5);
        colors[10][1] = PU.saturateColor(new Color(0x0094ff), 0.5);
        colors[11][1] = PU.saturateColor(new Color(0x0026ff), 0.5);
        colors[12][1] = PU.saturateColor(new Color(0x4800ff), 0.5);
        colors[13][1] = PU.saturateColor(new Color(0xb200ff), 0.5);
        colors[14][1] = PU.saturateColor(new Color(0xff00dc), 0.5);
        colors[15][1] = PU.saturateColor(new Color(0xff006e), 0.5);

        r = width * 0.4;
        colorWheel = new Ellipse2D.Double(PU.getXInBounds(section, r * 2, 0.5), PU.getYInBounds(section, r * 2, MU.getPercent(30, section.getHeight())), r * 2, r * 2);
        xc = colorWheel.getCenterX() - 32 + 3;
        yc = colorWheel.getCenterY();
        saturation = new Slider(PU.getXInBounds(bounds, width * 0.7, 0.5), PU.getYInBounds(bounds, height * 0.07, 0.7), Slider.HORIZONTAL, width * 0.7, height * 0.07, new Color(130, 130, 130), new Color(20, 20, 20));

        ta = new TextArea(PU.getXInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080) * MU.getPercent(120, 25), 0.5), PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.81), EditorScreen.s_maxHeight * MU.getPercent(25, 1080) * MU.getPercent(120, 25));
        selected = new Rectangle((int) (xc - 1.5), (int) (yc - 1.5), 3, 3);
        selectedC1 = new Color(255, 255, 255);
        selectedC2 = new Color(0, 0, 0);
        displayC1 = new Rectangle((int) PU.getXInBounds(bounds, width * 0.85 / 2, 0.25), (int) PU.getYInBounds(bounds, height * 0.1, 0.96), (int) (width * 0.85 / 2), (int) (height * 0.09));
        displayC2 = new Rectangle((int) PU.getXInBounds(bounds, width * 0.85 / 2, 0.25), (int) PU.getYInBounds(bounds, height * 0.1, 0.96), (int) (width * 0.85 / 2), (int) (height * 0.09));
        pt = new Rectangle(0, 0, 1, 1);
        add(ta);
        add(saturation);
        setToolBarTitle("COLOUR PICKER");
    }

    @Override
    public void update() {
        setBounds(EditorScreen.s_maxWidth * (1 - MU.getPercent(305 + 58, 1920)) - 16, 36, EditorScreen.s_maxWidth * MU.getPercent(300 + 58, 1920), EditorScreen.s_maxWidth * MU.getPercent(300, 1920) * 1.4);
        section.setBounds((int) x, (int) y, (int) width - 32 - 6, (int) height);
        r = section.getWidth() * 0.4;
        colorWheel.setFrame(PU.getXInBounds(section, r * 2, 0.5), PU.getYInBounds(section, r * 2, MU.getPercent(30, section.getHeight())), r * 2, r * 2);
        xc = colorWheel.getCenterX();
        yc = colorWheel.getCenterY();
        displayC1.setBounds((int) PU.getXInBounds(section, section.getWidth() * 0.85 / 3, 0.3), (int) PU.getYInBounds(section, section.getHeight() * 0.1, 0.96), (int) (section.getWidth() * 0.85 / 3), (int) (section.getHeight() * 0.09));
        displayC2.setBounds((int) PU.getXInBounds(section, section.getWidth() * 0.85 / 3, 0.7), (int) PU.getYInBounds(section, section.getHeight() * 0.1, 0.96), (int) (section.getWidth() * 0.85 / 3), (int) (section.getHeight() * 0.09));
        ta.setBounds(PU.getXInBounds(section, EditorScreen.s_maxHeight * MU.getPercent(25, 1080) * MU.getPercent(120, 25), 0.5), PU.getYInBounds(section, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.81), EditorScreen.s_maxHeight * MU.getPercent(25, 1080) * MU.getPercent(120, 25), EditorScreen.s_maxHeight * MU.getPercent(25, 1080));
        saturation.setBounds(PU.getXInBounds(section, section.getWidth() * 0.7, 0.5), PU.getYInBounds(section, section.getHeight() * 0.07, 0.7), section.getWidth() * 0.7, section.getHeight() * 0.07);
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 2; j++) {
                colorButtons[i][j].setBounds((int) (section.getX() + section.getWidth() - 5 + j * 16 + 2), (int) (y + i * 16 + 10), 16, 16);
            }
        }
    }

    @Override
    public void hover(MouseEvent e) {
        int count = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 2; j++) {
                if (colorButtons[i][j].contains(e.getX(), e.getY())) {
                    hover.setBounds(colorButtons[i][j]);
                    count = 0;
                }
                count++;
            }
        }
        if (count == 16 * 2) {
            hover.setBounds(0, 0, 0, 0);
        }
    }

    @Override
    public void drag(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        int dxpix = e.getXOnScreen() - mx;
        int dypix = e.getYOnScreen() - my;
        if (mb == 1) {
            pt.setLocation(mx, my);
            if (pt.intersects(bounds.getBounds())) {
                double sr = MU.getDistance(mx, my, xc, yc);
                if (sr < r) {
                    selected.setLocation((int) (mx - 1.5), (int) (my - 1.5));
                    if (!e.isShiftDown()) {
                        if (selectedC1.getRGB() != PU.inverseColor(Main.programRobot.getPixelColor(mx + dxpix, my + dypix)).getRGB())
                            selectedC1 = Main.programRobot.getPixelColor((mx + dxpix), (my + dypix));
                        else selectedC1 = PU.inverseColor(Main.programRobot.getPixelColor(mx + dxpix, my + dypix));
                    } else {
                        if (selectedC1.getRGB() != PU.inverseColor(Main.programRobot.getPixelColor(mx + dxpix, my + dypix)).getRGB())
                            selectedC2 = Main.programRobot.getPixelColor((mx + dxpix), (my + dypix));
                        else selectedC2 = PU.inverseColor(Main.programRobot.getPixelColor(mx + dxpix, my + dypix));
                    }
                }
            }
        }
    }

    @Override
    public void scroll(MouseWheelEvent mwe) {

    }

    @Override
    public void keyPress(KeyEvent e) {


    }

    @Override
    public void mousePress(@NotNull MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        int dxpix = e.getXOnScreen() - mx;
        int dypix = e.getYOnScreen() - my;
        mb = e.getButton();
        if (mb == 1) {
            pt.setLocation(mx, my);
            if (pt.intersects(bounds.getBounds())) {
                double sr = MU.getDistance(mx, my, xc, yc);
                if (sr < r - 2) {
                    selected.setLocation((int) (mx - 1.5), (int) (my - 1.5));
                    if (!e.isShiftDown()) {
                        if (selectedC1.getRGB() != PU.inverseColor(Main.programRobot.getPixelColor(mx + dxpix, my + dypix)).getRGB())
                            selectedC1 = Main.programRobot.getPixelColor((mx + dxpix), (my + dypix));
                        else selectedC1 = PU.inverseColor(Main.programRobot.getPixelColor(mx + dxpix, my + dypix));
                    } else {
                        if (selectedC2.getRGB() != PU.inverseColor(Main.programRobot.getPixelColor(mx + dxpix, my + dypix)).getRGB())
                            selectedC2 = Main.programRobot.getPixelColor((mx + dxpix), (my + dypix));
                        else selectedC2 = PU.inverseColor(Main.programRobot.getPixelColor(mx + dxpix, my + dypix));
                    }
                }
            }
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 2; j++) {
                    if (colorButtons[i][j].contains(e.getX(), e.getY())) {
                        if (!e.isShiftDown()) {
                            selectedC1 = colors[i][j];
                        } else {
                            selectedC2 = colors[i][j];
                        }
                    }
                }
            }

        }

    }

    @Override
    public void mouseRelease(MouseEvent e) {

    }

    @Override
    public void paintGuiComponent(@NotNull Graphics2D g2d) {
        PU.castShadow(g2d, new Rectangle((int) colorButtons[0][0].getX(), (int) colorButtons[0][0].getY(), 16 * 2, 16 * 16), 8, ComponentManager.bgColor);
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 2; j++) {
                g2d.setColor(colors[i][j]);
                g2d.fill(colorButtons[i][j]);
            }
        }
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.draw(hover);
        double t;
        double circum;
        double cb6;
        double rate;
        float percent = saturation.getPercent();
        int red = 0;
        int green = 0;
        int blue = 0;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        PU.castShadow(g2d, colorWheel, 8, new Color(0, 0, 0, 0));

        for (int i = 1; i < r; i += 3) {
            circum = MU.getCircumRadius(i);
            cb6 = 60;
            rate = 255 / cb6;
            for (int j = 1; j <= circum; j++) {
                t = j * 360 / circum;
                double x_ = (xc) + i * MU.sin(t);
                double y_ = (yc) + i * MU.cos(t);
                if (t > 0 && t <= cb6) {
                    red = 255;
                    green = (int) (rate * t);
                    blue = 0;
                }
                if (t > cb6 && t <= 2 * cb6) {
                    red = (int) (((255) - rate * (t - cb6)));
                    green = 255;
                    blue = 0;
                }
                if (t > 2 * cb6 && t <= 3 * cb6) {
                    red = 0;
                    green = 255;
                    blue = (int) (rate * (t - cb6 * 2));
                }
                if (t > 3 * cb6 && t <= 4 * cb6) {
                    red = 0;
                    green = (int) (((255) - rate * (t - cb6 * 3)));
                    blue = 255;
                }
                if (t > 4 * cb6 && t <= 5 * cb6) {
                    red = (int) (rate * (t - cb6 * 4));
                    green = 0;
                    blue = 255;
                }
                if ((t > 5 * cb6 && t <= 6 * cb6) || t == 0) {
                    red = 255;
                    green = 0;
                    blue = (int) (((255) - rate * (t - cb6 * 5)));
                }
                red += ((r - i) * ((255 * percent) - red) / r);
                green += ((r - i) * ((255 * percent) - green) / r);
                blue += ((r - i) * ((255 * percent) - blue) / r);
                g2d.setColor(new Color(red, green, blue));
                g2d.fillRect((int) x_, (int) y_, 3, 3);
            }
        }
        g2d.setColor(PU.inverseColor(selectedC1));
        g2d.draw(selected);
        PU.castShadow(g2d, displayC1, 8, selectedC1);
        PU.castShadow(g2d, displayC2, 8, selectedC2);
        ta.setDisplay("#" + Integer.toHexString(selectedC1.getRGB()).substring(2));

    }

    @NotNull
    @Override
    public String toString() {
        return "---------------------------\nColor Wheel:\n\tx: " + (int) x + "\n\ty: " + (int) y + "\n\twidth: " + (int) width + "\n\theight: " + (int) height + "\n\t# of subComponents: " + subComponents.size() + " \n---------------------------";
    }

    public int getC1Red() {
        return selectedC1.getRed();
    }

    public int getC1Green() {
        return selectedC1.getGreen();
    }

    public int getC1Blue() {
        return selectedC1.getBlue();
    }

    public int getC2Red() {
        return selectedC2.getRed();
    }

    public int getC2Green() {
        return selectedC2.getGreen();
    }

    public int getC2Blue() {
        return selectedC2.getBlue();
    }

    public Color getColor() {
        return selectedC1;
    }

    public void setColor(Color color) {
        selectedC1 = color;
    }
}
