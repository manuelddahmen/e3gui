package one.empty3.gui;

import one.empty3.library.HeightMapSurface;
import one.empty3.library.ITexture;
import one.empty3.library.core.nurbs.FunctionSurface;
import one.empty3.library.core.raytracer.tree.AlgebraicFormulaSyntaxException;

/**
 * Created by manue on 29-07-19.
 */
public class HeightMapSurfaceXYZ extends FunctionSurface implements HeightMapSurface{
    private ITexture heights = null;

    public HeightMapSurfaceXYZ() throws AlgebraicFormulaSyntaxException {
    }

    @Override
    public double height(double u, double v) {
        return heights.getColorAt(u,v);
    }


    public ITexture getHeights() {
        return heights;
    }

    public void setHeights(ITexture heights) {
        this.heights = heights;
    }

    @Override
    public void declareProperties() {
        super.declareProperties();
        getDeclaredTextures().put("height", heights);
    }
}
