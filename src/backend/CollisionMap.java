package backend;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Blake on 8/23/2016.
 * A collection of bounding boxes of project, which will be serialized for future use
 */
public class CollisionMap implements Serializable {
    //a collection of all bounding boxes in a single project, which can then be serialized as vmcm (Voxel Model Collision Map) file
    private ArrayList<BoundingBox> collisionBoxes;

    //used to transfer serialized files to other projects or programs
    public static final long serialVersionUID = -3203458971419879272L;

    public CollisionMap() {
        collisionBoxes = new ArrayList<>();
    }

    //adds bounds to arrayList collisionBoxes
    public void addCollisionBox(BoundingBox b) {
        collisionBoxes.add(b);
    }

    //returns the ArrayList which holds all the bounding boxes
    public ArrayList<BoundingBox> getCollisionBoxes() {
        return collisionBoxes;
    }

    //draws all bounding boxes on a grid
    public void paintMap(Grid g, Graphics2D g2d) {
        for (int i = 0; i < collisionBoxes.size(); i++)
            collisionBoxes.get(i).paintBounds(g, g2d);
    }

    //prints all the bounding boxes information
    public void display() {
        for (int i = 0; i < collisionBoxes.size(); i++) {
            System.out.println(collisionBoxes.get(i).toString());
        }
    }
}
