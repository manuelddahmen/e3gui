package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class EyeBallRoll extends JPanel {
    private static int RES = 200;
    private Draw draw;
    private boolean move = true;
    private Matrix33 matrice = Matrix33.I;
    private int i_current, j_current;
    private Point2D eZ = new Point2D(.2, 0.2);
    private ArrayList<Point3D> ps = new ArrayList<Point3D>();
    private Point3D pInitial;
    private Point3D pMoved;
    private Point3D vAxe = Point3D.X;
    private String inscr = "";

    public class Draw extends Thread {
        private ZBufferImpl z;
        private Scene scene;
        private boolean running;

        public Draw(Scene scene1) {
            this.z = new ZBufferImpl(RES, RES);
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
                    z = new ZBufferImpl(RES, RES);
                    initComputeArea();
                    System.out.println("Reninit");
                }
                Point3D[] colVectors = matrice.getRowVectors();
                Point2D ex = transform3D2D1(colVectors[0]);
                Point2D ey = transform3D2D1(colVectors[1]);
                Point2D ez = transform3D2D1(colVectors[2]);
                Graphics graphics = getGraphics();
                if (graphics != null) {
                    draw(pInitial);
                    draw(pMoved);
                    //graphics.drawImage(z.image(), 0, 0, getWidth(), getHeight(), null);
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(0, 0, RES, RES);

                    graphics.setColor(Color.RED);
                    graphics.drawLine(RES / 2, RES / 2, (int) ex.getX(), (int) ex.getY());
                    graphics.setColor(Color.GREEN);
                    graphics.drawLine(RES / 2, RES / 2, (int) ey.getX(), (int) ey.getY());
                    graphics.setColor(Color.BLUE);
                    graphics.drawLine(RES / 2, RES / 2, (int) ez.getX(), (int) ez.getY());
                    System.out.println("ex, ey, ez" + ex + "\n" + ey + "\n" + ez);


                    ex = transform3D2D1(Point3D.X);
                    ey = transform3D2D1(Point3D.Y);
                    ez = transform3D2D1(Point3D.Z);
                    System.out.println("Systeme d'origine ex, ey, ez" + ex + "\n" + ey + "\n" + ez);

                    graphics.setColor(Color.RED);
                    graphics.drawString("eX", (int) ex.getX(), (int) ex.getY());
                    graphics.setColor(Color.GREEN);
                    graphics.drawString("eY", (int) ey.getX(), (int) ey.getY());
                    graphics.setColor(Color.BLUE);
                    graphics.drawString("eZ", (int) ez.getX(), (int) ez.getY());

                    graphics.drawString(inscr, 0, 0);

                    System.out.println("Angle px,py " + Math.acos(colVectors[0].norme1().dot(colVectors[1].norme1())) / Math.PI * 2);
                    System.out.println("Angle py,pz " + Math.acos(colVectors[1].norme1().dot(colVectors[2].norme1())) / Math.PI * 2);
                    System.out.println("Angle pz,px " + Math.acos(colVectors[2].norme1().dot(colVectors[0].norme1())) / Math.PI * 2);
                    System.out.println("Angle pz,py " + Math.acos(colVectors[2].norme1().dot(colVectors[1].norme1())) / Math.PI * 2);
                    //draw2(sphere(angleA[i_current][j_current], angleB[i_current][j_current], RES));

                    //ps.forEach(point3D -> draw2(point3D));
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

    private void draw2(Point3D p3_current) {
        Graphics graphics = getGraphics();
        if (p3_current.texture() != null) {
            graphics.setColor(new Color(p3_current.texture().getColorAt(0.5, 0.5)));
            graphics.setColor(Color.BLACK);
        } else {
            graphics.setColor(Color.BLACK);
        }
    }


    private void draw(Point3D p3_current) {
        if (p3_current != null) {
            Graphics graphics = getGraphics();
            if (p3_current.texture() != null) {
                graphics.setColor(new Color(p3_current.texture().getColorAt(0.5, 0.5)));
            } else {
                graphics.setColor(Color.BLACK);
            }
            Point2D proj = transform3D2D1(p3_current);
            graphics.drawLine((int) proj.getX(), (int) proj.getY(), 0, 0);
        }
    }


    public EyeBallRoll() {
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vAxe = vAxe.equals(Point3D.X) ? Point3D.Y : (vAxe.equals(Point3D.Y) ? Point3D.Z : (vAxe.equals(Point3D.Z) ? Point3D.X : Point3D.X));
                inscr = "vAxe" + vAxe;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getX() >= 0 && e.getY() < RES && e.getX() >= 0 && e.getY() < RES) {
                    store(e.getX(), e.getY());
                    pInitial = pMoved;
                    pMoved = null;
                } else {
                    pInitial = null;
                    pMoved = null;
                    System.out.println("Error Point2D out");
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                move = true;

                initComputeArea();

                computeArea();

                store(e.getX(), e.getY());

                //System.out.println("Current location on screen : " + p2_current);
                System.out.println("Current location in space (move from ) " + pInitial);
                System.out.println("Current location in space (move to   ) " + pMoved);
                System.out.println("Current location in space i column     " + i_current);
                System.out.println("Current location in space j row        " + j_current);
                System.out.println("Matrice                                " + matrice);

                move = false;
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });


    }

    public Point3D transform2D3D(Point2D p) {
        Point2D p2 = new Point2D(p);
        p2.x = (p.x / RES - 0.5)*2;
        p2.y = (-p.y / RES + 0.5)*2 ;
        double z = 1. - p.x * p.x - p.y * p.y;
        return (new Point3D(p2.x, p2.y, Math.signum(z)*Math.sqrt(Math.abs(z))));
    }

//    public Point2D transform3D2D(Point3D p) {
//        //p = mult(p);
//        return new Point2D((int) (p.getX() / RES - 0.5) * 2, (int) (p.getY() / RES - 0.5) * 2);
//    }

    public Point2D transform3D2D1(Point3D p) {
        //p = matrice.mult(p);
        return new Point2D((int) ((p.getX() + 1.) * RES / 2), (int) ((1. - p.getY()) * RES / 2));
    }

    private synchronized void initComputeArea() {
    }

    private synchronized void computeArea() {
    }

    private void store(double x, double y) {
        int i = (int) x;
        int j = (int) y;
        if (i >= 0 && i < RES && j >= 0 && j < RES) {
            i_current = i;
            j_current = j;
            pMoved = transform2D3D(new Point2D(i, j));
            if (pMoved != null && pInitial != null) {
                Point3D[] matriceTmp = matrice.getRowVectors();


                /**
                 *  D en déduire la rotation. Tourner autour de l'axe passant par 0 et Perpenduculaire à D
                 *  d'un angle <pMoved_1_0,pInitial_0>
                 */
                double angleRot = Math.acos(pMoved.dot(pInitial));

                Point3D D = pMoved.moins(pInitial);
                Point3D p3 = D.prodVect(pInitial);
                Point3D eX = pInitial.norme1();
                Point3D eY = p3.norme1();
                Point3D eZ = eX.prodVect(eY).norme1();


                matrice = new Matrix33(new Point3D[]{eX, eY, eZ}).mult(matrice);

                assert matrice != null;

            }
        }
    }

    /***
     * On cherche n⃗ (x;y;z). On écrit que AB−→−⋅n⃗ =0 et AC−→−⋅n⃗ =0
     * 2) On obtient un système d'inconnue x, y, z.
     *      On résout le système.  Bie
     * @param p vecteur original
     * @return perpendulaire quelconque
     */
    public Point3D perp1(Point3D p) {
        Point3D p1 = new Point3D(p.getX(), 0., 0.);
        Point3D p2 = new Point3D(0.0, p.getY(), 0.);
        Point3D p3 = new Point3D(0., 0., p.getZ());

        Point3D n = p2.norme() > 0.4 ? p2 : ((p1.norme() > 0.4) ? p1 : ((p3.norme() > 0.4) ? p3 : p1));

        return p.prodVect(n);


    }

    public Point3D sphere(double longitude, double latitude, double size) {
        return new Point3D(Math.cos(longitude) * Math.sin(latitude), Math.sin(longitude) * Math.sin(latitude), Math.cos(latitude)).mult(size);
    }

    public Point2D unsphere(double x, double y, double size) {
        x = x / size - 0.5;
        y = -y / size + 0.5;

        return new Point2D((int) (size * Math.atan(y / x)), (int) (size * Math.sqrt(1 - x * x - y * y)));
    }

    public static void main(String[] args) {
        EyeBallRoll panelSphereMove = new EyeBallRoll();
        panelSphereMove.initComputeArea();
        panelSphereMove.computeArea();
        JFrame frame = new JFrame("EyeRoll");
        frame.setLayout(new MigLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelSphereMove);
        frame.setSize(new Dimension(RES, RES));
        frame.setResizable(false);
        frame.setVisible(true);

        Scene scene = new Scene();
        scene.add(new LineSegment(Point3D.O0, Point3D.X, new ColorTexture(Color.RED)));
        scene.add(new LineSegment(Point3D.O0, Point3D.Y, new ColorTexture(Color.GREEN)));
        scene.add(new LineSegment(Point3D.O0, Point3D.Z, new ColorTexture(Color.BLUE)));
        scene.cameraActive(new Camera(Point3D.Z.mult(-4.), Point3D.O0, Point3D.Y));
        panelSphereMove.draw = panelSphereMove.new Draw(scene);
        panelSphereMove.draw.start();


    }

}
