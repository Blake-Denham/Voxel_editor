package helpScreen;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Blake on 8/10/2016.
 */
public class HelpScreen extends JPanel {
    private JFrame window;
    private HelpScreenComponentManager hscm;
    private static int width = 830, height = 600;

    public HelpScreen() {
        super();
        hscm = new HelpScreenComponentManager();
        setBackground(new Color(35, 35, 35));
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                hscm.drag(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                hscm.hover(e);

            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                hscm.mousePress(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                hscm.mouseRelease(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        addMouseWheelListener(hscm::mouseWheel);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                hscm.keyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        setFocusable(true);
        add(hscm);
        window = new JFrame("Help Screen");
        window.add(this);
        window.setSize(width, height);
        window.setResizable(false);
        window.setLocation(75, 75);
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setVisible(true);


    }

    public static int getScreenWidth() {
        return width;
    }

    public static int getScreenHeight() {
        return height;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        hscm.paintComponent(g2d);
    }

    public boolean isOpen() {
        return window.isActive();
    }

    public void close() {
        window.dispose();
    }

}
