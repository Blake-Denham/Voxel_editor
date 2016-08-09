package backend;

import java.io.Serializable;

public class Project implements Serializable {
    private int[][][] cubeData;
    private int side, height;
    private static final long serialVersionUID = -5638513059997628790L;

    public Project(int[][][] cubes, int side, int height) {
        cubeData = new int[height - 1][side - 1][side - 1];
        cubeData = cubes;
        this.side = side;
        this.height = height;
    }

    public int[][][] getCubeData() {
        return cubeData;
    }

    public int getSide() {
        return side;
    }

    public int getCanvasHeight() {
        return height;
    }
}
