package one.empty3.gui;

import com.jhlabs.vecmath.AxisAngle4f;
import com.jhlabs.vecmath.Quat4f;
import one.empty3.library.*;
import one.empty3.library.core.move.Trajectoires;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class PanelSphereMove extends JPanel {
    private static int RES = 200;
    private final Draw draw;
    private boolean move = true;
    private Matrix33 matrice = Matrix33.I;
    private Point[][] p;
    private Point3D[][] p3;
    private Point3D p3_current;
    private Point p2_current;
    private Matrix33[][] matriceIJ;
    private Point2D eZ = new Point2D(.2, 0.2);
    private static int SIZE = 10;

    public class Draw extends Thread {
        private ZBufferImpl z;
        private Scene scene;
        private boolean running;

        public Draw(Scene scene1) {
            this.z = new ZBufferImpl(SIZE, SIZE);
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
                    z = new ZBufferImpl(SIZE, SIZE);
                    initComputeArea(SIZE, SIZE);
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


    public Matrix33 getMatrice() {
        return matrice;
    }

    public void setMatrice(Matrix33 matrice) {
        this.matrice = matrice;
    }

    public PanelSphereMove() {
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                move = true;
                double x = e.getX()*SIZE/RES;
                double y = e.getY()*SIZE/RES;

                if(matriceIJ==null)
                    initComputeArea(SIZE, SIZE);

                Point3D point3D = cord3D(x, y);
                computeArea((int) (double) (point3D.getX()), (int) (double) (point3D.getY()));


                store(x, y);

                System.out.println("Current location on screen : " + p2_current);
                System.out.println("Current location in space" + p3_current);


                move = false;
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

        return new Point((int) (RES / 2 + colVector.getX() * RES / 4 + colVector.getZ() * RES / 4 * eZ.getX()), RES / 2 +
                (int) (colVector.getY() * RES / 4 + colVector.getZ() * RES / 4 * eZ.getY()));

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
        p = new Point[SIZE][SIZE];
        p3 = new Point3D[SIZE][SIZE];
        matriceIJ = new Matrix33[SIZE][SIZE];
    }

    private void computeArea(int x, int y) {
        for (double i = -2.; i < 2.0; i += 1.0 / SIZE) {
            for (double j = -1; j < 1; j += 1.0 / SIZE / 2.0) {
                int ii = (int) ((i + 1) / 2);
                int ij = (int) (j + 0.5);
                Point3D p = matrice.mult(Trajectoires.sphere(i, j, 1.0));
                Point3D[] colVectors = matrice.getColVectors();
                Point3D ez = p;//.norme1();
                Point3D ex = colVectors[1].norme1().prodVect(ez).mult(-1);
                Point3D ey = ez.mult(ex).norme1();
                matriceIJ[ii][ij] = new Matrix33(new Point3D[]{ex, ey, ez}).tild();
                p3[ii][ij] = matriceIJ[ii][ij].mult(ez);


            }
        }

        Graphics graphics = getGraphics();
        if (graphics != null)
            for (double i = -2.; i < 2.0; i += 1.0 / SIZE) {
                for (double j = -1; j < 1; j += 1.0 / SIZE / 2.0) {
                    int ii = (int) ((i + 1) / 2);
                    int ij = (int) (j + 0.5);
                    Point3D p = matrice.mult(Trajectoires.sphere(i, j, 1.0));
                    Point p2r = proj(p);
                    graphics.setColor(Color.GRAY);
                    graphics.drawRect((int) p2r.getX(), (int) p2r.getY(), 1, 1);
                }
            }
    }

    private void store(double x, double y) {
        p3_current = p3[(int) x][(int) y];
        p2_current = p[(int) x][(int) y];
        if(matriceIJ[(int)x][(int)y]!=null)
            matrice = matriceIJ[(int)x][(int)y];
    }


    public static void main(String[] args) {
        PanelSphereMove panelSphereMove = new PanelSphereMove();
        JFrame frame = new JFrame("EyeRoll");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelSphereMove);
        frame.setSize(new Dimension(RES, RES));
        frame.setResizable(false);
        panelSphereMove.initComputeArea(SIZE, SIZE);
        panelSphereMove.computeArea(0, 0);
        frame.setVisible(true);

    }

}
