package editorScreen;

import backend.Project;
import guiTools.Button;
import guiTools.GuiComponent;
import util.MU;
import util.PU;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;

public class CanvasDataManager extends GuiComponent {
    private guiTools.Button save, load, newProject;

    CanvasDataManager(double x, double width, double height, Color bgColor) {
        super(x, (double) 5, width, height, bgColor, 14, true);
        setToolBarTitle("SAVE, LOAD OR NEW");
        try {
            Image s = ImageIO.read(Main.getResource("Images/save256.png"));
            Image l = ImageIO.read(Main.getResource("Images/load256.png"));
            Image np = ImageIO.read(Main.getResource("Images/newProject256.png"));

            save = new Button(0, 0, 0, 0, s, "save", () -> {
                String name = "";
                name = JOptionPane.showInputDialog("Enter project name");
                System.out.println(name);
                int[][][] temp = new int[ComponentManager.getCanvas().getCanvasHeight() - 1][ComponentManager.getCanvas().getSide() - 1][ComponentManager.getCanvas().getSide() - 1];
                for (int xi = 0; xi < ComponentManager.getCanvas().getSide() - 1; xi++) {
                    for (int yi = 0; yi < ComponentManager.getCanvas().getSide() - 1; yi++) {
                        for (int zi = 0; zi < ComponentManager.getCanvas().getCanvasHeight() - 1; zi++) {
                            if (ComponentManager.getCanvas().checkForCube(xi, yi, zi)) {
                                temp[zi][xi][yi] = ComponentManager.getCanvas().getCubes()[zi][xi][yi].getColorHex();
                            } else {
                                temp[zi][xi][yi] = -1;
                            }
                            System.out.print((temp[zi][xi][yi]) + "\t");
                        }
                        System.out.println();
                    }
                    System.out.println("-----------------");
                }
                if (!name.equals(("")) && !name.equals("null") && !name.contains("/")) {
                    ComponentManager.getCanvas().setDisplayPicture();
                    Main.getEditor().getModelImage(name);
                    Main.serialize(new Project(temp, ComponentManager.getCanvas().getSide(), ComponentManager.getCanvas().getCanvasHeight()), name);
                    Main.serialize(ComponentManager.getCanvas().getCollisionMap(), name);
                    ComponentManager.getCanvas().getCollisionMap().display();
                    ComponentManager.turnOnSettings();
                } else {
                    JOptionPane.showConfirmDialog(null, "invalid name. please do not use '/' or leave the name blank", "error", JOptionPane.DEFAULT_OPTION);
                }
            });
            add(save);
            load = new Button(0, 0, 0, 0, l, "load", Main::openLoadScreen);
            add(load);
            newProject = new Button(0, 0, 0, 0, np, "new Project", () -> ComponentManager.newCanvas(EditorScreen.getComponentManager(), Integer.parseInt(JOptionPane.showInputDialog("enter the length dimension")) + 1, Integer.parseInt(JOptionPane.showInputDialog("enter the height dimension")) + 1));
            add(newProject);

        } catch (IOException e) {
            e.printStackTrace();
        }
        setID("canvas data manager");
    }

    @Override
    protected void paintGuiComponent(Graphics2D g2d) {
    }

    @Override
    protected void update() {
        setBounds((EditorScreen.s_maxWidth * (MU.getPercent(5 + 25, 1920)) - 16), 10 + 26, EditorScreen.s_maxWidth * MU.getPercent(310, 1920), ComponentManager.getCanvasManipulator().getHeight());
        save.setBounds(PU.getXInBounds(bounds, width * 0.25, 0.1), PU.getYInBounds(bounds, width * 0.25, 0.15), width * 0.25, width * 0.25);
        load.setBounds(PU.getXInBounds(bounds, width * 0.25, 0.5), PU.getYInBounds(bounds, width * 0.25, 0.15), width * 0.25, width * 0.25);
        newProject.setBounds(PU.getXInBounds(bounds, width * 0.25, 0.9), PU.getYInBounds(bounds, width * 0.25, 0.15), width * 0.25, width * 0.25);

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
