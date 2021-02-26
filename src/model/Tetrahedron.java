package model;

import transform.Mat4RotX;
import transform.Mat4RotY;
import transform.Point3D;

import java.util.Arrays;
import java.util.List;

public class Tetrahedron extends Solid {

    public Tetrahedron() {


        Integer[] indexes = {0, 1, 0, 3, 0, 2, 1, 3, 3, 2, 1, 2};
        Point3D[] point3DS = {new Point3D(0, 0, 0), new Point3D(0, 0, -1), new Point3D(1, 0, 0), new Point3D(0, 1, 0)};


        setVertexBuffer(Arrays.asList(point3DS));
        setIndexBuffer(Arrays.asList(indexes));



        setColor(0x00ffff);

    }

}





