package one.empty3.gui;

import Jama.Matrix;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import one.empty3.library.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class PanelSphereMove extends JPanel {
    private final Draw draw;

    public class Draw extends Thread {
        private ZBufferImpl z;
        private Scene scene;
        private boolean running;

        public Draw(Scene scene1) {
            this.z = ZBufferFactory.instance(getWidth(), getHeight());
            running = true;
            scene = scene1;
            setSize(new Dimension(200, 200));
        }
        @Override
        public void run() {
            while(isRunning()) {
                z.draw(scene);
                if(z.largeur()!=getWidth()||z.hauteur()!=getHeight()) {
                    z = ZBufferFactory.instance(getWidth(), getHeight());
                    System.out.println("Reninit");
                }
                Graphics graphics = getGraphics();
                graphics.drawImage(z.image2(), 0, 0, getWidth(), getHeight(), null);
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
    private Quaternion[][] q;

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
                double x = e.getX();
                double y = e.getY();

                //
                rotate(x, y);
                computeArea();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        Scene scene = new Scene();
        scene.add(new LineSegment(Point3D.O0, Point3D.X, new ColorTexture(Color.RED)));
        scene.add(new LineSegment(Point3D.O0, Point3D.Y, new ColorTexture(Color.GREEN)));
        scene.add(new LineSegment(Point3D.O0, Point3D.Z, new ColorTexture(Color.BLUE)));
        draw = new Draw(scene);
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
        q = new Quaternion[width][height];

        for(int i=0; i<width; i++)
            for(int j=0; j<height; j++) {
                Point3D p = cord3D(i, j);
                q[i][j] = new Quaternion((float)(double)(p.get(0)), (float)(double)(p.get(1)), (float)(double)(p.get(2)), (float)(double)(1.0));
            }

    }
    private void computeArea() {

    }

    private void rotate(double x, double y) {
        Quaternion q1 = q[(int)(x)] [(int)(y)];
        Point3D p = cord3D(x, y);
        x = p.get(0);
        y = p.get(1);
        double z = p.get(2);
        double w = 1.0;
        Quaternion quaternion = new Quaternion((float) x, (float) y, (float) z, (float) w);

        // 1-2*y*y-2*z*z,2*x*y-2*y-2*z*w,2*x*z+2*y*w,2*x*y+2*zy+2*z*w,1-2*x*x-2*z*z,2*y*z-2*x*w,2*x*z-2*y*w,2*y*z+2*x*w,2*x*z-2*y*w,2*y*z+2*x*w,1-2*x*x-2*y*y)
        quaternion = quaternion.mul(q1);

        for(int i=0; i<getWidth(); i++)
            for(int j=0; j<getHeight(); j++) {
                q[i][j] = q[(int)x][(int)y].mul(q1);
            }

        System.out.println(""+quaternion.x+", "+ quaternion.y+", "+quaternion.z+", "+quaternion.w);

    }

    public static void main(String [] args) {
        PanelSphereMove panelSphereMove = new PanelSphereMove();
        JFrame frame = new JFrame("EyeRoll");
        frame.setContentPane(panelSphereMove);
        frame.setSize(new Dimension(200, 200));
        panelSphereMove.q = new Quaternion[200][200];
        panelSphereMove.initComputeArea(200, 200);
        frame.setVisible(true);

    }

}
