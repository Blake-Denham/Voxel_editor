package editorScreen;

import guiTools.GuiComponent;
import util.MU;
import util.PU;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;

/**
 * Created by Blake on 5/16/2016.
 */
public class CanvasManipulator extends GuiComponent {

    private guiTools.Button paint, select, addCube, removeCube;
    public static final int PAINT = 0;
    public static final int SELECT = 1;
    public static final int ADD = 2;
    public static final int REMOVE = 3;

    private guiTools.Button hideGrid, hideCoords, hideAxis, clearCanvas;

    public CanvasManipulator(double x, double y, double width, double height, Color bgColor) {
        super(x, y, width, height, bgColor, 14, true);
        setP(0.995);


        try {
            final Image b1, b2, b3, b4, b5, b5a, b6, b6a, b7, b7a, b8;
            b1 = ImageIO.read(Main.getResource("Images/paintBrush.png"));
            b2 = ImageIO.read(Main.getResource("Images/select.png"));
            b3 = ImageIO.read(Main.getResource("Images/add.png"));
            b4 = ImageIO.read(Main.getResource("Images/remove.png"));
            b5 = ImageIO.read(Main.getResource("Images/hideGrid.png"));
            b5a = ImageIO.read(Main.getResource("Images/showGrid.png"));
            b6 = ImageIO.read(Main.getResource("Images/hideAxis.png"));
            b6a = ImageIO.read(Main.getResource("Images/showAxis.png"));
            b7 = ImageIO.read(Main.getResource("Images/hideCoords.png"));
            b7a = ImageIO.read(Main.getResource("Images/showCoords.png"));
            b8 = ImageIO.read(Main.getResource("Images/clear.png"));
            paint = new guiTools.Button(0, 0, 0, 0, b1, "paint brush", () -> ComponentManager.setTool(PAINT));
            add(paint);
            select = new guiTools.Button(0, 0, 0, 0, b2, "select cube", () -> ComponentManager.setTool(SELECT));
            add(select);
            addCube = new guiTools.Button(0, 0, 0, 0, b3, "add cube", () -> ComponentManager.setTool(ADD));
            add(addCube);
            removeCube = new guiTools.Button(0, 0, 0, 0, b4, "remove cube", () -> ComponentManager.setTool(REMOVE));
            add(removeCube);
            hideGrid = new guiTools.Button(0, 0, 0, 0, b5, "hide/show grid", () -> {
                if (ComponentManager.settings.isShowGrid()) {
                    ComponentManager.settings.setShowGrid(false);
                    hideGrid.setButtonImage(b5a);
                } else {
                    ComponentManager.settings.setShowGrid(true);
                    hideGrid.setButtonImage(b5);
                }
            });
            add(hideGrid);
            hideAxis = new guiTools.Button(0, 0, 0, 0, b6, "hide/show axis", () -> {
                if (ComponentManager.settings.isShowAxis()) {
                    ComponentManager.settings.setShowAxis(false);
                    hideAxis.setButtonImage(b6a);

                } else {
                    ComponentManager.settings.setShowAxis(true);
                    hideAxis.setButtonImage(b6);
                }
            });
            add(hideAxis);
            hideCoords = new guiTools.Button(0, 0, 0, 0, b7, "hide/show co-ordinates", () -> {
                if (ComponentManager.settings.isShowCoords()) {
                    ComponentManager.settings.setShowCoords(false);
                    hideCoords.setButtonImage(b7a);
                } else {
                    ComponentManager.settings.setShowCoords(true);
                    hideCoords.setButtonImage(b7);
                }
            });
            add(hideCoords);
            clearCanvas = new guiTools.Button(0, 0, 0, 0, b8, "clear canvas", ComponentManager::clearCanvas);
            add(clearCanvas);
        } catch (IOException e) {

            e.printStackTrace();
        }

        setToolBarTitle("TOOLS");
    }

    @Override
    protected void paintGuiComponent(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);
        g2d.drawLine((int) (removeCube.getX() + removeCube.getWidth() + 10), (int) y, (int) (removeCube.getX() + removeCube.getWidth() + 10), (int) (y + height * 0.95));
    }

    @Override
    protected void update() {
        setBounds(EditorScreen.s_maxWidth * (MU.getPercent(340, 1920)), 10 + 26, EditorScreen.s_maxWidth * (1 - MU.getPercent(680, 1920)), EditorScreen.s_maxWidth * MU.getPercent(500, 1920) * 0.2);
        paint.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.02), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        select.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.12), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        addCube.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.22), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        removeCube.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.32), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        hideGrid.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.42), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        hideAxis.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.52), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        hideCoords.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.62), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        clearCanvas.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.72), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);

    }

    @Override
    protected void hover(MouseEvent e) {

    }

    @Override
    protected void drag(MouseEvent e) {

    }

    @Override
    protected void scroll(MouseWheelEvent mwe) {

    }

    @Override
    protected void keyPress(KeyEvent e) {

    }

    @Override
    protected void mousePress(MouseEvent e) {

    }

    @Override
    protected void mouseRelease(MouseEvent e) {

    }
}
