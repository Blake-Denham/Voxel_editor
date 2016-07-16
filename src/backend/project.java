package backend;

import java.io.Serializable;

/**
 * Created by Blake on 7/15/2016.
 */
public class Project implements Serializable {
    private Cube[][][] cubeData;
    private int side, height;

    public Project(Cube[][][] cubes, int side, int height) {
        cubeData = cubes;
        this.side = side;
        this.height = height;
    }

    public Cube[][][] getCubeData() {
        return cubeData;
    }

    public int getSide() {
        return side;
    }

    public int getCanvasHeight() {
        return height;
    }
}
