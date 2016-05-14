package screen;

import guiTools.*;
import guiTools.Label;
import util.MU;
import util.PU;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Blake on 5/13/2016.
 */
public class CanvasInfo extends GuiComponent {
    private guiTools.Label cubes, rotate, rotatey, zoom;
    private guiTools.Slider rotateSlider, rotateySlider, zoomSlider;

    public CanvasInfo(double x, double y, double width, Color bgColor) {
        //noinspection SuspiciousNameCombination
        super(x, y, width, width, bgColor, 14, true);
        rotate = new Label(PU.getXInBounds(bounds, width * 0.15, 0.5), PU.getYInBounds(bounds, 25, 0.15), width * 0.15);
        rotateSlider = new Slider(PU.getXInBounds(bounds, width * 0.5, 0.5), PU.getYInBounds(bounds, 30, 0.2), Slider.HORIZONTAL, width * 0.5, 20, new Color(130, 130, 130), new Color(20, 20, 20));
        add(rotate);
        add(rotateSlider);
        rotatey = new Label(PU.getXInBounds(bounds, width * 0.15, 0.5), PU.getYInBounds(bounds, 25, 0.42), width * 0.15);
        rotateySlider = new Slider(PU.getXInBounds(bounds, width * 0.5, 0.5), PU.getYInBounds(bounds, 30, 0.53), Slider.HORIZONTAL, width * 0.5, 20, new Color(130, 130, 130), new Color(20, 20, 20));
        add(rotatey);
        add(rotateySlider);
        zoom = new Label(PU.getXInBounds(bounds, width * 0.15, 0.5), PU.getYInBounds(bounds, 25, 0.42 + 0.27), width * 0.15);
        zoomSlider = new Slider(PU.getXInBounds(bounds, width * 0.5, 0.5), PU.getYInBounds(bounds, 30, 0.53 + 0.33), Slider.HORIZONTAL, width * 0.5, 20, new Color(130, 130, 130), new Color(20, 20, 20));
        add(zoom);
        add(zoomSlider);
        cubes = new Label(PU.getXInBounds(bounds, width * 0.45, 0.5), PU.getYInBounds(bounds, 25, 0.42 + 0.27 + 0.27), width * 0.45);
        cubes.setBackgroundColor(new Color(150, 150, 150));
        add(cubes);
        setToolBarTitle("CANVAS INFO");
    }

    @Override
    protected void paintGuiComponent(Graphics2D g2d) {

    }

    @Override
    protected void update() {
        double y_;
        if (ComponentManager.getColorWheel().getMinimized() < 0) {
            y_ = ComponentManager.getColorWheel().getY() + 15 + 30;
        } else {
            y_ = ComponentManager.getColorWheel().getY() + ComponentManager.getColorWheel().getHeight() + 15 + 30;
        }
        setBounds(EditorScreen.s_maxWidth * (1 - MU.getPercent(305, 1920)) - 16, y_, EditorScreen.s_maxWidth * MU.getPercent(300, 1920), EditorScreen.s_maxWidth * MU.getPercent(300, 1920) * 0.8);


        rotate.setDisplay((int) (ComponentManager.getCanvas().getGrid().getRotate()) + "");
        rotate.setBounds(PU.getXInBounds(bounds, width * 0.15, 0.5), PU.getYInBounds(bounds, EditorScreen.s_maxHeight*MU.getPercent(25,1080), 0.05), width * 0.15, EditorScreen.s_maxHeight*MU.getPercent(25,1080));
        rotateSlider.setBounds(PU.getXInBounds(bounds, width * 0.5, 0.5), PU.getYInBounds(bounds, EditorScreen.s_maxHeight*MU.getPercent(25,1080), 0.2), width * 0.5, EditorScreen.s_maxHeight*MU.getPercent(25,1080));
        if (rotateSlider.getChanging())
            ComponentManager.setRotateCamera((int) ((rotateSlider.getPercent()) * 360));
        rotateSlider.setPercent((float) (ComponentManager.getCanvas().getGrid().getRotate() / 360));
        rotatey.setDisplay((int) (ComponentManager.getCanvas().getGrid().getRotateY()) + "");
        rotatey.setBounds(PU.getXInBounds(bounds, width * 0.15, 0.5), PU.getYInBounds(bounds, EditorScreen.s_maxHeight*MU.getPercent(25,1080), 0.37), width * 0.15,EditorScreen.s_maxHeight*MU.getPercent(25,1080));
        rotateySlider.setBounds(PU.getXInBounds(bounds, width * 0.5, 0.5), PU.getYInBounds(bounds, EditorScreen.s_maxHeight*MU.getPercent(25,1080), 0.53), width * 0.5, EditorScreen.s_maxHeight*MU.getPercent(25,1080));
        if (rotateySlider.getChanging())
            ComponentManager.setRotateyCamera((int) (((rotateySlider.getPercent()) * 180) - 90));
        rotateySlider.setPercent((float) ((ComponentManager.getCanvas().getGrid().getRotateY() + 90) / 180));
        zoom.setDisplay((int) (MU.sin(ComponentManager.getCanvas().getGrid().getZoom()) * 100) + "");
        zoom.setBounds(PU.getXInBounds(bounds, width * 0.15, 0.5), PU.getYInBounds(bounds, EditorScreen.s_maxHeight*MU.getPercent(25,1080), 0.37 + 0.32), width * 0.15, EditorScreen.s_maxHeight*MU.getPercent(25,1080));
        zoomSlider.setBounds(PU.getXInBounds(bounds, width * 0.5, 0.5), PU.getYInBounds(bounds, EditorScreen.s_maxHeight*MU.getPercent(25,1080), 0.53 + 0.33), width * 0.5, EditorScreen.s_maxHeight*MU.getPercent(25,1080));
        if (zoomSlider.getChanging())
            ComponentManager.setZoomCamera((int) (zoomSlider.getPercent() * 90));
        zoomSlider.setPercent((float) ((ComponentManager.getCanvas().getGrid().getZoom()) / 90));


    }

    @Override
    protected void hover(MouseEvent e) {

    }

    @Override
    protected void click(MouseEvent e) {

    }

    @Override
    protected void drag(MouseEvent e) {

    }

    @Override
    protected void scroll(MouseWheelEvent mwe) {

    }

    @Override
    protected void keyPress(KeyEvent e) {

    }

    @Override
    protected void keyRelease(KeyEvent e) {

    }

    @Override
    protected void mousePress(MouseEvent e) {

    }

    @Override
    protected void mouseRelease(MouseEvent e) {

    }
}
