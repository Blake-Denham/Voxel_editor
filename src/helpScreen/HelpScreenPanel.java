package helpScreen;

import editorScreen.Main;
import guiTools.Button;
import guiTools.GuiComponent;
import util.PU;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;

public class HelpScreenPanel extends GuiComponent {
    private guiTools.Button addHelp, removeHelp, selectHelp, paintHelp;
    private HelpInfo hi;
    private Rectangle selected;

    public HelpScreenPanel() {
        super(0, 0, HelpScreen.getScreenWidth(), HelpScreen.getScreenHeight());
        hi = new HelpInfo();
        try {
            Image b1 = ImageIO.read(Main.getResource("Images/add256.png"));
            Image b2 = ImageIO.read(Main.getResource("Images/remove256.png"));
            Image b3 = ImageIO.read(Main.getResource("Images/select256.png"));
            Image b4 = ImageIO.read(Main.getResource("Images/paintBrush256.png"));
            addHelp = new Button(0, 0, 0, 0, b1, "add help", () -> hi.setHelpPage(HelpInfo.ADD_HELP_PAGE));
            removeHelp = new Button(0, 0, 0, 0, b2, "remove help", () -> hi.setHelpPage(HelpInfo.REMOVE_HELP_PAGE));
            selectHelp = new Button(0, 0, 0, 0, b3, "select help", () -> hi.setHelpPage(HelpInfo.SELECT_HELP_PAGE));
            paintHelp = new Button(0, 0, 0, 0, b4, "paint help", () -> hi.setHelpPage(HelpInfo.PAINT_HELP_PAGE));
            add(addHelp);
            add(removeHelp);
            add(selectHelp);
            add(paintHelp);
            add(hi);
        } catch (IOException e) {
            e.printStackTrace();
        }
        selected = new Rectangle();
    }

    @Override
    protected void paintGuiComponent(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);
        g2d.draw(selected);
    }

    @Override
    protected void update() {
        addHelp.setBounds(PU.getXInBounds(bounds, 100, 0.01), PU.getYInBounds(bounds, 100, 0.1), 100, 100);
        removeHelp.setBounds(PU.getXInBounds(bounds, 100, 0.01), PU.getYInBounds(bounds, 100, 0.35), 100, 100);
        selectHelp.setBounds(PU.getXInBounds(bounds, 100, 0.01), PU.getYInBounds(bounds, 100, 0.6), 100, 100);
        paintHelp.setBounds(PU.getXInBounds(bounds, 100, 0.01), PU.getYInBounds(bounds, 100, 0.85), 100, 100);
        switch (hi.getCurrentWindow()) {
            case HelpInfo.ADD_HELP_PAGE:
                selected.setBounds((int) addHelp.getX() - 1, (int) addHelp.getY() - 1, (int) addHelp.getWidth() + 2, (int) addHelp.getHeight() + 2);
                break;
            case HelpInfo.REMOVE_HELP_PAGE:
                selected.setBounds((int) removeHelp.getX() - 1, (int) removeHelp.getY() - 1, (int) removeHelp.getWidth() + 2, (int) removeHelp.getHeight() + 2);
                break;
            case HelpInfo.SELECT_HELP_PAGE:
                selected.setBounds((int) selectHelp.getX() - 1, (int) selectHelp.getY() - 1, (int) selectHelp.getWidth() + 2, (int) selectHelp.getHeight() + 2);
                break;
            case HelpInfo.PAINT_HELP_PAGE:
                selected.setBounds((int) paintHelp.getX() - 1, (int) paintHelp.getY() - 1, (int) paintHelp.getWidth() + 2, (int) paintHelp.getHeight() + 2);
                break;
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
