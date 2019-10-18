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

import com.sun.org.apache.regexp.internal.RE;
import one.empty3.library.*;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by manue on 15-07-19.
 */
public class RPropertyDetailsRow implements TableModel {
    java.util.List<Object> objectList = new ArrayList<>();

    public List<ObjectDetailDescription> objectDetailDescriptions = new ArrayList<>();
    public static final int ARRAYTYPE_SINGLE = 0;
    public static final int ARRAYTYPE_1D = 1;
    public static final int ARRAYTYPE_2D = 2;
    public static final int TYPE_REPRESENTABLE = 0;
    public static final int TYPE_DOUBLE = 1;
    public static final int TYPE_TEXTURE = 2;
    private MatrixPropertiesObject representable;
    String[] columnNames = {"Formal Name", "Description", "Dim", "Indices", "ObjectType", "Data"};
    Class<?>[] columnClass = {String.class, String.class, String.class, String.class, String.class, Object.class};
    private TableModelListener listener;

    public RPropertyDetailsRow(MatrixPropertiesObject representable) {
        this.representable = representable;
        if(representable instanceof Representable) {
            ((Representable)representable).declareProperties();
        }
        emptyTable();
        initTable();
    }

    public RPropertyDetailsRow(RPropertyDetailsRow rPropertyDetailsRow) {
        this(rPropertyDetailsRow.getRepresentable());
    }


    private void emptyTable() {
        index = 0;
        objectList.clear();
        objectDetailDescriptions.clear();
    }

    public void refresh() {
        emptyTable();
        initTable();
    }


    int index;


    protected String[] split(String entryKey) {
        return entryKey.split("/");
    }

    public void initTable() {
        index = 0;

        if (((MatrixPropertiesObject) representable).declarations().size() > 0) {
            representable.declarations().forEach(new BiConsumer<String, StructureMatrix>() {
                @Override
                public void accept(String s, StructureMatrix structureMatrix) {
                    String name = s;
                    String[] propName = name.split("/");
                    StructureMatrix data = structureMatrix;

                    int i = 0;
                    String[] split = split(name);
                    if (data.getDim() == 0) {
                        objectDetailDescriptions.add(new ObjectDetailDescription(propName[0], propName[1],
                                0, "0", data.getElem().getClass(), data.getElem()));
                        objectList.add(data.getElem());
                        index++;
                    }
                    if (data.getDim() == 1) {
                        for (int ind = 0; ind < data.getData1d().size(); ind++) {
                            objectDetailDescriptions.add(new ObjectDetailDescription(propName[0], propName[1],
                                    0, "" + ind, data.getElem(ind).getClass(), data.getElem(ind)));
                            objectList.add(data.getElem(ind));
                            index++;
                        }
                    }
                    if (data.getDim() == 2) {
                        for (int ind = 0; ind < data.getData2d().size(); ind++) {
                            for (int ind1 = 0; ind1 < ((List) ((List) data.getData2d()).get(ind)).size(); ind1++) {

                                objectDetailDescriptions.add(new ObjectDetailDescription(propName[0], propName[1],
                                        0, "" + ind + "," + ind1, data.getElem(ind, ind1).getClass(), data.getElem(ind, ind1)));
                                objectList.add(data.getElem(ind, ind1));
                                index++;
                            }
                        }

                    }

                }
            });
        /*
        if (representable.getDeclaredLists().size() > 0)
            representable.getDeclaredLists().forEach(new BiConsumer<String, Object>() {
                @Override
                public void accept(String s, Object o) {
                    int ind = 0;
                    objectDetailDescriptions.add(new ObjectDetailDescription(s.split("/")[0], s.split("/")[1], 1, "" + ind++,
                            o.getClass(), o));
                }
            });
*/
            if (representable instanceof Scene || representable instanceof RepresentableConteneur) {
                MyObservableList<ObjectDescription> objectDescriptions = RepresentableClassList.myList();
                objectDescriptions.forEach(objectDescription -> {
                    Representable value = null;
                    try {
                        value = objectDescription.getR().newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    objectDetailDescriptions.add(new ObjectDetailDescription(
                            objectDescription.getName(), "NEW", 1, "" + index, value.getClass(), null));
                    objectList.add(value);
                    index++;

                });


            }
            if (!(objectDetailDescriptions.size() == index && objectList.size() == index))
                System.exit(1);
        }
    }
/*
    public void addPoint(String propertyName, String propertyDescr, Point3D p3d, int ind1, int ind2) {
        int i=0;
        objectDetailDescriptions.add(new ObjectDetailDescription(propertyName, propertyDescr, 1, ""+ind1+","+ind2+","+i, p3d.getClass(), p3d.get(i)));
        objectList.add(p3d.get(i));
        i++;
        index++;
        objectDetailDescriptions.add(new ObjectDetailDescription(propertyName, propertyDescr, 1, ""+ind1+","+ind2+","+i, p3d.getClass(), p3d.get(i)));
        objectList.add(p3d.get(i));
        i++;
        index++;
        objectDetailDescriptions.add(new ObjectDetailDescription(propertyName, propertyDescr, 1, ""+ind1+","+ind2+","+i, p3d.getClass(), p3d.get(i)));
        objectList.add(p3d.get(i));
        i++;
        index++;
    }
*/
    @Override
    public int getRowCount() {
        return objectDetailDescriptions.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < objectDetailDescriptions.size() && rowIndex >= 0)
            return objectDetailDescriptions.get(rowIndex).get(columnIndex);
        return null;
    }



    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(rowIndex<objectDetailDescriptions.size()&&rowIndex>=0) {
            if(representable instanceof StructureMatrix)
                return;
            Representable representable  = (Representable)this.representable;
            ObjectDetailDescription descr = objectDetailDescriptions.get(rowIndex);
            Object property = null;
            Class propertyClass = null;
            try {
                property = representable.getProperty(descr.getName());
                propertyClass = property.getClass();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            //System.out.println("property : " + propertyClass.toString());
            try {
                Class propertyType = null;
                try {
                    propertyType = representable.getPropertyType(descr.getName());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                String propertyName = descr.getName();




                if(propertyClass.equals(StructureMatrix.class)) {
                    StructureMatrix property1 = (StructureMatrix) property;


                    switch (property1.getDim())
                    {
                        case 0:
                            propertyClass = property1.getElem().getClass();
                            break;
                        case 1:
                            propertyClass = property1.getElem(0).getClass();
                            break;
                        case 2:
                            propertyClass = property1.getElem(0,0).getClass();
                            break;

                    }

                    System.out.println(propertyClass.toString());
                    Object avalue = "Error";
                    switch (propertyClass.toString()) {
                        case "class java.lang.Double":
                            avalue = Double.parseDouble((String) aValue);
                            break;
                        case "class java.lang.Integer":
                            avalue = Integer.parseInt((String) aValue);
                            break;
                        case "class java.lang.String":
                            avalue = aValue.toString();
                            break;
                        case "class java.lang.Boolean":
                            avalue = Boolean.parseBoolean((String)aValue);
                            break;
                        default:
                            System.exit(1);
                            break;

                    }
                    //System.out.println(avalue.getClass());
                    switch (property1.getDim()) {
                        case 0:
                            property1.setElem(avalue);
                            break;
                        case 1:
                            property1.setElem(avalue, Integer.parseInt(descr.getIndexes()));
                            break;
                        case 2:
                            String indexes = descr.getIndexes();
                            String[] split = indexes.split(",");
                            property1.setElem(avalue, Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                            break;
                    }
                } else

                if ((propertyType.equals(Double.class) || propertyType.equals(Integer.class) || propertyType.equals(String.class)) && aValue.getClass().equals(String.class)) {
                    if (propertyType.equals(Double.class))
                        aValue = Double.parseDouble((String) aValue);
                    if (propertyType.equals(Integer.class))
                        aValue = Integer.parseInt((String) aValue);
                    System.out.println("Property type : " + propertyType.getName() + " Property name " + aValue);
                    ((Representable)representable).setProperty(descr.getName(),
                            aValue);
                    refresh();
                    descr.set(columnIndex, aValue);
                    System.out.print("save");
                    listener.tableChanged(new TableModelEvent(this, rowIndex, rowIndex, columnIndex));
                }

                int dim = descr.getDim();
                String indices = descr.getIndexes();
                if (dim > 0 && dim == 1 && propertyType.equals(Double[].class)) {
                    aValue = Double.parseDouble((String) aValue);
                    int rowArray = Integer.parseInt(indices);
                    ((Double[]) property)[rowArray] = Double.parseDouble((String) aValue);

                } else if (dim > 0 && dim <= 2 && propertyType.equals(Double[][].class)) {
                    aValue = Double.parseDouble((String) aValue);
                    if (dim == 2) {
                        String[] split = indices.split(",");
                        int rowArray = Integer.parseInt(split[0]);
                        int columnArray = Integer.parseInt(split[1]);

                        if (property.getClass().equals(Double[][].class)) {
                            ((Double[][]) property)[rowArray][columnArray] = Double.parseDouble((String) aValue);
                        }

                    } else {
                        int rowArray = Integer.parseInt(indices);
                        if (property.getClass().equals(Double[].class)) {
                            ((Double[]) property)[rowArray] = Double.parseDouble((String) aValue);
                        }
                    }
                    System.out.println("Property type : " + propertyType.getName() + " Property name " + descr.getName());
                    ((Representable)representable).setProperty(descr.getName(),
                            aValue);
                    refresh();
                    descr.set(columnIndex, aValue);
                    System.out.print("save");

                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        refresh();
    }



    @Override
    public void addTableModelListener(TableModelListener l) {
        listener = l;
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listener = l;

    }
    public Object getItemList(int current) {
        return objectList.get(current);
    }

    public MatrixPropertiesObject getRepresentable() {
        return representable;
    }

    public List<Object> getObjectList() {
        return objectList;
    }

    public List<ObjectDetailDescription> getObjectDetailDescriptions() {
        return objectDetailDescriptions;
    }
}
