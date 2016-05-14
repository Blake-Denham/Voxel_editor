package screen;


import backend.Cube;
import backend.Grid;
import guiTools.GuiComponent;
import org.jetbrains.annotations.NotNull;
import util.MU;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public class Canvas extends GuiComponent {

    private static int mx, my, mb;
    private int noCubes = 0;
    private int maxNoCubes = 0;

    @NotNull
    private final Grid grid;

    @NotNull
    private final Cube[][][] cubes;

    @NotNull
    private final Rectangle pnt;

    private boolean square, shiftSquare, half;

    @NotNull
    private final PaintEvent paintX;
    @NotNull
    private final PaintEvent paintY;

    @SuppressWarnings("SameParameterValue")
    public Canvas(int side, int height) {
        super(0, 0, EditorScreen.s_maxWidth, EditorScreen.s_maxHeight);
        grid = new Grid(side, height, EditorScreen.s_maxWidth / 2, EditorScreen.s_maxHeight / 2);
        cubes = new Cube[height - 1][side - 1][side - 1];
        pnt = new Rectangle(0, 0, 1, 1);
        square = MU.makeSquareB(false, (int) grid.getRotate(), 360);
        shiftSquare = MU.makeSquareB(true, (int) grid.getRotate(), 360);
        half = MU.makeHalvesB(true, (int) grid.getRotate(), 360);
        maxNoCubes = (int) MU.square(side - 1) * (height - 1);
        paintY = (PaintEvent e, int z, int y, Graphics2D g2d) -> {
            if (!square) {
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
            if (!shiftSquare) {
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
        sphere((int) ((side - 2) / 2.0), (int) ((side - 2) / 2.0), (int) ((height - 2) / 2.0), (int) ((side - 2) / 2.0), (int) ((side - 2) / 2.0), (int) ((height - 2) / 2.0));
        //cuboid(12,12,12,1,1,1);
    }

    @SuppressWarnings({"ConstantConditions", "SameParameterValue", "unused"})
    private void cuboid(int x, int y, int z, int width, int length, int height) {
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

    private void sphere(int x, int y, int z, double width, double length, double height) {
        for (int theta = 0; theta < 2880; theta += 5) {
            for (int pi = -1440; pi < 1440; pi += 5) {
                int xi = (int) (Math.round(x + (width * MU.cos(theta / 8.0) * MU.cos(pi / 8.0))));
                int yi = (int) (Math.round(y + (length * MU.sin(theta / 8.0) * MU.cos(pi / 8.0))));
                int zi = (int) (Math.round(z + (height * MU.sin(pi / 8.0))));

                cubes[zi][xi][yi] = new Cube(this, xi, yi, zi, 255, 255, 255);

            }
        }
    }

    @Override
    public void paintGuiComponent(@NotNull Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2d.setColor(Color.black);

        if (half) {
            paintZ(paintY, g2d);
            grid.paintAxis(g2d);
        } else {
            grid.paintAxis(g2d);
            paintZ(paintY, g2d);
        }
    }

    @Override
    public void update() {
        grid.update();
        pnt.setLocation(mx, my);
        square = MU.makeSquareB(false, (int) grid.getRotate(), 360);
        shiftSquare = MU.makeSquareB(true, (int) grid.getRotate(), 360);
        half = MU.makeHalvesB(true, (int) grid.getRotate(), 360);
        noCubes = 0;
        for (int zi = 0; zi < grid.getHeight() - 1; zi++) {
            for (int yi = 0; yi < grid.getSide() - 1; yi++) {
                for (int xi = 0; xi < grid.getSide() - 1; xi++) {
                    if (!(cubes[zi][xi][yi] == null)) {
                        cubes[zi][xi][yi].updateCube();
                        cubes[zi][xi][yi].hover(pnt);
                        noCubes++;
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public void addCube(int x, int y, int z, int red, int green, int blue) {
        cubes[z][x][y] = new Cube(this, x, y, z, red, green, blue);
    }

    @SuppressWarnings("unused")
    public void clearCanvas() {
        for (int z = 0; z < grid.getHeight() - 2; z++) {
            for (int y = 0; y < grid.getSide() - 1; y++) {
                for (int x = 0; x < grid.getSide() - 1; x++) {
                    cubes[z][x][y] = null;
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public static boolean checkForCube(@NotNull Canvas c, int x, int y, int z) {
        return c.cubes[z][x][y] != null;
    }

    private void paintZ(@NotNull PaintEvent e, Graphics2D g2d) {
        if (grid.getRotateY() < 0) {
            for (int zi = grid.getHeight() - 2; zi > -1; zi--) {
                e.event(paintX, zi, 0, g2d);
            }
            grid.paint(g2d);
        } else {
            grid.paint(g2d);
            for (int zi = 0; zi < grid.getHeight() - 1; zi++) {
                e.event(paintX, zi, 0, g2d);
            }
        }
    }

    @Override
    public void hover(@NotNull MouseEvent e) {
        mx = e.getX();
        my = e.getY();
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
    public void drag(@NotNull MouseEvent e) {
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
    public void mousePress(@NotNull MouseEvent e) {
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
    public void keyPress(@NotNull KeyEvent ke) {
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

        Cube.keyPressed(ke);
    }

    @Override
    public void scroll(@NotNull MouseWheelEvent e) {
        int n = e.getWheelRotation();
        grid.zoom((int) (-2.5 * n));
    }

    @NotNull
    public Grid getGrid() {
        return grid;
    }

    @NotNull
    public Cube[][][] getCubes() {
        return cubes;
    }

    @SuppressWarnings("unused")
    public int getNoCubes() {
        return noCubes;
    }

    @SuppressWarnings("unused")
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
}
