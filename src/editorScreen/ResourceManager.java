package editorScreen;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Blake on 8/7/2016.
 */
public class ResourceManager {
    private static HashMap<String, InputStream> resources;

    static {
        resources = new HashMap<>();
        resources.put("add", Main.getResource("Images\\add256.png"));
        resources.put("addCuboid", Main.getResource("Images\\addCuboid256.png"));
        resources.put("addCuboidFrame", Main.getResource("Images\\addCuboidFrame256.png"));
        resources.put("addLayer", Main.getResource("Images\\addLayer256.png"));
        resources.put("addSphere", Main.getResource("Images\\addSphere256.png"));
        resources.put("clear", Main.getResource("Images\\clear256.png"));
        resources.put("commonColors", Main.getResource("Images\\commonColors256.png"));
        resources.put("contour", Main.getResource("Images\\contour256.png"));
        resources.put("fill", Main.getResource("Images\\fill256.png"));
        resources.put("hideAxis", Main.getResource("Images\\hideAxis256.png"));
        resources.put("hideCoords", Main.getResource("Images\\hideCoords256.png"));
        resources.put("hideGrid", Main.getResource("Images\\hideGrid256.png"));
        resources.put("hideSelectedArea", Main.getResource("Images\\hideSelectedArea256.png"));
        resources.put("imageNotFound", Main.getResource("Images\\imageNotFound.png"));
        resources.put("inverse256", Main.getResource("Images\\inverse256.png"));
        resources.put("isometric", Main.getResource("Images\\isometric.png"));
        resources.put("load256", Main.getResource("Images\\load256.png"));
        resources.put("maximize", Main.getResource("Images\\maximize.png"));
        resources.put("minimize", Main.getResource("Images\\minimize.png"));
        resources.put("newProject", Main.getResource("Images\\newProject.png"));
        resources.put("paintBrush", Main.getResource("Images\\paintBrush256.png"));
        resources.put("remove", Main.getResource("Images\\remove256.png"));
        resources.put("removeCuboid", Main.getResource("Images\\removeCuboid256.png"));
        resources.put("removeLayer", Main.getResource("Images\\removeLayer256.png"));
        resources.put("removeSphere", Main.getResource("Images\\removeSphere256.png"));
        resources.put("rotate", Main.getResource("Images\\rotate.png"));
        resources.put("save", Main.getResource("Images\\save256.png"));
        resources.put("select", Main.getResource("Images\\select256.png"));
        resources.put("selectAll", Main.getResource("Images\\selectAll256.png"));
        resources.put("showAxis", Main.getResource("Images\\showAxis256.png"));
        resources.put("showCoords", Main.getResource("Images\\showCoords256.png"));
        resources.put("showGrid", Main.getResource("Images\\showGrid256.png"));
        resources.put("showSelectedArea", Main.getResource("Images\\showSelectedArea256.png"));
        resources.put("font", Main.getResource("Montserrat-Regular.ttf"));
    }

    public static InputStream getResource(String tag) {
        return resources.get(tag);
    }
}
