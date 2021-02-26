package model;

import transform.Point3D;

import java.util.Arrays;

public class Roof extends Solid {


    public Roof() {

        Point3D[] point3DS = {new Point3D(-1, -1, 0),
                new Point3D(1, -1, 0),
                new Point3D(-1, 1, 0),
                new Point3D(1, 1, 0),
                new Point3D(0, 0, -2),
        };
        setVertexBuffer(Arrays.asList(point3DS));
        Integer[] indexes = {
                0, 1, 0, 2, 0, 4, 1, 3, 3, 2, 4, 2, 4, 1, 4, 3
        };

        setVertexBuffer(Arrays.asList(point3DS));
        setIndexBuffer(Arrays.asList(indexes));
        setColor(0xaaddFF);
    }

}