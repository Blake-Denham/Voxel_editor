package backend;

import org.jetbrains.annotations.NotNull;
import util.MU;
import util.Vector2D;

class CirclePoint {
    //the amount of equally spaced points on a rotatable circle
    private final int pnts;

    //the coordinates of each point on the circle
    @NotNull
    private final Vector2D[] pts;

    //constructor sets a circle at origin 'x', 'y' with radius 'r';
    // rotation offset of 'rotate' degrees rotated in 3D either 'rotatex' degrees horizontally
    //or 'rotatey' degrees vertically, finally scaled by 'scale'
    public CirclePoint(int pnts, int x, int y, int r, double rotate, double rotatex, double rotatey, double scale) {
        this.pnts = pnts;
        int x_;
        int y_;
        pts = new Vector2D[pnts];
        for (int i = 0; i < pnts; i++) {
            // what is happening is the MU.rotate(), MU.rotatex(), MU.rotatey() and MU.zoom() each return a vector2D which are all multiplied together
            //to give a single point on a circle.
            //see MU.rotate(), MU.rotatex(), MU.rotatey() and MU.zoom() for details on the functions.

            //the x_ and y_ value will be a coordinate on the screen
            x_ = (int) (x + r * MU.rotate(i, pnts, rotate).getX() * MU.rotatex(rotatex).getX() * MU.rotatey(rotatey).getX() * MU.zoom(scale).getX());
            y_ = (int) (y + r * MU.rotate(i, pnts, rotate).getY() * MU.rotatex(rotatex).getY() * MU.rotatey(rotatey).getY() * MU.zoom(scale).getY());
            pts[i] = new Vector2D(x_, y_);
        }
    }

    //essentially the same as the constructor, allows for manipulation of the circle, the number of points of the circle cannot be changed
    public void update(int x, int y, int r, double rotate, double rotatex, double rotatey, double zoom) {
        int x_;
        int y_;
        for (int i = 0; i < pnts; i++) {
            x_ = (int) (x + r * MU.rotate(i, pnts, rotate).getX() * MU.rotatex(rotatex).getX() * MU.rotatey(rotatey).getX() * MU.zoom(zoom).getX());
            y_ = (int) (y + r * MU.rotate(i, pnts, rotate).getY() * MU.rotatex(rotatex).getY() * MU.rotatey(rotatey).getY() * MU.zoom(zoom).getY());
            pts[i].update(x_, y_);
        }
    }

    //returns the points of the circle
    @NotNull
    public Vector2D[] getPts() {
        return pts;
    }

}
