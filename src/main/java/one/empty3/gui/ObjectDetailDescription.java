package one.empty3.gui;

/**
 * Created by manue on 15-07-19.
 */
public class ObjectDetailDescription {
    private String name;
    private String descrition;
    private int dim;
    private String indexes;
    private String clazz;
    private Object value;
    String[] columnNames = {"Formal Name", "Description", "Dim", "Indices", "ObjectType", "Data"};
    Class<?>[] columnClass = {String.class, String.class, String.class, Class.class, Object.class};

    public ObjectDetailDescription(String name, String descrition, int i, String i1, String aClass, Object s) {
        this.name = name;
        this.descrition = descrition;
        this.dim = i;
        this.indexes = i1;
        this.clazz = aClass;
        this.value = s;
    }

    public Object get(int columnIndex) {
        Object o = null;
        switch (columnIndex)
        {
            case 0:
                o = name;
                break;
            case 1:
                o =descrition;
                break;
            case 2:
                o =dim;
                break;
            case 3:
                o =indexes;
                break;
            case 4:
                o =clazz;
                break;
            case 5:
                o = value;
                break;

        }
        return o;
    }

    public void set(int columnIndex, Object aValue) {
        switch (columnIndex)
        {
            case 0:
                name = (String)aValue;
                break;
            case 1:
                descrition= (String)aValue;
                break;
            case 2:
                dim = (int)aValue;
                break;
            case 3:
                indexes = (String) aValue;
                break;
            case 4:
                clazz = (String) aValue;
                break;
            case 5:
                value = (String) aValue;
                break;

        }
    }
}
