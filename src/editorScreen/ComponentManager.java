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
    private static CanvasDataManager cdm;
    public static Color bgColor = new Color(55, 55, 55);
    public static Settings settings;
    private static SettingsWindow sw;
    private static HelpWindow hw;

    public ComponentManager() {
        settings = new Settings();
        guiComponents = new ArrayList<>();
        canvas = new Canvas(17, 17);
        colorWheel = new ColorWheel(EditorScreen.s_maxWidth * (1 - MU.getPercent(315, 1920)), EditorScreen.s_maxWidth * MU.getPercent(300, 1920), bgColor);
        cc = new CameraControl(EditorScreen.s_maxWidth * (1 - MU.getPercent(315, 1920)), EditorScreen.s_maxHeight * MU.getPercent(EditorScreen.s_maxWidth * MU.getPercent(300, 1920) * 1.4 - 1080 - 8, 1080), EditorScreen.s_maxWidth * MU.getPercent(250, 1920), bgColor);
        cm = new CanvasManipulator(EditorScreen.s_maxWidth * (MU.getPercent(315, 1920)), EditorScreen.s_maxWidth * (1 - MU.getPercent(315, 1920)), EditorScreen.s_maxWidth * MU.getPercent(500, 1920) * 0.3, bgColor);
        cdm = new CanvasDataManager(EditorScreen.s_maxWidth * (MU.getPercent(315, 1920)), EditorScreen.s_maxWidth * (1 - MU.getPercent(315, 1920)), EditorScreen.s_maxWidth * MU.getPercent(500, 1920) * 0.3, bgColor);
        sw = new SettingsWindow(0, 0, 0, 0, bgColor);
        hw = new HelpWindow(0, 0, 0, 0);
        addComponent(canvas);
        addComponent(cdm);
        addComponent(colorWheel);
        addComponent(cc);
        addComponent(cm);
        addComponent(sw);
        addComponent(hw);
    }

    public static CanvasManipulator getCanvasManipulator() {
        return cm;
    }

    public static CanvasDataManager getCanvasDataManger() {
        return cdm;
    }

    public static SettingsWindow getSettingsWindow() {
        return sw;
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

    public static void setRotateCamera(double direction) {
        if (direction > 360) {
            direction = 0;
        }
        canvas.setRotate(direction);
    }

    public static void setRotateyCamera(double direction) {
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

    public static void turnOnSettings() {
        settings.setShowAxis(true);
        settings.setShowGrid(true);
        settings.setShowCoords(true);
        settings.setShowSelectedArea(true);
    }

    static void turnOffSettings() {
        settings.setShowAxis(false);
        settings.setShowGrid(false);
        settings.setShowCoords(false);
        settings.setShowSelectedArea(false);
    }

    public static void loadProjectIntoCanvas(ComponentManager cm, Project p) {
        Canvas c = new Canvas(p.getSide(), p.getCanvasHeight());
        int r, g, b;
        for (int x = 0; x < p.getSide() - 1; x++) {
            for (int y = 0; y < p.getSide() - 1; y++) {
                for (int z = 0; z < p.getCanvasHeight() - 1; z++) {
                    System.out.print(Integer.toHexString(p.getCubeData()[z][x][y]) + "\t");
                    if (p.getCubeData()[z][x][y] >= 0) {
                        r = (p.getCubeData()[z][x][y] & 0xff0000) >> 16; //extracts the red component of a colour
                        g = (p.getCubeData()[z][x][y] & 0xff00) >> 8;   //extracts the green component of a colour
                        b = (p.getCubeData()[z][x][y] & 0xff);          //extracts the blue component of a colour
                        c.setCube(x, y, z, r, g, b);
                    }
                }
                System.out.println();
            }
            System.out.println("---------------------------------------------------------------------");
        }
        c.setBuffer(p.getCubeData());
        canvas = c;
        cm.guiComponents.set(0, canvas);
    }

    static void newCanvas(ComponentManager cm, int side, int height) {
        canvas = new Canvas(side, height);
        cm.guiComponents.set(0, canvas);
    }

    static void addLayer() {
        int zc = 0;

        for (int z = 0; z < canvas.getCanvasHeight() - 1; z++) {
            for (int x = 0; x < canvas.getSide() - 1; x++) {
                for (int y = 0; y < canvas.getSide() - 1; y++) {
                    if (ComponentManager.getCanvas().checkForCube(x, y, zc)) {
                        zc++;
                        break;
                    }
                }
            }
        }
        for (int x = 0; x < canvas.getSide() - 1; x++) {
            for (int y = 0; y < canvas.getSide() - 1; y++) {
                canvas.setCube(x, y, zc, colorWheel.getC1Red(), colorWheel.getC1Green(), colorWheel.getC1Blue());
            }
        }
    }

    static void removeLayer() {
        int zc = canvas.getCanvasHeight() - 1;
        for (int z = canvas.getCanvasHeight() - 2; z >= 0; z--) {
            for (int x = 0; x < canvas.getSide() - 1; x++) {
                for (int y = 0; y < canvas.getSide() - 1; y++) {
                    if (!ComponentManager.getCanvas().checkForCube(x, y, zc)) {
                        zc--;
                        break;
                    }
                }
            }
        }
        for (int x = 0; x < canvas.getSide() - 1; x++) {
            for (int y = 0; y < canvas.getSide() - 1; y++) {
                canvas.removeCubeAt(x, y, zc);
            }
        }
    }

    static void switchContourDefault() {
        canvas.switchContourDefault();
    }

    public static void setHoveredColor(boolean a) {
        canvas.setSamplingColours(a);
    }

    public static void minimizeAll(ComponentManager cm) {
        for (int i = 1; i < cm.guiComponents.size(); i++) {
            cm.guiComponents.get(i).minimize();
        }
    }

    public static void maximizeAll(ComponentManager cm) {
        for (int i = 1; i < cm.guiComponents.size(); i++) {
            cm.guiComponents.get(i).maximize();
        }
    }

    public static boolean isIntersectingComponent(ComponentManager cm, int mx, int my) {
        for (int i = 0; i < cm.guiComponents.size(); i++) {
            if (cm.guiComponents.get(i).getBounds().contains(mx, my) && !cm.guiComponents.get(i).getID().equals("canvas")) {
                System.out.println("intersects: " + cm.guiComponents.get(i).ID);
                return true;
            }
        }
        return false;
    }
}
