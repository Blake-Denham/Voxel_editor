package backend;
import org.jetbrains.annotations.NotNull;
import util.MU;
import util.Vector2D;

public class GridPoint {

    //The screen coordinate vector which falls on a circle
    @NotNull
    private final Vector2D vec;

    //x and y is origin of the circle
    //r is the radius
    //rotate how much vec moves along the circle
    //rotateOffset is added to rotate and is necessary to create a grid, with all points on different circles
    //rotatey scales the y component only using trigonometry, creating a vertical rotation effect.
    //zoom is a constant which scales the circle
    public GridPoint(int x, int y, int r, double rotate, double rotateOffset, double rotatey, double zoom) {
        double x_;
        double y_;
        x_ = x + (r * MU.rotate(rotate + rotateOffset).getX() * MU.rotatey(rotatey).getX() * MU.zoom(zoom).getX());
        y_ = y + (r * MU.rotate(rotate + rotateOffset).getY() * MU.rotatey(rotatey).getY() * MU.zoom(zoom).getY());
        vec = new Vector2D(x_, y_);
    }

    //practically calls the constructor each frame
    public void update(int x, int y, int r, double rotate, double rotateOffset, double rotatey, double zoom) {
        double x_;
        double y_;
        x_ = x + (r * MU.rotate(rotate + rotateOffset).getX() * MU.rotatey(rotatey).getX() * MU.zoom(zoom).getX());
        y_ = y + (r * MU.rotate(rotate + rotateOffset).getY() * MU.rotatey(rotatey).getY() * MU.zoom(zoom).getY());
        vec.update(x_, y_);
    }

    //returns the screen coordinate of vec
    @NotNull
    public Vector2D getVec() {
        return vec;
    }

    //sets vec to a specific point.
    public void setVec(double x, double y) {
        vec.update(x, y);
    }

}
