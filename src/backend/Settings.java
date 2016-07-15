package backend;

/**
 * Created by Blake on 7/14/2016.
 */
public class Settings {
    private boolean showGrid = true;
    private boolean showAxis = true;
    private boolean showCoords = true;

    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
    }

    public void setShowCoords(boolean showCoords) {
        this.showCoords = showCoords;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public boolean isShowAxis() {
        return showAxis;
    }

    public boolean isShowCoords() {
        return showCoords;
    }

    public boolean isShowGrid() {
        return showGrid;
    }
}
