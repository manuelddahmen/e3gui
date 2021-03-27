package one.empty3.gui;

import one.empty3.library.*;
import one.empty3.library.core.nurbs.CourbeParametriquePolynomialeBezier;
import one.empty3.library.core.nurbs.ParametricCurve;
import one.empty3.library.core.nurbs.ParametricSurface;
import one.empty3.library.core.nurbs.TubeExtrusion;

import javax.swing.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GraphicalEditMesh {


    private boolean selecting;

    public GraphicalEditMesh() {
        selectionIn = new ArrayList<>();
        selectionOut = new ArrayList<>();

    }


    private static final int OUT = 1;
    private static final int IN = 0;
    private Main main;
    private boolean activeGraphicalEdit = true;
    private boolean endSel1;
    private UpdateViewMain panel;
    private int activeSelection = 0;
    private RepresentableClassList currentSelection;
    private boolean selection;
    private boolean transSel;
    private boolean rotSel;

    public boolean isEndSel1() {
        return endSel1;
    }

    public boolean isStartSel1() {
        return startSel1;
    }

    public void setStartSel1(boolean startSel1) {
        this.startSel1 = startSel1;
    }

    private boolean startSel1;

    public void setActiveGraphicalEdit(boolean activeGraphicalEdit) {
        this.activeGraphicalEdit = activeGraphicalEdit;
    }

    public void setMain(Main main) {
        this.main = main;
    }


    public void setEndSel1(boolean endSel1) {
        this.endSel1 = endSel1;
    }

    public void setActiveSelection(int activeSelection) {
        this.activeSelection = activeSelection;
    }

    public int getActiveSelection() {
        return activeSelection;
    }

    public ArrayList<Representable> getCurrentSelection() {
        if (getActiveSelection() == 0)
            return selectionIn;
        else if (getActiveSelection() == 1)
            return selectionOut;
        return null;
    }

    public JList<Representable> getJList() {
        switch (getActiveSelection()) {
            case 0:
                return main.getTreeSelIn();
            case 1:
                return main.getTreeSelOut();
        }
        return null;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }

    public boolean isSelection() {
        return selection;
    }

    public boolean getSelection() {
        return selection;
    }

    public void setTransSel(boolean transSel) {
        this.transSel = transSel;
    }

    public void setRotSel(boolean rotSel) {
        this.rotSel = rotSel;
    }

    public boolean isSelectingMultipleObjects() {
        return selectingMultipleObjects;
    }

    public void setSelectingMultipleObjects(boolean selectingMultipleObjects) {
        this.selectingMultipleObjects = selectingMultipleObjects;
    }

    public boolean isSelecting() {
        return selecting;
    }

    public enum Action {duplicateOnPoints, duplicateOnCurve, duplicateOnSurface, TRANSLATE, ROTATE, extrude, SELECT}

    ;
    private boolean isSelectingIn;
    private boolean isSelectingOut;
    private boolean selectingMultipleObjects = false;
    private boolean selectArbitraryPoints = false;
    private one.empty3.gui.GraphicalEdit2.Action actionToPerform = one.empty3.gui.GraphicalEdit2.Action.SELECT;
    private ArrayList<Representable> selectionIn;
    private ArrayList<Representable> selectionOut;

    public Main getMain() {
        return main;
    }

    public boolean isActiveGraphicalEdit() {
        return activeGraphicalEdit;
    }


    public boolean isSelectingIn() {
        return isSelectingIn;
    }

    public void setSelectingIn(boolean selectingIn) {
        isSelectingIn = selectingIn;
    }


    public boolean isSelectingOut() {
        return isSelectingOut;
    }

    public void setSelectingOut(boolean selectingOut) {
        isSelectingOut = selectingOut;
    }


    public boolean isSelectArbitraryPoints() {
        return selectArbitraryPoints;
    }

    public void setSelectArbitraryPointsIn(boolean selectArbitraryPoints) {
        this.selectArbitraryPoints = selectArbitraryPoints;
    }


    public one.empty3.gui.GraphicalEdit2.Action getActionToPerform() {
        return actionToPerform;
    }

    public void setActionToPerform(one.empty3.gui.GraphicalEdit2.Action actionToPerform) {
        this.actionToPerform = actionToPerform;
    }

    public ArrayList<Representable> getSelectionIn() {
        return selectionIn;
    }

    public void setSelectionIn(ArrayList<Representable> selectionIn) {
        this.selectionIn = selectionIn;
    }

    public ArrayList<Representable> getSelectionOut() {
        return selectionOut;
    }

    public void setSelectionOut(ArrayList<Representable> selectionOut) {
        this.selectionOut = selectionOut;
    }

    /***
     * Sel1: objets à dupliquer
     * Sel2: points  sur lesquels dupliquer
     * Adapation du repère en le liant à la courbe NON
     */
    public void duplicateOnPoints() {

    }

    /***
     * Sel1: objets à dupliquer
     * Sel2: points de la courbe sur lesquels dupliquer
     * Adapation du repère en le liant à la courbe OUI
     */
    public void duplicateOnCurve() {

    }

    /***
     * Sel1: objets à dupliquer
     * Sel2: points de la surface àsur lesquelsduplqiuer
     * Adapation du repère en le liant à la surface OUI
     */
    public void duplicateOnSurface() {

    }

    /***
     * Sel1 est considéré comme la surface à extruder (donc que des points)
     * Sel2 le chemin d'extrusion
     */
    public void extrude() {

    }

    private CourbeParametriquePolynomialeBezier getCurve(ArrayList<Representable> rs) {

        int l = 0;
        CourbeParametriquePolynomialeBezier courbeParametriquePolynomialeBezier = new CourbeParametriquePolynomialeBezier();
        for (int i = 0; i < rs.size(); i++)
            if (rs.get(i) instanceof Point3D) {
                l++;
            }

        Point3D[] ps = new Point3D[l];
        for (int i = 0; i < rs.size(); i++)
            if (rs.get(i) instanceof Point3D) {
                Point3D p = (Point3D) rs.get(i);
                ps[i] = p;
            }

        courbeParametriquePolynomialeBezier.getCoefficients().setAll(ps);

        return courbeParametriquePolynomialeBezier;
    }


    public void performAction() {
        if (actionToPerform.equals(one.empty3.gui.GraphicalEdit2.Action.extrude)) {
            ArrayList<Representable> selectionIn = getSelectionIn();
            ArrayList<Representable> selectionOut = getSelectionOut();

            if (selectionIn.size() > 0 && selectionOut.size() > 0) {
                CourbeParametriquePolynomialeBezier curve0 = getCurve(selectionIn);
                CourbeParametriquePolynomialeBezier curve1 = getCurve(selectionOut);
                TubeExtrusion tubeExtrusion = new TubeExtrusion();
                tubeExtrusion.getCurves().data1d.clear();
                tubeExtrusion.getCurves().add(1, curve0);
                tubeExtrusion.getCurves().add(1, curve1);

                getMain().getDataModel().getScene().add(tubeExtrusion);
            }
        } else if (actionToPerform.equals(one.empty3.gui.GraphicalEdit2.Action.TRANSLATE)) {

        } else if (actionToPerform.equals(one.empty3.gui.GraphicalEdit2.Action.ROTATE)) {
        } else if (actionToPerform.equals(one.empty3.gui.GraphicalEdit2.Action.duplicateOnCurve) || actionToPerform.equals(one.empty3.gui.GraphicalEdit2.Action.duplicateOnCurve)
                || actionToPerform.equals(one.empty3.gui.GraphicalEdit2.Action.duplicateOnPoints)) {
            getSelectionIn().forEach(representable -> {
                if (!representable.getClass().equals(Point3D.class)) {
                    getSelectionOut().forEach(on -> {
                        if (on instanceof ParametricCurve) {
                            ParametricCurve on1 = (ParametricCurve) on;
                            copyOn(representable, on1, (double) Double.parseDouble(
                                    main.getJtextfieldU()), 0);
                        } else if (on instanceof ParametricSurface) {
                            copyOn(representable, (ParametricSurface) on, Double.parseDouble(main.getJtextfieldU0()),
                                    Double.parseDouble(main.getJtextfield0V()), 0);
                        } else if (!(on instanceof Point3D)) {
                            copy(representable, on.getRotation().getElem().getCentreRot().getElem());

                        }
                    });
                }
            });
        } else if (actionToPerform.equals(one.empty3.gui.GraphicalEdit2.Action.duplicateOnSurface)) {

        }

    }

    public UpdateViewMain getPanel() {
        return panel;
    }

    public void setPanel(UpdateViewMain panel) {
        this.panel = panel;
    }

    public void run() {

    }


    public void add(Representable r) {
        ArrayList<Representable> currentSelection = getCurrentSelection();
        currentSelection.add(r);

        // getJList().add(r.toString(), new JLabel(r.toString()));
        //getJList().add(getJList().getCellRenderer().getListCellRendererComponent(getJList(), r, getJList().getModel().getSize(),
        //        false, false));
    }

    public void remove(Representable r) {
        switch (getActiveSelection()) {/*
            case IN:
                ((ListModelSelection)main.getTreeSelIn().getModel()).remove(r);
                break;
            case OUT:
                ((ListModelSelection)main.getTreeSelOut().getModel()).remove(r);
                break;*/
        }

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
                    if (cell.pRot instanceof Point3D) {
                        cell.pRot.changeTo(cell.pRot.plus(translate));
                    }
                }
            });
        } catch (CopyRepresentableError copyRepresentableError) {
            copyRepresentableError.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
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
        Point3D tangenteU = surface.calculerTangenteU(u, v);
        Point3D tangenteV = surface.calculerTangenteV(u, v);
        Point3D z = tangenteU.prodVect(tangenteV).norme1();
        Point3D[] point3DS = {tangenteU, tangenteV, z};
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
                    if (cell.pRot instanceof Point3D) {
                        cell.pRot.changeTo(cell.pRot.plus(pc.calculerPoint3D(u)));
                    }
                }
            });
            adaptToCurve(clone, pc, u, rotate);
            // TODO ??? orientation Rotation / courbe / surface
        } catch (CopyRepresentableError copyRepresentableError) {
            copyRepresentableError.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
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
                    if (cell.pRot instanceof Point3D) {
                        cell.pRot.changeTo(cell.pRot.plus(surface.calculerPoint3D(u, v)));
                    }
                }
            });
            adaptToSurface(clone, surface, u, v, rotate);
            // TODO ??? orientation Rotation / courbe / surface
        } catch (CopyRepresentableError copyRepresentableError) {
            copyRepresentableError.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
