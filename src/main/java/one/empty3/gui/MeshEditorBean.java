package one.empty3.gui;

import one.empty3.library.CopyRepresentableError;
import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.library.StructureMatrix;
import one.empty3.library.core.nurbs.CourbeParametriquePolynomiale;
import one.empty3.library.core.nurbs.SurfaceParametriquePolynomialeBezier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class MeshEditorBean {
    public static final int MESH_EDITOR_ParametricSurface = 0;
    public static final int MESH_EDITOR_Sphere = 1;
    public static final int MESH_EDITOR_Cube = 2;
    public static final int MESH_EDITOR_Plane = 4;
    private static final int OP_SELECTION_TRANSLATION = 0;
    private static final int OP_NEW_ROW_COL           = 1;
    private static final int OP_TRANSLATE_SURFACE     = 2;


    private int meshType;
    private boolean activateMarkers = false;
    private boolean selection;
    private boolean translation;
    private boolean newPoint;
    private boolean newRow;
    private boolean newCol;
    private double translateXonS;
    private double translateYonS;
    private double translateZonS;
    private boolean translateOnSuv;
    private double translateOnSu;
    private double translateOnSv;
    private ArrayList<Point3D> inSelection = new ArrayList<>();
    private HashMap<PMove, Point3D> inSelectionMoves;
    private int opType = 0;

    public static class ReplaceMatrix {
        private Representable representable;
        protected StructureMatrix<Point3D> in;
        protected StructureMatrix<Point3D> outs;

        public ReplaceMatrix(StructureMatrix<Point3D> origin, Representable r) {
            this.in = origin;
            try {
                this.outs = origin.copy();
                this.representable = r;
            } catch (IllegalAccessException | CopyRepresentableError | InstantiationException e) {
                e.printStackTrace();
            }
        }

        public boolean updateIjp(Point3D p, int i, int j) {
            if (i >= 0 && j >= 0 && outs.getDim() == 2) {
                outs.setElem(p, i, j);
                return true;
            }
            if (i >= 0 && j < 0 && outs.getDim() == 1) {
                outs.setElem(p, i);
                return true;
            }
            if (i < 0 && j < 0 && outs.getDim() == 0) {
                outs.setElem(p);
                return true;
            }
            return false;
        }

        public Representable getRepresentable() {
            return representable;
        }

        public void setRepresentable(Representable representable) {
            this.representable = representable;
        }

        public StructureMatrix<Point3D> getIn() {
            return in;
        }

        public void setIn(StructureMatrix<Point3D> in) {
            this.in = in;
        }

        public StructureMatrix<Point3D> getOuts() {
            return outs;
        }

        public void setOuts(StructureMatrix<Point3D> outs) {
            this.outs = outs;
        }

        @Override
        public String toString() {
            return "ReplaceMatrix{" +
                    "representable=" + representable +
                    ", in=" + in +
                    ", outs=" + outs +
                    '}';
        }
    }
    protected ArrayList<ReplaceMatrix> replaces = new ArrayList<>();


    public ArrayList<ReplaceMatrix> getReplaces() {
        return replaces;
    }

    public void setReplaces(ArrayList<ReplaceMatrix> replaces) {
        this.replaces = replaces;
    }

    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }


    public void actionOk() {
       switch(getOperationType()) {
           case OP_SELECTION_TRANSLATION:
               getReplaces().forEach(replaceMatrix -> {
                   System.out.println("Replace points from ("+replaceMatrix.representable.getClass()+") of string {\n"+replaceMatrix.representable.toString()+"+\n}");
                   if(replaceMatrix.representable instanceof SurfaceParametriquePolynomialeBezier) {
                       SurfaceParametriquePolynomialeBezier surface = (SurfaceParametriquePolynomialeBezier) replaceMatrix.representable;
                       for(int i=0; i<surface.getCoefficients().getData2d().size(); i++)
                           for(int j=0; j<surface.getCoefficients().getData2d().get(i).size(); j++)
                               surface.getCoefficients().setElem(replaceMatrix.outs.getElem(i, j), i, j);
                   } else if(replaceMatrix.representable instanceof CourbeParametriquePolynomiale) {
                       CourbeParametriquePolynomiale surface = (CourbeParametriquePolynomiale) replaceMatrix.representable;
                       for(int i=0; i<surface.getCoefficients().getData1d().size(); i++)
                           surface.getCoefficients().setElem(replaceMatrix.outs.getElem(i));
                   } else {

                   }
               });
               break;
           case OP_NEW_ROW_COL:
               break;
           case OP_TRANSLATE_SURFACE:
               break;
       }
   }

    private Main getMain() {
        return main;
    }

    private int getOperationType() {
        return opType;
    }

    private void setOperationType(int opType) {
        this.opType = opType;
    }


    public int getMeshType() {
        return meshType;
    }

    public void setMeshType(int meshType) {
        this.meshType = meshType;
    }

    public boolean isActivateMarkers() {
        return activateMarkers;
    }

    public void setActivateMarkers(boolean activateMarkers) {
        this.activateMarkers = activateMarkers;
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }

    public boolean isTranslation() {
        return translation;
    }

    public void setTranslation(boolean translation) {
        this.translation = translation;
    }

    public boolean isNewPoint() {
        return newPoint;
    }

    public void setNewPoint(boolean newPoint) {
        this.newPoint = newPoint;
    }

    public boolean isNewRow() {
        return newRow;
    }

    public void setNewRow(boolean newRow) {
        this.newRow = newRow;
    }
    public boolean isNewCol() {
        return newCol;
    }

    public void setNewCol(boolean newCol) {
        this.newCol = newCol;
    }

    public double getTranslateXonS() {
        return translateXonS;
    }

    public void setTranslateXonS(double translateXonS) {
        this.translateXonS = translateXonS;
    }

    public double getTranslateYonS() {
        return translateYonS;
    }

    public void setTranslateYonS(double translateYonS) {
        this.translateYonS = translateYonS;
    }

    public double getTranslateZonS() {
        return translateZonS;
    }

    public void setTranslateZonS(double translateZonS) {
        this.translateZonS = translateZonS;
    }

    public boolean isTranslateOnSuv() {
        return translateOnSuv;
    }

    public void setTranslateOnSuv(boolean translateOnSuv) {
        this.translateOnSuv = translateOnSuv;
    }

    public double getTranslateOnSu() {
        return translateOnSu;
    }

    public void setTranslateOnSu(double translateOnSu) {
        this.translateOnSu = translateOnSu;
    }

    public double getTranslateOnSv() {
        return translateOnSv;
    }

    public void setTranslateOnSv(double translateOnSv) {
        this.translateOnSv = translateOnSv;
    }

    public void setInSelection(ArrayList<Point3D> inSelection) {
        this.inSelection = inSelection;
    }

    public ArrayList<Point3D> getInSelection() {
        return inSelection;
    }

    public HashMap<PMove,Point3D> getInSelectionMoves() {
        return inSelectionMoves;
    }

    public void setInSelectionMoves(HashMap<PMove, Point3D> inSelectionMoves) {
        this.inSelectionMoves = inSelectionMoves;
    }

    public void setOpType(int i) {
        this.opType = i;
    }
}
