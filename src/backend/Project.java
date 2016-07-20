package backend;

import java.io.Serializable;

public class Project implements Serializable {
    private int[][][] cubeData;
    private int side, height;

    public Project(Cube[][][] cubes, int side, int height) {
        cubeData = new int[height - 1][side - 1][side - 1];
        for (int x = 0; x < side - 1; x++) {
            for (int y = 0; y < side - 1; y++) {
                for (int z = 0; z < height - 1; z++) {
                    if (cubes[z][x][y] != null) {
                        cubeData[z][x][y] = cubes[z][x][y].getColorHex();
                    } else {
                        cubeData[z][x][y] = -1;
                    }
                }
            }
        }
        this.side = side - 1;
        this.height = height - 1;
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
