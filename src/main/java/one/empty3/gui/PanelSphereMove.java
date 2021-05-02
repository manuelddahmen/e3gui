package one.empty3.gui;

import com.jhlabs.vecmath.AxisAngle4f;
import com.jhlabs.vecmath.Quat4f;
import com.jhlabs.vecmath.Tuple4f;
import one.empty3.library.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class PanelSphereMove extends JPanel {
    private static final int RES = 200;
    private final Draw draw;
    private boolean move = true;

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
            while(isRunning()) {
                z.scene(scene);
                try {
                    z.draw();
                }catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
                z.idzpp();
                if(z.largeur()!=getWidth()||z.hauteur()!=getHeight()) {
                    z  = new ZBufferImpl(getWidth(), getHeight());
                    initComputeArea(RES, RES);
                    System.out.println("Reninit");
                }
                Graphics graphics = getGraphics();
                while(!move) {
                    graphics.drawImage(z.image2(), 0, 0, getWidth(), getHeight(), null);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
                computeArea();
                move =false;
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

    public Point3D cord3D(double i, double j) {
        return new Point3D(getWidth()/2+i/getWidth()*2., (getHeight()/2-j)/getHeight()/2., Math.sqrt(i*2));
    }
    public Point cord2D(Point3D p) {
        return new Point((int)(getWidth()+p.get(0)/2*getWidth()),
                (int)(getHeight()+p.get(0)/2*getHeight())
                );
    }

    private void initComputeArea(int width, int height) {
        q = new Quat4f[width][height];

        for(int i=0; i<width; i++)
            for(int j=0; j<height; j++) {
                Point3D p = cord3D(i, j);
                q[i][j] = new Quat4f((float)(double)(p.get(0)), (float)(double)(p.get(1)), (float)(double)(p.get(2)), (float)(double)(1.0));
            }

    }
    private void computeArea() {

    }

    private void rotate(double x, double y) {
        Quat4f q1 = q[(int)(x)] [(int)(y)];
        Point3D p = cord3D(x, y);
        x = p.get(0);
        y = p.get(1);
        double z = p.get(2);
        double w = 1.0;
        Quat4f quaternion = new Quat4f((float) x, (float) y, (float) z, (float) w);
        // 1-2*y*y-2*z*z,2*x*y-2*y-2*z*w,2*x*z+2*y*w,2*x*y+2*zy+2*z*w,1-2*x*x-2*z*z,2*y*z-2*x*w,2*x*z-2*y*w,2*y*z+2*x*w,2*x*z-2*y*w,2*y*z+2*x*w,1-2*x*x-2*y*y)
        quaternion.set(new AxisAngle4f((float)x, (float)y, (float)1.0, (float) (Math.atan(y/x))));

        for(int i=0; i<getWidth(); i++)
            for(int j=0; j<getHeight(); j++) {
                q[i][j].add(new Tuple4f(q1.x, q1.y, q1.z, q1.w));
            }

        System.out.println(""+quaternion.x+", "+ quaternion.y+", "+quaternion.z+", "+quaternion.w);

    }

    public static void main(String [] args) {
        PanelSphereMove panelSphereMove = new PanelSphereMove();
        JFrame frame = new JFrame("EyeRoll");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelSphereMove);
        frame.setSize(new Dimension(RES, RES));
        panelSphereMove.q = new Quat4f[RES][RES];
        panelSphereMove.initComputeArea(RES, RES);
        frame.setVisible(true);

    }

}
