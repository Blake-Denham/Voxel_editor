package screen;

import guiTools.*;
import guiTools.TextArea;
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
    private Slider saturation;
    private TextArea ta;
    private Ellipse2D colorWheel;
    private Color selectedC;
    private Rectangle pt, display, selected;

    public ColorWheel(double x_, double y_, double width, Color bgColor) {
        super(x_, y_, width, width*1.5, bgColor, 14, true);
        r = width * 0.4;
        colorWheel = new Ellipse2D.Double(PU.getXInBounds(bounds, r * 2, 0.5), PU.getYInBounds(bounds, r * 2, MU.getPercent(30, height)), r * 2, r * 2);
        xc = colorWheel.getCenterX();
        yc = colorWheel.getCenterY();
        saturation = new Slider(PU.getXInBounds(bounds, width * 0.7, 0.5), PU.getYInBounds(bounds, height * 0.07, 0.7), Slider.HORIZONTAL, width * 0.7, height * 0.07, new Color(130, 130, 130), new Color(20, 20, 20));
        ta = new TextArea(PU.getXInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080) * MU.getPercent(120, 25), 0.5), PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.81), EditorScreen.s_maxHeight * MU.getPercent(25, 1080) * MU.getPercent(120, 25));
        selected = new Rectangle((int) (xc - 1.5), (int) (yc - 1.5), 3, 3);
        selectedC = new Color(255, 255, 255);
        display = new Rectangle((int)PU.getXInBounds(bounds,width*0.85,0.5), (int)PU.getYInBounds(bounds,height*0.1,0.96), (int) (width * 0.85), (int)(height*0.09));
        pt = new Rectangle(0, 0, 1, 1);
        add(ta);
        add(saturation);
        setToolBarTitle("COLOUR PICKER");
    }

    @Override
    public void update() {
        setBounds(EditorScreen.s_maxWidth*(1-MU.getPercent(305,1920))-16, 10+26,EditorScreen.s_maxWidth*MU.getPercent(300,1920),EditorScreen.s_maxWidth*MU.getPercent(300,1920)*1.4);
        r = width * 0.4;
        colorWheel.setFrame(PU.getXInBounds(bounds, r * 2, 0.5), PU.getYInBounds(bounds, r * 2, MU.getPercent(30, height)), r * 2, r * 2);
        xc = colorWheel.getCenterX();
        yc = colorWheel.getCenterY();
        display.setBounds((int)PU.getXInBounds(bounds,width*0.85,0.5), (int)PU.getYInBounds(bounds,height*0.1,0.96), (int) (width * 0.85), (int)(height*0.09));
        ta.setBounds(PU.getXInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080) * MU.getPercent(120, 25), 0.5), PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.81), EditorScreen.s_maxHeight * MU.getPercent(25, 1080) * MU.getPercent(120, 25), EditorScreen.s_maxHeight* MU.getPercent(25,1080));
        saturation.setBounds(PU.getXInBounds(bounds, width * 0.7, 0.5), PU.getYInBounds(bounds, height * 0.07, 0.7), width * 0.7, height * 0.07);
    }

    @Override
    public void hover(MouseEvent e) {

    }

    @Override
    public void click(MouseEvent e) {

    }

    @Override
    public void drag(MouseEvent e) {

    }

    @Override
    public void scroll(MouseWheelEvent mwe) {

    }

    @Override
    public void keyPress(KeyEvent e) {

    }

    @Override
    public void keyRelease(KeyEvent e) {

    }

    @Override
    public void mousePress(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        int dxpix = e.getXOnScreen() - mx;
        int dypix = e.getYOnScreen() - my;
        if (e.getButton() == 1) {
            pt.setLocation(mx, my);
            if (pt.intersects(bounds.getBounds())) {


                double sr = MU.getDistance(mx, my, xc, yc);
                if (sr < r - 2) {
                    selected.setLocation((int) (mx - 1.5), (int) (my - 1.5));
                    if (selectedC.getRGB() != PU.inverseColor(Main.programRobot.getPixelColor(mx + dxpix, my + dypix)).getRGB())
                        selectedC = Main.programRobot.getPixelColor((mx + dxpix), (my + dypix));
                    else selectedC = PU.inverseColor(Main.programRobot.getPixelColor(mx + dxpix, my + dypix));

                }
            }
        }
    }

    @Override
    public void mouseRelease(MouseEvent e) {

    }

    @Override
    public void paintGuiComponent(Graphics2D g2d) {
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
        //PU.castShadow(g2d, displayBG, 7, bgColor.brighter());
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
        g2d.setColor(PU.inverseColor(selectedC));
        g2d.draw(selected);
        PU.castShadow(g2d, display, 8, selectedC);
        ta.setDisplay("#" + Integer.toHexString(selectedC.getRGB()).substring(2));

    }

    @Override
    public String toString() {
        return "---------------------------\nColor Wheel:\n\tx: " + (int)x+"\n\ty: " + (int)y + "\n\twidth: " + (int)width + "\n\theight: " + (int)height+ "\n\t# of subComponents: " + subComponents.size()+ " \n---------------------------";
    }
}
