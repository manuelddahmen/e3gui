package one.empty3.gui;

import one.empty3.library.*;
import one.empty3.library.core.nurbs.ParametricCurve;
import one.empty3.library.core.nurbs.ParametricSurface;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MeshGraphicalEdit {

    public MeshGraphicalEdit(Main main) {
        this.main = main;

    }


    private static final int OUT = 1;
    private static final int IN = 0;
    private Main main;


    public Main getMain() {
        return main;
    }

    private MeshGEditorThread getThread() {
        return main.getMeshGEditorThread();
    }

    public MeshEditorBean getBean() {
        return  main.getUpdateView().getView().getMeshEditorBean();
    }
    public void copy(Representable representable, Point3D translate) {
        Representable clone;
        try {
            clone = (Representable) representable.copy();
            ModelBrowser modelBrowser = new ModelBrowser(getMain().getUpdateView().getzRunner().getzBuffer(),
                    getMain().getDataModel().getScene(), Point3D.class);
            modelBrowser.getObjects().forEach(new Consumer<ModelBrowser.Cell>() {
                @Override
                public void accept(ModelBrowser.Cell cell) {
                    if (cell.pRot != null) {
                        cell.pRot.changeTo(cell.pRot.plus(translate));
                    }
                }
            });
        } catch (CopyRepresentableError | IllegalAccessException | InstantiationException copyRepresentableError) {
            copyRepresentableError.printStackTrace();
        }
    }

    private Point3D[] rotateAxis(int rotate, Point3D[] vectors) {
        Point3D[] point3DS = new Point3D[3];
        for (int i = 0; i < 3; i++)
            point3DS[(i + rotate) % 3] = vectors[i];
        return point3DS;
    }

    private Rotation adaptToCurve(Representable representable, ParametricCurve curve, double u, int rotate) {
        Point3D tangente = curve.calculerTangente(u);
        Point3D normale = curve.calculerNormale(u);
        Point3D z = tangente.prodVect(normale);
        Point3D[] point3DS = {tangente, normale, z};

        return new Rotation(new Matrix33(rotateAxis(rotate, point3DS)), curve.calculerPoint3D(u));
    }

    private Rotation adaptToSurface(Representable representable, ParametricSurface surface, double u, double v, int rotate) {
        Point3D tangentU = surface.calculerTangenteU(u, v);
        Point3D tangentV = surface.calculerTangenteV(u, v);
        Point3D z = tangentU.prodVect(tangentV).norme1();
        Point3D[] point3DS = {tangentU, tangentV, z};
        Point3D[] point3DS1 = rotateAxis(rotate, point3DS);
        return new Rotation(new Matrix33(point3DS1), surface.calculerPoint3D(u, v));
    }

    public void copyOn(Representable representable, ParametricCurve pc, double u, int rotate) {
        Representable clone;
        try {
            clone = (Representable) representable.copy();
            ModelBrowser modelBrowser = new ModelBrowser(getMain().getUpdateView().getzRunner().getzBuffer(),
                    getMain().getDataModel().getScene(), Point3D.class);
            modelBrowser.getObjects().forEach(new Consumer<ModelBrowser.Cell>() {
                @Override
                public void accept(ModelBrowser.Cell cell) {
                    if (cell.pRot != null) {
                        cell.pRot.changeTo(cell.pRot.plus(pc.calculerPoint3D(u)));
                    }
                }
            });
            adaptToCurve(clone, pc, u, rotate);
            // TODO ??? orientation Rotation / courbe / surface
        } catch (CopyRepresentableError | IllegalAccessException | InstantiationException copyRepresentableError) {
            copyRepresentableError.printStackTrace();
        }

    }

    public void copyOn(Representable representable, ParametricSurface surface, double u, double v, int rotate) {
        Representable clone;
        try {
            clone = (Representable) representable.copy();
            ModelBrowser modelBrowser = new ModelBrowser(getMain().getUpdateView().getzRunner().getzBuffer(),
                    getMain().getDataModel().getScene(), Point3D.class);
            modelBrowser.getObjects().forEach(new Consumer<ModelBrowser.Cell>() {
                @Override
                public void accept(ModelBrowser.Cell cell) {
                    if (cell.pRot != null) {
                        cell.pRot.changeTo(cell.pRot.plus(surface.calculerPoint3D(u, v)));
                    }
                }
            });
            adaptToSurface(clone, surface, u, v, rotate);
            // TODO ??? orientation Rotation / courbe / surface
        } catch (CopyRepresentableError | IllegalAccessException | InstantiationException copyRepresentableError) {
            copyRepresentableError.printStackTrace();
        }

    }
}
