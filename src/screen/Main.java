package screen;

import java.awt.*;
import java.io.InputStream;


public class Main {

    public static Robot programRobot;
    public static final int tickRate = 10;



    public static void main(String[] args) throws InterruptedException {

        EditorScreen editor = new EditorScreen("Voxel Editor");
        try {
            programRobot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        //noinspection InfiniteLoopStatement
        while (true) {
            editor.repaint(tickRate);

        }
    }

    public static InputStream getResource(String fileName) {
        if (!ClassLoader.getSystemResourceAsStream(fileName).equals("null")) {
            System.out.println("resource loaded: " +fileName);
        } else {
            System.out.println("resource: " + fileName + " could not be found");
        }
        return ClassLoader.getSystemResourceAsStream(fileName);
    }




}
