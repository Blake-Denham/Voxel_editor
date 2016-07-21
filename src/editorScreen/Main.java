package editorScreen;

import backend.Project;
import loadScreen.LoadScreen;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Main {

    public static Robot programRobot;
    private static final int tickRate = 16;
    private static LoadScreen ls;
    private static EditorScreen editor;
    public static String appPath;

    public static void openLoadScreen() {
        ls = new LoadScreen();
    }

    public static void closeLoadScrene() {
        ls = null;
    }

    public static EditorScreen getEditor() {
        return editor;
    }

    public static LoadScreen getLoadScreen() {
        return ls;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        appPath = System.getProperty("user.dir");
        editor = new EditorScreen();
        try {
            programRobot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        //noinspection InfiniteLoopStatement
        while (true) {
            editor.repaint(tickRate);
            if (ls != null) {
                ls.repaint(tickRate);
            }
        }
    }

    public static InputStream getResource(String fileName) {
        //noinspection EqualsBetweenInconvertibleTypes
        if (!ClassLoader.getSystemResourceAsStream(fileName).equals("null")) {
            System.out.println("resource loaded: " + fileName);
        } else {
            System.out.println("resource: " + fileName + " could not be found");
        }
        return ClassLoader.getSystemResourceAsStream(fileName);
    }

    public static void serialize(Project p, String newFileName) {
        File f = new File(appPath + "\\data\\project\\" + newFileName + ".vem");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f))) {
            out.writeObject(p);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.err.println("serialization has failed");
            System.out.println(e.getMessage());
        }
    }

    public static Project loadProject(String fileName) {
        Project canvas = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(appPath + "\\data\\project\\" + fileName + ".vem"));
            canvas = (Project) (objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (canvas == null) {
            System.out.println("Aw shit buddy");
        }

        return canvas;
    }

}
