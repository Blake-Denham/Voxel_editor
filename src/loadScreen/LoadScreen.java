package loadScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Blake on 7/15/2016.
 */
public class LoadScreen extends JPanel {
    private JFrame window;
    private static int width = 830, height = 600;
    private LoadScreenComponentManager lscm;

    public LoadScreen() {
        super();
        setBackground(new Color(35, 35, 35));
        lscm = new LoadScreenComponentManager();
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                lscm.drag(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                lscm.hover(e);

            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                lscm.mousePress(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lscm.mouseRelease(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        addMouseWheelListener(lscm::mouseWheel);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                lscm.keyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        setFocusable(true);
        add(lscm);
        window = new JFrame("Load project");
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
        lscm.paintComponent(g2d);
    }

    public void close() {
        window.dispose();
    }


}
