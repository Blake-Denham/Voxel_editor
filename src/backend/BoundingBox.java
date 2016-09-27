package backend;

import util.Vector3D;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by Blake on 8/22/2016.
 * This class is used for making collision boxes, generally only used in separate projects such games
 */
public class BoundingBox implements Serializable {
    // the maximum and minimum of a cuboid
    private Vector3D min, max;

    //used for transferring serialized files to different projects
    public static final long serialVersionUID = 6902067095214394816L;

    //constructor accepts the min and max vectors for the bounding box
    public BoundingBox(Vector3D min, Vector3D max) {
        this.max = max;
        this.min = min;
    }

    //returns the max vector
    public Vector3D getMax() {
        return max;
    }

    //returns the min vector
    public Vector3D getMin() {
        return min;
    }

    //outlines the bounding box, in cyan colour
    public void paintBounds(Grid g, Graphics2D g2d) {
        g2d.setColor(new Color(0, 255, 255));
        //zxy-zxy
        //000-100
        g2d.drawLine(
                (int) g.getPts()[(int) min.getZ()][(int) min.getX()][(int) min.getY()].getVec().getX(),
                (int) g.getPts()[(int) min.getZ()][(int) min.getX()][(int) min.getY()].getVec().getY(),
                (int) g.getPts()[(int) max.getZ()][(int) min.getX()][(int) min.getY()].getVec().getX(),
                (int) g.getPts()[(int) max.getZ()][(int) min.getX()][(int) min.getY()].getVec().getY());
        //000-010
        g2d.drawLine(
                (int) g.getPts()[(int) min.getZ()][(int) min.getX()][(int) min.getY()].getVec().getX(),
                (int) g.getPts()[(int) min.getZ()][(int) min.getX()][(int) min.getY()].getVec().getY(),
                (int) g.getPts()[(int) min.getZ()][(int) max.getX()][(int) min.getY()].getVec().getX(),
                (int) g.getPts()[(int) min.getZ()][(int) max.getX()][(int) min.getY()].getVec().getY());
        //000-001
        g2d.drawLine(
                (int) g.getPts()[(int) min.getZ()][(int) min.getX()][(int) min.getY()].getVec().getX(),
                (int) g.getPts()[(int) min.getZ()][(int) min.getX()][(int) min.getY()].getVec().getY(),
                (int) g.getPts()[(int) min.getZ()][(int) min.getX()][(int) max.getY()].getVec().getX(),
                (int) g.getPts()[(int) min.getZ()][(int) min.getX()][(int) max.getY()].getVec().getY());
        //100-110
        g2d.drawLine(
                (int) g.getPts()[(int) max.getZ()][(int) min.getX()][(int) min.getY()].getVec().getX(),
                (int) g.getPts()[(int) max.getZ()][(int) min.getX()][(int) min.getY()].getVec().getY(),
                (int) g.getPts()[(int) max.getZ()][(int) max.getX()][(int) min.getY()].getVec().getX(),
                (int) g.getPts()[(int) max.getZ()][(int) max.getX()][(int) min.getY()].getVec().getY());
        //100-101
        g2d.drawLine(
                (int) g.getPts()[(int) max.getZ()][(int) min.getX()][(int) min.getY()].getVec().getX(),
                (int) g.getPts()[(int) max.getZ()][(int) min.getX()][(int) min.getY()].getVec().getY(),
                (int) g.getPts()[(int) max.getZ()][(int) min.getX()][(int) max.getY()].getVec().getX(),
                (int) g.getPts()[(int) max.getZ()][(int) min.getX()][(int) max.getY()].getVec().getY());
        //010-110
        g2d.drawLine(
                (int) g.getPts()[(int) min.getZ()][(int) max.getX()][(int) min.getY()].getVec().getX(),
                (int) g.getPts()[(int) min.getZ()][(int) max.getX()][(int) min.getY()].getVec().getY(),
                (int) g.getPts()[(int) max.getZ()][(int) max.getX()][(int) min.getY()].getVec().getX(),
                (int) g.getPts()[(int) max.getZ()][(int) max.getX()][(int) min.getY()].getVec().getY());
        //010-011
        g2d.drawLine(
                (int) g.getPts()[(int) min.getZ()][(int) max.getX()][(int) min.getY()].getVec().getX(),
                (int) g.getPts()[(int) min.getZ()][(int) max.getX()][(int) min.getY()].getVec().getY(),
                (int) g.getPts()[(int) min.getZ()][(int) max.getX()][(int) max.getY()].getVec().getX(),
                (int) g.getPts()[(int) min.getZ()][(int) max.getX()][(int) max.getY()].getVec().getY());
        //001-101
        g2d.drawLine(
                (int) g.getPts()[(int) min.getZ()][(int) min.getX()][(int) max.getY()].getVec().getX(),
                (int) g.getPts()[(int) min.getZ()][(int) min.getX()][(int) max.getY()].getVec().getY(),
                (int) g.getPts()[(int) max.getZ()][(int) min.getX()][(int) max.getY()].getVec().getX(),
                (int) g.getPts()[(int) max.getZ()][(int) min.getX()][(int) max.getY()].getVec().getY());
        //001-011
        g2d.drawLine(
                (int) g.getPts()[(int) min.getZ()][(int) min.getX()][(int) max.getY()].getVec().getX(),
                (int) g.getPts()[(int) min.getZ()][(int) min.getX()][(int) max.getY()].getVec().getY(),
                (int) g.getPts()[(int) min.getZ()][(int) max.getX()][(int) max.getY()].getVec().getX(),
                (int) g.getPts()[(int) min.getZ()][(int) max.getX()][(int) max.getY()].getVec().getY());
        //101-111
        g2d.drawLine(
                (int) g.getPts()[(int) max.getZ()][(int) min.getX()][(int) max.getY()].getVec().getX(),
                (int) g.getPts()[(int) max.getZ()][(int) min.getX()][(int) max.getY()].getVec().getY(),
                (int) g.getPts()[(int) max.getZ()][(int) max.getX()][(int) max.getY()].getVec().getX(),
                (int) g.getPts()[(int) max.getZ()][(int) max.getX()][(int) max.getY()].getVec().getY());
        //110-111
        g2d.drawLine(
                (int) g.getPts()[(int) max.getZ()][(int) max.getX()][(int) min.getY()].getVec().getX(),
                (int) g.getPts()[(int) max.getZ()][(int) max.getX()][(int) min.getY()].getVec().getY(),
                (int) g.getPts()[(int) max.getZ()][(int) max.getX()][(int) max.getY()].getVec().getX(),
                (int) g.getPts()[(int) max.getZ()][(int) max.getX()][(int) max.getY()].getVec().getY());
        //011-111
        g2d.drawLine(
                (int) g.getPts()[(int) min.getZ()][(int) max.getX()][(int) max.getY()].getVec().getX(),
                (int) g.getPts()[(int) min.getZ()][(int) max.getX()][(int) max.getY()].getVec().getY(),
                (int) g.getPts()[(int) max.getZ()][(int) max.getX()][(int) max.getY()].getVec().getX(),
                (int) g.getPts()[(int) max.getZ()][(int) max.getX()][(int) max.getY()].getVec().getY());
    }

    @Override
    public String toString() {
        return min.toString() + "\t" + max.toString();
    }
}
