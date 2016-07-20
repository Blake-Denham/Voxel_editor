package editorScreen;

import backend.Project;
import backend.Settings;
import guiTools.GuiComponent;
import org.jetbrains.annotations.NotNull;
import util.MU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;


public class ComponentManager extends JComponent {
    @NotNull
    private final ArrayList<GuiComponent> guiComponents;
    private static ColorWheel colorWheel;
    private static Canvas canvas;
    private static CameraControl cc;
    private static CanvasManipulator cm;
    private static CubeInfo ci;
    private static CanvasDataManager cdm;
    private static Color bgColor = new Color(55, 55, 55);
    public static Settings settings;

    public ComponentManager() {
        settings = new Settings();
        guiComponents = new ArrayList<>();
        canvas = new Canvas(17, 17 + 1);
        colorWheel = new ColorWheel(EditorScreen.s_maxWidth * (1 - MU.getPercent(315, 1920)), 5, EditorScreen.s_maxWidth * MU.getPercent(300, 1920), bgColor);
        cc = new CameraControl(EditorScreen.s_maxWidth * (1 - MU.getPercent(315, 1920)), EditorScreen.s_maxHeight * MU.getPercent(EditorScreen.s_maxWidth * MU.getPercent(300, 1920) * 1.4 - 1080 - 8, 1080), EditorScreen.s_maxWidth * MU.getPercent(250, 1920), bgColor);
        cm = new CanvasManipulator(EditorScreen.s_maxWidth * (MU.getPercent(315, 1920)), 5, EditorScreen.s_maxWidth * (1 - MU.getPercent(315, 1920)), EditorScreen.s_maxWidth * MU.getPercent(500, 1920) * 0.3, bgColor);
        ci = new CubeInfo(EditorScreen.s_maxWidth * (1 - MU.getPercent(315, 1920)), EditorScreen.s_maxHeight * MU.getPercent(EditorScreen.s_maxWidth * MU.getPercent(300, 1920) * 1.4 - 1080 - 8, 1080), EditorScreen.s_maxWidth * MU.getPercent(250, 1920), bgColor);
        cdm = new CanvasDataManager(EditorScreen.s_maxWidth * (MU.getPercent(315, 1920)), 5, EditorScreen.s_maxWidth * (1 - MU.getPercent(315, 1920)), EditorScreen.s_maxWidth * MU.getPercent(500, 1920) * 0.3, bgColor);
        addComponent(canvas);
        addComponent(cdm);
        addComponent(colorWheel);
        addComponent(cc);
        addComponent(cm);
        addComponent(ci);


    }

    public static CanvasManipulator getCanvasManipulator() {
        return cm;
    }

    public static CanvasDataManager getCanvasDataManger() {
        return cdm;
    }

    private void addComponent(GuiComponent component) {
        guiComponents.add(component);
    }

    private void update() {
        guiComponents.forEach(GuiComponent::updateAll);

    }

    public void hover(MouseEvent e) {
        guiComponents.stream().filter(guiComponent -> !guiComponent.getDisabled()).forEach(guiComponent -> guiComponent.mouseMove(e));
    }

    public void drag(MouseEvent e) {
        guiComponents.stream().filter(guiComponent -> !guiComponent.getDisabled()).forEach(guiComponent -> guiComponent.mouseDrag(e));
    }

    public void keyPress(KeyEvent e) {
        guiComponents.stream().filter(guiComponent -> !guiComponent.getDisabled()).forEach(guiComponent -> guiComponent.keyPressedSC(e));
    }

    public void mouseWheel(MouseWheelEvent e) {
        guiComponents.stream().filter(guiComponent -> !guiComponent.getDisabled()).forEach(guiComponent -> guiComponent.mouseWheel(e));
    }

    public void mousePress(MouseEvent e) {
        guiComponents.stream().filter(guiComponent -> !guiComponent.getDisabled()).forEach(guiComponent -> guiComponent.mousePressSC(e));
    }

    public void mouseRelease(MouseEvent e) {
        guiComponents.stream().filter(guiComponent -> !guiComponent.getDisabled()).forEach(guiComponent -> guiComponent.mouseReleaseSC(e));
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

    public static CameraControl getCameraControl() {
        return cc;
    }

    @SuppressWarnings("unused")
    public static CameraControl getCanvasInfo() {
        return cc;
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

    public static void setTool(int tool) {
        canvas.setSelectedTool(tool);
    }

    public static void clearCanvas() {
        int clear = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear the canvas", "Clear dialogue", JOptionPane.YES_NO_OPTION);
        if (clear == 0) {
            System.out.println("clearing canvas");
            canvas.clearCanvas();
        }
    }

    public static void setDisplayImage() {
        canvas.setDisplayPicture();
    }

    public static void turnOnSettings() {
        settings.setShowAxis(true);
        settings.setShowGrid(true);
        settings.setShowCoords(true);
    }

    public static void loadProjectIntoCanvas(Project p) {
        canvas = new Canvas(p.getSide() + 1, p.getCanvasHeight() + 1);
        int r;
        int g;
        int b;
        for (int x = 0; x < p.getSide(); x++) {
            for (int y = 0; y < p.getSide(); y++) {
                for (int z = 0; z < p.getCanvasHeight(); z++) {
                    if (p.getCubeData()[z][x][y] >= 0) {
                        r = (p.getCubeData()[z][x][y] & 0xff0000) >> 16;
                        g = (p.getCubeData()[z][x][y] & 0xff00) >> 8;
                        b = (p.getCubeData()[z][x][y] & 0xff);
                        canvas.setCube(x, y, z, r, g, b);
                    }
                }
            }
        }
    }

    public static void removeCubeAt(int x, int y, int z) {
        canvas.removeCubeAt(x, y, z);
    }

    public static void newCanvas(ComponentManager cm, int side, int height) {
        canvas = new Canvas(side, height);
        cm.guiComponents.set(0,canvas);
    }

    public static void setVoxel(int x, int y, int z, int red, int green, int blue) {
        canvas.setCube(x, y, z, red, green, blue);
    }

    public static void fillNextLayer() {
        canvas.fillNextLayer();
    }
}
