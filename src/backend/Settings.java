package backend;

/**
 * Created by Blake on 7/14/2016.
 * settings which manipulate the canvas. these settings are controlled in the SettingsWindow component.
 */
public class Settings {

    //shows/hides the grid
    private boolean showGrid = true;

    //shows/hides the axis of the canvas
    private boolean showAxis = true;

    //shows/hides the coordinates of the corners of the canvas
    private boolean showCoords = true;

    //shows/hides the selected area
    private boolean showSelectedArea = true;


    //mutators////////////
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
    }

    public void setShowCoords(boolean showCoords) {
        this.showCoords = showCoords;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public void setShowSelectedArea(boolean showSelectedArea) {
        this.showSelectedArea = showSelectedArea;
    }
    //////////////////////

    //accessors///////////
    public boolean isShowAxis() {
        return showAxis;
    }

    public boolean isShowCoords() {
        return showCoords;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public boolean isShowSelectedArea() {
        return showSelectedArea;
    }
    ///////////////////////
}
