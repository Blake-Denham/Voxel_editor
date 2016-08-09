package guiTools;

import editorScreen.EditorScreen;
import editorScreen.Main;
import org.jetbrains.annotations.NotNull;
import util.PU;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.util.ArrayList;

public abstract class GuiComponent {

    protected double x;
    protected double y;
    protected double width;
    protected double height;
    private double shadowSize;
    @NotNull
    protected final Rectangle bounds;
    Color bgColor;
    @NotNull
    protected final ArrayList<GuiComponent> subComponents;
    private int minimized = 1;
    private double p = 0.98;
    private final boolean drawBackground;
    private final boolean minimizable;
    private boolean ignoreMinimize = false;
    private Rectangle toolBar;
    private Button minimize;
    private String toolBarTitle = "";
    private String minimizeToolTip = "minimize";
    private boolean disabled = false;

    protected GuiComponent(double x, double y, double width, double height, Color bgColor, int shadowSize, boolean minimizable) {
        this.minimizable = minimizable;
        subComponents = new ArrayList<>();

        if (minimizable) {
            bounds = new Rectangle((int) x, (int) y + 30, (int) width, (int) height);
            setLocation(x, y + 30);
            toolBar = new Rectangle((int) x, (int) bounds.getY() - 30, (int) width, 30);
        } else {
            bounds = new Rectangle((int) x, (int) y, (int) width, (int) height);
            setLocation(x, y);
            toolBar = new Rectangle(0, 0, 0, 0);
        }
        if (minimizable) {
            try {
                final Image buttonSprite1 = ImageIO.read(Main.getResource("Images/minimize.png"));
                final Image buttonSprite2 = ImageIO.read(Main.getResource("Images/maximize.png"));
                minimize = new Button(PU.getXInBounds(toolBar, 25, 0.98), PU.getYInBounds(toolBar, 25, 0.5), 25, 25, buttonSprite1, minimizeToolTip, () -> {
                    minimized *= -1;
                    if (minimized > 0) {
                        minimize.setButtonImage(buttonSprite1);
                        minimizeToolTip = "minimize";
                        minimize.setToolTip(minimizeToolTip);
                    } else {
                        minimize.setButtonImage(buttonSprite2);
                        minimizeToolTip = "maximize";
                        minimize.setToolTip(minimizeToolTip);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            minimize.setIgnoreMinimize();
            add(minimize);
        }
        this.bgColor = bgColor;
        this.shadowSize = shadowSize;
        drawBackground = true;
        setDimensions(width, height);
    }

    protected GuiComponent(double x, double y, double width, double height) {

        drawBackground = false;
        minimizable = false;
        shadowSize = 0;
        bounds = new Rectangle((int) x, (int) y, (int) width, (int) height);
        setDimensions(width, height);
        setLocation(x, y);
        subComponents = new ArrayList<>();
    }

    private void paintBackground(@NotNull Graphics2D g2d) {
        if (drawBackground) {
            PU.castShadow(g2d, bounds, (int) shadowSize, bgColor);
        }
    }

    private void paintToolBar(@NotNull Graphics2D g2d) {
        if (drawBackground) {
            PU.castShadow(g2d, toolBar, (int) shadowSize, bgColor);
            g2d.setColor(Color.gray);
            if (minimized > 0) {
                g2d.drawLine((int) (x + 12), (int) (y - 1), (int) (x + width - 12), (int) (y - 1));
            }
            g2d.setColor(Color.white);
            g2d.setFont(EditorScreen.font.deriveFont(PU.getOptimalFontsize(g2d, EditorScreen.font, toolBarTitle, (float) 14, width)));
            PU.setTextRenderingQuality(g2d);
            g2d.drawString(toolBarTitle, (int) (x + 6), (int) (y - 30 / 2 + 12 / 2));
        }


    }

    private void paintSubComponents(@NotNull Graphics2D g2d) {
        for (GuiComponent subComponent : subComponents) {

            if (minimized < 0) {
                if (subComponent.ignoreMinimize)
                    subComponent.paintAll(g2d);
            } else {
                subComponent.paintAll(g2d);
            }
        }
    }

    public void paintAll(@NotNull Graphics2D g2d) {
        if (minimizable) {
            paintToolBar(g2d);
        }
        if (minimized > 0) {
            paintBackground(g2d);
            paintGuiComponent(g2d);
        }
        paintSubComponents(g2d);
    }

    void setShadowSize(double shadowSize) {
        this.shadowSize = shadowSize;
    }

    public void mouseMove(MouseEvent e) {
        hover(e);
        subComponents.stream().filter(subComponent -> !subComponent.getDisabled()).forEach(subComponent -> {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.hover(e);
                }
            } else {
                subComponent.hover(e);
            }
        });
    }

    public void mouseDrag(MouseEvent e) {
        drag(e);
        subComponents.stream().filter(subComponent -> !subComponent.getDisabled()).forEach(subComponent -> {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.drag(e);
                }
            } else {
                subComponent.drag(e);
            }
        });
    }

    public void mouseWheel(MouseWheelEvent e) {
        scroll(e);
        subComponents.stream().filter(subComponent -> !subComponent.getDisabled()).forEach(subComponent -> {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.scroll(e);
                }
            } else {
                subComponent.scroll(e);
            }
        });
    }

    public void keyPressedSC(KeyEvent e) {
        keyPress(e);
        subComponents.stream().filter(subComponent -> !subComponent.getDisabled()).forEach(subComponent -> {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.keyPress(e);
                }
            } else {
                subComponent.keyPress(e);
            }
        });
    }

    public void mousePressSC(MouseEvent e) {
        mousePress(e);
        subComponents.stream().filter(subComponent -> !subComponent.getDisabled()).forEach(subComponent -> {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.mousePress(e);
                }
            } else {
                subComponent.mousePress(e);
            }
        });
    }

    public void mouseReleaseSC(MouseEvent e) {
        mouseRelease(e);
        subComponents.stream().filter(subComponent -> !subComponent.getDisabled()).forEach(subComponent -> {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.mouseRelease(e);
                }
            } else {
                subComponent.mouseRelease(e);
            }
        });
    }

    public void updateAll() {
        update();
        if (drawBackground) {
            minimize.setBounds(PU.getXInBounds(toolBar, 25, p), PU.getYInBounds(toolBar, 25, 0.5), 25, 25);
            toolBar.setBounds((int) x, (int) bounds.getY() - 30, (int) width, 30);

        }
        subComponents.forEach(GuiComponent::update);
    }

    protected void add(GuiComponent subComponent) {
        subComponents.add(subComponent);
    }

    private void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
        bounds.setLocation((int) x, (int) y);

    }

    private void setDimensions(double width, double height) {
        this.width = width;
        this.height = height;

        bounds.setSize((int) width, (int) height);
    }

    public void setBounds(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        bounds.setBounds((int) x, (int) y, (int) width, (int) height);
    }

    public void setBounds(@NotNull Rectangle newBounds) {
        x = newBounds.getX();
        y = newBounds.getY();
        width = newBounds.getWidth();
        height = newBounds.getHeight();
        bounds.setBounds(newBounds);
    }

    void setIgnoreMinimize() {
        this.ignoreMinimize = true;
    }

    protected void setToolBarTitle(String title) {
        toolBarTitle = title;
    }

    public int getMinimized() {
        return minimized;
    }

    public double getY() {
        return y;
    }

    @SuppressWarnings("unused")
    public double getX() {
        return x;
    }

    @SuppressWarnings("unused")
    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @NotNull
    @Override
    public String toString() {
        return "---------------------------\nx: " + x + "\ny: " + y + "\nwidth: " + width + "\nheight: " + height + "\n# of subComponents: " + subComponents.size() + " \n---------------------------";
    }

    protected abstract void paintGuiComponent(Graphics2D g2d);

    protected abstract void update();

    protected abstract void hover(MouseEvent e);

    protected abstract void drag(MouseEvent e);

    protected abstract void scroll(MouseWheelEvent mwe);

    protected abstract void keyPress(KeyEvent e);

    protected abstract void mousePress(MouseEvent e);

    protected abstract void mouseRelease(MouseEvent e);

    protected void setP() {
        this.p = 0.995;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public @NotNull Rectangle getBounds() {
        return bounds;
    }

    public void hide() {
        setBounds(0, 0, 0, 0);
    }

    public void minimize() {
        minimized = -1;
    }

    public void maximize() {
        minimized = 1;
    }
}
