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
    private StructureMatrix<ITexture> heights = new StructureMatrix<>(0);

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
