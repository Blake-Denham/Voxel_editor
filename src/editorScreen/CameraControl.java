package editorScreen;

import guiTools.Button;
import guiTools.GuiComponent;
import guiTools.Label;
import guiTools.Slider;
import org.jetbrains.annotations.NotNull;
import util.MU;
import util.PU;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;

/**
 * Created by Blake on 5/13/2016.
 */

public class CameraControl extends GuiComponent {
    //the label which shows the rotate value of the canvas
    @NotNull
    private final guiTools.Label rotate;
    //the label which shows the vertical rotation of the canvas
    @NotNull
    private final guiTools.Label rotatey;
    //the label which shows the zoom percentage of the canvas
    @NotNull
    private final guiTools.Label zoom;
    //a slider which is used to rotate the canvas horizontally
    @NotNull
    private final guiTools.Slider rotateSlider;
    //a slider which rotates the canvas vertically
    @NotNull
    private final guiTools.Slider rotateySlider;
    //a slider which zooms in or out
    @NotNull
    private final guiTools.Slider zoomSlider;
    //a button which makes the model rotate horizontally without any user interaction
    @NotNull
    private final guiTools.Button rotator;
    //a button which makes the model rotate vertically without any user interaction
    @NotNull
    private final guiTools.Button rotatorY;
    //sets the canvas to an isometric view, ie: 45 degrees horizontal and 36 degrees vertical
    @NotNull
    private final guiTools.Button isometricView;
    //Integers used to control the horizontal and vertical rotation speed and direction
    private int rotationx = -1, rotationy = -1;

    private int direction = 1;

    //the screen coordinates where the panel is drawn
    public CameraControl(double x, double y, double width, Color bgColor) {
        //noinspection SuspiciousNameCombination
        super(x, y, width, width, bgColor, 14, true);
        Image rotateImage = null;
        Image isometricImg = null;

        try {
            rotateImage = ImageIO.read(Main.getResource("Images/rotate.png"));
            isometricImg = ImageIO.read(Main.getResource("Images/isometric.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        rotate = new Label(0, 0, 0);
        rotateSlider = new Slider(0, 0, Slider.HORIZONTAL, 0, 0, new Color(130, 130, 130), new Color(20, 20, 20));
        assert rotateImage != null;
        assert isometricImg != null;
        rotator = new Button(0, 0, 0, 0, rotateImage, "rotate horizontally", () -> rotationx *= -1);
        add(rotate);
        add(rotateSlider);
        add(rotator);
        rotatey = new Label(0, 0, 0);
        rotateySlider = new Slider(0, 0, Slider.HORIZONTAL, 0, 0, new Color(130, 130, 130), new Color(20, 20, 20));
        rotatorY = new Button(0, 0, 0, 0, rotateImage, "rotate vertically", () -> rotationy *= -1);
        add(rotatey);
        add(rotateySlider);
        add(rotatorY);
        zoom = new Label(0, 0, 0);
        zoomSlider = new Slider(0, 0, Slider.HORIZONTAL, 0, 0, new Color(130, 130, 130), new Color(20, 20, 20));
        add(zoom);
        add(zoomSlider);
        isometricView = new Button(0, 0, 0, 0, isometricImg, "show isometric view", () -> {
            ComponentManager.setRotateCamera(45);
            ComponentManager.setRotateyCamera(35);
        });
        add(isometricView);
        setID("camera control");
        setToolBarTitle("CAMERA CONTROL");
    }

    //paint method, will be called to draw the component on the screen
    @Override
    protected void paintGuiComponent(@NotNull Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.setFont(EditorScreen.font.deriveFont(11f));
        g2d.drawString(
                "Rotate x:",
                (int) PU.getXInBounds(bounds, g2d.getFontMetrics().stringWidth("Rotate x:"), 0.24 + 0.07),
                (int) (PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.05) + g2d.getFontMetrics().getHeight() - g2d.getFontMetrics().getHeight() / 6));

        g2d.drawString(
                "Rotate y:",
                (int) PU.getXInBounds(bounds, g2d.getFontMetrics().stringWidth("Rotate y:"), 0.24 + 0.07),
                (int) (PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.37) + g2d.getFontMetrics().getHeight() - g2d.getFontMetrics().getHeight() / 6));
        g2d.drawString(
                "Scale:",
                (int) PU.getXInBounds(bounds, g2d.getFontMetrics().stringWidth("Scale:"), 0.24 + 0.06),
                (int) (PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.69) + g2d.getFontMetrics().getHeight() - g2d.getFontMetrics().getHeight() / 6 + 1));
    }

    @Override
    protected void update() {
        double y_;
        if (ComponentManager.getColorWheel().getMinimized() < 0) {
            y_ = ComponentManager.getColorWheel().getY() + 15 + 30;
        } else {
            y_ = ComponentManager.getColorWheel().getY() + ComponentManager.getColorWheel().getHeight() + 15 + 30;
        }
        setBounds(EditorScreen.s_maxWidth * (1 - MU.getPercent(305 + 58, 1920)) - 16, y_, EditorScreen.s_maxWidth * MU.getPercent(300 + 58, 1920), EditorScreen.s_maxWidth * MU.getPercent(300, 1920) * 0.8);


        rotate.setDisplay((int) (ComponentManager.getCanvas().getGrid().getRotate()) + "");
        rotate.setBounds(
                PU.getXInBounds(bounds, width * 0.15, 0.5 + 0.07),
                PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.05),
                width * 0.15,
                EditorScreen.s_maxHeight * MU.getPercent(25, 1080));

        rotateSlider.setBounds(
                PU.getXInBounds(bounds, width * 0.5, 0.5),
                PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.2),
                width * 0.5,
                EditorScreen.s_maxHeight * MU.getPercent(25, 1080));

        if (rotateSlider.getChanging()) {
            ComponentManager.setRotateCamera((int) ((rotateSlider.getPercent()) * 360));
        }

        rotateSlider.setPercent((float) (ComponentManager.getCanvas().getGrid().getRotate() / 360));
        rotator.setBounds(
                PU.getXInBounds(bounds, width * 0.1, 0. + 0.14),
                PU.getYInBounds(bounds, width * 0.1, 0.12),
                width * 0.1,
                width * 0.1);


        isometricView.setBounds(
                PU.getXInBounds(bounds, width * 0.1, 0.72),
                PU.getYInBounds(bounds, width * 0.1, 0.03),
                width * 0.1,
                width * 0.1);


        rotatey.setDisplay((int) (ComponentManager.getCanvas().getGrid().getRotateY()) + "");
        rotatey.setBounds(
                PU.getXInBounds(bounds, width * 0.15, 0.5 + 0.07),
                PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.37),
                width * 0.15,
                EditorScreen.s_maxHeight * MU.getPercent(25, 1080));

        rotateySlider.setBounds(
                PU.getXInBounds(bounds, width * 0.5, 0.5),
                PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.53),
                width * 0.5,
                EditorScreen.s_maxHeight * MU.getPercent(25, 1080));

        if (rotateySlider.getChanging()) {
            ComponentManager.setRotateyCamera((int) (((rotateySlider.getPercent()) * 180) - 90));
        }
        rotateySlider.setPercent((float) ((ComponentManager.getCanvas().getGrid().getRotateY() + 90) / 180));

        rotatorY.setBounds(
                PU.getXInBounds(bounds, width * 0.1, 0. + 0.14),
                PU.getYInBounds(bounds, width * 0.1, 0.43),
                width * 0.1,
                width * 0.1);


        zoom.setDisplay((int) (MU.sin(ComponentManager.getCanvas().getGrid().getZoom()) * 100) + "");
        zoom.setBounds(
                PU.getXInBounds(bounds, width * 0.15, 0.5 + 0.07),
                PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.37 + 0.32),
                width * 0.15,
                EditorScreen.s_maxHeight * MU.getPercent(25, 1080));

        zoomSlider.setBounds(
                PU.getXInBounds(bounds, width * 0.5, 0.5),
                PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.53 + 0.33),
                width * 0.5,
                EditorScreen.s_maxHeight * MU.getPercent(25, 1080));

        if (zoomSlider.getChanging()) {
            ComponentManager.setZoomCamera((int) (zoomSlider.getPercent() * 90));
        }

        zoomSlider.setPercent((float) ((ComponentManager.getCanvas().getGrid().getZoom()) / 90));

        if (rotationx > 0) {
            ComponentManager.setRotateCamera((int) (ComponentManager.getCanvas().getGrid().getRotate() + 1));
        }
        if (rotationy > 0) {
            if (ComponentManager.getCanvas().getGrid().getRotateY() == 90 || ComponentManager.getCanvas().getGrid().getRotateY() == -90)
                direction *= -1;
            ComponentManager.setRotateyCamera((int) (ComponentManager.getCanvas().getGrid().getRotateY() + direction));
        }

    }

    @Override
    protected void hover(MouseEvent e) {

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
    protected void mousePress(MouseEvent e) {

    }
    @Override
    protected void mouseRelease(MouseEvent e) {

    }
}
