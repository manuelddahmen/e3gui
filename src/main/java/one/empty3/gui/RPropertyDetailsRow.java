package one.empty3.gui;

import one.empty3.library.ITexture;
import one.empty3.library.Point3D;
import one.empty3.library.Representable;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by manue on 15-07-19.
 */
public class RPropertyDetailsRow implements TableModel {
    public static final int ARRAYTYPE_SINGLE = 0;
    public static final int ARRAYTYPE_1D = 1;
    public static final int ARRAYTYPE_2D = 2;
    public static final int TYPE_REPRESENTABLE = 0;
    public static final int TYPE_DOUBLE = 1;
    public static final int TYPE_TEXTURE = 2;
    private Representable representable;
    String[] columnNames = {"Detail name", "Dim", "Indices", "ObjectType", "Data"};

    public RPropertyDetailsRow(Representable representable) {
        this.representable = representable;
        initTable();
    }

    int index = 0;

    private void initTable() {
        if (representable.getDeclaredRepresentables().size() > 0)
            representable.getDeclaredRepresentables().entrySet().forEach(new Consumer<Map.Entry<String, Representable>>() {
                @Override
                public void accept(Map.Entry<String, Representable> stringRepresentableEntry) {
                    Representable value = stringRepresentableEntry.getValue();
                    setValueAt(stringRepresentableEntry.getKey(), index, 0);
                    setValueAt(0, index, 1);
                    setValueAt(0, index, 2);
                    setValueAt(value.getClass(), index, 3);
                    setValueAt(value.toString(), index, 4);
                    index++;

                }
            });
        if (representable.getDeclaredTextures().size() > 0)
            representable.getDeclaredTextures().entrySet().forEach(new Consumer<Map.Entry<String, ITexture>>() {
                @Override
                public void accept(Map.Entry<String, ITexture> entry) {
                    ITexture value = entry.getValue();
                    setValueAt(entry.getKey(), index, 0);
                    setValueAt(0, index, 1);
                    setValueAt(0, index, 2);
                    setValueAt(value.getClass(), index, 3);
                    setValueAt(value.toString(), index, 4);
                    index++;


                }
            });
        if (representable.getDeclaredArray1Points().size() > 0)
            representable.getDeclaredArray1Points().forEach(new BiConsumer<String, Point3D[]>() {
                @Override
                public void accept(String s, Point3D[] point3DS) {
                    int i = 0;
                    for (Point3D p : point3DS) {

                        Representable value = p;
                        setValueAt(s, index, 0);
                        setValueAt(1, index, 1);
                        setValueAt(i, index, 2);
                        setValueAt(value.getClass(), index, 3);
                        setValueAt(value.toString(), index, 4);
                        index++;
                        i++;
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
                            setValueAt(s, index, 0);
                            setValueAt(1, index, 1);
                            setValueAt(i, index, 2);
                            setValueAt(value.getClass(), index, 3);
                            setValueAt(value.toString(), index, 4);
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
                    setValueAt(s, index, 0);
                    setValueAt(0, index, 1);
                    setValueAt(0, index, 2);
                    setValueAt(value.getClass(), index, 3);
                    setValueAt(value, index, 4);
                    index++;

                }
            });
        if (representable.getDeclaredArrays().size() > 0)
            representable.getDeclaredArrays().forEach(new BiConsumer<String, Double[]>() {
                @Override
                public void accept(String s, Double[] doubles) {
                    int i = 0;
                    for (double d : doubles) {

                        Object value = d;
                        setValueAt(s, index, 0);
                        setValueAt(1, index, 1);
                        setValueAt(i, index, 2);
                        setValueAt(value.getClass(), index, 3);
                        setValueAt(value, index, 4);
                        index++;
                        i++;
                    }

                }
            });
        if (representable.getDeclaredMatrix().size() > 0)

            representable.getDeclaredMatrix().forEach(new BiConsumer<String, Double[][]>() {
                @Override
                public void accept(String s, Double[][] doubles) {
                    int i = 0;
                    for (Double[] ds : doubles) {
                        {
                            int j = 0;
                            for (Double p : ds) {
                                Double value = p;
                                setValueAt(s, index, 0);
                                setValueAt(1, index, 1);
                                setValueAt(i, index, 2);
                                setValueAt(value.getClass(), index, 3);
                                setValueAt(value.toString(), index, 4);
                                index++;


                                j++;
                            }
                            i++;
                        }


                    }
                }
            });
    }

    @Override
    public int getRowCount() {
        return 0;
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
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
