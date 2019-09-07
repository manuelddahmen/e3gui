package one.empty3.gui;

import one.empty3.library.*;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by manue on 15-07-19.
 */
public class RPropertyDetailsRow implements TableModel {
    java.util.List<Object> objectList= new ArrayList<>();

    public List<ObjectDetailDescription> objectDetailDescriptions = new ArrayList<>();
    public static final int ARRAYTYPE_SINGLE = 0;
    public static final int ARRAYTYPE_1D = 1;
    public static final int ARRAYTYPE_2D = 2;
    public static final int TYPE_REPRESENTABLE = 0;
    public static final int TYPE_DOUBLE = 1;
    public static final int TYPE_TEXTURE = 2;
    private Representable representable;
    String[] columnNames = {"Formal Name", "Description" , "Dim", "Indices", "ObjectType", "Data"};
    Class<?>[] columnClass = {String.class, String.class, String.class, String.class, String.class, Object.class};
    private TableModelListener listener;

    public RPropertyDetailsRow(Representable representable) {
        this.representable = representable;
        representable.declareProperties();
        emptyTable();
        initTable();
    }
    public RPropertyDetailsRow(RPropertyDetailsRow rPropertyDetailsRow)
    {
        this(rPropertyDetailsRow.getRepresentable());
    }

    private void emptyTable() {
        index = 0;
        objectList.clear();
        objectDetailDescriptions.clear();
    }

    public void refresh()
    {
        emptyTable();
        initTable();
    }


    int index ;


    protected String[] split(String entryKey) { return entryKey.split("/"); }

    public void initTable() {

        if (representable.getDeclaredLists().size() > 0)
            representable.getDeclaredLists().entrySet().forEach(new Consumer<Map.Entry<String, List>>() {
                @Override
                public void accept(Map.Entry<String, List> stringListEntry) {

                    int i = 0;
                    String name = stringListEntry.getKey();
                    for (Object o : stringListEntry.getValue()) {
                        if (o instanceof Representable) {
                            String[] split = split(name);
                            Representable value = (Representable) o;
                            objectDetailDescriptions.add(new ObjectDetailDescription(split[0], split[1], 1, "" + i, value.getClass(), value.toString()));
                            objectList.add(value);
                            index++;
                            i++;
                        } else if (o instanceof Integer || o instanceof Double) {

                            String[] split = split(name);
                            Object value = o;
                            objectDetailDescriptions.add(new ObjectDetailDescription(split[0], split[1], 1, "" + i, value.getClass(), value.toString()));
                            objectList.add(value);
                            index++;
                            i++;

                        }

                    }
                    objectDetailDescriptions.add(new ObjectDetailDescription(stringListEntry.getKey().split("/")[0], "ADD NEW", 1, "" + stringListEntry.getValue().size(), Object.class,
                            Object.class.getName()));
                    objectList.add(null);
                }
            });
    
        if (representable.getDeclaredRepresentables().size() > 0)
            representable.getDeclaredRepresentables().entrySet().forEach(new Consumer<Map.Entry<String, Representable>>() {
                @Override
                public void accept(Map.Entry<String, Representable> stringRepresentableEntry) {
                    Representable value = stringRepresentableEntry.getValue();
                    String[] split = split(stringRepresentableEntry.getKey());
                    objectDetailDescriptions.add(new ObjectDetailDescription(
                            split[0], split[1], 0, "0", value.getClass(), value.toString()));
                    objectList.add(value);
                    index++;

                }
            });
        if (representable.getDeclaredPoints().size() > 0)
            representable.getDeclaredPoints().entrySet().forEach(new Consumer<Map.Entry<String, Point3D>>() {
                @Override
                public void accept(Map.Entry<String, Point3D> stringPoint3DEntry) {
                    Point3D value = stringPoint3DEntry.getValue();
                    String[] split = split(stringPoint3DEntry.getKey());
                    objectDetailDescriptions.add(new ObjectDetailDescription(
                            split[0], split[1], 0, "0", value.getClass(), value.toString()));
                    objectList.add(value);
                    index++;

                }
            });
        if (representable.getDeclaredTextures().size() > 0)
            representable.getDeclaredTextures().entrySet().forEach(new Consumer<Map.Entry<String, ITexture>>() {
                @Override
                public void accept(Map.Entry<String, ITexture> entry) {

                    ITexture value = entry.getValue();
                    if(value!=null) {
                        String[] split = split(entry.getKey());
                        objectDetailDescriptions.add(new ObjectDetailDescription(
                                split[0],
                                split[1],
                                0,
                                "0",
                                value.getClass(),
                                value.toString()));
                        objectList.add(value);
                        index++;
                    }

                }
            });
        if (representable.getDeclaredArray1Points().size() > 0)
            representable.getDeclaredArray1Points().forEach(new BiConsumer<String, Point3D[]>() {
                @Override
                public void accept(String s, Point3D[] point3DS) {
                    int i = 0;
                    for (Point3D p : point3DS) {
                        if(point3DS!=null) {
                            String[] split = split(s);
                            Point3D value = p;
                            objectDetailDescriptions.add(new ObjectDetailDescription(split[0], split[1], 1, "" + i, value.getClass(), value.toString()));
                            objectList.add(value);
                            index++;
                            i++;
                        }
                    }
                    objectDetailDescriptions.add(new ObjectDetailDescription(split(s)[0], "ADD NEW", 1, "" + point3DS.length, Point3D.class,
                            Point3D.class.toString()));
                    objectList.add(null);
    
                }
            });
        if (representable.getDeclaredArray2Points().size() > 0)
            representable.getDeclaredArray2Points().forEach(new BiConsumer<String, Point3D[][]>() {
                @Override
                public void accept(String s, Point3D[][] point3DS) {

                    int i = 0;
                    for (Point3D[] ps : point3DS) {
                        int j = 0;
                        for (Point3D p : ps) {
                            Representable value = p;
                            objectDetailDescriptions.add(new ObjectDetailDescription(
                                    split(s)[0], split(s)[1], 1, ""+i+","+j, value.getClass(), value.toString()));
                            objectList.add(value);
                            index++;


                            j++;
    
    
                        }
                        i++;
                    }
                    objectDetailDescriptions.add(new ObjectDetailDescription(split(s)[0], "INSERT NEW ROW/COLUMN", 1, "", Point3D.class,
                            Point3D.class.toString()));
                    objectList.add(null);
    
                }
            });
        if (representable.getDeclaredDoubles().size() > 0)
            representable.getDeclaredDoubles().forEach(new BiConsumer<String, Double>() {
                @Override
                public void accept(String s, Double aDouble) {
                    Double value = aDouble;
                    objectDetailDescriptions.add(new ObjectDetailDescription(
                            split(s)[0], split(s)[1], 0, "0", value.getClass(), value)
                    );
                    objectList.add(value);
                    index++;

                }
            });
        if (representable.getDeclaredArray1dDouble().size() > 0)
            representable.getDeclaredArray1dDouble().forEach(new BiConsumer<String, Double[]>() {
                @Override
                public void accept(String s, Double[] doubles) {
                    int i = 0;
                    for (double d : doubles) {

                        Object value = d;
                        objectDetailDescriptions.add(new ObjectDetailDescription(
                                split(s)[0],split(s)[1], 1, ""+i, value.getClass(), value));
                        objectList.add(null);
                        index++;
                        i++;
                    }
                    objectDetailDescriptions.add(new ObjectDetailDescription(
                            split(s)[0], split(s)[1], 1, "" + i,Double.class, 0.0));
                    objectList.add(null);
    
                }
            });
        if (representable.getDeclaredArrays2dDouble().size() > 0)

            representable.getDeclaredArrays2dDouble().forEach(new BiConsumer<String, Double[][]>() {
                @Override
                public void accept(String s, Double[][] doubles) {
                    int i = 0;
                    for (Double[] ds : doubles) {
                        {
                            int j = 0;
                            for (Double p : ds) {
                                Double value = p;
                                objectDetailDescriptions.add(new ObjectDetailDescription(
                                        split(s)[0],split(s)[1], 1, ""+i+","+j, value.getClass(), value));
                                objectList.add(value);

                                index++;


                                j++;
                            }
                            i++;
                        }
                        objectDetailDescriptions.add(new ObjectDetailDescription(
                                split(s)[0], split(s)[1], 1, "INSERT ROW/COLUMN" + i + "", Double.class, 0.0));
                        objectList.add(null);
    
    
                    }
                }
            });
        if (representable.getDeclaredString().size() > 0)

            representable.getDeclaredString().forEach(new BiConsumer<String, String>() {
                @Override
                public void accept(String s, String value) {
                    int i = 0;
                                objectDetailDescriptions.add(new ObjectDetailDescription(
                                        split(s)[0],split(s)[1], 1, ""+i, value.getClass(), value));
                                objectList.add(value);

                                index++;


                            i++;

                }
            });
        if (representable.getDeclaredInteger().size() > 0)
        
            representable.getDeclaredInteger().forEach(new BiConsumer<String, Integer>() {
                @Override
                public void accept(String s, Integer value) {
                    int i = 0;
                    objectDetailDescriptions.add(new ObjectDetailDescription(
                            split(s)[0], split(s)[1], 1, "" + i, value.getClass(), value));
                    objectList.add(value);
                    index++;
                
                
                    i++;
                
                }
            });
    
        if(representable instanceof Scene || representable instanceof RepresentableConteneur) {
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
                        objectDescription.getName(), "NEW", 1, "", value.getClass(),null));
                objectList.add(value);
                index++;


            });

        }
        
        assert objectDetailDescriptions.size()==index&&objectList.size()==index;
    }

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
        return columnIndex==5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex<objectDetailDescriptions.size()&&rowIndex>=0)
            return objectDetailDescriptions.get(rowIndex).get(columnIndex);
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(rowIndex<objectDetailDescriptions.size()&&rowIndex>=0) {
            try {
                Class propertyType = representable.getPropertyType(objectDetailDescriptions.get(rowIndex).getName());
                String propertyName = objectDetailDescriptions.get(rowIndex).getName();
                if((propertyType.equals(Double.class) || propertyType.equals(Integer.class) ||propertyType.equals(String.class))&& aValue.getClass().equals(String.class))
                {
                    if(propertyType.equals(Double.class))
                        aValue = Double.parseDouble((String)aValue);
                    if(propertyType.equals(Integer.class))
                        aValue = Integer.parseInt((String)aValue);
                    System.out.println("Property type : " + propertyType.getName()+ " Property name "+aValue);
                    representable.setProperty(objectDetailDescriptions.get(rowIndex).getName(),
                            aValue);
                    refresh();
                    objectDetailDescriptions.get(rowIndex).set(columnIndex, aValue);
                    System.out.print("save");
                    listener.tableChanged(new TableModelEvent(this, rowIndex, rowIndex, columnIndex));
                }

                int dim = objectDetailDescriptions.get(rowIndex).getDim();
                int rowArray, columnArray;
                String indices = objectDetailDescriptions.get(rowIndex).getIndexes();
                Object property = representable.getProperty(propertyName);
                if(dim>0&&dim==1 && propertyType.equals(Double[].class) ) {
                    aValue = Double.parseDouble((String)aValue);
                        rowArray = Integer.parseInt(indices);
                        ((Double[])property)[rowArray] = Double.parseDouble((String)aValue);

                    }
                else if(dim>0&&dim<=2 && propertyType.equals(Double[][].class) )
                {
                    aValue = Double.parseDouble((String)aValue);
                    if(dim==2) {
                        String[] split = indices.split(",");
                        rowArray = Integer.parseInt(split[0]);
                        columnArray = Integer.parseInt(split[1]);

                        if(property.getClass().equals(Double[][].class))
                        {
                            ((Double[][])property)[rowArray][columnArray] = Double.parseDouble((String)aValue);
                        }

                    }
                    else {
                        rowArray = Integer.parseInt(indices);
                        if(property.getClass().equals(Double[].class))
                        {
                            ((Double[])property)[rowArray] = Double.parseDouble((String)aValue);
                        }
                    }
                    System.out.println("Property type : " + propertyType.getName()+ " Property name "+objectDetailDescriptions.get(rowIndex).getName());
                    representable.setProperty(objectDetailDescriptions.get(rowIndex).getName(),
                            aValue);
                    refresh();
                    objectDetailDescriptions.get(rowIndex).set(columnIndex, aValue);
                    System.out.print("save");

                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        else
        {

        }
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

    public Representable getRepresentable() {
        return representable;
    }

    public List<Object> getObjectList() {
        return objectList;
    }

    public List<ObjectDetailDescription> getObjectDetailDescriptions() {
        return objectDetailDescriptions;
    }
}
