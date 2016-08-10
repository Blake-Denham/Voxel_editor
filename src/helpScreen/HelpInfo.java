package helpScreen;

import editorScreen.EditorScreen;
import editorScreen.Main;
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
public class HelpInfo extends GuiComponent {
    public static final int ADD_HELP_PAGE = 0;
    public static final int REMOVE_HELP_PAGE = 1;
    public static final int SELECT_HELP_PAGE = 2;
    public static final int PAINT_HELP_PAGE = 3;

    private String addStrings[];
    private String removeStrings[];
    private String selectStrings[];
    private String paintStrings[];
    private String[][] helpStrings;
    private Rectangle backGround;
    private Rectangle[] sections;
    private Color bgColor;
    private Image[][] displays;
    private int currentHelpPage = ADD_HELP_PAGE;

    protected HelpInfo() {
        super(0, 0, HelpScreen.getScreenWidth(), HelpScreen.getScreenHeight());
        backGround = new Rectangle((int) PU.getXInBounds(bounds, HelpScreen.getScreenWidth() - 140, 0.2), 50, HelpScreen.getScreenWidth() - 140, HelpScreen.getScreenHeight() - 100);
        bgColor = new Color(85, 85, 85);
        sections = new Rectangle[5];
        displays = new Image[4][5];
        helpStrings = new String[5][100];
        addStrings = new String[5];
        addStrings[0] = "This main tool allows the user place cubes in the canvas by either left         clicking the grid or another cube. To quickly access this tool press the        hotkey 'Q'.";
        addStrings[1] = "This tool allows the user to add a sphere in the selected area. This button is found when the main tool is 'add', under the 'TOOLS' panel.";
        addStrings[2] = "This tool allows the user to fill the entire of the selected area. This button is found when the main tool is 'add', under the 'TOOLS' panel.";
        addStrings[3] = "This tool allows the user to fill only the edges of the selected area. This      button is found when the main tool is 'add', under the 'TOOLS' panel.";
        addStrings[4] = "This tool allows the user to fill the lowest, empty, horizontal layer. This       button is found when the main tool is 'add', under the 'TOOLS' panel.";

        removeStrings = new String[5];
        removeStrings[0] = "This main tool removes cubes by left clicking on the cube you want to remove. To quickly access this tool press the hotkey 'W'.";
        removeStrings[1] = "This tool removes every cube which is within the sphere contained by the selected area. This button is found when the main tool is 'remove', under the 'TOOLS' panel.";
        removeStrings[2] = "This tool removes every cube in the selected area. This button is found when the main tool is 'remove', under the 'TOOLS' panel.";
        removeStrings[3] = "This tool clears the entire canvas, WARNING this action cannot be reversed. This button is found when the main tool is 'remove', under the 'TOOLS' panel.";
        removeStrings[4] = "This tool allows the user to remove the highest, full, horizontal layer, This button is found when the main tool is 'remove', under the 'TOOLS' panel.";

        selectStrings = new String[3];
        selectStrings[0] = "This main tool allows you to select an area which many other functions will work with. To set a new area left click on a cube to be be the first corner of the selected area, then hold shift and left click on another cube to set the second corner, which is opposite to the first. Press 'E' to quickly access.";
        selectStrings[1] = "This button will set the selected area to the entire canvas, which is the default setting on opening the program";
        selectStrings[2] = "The slider, will indicate how much of the selected area is edited, when functions such as fill and add cuboid are used.";

        paintStrings = new String[5];
        paintStrings[0] = "This main tool allows you to change the colour of an already placed cube, to do this, left click on the cube you wish to change the colour to the primary colour of the colour wheel, or hold shift and left click to change the colour to the secondary colour of the colour wheel";
        paintStrings[1] = "This tool will set the colour of every cube in the selected area to the primary colour of the colour wheel";
        paintStrings[2] = "This tool will inverse the colour of every cube in the selected area, eg: white turns to black";
        paintStrings[3] = "This toggleable tool sets the colour mode of the canvas to contour if the mode is normal and vice versa. Contour mode shows the difference in height of each cube.";
        paintStrings[4] = "This tool allows you to set the primary colour of the colour wheel to the colour of a cube you choose by left clicking.";
        for (int i = 0; i < 5; i++) {
            sections[i] = new Rectangle();
        }
        try {
            displays[0][0] = ImageIO.read(Main.getResource("Images/add256.png"));
            displays[0][1] = ImageIO.read(Main.getResource("Images/addSphere256.png"));
            displays[0][2] = ImageIO.read(Main.getResource("Images/addCuboid256.png"));
            displays[0][3] = ImageIO.read(Main.getResource("Images/addCuboidFrame256.png"));
            displays[0][4] = ImageIO.read(Main.getResource("Images/addLayer256.png"));

            displays[1][0] = ImageIO.read(Main.getResource("Images/remove256.png"));
            displays[1][1] = ImageIO.read(Main.getResource("Images/removeSphere256.png"));
            displays[1][2] = ImageIO.read(Main.getResource("Images/removeCuboid256.png"));
            displays[1][3] = ImageIO.read(Main.getResource("Images/clear256.png"));
            displays[1][4] = ImageIO.read(Main.getResource("Images/removeLayer256.png"));

            displays[2][0] = ImageIO.read(Main.getResource("Images/select256.png"));
            displays[2][1] = ImageIO.read(Main.getResource("Images/selectAll256.png"));

            displays[3][0] = ImageIO.read(Main.getResource("Images/paintBrush256.png"));
            displays[3][1] = ImageIO.read(Main.getResource("Images/fill256.png"));
            displays[3][2] = ImageIO.read(Main.getResource("Images/inverse256.png"));
            displays[3][3] = ImageIO.read(Main.getResource("Images/contour256.png"));
            displays[3][4] = ImageIO.read(Main.getResource("Images/colorPicker256.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintGuiComponent(Graphics2D g2d) {
        PU.castShadow(g2d, backGround, 8, bgColor);
        g2d.setColor(Color.white);
        g2d.setFont(EditorScreen.font.deriveFont(16f));
        PU.setTextRenderingQuality(g2d);
        for (int i = 0; i < 5; i++) {
            g2d.draw(sections[i]);
            int l;
            double width;
            String line = "";
            int lineNo = 0;
            int a = 0;
            switch (currentHelpPage) {
                case ADD_HELP_PAGE:
                    if (displays[ADD_HELP_PAGE][i] != null) {
                        g2d.drawImage(displays[ADD_HELP_PAGE][i],
                                (int) PU.getXInBounds(sections[i], sections[i].getHeight() - 10, 0.01),
                                (int) PU.getYInBounds(sections[i], sections[i].getHeight() - 10, 0.5),
                                (int) sections[i].getHeight() - 10,
                                (int) sections[i].getHeight() - 10, null);
                    }
                    l = addStrings[i].length();
                    for (int j = 0; j < l; j++) {
                        width = g2d.getFontMetrics().stringWidth(addStrings[i].substring(a, j));
                        if (width < sections[i].getWidth() - 115 && j != l - 1) {
                            line += "" + addStrings[i].charAt(j);
                        } else {
                            helpStrings[i][lineNo] = line;
                            lineNo++;
                            if (j != l - 1) {
                                line = "" + addStrings[i].charAt(j);
                            } else {
                                line = "";
                            }
                            a = j;
                        }
                    }
                    for (int j = 0; j < helpStrings[i].length; j++) {
                        if (helpStrings[i][j] != null) {
                            g2d.drawString(helpStrings[i][j], (int) PU.getXInBounds(sections[i], sections[i].getWidth() - 115, 0.9), (int) (sections[i].getY() + 16 + j * sections[i].getHeight() / 6.0));
                        }
                    }
                    break;
                case REMOVE_HELP_PAGE:
                    if (displays[REMOVE_HELP_PAGE][i] != null) {
                        g2d.drawImage(displays[REMOVE_HELP_PAGE][i],
                                (int) PU.getXInBounds(sections[i], sections[i].getHeight() - 10, 0.01),
                                (int) PU.getYInBounds(sections[i], sections[i].getHeight() - 10, 0.5),
                                (int) sections[i].getHeight() - 10,
                                (int) sections[i].getHeight() - 10, null);
                    }
                    l = removeStrings[i].length();
                    for (int j = 0; j < l; j++) {
                        width = g2d.getFontMetrics().stringWidth(removeStrings[i].substring(a, j));
                        if (width < sections[i].getWidth() - 115 && j != l - 1) {
                            line += "" + removeStrings[i].charAt(j);
                        } else {
                            helpStrings[i][lineNo] = line;
                            lineNo++;
                            if (j != l - 1) {
                                line = "" + removeStrings[i].charAt(j);
                            } else {
                                line = "";
                            }
                            a = j;
                        }
                    }
                    for (int j = 0; j < helpStrings[i].length; j++) {
                        if (helpStrings[i][j] != null) {
                            g2d.drawString(helpStrings[i][j], (int) PU.getXInBounds(sections[i], sections[i].getWidth() - 115, 0.9), (int) (sections[i].getY() + 16 + j * sections[i].getHeight() / 6.0));
                        }
                    }
                    break;
                case SELECT_HELP_PAGE:
                    if (displays[SELECT_HELP_PAGE][i] != null) {
                        g2d.drawImage(displays[SELECT_HELP_PAGE][i],
                                (int) PU.getXInBounds(sections[i], sections[i].getHeight() - 10, 0.01),
                                (int) PU.getYInBounds(sections[i], sections[i].getHeight() - 10, 0.5),
                                (int) sections[i].getHeight() - 10,
                                (int) sections[i].getHeight() - 10, null);
                    }
                    if (i < 3) {
                        l = selectStrings[i].length();
                        for (int j = 0; j < l; j++) {
                            width = g2d.getFontMetrics().stringWidth(selectStrings[i].substring(a, j));
                            if (width < sections[i].getWidth() - 115 && j != l - 1) {
                                line += "" + selectStrings[i].charAt(j);
                            } else {
                                helpStrings[i][lineNo] = line;
                                lineNo++;
                                if (j != l - 1) {
                                    line = "" + selectStrings[i].charAt(j);
                                } else {
                                    line = "";
                                }
                                a = j;
                            }
                        }
                        for (int j = 0; j < helpStrings[i].length; j++) {
                            if (helpStrings[i][j] != null) {
                                g2d.drawString(helpStrings[i][j], (int) PU.getXInBounds(sections[i], sections[i].getWidth() - 115, 0.9), (int) (sections[i].getY() + 16 + j * sections[i].getHeight() / 6.0));
                            }
                        }
                    }
                    break;
                case PAINT_HELP_PAGE:
                    if (displays[PAINT_HELP_PAGE][i] != null) {
                        g2d.drawImage(displays[PAINT_HELP_PAGE][i],
                                (int) PU.getXInBounds(sections[i], sections[i].getHeight() - 10, 0.01),
                                (int) PU.getYInBounds(sections[i], sections[i].getHeight() - 10, 0.5),
                                (int) sections[i].getHeight() - 10,
                                (int) sections[i].getHeight() - 10, null);
                    }
                    l = paintStrings[i].length();
                    for (int j = 0; j < l; j++) {
                        width = g2d.getFontMetrics().stringWidth(paintStrings[i].substring(a, j));
                        if (width < sections[i].getWidth() - 115 && j != l - 1) {
                            line += "" + paintStrings[i].charAt(j);
                        } else {
                            helpStrings[i][lineNo] = line;
                            lineNo++;
                            if (j != l - 1) {
                                line = "" + paintStrings[i].charAt(j);
                            } else {
                                line = "";
                            }
                            a = j;
                        }
                    }
                    for (int j = 0; j < helpStrings[i].length; j++) {
                        if (helpStrings[i][j] != null) {
                            g2d.drawString(helpStrings[i][j], (int) PU.getXInBounds(sections[i], sections[i].getWidth() - 115, 0.9), (int) (sections[i].getY() + 16 + j * sections[i].getHeight() / 6.0));
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void update() {
        backGround.setBounds((int) PU.getXInBounds(bounds, HelpScreen.getScreenWidth() - 140, 0.8), 50, HelpScreen.getScreenWidth() - 140, HelpScreen.getScreenHeight() - 100);
        for (int i = 0; i < 5; i++) {
            sections[i].setBounds((int) backGround.getX(), (int) (backGround.getY() + backGround.getHeight() / 5.0 * i), (int) backGround.getWidth(), (int) (backGround.getHeight() / 5.0));
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

    public void setHelpPage(int page) {
        currentHelpPage = page;
    }

    public int getCurrentWindow() {
        return currentHelpPage;
    }
}
