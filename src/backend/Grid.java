package backend;

import editorScreen.Canvas;
import org.jetbrains.annotations.NotNull;
import util.MU;

import java.awt.*;

public class Grid {

    /**
     * class is used for creating 3-Dimensional space which can my rotated and scaled
     *
     * @see GridPoint is combined
     * @see CirclePoint
     */

    //x screen coordinate of the grid
    private int x;

    //y screen coordinate of the grid
    private int y;

    //the height of the grid, ie how many points on the z axis to make
    private final int height;

    //the length and width, ie how many points are on the x and y axis each
    private final int side;

    //used for defining the screen x coordinates of the grid polygons
    @NotNull
    private final int[][][] xp;

    //used for defining the screen y coordinates of the grid polygons
    @NotNull
    private final int[][][] yp;

    //the horizontal and vertical rotate and the zoom value
    private double rotate, rotatey, zoom;

    //all the x and y screen coordinates corresponding with a dimension, each dimension of the array represents a dimension in space [z][x][y].
    @NotNull
    private final GridPoint[][][] pts;

    //the 2D grid which displays the grid
    @NotNull
    private final Polygon[][] p;

    //used to define the z axis
    @NotNull
    private final CirclePoint[] zAxis;

    //used to centre the grid around a specified point at 'cent' instead of the origin of the 3D space ie: (0,0,0)
    @NotNull
    private final CirclePoint cent;

    //for aesthetics no real function
    private static final Color colorGridp = new Color(1f, 1f, 1f, 0.4f);
    private static final Color colorGridn = new Color(1f, 1f, 1f, 0.3f);

    //the polygon of the grid which is being hovered on
    private Polygon selected;

    //the coordinates of the selected polygon
    private int selectedX, selectedY;

    //constructor sets the dimensions and the screen location
    public Grid(int side, int height, int x, int y) {
        /*
          creates the 3D space with specified location, dimensions, rotations and scale
          as point falls on a circle of radius r, offset by p degrees the points are
          made using the formulae.
          r = squareRoot((100*side*xPoint*cos(45)+100*side*yPoint*sin(45))^2).
          p = (180*arctan(yPoint/xPoint))/PI.
          a circle,with a radius of root(200)*n,n increments until it is equal to height, and side^2 crosses are made at every rotatey of each one of the circles which were created
         */
        selected = new Polygon();
        this.y = y;
        this.x = x;

        //default view is isometric, ie: 45 degrees horizontally and 35 degrees vertically
        this.rotate = (double) 45;
        this.rotatey = (double) 35;

        //a moderate default zoom
        this.zoom = (double) 15;

        this.side = side;
        this.height = height;
        zAxis = new CirclePoint[height];

        //see 'CirclePoint for details
        //the √((100 x side x cos(45))^2 + (100 x side x sin(45))^2) returns a radius from the centre of a square to its edge
        cent = new CirclePoint(90, x, y,
                (int) (Math.sqrt(MU.square(100 * side * MU.cos(45)) + MU.square(100 * side * MU.sin(45)))),
                0, 90, (double) 90, (double) 15);

        pts = new GridPoint[height][side][side];
        int y_ = (int) cent.getPts()[0].getY();
        int x_ = (int) cent.getPts()[0].getX();

        for (int z = 0; z < height; z++) {
            for (int xi = 0; xi < side; xi++) {
                for (int yi = 0; yi < side; yi++) {
                    pts[z][xi][yi] = new GridPoint(x_, y_,
                            //same as cent, except for every point in 3D space
                            (int) (Math.sqrt(MU.square(100 * (xi) * MU.cos(45)) + MU.square(100 * yi * MU.sin(45)))),
                            (double) 0,
                            //a rotation offset as it is used making squares in a circle
                            Math.toDegrees(MU.arctan((double) yi / (double) xi)), (double) 90, (double) 15);
                }
            }
            pts[z][0][0].setVec(x_, y_);
            zAxis[z] = new CirclePoint(1, x_, y_, 50 * (z + 1), 90, 0, (double) 37 - 90, (double) 15);
            y_ = (int) (zAxis[z].getPts()[0].getY());
        }
        p = new Polygon[(side) - 1][side - 1];
        xp = new int[side][side][4];
        yp = new int[side][side][4];

        //makes the polygons for the intractable grid
        for (int xi = 0; xi < side - 1; xi++) {
            for (int yi = 0; yi < side - 1; yi++) {
                for (int i = 0; i < 4; i++) {
                    xp[xi][yi][i] = (int) pts[0][xi + MU.makeSquareI(false, i, 4)][yi + MU.makeSquareI(true, i, 4)].getVec().getX();
                    yp[xi][yi][i] = (int) pts[0][xi + MU.makeSquareI(false, i, 4)][yi + MU.makeSquareI(true, i, 4)].getVec().getY();
                }
                p[xi][yi] = new Polygon(xp[xi][yi], yp[xi][yi], 4);
            }
        }
    }

    //called once a frame and essentially does what the constructor does
    public void update() {
        cent.update(x, y,
                (int) (Math.sqrt(MU.square(100 * (side - 1) * MU.cos(45)) + MU.square(100 * (side - 1) * MU.sin(45))) / 2),
                rotate - 135, 90, rotatey, zoom);

        int x_ = (int) cent.getPts()[0].getX();
        int y_ = (int) cent.getPts()[0].getY();
        for (int zi = 0; zi < height; zi++) {
            for (int xi = 0; xi < side; xi++) {
                for (int yi = 0; yi < side; yi++) {
                    pts[zi][xi][yi].update(x_, y_,
                            (int) (Math.sqrt(MU.square(100 * (xi) * MU.cos(45)) + MU.square(100 * yi * MU.sin(45)))),
                            rotate, Math.toDegrees(MU.arctan((double) yi / (double) xi)), rotatey, zoom);
                }
            }
            pts[zi][0][0].setVec(x_, y_);
            zAxis[zi].update(x_, (int) ((cent.getPts()[0].getY())),
                    (int) ((zi + 1) * Math.abs(Math.sqrt(MU.square(100 * MU.cos(45)) + MU.square(100 * MU.sin(45)))     //(z+1)*|squareRoot(((100*cos(45))^2)+(100*sin(45))^2) - squareRoot((200*cos(45))^2+(200*sin45)^2)|
                            - Math.sqrt(MU.square(100 * 2 * MU.cos(45)) + MU.square(100 * 2 * MU.sin(45)))) / Math.sqrt(2)),
                    90, 0, rotatey - 90, zoom);
            y_ = (int) (zAxis[zi].getPts()[0].getY());
        }
        for (int xi = 0; xi < side - 1; xi++) {
            for (int yi = 0; yi < side - 1; yi++) {
                for (int i = 0; i < 4; i++) {
                    xp[xi][yi][i] = (int) pts[0][xi + MU.makeSquareI(false, i, 4)][yi + MU.makeSquareI(true, i, 4)].getVec().getX();
                    yp[xi][yi][i] = (int) pts[0][xi + MU.makeSquareI(false, i, 4)][yi + MU.makeSquareI(true, i, 4)].getVec().getY();
                }
                p[xi][yi].xpoints = xp[xi][yi];
                p[xi][yi].ypoints = yp[xi][yi];
                p[xi][yi].reset();
                p[xi][yi].npoints = 4;
            }
        }
    }

    //draws the grid onto the screen
    public void paint(@NotNull Graphics2D g2d) {
        g2d.setColor(colorGridp);
        if (rotatey < 0) {
            g2d.setColor(colorGridn);
            for (int xi = 0; xi < side - 1; xi++) {
                for (int yi = 0; yi < side - 1; yi++) {
                    g2d.setColor(new Color(0.1f, 0.1f, 0.1f, 0.2f));
                    g2d.fill(p[xi][yi]);
                    g2d.setColor(colorGridn);
                    g2d.draw(p[xi][yi]);
                }
            }
        } else if (rotatey >= 0) {
            for (int xi = 0; xi < side - 1; xi++) {
                for (int yi = 0; yi < side - 1; yi++) {
                    g2d.setColor(new Color(0.1f, 0.1f, 0.1f, 0.4f));
                    g2d.fill(p[xi][yi]);
                    g2d.setColor(colorGridp);
                    g2d.draw(p[xi][yi]);
                }
            }
        }
        g2d.setColor(Color.GREEN);
        g2d.fill(selected);
    }


    public void paintAxis(@NotNull Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.drawLine(
                (int) pts[0][0][0].getVec().getX(),
                (int) pts[0][0][0].getVec().getY(),
                (int) pts[0][side - 1][0].getVec().getX(),
                (int) pts[0][side - 1][0].getVec().getY());
        g2d.setColor(Color.GREEN);
        g2d.drawLine(
                (int) pts[0][0][0].getVec().getX(),
                (int) pts[0][0][0].getVec().getY(),
                (int) pts[0][0][side - 1].getVec().getX(),
                (int) pts[0][0][side - 1].getVec().getY());
        g2d.setColor(Color.BLUE);
        g2d.drawLine(
                (int) pts[0][0][0].getVec().getX(),
                (int) pts[0][0][0].getVec().getY(),
                (int) pts[height - 1][0][0].getVec().getX(),
                (int) pts[height - 1][0][0].getVec().getY());
    }

    public void rotate(double direction) {
        if (rotate + direction < 0) {
            rotate += 360;
        }
        if (rotate + direction > 360) {
            rotate -= 360;
        }
        rotate += direction;
    }

    public void rotatey(int direction) {
        if (direction < 0) {
            if (rotatey + direction >= -90) {
                rotatey += direction;
            }
        }
        if (direction > 0) {
            if (rotatey + direction <= 90) {
                rotatey += direction;
            }
        }
    }

    public void zoom(int direction) {
        if (direction < 0) {
            if (zoom + direction >= 0) {
                zoom += direction;
            }
        }
        if (direction > 0) {
            if (zoom + direction <= 90) {
                zoom += direction;
            }
        }
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getSide() {
        return side;
    }

    public int getHeight() {
        return height;
    }

    public double getRotate() {
        return rotate;
    }

    public double getRotateY() {
        return rotatey;
    }

    @NotNull
    public GridPoint[][][] getPts() {
        return pts;
    }

    public double getZoom() {
        return zoom;
    }

    public void setRotate(double rotate) {
        this.rotate = rotate;
    }

    public void setRotatey(double rotatey) {
        this.rotatey = rotatey;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void gridInterfaceHover(int mx, int my) {
        if (!Canvas.isDetected()) {
            for (int x = 0; x < side - 1; x++) {
                for (int y = 0; y < side - 1; y++) {
                    if (p[x][y].contains(mx, my)) {
                        selected = p[x][y];
                        selectedX = x;
                        selectedY = y;
                    }
                }
            }
        } else {
            selected = Cube.getEmpty();
        }

    }

    public Polygon getHovered() {
        return selected;
    }

    public int getSelectedX() {
        return selectedX;
    }

    public int getSelectedY() {
        return selectedY;
    }

    public Polygon getSelected() {
        return selected;
    }

    public void setSelected(Polygon selected) {
        this.selected = selected;
    }

    public void showPoints(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        for (int x = 0; x < side; x++) {
            for (int y = 0; y < side; y++) {
                for (int z = 0; z < height; z++) {
                    g2d.fillRect((int) pts[z][x][y].getVec().getX(), (int) pts[z][x][y].getVec().getY(), 2, 2);
                }
            }
        }
    }
}
