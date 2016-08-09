package backend;
import org.jetbrains.annotations.NotNull;
import util.MU;
import util.Vector2D;

public class GridPoint {

    @NotNull
    private final Vector2D vecs;

    public GridPoint(int x, int y, int r, double rotate, double rotateOffset, double rotatey, double zoom) {
        double x_;
        double y_;
        x_ = x + (r * MU.rotate(rotate + rotateOffset).getX() * MU.rotatey(rotatey).getX() * MU.zoom(zoom).getX());
        y_ = y + (r * MU.rotate(rotate + rotateOffset).getY() * MU.rotatey(rotatey).getY() * MU.zoom(zoom).getY());
        vecs = new Vector2D(x_, y_);
    }

    public void update(int x, int y, int r, double rotate, double rotateOffset, double rotatey, double zoom) {
        double x_;
        double y_;
        x_ = x + (r * MU.rotate(rotate + rotateOffset).getX() * MU.rotatey(rotatey).getX() * MU.zoom(zoom).getX());
        y_ = y + (r * MU.rotate(rotate + rotateOffset).getY() * MU.rotatey(rotatey).getY() * MU.zoom(zoom).getY());
        vecs.update(x_, y_);
    }

    @NotNull
    public Vector2D getVecs() {
        return vecs;
    }

    public void setVec(double x, double y) {
        vecs.update(x, y);
    }

}
