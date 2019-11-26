/*
 *
 *  *  This file is part of Empty3.
 *  *
 *  *     Empty3 is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
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
import one.empty3.library.core.nurbs.CourbeParametriquePolynomialeBezier;
import one.empty3.library.core.nurbs.ParametricCurve;
import one.empty3.library.core.nurbs.ParametricSurface;
import one.empty3.library.core.nurbs.TubeExtrusion;

import javax.swing.*;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created by manue on 19-11-19.
 */
public class GraphicalEdit2  {
    private static final int OUT = 1;
    private static final int IN = 0;
    private Main main;
    private boolean running = true;
    private boolean endSel1;
    private UpdateViewMain panel;
    private int activeSelection = 0;
    private RepresentableClassList currentSelection;
    private boolean selection;

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

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setMain(Main main) {
        this.main = main;
        JList<Representable> treeSelIn = main.getTreeSelIn();
        treeSelIn.setModel(new ListModelSelection());
        treeSelIn.setCellRenderer(new Rendu());
        JList<Representable> treeSelOut = main.getTreeSelOut();
        treeSelOut.setModel(new ListModelSelection());
        treeSelOut.setCellRenderer(new Rendu());
    }

    public void setSelectingMultipleObjectsIn(boolean selectingMultipleObjectsIn) {
        this.selectingMultipleObjectsIn = selectingMultipleObjectsIn;
    }

    public boolean isSelectingMultipleObjectsIn() {
        return selectingMultipleObjectsIn;
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

    public MyObservableList<Representable> getCurrentSelection() {
        if(getActiveSelection()==0)
            return selectionIn;
        else
             if(getActiveSelection()==1)
                 return selectionOut;
        return null;
    }

    public JList<Representable> getJList() {
        switch (getActiveSelection())
        {
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

    public enum Action {duplicateOnPoints,duplicateOnCurve,duplicateOnSurface, TRANSLATE, ROTATE, extrude};
    private boolean isSelectingIn;
    private boolean isSelectingMultipleIn;
    private boolean isSelectingOut;
    private boolean isSelectingMultipleOut;
    private boolean SelectMultipleIn, SelectArbitraryPointsIn, selectingMultipleObjectsIn;
    private boolean SelectMultipleOut, SelectArbitraryPointsOut;
    private Action actionToPerform = Action.TRANSLATE;
    private MyObservableList<Representable> selectionIn = new MyObservableList<>();
    private MyObservableList<Representable> selectionOut = new MyObservableList<>();
    public GraphicalEdit2() {

    }

    public Main getMain() {
        return main;
    }

    public boolean isRunning() {
        return running;
    }


    public boolean isSelectingIn() {
        return isSelectingIn;
    }

    public void setSelectingIn(boolean selectingIn) {
        isSelectingIn = selectingIn;
    }

    public boolean isSelectingMultipleIn() {
        return isSelectingMultipleIn;
    }

    public void setSelectingMultipleIn(boolean selectingMultipleIn) {
        isSelectingMultipleIn = selectingMultipleIn;
    }


    public boolean isSelectingOut() {
        return isSelectingOut;
    }

    public void setSelectingOut(boolean selectingOut) {
        isSelectingOut = selectingOut;
    }

    public boolean isSelectingMultipleOut() {
        return isSelectingMultipleOut;
    }

    public void setSelectingMultipleOut(boolean selectingMultipleOut) {
        isSelectingMultipleOut = selectingMultipleOut;
    }

    public boolean isSelectMultipleIn() {
        return SelectMultipleIn;
    }

    public void setSelectMultipleIn(boolean selectMultipleIn) {
        SelectMultipleIn = selectMultipleIn;
    }

    public boolean isSelectArbitraryPointsIn() {
        return SelectArbitraryPointsIn;
    }

    public void setSelectArbitraryPointsIn(boolean selectArbitraryPointsIn) {
        SelectArbitraryPointsIn = selectArbitraryPointsIn;
    }

    public boolean isSelectMultipleOut() {
        return SelectMultipleOut;
    }

    public void setSelectMultipleOut(boolean selectMultipleOut) {
        SelectMultipleOut = selectMultipleOut;
    }

    public boolean isSelectArbitraryPointsOut() {
        return SelectArbitraryPointsOut;
    }

    public void setSelectArbitraryPointsOut(boolean selectArbitraryPointsOut) {
        SelectArbitraryPointsOut = selectArbitraryPointsOut;
    }

    public Action getActionToPerform() {
        return actionToPerform;
    }

    public void setActionToPerform(Action actionToPerform) {
        this.actionToPerform = actionToPerform;
    }

    public MyObservableList<Representable> getSelectionIn() {
        return selectionIn;
    }

    public void setSelectionIn(MyObservableList<Representable> selectionIn) {
        this.selectionIn = selectionIn;
    }

    public MyObservableList<Representable> getSelectionOut() {
        return selectionOut;
    }

    public void setSelectionOut(MyObservableList<Representable> selectionOut) {
        this.selectionOut = selectionOut;
    }

    /***
     * Sel1: objets à dupliquer
     * Sel2: points  sur lesquels dupliquer
     * Adapation du repère en le liant à la courbe NON
     */
    public void duplicateOnPoints()
    {

    }
    /***
     * Sel1: objets à dupliquer
     * Sel2: points de la courbe sur lesquels dupliquer
     * Adapation du repère en le liant à la courbe OUI
     */
    public void duplicateOnCurve()
    {

    }

    /***
     * Sel1: objets à dupliquer
     * Sel2: points de la surface àsur lesquelsduplqiuer
     * Adapation du repère en le liant à la surface OUI
     */
    public void duplicateOnSurface()
    {

    }

    /***
     * Sel1 est considéré comme la surface à extruder (donc que des points)
     * Sel2 le chemin d'extrusion
     */
    public void extrude()
    {

    }

    private CourbeParametriquePolynomialeBezier getCurve(ArrayList<Representable> rs)
    {

        int l = 0;
        CourbeParametriquePolynomialeBezier courbeParametriquePolynomialeBezier = new CourbeParametriquePolynomialeBezier();
        for(int i = 0 ; i<rs.size(); i++)
            if(rs.get(i) instanceof Point3D)
            {
                l++;
            }

        Point3D [] ps = new Point3D[l];
        for(int i = 0 ; i<rs.size(); i++)
            if(rs.get(i) instanceof Point3D)
            {
                Point3D p = (Point3D) rs.get(i);
                ps[i] = p;
            }

        courbeParametriquePolynomialeBezier.getCoefficients().setAll(ps);

        return courbeParametriquePolynomialeBezier;
    }


    public void performAction()
    {
        if(actionToPerform.equals(Action.extrude))
        {
            MyObservableList<Representable> selectionIn = getSelectionIn();
            MyObservableList<Representable> selectionOut = getSelectionOut();

            if(selectionIn.size()>0 && selectionOut.size()>0)
            {
                CourbeParametriquePolynomialeBezier curve0 = getCurve(selectionIn);
                CourbeParametriquePolynomialeBezier curve1 = getCurve(selectionOut);
                TubeExtrusion tubeExtrusion = new TubeExtrusion();
                tubeExtrusion.getCurves().data1d.clear();
                tubeExtrusion.getCurves().add(1, curve0);
                tubeExtrusion.getCurves().add(1, curve1);

                getMain().getDataModel().getScene().add(tubeExtrusion);
            }
        } else if(actionToPerform.equals(Action.TRANSLATE))
        {
            MyObservableList<Representable> selectionIn = getSelectionIn();
            if(selectionIn.size()>0)
            {
                getMain().getUpdateView().setTranslate(selectionIn);
            }
        } else if(actionToPerform.equals(Action.ROTATE))
        {
            MyObservableList<Representable> selectionIn = getSelectionIn();
            if(selectionIn.size()>0)
            {
                getMain().getUpdateView().setTranslate(selectionIn);
            }
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
        switch(getActiveSelection()) {
            case IN:
                ((ListModelSelection)main.getTreeSelIn().getModel()).add(0, r);
                getCurrentSelection().add(r);
                System.out.println("added to in");
                break;
            case OUT:
                ((ListModelSelection)main.getTreeSelOut().getModel()).add(0, r);
                getCurrentSelection().add(r);
                System.out.println("added to out");
                break;
        }
    }

    public void remove(Representable r) {
        switch(getActiveSelection()) {/*
            case IN:
                ((ListModelSelection)main.getTreeSelIn().getModel()).remove(r);
                break;
            case OUT:
                ((ListModelSelection)main.getTreeSelOut().getModel()).remove(r);
                break;*/
        }

    }
    public void copy(Representable representable, Point3D translate)
    {
        Representable clone;
        try {
            clone = representable.copy();
            ModelBrowser modelBrowser = new ModelBrowser(getMain().getUpdateView().getzRunner().getzBuffer(),
                    getMain().getDataModel().getScene(), Point3D.class);
            modelBrowser.getObjects().forEach(new Consumer<ModelBrowser.Cell>() {
                @Override
                public void accept(ModelBrowser.Cell cell) {
                    if(cell.pRot instanceof Point3D)
                    {
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

    private Point3D [] rotateAxis(int rotate, Point3D [] vectors)
    {
        Point3D[] point3DS = new Point3D[3];
        for(int i=0; i<3; i++)
            point3DS[(i+rotate)%3] = vectors[i];
        return point3DS;
    }

    private Rotation adaptToCurve(Representable representable, ParametricCurve curve, double u, int rotate)
    {
        Point3D tangente = curve.calculerTangente(u);
        Point3D normale = curve.calculerNormale(u);
        Point3D z = tangente.prodVect(normale);
        Point3D[] point3DS = {tangente, normale, z};

        return new Rotation(new Matrix33(rotateAxis(rotate, point3DS)), curve.calculerPoint3D(u));
    }
    private Rotation adaptToSurface(Representable representable, ParametricSurface surface, double u, double v, int rotate)
    {
        Point3D tangenteU = surface.calculerTangenteU(u, v);
        Point3D tangenteV = surface.calculerTangenteV(u, v);
        Point3D z = tangenteU.prodVect(tangenteV).norme1();
        Point3D[] point3DS = {tangenteU, tangenteV, z};
        Point3D[] point3DS1 = rotateAxis(rotate, point3DS);
        return new Rotation(new Matrix33(point3DS1), surface.calculerPoint3D(u, v));
    }
    public void copyOn(Representable representable, ParametricCurve pc, double u, int rotate)
    {
        Representable clone;
        try {
            clone = representable.copy();
            ModelBrowser modelBrowser = new ModelBrowser(getMain().getUpdateView().getzRunner().getzBuffer(),
                    getMain().getDataModel().getScene(), Point3D.class);
            modelBrowser.getObjects().forEach(new Consumer<ModelBrowser.Cell>() {
                @Override
                public void accept(ModelBrowser.Cell cell) {
                    if(cell.pRot instanceof Point3D)
                    {
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
    public void copyOn(Representable representable, ParametricSurface surface, double u, double v, int rotate)
    {
        Representable clone;
        try {
            clone = representable.copy();
            ModelBrowser modelBrowser = new ModelBrowser(getMain().getUpdateView().getzRunner().getzBuffer(),
                    getMain().getDataModel().getScene(), Point3D.class);
            modelBrowser.getObjects().forEach(new Consumer<ModelBrowser.Cell>() {
                @Override
                public void accept(ModelBrowser.Cell cell) {
                    if(cell.pRot instanceof Point3D)
                    {
                        cell.pRot.changeTo(cell.pRot.plus(surface.calculerPoint3D(u,v)));
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