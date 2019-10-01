/*
 *
 *  *  This file is part of Empty3.
 *  *
 *  *     Empty3 is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 2 of the License, or
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

import one.empty3.library.HeightMapSurface;
import one.empty3.library.ITexture;
import one.empty3.library.StructureMatrix;
import one.empty3.library.core.nurbs.FunctionSurface;
import one.empty3.library.core.raytracer.tree.AlgebraicFormulaSyntaxException;

import java.util.ArrayList;

/**
 * Created by manue on 29-07-19.
 */
public class HeightMapSurfaceXYZ extends FunctionSurface implements HeightMapSurface{
    private StructureMatrix<ITexture> heights = new StructureMatrix(0, ITexture.class);

    public HeightMapSurfaceXYZ() throws AlgebraicFormulaSyntaxException {
    }

    @Override
    public double height(double u, double v) {
        return heights.getElem().getColorAt(u,v);
    }


    public ITexture getHeights() {
        return heights.getElem();
    }

    public void setHeights(ITexture heights) {
        this.heights.setElem(heights);
    }

    @Override
    public void declareProperties() {
        super.declareProperties();
        getDeclaredDataStructure().put("height", heights
        );
    }
}
