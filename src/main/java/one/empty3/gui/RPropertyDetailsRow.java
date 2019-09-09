package one.empty3.gui;

import one.empty3.library.*;

import javax.lang.model.type.TypeVariable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Struct;
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
        if (representable.getDeclaredDataStructure().size() > 0)
            representable.getDeclaredDataStructure().forEach(new BiConsumer<String, StructureMatrix>() {
                @Override
                public void accept(String s, StructureMatrix structureMatrix) {
                    String name = s;
                    StructureMatrix data = structureMatrix;

                    int i = 0;
                    String[] split = split(name);
                    if(data.getDim()==0)
                    {
                        objectDetailDescriptions.add(new ObjectDetailDescription(split[0], split[1], data.getDim(), "0", data.getData0d().getClass(), data.getData0d()));
                        objectList.add(data.getData0d());
                        index++;
                        i++;
                    }
                    if(data.getDim()==1)
                    {
                        for(int index=0; index<data.getData1d().size(); index++) {
                            objectDetailDescriptions.add(new ObjectDetailDescription(split[0], split[1],data.getDim(), "" + index, data.getData1d().getClass(), data.getData1d()));
                            objectList.add(data.getData0d());
                            index++;
                            i++;
                        }
                    }
                    if(data.getDim()==2)
                    {
                        for(int ind=0; ind<data.getData2d().size(); ind++) {
                            for(int ind1=0; ind1<((List)((List)data.getData2d()).get(ind)).size(); ind1++) {
                                    objectDetailDescriptions.add(new ObjectDetailDescription(split[0], split[1], data.getDim(), "" +
                                            ind + "," + ind1,
                                            data.getElem(ind, ind1).getClass(), data.getElem(ind, ind1)));

                                objectList.add(data.getElem(ind, ind1));
                                index++;
                                i++;
                            }
                        }

                    }

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
                ObjectDetailDescription descr = objectDetailDescriptions.get(rowIndex);
                Class propertyType = representable.getPropertyType(descr.getName());
                String propertyName = descr.getName();
                if((propertyType.equals(Double.class) || propertyType.equals(Integer.class) ||propertyType.equals(String.class))&& aValue.getClass().equals(String.class))
                {
                    if(propertyType.equals(Double.class))
                        aValue = Double.parseDouble((String)aValue);
                    if(propertyType.equals(Integer.class))
                        aValue = Integer.parseInt((String)aValue);
                    System.out.println("Property type : " + propertyType.getName()+ " Property name "+aValue);
                    representable.setProperty(descr.getName(),
                            aValue);
                    refresh();
                    descr.set(columnIndex, aValue);
                    System.out.print("save");
                    listener.tableChanged(new TableModelEvent(this, rowIndex, rowIndex, columnIndex));
                }

                int dim = descr.getDim();
                int rowArray, columnArray;
                String indices = descr.getIndexes();
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
                    System.out.println("Property type : " + propertyType.getName()+ " Property name "+ descr.getName());
                    representable.setProperty(descr.getName(),
                            aValue);
                    refresh();
                    descr.set(columnIndex, aValue);
                    System.out.print("save");

                } else if(propertyType.equals(StructureMatrix.class)) {
                    StructureMatrix property1 = (StructureMatrix) property;
                        switch(property1.getDim())
                        {
                            case 0:
                                property1.setElem(aValue);
                                break;
                            case 1:
                                property1.setElem(aValue, Integer.parseInt(descr.getIndexes()));
                                break;
                            case 2:
                                String indexes = descr.getIndexes();
                                String [] split = indexes.split(",");
                                property1.setElem(aValue, Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                                break;
                        }
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
