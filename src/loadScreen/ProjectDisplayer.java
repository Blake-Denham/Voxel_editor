package loadScreen;

import editorScreen.ComponentManager;
import editorScreen.Main;
import guiTools.Button;
import guiTools.GuiComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Blake on 7/15/2016.
 */
public class ProjectDisplayer extends GuiComponent {
    private final ArrayList<Image> projectImages;
    private final ArrayList<guiTools.Button> projectButtons;
    private final ArrayList<String> projectNames;

    protected ProjectDisplayer() {
        super(0, 0, LoadScreen.getScreenWidth(), LoadScreen.getScreenHeight());
        projectButtons = new ArrayList<>();
        projectNames = new ArrayList<>();
        projectImages = new ArrayList<>();
        File f = new File("assets\\data\\projects");
        int noProjects;
        //noinspection ConstantConditions
        noProjects = f.listFiles().length;
        for (int i = 0; i < noProjects; i++)
            try {
                //noinspection ConstantConditions
                projectImages.add(ImageIO.read(Main.getResource("assets\\data\\projectImages\\" + f.listFiles()[i])));
                //noinspection ConstantConditions
                projectNames.add(f.listFiles()[i].getName());
                final String name = projectNames.get(i);
                projectButtons.add(new Button(0, 0, 0, 0, projectImages.get(i), projectNames.get(i), () -> {
                    ComponentManager.loadProjectIntoCanvas(Main.loadProject("assets\\data\\project\\" + name));
                }));

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    protected void paintGuiComponent(Graphics2D g2d) {

    }

    @Override
    protected void update() {

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
