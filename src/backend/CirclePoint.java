package backend;

import util.MU;
import util.Vector2D;

class CirclePoint {
    //ints------------------
    private final int pnts;
    //----------------------

    //Vectors---------------
    private final Vector2D[] pts;
    //----------------------

    //constructor/////////////////////////////////////////////////////////////////////////////////////////////////////
    public CirclePoint(int pnts, int x, int y, int r, double rotate, double rotatex, double rotatey, double rotatez) {
        this.pnts = pnts;
        int x_;
        int y_;
        pts = new Vector2D[pnts];
        for (int i = 0; i < pnts; i++) {
            x_ = (int) (x + r * MU.rotate(i, pnts, rotate).getX() * MU.rotatex(rotatex).getX() * MU.rotatey(rotatey).getX() * MU.zoom(rotatez).getX());
            y_ = (int) (y + r * MU.rotate(i, pnts, rotate).getY() * MU.rotatex(rotatex).getY() * MU.rotatey(rotatey).getY() * MU.zoom(rotatez).getY());
            pts[i] = new Vector2D(x_, y_);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //drawing and graphics update////////////////////////////////////////////////////////////////////////////////
    public void update(int x, int y, int r, double rotate, double rotatex, double rotatey, double rotatez) {
        int x_;
        int y_;
        for (int i = 0; i < pnts; i++) {
            x_ = (int) (x + r * MU.rotate(i, pnts, rotate).getX() * MU.rotatex(rotatex).getX() * MU.rotatey(rotatey).getX() * MU.zoom(rotatez).getX());
            y_ = (int) (y + r * MU.rotate(i, pnts, rotate).getY() * MU.rotatex(rotatex).getY() * MU.rotatey(rotatey).getY() * MU.zoom(rotatez).getY());
            pts[i].update(x_, y_);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //accessors////////////////////////////////
    public Vector2D[] getPts() {
        return pts;
    }
    ///////////////////////////////////////////

}
