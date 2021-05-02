package one.empty3.gui;

import com.jhlabs.vecmath.AxisAngle4f;
import com.jhlabs.vecmath.Quat4f;
import com.jhlabs.vecmath.Tuple4f;
import one.empty3.library.*;
import one.empty3.library.core.lighting.Colors;
import one.empty3.library.core.move.Trajectoires;
import one.empty3.library.shader.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class PanelSphereMove extends JPanel {
    private static int RES = 200;
    private final Draw draw;
    private boolean move = true;
    private Matrix33 matrice = Matrix33.I;
    private Point3D p3_current;
    private Point p2_current;

    public class Draw extends Thread {
        private ZBufferImpl z;
        private Scene scene;
        private boolean running;

        public Draw(Scene scene1) {
            this.z = new ZBufferImpl(getWidth(), getHeight());
            running = true;
            scene = scene1;
            z.scene(scene1);

            setSize(new Dimension(RES, RES));
        }


        @Override
        public void run() {
            while (isRunning()) {
                z.scene(scene);
                try {
                    z.draw();
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
                z.idzpp();
                if (z.largeur() != getWidth() || z.hauteur() != getHeight()) {
                    z = new ZBufferImpl(getWidth(), getHeight());
                    initComputeArea(RES, RES);
                    System.out.println("Reninit");
                }
                Point3D[] colVectors = matrice.getColVectors();
                Point ex = proj(colVectors[0].norme1());
                Point ey = proj(colVectors[1].norme1());
                Point ez = proj(colVectors[2].norme1());
                Graphics graphics = getGraphics();
                if (graphics != null) {
                    //graphics.drawImage(z.image(), 0, 0, getWidth(), getHeight(), null);
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(0, 0, RES, RES);
                    graphics.setColor(Color.RED);
                    graphics.drawLine(RES / 2, RES / 2, (int) ex.getX(), (int) ex.getY());
                    graphics.drawRect((int) ex.getX() - 10, (int) ex.getY() - 10, 10, 10);
                    graphics.setColor(Color.GREEN);
                    graphics.drawLine(RES / 2, RES / 2, (int) ey.getX(), (int) ey.getY());
                    graphics.drawRect((int) ey.getX() - 10, (int) ey.getY() - 10, 10, 10);
                    graphics.setColor(Color.BLUE);
                    graphics.drawLine(RES / 2, RES / 2, (int) ez.getX(), (int) ez.getY());
                    graphics.drawRect((int) ez.getX() - 10, (int) ez.getY() - 10, 10, 10);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public boolean isRunning() {

            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
    }

    Matrix33 matrix33 = Matrix33.I;
    private Quat4f[][] q;
    private Point[][] p;
    private Point3D[][] p3;

    public Matrix33 getMatrix33() {
        return matrix33;
    }

    public void setMatrix33(Matrix33 matrix33) {
        this.matrix33 = matrix33;
    }

    public PanelSphereMove() {
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                move = true;
                double x = e.getX();
                double y = e.getY();

                Point3D point3D = cord3D(x, y);
                rotate(point3D.getX(), point3D.getY());
                computeArea((int) (double) (point3D.getX()), (int) (double) (point3D.getY()));
                move = false;

                initComputeArea(RES, RES);

                System.out.println("Current location on screen : " + p2_current);
                System.out.println("Current location in space" + p3_current);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });


        Scene scene = new Scene();
        scene.add(new LineSegment(Point3D.O0, Point3D.X, new ColorTexture(Color.RED)));
        scene.add(new LineSegment(Point3D.O0, Point3D.Y, new ColorTexture(Color.GREEN)));
        scene.add(new LineSegment(Point3D.O0, Point3D.Z, new ColorTexture(Color.BLUE)));
        scene.cameraActive(new Camera(Point3D.Z.mult(-4.), Point3D.O0, Point3D.Y));
        draw = new Draw(scene);
        draw.start();
    }

    private Point proj(Point3D colVector) {

        return new Point((int) (RES / 2 + colVector.getX() * RES / 4 + colVector.getZ() * RES / 4 / 5.), RES / 2 + (int) (colVector.getY() * RES / 4 + colVector.getZ() * RES / 4 / 5.));

    }

    public Point3D cord3D(double i, double j) {
        return new Point3D(getWidth() / 2 + i / getWidth() * 2., (getHeight() / 2 - j) / getHeight() / 2., Math.sqrt(i * 2));
    }

    public Point cord2D(Point3D p) {
        return new Point((int) (getWidth() + p.get(0) / 2 * getWidth()),
                (int) (getHeight() + p.get(0) / 2 * getHeight())
        );
    }

    private void initComputeArea(int width, int height) {
        q = new Quat4f[width][height];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                Point3D p = cord3D(i, j);
                q[i][j] = new Quat4f((float) (double) (p.get(0)), (float) (double) (p.get(1)), (float) (double) (p.get(2)), (float) (double) (1.0));
            }
        p = new Point[RES][RES];
        p3 = new Point3D[RES][RES];

        for (int i = 0; i < RES; i++) {
            for (int j = 0; j < RES; j++) {
                p3[i][j] = Trajectoires.sphere(1.0 * (i - RES) / RES, 1.0 * (-j + RES) / RES, 1.0);

                p[i][j] = proj(p3[i][j]);
            }
        }
    }

    private void computeArea(int x, int y) {
        p3_current = p3[x][y];
        p2_current = p[x][y];
    }

    private void rotate(double x, double y) {
        Quat4f q1 = q[(int) (x)][(int) (y)];
        Point3D p = cord3D(x, y);
        x = p.get(0);
        y = p.get(1);
        double z = p.get(2);
        double w = 1.0;


        if (p3_current == null) {
            computeArea((int) x, (int) y);
        }
        Point3D Z = p3_current;
        Point3D Y = p3[RES / 2][0];
        Y = Z.prodVect(Z).norme1();
        Point3D X = Z.prodVect(Y);

        if (X.norme() > 0.8) {
            Matrix33 m2 = new Matrix33(new Point3D[]{X, Y, Z}).tild();
            matrice = matrice.mult(m2);
            Point3D[] colVectors = matrice.getColVectors();
            System.out.println("Vectors in matrix\n" + colVectors[0] + ", " + colVectors[1] + ", " + colVectors[2] + ")");
        }
    }


    private void fill(Quat4f quaternion) {
        for (int i = 0; i < getWidth(); i++)
            for (int j = 0; j < getHeight(); j++) {
                q[i][j].set(new AxisAngle4f(quaternion.x + q[i][j].y, quaternion.y + q[i][j].x, quaternion.z + q[i][j].x + q[i][j].y, quaternion.w + q[i][j].w));
            }

    }

    public static void main(String[] args) {
        PanelSphereMove panelSphereMove = new PanelSphereMove();
        JFrame frame = new JFrame("EyeRoll");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelSphereMove);
        frame.setSize(new Dimension(RES, RES));
        frame.setResizable(false);
        panelSphereMove.q = new Quat4f[RES][RES];
        panelSphereMove.initComputeArea(RES, RES);
        frame.setVisible(true);

    }

}
