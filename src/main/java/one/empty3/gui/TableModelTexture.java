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

import one.empty3.library.ITexture;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by manue on 25-07-19.
 */
public class TableModelTexture implements TableModel {
    TableModelListener listener;
    private int rows;

    public ArrayList<ModelLine> getLines() {
        return lines;
    }

    /**
     * Created by manue on 25-07-19.
     */
    static class ModelLine {
        private File file;
        private ITexture iTexture;
        private Class clazz;

        public ModelLine(File file, ITexture iTexture, Class clazz) {
            this.file = file;
            this.iTexture = iTexture;
            this.clazz = clazz;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public ITexture getiTexture() {
            return iTexture;
        }

        public void setiTexture(ITexture iTexture) {
            this.iTexture = iTexture;
        }

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public void set(int columnIndex, Object aValue) {
            if(columnIndex==0) {
                if (aValue instanceof File) {
                    this.file = (File) aValue;
                } else if (aValue instanceof ITexture)
                    this.iTexture = (ITexture) aValue;
            } else if(columnIndex==1)
            {
                if(aValue instanceof Class)
                {
                    this.clazz = (Class) aValue;
                }
            }
        }

        public Object get(int columnIndex) {
            return columnIndex==0?(getFile()==null?getiTexture():getFile()):getClazz();

        }

    }
    private ArrayList<ModelLine> lines = new ArrayList<>();
    private final MyObservableList<File> listFile = new MyObservableList<>();
    private final MyObservableList<ITexture> listText = new MyObservableList<>();

    public MyObservableList<File> getListFile() {
        return listFile;
    }

    public MyObservableList<ITexture> getListText() {
        return listText;
    }

    public TableModelTexture(){
        initTable();
    }

    public void initTable()
    {
//        lines.clear();
//        index = 0;
//        listText.forEach(iTexture -> {
//            lines.add(new ModelLine(null, iTexture, ITexture.class));
//            rows++;
//            setValueAt(iTexture, index, 0);
//            setValueAt(iTexture!=null?iTexture.getClass():null, index, 1);
//            index++;
//        });
//        listFile.forEach(file -> {
//            lines.add(new ModelLine(file, null, File.class));
//            setValueAt(file, index, 0);
//            setValueAt(file!=null?file.getClass():null, index, 1);
//            rows++;
//            index++;
//        });
        System.out.println("Lines count =  " +getRowCount());
        if(lines.size()>0) {
      //      listener.tableChanged(new TableModelEvent(this, 0, lines.size(), 0, 2));
        }
    }

    @Override
    public int getRowCount() {
        return lines.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnIndex==0?"Texture":"Text type";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex==0?Object.class:Class.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return lines.get(rowIndex).get(columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        lines.get(rowIndex).set(columnIndex, aValue);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        this.listener = l;
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        this.listener = null;
    }
}
