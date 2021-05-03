package one.empty3.gui;

import one.empty3.library.*;
import one.empty3.library.core.move.Trajectoires;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class PanelSphereMove2 extends JPanel {
    private static int RES = 200;
    private Draw draw;
    private boolean move = true;
    private Matrix33 matrice = Matrix33.I;
    private Point[][] p;
    private Point3D[][][] p3;
    private Point3D p3_current;
    private Point p2_current;
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
                    initComputeArea();
                    System.out.println("Reninit");
                }
                Point3D[] colVectors = matrice.getColVectors();
                Point ex = proj(Point3D.X);
                Point ey = proj(Point3D.Y);
                Point ez = proj(Point3D.Z);
                Graphics graphics = getGraphics();
                if (graphics != null) {
                    draw(p3_current);
                    //graphics.drawImage(z.image(), 0, 0, getWidth(), getHeight(), null);
                    graphics.setColor(Color.WHITE);
                    //graphics.fillRect(0, 0, RES, RES);
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

    private void draw(Point3D p3_current) {
        if(p3_current!=null) {
            Graphics graphics = getGraphics();
            if(p3_current.texture()!=null) {
                graphics.setColor(new Color(p3_current.texture().getColorAt(0.5, 0.5)));
            } else {
                graphics.setColor(Color.BLACK);
            }
            Point proj = proj(p3_current);
            graphics.drawLine((int) proj.getX(), (int) proj.getY(), 0, 0);
        }
    }


    public PanelSphereMove2() {
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                move = true;
                double x = e.getX()*SIZE*1./RES;
                double y = e.getY()*SIZE*1./RES;

                initComputeArea();

                Point3D unproj = unproj(e.getX(), e.getY());

                //unproj = sphere(x, y, RES);

                computeArea();


                store(x, y);

                System.out.println("Current location on screen : " + p2_current);
                System.out.println("Current location in space" + p3_current);


                move = false;
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });


    }

    private Point3D unproj(int x, int y) {
        double px = (x+0.5)/RES;
        double py = (y+0.5)/RES;
        double z = 0.0;
        return new Point3D(px,py,z);

    }

    /***
     *
     * @param colVector point centré dans le cube [-1;1][-1;1][-1;1]
     * @return coordonnées écran
     */
    private Point proj(Point3D colVector) {

        double ii =  RES/2.*colVector.getX()  / 4 + colVector.getZ() / 4 * RES*eZ.getX();
        double ij =  RES/2.*(int) (colVector.getY() * SIZE / 4 + colVector.getZ() / 4 *RES* eZ.getY());
        double ik = (colVector.getZ()+1)*SIZE;
        return new Point((int)ii, (int)ij);
    }

    public Point cord2D(Point3D p) {
        return new Point((int) (getWidth() + p.get(0) / 2 * getWidth()),
                (int) (getHeight() + p.get(0) / 2 * getHeight())
        );
    }

    private void initComputeArea() {
        p = new Point[SIZE][SIZE];
        p3 = new Point3D[SIZE][SIZE][SIZE];
    }

    private void computeArea() {
        for (double i = -2.; i < 2.0; i += 4.0 /SIZE) {
            for (double j = -1; j < 1; j += 2.0 / SIZE) {
                for(double k=-1; k<1; k+= 1./SIZE) {
                    int ii = (int) (((i + 2) / 4)*SIZE);
                    int ij = (int) ((j + 1) / 2*SIZE);
                    int ik = (int) ((k + 1) / 2*SIZE);
                    if(ii>=0 && ii<SIZE && ij>=0&&ij<SIZE&&ik>=0&&ik<SIZE) {
                        p3[ii][ij][ik] = new Point3D(i,j,k);

                    }
                }
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
                    graphics.drawRect((int) p2r.getX(), (int) p2r.getY(), RES/SIZE, RES/SIZE);
                }
            }
    }

    private void store(double x, double y) {
        Point p2 = proj(new Point3D(x, y, 0.0));

        if(p2.getX()>=0&&p2.getX()<SIZE&&p2.getY()>=0&&p2.getY()<SIZE) {
            Point3D p = p3[(int) p2.getX()][(int) p2.getY()][0];
            Point3D ex = p.norme1().prodVect(p3[SIZE/2][0][0]).prodVect(p).mult(-1).norme1();
            Point3D ey = p.mult(ex).norme1();
            matrice.set(0, ex);
            matrice.set(0, ey);
            matrice.set(0, p);

            p2_current = p2;
            p3_current = p;
        }
    }

    public Point3D sphere(double longitude, double latitude, double size) {
        return new Point3D(Math.cos(longitude)*Math.sin(latitude), Math.cos(longitude)*Math.sin(latitude), Math.cos(latitude)).mult(size);
    }
    public Point unsphere(double x, double y, double size) {
        x = x/size-0.5;
        y = -y/size+0.5;

        return new Point((int)(size*Math.atan(y/x)), (int)(size*Math.sqrt(1-x*x-y*y)));
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
