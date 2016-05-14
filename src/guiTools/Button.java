package guiTools;


import org.jetbrains.annotations.NotNull;
import util.PU;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

public class Button extends GuiComponent {

    private Image buttonImage;
    @NotNull
    private final AffineTransform af;
    private final GuiEvent event;
    @NotNull
    private final Rectangle hoverBounds;
    @NotNull
    private final Rectangle defaultBounds;
    @NotNull
    private final Rectangle clickBounds;

    public Button(double x_, double y_, double width, double height, @NotNull Image buttonImage, GuiEvent event) {
        super(x_, y_, width, height, new Color(0,0,0), 4, false);
        this.buttonImage = buttonImage;
        double ibr = (width)/buttonImage.getWidth(null);
        af =  new AffineTransform(ibr,0,0,ibr, PU.getXInBounds(bounds,width,0.5),PU.getYInBounds(bounds,height,0.5));
        defaultBounds = new Rectangle((int)x,(int)y,(int)width,(int)height);
        hoverBounds = new Rectangle((int)(x-3/2.0),(int)(y-3/2.0),(int)(width+3),(int)(height+3));
        clickBounds = new Rectangle((int)(x+1),(int)(y+1),(int)(width-2),(int)(height-2));
        this.event =event;
    }

    @Override
    protected void paintGuiComponent(@NotNull Graphics2D g2d) {
        g2d.drawImage(buttonImage, af, null);
    }

    @Override
    protected void update() {
        double ibr = (width)/buttonImage.getWidth(null);
        af.setTransform(ibr,0,0,ibr, PU.getXInBounds(bounds,width,0.5),PU.getYInBounds(bounds,height,0.5));
        defaultBounds.setLocation((int)x,(int)y);
        hoverBounds.setLocation((int)(x-3/2.0),(int)(y-3/2.0));
        clickBounds.setLocation((int)(x+1),(int)(y+1));
    }

    @Override
    protected void hover(@NotNull MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if (bounds.contains(mx, my)) {
            setBounds(hoverBounds);
            double ibr = (width)/buttonImage.getWidth(null);
            af.setTransform(ibr,0,0,ibr, PU.getXInBounds(bounds,width,0.5),PU.getYInBounds(bounds,height,0.5));
            setShadowSize(6);
        } else {
            setBounds(defaultBounds);
            double ibr = (width)/buttonImage.getWidth(null);
            af.setTransform(ibr,0,0,ibr, PU.getXInBounds(bounds,width,0.5),PU.getYInBounds(bounds,height,0.5));
            setShadowSize(4);
        }
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
    public void mousePress(@NotNull MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if (bounds.contains(mx, my)) {

            setBounds(clickBounds);
            double ibr = (width)/buttonImage.getWidth(null);
            af.setTransform(ibr,0,0,ibr, PU.getXInBounds(bounds,width,0.5),PU.getYInBounds(bounds,height,0.5));
            setShadowSize(3);
            event.event();


        }
    }

    @Override
    public void mouseRelease(@NotNull MouseEvent e) {
        double ibr = (width)/buttonImage.getWidth(null);
        af.setTransform(ibr,0,0,ibr, PU.getXInBounds(bounds,width,0.5),PU.getYInBounds(bounds,height,0.5));
        setBounds(defaultBounds);
        setShadowSize(4);

        hover(e);
    }

    public void setButtonImage(Image img){
        buttonImage = img;
    }

}
