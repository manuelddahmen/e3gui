package one.empty3.gui;

import one.empty3.library.*;
import one.empty3.library.core.lighting.Colors;
import one.empty3.library.core.nurbs.ParametricSurface;
import one.empty3.library.core.raytracer.tree.AlgebraicFormulaSyntaxException;
import one.empty3.library.core.raytracer.tree.AlgebricTree;
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
    private boolean running = true;
    private Image lastImage;
    private ITexture iTexture;
    private Camera camera = new Camera(Point3D.Z.mult(-100), Point3D.O0);
    String x = "0", y = "0", z = "0";
    double u0, u1, v0, v1;
    private ZBufferImpl zBuffer;
    private boolean propertyChanged = false;
    private boolean updateGraphics = false;
    private FormFunction ff;


    public ZRunner() {
        log = Logger.getAnonymousLogger();
        log.setLevel(Level.FINEST);
        running = true;
        zBuffer = null;
        System.out.println("ZRunner new instance");
        setUpdateView(ff);
        start();
    }

    public void setUpdateView(FormFunction ff) {

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

    class FunctionSurface extends ParametricSurface {
        private AlgebricTree treeX;
        private AlgebricTree treeY;
        private AlgebricTree treeZ;
        HashMap<String, Double> hashMap = new HashMap<>(2);

        FunctionSurface() throws AlgebraicFormulaSyntaxException {
            hashMap.put("u", 0d);
            hashMap.put("v", 0d);
            setStartU(u0);
            setEndU(u1);
            setIncrU((u1 - u0) / 10);
            setStartV(v0);
            setEndV(v1);
            setIncrV((v1 - v0) / 10);
            treeX = new AlgebricTree(x);
            treeX.getParametersValues().putAll(hashMap);
            treeX.construct();
            treeY = new AlgebricTree(y);
            treeY.getParametersValues().putAll(hashMap);
            treeY.construct();
            treeZ = new AlgebricTree(z);
            treeZ.getParametersValues().putAll(hashMap);
            treeZ.construct();

            texture(iTexture == null ? new TextureCol(Colors.random()) : texture);
        }

        public Point3D calculerPoint3D(double u, double v) {
            try {
                hashMap.put("u", u);
                hashMap.put("v", v);
                double evalX = treeX.eval();
                double evalY = treeY.eval();
                double evalZ = treeZ.eval();
                return new Point3D(evalX, evalY, evalZ);
            } catch (TreeNodeEvalException | AlgebraicFormulaSyntaxException | NullPointerException exceptione) {
                log.warning("Error evaluate Tree eval()");
                return null;
            }


        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //updateView = ff.getPanelView3D();
        if (ff == null)
            log.warning("Form :: null");
        else {
            if(updateView==null || updateView!=ff.getPanelView3D()) {
                updateView = ff.getPanelView3D();
                log.log(Level.WARNING, "UpdateVie instancié");
            }if (updateView == null) {
                log.warning("updateView :: null");
            } else {
            }
        }
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
            case "zDisplayType":
                zBuffer.setDisplayType(updateView.getView().getzDiplayType());
                log.info("zDisplay display : " + updateView.getView().getzDiplayType());
                break;
            case "zRunner":
                log.info("zRunner start : ");
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
            if (ff != null)
                updateView = ff.getPanelView3D();
            if (updateView != null && updateView.getWidth() > 0 && updateView.getHeight() > 0) {
               // log.log(Level.WARNING, "UpdateView" + updateView.getWidth() + ", " + updateView.getHeight() + " " + updateView.hashCode());
                while (!propertyChanged && !updateGraphics) {
                    Graphics updateViewGraphics = updateView.getGraphics();
                    if (lastImage != null) {
                        updateViewGraphics.drawImage(lastImage, 0, 0, updateView.getWidth(), updateView.getHeight(), null);
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.log(Level.WARNING, "start rendering");
                if (zBuffer == null || zBuffer.largeur() != updateView.getWidth() || updateView.getHeight() != zBuffer.hauteur()) {
                    zBuffer = new ZBufferImpl(updateView.getWidth(), updateView.getHeight());
                    log.log(Level.WARNING, "Zbuffer dim" + zBuffer.largeur() + ", " + zBuffer.hauteur());
                }
                //zBuffer.setDimension(updateView.getWidth(), updateView.getHeight());
                zBuffer.scene(new Scene());
                try {
                    FunctionSurface functionSurface = new FunctionSurface();
                    zBuffer.scene().add(functionSurface);
                } catch (AlgebraicFormulaSyntaxException | NullPointerException e) {
                    log.log(Level.WARNING, e.getLocalizedMessage(), e.getLocalizedMessage());
                    log.info("Error adding function");
                }
                addRepere(zBuffer);
                zBuffer.camera(camera);
                zBuffer.next();
                try {
                    zBuffer.draw();
                    lastImage = zBuffer.image();
                    log.info("Draw OK");
                    propertyChanged = false;
                    updateGraphics = false;
                } catch (NullPointerException t) {
                    log.info("function drawing error");
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
