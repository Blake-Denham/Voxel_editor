package editorScreen;

import backend.BoundingBox;
import guiTools.Button;
import guiTools.GuiComponent;
import guiTools.Label;
import guiTools.Slider;
import util.MU;
import util.PU;
import util.Vector3D;

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
    private Rectangle tool, subTool;
    private static HashMap<Integer, String> tools;

    static {
        tools = new HashMap<>();
        tools.put(PAINT, "PAINT");
        tools.put(SELECT, "SELECT");
        tools.put(ADD, "ADD");
        tools.put(REMOVE, "REMOVE");
    }

    //add sub components
    private Button addSphere, addCuboid, addCuboidFrame, addLayer;

    //remove sub buttons
    private Button removeSphere, removeCuboid, removeLayer, clearCanvas;

    //paint sub buttons
    private Button paintSelected, inverseSelected, contourDefaultToggle, getCubeColor;

    //select sub components
    private Button selectAll, setNewCollisionBounds;
    private Slider fillPercent;
    private guiTools.Label fillPercentLabel;

    public CanvasManipulator(double x, double width, double height, Color bgColor) {
        super(x, (double) 5, width, height, bgColor, 14, true);
        setP();
        tool = new Rectangle();
        subTool = new Rectangle();
        try {
            final Image b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19;
            b1 = ImageIO.read(Main.getResource("Images/paintBrush256.png"));
            b2 = ImageIO.read(Main.getResource("Images/select256.png"));
            b3 = ImageIO.read(Main.getResource("Images/add256.png"));
            b4 = ImageIO.read(Main.getResource("Images/remove256.png"));
            b5 = ImageIO.read(Main.getResource("Images/addCuboid256.png"));
            b6 = ImageIO.read(Main.getResource("Images/addSphere256.png"));
            b7 = ImageIO.read(Main.getResource("Images/removeCuboid256.png"));
            b8 = ImageIO.read(Main.getResource("Images/removeSphere256.png"));
            b9 = ImageIO.read(Main.getResource("Images/addCuboidFrame256.png"));
            b10 = ImageIO.read(Main.getResource("Images/fill256.png"));
            b11 = ImageIO.read(Main.getResource("Images/inverse256.png"));
            b12 = ImageIO.read(Main.getResource("Images/contour256.png"));
            b13 = ImageIO.read(Main.getResource("Images/commonColors256.png"));
            b14 = ImageIO.read(Main.getResource("Images/selectAll256.png"));
            b15 = ImageIO.read(Main.getResource("Images/clear256.png"));
            b16 = ImageIO.read(Main.getResource("Images/addLayer256.png"));
            b17 = ImageIO.read(Main.getResource("Images/removeLayer256.png"));
            b18 = ImageIO.read(Main.getResource("Images/colorPicker256.png"));
            b19 = ImageIO.read(Main.getResource("Images/collisionBoundsSetter256.png"));
            paint = new guiTools.Button(0, 0, 0, 0, b1, "paint brush (R)", () -> ComponentManager.setTool(PAINT));

            select = new guiTools.Button(0, 0, 0, 0, b2, "select (E)", () -> {
                ComponentManager.setTool(SELECT);
                if (ComponentManager.settings.isShowSelectedArea()) {
                    ComponentManager.settings.setShowSelectedArea(false);
                } else {
                    ComponentManager.settings.setShowSelectedArea(true);
                }
            });

            addCube = new guiTools.Button(0, 0, 0, 0, b3, "add (Q)", () -> ComponentManager.setTool(ADD));

            removeCube = new guiTools.Button(0, 0, 0, 0, b4, "remove (W)", () -> ComponentManager.setTool(REMOVE));

            addSphere = new Button(0, 0, 0, 0, b6, "add sphere (SHIFT + S)", () -> {
                Vector3D v1 = ComponentManager.getCanvas().getSpt1();
                Vector3D v2 = ComponentManager.getCanvas().getSpt2();
                ComponentManager.getCanvas().addSphere((int) v1.getX(), (int) v1.getY(), (int) v1.getZ(), (int) (v2.getX() - v1.getX() - 1), (int) (v2.getY() - v1.getY() - 1), (int) (v2.getZ() - v1.getZ()) - 1);
            });
            addCuboid = new Button(0, 0, 0, 0, b5, "add cuboid (SHIFT + C)", () -> {
                Vector3D v1 = ComponentManager.getCanvas().getSpt1();
                Vector3D v2 = ComponentManager.getCanvas().getSpt2();
                ComponentManager.getCanvas().addCuboid((int) v1.getX(), (int) v1.getY(), (int) v1.getZ(), (int) (v2.getX() - v1.getX()), (int) (v2.getY() - v1.getY()), (int) (v2.getZ() - v1.getZ()));

            });
            addCuboidFrame = new Button(0, 0, 0, 0, b9, "add cuboid frame (SHIFT + F)", () -> {
                Vector3D v1 = ComponentManager.getCanvas().getSpt1();
                Vector3D v2 = ComponentManager.getCanvas().getSpt2();
                ComponentManager.getCanvas().addCuboidFrame((int) v1.getX(), (int) v1.getY(), (int) v1.getZ(), (int) (v2.getX() - v1.getX() - 1), (int) (v2.getY() - v1.getY() - 1), (int) (v2.getZ() - v1.getZ() - 1));

            });
            paintSelected = new Button(0, 0, 0, 0, b10, "paint selected area (SHIFT + F)", () -> {
                Vector3D v1 = ComponentManager.getCanvas().getSpt1();
                Vector3D v2 = ComponentManager.getCanvas().getSpt2();
                ComponentManager.getCanvas().fillSelected((int) v1.getX(), (int) v1.getY(), (int) v1.getZ(), (int) (v2.getX() - v1.getX()), (int) (v2.getY() - v1.getY()), (int) (v2.getZ() - v1.getZ()));
            });
            inverseSelected = new Button(0, 0, 0, 0, b11, "inverse selected area (SHIFT + I)", () -> {
                Vector3D v1 = ComponentManager.getCanvas().getSpt1();
                Vector3D v2 = ComponentManager.getCanvas().getSpt2();
                ComponentManager.getCanvas().inverseColors((int) v1.getX(), (int) v1.getY(), (int) v1.getZ(), (int) (v2.getX() - v1.getX()), (int) (v2.getY() - v1.getY()), (int) (v2.getZ() - v1.getZ()));
            });
            contourDefaultToggle = new Button(0, 0, 0, 0, b12, "contour/original colours (SHIFT + C)", () -> {
                if (ComponentManager.getCanvas().isNormalColours()) {
                    contourDefaultToggle.setButtonImage(b13);
                } else {
                    contourDefaultToggle.setButtonImage(b12);
                }
                ComponentManager.switchContourDefault();
            });
            removeCuboid = new Button(0, 0, 0, 0, b7, "remove cuboid (SHIFT + C)", () -> {
                Vector3D v1 = ComponentManager.getCanvas().getSpt1();
                Vector3D v2 = ComponentManager.getCanvas().getSpt2();
                ComponentManager.getCanvas().removeCuboid((int) v1.getX(), (int) v1.getY(), (int) v1.getZ(), (int) (v2.getX() - v1.getX()), (int) (v2.getY() - v1.getY()), (int) (v2.getZ() - v1.getZ()));
            });
            removeSphere = new Button(0, 0, 0, 0, b8, "remove sphere (SHIFT + S)", () -> {
                Vector3D v1 = ComponentManager.getCanvas().getSpt1();
                Vector3D v2 = ComponentManager.getCanvas().getSpt2();
                ComponentManager.getCanvas().removeSphere((int) v1.getX(), (int) v1.getY(), (int) v1.getZ(), (int) (v2.getX() - v1.getX() - 1), (int) (v2.getY() - v1.getY() - 1), (int) (v2.getZ() - v1.getZ()) - 1);
            });
            selectAll = new Button(0, 0, 0, 0, b14, "select all (SHIFT + A)", () -> ComponentManager.getCanvas().selectAll());
            fillPercent = new Slider(0, 0, Slider.HORIZONTAL, 0, 0, new Color(130, 130, 130), new Color(20, 20, 20));
            fillPercentLabel = new Label(0, 0, 0);
            clearCanvas = new guiTools.Button(0, 0, 0, 0, b15, "clear canvas (SHIFT + D)", ComponentManager::clearCanvas);
            addLayer = new guiTools.Button(0, 0, 0, 0, b16, "add layer (SHIFT + L)", ComponentManager::addLayer);
            removeLayer = new guiTools.Button(0, 0, 0, 0, b17, "remove layer (SHIFT + L)", ComponentManager::removeLayer);
            getCubeColor = new Button(0, 0, 0, 0, b18, "Colour sampler (SHIFT + P)", () -> ComponentManager.setHoveredColor(!ComponentManager.getCanvas().isSampling()));
            setNewCollisionBounds = new Button(0, 0, 0, 0, b19, "add collision box (SHIFT + C)", () -> {
                Vector3D v1 = new Vector3D(0, 0, 0);
                v1.set((int) ComponentManager.getCanvas().getSpt1().getX(), (int) ComponentManager.getCanvas().getSpt1().getY(), (int) ComponentManager.getCanvas().getSpt1().getZ());
                Vector3D v2 = new Vector3D(0, 0, 0);
                v2.set((int) ComponentManager.getCanvas().getSpt2().getX(), (int) ComponentManager.getCanvas().getSpt2().getY(), (int) ComponentManager.getCanvas().getSpt2().getZ());
                ComponentManager.getCanvas().getCollisionMap().addCollisionBox(new BoundingBox(v1, v2));
            });
            add(addCube);
            add(removeCube);
            add(select);
            add(paint);
            add(addSphere);
            add(addCuboid);
            add(addCuboidFrame);
            add(paintSelected);
            add(inverseSelected);
            add(contourDefaultToggle);
            add(removeSphere);
            add(removeCuboid);
            add(selectAll);
            add(fillPercent);
            add(fillPercentLabel);
            add(clearCanvas);
            add(addLayer);
            add(removeLayer);
            add(getCubeColor);
            add(setNewCollisionBounds);
        } catch (IOException e) {

            e.printStackTrace();
        }

        setToolBarTitle("TOOLS");
        setID("canvas manipulator");
    }

    @Override
    protected void paintGuiComponent(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);
        g2d.drawLine((int) (paint.getX() + paint.getWidth() + 10), (int) y, (int) (paint.getX() + paint.getWidth() + 10), (int) (y + height * 0.95));
        g2d.draw(tool);
        g2d.draw(subTool);
    }

    @Override
    protected void update() {
        setBounds(EditorScreen.s_maxWidth * (MU.getPercent(332, 1920)), 10 + 26, EditorScreen.s_maxWidth * (1 - MU.getPercent(680 + 58, 1920)), EditorScreen.s_maxWidth * MU.getPercent(500, 1920) * 0.2);
        addCube.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.02), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        removeCube.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.12), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        select.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.22), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        paint.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.32), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
        if (ComponentManager.getCanvas().getSelectedTool() == ADD) {
            addSphere.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.43), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            addCuboid.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.53), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            addCuboidFrame.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.63), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            addLayer.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.73), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            tool.setBounds((int) addCube.getX() - 2, (int) addCube.getY() - 2, (int) addCube.getWidth() + 4, (int) addCube.getHeight() + 4);
        } else {
            addSphere.hide();
            addCuboid.hide();
            addCuboidFrame.hide();
            addLayer.hide();
        }

        if (ComponentManager.getCanvas().getSelectedTool() == PAINT) {
            paintSelected.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.43), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            inverseSelected.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.53), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            contourDefaultToggle.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.63), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            getCubeColor.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.73), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            tool.setBounds((int) paint.getX() - 2, (int) paint.getY() - 2, (int) paint.getWidth() + 4, (int) paint.getHeight() + 4);
            if (ComponentManager.getCanvas().isSampling()) {
                subTool.setBounds((int) getCubeColor.getX() - 2, (int) getCubeColor.getY() - 2, (int) getCubeColor.getWidth() + 4, (int) getCubeColor.getHeight() + 4);
            } else {
                subTool.setBounds(0, 0, 0, 0);
            }
        } else {
            paintSelected.hide();
            inverseSelected.hide();
            contourDefaultToggle.hide();
            getCubeColor.hide();
            subTool.setBounds(0, 0, 0, 0);
        }

        if (ComponentManager.getCanvas().getSelectedTool() == REMOVE) {
            removeSphere.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.43), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            removeCuboid.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.53), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            clearCanvas.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.63), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            removeLayer.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.73), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            tool.setBounds((int) removeCube.getX() - 2, (int) removeCube.getY() - 2, (int) removeCube.getWidth() + 4, (int) removeCube.getHeight() + 4);
        } else {
            removeCuboid.hide();
            removeSphere.hide();
            clearCanvas.hide();
            removeLayer.hide();
        }

        if (ComponentManager.getCanvas().getSelectedTool() == SELECT) {
            selectAll.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.43), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            fillPercent.setBounds(PU.getXInBounds(bounds, 100, 0.558), PU.getYInBounds(bounds, 20, 0.75), 100, 20);
            fillPercentLabel.setBounds(PU.getXInBounds(bounds, 100, 0.555), PU.getYInBounds(bounds, 20, 0.25), 105, 20);
            fillPercentLabel.setDisplay("Opacity: " + (int) (fillPercent.getPercent() * 100) + "%");
            setNewCollisionBounds.setBounds(PU.getXInBounds(bounds, height * 0.9, 0.678), PU.getYInBounds(bounds, height * 0.9, 0.5), height * 0.9, height * 0.9);
            tool.setBounds((int) select.getX() - 2, (int) select.getY() - 2, (int) select.getWidth() + 4, (int) select.getHeight() + 4);
        } else {
            selectAll.hide();
            fillPercent.hide();
            fillPercentLabel.hide();
            setNewCollisionBounds.hide();

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

    public double getFillPercent() {
        return fillPercent.getPercent();
    }

    public Button getContourDefaultToggle() {
        return contourDefaultToggle;
    }
}
