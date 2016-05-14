package screen;


import backend.Cube;
import backend.Grid;
import guiTools.GuiComponent;
import util.MU;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public class Canvas extends GuiComponent {

    //int------------------------------                                                                                 //mouse location variables (mx,my)
    private static int mx, my, mb;
    private int noCubes = 0;
    private int maxNoCubes = 0;                                                                                          //and a variable to indicate which button was clicked on the mouse (mb)
    //---------------------------------                                                                                 //

    //Grid-----------------------------                                                                                 //
    private Grid grid;                                                                                                  //grid variable, when instantiated,
    //---------------------------------                                                                                 //it will hold every point in 3-Dimension(grid)

    //Cube-----------------------------                                                                                 //
    private Cube[][][] cubes;                                                                                           //all the cube data each dimension of the array represents a dimension in
    //---------------------------------                                                                                 //3-Dimensions (cubes)

    //Rectangle------------------------                                                                                 //
    private Rectangle pnt;                                                                                              //a pixel which is used which follows the mouse used for interaction such as
    //---------------------------------                                                                                 //hovering and clicking

    //boolean--------------------------                                                                                 //
    private boolean on = true;                                                                                          //used for debugging and optimising
    private boolean square, shiftSquare, half;                                                                          //used for deciding the order the cubes are drawn
    //---------------------------------                                                                                 //

    //VEvents--------------------------                                                                                 //these variables are a part of the paint ordering
    private final PaintEvent paintX;
    private final PaintEvent paintY;                                                                                    //basically methods stored in a variable
    //---------------------------------                                                                                 //PaintEvent is an interface with one method called event

    //constructor////////////////////////////
    public Canvas(int side, int height) {
        super(0, 0, EditorScreen.s_maxWidth, EditorScreen.s_maxHeight);
        grid = new Grid(side, height, EditorScreen.s_maxWidth / 2, EditorScreen.s_maxHeight / 2, 45, 37, 15);           // instantiates the grid, starts in isometric view and centers it.
        cubes = new Cube[height - 1][side - 1][side - 1];                                                               // instantiates the cube data to fit the grid points as z,x,y
        pnt = new Rectangle(0, 0, 1, 1);                                                                                // instantiates the mouse pointer
        square = MU.makeSquareB(false, (int) grid.getRotate(), 360);                                                    //}
        shiftSquare = MU.makeSquareB(true, (int) grid.getRotate(), 360);                                                //}~~ instantiates the booleans which decide the draw order
        half = MU.makeHalvesB(true, (int) grid.getRotate(), 360);                                                       //}
        maxNoCubes = (int) MU.square(side - 1) * (height - 1);
        paintY = (PaintEvent e, int z, int y, int x, Graphics2D g2d) -> {                                               // instantiates the PaintEvent method paintX which will either draw cubes forward or backwards
            if (!square) {                                                                                              // according to their x values
                for (int yi = 0; yi < grid.getSide() - 1; yi++) {
                    e.event(null, z, yi, 0, g2d);
                }
            } else {
                for (int yi = grid.getSide() - 2; yi >= 0; yi--) {
                    e.event(null, z, yi, 0, g2d);
                }
            }
        };
        paintX = (PaintEvent e, int z, int y, int x, Graphics2D g2d) -> {                                               // instantiates the PaintEvent method paintX which will either draw cubes forward or backwards
            if (!shiftSquare) {                                                                                         // according to their y values
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
        cuboid(0, 0, 0, side - 1, side - 1, height - 1);
        //sphere((int)((side-2)/2.0), (int)((side-2)/2.0), (int)((height-2)/2.0), (int) ((side - 2) / 2.0), (int) ((side - 2) / 2.0),(int)((height-2)/2.0));
        //cuboid(12,12,12,1,1,1);
    }
    /////////////////////////////////////////

    //shapes//////////////////////////////////////////////////////////////////////////////////
    private void cuboid(int x, int y, int z, int width, int length, int height) {                                       // makes a cuboid with specified location and dimensions
        for (int xi = x; xi < x + width; xi += 1) {
            for (int yi = y; yi < y + length; yi += 1) {
                for (int zi = z; zi < z + height; zi += 1) {
                    if ((!(zi > grid.getHeight() - 2) && !(zi < 0)) && (!(xi > grid.getSide() - 1) && !(xi < 0)) && (!(yi > grid.getSide() - 1) && !(yi < 0))) {

                        cubes[zi][xi][yi] = new Cube(this, xi, yi, zi, (int) (xi * 255.0 / width), (int) (yi * 255.0 / length), (int) (zi * 255.0 / height));
                        //cubes[zi][xi][yi] = new Cube(this, xi, yi, zi, 255, 255, 255);
                    }
                }
            }
        }
    }

    public void sphere(int x, int y, int z, double width, double length, double height) {                               // makes a 3D ellipse(irregular sphere) with a specified location and dimensions
        for (int theta = 0; theta < 2880; theta += 5) {
            for (int pi = -1440; pi < 1440; pi += 5) {
                int xi = (int) (Math.round(x + (width * MU.cos(theta / 8.0) * MU.cos(pi / 8.0))));
                int yi = (int) (Math.round(y + (length * MU.sin(theta / 8.0) * MU.cos(pi / 8.0))));
                int zi = (int) (Math.round(z + (height * MU.sin(pi / 8.0))));

                cubes[zi][xi][yi] = new Cube(this, xi, yi, zi, 255, 255, 255);

            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////

    //drawing and graphics updates////////////////
    @Override
    public void paintGuiComponent(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);   //increases speed of opaque cubes
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);              //makes graphics choose speed over quality
        g2d.setColor(Color.black);

        if (half) {
            paintZ(paintY, g2d);
            grid.paintAxis(g2d);
        } else {
            grid.paintAxis(g2d);                                                                                        //ui graphic
            paintZ(paintY, g2d);
        }
    }

    @Override
    public void update() {                                                                                               //updates the graphics objects
        grid.update();
        pnt.setLocation(mx, my);
        square = MU.makeSquareB(false, (int) grid.getRotate(), 360);
        shiftSquare = MU.makeSquareB(true, (int) grid.getRotate(), 360);
        half = MU.makeHalvesB(true, (int) grid.getRotate(), 360);
        noCubes = 0;
        for (int zi = 0; zi < grid.getHeight() - 1; zi++) {
            for (int yi = 0; yi < grid.getSide() - 1; yi++) {
                for (int xi = 0; xi < grid.getSide() - 1; xi++) {
                    if (!(cubes[zi][xi][yi] == null)) {                                                                 //ignores empty spaces
                        cubes[zi][xi][yi].updateCube();
                        cubes[zi][xi][yi].hover(pnt);
                        noCubes++;
                    }
                }
            }
        }
    }
    //////////////////////////////////////////////

    //canvas manipulation////////////////////////////////////////////////////////
    public void addCube(int x, int y, int z, int red, int green, int blue) {                                            //creates a cube at a specified location
        cubes[z][x][y] = new Cube(this, x, y, z, red, green, blue);
    }

    public void clearCanvas() {                                                                                         //clears the canvas of all cubes
        for (int z = 0; z < grid.getHeight() - 2; z++) {
            for (int y = 0; y < grid.getSide() - 1; y++) {
                for (int x = 0; x < grid.getSide() - 1; x++) {
                    cubes[z][x][y] = null;
                }
            }
        }
    }

    public static boolean checkForCube(Canvas c, int x, int y, int z) {                                                 //searches for a cube at certain location
        return c.cubes[z][x][y] != null;
    }

    private void paintZ(PaintEvent e, Graphics2D g2d) {                                                                 //decides whether to draw the cubes forwards or backwards according to their z values
        if (grid.getRotateY() < 0) {
            for (int zi = grid.getHeight() - 2; zi > -1; zi--) {
                e.event(paintX, zi, 0, 0, g2d);
            }
            grid.paint(g2d);
        } else {
            grid.paint(g2d);
            for (int zi = 0; zi < grid.getHeight() - 1; zi++) {
                e.event(paintX, zi, 0, 0, g2d);
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////

    //user input//////////////////////////////////////////////////
    @Override
    public void hover(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
        grid.hover(pnt);
        for (int zi = 0; zi < grid.getHeight() - 1; zi++) {
            for (int yi = 0; yi < grid.getSide() - 1; yi++) {
                for (int xi = 0; xi < grid.getSide() - 1; xi++) {
                    if (!(cubes[zi][xi][yi] == null)) {
                        cubes[zi][xi][yi].hover(pnt);
                    }
                }
            }
        }
    }
    @Override
    public void drag(MouseEvent e) {
        int dx = mx - e.getX();
        int dy = my - e.getY();
        if (mb == 2) {
            grid.update();
            grid.setLocation(mx,my);
        }
        mx = e.getX();
        my = e.getY();

        if (mb == 3) {
            grid.rotate(dx / 2);
            grid.rotatey(-dy / 2);
        }
    }
    @Override
    public void click(MouseEvent e) {                                                                                   //is called when any button on the mouse is clicked
    }
    @Override
    public void mousePress(MouseEvent e) {                                                                              //is called whenever a button is pressed down on the mouse
        mb = e.getButton();
        mx = e.getX();
        my = e.getY();
        if (mb == 1) {
            for (int zi = 0; zi < grid.getHeight() - 1; zi++) {
                for (int yi = 0; yi < grid.getSide() - 1; yi++) {
                    for (int xi = 0; xi < grid.getSide() - 1; xi++) {
                        if (!(cubes[zi][xi][yi] == null)) {
                            cubes[zi][xi][yi].click(pnt);
                            break;
                        }
                    }
                }
            }
        }
    }
    @Override
    public void mouseRelease(MouseEvent e) {

    }
    @Override
    public void keyPress(KeyEvent ke) {                                                                                 //is called whenever a key on the keyboard is pressed
        if (ke.getKeyCode() == KeyEvent.VK_W) {
            grid.rotatey(2);
        }
        if (ke.getKeyCode() == KeyEvent.VK_S) {
            grid.rotatey(-2);
        }
        if (ke.getKeyCode() == KeyEvent.VK_E) {
            grid.rotate(2);
        }
        if (ke.getKeyCode() == KeyEvent.VK_Q) {
            grid.rotate(-2);
        }
        if (ke.getKeyCode() == KeyEvent.VK_UP) {
            grid.zoom(2);
        }
        if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
            grid.zoom(-2);
        }
        if (ke.getKeyCode() == KeyEvent.VK_Z) {
            on = false;
        }
        if (ke.getKeyCode() == KeyEvent.VK_X) {
            on = true;
        }
        Cube.keyPressed(ke);
    }
    @Override
    public void keyRelease(KeyEvent e) {

    }
    @Override
    public void scroll(MouseWheelEvent e) {                                                                         //is called whenever the mouse wheel is scrolled
        int n = e.getWheelRotation();
        grid.zoom((int) (-2.5 * n));
    }
    /////////////////////////////////////////////////////////////

    //accessors/////////////////////////////////////
    public Grid getGrid() {                                                                                             //returns the grid
        return grid;
    }

    public Cube[][][] getCubes() {                                                                                      //returns the cube data
        return cubes;
    }

    public int getNoCubes() {
        return noCubes;
    }

    public int getMaxNoCubes() {
        return maxNoCubes;
    }

    public void setRotate(int direction) {
        grid.setRotate(direction);
    }

    public void setRotatey(int direction){
        grid.setRotatey(direction);
    }

    public void setZoom(int zoom){
        grid.setZoom(zoom);
    }
    ///////////////////////////////////////////////
}
