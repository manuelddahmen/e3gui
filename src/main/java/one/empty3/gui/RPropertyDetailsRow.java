package one.empty3.gui;

import one.empty3.library.ITexture;
import one.empty3.library.Point3D;
import one.empty3.library.Representable;

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
                        if(o instanceof Representable) {
                            String[] split = split(name);
                            Representable value = (Representable) o;
                            setValueAt(split[0], index, 0);
                            setValueAt(split[1], index, 1);
                            setValueAt(1, index, 2);
                            setValueAt(i, index, 3);
                            setValueAt(value.getClass(), index, 4);
                            setValueAt(value.toString(), index, 5);
                            objectDetailDescriptions.add(new ObjectDetailDescription(split[0], split[1], 1, "" + i, value.getClass(), value.toString()));
                            objectList.add(value);
                            index++;
                            i++;
                        }

                    }
                }
            });
        if (representable.getDeclaredRepresentables().size() > 0)
            representable.getDeclaredRepresentables().entrySet().forEach(new Consumer<Map.Entry<String, Representable>>() {
                @Override
                public void accept(Map.Entry<String, Representable> stringRepresentableEntry) {
                    Representable value = stringRepresentableEntry.getValue();
                    String[] split = split(stringRepresentableEntry.getKey());
                    setValueAt(split[0], index, 0);
                    setValueAt(split[1], index, 1);
                    setValueAt(0, index, 2);
                    setValueAt(0, index, 3);
                    setValueAt(value.getClass(), index, 4);
                    setValueAt(value.toString(), index, 5);
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
                    setValueAt(split[0], index, 0);
                    setValueAt(split[1], index, 1);
                    setValueAt(0, index, 2);
                    setValueAt(0, index, 3);
                    setValueAt(value.getClass(), index, 4);
                    setValueAt(value.toString(), index, 5);
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
                    String[] split = split(entry.getKey());
                    setValueAt(split(entry.getKey())[0], index, 0);
                    setValueAt(split(entry.getKey())[1], index, 1);
                    setValueAt(""+0, index, 2);
                    setValueAt(""+0, index, 3);
                    setValueAt(value.getClass(), index, 4);
                    setValueAt(value.toString(), index, 5);
                    objectDetailDescriptions.add(new ObjectDetailDescription(
                            split[0], split[1], 0, "0", value.getClass(), value.toString()));
                    objectList.add(value);
                    index++;


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
                            setValueAt(split[0], index, 0);
                            setValueAt(split[1], index, 1);
                            setValueAt(1, index, 2);
                            setValueAt(i, index, 3);
                            setValueAt(value.getClass(), index, 4);
                            setValueAt(value.toString(), index, 5);
                            objectDetailDescriptions.add(new ObjectDetailDescription(split[0], split[1], 1, "" + i, value.getClass(), value.toString()));
                            objectList.add(value);
                            index++;
                            i++;
                        }
                    }
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
                            setValueAt(split(s)[0], index, 0);
                            setValueAt(split(s)[1], index, 1);
                            setValueAt(1, index, 2);
                            setValueAt(i, index, 3);
                            setValueAt(value.getClass(), index, 4);
                            setValueAt(value.toString(), index, 5);
                            objectDetailDescriptions.add(new ObjectDetailDescription(
                                    split(s)[0], split(s)[1], 1, ""+i+","+j, value.getClass(), value.toString()));
                            objectList.add(value);
                            index++;


                            j++;
                        }
                        i++;
                    }

                }
            });
        if (representable.getDeclaredDoubles().size() > 0)
            representable.getDeclaredDoubles().forEach(new BiConsumer<String, Double>() {
                @Override
                public void accept(String s, Double aDouble) {
                    Double value = aDouble;
                    setValueAt(split(s)[0], index, 0);
                    setValueAt(split(s)[1], index, 1);
                    setValueAt(0, index, 2);
                    setValueAt(0, index, 3);
                    setValueAt(value.getClass(), index, 4);
                    setValueAt(value, index, 5);
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
                        setValueAt(split(s)[0], index, 0);
                        setValueAt(split(s)[1], index, 1);
                        setValueAt(1, index, 2);
                        setValueAt(i, index, 3);
                        setValueAt(value.getClass(), index, 4);
                        setValueAt(value, index, 5);
                        objectDetailDescriptions.add(new ObjectDetailDescription(
                                split(s)[0],split(s)[1], 1, ""+i, value.getClass(), value));
                        objectList.add(null);
                        index++;
                        i++;
                    }

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
                                setValueAt(split(s)[0], index, 0);
                                setValueAt(split(s)[1], index, 1);
                                setValueAt(1, index, 2);
                                setValueAt(""+i+","+j, index, 3);
                                setValueAt(value.getClass(), index, 4);
                                setValueAt(value.toString(), index, 5);
                                objectDetailDescriptions.add(new ObjectDetailDescription(
                                        split(s)[0],split(s)[1], 1, ""+i+","+j, value.getClass(), value));
                                objectList.add(value);

                                index++;


                                j++;
                            }
                            i++;
                        }


                    }
                }
            });
        if (representable.getDeclaredString().size() > 0)

            representable.getDeclaredString().forEach(new BiConsumer<String, String>() {
                @Override
                public void accept(String s, String value) {
                    int i = 0;
                                setValueAt(split(s)[0], index, 0);
                                setValueAt(split(s)[1], index, 1);
                                setValueAt(1, index, 2);
                                setValueAt(i, index, 3);
                                setValueAt(value.getClass(), index, 4);
                                setValueAt(value.toString(), index, 5);
                                objectDetailDescriptions.add(new ObjectDetailDescription(
                                        split(s)[0],split(s)[1], 1, ""+i, value.getClass(), value));
                                objectList.add(value);

                                index++;


                            i++;

                }
            });

        MyObservableList<ObjectDescription> objectDescriptions = RepresentableClassList.myList();
        objectDescriptions.forEach(new Consumer<ObjectDescription>() {
            @Override
            public void accept(ObjectDescription objectDescription) {
                //System.out.println("objet"+objectDescription.getName());
                Representable value = null;
                try {
                    value = objectDescription.getR().newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                setValueAt(objectDescription.getName(), index, 0);
                setValueAt("NEW", index, 1);
                setValueAt(1, index, 2);
                setValueAt("", index, 3);
                setValueAt(value.getClass(), index, 4);
                setValueAt(value.toString(), index, 5);
                objectDetailDescriptions.add(new ObjectDetailDescription(
                        objectDescription.getName(),"NEW", 1, "", value.getClass(), value));
                objectList.add(value);

                index++;


            }
        });
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
                if(propertyType.equals(Double.class) && aValue.getClass().equals(String.class))
                {
                    aValue = Double.parseDouble((String)aValue);
                System.out.println("Property type : " + propertyType.getName()+ " Property name "+objectDetailDescriptions.get(rowIndex).getName());
                    representable.setProperty(objectDetailDescriptions.get(rowIndex).getName(),
                            aValue);
                    objectDetailDescriptions.get(rowIndex).set(columnIndex, aValue);
                    System.out.print("save");
                    refresh();
                }
                /*
                else if(propertyType.equals(Array.class) && aValue.getClass().equals(String.class))
                {
                    aValue = Double.parseDouble((String)aValue);
                    System.out.println("Property type : " + propertyType.getName()+ " Property name "+objectDetailDescriptions.get(rowIndex).getName());
                    representable.setProperty(objectDetailDescriptions.get(rowIndex).getName(),
                            aValue);
                    objectDetailDescriptions.get(rowIndex).set(columnIndex, aValue);
                    System.out.print("save");
                    refresh();

                }
                */
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }

    public Object getItemList(int current) {
        return objectList.get(current);
    }

    public Representable getRepresentable() {
        return representable;
    }

    public void setRepresentable(Representable representable) {
        this.representable = representable;
    }
}
