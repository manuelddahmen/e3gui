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
import one.empty3.library.core.nurbs.ParametricCurve;
import one.empty3.library.core.nurbs.ParametricSurface;

import java.util.HashMap;

/**
 * Created by manue on 17-11-19.
 */
public class ModifyR {
    private final ZBufferImpl zBuffer;
    protected Representable representable;
    protected Matrix33 vectorsDir;
    protected LineSegment[] axes;
    protected double[] posAxe = new double[3];
    protected HashMap<Integer, Double> rotAxes = new HashMap<Integer, Double>();
    private Point3D selectedPoint
            ;
    private Double unit;
    private RepresentableConteneur representableConteneur;

    public ModifyR(ZBufferImpl impl)
    {
        this.zBuffer = impl;
    }

    public void init(Point3D selectedPoint, Representable representable)
    {
        this.selectedPoint = selectedPoint;
        this.representable = representable;
    }

    public void calculerAxes()
    {
        unit = selectedPoint.moins(zBuffer.camera().getEye()).norme();
        if(representable.getClass().isAssignableFrom(ParametricSurface.class))
                {
                    ParametricSurface ref = (ParametricSurface) representable;
                    //double u = selectedPoint.getData("u");
                    //double v = selectedPoint.getData("v");


                    //norme(ref, unit, u, v);


                } else
                if(representable.getClass().isAssignableFrom(ParametricCurve.class))
                {
                    ParametricCurve ref = (ParametricCurve) representable;
                    //double u = selectedPoint.getData("u");
                    //norme(ref, unit, u);
                }

    }

    private void norme(ParametricCurve ref, double mult, double u) {
        Point3D p0 = ref.calculerPoint3D(u);
        Point3D pX = ref.calculerTangente(u);
        Point3D pZ = ref.calculerNormale(u);
        Point3D pY = pZ.prodVect(pX).norme1();
        Point3D X = pX.moins(p0).norme1();
        Point3D Y = pY.moins(p0).norme1();
        Point3D Z = pZ.moins(p0).norme1();
        vectorsDir = new Matrix33(new Point3D[]{X, Y, Z});
        axes = new LineSegment[]
                {
                        new LineSegment(p0, pZ),
                        new LineSegment(p0, pZ),
                        new LineSegment(p0, pZ),
                };
     }

    private void norme(ParametricSurface ref, double mult, double u, double v) {
        Point3D p0 = ref.calculerPoint3D(u, v);
        Point3D pX = ref.calculerTangenteU(u, v);
        Point3D pY = ref.calculerTangenteV(u, v);
        Point3D pZ = ref.calculerNormale3D(u, v);
        Point3D X = pX.moins(p0).norme1().mult(mult);
        Point3D Y = pY.moins(p0).norme1().mult(mult);
        Point3D Z = pZ.moins(p0).norme1().mult(mult);
        vectorsDir = new Matrix33(new Point3D[]{X, Y, Z});
        axes = new LineSegment[]
                {
                        new LineSegment(p0, pZ),
                        new LineSegment(p0, pZ),
                        new LineSegment(p0, pZ),
                };
    }

    public LineSegment[] getAxes()
    {

        return axes;
    }

    public void setAxes(LineSegment[] axes) {
        this.axes = axes;
    }

    public RepresentableConteneur getRepresentableConteneur() {
        calculerAxes();
        representableConteneur = new RepresentableConteneur();
        representableConteneur.add(axes[0]);
        representableConteneur.add(axes[1]);
        representableConteneur.add(axes[2]);
        Sphere sphere = new Sphere(new Point3D(posAxe[0], posAxe[1], posAxe[2]), unit / 100);
        //sphere.addData("modifyR", 1.0);
        representableConteneur.add(sphere);
        return representableConteneur;
    }

    public void setRepresentableConteneur(RepresentableConteneur representableConteneur) {
        this.representableConteneur = representableConteneur;
    }

    private Point3D pointAxes()
    {
        return new Point3D(posAxe[0], posAxe[1], posAxe[2]);
    }
}
