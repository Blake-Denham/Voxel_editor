package editorScreen;

import guiTools.Button;
import guiTools.GuiComponent;
import util.MU;
import util.PU;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Blake on 5/16/2016.
 */
public class CanvasManipulator extends GuiComponent {

    private guiTools.Button paint, select, addCube, removeCube;
    public static final int PAINT = 0;
    public static final int SELECT = 1;
    public static final int ADD = 2;
    public static final int REMOVE = 3;

    private static HashMap<Integer, String> tools;

    static {
        tools = new HashMap<>();
        tools.put(PAINT, "PAINT");
        tools.put(SELECT, "SELECT");
        tools.put(ADD, "ADD");
        tools.put(REMOVE, "REMOVE");
    }

    //add sub components
    private Button addSphere, addCuboid, addCuboidFrame;

    //remove sub buttons
    private Button removeSphere, removeCuboid;

    //paint sub buttons
    private Button paintSelected, inverseSelected, contourDefaultToggle;

    //select sub buttons
    private Button selectAll;

    public CanvasManipulator(double x, double y, double width, double height, Color bgColor) {
        super(x, y, width, height, bgColor, 14, true);
        setP(0.995);


        try {
            final Image b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13;
            b1 = ImageIO.read(Main.getResource("Images/paintBrush.png"));
            b2 = ImageIO.read(Main.getResource("Images/select.png"));
            b3 = ImageIO.read(Main.getResource("Images/add.png"));
            b4 = ImageIO.read(Main.getResource("Images/remove.png"));
            b5 = ImageIO.read(Main.getResource("Images/addCuboid.png"));
            b6 = ImageIO.read(Main.getResource("Images/addSphere.png"));
            b7 = ImageIO.read(Main.getResource("Images/removeCuboid.png"));
            b8 = ImageIO.read(Main.getResource("Images/removeSphere.png"));
            b9 = ImageIO.read(Main.getResource("Images/addCuboidFrame.png"));
            b10 = ImageIO.read(Main.getResource("Images/fill.png"));
            b11 = ImageIO.read(Main.getResource("Images/inverse.png"));
            b12 = ImageIO.read(Main.getResource("Images/contour.png"));
            b13 = ImageIO.read(Main.getResource("Images/commonColors.png"));

            paint = new guiTools.Button(0, 0, 0, 0, b1, "paint brush", () -> ComponentManager.setTool(PAINT));
            add(paint);
            select = new guiTools.Button(0, 0, 0, 0, b2, "select", () -> ComponentManager.setTool(SELECT));
            add(select);
            addCube = new guiTools.Button(0, 0, 0, 0, b3, "add", () -> ComponentManager.setTool(ADD));
            add(addCube);
            removeCube = new guiTools.Button(0, 0, 0, 0, b4, "remove", () -> ComponentManager.setTool(REMOVE));
            add(removeCube);
            addSphere = new Button(0, 0, 0, 0, b6, "add sphere", () -> {

            });
            addCuboid = new Button(0, 0, 0, 0, b5, "add cuboid", () -> {

            });
            addCuboidFrame = new Button(0, 0, 0, 0, b9, "add cuboid frame", () -> {

            });
            paintSelected = new Button(0, 0, 0, 0, b10, "paint selected area", () -> {

            });
            inverseSelected = new Button(0, 0, 0, 0, b11, "inverse selected area", () -> {

            });
            contourDefaultToggle = new Button(0, 0, 0, 0, b12, "contour/original colours", () -> {

            });
            removeCuboid = new Button(0, 0, 0, 0, b7, "delete cuboid", () -> {

            });
            removeSphere = new Button(0, 0, 0, 0, b8, "delete sphere", () -> {

            });
            add(addSphere);
            add(addCuboid);
            add(addCuboidFrame);
            add(paintSelected);
            add(inverseSelected);
            add(contourDefaultToggle);
            add(removeSphere);
            add(removeCuboid);
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
        setBounds(EditorScreen.s_maxWidth * (MU.getPercent(332, 1920)), 10 + 26, EditorScreen.s_maxWidth * (1 - MU.getPercent(680 + 58, 1920)), EditorScreen.s_maxWidth * MU.getPercent(500, 1920) * 0.2);
        paint.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.02), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        select.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.12), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        addCube.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.22), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        removeCube.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.32), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        if (ComponentManager.getCanvas().getSelectedTool() == ADD) {
            addSphere.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.43), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            addCuboid.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.53), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            addCuboidFrame.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.63), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        } else {
            addSphere.setBounds(0, 0, 0, 0);
            addCuboid.setBounds(0, 0, 0, 0);
            addCuboidFrame.setBounds(0, 0, 0, 0);
        }

        if (ComponentManager.getCanvas().getSelectedTool() == PAINT) {
            paintSelected.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.43), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            inverseSelected.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.53), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            contourDefaultToggle.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.63), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        } else {
            paintSelected.setBounds(0, 0, 0, 0);
            inverseSelected.setBounds(0, 0, 0, 0);
            contourDefaultToggle.setBounds(0, 0, 0, 0);
        }
        if (ComponentManager.getCanvas().getSelectedTool() == REMOVE) {
            removeSphere.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.43), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            removeCuboid.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.53), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        } else {
            removeCuboid.setBounds(0, 0, 0, 0);
            removeSphere.setBounds(0, 0, 0, 0);
        }
        setToolBarTitle("TOOLS: " + tools.get(ComponentManager.getCanvas().getSelectedTool()));

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
