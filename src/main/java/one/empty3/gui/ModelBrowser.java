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

import com.sun.org.apache.regexp.internal.RE;
import one.empty3.library.*;
import one.empty3.library.core.nurbs.ParametricCurve;
import one.empty3.library.core.nurbs.ParametricSurface;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by manue on 07-10-19.
 */
public class ModelBrowser {
    private final ZBufferImpl zimpl;

    public List<Cell> getObjects() {
        return objects;
    }

    class Cell {
        public Point3D pRot;
        StructureMatrix structureMatrix;
        Object o;
        int dim;
        int row, col;
        public Representable ref;

        public Cell(StructureMatrix structureMatrix, int dim, int row, int col, Representable ref, Object o, Point3D oRot) {
            this.structureMatrix = structureMatrix;
            this.dim = dim;
            this.row = row;
            this.col = col;
            this.o = o;
            this.pRot= oRot;
        }

        public Cell(StructureMatrix structureMatrix, int dim, int row, int col, Representable ref, Object o) {
            this.structureMatrix = structureMatrix;
            this.dim = dim;
            this.row = row;
            this.col = col;
            this.o = o;
        }

        }


    private List<Cell> objects = Collections.synchronizedList(new ArrayList<>());
    private Class classes;

    public ModelBrowser(ZBufferImpl impl, Scene scene, Class classes)
    {
        this.zimpl  = impl;
        this.classes = classes;
        for(int i = 0; i<scene.getObjets().getData1d().size(); i++)
        {
            Representable representable = scene.getObjets().getData1d().get(i);
            browser(representable, scene);
        }
    }

    private void browser(Representable representable, Representable ref) {
        if(representable!=null)
        {
            representable.declareProperties();
            representable.declarations().forEach((s, structureMatrix) -> {
                    switch (structureMatrix.getDim())
                    {
                        case 0:
                            browser(structureMatrix, structureMatrix.getDim(), -1, -1, structureMatrix.getElem(), representable);
                            break;
                        case 1:
                            int [] i = new int[ ] {0};
                            structureMatrix.data1d.forEach(new Consumer() {
                                @Override
                                public void accept(Object o) {
                                    browser(structureMatrix, structureMatrix.getDim(), i[0], -1, o, representable);
                                    i[0]++;
                                }
                            });
                            break;
                        case 2:
                            i = new int[ ] {0, 0};
                            structureMatrix.data2d.forEach(o -> {
                                for (Object o1 : ((List) o)) {
                                    browser(structureMatrix, structureMatrix.getDim(), i[0], i[1], o1, representable);
                                    i[1]++;
                                }
                                i[0]++;
                            });
                            break;

                    }
            });
        }

    }

    private void browser(StructureMatrix structureMatrix, int dim, int row, int col, Object o, Representable ref) {
        Object e = o;


        if(e instanceof Integer)
        {
            if(classes.equals(Integer.class))
            {
                objects.add(new Cell(structureMatrix, dim, row, col, ref, e));
            }
        }
        else if(e instanceof Double) {
            if(classes.equals(Double.class))
            {
                objects.add(new Cell(structureMatrix, dim, row, col, ref, e));
            }

        }
        else if(e instanceof Boolean) {
            if(classes.equals(Boolean.class))
            {
                objects.add(new Cell(structureMatrix, dim, row, col, ref, e));
            }

        }
        else if(e instanceof String) {
            if(classes.equals(String.class))
            {
                objects.add(new Cell(structureMatrix, dim, row, col, ref, e));
            }

        }
        else if(e instanceof File) {
            if(classes.equals(File.class))
            {
                objects.add(new Cell(structureMatrix, dim, row, col, ref, e));
            }
        }
        else if(Representable.class.isAssignableFrom(e.getClass())) {
            if(classes.equals(e.getClass())) {
                if(e.getClass().equals(Point3D.class) && (ref!=null) && (ref.getRotation()!=null)) {
                    objects.add(new Cell(structureMatrix, dim, row, col, ref, e, zimpl.rotate((Point3D) e, ref)));
                }
                else
                {
                    objects.add(new Cell(structureMatrix, dim, row, col, ref, e));
                }
            }
            browser((Representable) e, ref);

        }
    }

    public void makeSelection(List<Cell> path)
    {

    }

    public void updateSelection()
    {

    }
}
