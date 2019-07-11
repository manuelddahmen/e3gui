package one.empty3.gui;

import one.empty3.library.*;
import one.empty3.library.core.lighting.Colors;
import one.empty3.library.core.nurbs.ParametricSurface;
import one.empty3.library.core.raytracer.tree.AlgebraicFormulaSyntaxException;
import one.empty3.library.core.raytracer.tree.AlgebraicTree;
import one.empty3.library.core.raytracer.tree.TreeNodeEvalException;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by manue on 26-06-19.
 */
public class ZRunner extends Thread implements PropertyChangeListener {
    private final Logger log;
    private UpdateView updateView = null;
    private boolean running;
    private Image lastImage;
    private ITexture iTexture;
    private Camera camera = new Camera(Point3D.Z.mult(-100), Point3D.O0);
    String x = "0", y = "0", z = "0";
    double u0, u1, v0, v1;
    private ZBufferImpl zBuffer;
    private boolean propertyChanged = false;
    private boolean updateGraphics = false;

    public ZRunner() {
        log = Logger.getAnonymousLogger();
        log.setLevel(Level.ALL);
        running = true;
        zBuffer = null;
    }

    public void setUpdateView(UpdateView updateView) {
        this.updateView = updateView;
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

    class FunctionSurface extends ParametricSurface {
        private AlgebraicTree treeX;
        private AlgebraicTree treeY;
        private AlgebraicTree treeZ;
        HashMap hashMap = new HashMap(2);

        public FunctionSurface() {
            hashMap.put("u", 0d);
            hashMap.put("v", 0d);
            setStartU(u0);
            setIncrU(0.01);
            setEndU(u1);
            setStartV(v0);
            setIncrV(0.01);
            setEndV(v1);
            try {
                treeX = new AlgebraicTree(x);
                treeX.setParametersValues(hashMap);
                treeX.construct();
                treeY = new AlgebraicTree(y);
                treeY.setParametersValues(hashMap);
                treeY.construct();
                treeZ = new AlgebraicTree(z);
                treeZ.setParametersValues(hashMap);
                treeZ.construct();

            } catch (AlgebraicFormulaSyntaxException e) {
                e.printStackTrace();
            }
            texture(iTexture == null ? new TextureCol(Colors.random()) : texture);
        }

        public Point3D calculerPoint3D(double u, double v) {
            try {
                System.out.println("+");
                hashMap.put("u", u);
                hashMap.put("v", v);
                double evalX = treeX.eval();
                double evalY = treeY.eval();
                double evalZ = treeZ.eval();
                Point3D point3D = new Point3D(evalX, evalY, evalZ);
                return point3D;
            } catch (TreeNodeEvalException | AlgebraicFormulaSyntaxException e) {
                e.printStackTrace();
                System.out.println("null");
                e.printStackTrace();
                return null;
            }


        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        log.info("Property changed: " + propertyName);
        switch (propertyName) {
            case "xFormula":
                x = updateView.getView().getxFormula();
                log.info("x formula update");
                break;
            case "yFormula":
                y = updateView.getView().getyFormula();
                log.info("y formula update");
                break;
            case "zFormula":
                z = updateView.getView().getzFormula();
                log.info("z formula update");
                break;
            case "uMin":
                u0 = updateView.getView().getuMin();
                log.info("u min update");
                break;
            case "uMax":
                u1 = updateView.getView().getuMax();
                log.info("u max update");
                break;
            case "vMin":
                v0 = updateView.getView().getvMin();
                log.info("v min update");
                break;
            case "vMax":
                v1 = updateView.getView().getvMax();
                log.info("v max update");
                break;
            case "texture":
                log.info("texture update");
                iTexture = updateView.getView().getTexture();
                break;
            case "refresh":
                updateGraphics = true;
                log.info("refresh update");
                break;
            case "camera":
                camera = updateView.getView().getCamera();
                log.info("camera update");
                break;
            case "zbuffer":
                this.zBuffer = updateView.getView().getzBuffer();
                log.info("zbuffer dim update ()");
                break;
            case "zDisplayType":
                zBuffer.setDisplayType(updateView.getView().getzDiplayType());
                log.info("zDisplay display : " + updateView.getView().getzDiplayType());
                break;
            case "zRunner":
                start();
                break;
            default:
                log.log(Level.WARNING, "Property not found");
                break;
        }
        propertyChanged = true;
    }


    public void run() {
        log.info("running renderer loop....");
        while (isRunning()) {
            try {
                if (updateView != null && updateView.getWidth() > 0 && updateView.getHeight() > 0) {
                    while (!propertyChanged && !updateGraphics) {
                        Graphics updateViewGraphics = updateView.getGraphics();
                        if(lastImage!=null) {
                            updateViewGraphics.drawImage(lastImage, 0, 0, updateView.getWidth(), updateView.getHeight(), null);
                        }
                    }
                    log.info("start rendering");
                    if (zBuffer == null || zBuffer.largeur() != updateView.getWidth() || updateView.getHeight() != zBuffer.hauteur()) {
                        updateView.getView().setzBuffer(new ZBufferImpl(updateView.getWidth(), updateView.getHeight()));
                        log.info("Zbbuffer dim" + zBuffer.largeur() + ", " + zBuffer.hauteur());
                    }
                    zBuffer = updateView.getView().getzBuffer();
                    //zBuffer.setDimension(updateView.getWidth(), updateView.getHeight());
                    zBuffer.scene(new Scene());
                    zBuffer.scene().add(new FunctionSurface());
                    addRepere(zBuffer);
                    zBuffer.camera(camera);
                    zBuffer.next();
                    zBuffer.draw();
                    lastImage = zBuffer.image();
                    log.info("image rendered");
                    propertyChanged = false;
                    updateGraphics = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("Ending renderer loop....");
    }

    private void addRepere(ZBufferImpl zBuffer) {
        LineSegment ls = new LineSegment(Point3D.O0, Point3D.X);
        ls.texture(new TextureCol(Color.RED));
        zBuffer.scene().add(ls);
        ls = new LineSegment(Point3D.O0, Point3D.Y);
        ls.texture(new TextureCol(Color.GREEN));
        zBuffer.scene().add(ls);
        ls = new LineSegment(Point3D.O0, Point3D.Z);
        ls.texture(new TextureCol(Color.BLUE));
        zBuffer.scene().add(ls);

    }


    public boolean isRunning() {
        return running;
    }
}
