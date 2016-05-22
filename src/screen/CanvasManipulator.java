package screen;

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

    public CanvasManipulator(double x, double y, double width, double height, Color bgColor) {
        super(x, y, width, height, bgColor, 14, true);
        setP(0.995);
        Image b1 = null, b2 = null, b3 = null, b4 = null;
        try {
            b1 = ImageIO.read(Main.getResource("Images/paintBrush.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert b1 != null;
        paint = new guiTools.Button(PU.getXInBounds(bounds, 0.1, height * 0.9), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9, b1, () -> {
            ComponentManager.setTool(PAINT);
        });
        add(paint);
        setToolBarTitle("TOOLS");

    }

    @Override
    protected void paintGuiComponent(Graphics2D g2d) {

    }

    @Override
    protected void update() {
        setBounds(EditorScreen.s_maxWidth * (MU.getPercent(340, 1920)), 10 + 26, EditorScreen.s_maxWidth * (1 - MU.getPercent(680, 1920)), EditorScreen.s_maxWidth * MU.getPercent(500, 1920) * 0.2);
        paint.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.02), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
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
