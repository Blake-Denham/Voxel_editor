package backend;

import editorScreen.Canvas;
import editorScreen.ComponentManager;
import org.jetbrains.annotations.NotNull;
import util.MU;
import util.PU;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Cube {
    //this object will be used as a the voxel (3D pixel),

    //the x value in 3D space
    private int x;
    //the y value in 3D space
    private int y;
    //the z value in 3D space
    private int z;

    //booleans tb - stands for tobBottom - is true if (the cameras vertical rotation is greater than 0 AND there is a cube on top of this cube) OR
    // (the cameras vertical rotation is less than zero AND there is cube below this cube)
    //
    //bf - stands for backFront -  is true if (the cameras horizontal rotation is between 0 and 90 OR 270 and 360 AND there is a cube in front of this cube) OR
    //(the cameras horizontal rotation is not between 0 and 90 OR 270 and 360 AND there is a cube behind this cube)
    //
    //lr - stands for leftRight - is true if(the cameras horizontal rotation is between 0 and 180 AND there is a cube next to the left to this cube) OR
    //(the cameras horizontal rotation is not between 0 and 180 AND there is a cube to the right of the cube)
    //
    //all these are used in optimization as if they are true, hidden polygons, which are those with cubes next to a face of this cube,
    //then the program will not draw or update the hidden polygons
    private boolean tb, bf, lr;

    //used for defining squares, which are the faces of the cubes, xps represents the screen x values and yps represent the screen y values
    //the first dimension of the 2D arrays represent the corners of the square
    //the second dimension represents the face of the cube
    private int[][] xps, yps;

    //the Polygon object is from java swing, which is used for the cube faces, are defined by xps, and yps
    //each element of the array works with the each element of the second dimension of xps and yps
    private Polygon[] cube;

    //the hex value (base 16) of the colour of the cube, two 4 bytes for each red, green, blue looks like this 0xRRGGBB(R-Red, G-Green, B-Blue)
    private int colorHex;

    //each face has a slightly different colour to make seeing it in 3D easier, as to create a shading effect
    //so each element represent the colour of each face
    // each colour is derived from colorHex
    private Color colors[];

    //as only 3 faces are shown, therefore 'colors' only has 3 elements, and a cube has six faces the unseen faces color values still need to be stored
    private Color top, bot, back, front, left, right;

    //the 3D space made in the grid class is here to allow access to the 3D space.
    private Grid grid;

    //the selected Polygon is highlighted each cube will be checked to see if the mouse is hovering over it and
    //empty is an empty polygon, as in x = 0, y = 0, width = 0, height = 0.
    private static Polygon selected, empty;

    //0 to 2 representing which face is being hovered on
    private static int face;

    //the Canvas in the editor
    private Canvas can;

    //initializer of selected and empty so it is made before the program starts
    static {
        selected = new Polygon();
        empty = new Polygon();
    }

    //constructor of the object, accepts a Canvas, the location in 3D space (x,y,z), and the Red Green and Blue components
    //for the colorHex.
    public Cube(@NotNull Canvas can, int x, int y, int z, int red, int green, int blue) {
        this.can = can;
        this.grid = can.getGrid();
        this.x = x;
        this.y = y;
        this.z = z;
        this.colors = new Color[3];
        //combines the rgb components into a single hex number using bit shifting
        colorHex = (red << 16) | (green << 8) | (blue);

        //see PU.saturateColor() for details
        //this is where the shading effect is made
        back = PU.saturateColor(new Color(colorHex), 0.5);
        bot = PU.saturateColor(new Color(colorHex), 0.6);
        left = PU.saturateColor(new Color(colorHex), 0.7);
        right = PU.saturateColor(new Color(colorHex), 0.8);
        top = PU.saturateColor(new Color(colorHex), 0.9);
        front = new Color(colorHex);
        this.colors[0] = top;
        this.colors[1] = front;
        this.colors[2] = right;
        cube = new Polygon[3];
        xps = new int[3][4];
        yps = new int[3][4];
        cube[0] = new Polygon();
        cube[1] = new Polygon();
        cube[2] = new Polygon();
    }

    //returns the empty polygon
    public static Polygon getEmpty() {
        return empty;
    }

    //sets the selected cube
    public static void setSelected(Polygon selected) {
        Cube.selected = selected;
    }

    //is called every frame
    public void updateCube() {
        {
            //will be either 0 or 1 and will switch between drawing the top or bottom face depending if the vertical rotate (rotatey) is bigger than or less than 0
            int drawTop;
            if (grid.getRotateY() < 0) {
                drawTop = 0;
                colors[0] = bot;
                if (cubeBelow()) {
                    tb = false;

                    //hides the bottom face if there is another cube below it
                    cube[0].reset();
                } else {
                    tb = true;
                    //sets the top or bottom, depending on 'drawTop'
                    for (int i = 0; i < 4; i++) {
                        //see MU.makeSquareI() for more details
                        xps[0][i] = (int) grid.getPts()[z + drawTop][x + MU.makeSquareI(false, i, 4)][y + MU.makeSquareI(true, i, 4)].getVec().getX();
                        yps[0][i] = (int) grid.getPts()[z + drawTop][x + MU.makeSquareI(false, i, 4)][y + MU.makeSquareI(true, i, 4)].getVec().getY();
                    }
                    cube[0].xpoints = xps[0];
                    cube[0].ypoints = yps[0];
                    cube[0].reset();
                    cube[0].npoints = 4;
                }
            } else {
                drawTop = 1;
                colors[0] = top;
                if (cubeAbove()) {
                    tb = false;
                    cube[0].reset();
                } else {
                    tb = true;
                    for (int i = 0; i < 4; i++) {
                        xps[0][i] = (int) grid.getPts()[z + drawTop][x + MU.makeSquareI(false, i, 4)][y + MU.makeSquareI(true, i, 4)].getVec().getX();
                        yps[0][i] = (int) grid.getPts()[z + drawTop][x + MU.makeSquareI(false, i, 4)][y + MU.makeSquareI(true, i, 4)].getVec().getY();
                    }
                    cube[0].xpoints = xps[0];
                    cube[0].ypoints = yps[0];
                    cube[0].reset();
                    cube[0].npoints = 4;
                }
            }
        }
        {
            // the same function as the previous block of code except for the back and front face
            int shift;
            if ((grid.getRotate() >= 0 && grid.getRotate() <= 90) || (grid.getRotate() >= 270 && grid.getRotate() <= 360)) {
                shift = 1;
                colors[1] = back;
                if (cubeFront()) {
                    bf = false;
                    cube[1].reset();
                } else {
                    bf = true;
                    for (int i = 0; i < 4; i++) {
                        xps[1][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + MU.makeSquareI(false, i, 4)][y + shift].getVec().getX();
                        yps[1][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + MU.makeSquareI(false, i, 4)][y + shift].getVec().getY();
                    }
                    cube[1].xpoints = xps[1];
                    cube[1].ypoints = yps[1];
                    cube[1].reset();
                    cube[1].npoints = 4;
                }
            } else {
                shift = 0;
                colors[1] = front;
                if (cubeBack()) {
                    bf = false;
                    cube[1].reset();
                } else {
                    bf = true;
                    for (int i = 0; i < 4; i++) {
                        xps[1][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + MU.makeSquareI(false, i, 4)][y + shift].getVec().getX();
                        yps[1][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + MU.makeSquareI(false, i, 4)][y + shift].getVec().getY();
                    }
                    cube[1].xpoints = xps[1];
                    cube[1].ypoints = yps[1];
                    cube[1].reset();
                    cube[1].npoints = 4;
                }
            }
        }
        {
            //the same as the previous block of code except for the left and right face
            int shift;
            if (grid.getRotate() >= 0 && grid.getRotate() <= 180) {
                shift = 1;
                colors[2] = left;
                if (cubeLeft()) {
                    lr = false;
                    cube[2].reset();
                } else {
                    lr = true;
                    for (int i = 0; i < 4; i++) {
                        xps[2][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + shift][y + MU.makeSquareI(false, i, 4)].getVec().getX();
                        yps[2][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + shift][y + MU.makeSquareI(false, i, 4)].getVec().getY();
                    }
                    cube[2].xpoints = xps[2];
                    cube[2].ypoints = yps[2];
                    cube[2].reset();
                    cube[2].npoints = 4;
                }
            } else {
                shift = 0;
                colors[2] = right;
                if (cubeRight()) {
                    lr = false;
                    cube[2].reset();
                } else {
                    lr = true;
                    for (int i = 0; i < 4; i++) {
                        xps[2][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + shift][y + MU.makeSquareI(false, i, 4)].getVec().getX();
                        yps[2][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + shift][y + MU.makeSquareI(false, i, 4)].getVec().getY();
                    }
                    cube[2].xpoints = xps[2];
                    cube[2].ypoints = yps[2];
                    cube[2].reset();
                    cube[2].npoints = 4;
                }
            }
        }
    }

    public void fillCube(@NotNull Graphics2D g2d) {
        if (tb) { //if there is no cube above or below then either the top or bottom face will be drawn
            g2d.setColor(colors[0]);
            g2d.fill(cube[0]);
        }
        if (bf) { //if there is no cube in front or to the back of this cube then either the front or back face fill be drawn
            g2d.setColor(colors[1]);
            g2d.fill(cube[1]);
        }
        if (lr) {  //if there is no cube to the left or right of this cube then the left or right face will drawn
            g2d.setColor(colors[2]);
            g2d.fill(cube[2]);
        }

        //draws the selected face
        g2d.setColor(Color.green);
        g2d.fill(selected);
    }

    //checks if there is a cube above itself
    private boolean cubeAbove() {// checks for xps cube above its self
        return z + 1 < grid.getHeight() - 1 && can.getCubes()[z + 1][x][y] != null;
    }

    //checks if there is a cube below itself
    private boolean cubeBelow() {// checks for xps cube below its self
        return z - 1 >= 0 && can.getCubes()[z - 1][x][y] != null;
    }

    //checks if there is a cube left of itself
    private boolean cubeLeft() {// checks for xps cube to the left of its self
        return x + 1 < grid.getSide() - 1 && can.getCubes()[z][x + 1][y] != null;
    }

    //checks if there is a cube right itself
    private boolean cubeRight() {// checks for xps cube to the right of its self
        return x - 1 >= 0 && can.getCubes()[z][x - 1][y] != null;
    }

    //checks if there is a cube in front of itself
    private boolean cubeFront() {// checks for xps cube in front of its self
        return y + 1 < grid.getSide() - 1 && can.getCubes()[z][x][y + 1] != null;
    }

    //checks if there is a cube behind itself
    private boolean cubeBack() {// checks for xps cube behind its self
        return y - 1 >= 0 && can.getCubes()[z][x][y - 1] != null;
    }

    //to string for debugging
    @NotNull
    @Override
    public String toString() {
        return x + " : " + y + " : " + z + "\t:0x" + Integer.toHexString(colorHex);
    }

    //accepts a x and y value, which will be the mouse's location and checks if it intersects any of the faces
    public boolean contains(int mx, int my) {
        boolean temp = false;
        for (int i = 0; i < 3; i++) {
            if (cube[i].contains(mx, my)) {
                face = i;
                temp = true;
            }
        }
        return temp;
    }

    //returns the color hex value of the cube
    public int getColorHex() {
        return colorHex;
    }

    //returns the selected face
    public static int getFace() {
        return face;
    }

    //is a function of the canvas, which sets the color to the primary colorWheel color, or secondary color if shift is held down
    public void paint(MouseEvent e) {
        int red, green, blue;
        if (ComponentManager.getCanvas().isNormalColours()) { // as there is two color modes contour(height map) or normal the
            if (e.isShiftDown()) {
                red = ComponentManager.getColorWheel().getC2Red();
                green = ComponentManager.getColorWheel().getC2Green();
                blue = ComponentManager.getColorWheel().getC2Blue();
            } else {
                red = ComponentManager.getColorWheel().getC1Red();
                green = ComponentManager.getColorWheel().getC1Green();
                blue = ComponentManager.getColorWheel().getC1Blue();
            }
        } else {

            if (e.isShiftDown()) {
                red = ComponentManager.getColorWheel().getC2Red();
                green = ComponentManager.getColorWheel().getC2Green();
                blue = ComponentManager.getColorWheel().getC2Blue();
            } else {
                red = ComponentManager.getColorWheel().getC1Red();
                green = ComponentManager.getColorWheel().getC1Green();
                blue = ComponentManager.getColorWheel().getC1Blue();
            }
            //this is two allow for editing without affecting what is being displayed, by editing a buffer, but not actually
            //displaying the code
            ComponentManager.getCanvas().setNormalBufferAtPoint(x, y, z, (red << 16) | (green << 8) | blue);
            red = (int) (255.0 * z / (ComponentManager.getCanvas().getCanvasHeight() - 1.0));
            green = (int) (255.0 * z / (ComponentManager.getCanvas().getCanvasHeight() - 1.0));
            blue = (int) (255.0 * z / (ComponentManager.getCanvas().getCanvasHeight() - 1.0));

        }
        back = new Color((int) (red * 0.5), (int) (green * 0.5), (int) (blue * 0.5));
        bot = new Color((int) (red * 0.6), (int) (green * 0.6), (int) (blue * 0.6));
        left = new Color((int) (red * 0.7), (int) (green * 0.7), (int) (blue * 0.7));
        right = new Color((int) (red * 0.8), (int) (green * 0.8), (int) (blue * 0.8));
        top = new Color((int) (red * 0.9), (int) (green * 0.9), (int) (blue * 0.9));
        front = new Color((red), (green), (blue));
        colorHex = (red << 16) | (green << 8) | blue;
        this.colors[0] = top;
        this.colors[1] = front;
        this.colors[2] = right;
    }

    //returns the polygons of each face of the cube
    public Polygon[] getFaces() {
        return cube;
    }

    //sets the color
    public void setColor(int red, int green, int blue) {
        back = new Color((int) (red * 0.5), (int) (green * 0.5), (int) (blue * 0.5));
        bot = new Color((int) (red * 0.6), (int) (green * 0.6), (int) (blue * 0.6));
        left = new Color((int) (red * 0.7), (int) (green * 0.7), (int) (blue * 0.7));
        right = new Color((int) (red * 0.8), (int) (green * 0.8), (int) (blue * 0.8));
        top = new Color((int) (red * 0.9), (int) (green * 0.9), (int) (blue * 0.9));
        front = new Color((red), (green), (blue));
        colorHex = (red << 16) | (green << 8) | blue;
        this.colors[0] = top;
        this.colors[1] = front;
        this.colors[2] = right;
    }

    //sets the color
    public void setColors(Color c) {
        int red = c.getRed();
        int blue = c.getBlue();
        int green = c.getGreen();
        back = new Color((int) (red * 0.5), (int) (green * 0.5), (int) (blue * 0.5));
        bot = new Color((int) (red * 0.6), (int) (green * 0.6), (int) (blue * 0.6));
        left = new Color((int) (red * 0.7), (int) (green * 0.7), (int) (blue * 0.7));
        right = new Color((int) (red * 0.8), (int) (green * 0.8), (int) (blue * 0.8));
        top = new Color((int) (red * 0.9), (int) (green * 0.9), (int) (blue * 0.9));
        front = new Color((red), (green), (blue));
        colorHex = (red << 16) | (green << 8) | blue;
        this.colors[0] = top;
        this.colors[1] = front;
        this.colors[2] = right;
    }
}
