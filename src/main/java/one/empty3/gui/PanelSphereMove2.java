package one.empty3.gui;

import one.empty3.library.*;
import one.empty3.library.core.move.Trajectoires;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.function.Consumer;

public class PanelSphereMove2 extends JPanel {
    private static int RES = 200;
    private Draw draw;
    private boolean move = true;
    private Matrix33 matrice = Matrix33.I;
    private Point3D[][] p3;
    private double[][] angleA;
    private double[][] angleB;
    private int i_current, j_current;
    private Point2D eZ = new Point2D(.2, 0.2);
    private ArrayList<Point3D> ps = new ArrayList<Point3D>();

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
                Point ex = transform3D2D1(colVectors[0]);
                Point ey = transform3D2D1(colVectors[1]);
                Point ez = transform3D2D1(colVectors[2]);
                Graphics graphics = getGraphics();
                if (graphics != null) {
                    draw(p3[i_current][j_current]);
                    //graphics.drawImage(z.image(), 0, 0, getWidth(), getHeight(), null);
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(0, 0, RES, RES);

                    graphics.setColor(Color.RED);
                    graphics.drawLine(RES / 2, RES / 2, (int) ex.getX(), (int) ex.getY());
                    graphics.setColor(Color.GREEN);
                    graphics.drawLine(RES / 2, RES / 2, (int) ey.getX(), (int) ey.getY());
                    graphics.setColor(Color.BLUE);
                    graphics.drawLine(RES / 2, RES / 2, (int) ez.getX(), (int) ez.getY());
                    System.out.println("ex, ey, ez"+ex+"\n"+ey+"\n"+ez);


                    ex = transform3D2D1(Point3D.X);
                    ey = transform3D2D1(Point3D.Y);
                    ez = transform3D2D1(Point3D.Z);
                    System.out.println("Systeme d'origine ex, ey, ez"+ex+"\n"+ey+"\n"+ez);

                    graphics.setColor(Color.RED);
                    graphics.drawString("eX", (int) ex.getX(), (int) ex.getY());
                    graphics.setColor(Color.GREEN);
                    graphics.drawString("eY", (int) ey.getX(), (int) ey.getY());
                    graphics.setColor(Color.BLUE);
                    graphics.drawString("eZ", (int) ez.getX(), (int) ez.getY());



                    System.out.println("Angle px,py " +Math.acos(colVectors[0].norme1().dot(colVectors[1].norme1()))/Math.PI*2);
                    System.out.println("Angle py,pz " +Math.acos(colVectors[1].norme1().dot(colVectors[2].norme1()))/Math.PI*2);
                    System.out.println("Angle pz,px " +Math.acos(colVectors[2].norme1().dot(colVectors[0].norme1()))/Math.PI*2);
                    System.out.println("Angle pz,py " +Math.acos(colVectors[2].norme1().dot(colVectors[1].norme1()))/Math.PI*2);
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
        Point proj = transform3D2D(p3_current);
        //graphics.drawLine((int) proj.getX(), (int) proj.getY(), 0, 0);
        //graphics.drawLine((int) (double) Math.abs(p3_current.getX()), (int) (double) Math.abs(p3_current.getY()), 0, 0);
    }


    private void draw(Point3D p3_current) {
        if (p3_current != null) {
            Graphics graphics = getGraphics();
            if (p3_current.texture() != null) {
                graphics.setColor(new Color(p3_current.texture().getColorAt(0.5, 0.5)));
            } else {
                graphics.setColor(Color.BLACK);
            }
            Point proj = transform3D2D1(p3_current);
            graphics.drawLine((int) proj.getX(), (int) proj.getY(), 0, 0);
        }
    }


    public PanelSphereMove2() {
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                move = true;
                Point3D p = transform2D3D(new Point(e.getX(), e.getY()));

                initComputeArea();

                computeArea();

                store(e.getX(), e.getY(), p);

                //System.out.println("Current location on screen : " + p2_current);
                System.out.println("Current location in space          " + p3[i_current][j_current]);
                System.out.println("Current location in space i column " + i_current);
                System.out.println("Current location in space j row    " + j_current);
                System.out.println("Matrice                            " + matrice);

                move = false;
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });


    }

    public Point3D transform2D3D(Point p) {
        return new Point3D(p.getX() * RES / 2 + RES / 2, -p.getY() * RES / 2 + RES / 2, 0.0);
    }

    public Point transform3D2D(Point3D p) {
        return new Point((int) (p.getX() / RES - 0.5) * 2, (int) (p.getY() / RES - 0.5) * 2);
    }

    public Point transform3D2D1(Point3D p) {
        return new Point((int) ((p.getX() + 1.)* RES  / 2), (int) ((p.getY()  + 1.) * RES / 2));
    }

    private synchronized void initComputeArea() {
        p3 = new Point3D[RES][RES];
        angleA = new double[RES][RES];
        angleB = new double[RES][RES];
    }

    private synchronized void computeArea() {
        for (double i = -2.; i < 2.0; i += 4.0 / RES) {
            for (double j = -1; j < 1; j += 2.0 / RES) {
                int ii = (int) (((i + 2) / 4) * RES);
                int ij = (int) ((j + 1) / 2 * RES);

                if (ii >= 0 && ii < RES && ij >= 0 && ij < RES) {
                    p3[ii][ij] = sphere(i * Math.PI, j * Math.PI, 1.0);
                    angleA[ii][ij] = i * Math.PI;
                    angleB[ii][ij] = j * Math.PI;
                }
            }
        }


        Graphics graphics = getGraphics();
        if (graphics != null)
            for (double i = -2.; i < 2.0; i += 1.0 / RES) {
                for (double j = -1; j < 1; j += 1.0 / RES / 2.0) {
                    int ii = (int) ((i + 1) / 2);
                    int ij = (int) (j + 0.5);
                    Point3D p = sphere(i, j, 1.0);
                    Point p2r = transform3D2D(p);
                    graphics.setColor(Color.GRAY);
                    //graphics.drawRect((int) p2r.getX(), (int) p2r.getY(), 5, 5);
                }
            }
    }

    private void store(double x, double y, Point3D p) {
        int i = (int) x;
        int j = (int) y;
        if(i>=0 && i<RES&&j>=0&&j<RES) {
            i_current = (int) x;
            j_current = (int) y;
            Point3D p2 = p3[i_current][j_current].norme1();
            if (p2 != null) {
                ps.add(p2);

                Point3D ex = perp1(p2);
                Point3D ey = ex.prodVect(p2).norme1();
                Point3D ez = p2;
                matrice.set(0, ex);
                matrice.set(1, ey);
                matrice.set(2, ez);
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
        Point3D p1 = new Point3D(p.getX(),0.,0.);
        Point3D p2  = new Point3D(0.0,p.getY(),0.);
        Point3D p3 = new Point3D(0.,0., p.getZ());

        Point3D n = p2.norme()>0.3?p2:((p1.norme()>0.3)?p1:((p3.norme()>0.3)?p3:p1));

        return p.prodVect(n);



    }
    public Point3D sphere(double longitude, double latitude, double size) {
        return new Point3D(Math.cos(longitude) * Math.sin(latitude), Math.sin(longitude) * Math.sin(latitude), Math.cos(latitude)).mult(size);
    }

    public Point unsphere(double x, double y, double size) {
        x = x / size - 0.5;
        y = -y / size + 0.5;

        return new Point((int) (size * Math.atan(y / x)), (int) (size * Math.sqrt(1 - x * x - y * y)));
    }

    public static void main(String[] args) {
        PanelSphereMove2 panelSphereMove = new PanelSphereMove2();
        panelSphereMove.initComputeArea();
        panelSphereMove.computeArea();
        JFrame frame = new JFrame("EyeRoll");
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
