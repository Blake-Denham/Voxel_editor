package util;

import java.io.Serializable;

/**
 * Created by Blake on 7/25/2016.
 */
public class Vector3D implements Serializable {
    private double x, y, z;
    public static final long serialVersionUID = -6308054045429273617L;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void set(int hx, int hy, int hz) {
        this.x = hx;
        this.y = hy;
        this.z = hz;
    }


    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
