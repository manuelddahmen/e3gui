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

import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.library.Scene;
import one.empty3.library.StructureMatrix;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by manue on 07-10-19.
 */
public class ModelBrowser {
    class Cell {
        StructureMatrix structureMatrix;
        int dim;
        int row, col;
    }


    private List<Cell> objects = Collections.synchronizedList(new ArrayList<>());
    private Class classes;

    public ModelBrowser(Scene scene, Class classes)
    {
        this.classes = classes;
        for(int i = 0; i<scene.getObjets().getData1d().size(); i++)
        {
            Representable representable = scene.getObjets().getData1d().get(i);
            browser(representable);
        }
    }

    private void browser(Representable representable) {
        if(representable!=null)
        {
            representable.declareProperties();
            representable.declarations().forEach((s, structureMatrix) -> {
                    switch (structureMatrix.getDim())
                    {
                        case 0:
                            browser(structureMatrix.getElem());
                            break;
                        case 1:
                            structureMatrix.data1d.forEach(new Consumer() {
                                @Override
                                public void accept(Object o) {
                                    browser(o);
                                }
                            });
                            break;
                        case 2:
                            structureMatrix.data2d.forEach(new Consumer() {
                                @Override
                                public void accept(Object o) {
                                    for (Object o1 : ((List) o)) {
                                        browser(o1);
                                    }
                                }
                            });
                            break;

                    }
            });
        }

    }

    private void browser(Object elem) {
        if(elem instanceof Integer)
        {
            if(classes.equals(Integer.class))
            {
                objects.add(new Cell());
            }
        }
        else if(elem instanceof Double) {
            if(classes.equals(Double.class))
            {
                objects.add(new Cell());
            }

        }
        else if(elem instanceof Boolean) {
            if(classes.equals(Boolean.class))
            {
                objects.add(new Cell());
            }

        }
        else if(elem instanceof String) {
            if(classes.equals(String.class))
            {
                objects.add(new Cell());
            }

        }
        else if(elem instanceof File) {
            if(classes.equals(File.class))
            {
                objects.add(new Cell());
            }
        }
        else if(elem.getClass().isAssignableFrom(Representable.class)) {
            if(classes.equals(elem.getClass())) {
                objects.add(new Cell());
                browser((Representable) elem);
            }
        }
    }
}
