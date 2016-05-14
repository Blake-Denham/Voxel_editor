package backend;

import screen.Canvas;
import screen.EditorScreen;
import util.MU;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Color;

public class Grid {

    /**
     * -use
     * class is used for creating 3-Dimensional space which can my rotated and scaled
     * @see GridPoint is combined
     * @see CirclePoint
     */
    //ints-------------------------
    private int x;
    private int y;
    private final int height;
    private final int side;
    private final int[][][] xp;
    private final int[][][] yp;
    //-----------------------------

    //doubles----------------------------
    private double rotate, rotatey, zoom;
    //-----------------------------------

    //Crosses---------------
    private final GridPoint[][][] pts;
    //----------------------

    //Polygons---------------
    private final Polygon[][] p;
    //-----------------------

    //Circles-------------------------
    private final CirclePoint[] zAxis;
    private final CirclePoint cent;
    //--------------------------------

    //Colors------------------------------------------------------
    private static final Color colorGridp = new Color(1f, 1f, 1f, 0.6f);
    private static final Color colorGridn = new Color(1f, 1f, 1f, 0.3f);
    //------------------------------------------------------------

    //Constructor////////////////////////////////////////////////////////////////////////////////////
    public Grid(int side, int height, int x, int y, double rotate, double rotatey, double zoom) {
        /**
         * creates the 3D space with specified location, dimensions, rotations and scale
         * as point falls on a circle of radius r, offset by p degrees the points are
         * made using the formulae.
         * r = squareRoot((100*side*xPoint*cos(45)+100*side**yPoint*sin(45))^2).
         * p = (180*arctan(yPoint/xPoint))/PI.
         * a circle,with a radius of root(200)*n,n increments until it is equal to height, and side^2 crosses are made at every rotatey of each one of the circles which were created
         */
        this.y = y;
        this.x = x;
        this.rotate = rotate;
        this.rotatey = rotatey;
        this.zoom = zoom;
        this.side = side;
        this.height = height;
        zAxis = new CirclePoint[height];
        cent = new CirclePoint(90, x, y, (int) (Math.sqrt(MU.square(100 * side * MU.cos(45)) + MU.square(100 * side * MU.sin(45)))), -rotate, 90, rotatey, zoom);
        pts = new GridPoint[height][side][side];
        int y_ = (int) cent.getPts()[0].getY();
        int x_ = (int) cent.getPts()[0].getX();
        for (int z = 0; z < height; z++) {
            for (int xi = 0; xi < side; xi++) {
                for (int yi = 0; yi < side; yi++) {
                    pts[z][xi][yi] = new GridPoint(x_, y_, (int) (Math.sqrt(MU.square(100 * (xi) * MU.cos(45)) + MU.square(100 * yi * MU.sin(45)))), rotate, Math.toDegrees(MU.arctan((double) yi / (double) xi)), rotatey, zoom);
                }
            }
            pts[z][0][0].setVec(x_, y_);
            zAxis[z] = new CirclePoint(1, x_, y_, 50 * z, 90, 0, rotatey - 90, zoom);
            y_ = (int) (zAxis[z].getPts()[0].getY());
        }
        p = new Polygon[(side) - 1][side - 1];
        xp = new int[side][side][4];
        yp = new int[side][side][4];
        for (int xi = 0; xi < side - 1; xi++) {
            for (int yi = 0; yi < side - 1; yi++) {
                for (int i = 0; i < 4; i++) {
                    xp[xi][yi][i] = (int) pts[0][xi + MU.makeSquareI(false, i, 4)][yi + MU.makeSquareI(true, i, 4)].getVecs().getX();
                    yp[xi][yi][i] = (int) pts[0][xi + MU.makeSquareI(false, i, 4)][yi + MU.makeSquareI(true, i, 4)].getVecs().getY();
                }
                p[xi][yi] = new Polygon(xp[xi][yi], yp[xi][yi], 4);
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

    //drawing and updates//////////////////////
    public void update() {

        cent.update(x, y, (int) (Math.sqrt(MU.square(100 * (side - 1) * MU.cos(45)) + MU.square(100 * (side - 1) * MU.sin(45))) / 2), rotate - 135, 90, rotatey, zoom);
        int x_ = (int) cent.getPts()[0].getX();
        int y_ = (int) cent.getPts()[0].getY();
        for (int zi = 0; zi < height; zi++) {
            for (int xi = 0; xi < side; xi++) {
                for (int yi = 0; yi < side; yi++) {
                    pts[zi][xi][yi].update(x_, y_, (int) (Math.sqrt(MU.square(100 * (xi) * MU.cos(45)) + MU.square(100 * yi * MU.sin(45)))), rotate, Math.toDegrees(MU.arctan((double) yi / (double) xi)), rotatey, zoom);
                }
            }
            pts[zi][0][0].setVec(x_, y_);
            zAxis[zi].update(x_, (int) (y - (y - cent.getPts()[0].getY())), (int) (zi * Math.abs(Math.sqrt(MU.square(100 * MU.cos(45)) + MU.square(100 * MU.sin(45))) - Math.sqrt(MU.square(100 * 2 * MU.cos(45)) + MU.square(100 * 2 * MU.sin(45)))) / Math.sqrt(2)), 90, 0, rotatey - 90, zoom);
            y_ = (int) (zAxis[zi].getPts()[0].getY());
        }
        for (int xi = 0; xi < side - 1; xi++) {
            for (int yi = 0; yi < side - 1; yi++) {
                for (int i = 0; i < 4; i++) {
                    xp[xi][yi][i] = (int) pts[0][xi + MU.makeSquareI(false, i, 4)][yi + MU.makeSquareI(true, i, 4)].getVecs().getX();
                    yp[xi][yi][i] = (int) pts[0][xi + MU.makeSquareI(false, i, 4)][yi + MU.makeSquareI(true, i, 4)].getVecs().getY();
                }
                p[xi][yi].xpoints = xp[xi][yi];
                p[xi][yi].ypoints = yp[xi][yi];
                p[xi][yi].reset();
                p[xi][yi].npoints = 4;
            }
        }
    }

    public void paint(Graphics2D g2d) {
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
                    g2d.setColor(new Color(0.1f, 0.1f, 0.1f, 1f));
                    g2d.fill(p[xi][yi]);
                    g2d.setColor(colorGridn);
                    g2d.draw(p[xi][yi]);
                }
            }
        }
    }

    public void paintAxis(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.drawLine((int) pts[0][0][0].getVecs().getX(), (int) pts[0][0][0].getVecs().getY(), (int) pts[0][side - 1][0].getVecs().getX(), (int) pts[0][side - 1][0].getVecs().getY());
        g2d.setColor(Color.GREEN);
        g2d.drawLine((int) pts[0][0][0].getVecs().getX(), (int) pts[0][0][0].getVecs().getY(), (int) pts[0][0][side - 1].getVecs().getX(), (int) pts[0][0][side - 1].getVecs().getY());
        g2d.setColor(Color.BLUE);
        g2d.drawLine((int) pts[0][0][0].getVecs().getX(), (int) pts[0][0][0].getVecs().getY(), (int) pts[height - 1][0][0].getVecs().getX(), (int) pts[height - 1][0][0].getVecs().getY());
    }
    ///////////////////////////////////////////

    //mutators////////////////////////////////////////////////////
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
    /////////////////////////////////////////////////////////////

    //user input///////////////////////////
    public void hover(Rectangle pnt) {
    }
    ///////////////////////////////////////

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

    public GridPoint[][][] getPts() {
        return pts;
    }

    public double getZoom() {
        return zoom;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public void setRotatey(int rotatey) {
        this.rotatey = rotatey;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }



    ////////////////////////////////////////////////
}
