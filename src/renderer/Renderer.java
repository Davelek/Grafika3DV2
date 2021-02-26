package renderer;

import model.Solid;
import transform.Mat4;
import transform.Mat4Identity;
import transform.Point3D;
import transform.Vec3D;

import java.util.ArrayList;
import java.util.List;

public class Renderer {


    private Mat4 view = new Mat4Identity();
    private Mat4 proj = new Mat4Identity();

    private Rasteriser rasteriser;


    public Renderer(Rasteriser rasteriser) {
        this.rasteriser = rasteriser;
    }

    public void setRasteriser(Rasteriser rasteriser) {
        this.rasteriser = rasteriser;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }


    private void renderLine(Point3D a, Point3D b, int color) {

        if (orez(a)) {
            if (orez(b)) {
                Vec3D va;
                Vec3D vb;
                if (!a.dehomog().isPresent() || !b.dehomog().isPresent())
                    return;
                va = a.dehomog().get();
                vb = b.dehomog().get();

                double x1 = va.getX();
                double y1 = va.getY();
                double x2 = vb.getX();
                double y2 = vb.getY();


                rasteriser.drawLine(x1, y1, x2, y2, color);
            }
        }
    }

    public void draw(List<Solid> solids) {
        for (Solid solid : solids
        ) {
            List<Point3D> vertexes = new ArrayList<>();
            Mat4 transform = solid.getTransform().mul(view).mul(proj);
            for (Point3D point : solid.getVertexBuffer()) {
                vertexes.add(point.mul(transform));
            }
            List<Integer> indexBuffer = solid.getIndexBuffer();
            for (int i = 0; i <= indexBuffer.size() - 1; i = i + 2) {
                Point3D a = vertexes.get(indexBuffer.get(i));
                Point3D b = vertexes.get(indexBuffer.get(i + 1));
                renderLine(a, b, solid.getColor());
            }
        }
    }


    private boolean orez(Point3D point) {
        if ((-point.getW() <= point.getX() && point.getY() <= point.getW() && 0 <= point.getZ() && point.getZ() <= point.getW()))
            return true;
        return false;
    }

}
