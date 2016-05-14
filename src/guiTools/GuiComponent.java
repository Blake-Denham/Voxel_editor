package guiTools;

import screen.EditorScreen;
import screen.Main;
import util.PU;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.util.ArrayList;

public abstract class GuiComponent {

    protected double x, y, width, height, shadowSize;
    protected Rectangle bounds;
    protected Color bgColor;
    protected ArrayList<GuiComponent> subComponents;

    private int minimized = 1;
    private boolean drawBackground, minimizable, ignoreMinimize = false;
    private Rectangle toolBar;
    private Button minimize;
    private String toolBarTitle = "";

    public GuiComponent(double x, double y, double width, double height, Color bgColor, int shadowSize, boolean minimizable) {
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
                final Image buttonSprite1 = ImageIO.read(Main.getResource("minimize.png"));
                final Image buttonSprite2 = ImageIO.read(Main.getResource("maximize.png"));
                minimize = new Button(PU.getXInBounds(toolBar, toolBar.getWidth() * 0.1, 0.98), PU.getYInBounds(toolBar, toolBar.getWidth() * 0.1, 0.5), toolBar.getWidth() * 0.1, toolBar.getWidth() * 0.1,  buttonSprite1, () -> {
                    minimized *= -1;
                    if (minimized > 0) {
                        minimize.setButtonImage(buttonSprite1);
                    } else {
                        minimize.setButtonImage(buttonSprite2);
                    }

                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            minimize.setIgnoreMinimize(true);
            add(minimize);
        }
        this.bgColor = bgColor;
        this.shadowSize = shadowSize;
        drawBackground = true;
        setDimensions(width, height);

    }

    public GuiComponent(double x, double y, double width, double height) {

        drawBackground = false;
        minimizable = false;
        shadowSize = 0;
        bounds = new Rectangle((int) x, (int) y, (int) width, (int) height);
        setDimensions(width, height);
        setLocation(x, y);
        subComponents = new ArrayList<>();
    }

    private void paintBackground(Graphics2D g2d) {
        if (drawBackground) {
            PU.castShadow(g2d, bounds, (int) shadowSize, bgColor);
        }
    }

    private void paintToolBar(Graphics2D g2d) {
        if (drawBackground) {
            PU.castShadow(g2d, toolBar, (int) shadowSize, bgColor);
            g2d.setColor(Color.gray);
            if (minimized > 0) {
                g2d.drawLine((int) (x + 12), (int) (y - 1), (int) (x + width - 12), (int) (y - 1));
            }
            g2d.setColor(Color.white);
            g2d.setFont(EditorScreen.font.deriveFont(PU.getOptimalFontsize(g2d, EditorScreen.font, toolBarTitle, (float) 14, width)));
            PU.setTextRenderingQuality(g2d, true);
            g2d.drawString(toolBarTitle, (int) (x + 6), (int) (y - 30 / 2 + 12 / 2));
        }


    }

    private void paintSubComponents(Graphics2D g2d) {
        for (GuiComponent subComponent : subComponents) {

            if (minimized < 0) {
                if (subComponent.ignoreMinimize)
                    subComponent.paintAll(g2d);
            } else {
                subComponent.paintAll(g2d);
            }
        }
    }

    public void paintAll(Graphics2D g2d) {
        if (minimizable) {
            paintToolBar(g2d);
        }
        if (minimized > 0) {
            paintBackground(g2d);
            paintGuiComponent(g2d);

        }
        paintSubComponents(g2d);
    }

    public void setShadowSize(double shadowSize) {
        this.shadowSize = shadowSize;
    }

    public void mouseMove(MouseEvent e) {
        hover(e);
        for (GuiComponent subComponent : subComponents) {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.hover(e);
                }
            } else {
                subComponent.hover(e);
            }
        }
    }

    public void mouseClick(MouseEvent e) {
        click(e);
        for (GuiComponent subComponent : subComponents) {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.click(e);
                }
            } else {
                subComponent.click(e);
            }
        }
    }

    public void mouseDrag(MouseEvent e) {
        drag(e);
        for (GuiComponent subComponent : subComponents) {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.drag(e);
                }
            } else {
                subComponent.drag(e);
            }
        }
    }

    public void mouseWheel(MouseWheelEvent e) {
        scroll(e);
        for (GuiComponent subComponent : subComponents) {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.scroll(e);
                }
            } else {
                subComponent.scroll(e);
            }
        }
    }

    public void keyPressedSC(KeyEvent e) {
        keyPress(e);
        for (GuiComponent subComponent : subComponents) {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.keyPress(e);
                }
            } else {
                subComponent.keyPress(e);
            }
        }
    }

    public void keyReleaseSC(KeyEvent e) {
        keyRelease(e);
        for (GuiComponent subComponent : subComponents) {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.keyRelease(e);
                }
            } else {
                subComponent.keyRelease(e);
            }
        }
    }

    public void mousePressSC(MouseEvent e) {
        mousePress(e);
        for (GuiComponent subComponent : subComponents) {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.mousePress(e);
                }
            } else {
                subComponent.mousePress(e);
            }
        }
    }

    public void mouseReleaseSC(MouseEvent e) {
        mouseRelease(e);
        for (GuiComponent subComponent : subComponents) {
            if (minimized < 0) {
                if (subComponent.ignoreMinimize) {
                    subComponent.mouseRelease(e);
                }
            } else {
                subComponent.mouseRelease(e);
            }
        }
    }

    public void updateAll() {
        update();
        if (drawBackground) {
            minimize.setBounds(PU.getXInBounds(toolBar, toolBar.getWidth() * 0.1, 0.98), PU.getYInBounds(toolBar, toolBar.getWidth() * 0.1, 0.5), toolBar.getWidth() * 0.1, toolBar.getWidth() * 0.1);
            toolBar.setBounds((int) x, (int) bounds.getY() - 30, (int) width, 30);

        }
        subComponents.forEach(GuiComponent::update);
    }

    public void add(GuiComponent subComponent) {
        subComponents.add(subComponent);
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
        bounds.setLocation((int) x, (int) y);

    }

    public void setDimensions(double width, double height) {
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

    public void setBounds(Rectangle newBounds) {
        x = newBounds.getX();
        y = newBounds.getY();
        width = newBounds.getWidth();
        height = newBounds.getHeight();
        bounds.setBounds(newBounds);
    }

    public void setIgnoreMinimize(boolean ignoreMinimize) {
        this.ignoreMinimize = ignoreMinimize;
    }

    public void setToolBarTitle(String title) {
        toolBarTitle = title;
    }

    public int getMinimized(){
        return minimized;
    }

    public double getY() {
        return y;
    }

    public double getX(){
        return x;
    }

    public double getWidth(){
        return width;
    }

    public double getHeight(){
        return  height;
    }

    @Override
    public String toString() {
        return "---------------------------\nx: " + x + "\ny: " + y + "\nwidth: " + width + "\nheight: " + height + "\n# of subComponents: " + subComponents.size() + " \n---------------------------";
    }

    protected abstract void paintGuiComponent(Graphics2D g2d);

    protected abstract void update();

    protected abstract void hover(MouseEvent e);

    protected abstract void click(MouseEvent e);

    protected abstract void drag(MouseEvent e);

    protected abstract void scroll(MouseWheelEvent mwe);

    protected abstract void keyPress(KeyEvent e);

    protected abstract void keyRelease(KeyEvent e);

    protected abstract void mousePress(MouseEvent e);

    protected abstract void mouseRelease(MouseEvent e);

    public void setBackgroundColor(Color backgroundColor) {
        this.bgColor = backgroundColor;
    }
}
