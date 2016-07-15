package screen;

import backend.Project;

import java.awt.*;
import java.io.*;


public class Main {

    public static Robot programRobot;
    private static final int tickRate = 10;


    public static void main(String[] args) throws InterruptedException, IOException {

        EditorScreen editor = new EditorScreen();
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
        //noinspection EqualsBetweenInconvertibleTypes
        if (!ClassLoader.getSystemResourceAsStream(fileName).equals("null")) {
            System.out.println("resource loaded: " +fileName);
        } else {
            System.out.println("resource: " + fileName + " could not be found");
        }
        return ClassLoader.getSystemResourceAsStream(fileName);
    }

    public static void serialize(Project p, String newFileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(newFileName + ".vem")))) {
            out.writeObject(p);
        } catch (IOException e) {
            System.err.println("serialization has failed");
            System.out.println(e.getMessage());
        }
    }

    public static Project loadProject(String fileName) {
        Project canvas = null;

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("data\\projects\\" + fileName + ".vem"));
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
