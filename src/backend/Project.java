package backend;

import java.io.Serializable;

//this object will be made when a user saves the project.
//and is used to store saved data in a .vem (Voxel editor model) file.
public class Project implements Serializable {
    //a 3D int array where each dimension represents a coordinate in space ([z][x][y]) and the value of each element is a colour value.
    private int[][][] cubeData;

    //the dimensions of the space, side represents width and length, and height represents height
    private int side, height;

    //used for serialisation
    private static final long serialVersionUID = -5638513059997628790L;

    //will take the buffer from the canvas class and store it this serializable object
    public Project(int[][][] cubes, int side, int height) {
        cubeData = new int[height - 1][side - 1][side - 1];
        cubeData = cubes;
        this.side = side;
        this.height = height;
    }

    //returns the cubeData
    public int[][][] getCubeData() {
        return cubeData;
    }

    //returns the width/length of the saved canvas
    public int getSide() {
        return side;
    }

    //returns the height of the saved project
    public int getCanvasHeight() {
        return height;
    }
}
