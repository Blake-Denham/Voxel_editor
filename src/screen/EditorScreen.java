package screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class EditorScreen extends JPanel {
    public static int s_maxWidth, s_maxHeight;
    public static Font font;
    private final JFrame jf;
    private final ComponentManager cm;

    public EditorScreen(String title) {
        super();
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, Main.getResource("Montserrat-Regular.ttf"));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        jf = new JFrame(title);
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        s_maxHeight = ss.height;
        s_maxWidth = ss.width;
        cm = new ComponentManager();
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocation(0, 0);
        jf.setSize((int)(s_maxWidth*0.9), (int)(s_maxHeight*0.9));

        //jf.setResizable(false);
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                cm.drag(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                cm.hover(e);
            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cm.click(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                cm.mousePress(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                cm.mouseRelease(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        addMouseWheelListener(cm::mouseWheel);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                cm.keyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                cm.keyRelease(e);
            }
        });
        setFocusable(true);
        setBackground(new Color(35, 35, 35));
        GridLayout lay = new GridLayout(0, 1);
        setSize(s_maxWidth, s_maxHeight);
        setLayout(lay);
        setDoubleBuffered(true);
        add(cm);
        jf.add(this);
        jf.setVisible(true);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        s_maxWidth = jf.getWidth();
        s_maxHeight = jf.getHeight();
        displayScreenInfo();

    }



    @Override
    public void paint(Graphics g) {
        super.paint(g);
        s_maxWidth = jf.getWidth();
        s_maxHeight = jf.getHeight();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    public void displayScreenInfo(){
        System.out.println("---------------------------");
        System.out.println("Screen info:");
        System.out.println("\tx: " + jf.getX());
        System.out.println("\ty: " + jf.getY());
        System.out.println("\twidth: " + s_maxWidth);
        System.out.println("\theight: " + s_maxHeight);
        System.out.println("\tcolor model: " +jf.getColorModel().toString());
        System.out.println("---------------------------");
    }
}
