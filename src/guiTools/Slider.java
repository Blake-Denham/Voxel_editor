package guiTools;

import org.jetbrains.annotations.NotNull;
import util.PU;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Ellipse2D;

@SuppressWarnings("SameParameterValue")
public class Slider extends GuiComponent {
    private static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;

    private GuiEvent onDrag;
    private final Color sliderColor;
    private final Color bgColor;
    private float percent;
    private final int direction;
    private int mb;
    private double sx, sy, ssize, isize;
    private boolean changing = false, grow = false;
    private Ellipse2D slider, inslider;

    public Slider(double x, double y, int direction, double width, double height, Color bgColor, Color sliderColor) {
        super(x, y, width, height);
        this.sliderColor = sliderColor;
        this.bgColor = bgColor;
        percent = 1f;
        ssize = 10;
        isize = 0;
        switch (direction) {
            case VERTICAL:
                sx = (x + (width / 8));
                sy = ((y + (height - ssize / 2) * percent));
                slider = new Ellipse2D.Double(sx, sy, (7 * width / 8), 15);
                inslider = new Ellipse2D.Double(sx + ssize / 2, sy + ssize / 2, isize, isize);
                break;
            case HORIZONTAL:
                sx = (x + (width - ssize / 2) * percent);
                sy = (y + (height / 8));
                slider = new Ellipse2D.Double(sx, sy, 15, (7 * height / 8));
                inslider = new Ellipse2D.Double(sx + ssize / 2, sy + ssize / 2, isize, isize);
                break;
        }
        if (direction != 0 && direction != 1) {
            this.direction = 0;
        } else {
            this.direction = direction;
        }
    }


    @Override
    protected void paintGuiComponent(@NotNull Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        PU.castShadow(g2d, bounds, 8, bgColor);
        switch (direction) {
            case VERTICAL:
                g2d.setColor(Color.black);
                g2d.fillRect((int) (x + (width / 2) - 1), (int) (y + 4), 2, (int) (height - 2));
                break;
            case HORIZONTAL:
                g2d.setColor(new Color(80, 80, 80));
                g2d.fillRect((int) (x + ((width - 8) * percent)), (int) (y + (height / 2) - 1), (int) ((width - 4) - (width - 8) * percent), 2);
                g2d.setColor(Color.black);
                g2d.fillRect((int) (x + 4), (int) (y + (height / 2) - 1), (int) ((width - 8) * percent), 2);
                break;
        }
        g2d.setColor(sliderColor);
        g2d.fill(slider);
        g2d.setColor(PU.saturateColor(Color.white, percent));
        g2d.fill(inslider);
        if (grow && isize < ssize - 5) {
            isize += 1;
        } else if (!grow && isize > 0) {
            isize -= 1.2;

        }
    }

    @Override
    protected void update() {
        switch (direction) {
            case VERTICAL:
                sx = (x + (width / 2) - ssize / 2);
                sy = ((y + (height - ssize) * percent));
                slider.setFrame(sx, sy, ssize, ssize);
                inslider.setFrame(sx + ssize / 2 - isize / 2, sy + ssize / 2 - isize / 2, isize, isize);
                break;
            case HORIZONTAL:
                sx = (x + (width - ssize) * percent);
                sy = (y + (height / 2) - ssize / 2);
                slider.setFrame(sx, sy, ssize, ssize);
                inslider.setFrame(sx + ssize / 2 - isize / 2, sy + ssize / 2 - isize / 2, isize, isize);
                break;
        }
    }

    @Override
    public void hover(@NotNull MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if (slider.contains(mx, my)) {
            ssize = 5*height/8;
            grow = true;
        } else {
            ssize = height/2;

            grow = false;
        }
    }

    @Override
    public void drag(@NotNull MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (mb == 1 && changing) {
            switch (direction) {
                case VERTICAL:
                    if (my > y && my < y + height) {
                        percent = (float) ((my - y) / height * 1f);
                    }
                    if (my > y + height) {
                        percent = 1f;
                    }
                    if (my < y) {
                        percent = 0f;
                    }
                    if (onDrag != null) {
                        onDrag.event();
                    }
                    break;
                case HORIZONTAL:
                    if (mx > x && mx < x + width) {
                        percent = (float) ((mx - x) / width * 1f);
                    }
                    if (mx > x + width) {
                        percent = 1f;
                    }
                    if (mx < x) {
                        percent = 0f;
                    }
                    if (onDrag != null) {
                        onDrag.event();
                    }
                    break;
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
        mb = e.getButton();
        int mx = e.getX();
        int my = e.getY();

        Rectangle pt = new Rectangle(mx, my, 1, 1);
        changing = pt.intersects(slider.getBounds());
    }

    @Override
    public void mouseRelease(@NotNull MouseEvent e) {
        if (!slider.contains(e.getX(), e.getY())) {
            grow = false;
            ssize = height/2;
            changing = false;
        }
    }

    public float getPercent() {
        return percent;
    }

    public boolean getChanging(){
        return changing;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public void setOnDrag(GuiEvent ge) {
        onDrag = ge;
    }
}
