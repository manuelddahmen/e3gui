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

import java.awt.*;

/**
 * Created by manue on 17-11-19.
 */

/***
 * Translate around Object's axis. New axis. Re
 */
public class TranslateR  extends ModifyR {

    public TranslateR(ZBufferImpl impl)
    {
        super(impl);
    }
    public LineSegment[] segments()
    {
        Matrix33 elem = representable.getRotation().getElem().getRot().getElem();
        Point3D centreRot = representable.getRotation().getElem().getCentreRot().getElem();
        Point3D[] colVectors = elem.getColVectors();
        axes =  new LineSegment[] {new LineSegment(
                colVectors[0].moins(centreRot ),
                centreRot)
                ,
                new LineSegment(
                        colVectors[1].moins(centreRot ), centreRot),
                new LineSegment(
                        colVectors[2].moins(centreRot ), centreRot) };
        return axes;
    }

    public void startMove() {}
    public void endMove(Point p0) {}

    private void pointAxes(ParametricSurface s)
    {

    }
    private void pointAxes(ParametricCurve s)
    {

    }
}
