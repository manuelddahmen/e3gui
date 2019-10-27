/*
 *
 *  *  This file is part of Empty3.
 *  *
 *  *     Empty3 is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 2 of the License, or
 *  *     (at your option) any later version.
 *  *
 *  *     Empty3 is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU General Public License
 *  *     along with Empty3.  If not, see <https://www.gnu.org/licenses/>. 2
 *
 *
 */

package one.empty3.gui;

import one.empty3.library.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
    private Main main;
    private Scene scene;
    private PropertyChangeListener changeListener;
    private boolean stopCurrentRender;
    private boolean graphicalEditing;


    public ZRunnerMain() {
        log = Logger.getAnonymousLogger();
        log.setLevel(Level.FINEST);
        running = true;
        zBuffer = null;
        System.out.println("ZRunner new instance");
        start();
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
        boolean renderedImageOK = false;
        log.info("running renderer loop....");
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                while(running) {
                    if (updateViewMain != null && updateViewMain.getWidth() > 0 && updateViewMain.getHeight() > 0) {
                        Graphics updateViewGraphics = updateViewMain.getGraphics();
                        if (lastImage != null) {
                            updateViewGraphics.drawImage(lastImage, 0, 0, updateViewMain.getWidth(), updateViewMain.getHeight(), null);
                        }
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        ThreadGraphicalEditor threadGraphicalEditor = new ThreadGraphicalEditor(getMain());
        threadGraphicalEditor.setMain(getMain());
        threadGraphicalEditor.start();
        changeSupport.addPropertyChangeListener(threadGraphicalEditor);
        while (isRunning()) {
            if (main != null)
                updateViewMain = main.getUpdateView();
            if (updateViewMain != null && updateViewMain.getWidth() > 0 && updateViewMain.getHeight() > 0) {

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (zBuffer == null || zBuffer.largeur() != updateViewMain.getWidth() || updateViewMain.getHeight() != zBuffer.hauteur()) {
                    zBuffer = new ZBufferImpl(updateViewMain.getWidth(), updateViewMain.getHeight());
                    log.info("UpdateView" + updateViewMain.getWidth() + ", " + updateViewMain.getHeight() + " " + updateViewMain.hashCode());
                    log.info("Zbuffer dim" + zBuffer.largeur() + ", " + zBuffer.hauteur());
                }
                //zBuffer.setDimension(updateViewMain.getWidth(), updateViewMain.getHeight());

                try {
                    Scene scene = getMain().getDataModel().getScene();

                    zBuffer.scene(scene);

                    zBuffer.camera(scene.cameraActive());
                    scene.cameraActive.getElem().calculerMatrice(scene.cameraActive.getElem().getVerticale().getElem());
                    zBuffer.setDisplayType(updateViewMain.getDisplayType());
                    showRepere(zBuffer);
                    zBuffer.next();
                    zBuffer.draw(scene);
                    lastImage = zBuffer.image();
                    changeSupport.firePropertyChange("renderedImageOK", null , lastImage);
                    renderedImageOK = true;
                    propertyChanged = false;
                    updateGraphics = false;
                    drawSuccess();
                } catch (NullPointerException ex)
                {
                    changeSupport.firePropertyChange("renderedImageOK", null, 0);
                    drawFailed();
                    renderedImageOK = true;
                    ex.printStackTrace();
                }
                catch (ConcurrentModificationException ex) {
                    changeSupport.firePropertyChange("renderedImageOK", null, 0);
                    drawFailed();
                    renderedImageOK = true;
                    log.warning("Wait concurrent modification");
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                    drawFailed();
                    changeSupport.firePropertyChange("renderedImageOK", null,-1);
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

    private void drawSuccess() {
        Graphics graphics = updateViewMain.getGraphics();
        graphics.setColor(Color.GREEN);
        graphics.fillRect(0, 0, 10, 10);
    }
    private void drawFailed() {
        Graphics graphics = updateViewMain.getGraphics();
        graphics.setColor(Color.RED);
        graphics.fillRect(0, 0, 10, 10);
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

    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener main) {
        changeSupport.addPropertyChangeListener(main);
    }

    public void setStopCurrentRender(boolean stopCurrentRender) {
        this.stopCurrentRender = stopCurrentRender;
    }

    public void setLastImage(BufferedImage lastImage) {
        this.lastImage = lastImage;
    }

    public ZBufferImpl getzBuffer() {
        return zBuffer;
    }

    public void setzBuffer(ZBufferImpl zBuffer) {
        this.zBuffer = zBuffer;
    }

    public void setGraphicalEditing(boolean graphicalEditing) {
        this.graphicalEditing = graphicalEditing;
    }

    public boolean isGraphicalEditing() {
        return graphicalEditing;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
