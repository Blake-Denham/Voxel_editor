package loadScreen;

import editorScreen.ComponentManager;
import editorScreen.EditorScreen;
import editorScreen.Main;
import guiTools.Button;
import guiTools.GuiComponent;
import util.PU;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectDisplayer extends GuiComponent {
    private final ArrayList<guiTools.Button> projectButtons;

    ProjectDisplayer() {
        super(0, 0, LoadScreen.getScreenWidth(), LoadScreen.getScreenHeight());
        projectButtons = new ArrayList<>();
        ArrayList<String> projectNames = new ArrayList<>();
        File f = new File(Main.appPath + "\\data\\project\\");
        //noinspection ResultOfMethodCallIgnored
        f.mkdirs();
        int noProjects;
        noProjects = f.list().length;
        for (int i = 0; i < noProjects; i++)
            try {
                projectNames.add(f.listFiles()[i].getName());
                Image image;
                String temp = projectNames.get(i).split(".vem")[0];
                if (new File(Main.appPath + "\\data\\projectImages\\" + temp + ".png").exists()) {
                    image = ImageIO.read(new File(Main.appPath + "\\data\\projectImages\\" + temp + ".png"));
                } else {
                    image = ImageIO.read(Main.getResource("Images\\imageNotFound.png"));
                }
                final String name = projectNames.get(i);
                projectButtons.add(new Button(128 * (i) + 2 + 5, 5, 128, 128, image, projectNames.get(i), () -> {
                    ComponentManager.loadProjectIntoCanvas(EditorScreen.getComponentManager(), Main.loadProject(name));
                    Main.closeLoadScreen();
                }));
                add(projectButtons.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    protected void paintGuiComponent(Graphics2D g2d) {
        PU.castShadow(g2d, new Rectangle(12, 57, LoadScreen.getScreenWidth() - 30, LoadScreen.getScreenHeight() - 114), 10, new Color(55, 55, 55));
        g2d.setFont(EditorScreen.font.deriveFont(16f));
        PU.castShadow(g2d, new Rectangle(10, 57 - 16 - 2 - 16, g2d.getFontMetrics().stringWidth("LOAD SCREEN, CLICK A PROJECT TO LOAD IT") + 4, 20), 6, new Color(55, 55, 55));
        g2d.setColor(Color.LIGHT_GRAY);
        PU.setTextRenderingQuality(g2d);
        g2d.drawString("LOAD SCREEN, CLICK A PROJECT TO LOAD IT", 12, 40);
    }
    @Override
    protected void update() {
        for (int i = 0; i < projectButtons.size(); i++) {
            int x_ = 15;
            x_ += 133 * i;
            int y_ = 60;
            if (x_ + 133 > LoadScreen.getScreenWidth()) {
                x_ = 15 + 133 * (i % 6);
                y_ += 133;
            }
            projectButtons.get(i).setBounds(x_, y_, 128, 128);
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
