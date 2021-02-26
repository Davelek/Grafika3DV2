import model.*;
import renderer.RasteriserLine;
import renderer.Renderer;
import transform.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Main extends JFrame {
    private int clickX, clickY, pohledX, pohledY;
    private Camera camera = new Camera().withAzimuth(-2.2).withPosition(new Vec3D(7, 9, -7)).withZenith(0.5);
    private BufferedImage img;
    private JPanel panel;
    private Renderer renderer;
    private Mat4 solidTransform = new Mat4Identity();
    private Mat4 prespective = new Mat4Identity();
    private ArrayList<Solid> solids = new ArrayList<>();

    private int klik = 0;


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Main(800, 600).start());
    }


    private Main(int width, int height) {

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        setLayout(new BorderLayout());
        setTitle("PGRF ukol 3");

        setResizable(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };

        panel.setPreferredSize(new Dimension(width, height));


        // panel.requestFocus();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_P) {
                    camera = new Camera()
                            .withPosition(new Vec3D(0, -6, 0))
                            .withAzimuth(Math.toRadians(90))
                            .withZenith(Math.toRadians(0));

                    prespective = new Mat4OrthoRH(7, 8, 0.01, 25);
                    renderer.setProj(prespective);

                    draw();

                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {

                    Mat4Scale mat4Scale = new Mat4Scale(1.2, 1.2, 1.2);
                    for (Solid solid : solids
                    ) {
                        if (solid.isTransformAble())
                            solid.setTransform(solid.getTransform().mul(mat4Scale));
                    }
                    draw();
                }

                if (e.getKeyCode() == KeyEvent.VK_W) {
                    //  System.out.println("proč najednou nefunguješwwww");
                    camera = camera.forward(0.5);
                    draw();
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    camera = camera.backward(0.5);


                    draw();
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    camera = camera.left(0.5);
                    draw();
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    camera = camera.right(0.5);
                    draw();
                }
                if (e.getKeyCode() == KeyEvent.VK_ADD) {
                    camera = camera.up(0.5);
                    draw();
                }
                if (e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
                    camera = camera.down(0.5);
                    draw();
                }
                if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_LEFT) {
                    camera = camera.addAzimuth(0.1);
                    draw();
                }


                if (e.getKeyCode() == KeyEvent.VK_DOWN) {

                    Mat4Scale mat4Scale = new Mat4Scale(0.8, 0.8, 0.8);
                    for (Solid solid : solids
                    ) {
                        if (solid.isTransformAble()) {
                            solid.setTransform(solid.getTransform().mul(mat4Scale));
                        }
                    }

                    draw();
                }

                if (e.getKeyCode() == KeyEvent.VK_E || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    camera = camera.addAzimuth(-0.1);
                    draw();
                }

                if (e.getKeyCode() == KeyEvent.VK_R) {
                    camera = new Camera().withAzimuth(-2.2).withPosition(new Vec3D(7, 9, -7)).withZenith(0.5);
                    prespective = new Mat4PerspRH(Math.PI / 3, 4 / 3.0, 0.01, 25);
                    renderer.setProj(prespective);
                    draw();
                }


            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {


                    clickX = e.getX();
                    clickY = e.getY();
                    klik = 1;

                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    pohledX = e.getX();
                    pohledY = e.getY();
                    klik = 2;


                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                klik = 0;

            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent f) {

                if (klik == 1) {


                    for (Solid solid : solids
                    ) {
                        Mat4RotXYZ rot = new Mat4RotXYZ(-(clickX - f.getX()) * 0.02, -(f.getY() - clickY) * (0.02), 0);
                        if (solid.isTransformAble())
                            solid.setTransform(solid.getTransform().mul(rot));


                    }
                    draw();
                    clickX = f.getX();
                    clickY = f.getY();
                }
                if (klik == 2) {


                    camera = camera.addAzimuth((f.getX() - pohledX) * (0.002)).addZenith((f.getY() - pohledY) * (0.002));
                    pohledX = f.getX();
                    pohledY = f.getY();

                    draw();

                }
            }


        });

        add(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);


    }

    private void start() {
        renderer = new Renderer(new RasteriserLine(img));

        solidTransform = new Mat4RotX(Math.PI / 4).mul(new Mat4RotY(-Math.PI / 4)).mul(new Mat4Transl(2, 2, 0));
        Mat4 view = new Mat4ViewRH(new Vec3D(0, 10, 10), new Vec3D(-1, -1, -1), new Vec3D(0, 1, 0));
        prespective = new Mat4PerspRH(Math.PI / 3, 4 / 3.0, 0.01, 25);

        renderer.setProj(prespective);
        renderer.setView(view);
        solids.add(new Axis("x"));
        solids.add(new Axis("y"));
        solids.add(new Axis("z"));
        solids.add(new Tetrahedron());
        solids.add(new Cube());
        solids.add(new Floor(6, 6));
        Mat4Transl mat4Scale2 = new Mat4Transl(-3, -3, 0);
        solids.get(5).setTransform(solids.get(5).getTransform().mul(mat4Scale2));
        solids.add(new Cube());
        Mat4Transl mat4Scale = new Mat4Transl(-4, 2, 0);
        solids.get(6).setTransform(solids.get(6).getTransform().mul(mat4Scale));
        solids.add(new Roof());
        Mat4Transl mat4Scale3 = new Mat4Transl(-4, 2, -2);
        solids.get(7).setTransform(solids.get(7).getTransform().mul(mat4Scale3));


        // addStaticSolid();
        draw();
        panel.repaint();
    }


    private void present(Graphics graphics) {
        graphics.drawImage(img, 0, 0, null);
    }

    private void draw() {

        clear();
        renderer.setView(camera.getViewMatrix());


        renderer.draw(solids);

        panel.repaint();

    }


    private void clear() {
        Graphics gr = img.getGraphics();
        gr.setColor(new Color(0x2f2f2f));
        gr.fillRect(0, 0, img.getWidth(), img.getHeight());

    }

}
