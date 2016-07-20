package editorScreen;


import backend.Cube;
import backend.Grid;
import guiTools.GuiComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import util.MU;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public class Canvas extends GuiComponent {
    private int noCubes = 0;    //to store the number of cubes on the canvas
    private int maxNoCubes = 0; //to store the max number of the canvas
    private int side, height;   // the sides length and height of the canvas
    @NotNull
    private final Grid grid;    //the 3D space and grid which is used to display cubes, see the Grid class
    @NotNull
    private Cube[][][] cubes;   //stores the cube data of the canvas
    @NotNull
    private final Rectangle pnt;//used for user input
    private boolean square, shiftSquare, half;//used for paint efficiency
    @NotNull
    private final PaintEvent paintX;// an interface object, which is used for order of painting
    @NotNull
    private final PaintEvent paintY;// an interface object, which is used for order of painting
    private Rectangle displayPicture;//used for capturing screenshots

    private static int mx, my, mb;//mouse location and which button on the mouse is clicked
    private static int selectedTool = CanvasManipulator.ADD;// the currently selected tool
    private static boolean gridSelect;


    public Canvas(int side, int height) {  //constructor
        super(0, 0, EditorScreen.s_maxWidth, EditorScreen.s_maxHeight); // the canvas counts as a gui component
        grid = new Grid(side, height, EditorScreen.s_maxWidth / 2, EditorScreen.s_maxHeight / 2 + 150);//instantiates the grid in the centre of the screen
        cubes = new Cube[height - 1][side - 1][side - 1];//instantiates the cube array according the grid
        pnt = new Rectangle(0, 0, 1, 1);// instantiates the rectangle used for user input
        square = MU.makeSquareB(false, (int) grid.getRotate(), 360);//see the MU(math utilities) class for information
        shiftSquare = MU.makeSquareB(true, (int) grid.getRotate(), 360);//""    ""  ""  ""  ""  ""  ""  ""  ""
        half = MU.makeHalvesB(true, (int) grid.getRotate(), 360);//""   ""  ""  ""  ""  ""  ""  ""  ""  ""  ""
        maxNoCubes = (int) MU.square(side - 1) * (height - 1);//""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""
        this.side = side;
        this.height = height;
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
        //cuboid(0, 0, 0, side - 1, side - 1, height - 1);
        //sphere((int) ((side - 2) / 2.0), (int) ((side - 2) / 2.0), (int) ((height - 2) / 2.0), (int) ((side - 2) / 2.0), (int) ((side - 2) / 2.0), (int) ((height - 2) / 2.0));
        //cuboid(12,12,12,1,1,1);
    }

    public int getSelectedTool() {
        return selectedTool;
    }

    @SuppressWarnings({"ConstantConditions", "SameParameterValue", "unused"})
    private void cuboid(int x, int y, int z, int width, int length, int height) {   // creates a white cuboid in the canvas
        for (int xi = x; xi < x + width; xi += 1) {
            for (int yi = y; yi < y + length; yi += 1) {
                for (int zi = z; zi < z + height; zi += 1) {
                    if ((!(zi > grid.getHeight() - 2) && !(zi < 0)) && (!(xi > grid.getSide() - 1) && !(xi < 0)) && (!(yi > grid.getSide() - 1) && !(yi < 0))) {
                        //cubes[zi][xi][yi] = new Cube(this, xi, yi, zi, (int) (xi * 255.0 / width), (int) (yi * 255.0 / length), (int) (zi * 255.0 / height));
                        cubes[zi][xi][yi] = new Cube(this, xi, yi, zi, 255, 255, 255);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void sphere(int x, int y, int z, double width, double length, double height) { // creates a white sphere in the canvas
        for (int theta = 0; theta < 2880; theta += 5) {
            for (int pi = -1440; pi < 1440; pi += 5) {
                int xi = (int) (Math.round(x + (width * MU.cos(theta / 8.0) * MU.cos(pi / 8.0))));
                int yi = (int) (Math.round(y + (length * MU.sin(theta / 8.0) * MU.cos(pi / 8.0))));
                int zi = (int) (Math.round(z + (height * MU.sin(pi / 8.0))));

                cubes[zi][xi][yi] = new Cube(this, xi, yi, zi, 255, 255, 255);

            }
        }
    }

    @SuppressWarnings("unused")
    public void setCube(int x, int y, int z, int red, int green, int blue) {// sets a single cube in the canvas with a specified colour
        if (checkIfInBounds(x, y, z))
            cubes[z][x][y] = new Cube(this, x, y, z, red, green, blue);
    }

    @SuppressWarnings("unused")
    public void clearCanvas() { // removes all cubes in the canvas
        for (int z = 0; z < grid.getHeight() - 1; z++) {
            for (int y = 0; y < grid.getSide() - 1; y++) {
                for (int x = 0; x < grid.getSide() - 1; x++) {
                    cubes[z][x][y] = null;
                }
            }
        }
    }

    public boolean checkIfInBounds(int x, int y, int z) {// checks if the given coordinates falls in the canvas
        return (!(x < 0) && !(x >= grid.getSide() - 1) && !(y < 0) && !(y >= grid.getSide() - 1) && !(z < 0) && !(z >= grid.getHeight() - 1));
    }

    @Contract(pure = true)
    @SuppressWarnings("unused")
    public static boolean checkForCube(@NotNull Canvas c, int x, int y, int z) {// checks if there is a cube at given coordinates
        return c.checkIfInBounds(x, y, z) && c.cubes[z][x][y] != null;
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

    private void showCoords(Graphics2D g2d) {// apart of a setting which shows the coordinates at each corner of the canvas
        int x_ = grid.getSide() - 1;
        int y_ = grid.getSide() - 1;
        int z_ = grid.getHeight() - 1;
        int side = grid.getSide() - 1;
        int height = grid.getHeight() - 2;
        g2d.setColor(Color.white);
        g2d.setFont(EditorScreen.font.deriveFont(11f));
        g2d.drawString("(z,x,y)", (int) grid.getPts()[0][0][0].getVecs().getX(), (int) grid.getPts()[0][0][0].getVecs().getY() - 14);
        g2d.drawString("(0,0,0)", (int) grid.getPts()[0][0][0].getVecs().getX(), (int) grid.getPts()[0][0][0].getVecs().getY());
        g2d.drawString("(0," + side + ",0)", (int) grid.getPts()[0][x_][0].getVecs().getX(), (int) grid.getPts()[0][x_][0].getVecs().getY());
        g2d.drawString("(0,0," + side + ")", (int) grid.getPts()[0][0][y_].getVecs().getX(), (int) grid.getPts()[0][0][y_].getVecs().getY());
        g2d.drawString("(0," + side + "," + side + ")", (int) grid.getPts()[0][x_][y_].getVecs().getX(), (int) grid.getPts()[0][x_][y_].getVecs().getY());
        g2d.drawString("(" + height + ",0,0)", (int) grid.getPts()[z_][0][0].getVecs().getX(), (int) grid.getPts()[z_][0][0].getVecs().getY());
        g2d.drawString("(" + height + "," + side + ",0)", (int) grid.getPts()[z_][x_][0].getVecs().getX(), (int) grid.getPts()[z_][x_][0].getVecs().getY());
        g2d.drawString("(" + height + ",0," + side + ")", (int) grid.getPts()[z_][0][y_].getVecs().getX(), (int) grid.getPts()[z_][0][y_].getVecs().getY());
        g2d.drawString("(" + height + "," + side + "," + side + ")", (int) grid.getPts()[z_][x_][y_].getVecs().getX(), (int) grid.getPts()[z_][x_][y_].getVecs().getY());
    }

    @Override
    protected void paintGuiComponent(@NotNull Graphics2D g2d) { //overridden from guicomponent which eventually gets fed into the screen painter in the EditorScreen class
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);                  //settings for optimization
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2d.setColor(Color.black);

        if (half) {
            paintZ(paintY, g2d);
            if (ComponentManager.settings.isShowAxis()) {
                grid.paintAxis(g2d);
            }
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

    }

    @Override
    protected void update() {// is called every frame
        grid.update();
        pnt.setLocation(mx, my);
        square = MU.makeSquareB(false, (int) grid.getRotate(), 360);
        shiftSquare = MU.makeSquareB(true, (int) grid.getRotate(), 360);
        half = MU.makeHalvesB(true, (int) grid.getRotate(), 360);
        noCubes = 0;
        for (int zi = 0; zi < grid.getHeight() - 1; zi++) {
            for (int yi = 0; yi < grid.getSide() - 1; yi++) {
                for (int xi = 0; xi < grid.getSide() - 1; xi++) {
                    if (cubes[zi][xi][yi] != null)
                        cubes[zi][xi][yi].updateCube();
                }
            }
        }


    }

    @Override
    protected void hover(@NotNull MouseEvent e) {// is called every frame when the mouse is moving
        mx = e.getX();
        my = e.getY();
        int count = 0;
        for (int x = 0; x < side - 1; x++) {
            for (int y = 0; y < side - 1; y++) {
                for (int z = 0; z < height - 1; z++) {
                    if (cubes[z][x][y] != null && cubes[z][x][y].intersect(mx, my)) {

                        count = 0;
                    }
                    count++;
                }
            }
        }
        gridSelect = count == (side - 1) * (side - 1) * (height - 1);
        if (gridSelect) {
            grid.gridInterfaceHover(e);
            Cube.setSelected(Cube.getEmpty());
        }
        if (!Cube.getSelected().equals(Cube.getEmpty())) {
            grid.setSelected(Cube.getEmpty());
        }


    }

    @Override
    protected void drag(@NotNull MouseEvent e) {// is called every frame every frame when the mouse is moving and a button is held doen
        int dx = mx - e.getX();
        int dy = my - e.getY();
        int count = 0;

        if (mb == 2) {              //mouse wheel click
            grid.update();          // translates the canvas to the mouses location
            grid.setLocation(mx, my);
        }
        mx = e.getX();
        my = e.getY();
        if (mb == 3) {              //right click
            grid.rotate(dx / 2);    // rotates the canvas vertically and horizontally
            grid.rotatey(-dy / 2);
            for (int x = 0; x < side - 1; x++) {
                for (int y = 0; y < side - 1; y++) {
                    for (int z = 0; z < height - 1; z++) {
                        if (cubes[z][x][y] != null && cubes[z][x][y].intersect(mx, my)) {
                            count = 0;

                        }
                        count++;
                    }
                }
            }
            gridSelect = count == (side - 1) * (side - 1) * (height - 1);
            if (gridSelect) {
                grid.gridInterfaceHover(e);
                Cube.setSelected(Cube.getEmpty());
            }
            if (!Cube.getSelected().equals(Cube.getEmpty())) {
                grid.setSelected(Cube.getEmpty());
            }
        }

    }

    @Override
    protected void mousePress(@NotNull MouseEvent e) {
        mb = e.getButton();
        mx = e.getX();
        my = e.getY();
        if (mb == 1) {
            if (gridSelect) {
                if (grid.getHovered().contains(mx, my)) {
                    if (selectedTool == CanvasManipulator.ADD && cubes[0][grid.getSelectedX()][grid.getSelectedY()] == null) {
                        setCube(grid.getSelectedX(), grid.getSelectedY(), 0, ComponentManager.getColorWheel().getRed(), ComponentManager.getColorWheel().getGreen(), ComponentManager.getColorWheel().getBlue());
                    }
                }
            } else {
                for (int x = 0; x < side - 1; x++) {
                    for (int y = 0; y < side - 1; y++) {
                        for (int z = 0; z < height - 1; z++) {
                            if (cubes[z][x][y] != null && cubes[z][x][y].intersect(mx, my)) {
                                cubes[z][x][y].click(mx, my);
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    protected void mouseRelease(MouseEvent e) {

    }

    // keyboard input
    @Override
    protected void keyPress(@NotNull KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_W) { //w key
            grid.rotatey(1);                    // rotates canvas vertically up
        }
        if (ke.getKeyCode() == KeyEvent.VK_S) { //s key
            grid.rotatey(-1);                   //rotates canvas vertically down
        }
        if (ke.getKeyCode() == KeyEvent.VK_D) { //d key
            grid.rotate(1);                     // rotates canvas horizontally right
        }
        if (ke.getKeyCode() == KeyEvent.VK_A) { //a key
            grid.rotate(-1);                    // rotates canvas horizontally left
        }
        if (ke.getKeyCode() == KeyEvent.VK_UP) { //up arrow key
            grid.zoom(1);                        // zooms in
        }
        if (ke.getKeyCode() == KeyEvent.VK_DOWN) { //down arrow key
            grid.zoom(-1);                         // zooms out
        }
    }

    @Override
    protected void scroll(@NotNull MouseWheelEvent e) {
        int n = e.getWheelRotation();// checks if the user is rolling the mouse wheel forward or backwards
        grid.zoom((int) (-2.5 * n));// zooms in or out depending on the sign of n;
    }

    @NotNull
    public Grid getGrid() {
        return grid; // returns the 3d spcae points;
    }

    @NotNull
    public Cube[][][] getCubes() {
        return cubes; // returns the 3d array which holds the cube data
    }

    @SuppressWarnings("unused")
    public int getNoCubes() {
        return noCubes; // returns how many cubes are in the canvas
    }

    @SuppressWarnings("unused")
    public int getMaxNoCubes() {
        return maxNoCubes;// returns the volume of the canvas
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
        displayPicture = new Rectangle();
        setRotate(45);  //sets the to isometric view
        setRotatey(36); //""    ""   ""     ""  ""
        setZoom(15);    //sets the canvas to nice zoom
        displayPicture.setBounds(   //sets the rectangle
                (int) (grid.getPts()[0][0][side - 1].getVecs().getX()),
                (int) (grid.getPts()[height - 1][0][0].getVecs().getY() + 26),
                (int) (grid.getPts()[height - 1][side - 1][0].getVecs().getX() - grid.getPts()[0][0][side - 1].getVecs().getX()),
                (int) (grid.getPts()[0][side - 1][side - 1].getVecs().getY() - grid.getPts()[height - 1][0][0].getVecs().getY()));
        grid.setLocation(EditorScreen.s_maxWidth / 2, EditorScreen.s_maxHeight / 2 + 100);// moves the canvas to the centre of the screen
        ComponentManager.settings.setShowAxis(false);// disables the settings
        ComponentManager.settings.setShowCoords(false);//"" ""  ""  ""  ""
        ComponentManager.settings.setShowGrid(false);// ""  ""  ""  ""  ""

    }


    public Rectangle getDisplayImage() {
        return displayPicture;// returns the rectangle which will be used to capture the image

    }

    public static boolean isGridSelect() {
        return gridSelect;
    }

    public static void setGridSelect(boolean gridSelect) {
        Canvas.gridSelect = gridSelect;
    }

    public void removeCubeAt(int x, int y, int z) {
        if (checkForCube(this, x, y, z)) {
            cubes[z][x][y] = null;
        }
    }
}
