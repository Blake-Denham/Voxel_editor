package loadScreen;

import guiTools.GuiComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

/**
 * Created by Blake on 7/15/2016.
 */
public class LoadScreenComponentManager extends JComponent {
    private final ArrayList<GuiComponent> guiComponents;

    public LoadScreenComponentManager() {
        guiComponents = new ArrayList<>();
    }

    private void addComponent(GuiComponent component) {
        guiComponents.add(component);
    }

    private void update() {
        guiComponents.forEach(GuiComponent::updateAll);

    }

    public void hover(MouseEvent e) {
        guiComponents.stream().filter(guiComponent -> !guiComponent.getDisabled()).forEach(guiComponent -> guiComponent.mouseMove(e));
    }

    public void drag(MouseEvent e) {
        guiComponents.stream().filter(guiComponent -> !guiComponent.getDisabled()).forEach(guiComponent -> guiComponent.mouseDrag(e));
    }

    public void keyPress(KeyEvent e) {
        guiComponents.stream().filter(guiComponent -> !guiComponent.getDisabled()).forEach(guiComponent -> guiComponent.keyPressedSC(e));
    }

    public void mouseWheel(MouseWheelEvent e) {
        guiComponents.stream().filter(guiComponent -> !guiComponent.getDisabled()).forEach(guiComponent -> guiComponent.mouseWheel(e));
    }

    public void mousePress(MouseEvent e) {
        guiComponents.stream().filter(guiComponent -> !guiComponent.getDisabled()).forEach(guiComponent -> guiComponent.mousePressSC(e));
    }

    public void mouseRelease(MouseEvent e) {
        guiComponents.stream().filter(guiComponent -> !guiComponent.getDisabled()).forEach(guiComponent -> guiComponent.mouseReleaseSC(e));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        update();
        Graphics2D g2d = (Graphics2D) g;
        for (GuiComponent guiComponent : guiComponents) {
            guiComponent.paintAll(g2d);
        }
    }
}
