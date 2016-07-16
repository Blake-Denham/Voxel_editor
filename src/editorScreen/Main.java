package editorScreen;

import backend.Project;
import loadScreen.LoadScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


public class Main {

    public static Robot programRobot;
    private static final int tickRate = 10;
    private static LoadScreen ls;

    public static void openLoadScreen() {
        ls = new LoadScreen();
    }

    public static void closeLoadScrene() {
        ls = null;
    }

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
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("assets\\data\\project\\" + newFileName + ".vem")))) {
            out.writeObject(p);
        } catch (IOException e) {
            System.err.println("serialization has failed");
            System.out.println(e.getMessage());
        }
    }

    public static Project loadProject(String fileName) {
        Project canvas = null;

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("data\\project\\" + fileName + ".vem"));
            canvas = (Project) (objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (canvas == null) {
            System.out.println("Aw shit buddy");
        }

        return canvas;
    }

    public static void cropImage(Image src, Rectangle crop, String pathName) {
        int x = (int) crop.getX(), y = (int) crop.getY(), w = (int) crop.getWidth(), h = (int) crop.getHeight();

        BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        dst.getGraphics().drawImage(src, 0, 0, w, h, x, y, x + w, y + h, null);
        try {
            ImageIO.write(dst, "png", new File(pathName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage screenShot() {
        BufferedImage src;
        src = Main.programRobot.createScreenCapture(new Rectangle(EditorScreen.locX, EditorScreen.locY, EditorScreen.s_maxWidth, EditorScreen.s_maxHeight));
        try {
            ImageIO.write(src, "png", new File("temp" + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return src;
    }


}
