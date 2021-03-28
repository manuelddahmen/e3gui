package one.empty3.gui;

public class MeshEditorBean {
    private int meshType;
    private boolean activateMarkers;
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
}
