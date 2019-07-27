package one.empty3.gui;

import one.empty3.library.*;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ConcurrentModificationException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by manue on 26-06-19.
 */
public class ZRunnerMain extends Thread implements PropertyChangeListener {
    private final Logger log;
    private UpdateViewMain updateViewMain = null;
    private boolean running = true;
    private Image lastImage;
    private ITexture iTexture;
    private Camera camera = new Camera(Point3D.Z.mult(-100d), Point3D.O0);
    String x = "0", y = "0", z = "0";
    double u0, u1, v0, v1;
    private ZBufferImpl zBuffer;
    private boolean propertyChanged = false;
    private boolean updateGraphics = false;
    private Main ff;
    private Scene scene;


    public ZRunnerMain() {
        log = Logger.getAnonymousLogger();
        log.setLevel(Level.FINEST);
        running = true;
        zBuffer = null;
        System.out.println("ZRunner new instance");
        setUpdateView(ff);
        start();
    }

    public void setUpdateView(Main ff) {

        this.ff = ff;
    }

    public LineSegment getClick(int x, int y) {
        throw new UnsupportedOperationException("No click");
    }

    public Image getLastImage() {
        return lastImage;
    }

    public void setiTexture(ITexture iTexture) {
        this.iTexture = iTexture;
    }

    public void update() {
        updateGraphics = true;
    }


    public void run() {
        log.info("running renderer loop....");
        while (isRunning()) {
            if (ff != null)
                updateViewMain = ff.getUpdateView();
            if (updateViewMain != null && updateViewMain.getWidth() > 0 && updateViewMain.getHeight() > 0) {
                // log.log(Level.WARNING, "UpdateView" + updateViewMain.getWidth() + ", " + updateViewMain.getHeight() + " " + updateViewMain.hashCode());
                Graphics updateViewGraphics = updateViewMain.getGraphics();
                if (lastImage != null) {
                    updateViewGraphics.drawImage(lastImage, 0, 0, updateViewMain.getWidth(), updateViewMain.getHeight(), null);
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("rendering");
                if (zBuffer == null || zBuffer.largeur() != updateViewMain.getWidth() || updateViewMain.getHeight() != zBuffer.hauteur()) {
                    zBuffer = new ZBufferImpl(updateViewMain.getWidth(), updateViewMain.getHeight());
                    log.log(Level.WARNING, "Zbuffer dim" + zBuffer.largeur() + ", " + zBuffer.hauteur());
                }
                //zBuffer.setDimension(updateViewMain.getWidth(), updateViewMain.getHeight());

                try {
                    Scene scene = ff.getDataModel().getScene();

                    zBuffer.scene(scene);

                    zBuffer.camera(scene.cameraActive());
                    showRepere(zBuffer);
                    zBuffer.draw(scene);
                    lastImage = zBuffer.image();
                    zBuffer.next();
                   // System.out.println(">");
                    propertyChanged = false;
                    updateGraphics = false;
                } catch (NullPointerException ex)
                {
                    ex.printStackTrace();
                }
                catch (ConcurrentModificationException ex) {
                    log.warning("Wait concurrent modification");
                }
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("Ending renderer loop....");
    }

    private void addRepere(Scene scene1) {
        LineSegment ls = new LineSegment(Point3D.O0, Point3D.X.mult(10d));
        ls.texture(new TextureCol(Color.RED));
        zBuffer.draw(ls);
        ls = new LineSegment(Point3D.O0, Point3D.Y.mult(10d));
        ls.texture(new TextureCol(Color.GREEN));
        zBuffer.draw(ls);
        ls = new LineSegment(Point3D.O0, Point3D.Z.mult(10d));
        ls.texture(new TextureCol(Color.BLUE));
        zBuffer.draw(ls);
    }


    public boolean isRunning() {
        return running;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "scene":
                scene = (Scene) evt.getNewValue();
        }

    }


    private void showRepere(ZBuffer zBuffer) {
        Scene scene = new Scene();
        LineSegment ls = new LineSegment(Point3D.O0, Point3D.X);
        ls.texture(new TextureCol(Color.RED));
        scene.add(ls);
        ls = new LineSegment(Point3D.O0, Point3D.Y);
        ls.texture(new TextureCol(Color.GREEN));
        scene.add(ls);
        ls = new LineSegment(Point3D.O0, Point3D.Z);
        ls.texture(new TextureCol(Color.BLUE));
        scene.add(ls);
        zBuffer.draw(scene);
    }
}
