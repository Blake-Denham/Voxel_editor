package editorScreen;

import guiTools.GuiComponent;
import util.PU;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;

/**
 * Created by Blake on 7/22/2016.
 */
public class SettingsWindow extends GuiComponent {
    private guiTools.Button hideGrid, hideCoords, hideAxis;

    public SettingsWindow(double x, double y, double width, double height, Color bgColor) {

        super(x, y, width, height, bgColor, 14, true);
        try {
            final Image b5, b5a, b6, b6a, b7, b7a, b8, b9, b10;
            b5 = ImageIO.read(Main.getResource("Images/hideGrid256.png"));
            b5a = ImageIO.read(Main.getResource("Images/showGrid256.png"));
            b6 = ImageIO.read(Main.getResource("Images/hideAxis256.png"));
            b6a = ImageIO.read(Main.getResource("Images/showAxis256.png"));
            b7 = ImageIO.read(Main.getResource("Images/hideCoords256.png"));
            b7a = ImageIO.read(Main.getResource("Images/showCoords256.png"));

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

        } catch (IOException e) {
            e.printStackTrace();
        }
        setToolBarTitle("OPTIONS");

    }

    @Override
    protected void paintGuiComponent(Graphics2D g2d) {

    }

    @Override
    protected void update() {
        double y_ = 0;
        if (ComponentManager.getCanvasDataManger().getMinimized() < 0) {
            y_ = ComponentManager.getCanvasDataManger().getY() + 15 + 30;
        } else {
            y_ = ComponentManager.getCanvasDataManger().getY() + ComponentManager.getCanvasDataManger().getHeight() + 15 + 30;
        }
        setBounds(ComponentManager.getCanvasDataManger().getX(), y_, ComponentManager.getCanvasDataManger().getWidth(), 250);
        hideGrid.setBounds(PU.getXInBounds(bounds, 72, 0.2), PU.getYInBounds(bounds, 72, 0.05), 72, 72);
        hideAxis.setBounds(PU.getXInBounds(bounds, 72, 0.8), PU.getYInBounds(bounds, 72, 0.05), 72, 72);
        hideCoords.setBounds(PU.getXInBounds(bounds, 72, 0.8), PU.getYInBounds(bounds, 72, 0.5), 72, 72);

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
