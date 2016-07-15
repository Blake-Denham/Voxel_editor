package backend;

import java.io.Serializable;

/**
 * Created by Blake on 7/15/2016.
 */
public class Project implements Serializable {
    private Cube[][][] cubeData;

    public Project(Cube[][][] cubes) {
        cubeData = cubes;
    }

}
