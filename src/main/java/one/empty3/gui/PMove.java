package one.empty3.gui;

import one.empty3.library.Point3D;
import one.empty3.library.Representable;

public class PMove {
    private Point3D in;
    private double [] mXyz;
    private Representable representable;
    private int row, col;
    public Point3D getIn() {
        return in;
    }

    public void setIn(Point3D in) {
        this.in = in;
    }

    public double[] getmXyz() {
        return mXyz;
    }

    public void setmXyz(double[] mXyz) {
        this.mXyz = mXyz;
    }

    public Representable getRepresentable() {
        return representable;
    }

    public void setRepresentable(Representable representable) {
        this.representable = representable;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}