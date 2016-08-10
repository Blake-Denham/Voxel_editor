package editorScreen;

import guiTools.Button;
import guiTools.GuiComponent;
import util.PU;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;

/**
 * Created by Blake on 8/10/2016.
 */
public class HelpWindow extends GuiComponent {
    private guiTools.Button helpButton;

    public HelpWindow(double x, double y, double width, double height) {
        super(x, y, width, height, ComponentManager.bgColor, 14, true);

        try {
            Image b = ImageIO.read(Main.getResource("Images/help256.png"));
            helpButton = new Button(0, 0, 0, 0, b, "help screen", () -> Main.openHelpWindow());
            add(helpButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setToolBarTitle("HELP");

    }

    @Override
    protected void paintGuiComponent(Graphics2D g2d) {

    }

    @Override
    protected void update() {
        double y_;
        if (ComponentManager.getSettingsWindow().getMinimized() < 0) {
            y_ = ComponentManager.getSettingsWindow().getY() + 15 + 30;
        } else {
            y_ = ComponentManager.getSettingsWindow().getY() + ComponentManager.getSettingsWindow().getHeight() + 15 + 30;
        }
        setBounds(ComponentManager.getSettingsWindow().getX(), y_, ComponentManager.getSettingsWindow().getWidth(), 160);
        helpButton.setBounds(PU.getXInBounds(bounds, 128, 0.5), PU.getYInBounds(bounds, 128, 0.5), 128, 128);
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
