package screen;

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
@SuppressWarnings("DefaultFileTemplate")
public class CameraControl extends GuiComponent {
    @NotNull
    private final guiTools.Label cubes;
    @NotNull
    private final guiTools.Label rotate;
    @NotNull
    private final guiTools.Label rotatey;
    @NotNull
    private final guiTools.Label zoom;
    @NotNull
    private final guiTools.Slider rotateSlider;
    @NotNull
    private final guiTools.Slider rotateySlider;
    @NotNull
    private final guiTools.Slider zoomSlider;
    @NotNull
    private final guiTools.Button rotator;
    @NotNull
    private final guiTools.Button rotatorY;
    private int rotationx = -1, rotationy = -1;
    private int direction = 1;


    public CameraControl(double x, double y, double width, Color bgColor) {
        //noinspection SuspiciousNameCombination
        super(x, y, width, width, bgColor, 14, true);
        Image rotateImage = null;
        try {
            rotateImage = ImageIO.read(Main.getResource("rotate.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        rotate = new Label(PU.getXInBounds(bounds, width * 0.15, 0.5), PU.getYInBounds(bounds, 25, 0.15), width * 0.15);
        rotateSlider = new Slider(PU.getXInBounds(bounds, width * 0.5, 0.5), PU.getYInBounds(bounds, 30, 0.2), Slider.HORIZONTAL, width * 0.5, 20, new Color(130, 130, 130), new Color(20, 20, 20));
        assert (rotateImage != null ? rotateImage : null) != null;
        rotator = new Button(PU.getXInBounds(bounds, width * 0.1, 0. + 0.14), PU.getYInBounds(bounds, width * 0.1, 0.15), width * 0.1, width * 0.1, rotateImage, () -> rotationx *= -1);
        add(rotate);
        add(rotateSlider);
        add(rotator);
        rotatey = new Label(PU.getXInBounds(bounds, width * 0.15, 0.5), PU.getYInBounds(bounds, 25, 0.42), width * 0.15);
        rotateySlider = new Slider(PU.getXInBounds(bounds, width * 0.5, 0.5), PU.getYInBounds(bounds, 30, 0.53), Slider.HORIZONTAL, width * 0.5, 20, new Color(130, 130, 130), new Color(20, 20, 20));
        rotatorY = new Button(PU.getXInBounds(bounds, width * 0.1, 0. + 0.14), PU.getYInBounds(bounds, width * 0.1, 0.37), width * 0.1, width * 0.1, rotateImage, () -> rotationy *= -1);
        add(rotatey);
        add(rotateySlider);
        add(rotatorY);
        zoom = new Label(PU.getXInBounds(bounds, width * 0.15, 0.5), PU.getYInBounds(bounds, 25, 0.42 + 0.27), width * 0.15);
        zoomSlider = new Slider(PU.getXInBounds(bounds, width * 0.5, 0.5), PU.getYInBounds(bounds, 30, 0.53 + 0.33), Slider.HORIZONTAL, width * 0.5, 20, new Color(130, 130, 130), new Color(20, 20, 20));
        add(zoom);
        add(zoomSlider);
        cubes = new Label(PU.getXInBounds(bounds, width * 0.45, 0.5), PU.getYInBounds(bounds, 25, 0.42 + 0.27 + 0.27), width * 0.45);
        cubes.setBackgroundColor(new Color(150, 150, 150));
        add(cubes);
        setToolBarTitle("CAMERA CONTROL");
    }

    @Override
    protected void paintGuiComponent(@NotNull Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.drawString("Rotate x:", (int) PU.getXInBounds(bounds, g2d.getFontMetrics().stringWidth("Rotate x:"), 0.24 + 0.07), (int) (PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.05) + g2d.getFontMetrics().getHeight() - g2d.getFontMetrics().getHeight() / 6));
        g2d.drawString("Rotate y:", (int) PU.getXInBounds(bounds, g2d.getFontMetrics().stringWidth("Rotate y:"), 0.24 + 0.07), (int) (PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.37) + g2d.getFontMetrics().getHeight() - g2d.getFontMetrics().getHeight() / 6));
        g2d.drawString("Scale:", (int) PU.getXInBounds(bounds, g2d.getFontMetrics().stringWidth("Scale:"), 0.24 + 0.06), (int) (PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.69) + g2d.getFontMetrics().getHeight() - g2d.getFontMetrics().getHeight() / 6 + 1));
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
        rotate.setBounds(PU.getXInBounds(bounds, width * 0.15, 0.5 + 0.07), PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.05), width * 0.15, EditorScreen.s_maxHeight * MU.getPercent(25, 1080));
        rotateSlider.setBounds(PU.getXInBounds(bounds, width * 0.5, 0.5), PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.2), width * 0.5, EditorScreen.s_maxHeight * MU.getPercent(25, 1080));
        if (rotateSlider.getChanging()) ComponentManager.setRotateCamera((int) ((rotateSlider.getPercent()) * 360));
        rotateSlider.setPercent((float) (ComponentManager.getCanvas().getGrid().getRotate() / 360));
        rotator.setBounds(PU.getXInBounds(bounds, width * 0.1, 0. + 0.14), PU.getYInBounds(bounds, width * 0.1, 0.12), width * 0.1, width * 0.1);
        rotatey.setDisplay((int) (ComponentManager.getCanvas().getGrid().getRotateY()) + "");
        rotatey.setBounds(PU.getXInBounds(bounds, width * 0.15, 0.5 + 0.07), PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.37), width * 0.15, EditorScreen.s_maxHeight * MU.getPercent(25, 1080));
        rotateySlider.setBounds(PU.getXInBounds(bounds, width * 0.5, 0.5), PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.53), width * 0.5, EditorScreen.s_maxHeight * MU.getPercent(25, 1080));
        if (rotateySlider.getChanging())
            ComponentManager.setRotateyCamera((int) (((rotateySlider.getPercent()) * 180) - 90));
        rotateySlider.setPercent((float) ((ComponentManager.getCanvas().getGrid().getRotateY() + 90) / 180));
        rotatorY.setBounds(PU.getXInBounds(bounds, width * 0.1, 0. + 0.14), PU.getYInBounds(bounds, width * 0.1, 0.43), width * 0.1, width * 0.1);
        zoom.setDisplay((int) (MU.sin(ComponentManager.getCanvas().getGrid().getZoom()) * 100) + "");
        zoom.setBounds(PU.getXInBounds(bounds, width * 0.15, 0.5 + 0.07), PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.37 + 0.32), width * 0.15, EditorScreen.s_maxHeight * MU.getPercent(25, 1080));
        zoomSlider.setBounds(PU.getXInBounds(bounds, width * 0.5, 0.5), PU.getYInBounds(bounds, EditorScreen.s_maxHeight * MU.getPercent(25, 1080), 0.53 + 0.33), width * 0.5, EditorScreen.s_maxHeight * MU.getPercent(25, 1080));
        if (zoomSlider.getChanging()) ComponentManager.setZoomCamera((int) (zoomSlider.getPercent() * 90));
        zoomSlider.setPercent((float) ((ComponentManager.getCanvas().getGrid().getZoom()) / 90));
        if (rotationx > 0)
            ComponentManager.setRotateCamera((int) (ComponentManager.getCanvas().getGrid().getRotate() + 1));
        if (rotationy > 0)
            if (ComponentManager.getCanvas().getGrid().getRotateY() == 90 || ComponentManager.getCanvas().getGrid().getRotateY() == -90)
                direction *= -1;
        ComponentManager.setRotateyCamera((int) (ComponentManager.getCanvas().getGrid().getRotateY() + direction));
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
