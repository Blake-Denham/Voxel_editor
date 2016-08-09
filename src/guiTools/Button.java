package guiTools;


import editorScreen.EditorScreen;
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
    @NotNull
    private final GuiEvent event;
    @NotNull
    private final Rectangle hoverBounds;
    @NotNull
    private final Rectangle defaultBounds;
    @NotNull
    private final Rectangle clickBounds;
    private String toolTip;
    private boolean isHovering;
    private int mx, my;

    public Button(double x_, double y_, double width, double height, @NotNull Image buttonImage, String toolTip, @NotNull GuiEvent event) {
        super(x_, y_, width, height, new Color(0, 0, 0), 4, false);
        this.buttonImage = buttonImage;
        double ibr = (width) / buttonImage.getWidth(null);
        af = new AffineTransform(ibr, 0, 0, ibr, PU.getXInBounds(bounds, width, 0.5), PU.getYInBounds(bounds, height, 0.5));
        defaultBounds = new Rectangle((int) x, (int) y, (int) width, (int) height);
        hoverBounds = new Rectangle((int) (x - 3 / 2.0), (int) (y - 3 / 2.0), (int) (width + 3), (int) (height + 3));
        clickBounds = new Rectangle((int) (x + 1), (int) (y + 1), (int) (width - 2), (int) (height - 2));
        this.event = event;
        this.toolTip = toolTip;
    }

    @Override
    protected void paintGuiComponent(@NotNull Graphics2D g2d) {
        g2d.drawImage(buttonImage, af, null);
        if (isHovering) {
            g2d.setColor(new Color(0xf6ff92));
            g2d.setFont(EditorScreen.font.deriveFont(12f));
            g2d.fillRect(mx - g2d.getFontMetrics().stringWidth(toolTip), my - g2d.getFontMetrics().getHeight(), g2d.getFontMetrics().stringWidth(toolTip), g2d.getFontMetrics().getHeight());
            g2d.setColor(Color.BLACK);
            g2d.drawString(toolTip, mx - g2d.getFontMetrics().stringWidth(toolTip), my - g2d.getFontMetrics().getHeight() / 3);

        }
    }

    @Override
    protected void update() {
        double ibr = (width) / buttonImage.getWidth(null);
        double ibr2 = (height) / buttonImage.getHeight(null);
        af.setTransform(ibr, 0, 0, ibr2, PU.getXInBounds(bounds, width, 0.5), PU.getYInBounds(bounds, height, 0.5));
        defaultBounds.setLocation((int) x, (int) y);
        hoverBounds.setLocation((int) (x), (int) (y));
        clickBounds.setLocation((int) (x), (int) (y));

    }

    @Override
    protected void hover(@NotNull MouseEvent e) {
        mx = e.getX();
        my = e.getY();
        if (bounds.contains(mx, my)) {
            setBounds(hoverBounds);
            double ibr = (width) / buttonImage.getWidth(null);
            double ibr2 = (height) / buttonImage.getHeight(null);
            af.setTransform(ibr, 0, 0, ibr2, PU.getXInBounds(bounds, width, 0.5), PU.getYInBounds(bounds, height, 0.5));
            setShadowSize(6);
            isHovering = true;
        } else {
            setBounds(defaultBounds);
            double ibr = (width) / buttonImage.getWidth(null);
            double ibr2 = (height) / buttonImage.getHeight(null);
            af.setTransform(ibr, 0, 0, ibr2, PU.getXInBounds(bounds, width, 0.5), PU.getYInBounds(bounds, height, 0.5));
            setShadowSize(4);
            isHovering = false;
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
            double ibr = (width) / buttonImage.getWidth(null);
            af.setTransform(ibr, 0, 0, ibr, PU.getXInBounds(bounds, width, 0.5), PU.getYInBounds(bounds, height, 0.5));
            setShadowSize(3);
            event.event();


        }
    }

    @Override
    public void mouseRelease(@NotNull MouseEvent e) {
        double ibr = (width) / buttonImage.getWidth(null);
        af.setTransform(ibr, 0, 0, ibr, PU.getXInBounds(bounds, width, 0.5), PU.getYInBounds(bounds, height, 0.5));
        setBounds(defaultBounds);
        setShadowSize(4);
        hover(e);
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public void setButtonImage(Image img) {
        buttonImage = img;
    }

    public GuiEvent getActionEvent() {
        return event;
    }
}
