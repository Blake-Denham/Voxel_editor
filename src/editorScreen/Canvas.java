package editorScreen;

import backend.BoundingBox;
import backend.CollisionMap;
import backend.Cube;
import backend.Grid;
import guiTools.GuiComponent;
import org.jetbrains.annotations.NotNull;
import util.MU;
import util.PU;
import util.Vector3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Canvas extends GuiComponent {
    @NotNull
    private final Grid grid;    //the 3D space and grid which is used to display cubes, see the Grid class
    private Cube[][][] cubes;   //stores the cube data of the canvas
    private int[][][] normalBuffer;
    private final @NotNull Rectangle pnt;//used for user
    private int side, height;   // the sides length and height of the canvas
    private int hx, hy, hz;
    private Rectangle displayPicture;//used for capturing screenshots
    private boolean square;
    private boolean shiftSquare;
    private boolean half;
    private static boolean detected;//used for paint efficiency
    private final @NotNull PaintEvent paintX;// an interface object, which is used for order of painting
    private final @NotNull PaintEvent paintY;// an interface object, which is used for order of painting to ensure no
    private final @NotNull SortEvent searchX;//an interface object, which is used for order of searching for which cube is being hovered on/clicked on;
    private final @NotNull SortEvent searchY;//an interface object, which is used for order of searching for which cube is being hovered on/clicked on;
    private static int mx, my, mb;//mouse location and which button on the mouse is clicked
    private static int selectedTool = CanvasManipulator.ADD;// the currently selected tool
    private Vector3D spt1, spt2;// two corners of a selected volume
    private boolean isNormalColour = true;
    private boolean isSampling = false;
    private CollisionMap collisionMap;
    private boolean showingPoints = false;

    public Canvas(int side, int height) {  //constructor
        super(0, 0, EditorScreen.s_maxWidth, EditorScreen.s_maxHeight); // the canvas counts as a gui component
        grid = new Grid(side, height, EditorScreen.s_maxWidth / 2, EditorScreen.s_maxHeight / 2 + 150);//instantiates the grid in the centre of the screen
        cubes = new Cube[height - 1][side - 1][side - 1];//instantiates the cube array according the grid
        normalBuffer = new int[height - 1][side - 1][side - 1];
        pnt = new Rectangle(0, 0, 1, 1);// instantiates the rectangle used for user input
        square = MU.makeSquareB(false, (int) grid.getRotate(), 360);//see the MU(math utilities) class for information
        shiftSquare = MU.makeSquareB(true, (int) grid.getRotate(), 360);//""    ""  ""  ""  ""  ""  ""  ""  ""
        half = MU.makeHalvesB(true, (int) grid.getRotate(), 360);//""   ""  ""  ""  ""  ""  ""  ""  ""  ""  ""
        this.side = side;
        this.height = height;
        spt1 = new Vector3D(0, 0, 0);
        spt2 = new Vector3D(this.side - 1, this.side - 1, this.height - 1);
        paintY = (PaintEvent e, int z, int y, Graphics2D g2d) -> {
            if (!square) {                                      // depending on the angle of the camera the order which the cubes of the cubes are painted changes
                for (int yi = 0; yi < grid.getSide() - 1; yi++) {
                    e.event(null, z, yi, g2d);
                }
            } else {
                for (int yi = grid.getSide() - 2; yi >= 0; yi--) {
                    e.event(null, z, yi, g2d);
                }
            }
        };
        paintX = (PaintEvent e, int z, int y, Graphics2D g2d) -> {
            if (!shiftSquare) {                                 // depending on the angle of the camera the order which the cubes of the cubes are painted changes
                for (int xi = 0; xi < grid.getSide() - 1; xi++) {
                    if (!(cubes[z][xi][y] == null)) {
                        cubes[z][xi][y].fillCube(g2d);
                    }
                }
            } else {
                for (int xi = grid.getSide() - 2; xi >= 0; xi--) {
                    if (!(cubes[z][xi][y] == null)) {
                        cubes[z][xi][y].fillCube(g2d);
                    }
                }
            }
        };

        searchY = (SortEvent e, int z, int y) -> {
            if (square) {                                      // depending on the angle of the camera the order which the cubes of the cubes are painted changes
                for (int yi = 0; yi < grid.getSide() - 1; yi++) {
                    e.event(null, z, yi);
                    if (detected) {
                        break;
                    }
                }
            } else {
                for (int yi = grid.getSide() - 2; yi >= 0; yi--) {
                    e.event(null, z, yi);
                    if (detected) {
                        break;
                    }
                }
            }
        };
        searchX = (SortEvent e, int z, int y) -> {
            if (shiftSquare) {                                 // depending on the angle of the camera the order which the cubes of the cubes are painted changes
                for (int xi = 0; xi < grid.getSide() - 1; xi++) {
                    if (!(cubes[z][xi][y] == null)) {
                        if (cubes[z][xi][y].contains(mx, my)) {
                            detected = true;
                            hx = xi;
                            hy = y;
                            hz = z;
                        }
                    }
                }
            } else {
                for (int xi = grid.getSide() - 2; xi >= 0; xi--) {
                    if (!(cubes[z][xi][y] == null)) {
                        if (cubes[z][xi][y].contains(mx, my)) {
                            detected = true;
                            hx = xi;
                            hy = y;
                            hz = z;
                        }
                    }
                }
            }
        };
        displayPicture = new Rectangle();
        collisionMap = new CollisionMap();
        setID("canvas");
    }

    private void addCubeFromClick(int x, int y, int z) {
        int r, g, b;
        if (isNormalColour) {
            r = ComponentManager.getColorWheel().getC1Red();
            g = ComponentManager.getColorWheel().getC1Green();
            b = ComponentManager.getColorWheel().getC1Blue();
        } else {
            r = (int) (255.0 * z / (height - 1.0));
            g = (int) (255.0 * z / (height - 1.0));
            b = (int) (255.0 * z / (height - 1.0));
        }
        if (Cube.getFace() == 0) {
            if (grid.getRotateY() >= 0) {
                setCube(x, y, z + 1, r, g, b);
                if (checkIfInBounds(x, y, z + 1))
                    normalBuffer[z + 1][x][y] = cubes[z + 1][x][y].getColorHex();
            } else {
                setCube(x, y, z - 1, r, g, b);
                if (checkIfInBounds(x, y, z - 1))
                    normalBuffer[z - 1][x][y] = cubes[z - 1][x][y].getColorHex();
            }
        } else if (Cube.getFace() == 1) {
            if (!square) {
                setCube(x, y + 1, z, r, g, b);
                if (checkIfInBounds(x, y + 1, z))
                    normalBuffer[z][x][y + 1] = cubes[z][x][y + 1].getColorHex();
            } else {
                setCube(x, y - 1, z, r, g, b);
                if (checkIfInBounds(x, y - 1, z))
                    normalBuffer[z][x][y - 1] = cubes[z][x][y - 1].getColorHex();
            }
        } else if (Cube.getFace() == 2) {
            if (!shiftSquare) {
                setCube(x + 1, y, z, r, g, b);
                if (checkIfInBounds(x + 1, y, z))
                    normalBuffer[z][x + 1][y] = cubes[z][x + 1][y].getColorHex();
            } else {
                setCube(x - 1, y, z, r, g, b);
                if (checkIfInBounds(x - 1, y, z))
                    normalBuffer[z][x - 1][y] = cubes[z][x - 1][y].getColorHex();
            }

        }
    }

    private void paintZ(@NotNull PaintEvent e, Graphics2D g2d) {// the final dimension of ordering the cubes to be painted
        if (grid.getRotateY() < 0) {
            for (int zi = grid.getHeight() - 2; zi > -1; zi--) {
                e.event(paintX, zi, 0, g2d);
            }
            if (ComponentManager.settings.isShowGrid())
                grid.paint(g2d);
        } else {
            if (ComponentManager.settings.isShowGrid())
                grid.paint(g2d);
            for (int zi = 0; zi < grid.getHeight() - 1; zi++) {
                e.event(paintX, zi, 0, g2d);
            }
        }
    }

    private void searchZ(@NotNull SortEvent e) {
        detected = false;
        if (!(grid.getRotateY() < 0)) {
            for (int zi = grid.getHeight() - 2; zi > -1; zi--) {
                e.event(searchX, zi, 0);
                if (detected) {
                    break;
                }
            }
        } else {
            for (int zi = 0; zi < grid.getHeight() - 1; zi++) {
                e.event(searchX, zi, 0);
                if (detected) {
                    break;
                }
            }
        }
    }

    public int getSelectedTool() {
        return selectedTool;
    }

    public void selectAll() {
        spt1.set(0, 0, 0);
        spt2.set(this.side - 1, this.side - 1, this.height - 1);
    }

    public void switchContourDefault() {
        if (isNormalColour) {
            for (int x = 0; x < side - 1; x++) {
                for (int y = 0; y < side - 1; y++) {
                    for (int z = 0; z < height - 1; z++) {
                        if (checkForCube(x, y, z)) {
                            normalBuffer[z][x][y] = cubes[z][x][y].getColorHex();
                            cubes[z][x][y].setColors(PU.saturateColor(new Color(255, 255, 255), (z / (height - 1.0))));
                        }
                    }
                }
            }

        } else {
            for (int x = 0; x < side - 1; x++) {
                for (int y = 0; y < side - 1; y++) {
                    for (int z = 0; z < height - 1; z++) {
                        if (checkForCube(x, y, z)) {
                            cubes[z][x][y].setColors(new Color(normalBuffer[z][x][y]));
                        }
                    }
                }
            }
        }
        isNormalColour = !isNormalColour;
    }

    public void inverseColors(int x, int y, int z, int width, int length, int height) {
        for (int xi = x; xi < x + width; xi++) {
            for (int yi = y; yi < y + length; yi++) {
                for (int zi = z; zi < z + height; zi++) {
                    if (checkForCube(xi, yi, zi)) {
                        if (isNormalColour) {
                            cubes[zi][xi][yi].setColors(PU.inverseColor(new Color(cubes[zi][xi][yi].getColorHex())));
                        } else {
                            normalBuffer[zi][xi][yi] = PU.inverseColor(new Color(cubes[zi][xi][yi].getColorHex())).getRGB();
                        }
                    }
                }
            }
        }
    }

    public void fillSelected(int x, int y, int z, int width, int length, int height) {
        double fillPercent = ComponentManager.getCanvasManipulator().getFillPercent();
        for (double xi = x; xi < x + width; xi += 1.0 / Math.cbrt(fillPercent)) {
            for (double yi = y; yi < y + length; yi += 1.0 / Math.cbrt(fillPercent)) {
                for (double zi = z; zi < z + height; zi += 1.0 / Math.cbrt(fillPercent)) {
                    if (checkForCube((int) Math.round(xi), (int) Math.round(yi), (int) Math.round(zi))) {
                        if (isNormalColour) {
                            cubes[(int) Math.round(zi)][(int) Math.round(xi)][(int) Math.round(yi)].setColor(ComponentManager.getColorWheel().getC1Red(), ComponentManager.getColorWheel().getC1Green(), ComponentManager.getColorWheel().getC1Blue());
                        } else {
                            normalBuffer[(int) Math.round(zi)][(int) Math.round(xi)][(int) Math.round(yi)] = new Color(ComponentManager.getColorWheel().getC1Red(), ComponentManager.getColorWheel().getC1Green(), ComponentManager.getColorWheel().getC1Blue()).getRGB();
                        }
                    }
                }
            }
        }
    }

    public void addCuboid(int x, int y, int z, int width, int length, int height) {   // creates a white cuboid in the canvas
        double fillPercent = ComponentManager.getCanvasManipulator().getFillPercent();
        for (double xi = x; xi < x + width; xi += 1.0 / Math.cbrt(fillPercent)) {
            for (double yi = y; yi < y + length; yi += 1.0 / Math.cbrt(fillPercent)) {
                for (double zi = z; zi < z + height; zi += 1.0 / Math.cbrt(fillPercent)) {
                    setCube((int) (xi), (int) (yi), (int) (zi), ComponentManager.getColorWheel().getC1Red(), ComponentManager.getColorWheel().getC1Green(), ComponentManager.getColorWheel().getC1Blue());
                }
            }
        }
    }

    public void addSphere(int x, int y, int z, double width, double length, double height) { // creates a white sphere in the canvas
        for (int i = 1; i < 20; i++) {
            for (int theta = 0; theta < 720; theta += 5) {
                for (int pi = -360; pi < 360; pi += 5) {
                    int xi = (int) (Math.round((x + width / 2) + ((width * i / 20.0) / 2 * MU.cos(theta / 2.0) * MU.cos(pi / 2.0))));
                    int yi = (int) (Math.round((y + length / 2) + ((length * i / 20.0) / 2 * MU.sin(theta / 2.0) * MU.cos(pi / 2.0))));
                    int zi = (int) (Math.round((z + height / 2) + ((height * i / 20.0) / 2 * MU.sin(pi / 2.0))));
                    setCube(xi, yi, zi, ComponentManager.getColorWheel().getC1Red(), ComponentManager.getColorWheel().getC1Green(), ComponentManager.getColorWheel().getC1Blue());
                }
            }
        }
    }

    private void drawLine(int x, int y, int z, int x1, int y1, int z1) { // draws a line from one point to another
        double h = MU.getDistance(new Vector3D(x, y, z), new Vector3D(x1, y1, z1));
        double dx, dy, dz;
        double ry, rx;
        if (x1 - x == 0) {
            rx = 90;
        } else {
            rx = Math.toDegrees(MU.arctan((y1 - y) / (x1 - x)));  // phi = atan(y/x)
        }
        ry = Math.toDegrees(MU.arccos((z1 - z) / (h)));     // thetaz = acos(z/h)
        dx = MU.cos(rx) * MU.sin(ry);                     // is the rate of change of the x dimension
        dy = MU.sin(rx) * MU.sin(ry);                     // rate of change of y dimension
        dz = MU.cos(ry);                                // rate of change of the z dimension
        for (double i = 0; i < h; i += 1) {             // follows
            setCube((int) (x + i * dx), (int) (y + i * dy), (int) (z + i * dz), ComponentManager.getColorWheel().getC1Red(), ComponentManager.getColorWheel().getC1Green(), ComponentManager.getColorWheel().getC1Blue());
        }
    }

    public void addCuboidFrame(int x, int y, int z, int width, int length, int height) {
        drawLine(x, y, z, x + width, y, z);
        drawLine(x, y, z, x, y + length, z);
        drawLine(x, y, z, x, y, z + height);
        drawLine(x + width, y + length, z, x + width, y + length, z + height + 1);
        drawLine(x + width, y, z + height, x + width, y + length + 1, z + height);
        drawLine(x + width, y, z, x + width, y, z + height);
        drawLine(x + width, y, z, x + width, y + length, z);
        drawLine(x + width, y, z + height, x + width, y + length, z + height);
        drawLine(x, y + length, z + height, x + width + 1, y + length, z + height);
        drawLine(x, y + length, z, x, y + length, z + height);
        drawLine(x, y + length, z, x + width, y + length, z);
        drawLine(x, y + length, z + height, x + width, y + length, z + height);
        drawLine(x, y, z + height, x + width, y, z + height);
        drawLine(x, y, z + height, x, y + length, z + height);
    }

    public void removeSphere(int x, int y, int z, double width, double length, double height) {
        for (int i = 1; i < 20; i++) {
            for (int theta = 0; theta < 720; theta += 5) {
                for (int pi = -360; pi < 360; pi += 5) {
                    int xi = (int) (Math.round((x + width / 2) + ((width * i / 20.0) / 2 * MU.cos(theta / 2.0) * MU.cos(pi / 2.0))));
                    int yi = (int) (Math.round((y + length / 2) + ((length * i / 20.0) / 2 * MU.sin(theta / 2.0) * MU.cos(pi / 2.0))));
                    int zi = (int) (Math.round((z + height / 2) + ((height * i / 20.0) / 2 * MU.sin(pi / 2.0))));
                    removeCubeAt(xi, yi, zi);
                }
            }
        }
    }

    public void removeCuboid(int x, int y, int z, int width, int length, int height) {
        for (int xi = x; xi < x + width; xi += 1) {
            for (int yi = y; yi < y + length; yi += 1) {
                for (int zi = z; zi < z + height; zi += 1) {
                    removeCubeAt(xi, yi, zi);
                }
            }
        }
    }

    public void setCube(int x, int y, int z, int red, int green, int blue) {// sets a single cube in the canvas with a specified colour
        if (checkIfInBounds(x, y, z)) {
            int r, g, b;
            if (isNormalColour) {
                r = red;
                g = green;
                b = blue;
            } else {
                r = (int) (255.0 * z / (height - 1));
                g = (int) (255.0 * z / (height - 1));
                b = (int) (255.0 * z / (height - 1));
            }
            cubes[z][x][y] = new Cube(this, x, y, z, r, g, b);
            normalBuffer[z][x][y] = (red << 16) | (green << 8) | blue;
        }
    }

    public void clearCanvas() { // removes all cubes in the canvas
        for (int z = 0; z < grid.getHeight() - 1; z++) {
            for (int y = 0; y < grid.getSide() - 1; y++) {
                for (int x = 0; x < grid.getSide() - 1; x++) {
                    cubes[z][x][y] = null;
                }
            }
        }
    }

    private boolean checkIfInBounds(int x, int y, int z) {// checks if the given coordinates falls in the canvas
        return (!(x < 0) && !(x >= grid.getSide() - 1) && !(y < 0) && !(y >= grid.getSide() - 1) && !(z < 0) && !(z >= grid.getHeight() - 1));
    }

    public boolean checkForCube(int x, int y, int z) {// checks if there is a cube at given coordinates
        return checkIfInBounds(x, y, z) && cubes[z][x][y] != null;
    }

    private void showCoords(Graphics2D g2d) {// apart of a setting which shows the coordinates at each corner of the canvas
        int x_ = grid.getSide() - 1;
        int y_ = grid.getSide() - 1;
        int z_ = grid.getHeight() - 1;
        int side = grid.getSide() - 1;
        int height = grid.getHeight() - 1;
        g2d.setColor(Color.white);
        g2d.setFont(EditorScreen.font.deriveFont(11f));
        g2d.drawString("(z,x,y)", (int) grid.getPts()[0][0][0].getVec().getX(), (int) grid.getPts()[0][0][0].getVec().getY() - 14);
        g2d.drawString("(0,0,0)", (int) grid.getPts()[0][0][0].getVec().getX(), (int) grid.getPts()[0][0][0].getVec().getY());
        g2d.drawString("(0," + side + ",0)", (int) grid.getPts()[0][x_][0].getVec().getX(), (int) grid.getPts()[0][x_][0].getVec().getY());
        g2d.drawString("(0,0," + side + ")", (int) grid.getPts()[0][0][y_].getVec().getX(), (int) grid.getPts()[0][0][y_].getVec().getY());
        g2d.drawString("(0," + side + "," + side + ")", (int) grid.getPts()[0][x_][y_].getVec().getX(), (int) grid.getPts()[0][x_][y_].getVec().getY());
        g2d.drawString("(" + height + ",0,0)", (int) grid.getPts()[z_][0][0].getVec().getX(), (int) grid.getPts()[z_][0][0].getVec().getY());
        g2d.drawString("(" + height + "," + side + ",0)", (int) grid.getPts()[z_][x_][0].getVec().getX(), (int) grid.getPts()[z_][x_][0].getVec().getY());
        g2d.drawString("(" + height + ",0," + side + ")", (int) grid.getPts()[z_][0][y_].getVec().getX(), (int) grid.getPts()[z_][0][y_].getVec().getY());
        g2d.drawString("(" + height + "," + side + "," + side + ")", (int) grid.getPts()[z_][x_][y_].getVec().getX(), (int) grid.getPts()[z_][x_][y_].getVec().getY());
    }

    private void paintSelectedArea(Graphics2D g2d) {
        g2d.setColor(new Color(200, 200, 200));
        int z, z1, x, x1, y, y1;
        z = (int) spt1.getZ();
        z1 = (int) spt2.getZ();
        x = (int) spt1.getX();
        x1 = (int) spt2.getX();
        y = (int) spt1.getY();
        y1 = (int) spt2.getY();
        //zxy-zxy
        //000-100
        g2d.drawLine(
                (int) grid.getPts()[z][x][y].getVec().getX(),
                (int) grid.getPts()[z][x][y].getVec().getY(),
                (int) grid.getPts()[z1][x][y].getVec().getX(),
                (int) grid.getPts()[z1][x][y].getVec().getY());
        //000-010
        g2d.drawLine(
                (int) grid.getPts()[z][x][y].getVec().getX(),
                (int) grid.getPts()[z][x][y].getVec().getY(),
                (int) grid.getPts()[z][x1][y].getVec().getX(),
                (int) grid.getPts()[z][x1][y].getVec().getY());
        //000-001
        g2d.drawLine(
                (int) grid.getPts()[z][x][y].getVec().getX(),
                (int) grid.getPts()[z][x][y].getVec().getY(),
                (int) grid.getPts()[z][x][y1].getVec().getX(),
                (int) grid.getPts()[z][x][y1].getVec().getY());
        //010-110
        g2d.drawLine(
                (int) grid.getPts()[z][x1][y].getVec().getX(),
                (int) grid.getPts()[z][x1][y].getVec().getY(),
                (int) grid.getPts()[z1][x1][y].getVec().getX(),
                (int) grid.getPts()[z1][x1][y].getVec().getY());
        //001-101
        g2d.drawLine(
                (int) grid.getPts()[z][x][y1].getVec().getX(),
                (int) grid.getPts()[z][x][y1].getVec().getY(),
                (int) grid.getPts()[z1][x][y1].getVec().getX(),
                (int) grid.getPts()[z1][x][y1].getVec().getY());
        //010-011
        g2d.drawLine(
                (int) grid.getPts()[z][x1][y].getVec().getX(),
                (int) grid.getPts()[z][x1][y].getVec().getY(),
                (int) grid.getPts()[z][x1][y1].getVec().getX(),
                (int) grid.getPts()[z][x1][y1].getVec().getY());
        //011-001
        g2d.drawLine(
                (int) grid.getPts()[z][x1][y1].getVec().getX(),
                (int) grid.getPts()[z][x1][y1].getVec().getY(),
                (int) grid.getPts()[z][x][y1].getVec().getX(),
                (int) grid.getPts()[z][x][y1].getVec().getY());
        //111-011
        g2d.drawLine(
                (int) grid.getPts()[z1][x1][y1].getVec().getX(),
                (int) grid.getPts()[z1][x1][y1].getVec().getY(),
                (int) grid.getPts()[z][x1][y1].getVec().getX(),
                (int) grid.getPts()[z][x1][y1].getVec().getY());
        //111-101
        g2d.drawLine(
                (int) grid.getPts()[z1][x1][y1].getVec().getX(),
                (int) grid.getPts()[z1][x1][y1].getVec().getY(),
                (int) grid.getPts()[z1][x][y1].getVec().getX(),
                (int) grid.getPts()[z1][x][y1].getVec().getY());
        //111-110
        g2d.drawLine(
                (int) grid.getPts()[z1][x1][y1].getVec().getX(),
                (int) grid.getPts()[z1][x1][y1].getVec().getY(),
                (int) grid.getPts()[z1][x1][y].getVec().getX(),
                (int) grid.getPts()[z1][x1][y].getVec().getY());
        //100-110
        g2d.drawLine(
                (int) grid.getPts()[z1][x][y].getVec().getX(),
                (int) grid.getPts()[z1][x][y].getVec().getY(),
                (int) grid.getPts()[z1][x1][y].getVec().getX(),
                (int) grid.getPts()[z1][x1][y].getVec().getY());
        //100-101
        g2d.drawLine(
                (int) grid.getPts()[z1][x][y].getVec().getX(),
                (int) grid.getPts()[z1][x][y].getVec().getY(),
                (int) grid.getPts()[z1][x][y1].getVec().getX(),
                (int) grid.getPts()[z1][x][y1].getVec().getY());


    }

    @Override
    protected void paintGuiComponent(@NotNull Graphics2D g2d) { //overridden from guiComponent which eventually gets fed into the screen painter in the EditorScreen class
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);                  //settings for optimization
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2d.setColor(Color.black);

        if (half) {
            if (ComponentManager.settings.isShowAxis()) {
                grid.paintAxis(g2d);
            }
            paintZ(paintY, g2d);

        } else {
            if (ComponentManager.settings.isShowAxis()) {
                grid.paintAxis(g2d);
            }
            paintZ(paintY, g2d);
        }
        g2d.setColor(new Color(1f, 1f, 1f, 0.4f));

        if (ComponentManager.settings.isShowCoords()) {
            showCoords(g2d);
        }
        if (ComponentManager.settings.isShowSelectedArea()) {
            paintSelectedArea(g2d);
        }
        collisionMap.paintMap(grid, g2d);
        if (showingPoints)
            grid.showPoints(g2d);
    }

    @Override
    protected void update() {// is called every frame
        grid.update();
        pnt.setLocation(mx, my);
        square = MU.makeSquareB(false, (int) grid.getRotate(), 360);
        shiftSquare = MU.makeSquareB(true, (int) grid.getRotate(), 360);
        half = MU.makeHalvesB(true, (int) grid.getRotate(), 360);
        for (int zi = 0; zi < grid.getHeight() - 1; zi++) {
            for (int yi = 0; yi < grid.getSide() - 1; yi++) {
                for (int xi = 0; xi < grid.getSide() - 1; xi++) {
                    if (cubes[zi][xi][yi] != null)
                        cubes[zi][xi][yi].updateCube();
                }
            }
        }
        searchZ(searchY);
        if (detected) {
            if (checkForCube(hx, hy, hz)) {
                if (cubes[hz][hx][hy].contains(mx, my)) {
                    Cube.setSelected(cubes[hz][hx][hy].getFaces()[Cube.getFace()]);
                }
            }
            grid.setSelected(Cube.getEmpty());
        } else {
            Cube.setSelected(Cube.getEmpty());
            grid.gridInterfaceHover(mx, my);
        }
        if (selectedTool == CanvasManipulator.SELECT) {
            ComponentManager.settings.setShowSelectedArea(true);
        }
    }

    @Override
    protected void hover(@NotNull MouseEvent e) {// is called every frame when the mouse is moving
        mx = e.getX();
        my = e.getY();
        searchZ(searchY);
        if (detected) {
            if (checkForCube(hx, hy, hz)) {
                if (cubes[hz][hx][hy].contains(mx, my)) {
                    Cube.setSelected(cubes[hz][hx][hy].getFaces()[Cube.getFace()]);
                }
            }
            grid.setSelected(Cube.getEmpty());
        } else {
            Cube.setSelected(Cube.getEmpty());
            grid.gridInterfaceHover(mx, my);
        }
    }

    @Override
    protected void drag(@NotNull MouseEvent e) {// is called every frame every frame when the mouse is moving and a button is held down
        int dx = mx - e.getX();
        int dy = my - e.getY();

        if (mb == 2) {              //mouse wheel click
            grid.update();          // translates the canvas to the mouses location
            grid.setLocation(mx, my);
        }
        mx = e.getX();
        my = e.getY();

        if (mb == 1) {
            if (detected) {
                if (checkForCube(hx, hy, hz)) {
                    if (cubes[hz][hx][hy].contains(mx, my))
                        switch (selectedTool) {
                            case CanvasManipulator.PAINT:
                                if (!ComponentManager.isIntersectingComponent(EditorScreen.getComponentManager(), mx, my))
                                    cubes[hz][hx][hy].paint(e);
                                break;
                        }
                }
            } else {
                grid.gridInterfaceHover(mx, my);
            }
        }
        if (mb == 3) {              //right click
            grid.rotate(dx / 2);    // rotates the canvas vertically and horizontally
            grid.rotatey(-dy / 2);
        }

    }

    @Override
    protected void mousePress(@NotNull MouseEvent e) {
        mb = e.getButton();
        mx = e.getX();
        my = e.getY();
        if (mb == 1 && !ComponentManager.isIntersectingComponent(EditorScreen.getComponentManager(), mx, my)) {
            if (detected) {
                if (checkForCube(hx, hy, hz)) {
                    if (cubes[hz][hx][hy].contains(mx, my))
                        switch (selectedTool) {
                            case CanvasManipulator.ADD:
                                addCubeFromClick(hx, hy, hz);
                                break;
                            case CanvasManipulator.PAINT:
                                if (!isSampling) {
                                    cubes[hz][hx][hy].paint(e);
                                } else {
                                    ComponentManager.getColorWheel().setColor(new Color(cubes[hz][hx][hy].getColorHex()));
                                    isSampling = false;
                                }
                                break;
                            case CanvasManipulator.REMOVE:
                                cubes[hz][hx][hy] = null;
                                break;
                            case CanvasManipulator.SELECT:
                                if (e.isShiftDown()) {
                                    spt2.set(hx + 1, hy + 1, hz + 1); // know idea why I need to add one, but i do.
                                } else {
                                    spt1.set(hx, hy, hz);
                                }
                                orderSelectPoints();
                                break;
                        }
                }
            } else {
                if (grid.getSelected().contains(mx, my) && selectedTool == CanvasManipulator.ADD) {
                    setCube(grid.getSelectedX(), grid.getSelectedY(), 0, ComponentManager.getColorWheel().getC1Red(), ComponentManager.getColorWheel().getC1Green(), ComponentManager.getColorWheel().getC1Blue());
                }
                if (grid.getSelected().contains(mx, my) && selectedTool == CanvasManipulator.SELECT) {
                    if (e.isShiftDown()) {
                        spt2.set(grid.getSelectedX() + 1, grid.getSelectedY() + 1, 1);
                    } else {
                        spt1.set(grid.getSelectedX(), grid.getSelectedY(), 0);
                    }
                    orderSelectPoints();

                }
            }
        }
    }

    private void orderSelectPoints() {
        double temp;
        double x1 = spt1.getX();
        double x2 = spt2.getX();
        if (x1 > x2) {
            temp = x2;
            x2 = x1 + 1;
            x1 = temp - 1;
        }
        double y1 = spt1.getY();
        double y2 = spt2.getY();
        if (y1 > y2) {
            temp = y2;
            y2 = y1 + 1;
            y1 = temp - 1;
        }
        double z1 = spt1.getZ();
        double z2 = spt2.getZ();
        if (z1 > z2) {
            temp = z2;
            z2 = z1 + 1;
            z1 = temp - 1;
        }
        if (x2 - x1 == 0) {
            if (checkIfInBounds((int) (x2 + 1), 0, 0)) {
                x2++;
            } else {
                x1--;
            }
        }
        if (y2 - y1 == 0) {
            if (checkIfInBounds(0, (int) (y2 + 1), 0)) {
                y2++;
            } else {
                y1--;
            }
        }
        if (z2 - z1 == 0) {
            if (checkIfInBounds(0, 0, (int) (z2 + 1))) {
                z2++;
            } else {
                z1--;
            }
        }
        spt1.set((int) x1, (int) y1, (int) z1);
        spt2.set((int) x2, (int) y2, (int) z2);
    }

    @Override
    protected void mouseRelease(MouseEvent e) {

    }

    // keyboard input
    @Override
    protected void keyPress(@NotNull KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_Q) {
            selectedTool = CanvasManipulator.ADD;
        }
        if (ke.getKeyCode() == KeyEvent.VK_W) {
            selectedTool = CanvasManipulator.REMOVE;
        }
        if (ke.getKeyCode() == KeyEvent.VK_E) {
            selectedTool = CanvasManipulator.SELECT;

            if (ComponentManager.settings.isShowSelectedArea()) {
                ComponentManager.settings.setShowSelectedArea(false);
            } else {
                ComponentManager.settings.setShowSelectedArea(true);
            }
        }
        if (ke.getKeyCode() == KeyEvent.VK_R) {
            selectedTool = CanvasManipulator.PAINT;
        }
        if (ke.isShiftDown()) {
            switch (selectedTool) {
                case CanvasManipulator.ADD:
                    if (ke.getKeyCode() == KeyEvent.VK_S) {
                        addSphere(
                                (int) spt1.getX(), (int) spt1.getY(), (int) spt1.getZ(),
                                (int) (spt2.getX() - spt1.getX() - 1), (int) (spt2.getY() - spt1.getY() - 1), (int) (spt2.getZ() - spt1.getZ()) - 1);
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_C) {
                        addCuboid(
                                (int) spt1.getX(), (int) spt1.getY(), (int) spt1.getZ(),
                                (int) (spt2.getX() - spt1.getX()), (int) (spt2.getY() - spt1.getY()), (int) (spt2.getZ() - spt1.getZ()));
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_F) {
                        addCuboidFrame(
                                (int) spt1.getX(), (int) spt1.getY(), (int) spt1.getZ(),
                                (int) (spt2.getX() - spt1.getX() - 1), (int) (spt2.getY() - spt1.getY() - 1), (int) (spt2.getZ() - spt1.getZ() - 1));
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_L) {
                        ComponentManager.addLayer();
                    }
                    break;
                case CanvasManipulator.PAINT:
                    if (ke.getKeyCode() == KeyEvent.VK_F) {
                        fillSelected(
                                (int) spt1.getX(), (int) spt1.getY(), (int) spt1.getZ(),
                                (int) (spt2.getX() - spt1.getX()), (int) (spt2.getY() - spt1.getY()), (int) (spt2.getZ() - spt1.getZ()));
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_I) {
                        inverseColors(
                                (int) spt1.getX(), (int) spt1.getY(), (int) spt1.getZ(),
                                (int) (spt2.getX() - spt1.getX()), (int) (spt2.getY() - spt1.getY()), (int) (spt2.getZ() - spt1.getZ()));
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_C) {
                        switchContourDefault();
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_P) {
                        isSampling = !isSampling;
                    }
                    break;
                case CanvasManipulator.REMOVE:
                    if (ke.getKeyCode() == KeyEvent.VK_S) {
                        removeSphere(
                                (int) spt1.getX(), (int) spt1.getY(), (int) spt1.getZ(),
                                (int) (spt2.getX() - spt1.getX() - 1), (int) (spt2.getY() - spt1.getY() - 1), (int) (spt2.getZ() - spt1.getZ()) - 1);
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_C) {
                        removeCuboid(
                                (int) spt1.getX(), (int) spt1.getY(), (int) spt1.getZ(),
                                (int) (spt2.getX() - spt1.getX()), (int) (spt2.getY() - spt1.getY()), (int) (spt2.getZ() - spt1.getZ()));
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_D) {
                        int clear = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear the canvas", "Clear dialogue", JOptionPane.YES_NO_OPTION);
                        if (clear == 0) {
                            System.out.println("clearing canvas");
                            clearCanvas();
                        }
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_L) {
                        ComponentManager.removeLayer();
                    }
                    break;
                case CanvasManipulator.SELECT:
                    if (ke.getKeyCode() == KeyEvent.VK_A) {
                        selectAll();
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_C) {
                        collisionMap.addCollisionBox(new BoundingBox(spt1, spt2));
                    }
                    break;
            }
            if (ke.isControlDown() && ke.isAltDown()) {
                if (ke.getKeyCode() == KeyEvent.VK_P) {
                    showingPoints = !showingPoints;
                }
            }
        }

    }

    @Override
    protected void scroll(@NotNull MouseWheelEvent e) {
        int n = e.getWheelRotation();// checks if the user is rolling the mouse wheel forward or backwards
        grid.zoom((int) (-2.5 * n));// zooms in or out depending on the sign of n;
    }

    @NotNull
    public Grid getGrid() {
        return grid; // returns the 3d space points;
    }

    @NotNull
    public Cube[][][] getCubes() {
        return cubes; // returns the 3d array which holds the cube data
    }

    public int getSide() {
        return side; //returns the length, which equal to the width of the canvas
    }

    public int getCanvasHeight() {
        return height;// returns the height of the canvas
    }

    public void setRotate(double rotate) {
        grid.setRotate(rotate);// sets the degree at which the canvas is horizontally rotated
    }

    public void setRotatey(double rotate) {
        grid.setRotatey(rotate);// sets the degree at which the canvas is vertically rotated
    }

    public void setZoom(int zoom) {
        grid.setZoom(zoom);//sets how zoomed in or out the canvas is
    }

    public void setSelectedTool(int tool) {
        selectedTool = tool;// sets which tool will be used from the canvas manipulator class
    }

    public void setDisplayPicture() { // sets the canvas to an isometric view and creates a rectangle which is used to capture an image of the project to display later when loading another project
        grid.setRotate(45);  //sets the to isometric view
        grid.setRotatey(35); //""    ""   ""     ""  ""
        grid.setZoom((int) (40.0 / Math.sqrt(getCanvasHeight())));
        grid.setLocation(EditorScreen.s_maxWidth / 2, EditorScreen.s_maxHeight / 2 + 200);// moves the canvas to the centre of the screen
        if (isNormalColour)
            ComponentManager.getCanvasManipulator().getContourDefaultToggle().getActionEvent();
        update();
        displayPicture.setBounds(   //sets the rectangle
                (int) (grid.getPts()[0][0][side - 1].getVec().getX()),
                (int) (grid.getPts()[height - 1][0][0].getVec().getY()),
                (int) (grid.getPts()[height - 1][side - 1][0].getVec().getX() - grid.getPts()[0][0][side - 1].getVec().getX()),
                (int) (grid.getPts()[0][side - 1][side - 1].getVec().getY() - grid.getPts()[height - 1][0][0].getVec().getY()));
        ComponentManager.minimizeAll(EditorScreen.getComponentManager());
        ComponentManager.turnOffSettings();

    }

    public Rectangle getDisplayImage() {
        return displayPicture;// returns the rectangle which will be used to capture the image

    }

    public void removeCubeAt(int x, int y, int z) {
        if (checkForCube(x, y, z)) {
            if (checkIfInBounds(x, y, z)) {
                cubes[z][x][y] = null;
            }
        }
    }

    public static boolean isDetected() {
        return detected;
    }

    public Vector3D getSpt1() {
        return spt1;
    }

    public Vector3D getSpt2() {
        return spt2;
    }

    public boolean isNormalColours() {
        return isNormalColour;
    }

    public void setNormalBufferAtPoint(int x, int y, int z, int colorHex) {
        normalBuffer[z][x][y] = colorHex;
    }

    public void setBuffer(int[][][] buffer) {
        this.normalBuffer = buffer;
        int r, g, b;
        for (int x = 0; x < side - 1; x++) {
            for (int y = 0; y < side - 1; y++) {
                for (int z = 0; z < getCanvasHeight() - 1; z++) {
                    if (buffer[z][x][y] >= 0) {
                        r = (buffer[z][x][y] & 0xff0000) >> 16;
                        g = (buffer[z][x][y] & 0xff00) >> 8;
                        b = buffer[z][x][y] & 0xff;
                        setCube(x, y, z, r, g, b);
                    }
                }
            }
        }
    }

    public void setSamplingColours(boolean a) {
        isSampling = a;
    }

    public boolean isSampling() {
        return isSampling;
    }

    public CollisionMap getCollisionMap() {
        return collisionMap;
    }
}
