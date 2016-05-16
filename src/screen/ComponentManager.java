package screen;

import guiTools.GuiComponent;
import org.jetbrains.annotations.NotNull;
import util.MU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;


class ComponentManager extends JComponent {
    @NotNull
    private final ArrayList<GuiComponent> guiComponents;
    private static ColorWheel colorWheel;
    private static Canvas canvas;
    private static CameraControl ci;
    private static CanvasManipulator cm;

    public ComponentManager() {

        guiComponents = new ArrayList<>();
        canvas = new Canvas(24, 24);
        colorWheel = new ColorWheel(EditorScreen.s_maxWidth * (1 - MU.getPercent(315, 1920)), 5, EditorScreen.s_maxWidth * MU.getPercent(300, 1920), new Color(55, 55, 55));
        ci = new CameraControl(EditorScreen.s_maxWidth * (1 - MU.getPercent(315, 1920)), EditorScreen.s_maxHeight * MU.getPercent(EditorScreen.s_maxWidth * MU.getPercent(300, 1920) * 1.4 - 1080 - 8, 1080), EditorScreen.s_maxWidth * MU.getPercent(250, 1920), new Color(55, 55, 55));
        cm = new CanvasManipulator(EditorScreen.s_maxWidth * (MU.getPercent(315, 1920)), 5, EditorScreen.s_maxWidth * (1 - MU.getPercent(315, 1920)), EditorScreen.s_maxWidth * MU.getPercent(500, 1920) * 0.3, new Color(55, 55, 55));
        addComponent(canvas);
        addComponent(colorWheel);
        addComponent(ci);
        addComponent(cm);

    }

    private void addComponent(GuiComponent component) {
        guiComponents.add(component);
    }

    private void update() {
        guiComponents.forEach(GuiComponent::updateAll);

    }

    public void hover(MouseEvent e) {
        for (GuiComponent guiComponent : guiComponents) {
            guiComponent.mouseMove(e);
        }
    }

    public void drag(MouseEvent e) {
        for (GuiComponent guiComponent : guiComponents) {
            guiComponent.mouseDrag(e);
        }
    }

    public void keyPress(KeyEvent e) {
        for (GuiComponent guiComponent : guiComponents) {
            guiComponent.keyPressedSC(e);
        }
    }

    public void mouseWheel(MouseWheelEvent e) {
        for (GuiComponent guiComponent : guiComponents) {
            guiComponent.mouseWheel(e);
        }
    }

    public void mousePress(MouseEvent e) {
        for (GuiComponent guiComponent : guiComponents) {
            guiComponent.mousePressSC(e);
        }
    }

    public void mouseRelease(MouseEvent e) {
        for (GuiComponent guiComponent : guiComponents) {
            guiComponent.mouseReleaseSC(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        update();
        Graphics2D g2d = (Graphics2D) g;
        for (GuiComponent guiComponent : guiComponents) {
            guiComponent.paintAll(g2d);
        }

    }

    public static Canvas getCanvas() {
        return canvas;
    }

    public static ColorWheel getColorWheel() {
        return colorWheel;
    }

    @SuppressWarnings("unused")
    public static CameraControl getCanvasInfo() {
        return ci;
    }

    public static void setRotateCamera(int direction) {
        if (direction > 360) {
            direction = 0;
        }
        canvas.setRotate(direction);
    }

    public static void setRotateyCamera(int direction) {
        if (direction > 90) {
            direction = 90;
        }
        if (direction < -90) {
            direction = -90;
        }
        canvas.setRotatey(direction);
    }

    public static void setZoomCamera(int zoom) {
        canvas.setZoom(zoom);
    }
}
