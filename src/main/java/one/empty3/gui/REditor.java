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

/*
 * Created by JFormDesigner on Mon Jul 15 09:34:29 CEST 2019
 */

package one.empty3.gui;

import java.lang.annotation.Repeatable;
import java.util.*;

import javafx.scene.control.ColorPicker;
import net.miginfocom.swing.MigLayout;
import one.empty3.library.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Manuel Dahmen
 */
public class REditor extends JPanel implements PropertyChangeListener, RepresentableEditor {
    private static final int INSERT_ROW_AT_DIM1 = 0;
    private static final int INSERT_ROW_AFTER_DIM1 = 1;
    private static final int INSERT_ROW_AT_DIM2 = 2;
    private static final int INSERT_ROW_AFTER_DIM2 = 3;
    private static final int INSERT_COL_AT = 4;
    private static final int INSERT_COL_AFTER = 5;
    private static final int DELETE_AT = 6;
    private static final int DELETE_COL = 7;

    private MatrixPropertiesObject r;
    History history = new History();

    private RPropertyDetailsRow tableModel;
    private Main main;


    public REditor(Main main, Representable re) {
        super();
        this.main = main;
        initComponents();
        init(re);
        history.addToHistory(tableModel);
        init(history.getCurrentRow());
        this.r = re;
    }

    public void init(Object re) {
        if(re!=null) {
            if (re instanceof Representable) {
                labelBreadCumbs.setText(re.getClass().getSimpleName());
                this.tableModel = new RPropertyDetailsRow((Representable) re);
                tableObjectDetails.setModel(tableModel);
                firePropertyChange("representable", r, tableModel.getRepresentable());
                this.r  = tableModel.getRepresentable();

            } else if (re instanceof RPropertyDetailsRow) {
                labelBreadCumbs.setText(((RPropertyDetailsRow) re).getRepresentable().getClass().getSimpleName());
                RPropertyDetailsRow model = new RPropertyDetailsRow((RPropertyDetailsRow) re);
                this.tableModel = model;
                tableObjectDetails.setModel(model);
                firePropertyChange("representable", r, tableModel.getRepresentable());
                this.r  = tableModel.getRepresentable();
            }
        }
        tableObjectDetails.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                try {
                    getMain().getDataModel().save(null);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        tableObjectDetails.repaint();
    }

    private void buttonRefreshActionPerformed(ActionEvent e) {
        tableModel.initTable();
    }

    private void scrollPane2MouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void tableObjectDetailsMouseClicked(MouseEvent e)  {
        int selectedRow = tableObjectDetails.getSelectedRow();
        ObjectDetailDescription objectDetailDescription = tableModel.getObjectDetailDescriptions().get(selectedRow);
        if(e.getButton()==1) {
            if (tableModel.getItemList(selectedRow) != null) {
                if (tableModel.getItemList(selectedRow) instanceof MatrixPropertiesObject) {
                    boolean isNew = tableModel.getValueAt(selectedRow, 1) != null && tableModel.getValueAt(selectedRow, 1).toString().equals("NEW");
                    MatrixPropertiesObject newR = (MatrixPropertiesObject) tableModel.getItemList(selectedRow);
                    MatrixPropertiesObject oldR = r;
                    if (isNew) {
                        if (oldR instanceof Scene) {
                            ((Scene) oldR).add((Representable)newR);
                            System.out.print("Added to scene" + newR.toString());
                        }
                        if (oldR instanceof RepresentableConteneur) {
                            ((RepresentableConteneur) oldR).add((Representable)newR);
                            System.out.print("Added to scene" + newR.toString());
                        }

                    }
                        history.addToHistory(new RPropertyDetailsRow(newR));
                        System.out.println("add to history " + history.getCurrent());
                        init(newR);
                        refreshTable();

                } else if(tableModel.getItemList(selectedRow) instanceof Color) {
                    Color o = (Color) tableModel.getItemList(selectedRow);
                    JColorChooser jColorChooser = new JColorChooser(o);
                    jColorChooser.setVisible(true);
                    o = jColorChooser.getColor();
                    tableModel.setValueAt(o, selectedRow, 5);
                }
            }
        }
        else if(e.getButton()==3) {
                 // PopUp

        }
    }


    private void objectType(Class<?> aClass) {

    }

    public void historyBack() {
        history.back();
        refreshTable();

    }

    public void historyNext() {
        history.next();
        refreshTable();

    }

    public void refreshTable() {
        init(history.getCurrentRow());
    }

    private void buttonNextActionPerformed(ActionEvent e) {
        historyNext();
    }

    private void buttonBackActionPerformed(ActionEvent e) {
        historyBack();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof ITexture) {
            try {

                ((Representable)tableModel.getRepresentable()).setProperty(evt.getPropertyName(), evt.getNewValue());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

    }

    private void okButtonActionPerformed(ActionEvent e) {
        firePropertyChange("newObject", r, r);
    }

    private void button1ActionPerformed(ActionEvent e) {

        history.clear();
        init(getMain().getDataModel().getScene());
    }

    private void button2ActionPerformed(ActionEvent e) {
        init(history.get(1));
    }

    private void button3ActionPerformed(ActionEvent e) {
        init(history.get(2));

    }

    private void button4ActionPerformed(ActionEvent e) {
        init(history.get(3));

    }

    private void button6ActionPerformed(ActionEvent e) {
        init(history.get(4));

    }

    private void button7ActionPerformed(ActionEvent e) {
        init(history.get(5));

    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }


    private StructureMatrix getProperty()
    {
        ObjectDetailDescription description = tableModel.getObjectDetailDescriptions().get(tableObjectDetails.getSelectedRow());
        Logger.getAnonymousLogger().info("+++"+ description);
        try {
            Object property = ((Representable)getRepresentable()).getProperty(description.getName());
            if(property instanceof StructureMatrix)
            {
                return (StructureMatrix)property;
            }
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Insert
    private void menuItemInsertActionPerformed(ActionEvent e) {
        StructureMatrix property = getProperty();
        ObjectDetailDescription description = tableModel.getObjectDetailDescriptions().get(tableObjectDetails.getSelectedRow());
        if(property!=null)
        {
            if(property.getDim()==1)
            {
                int parseInt = Integer.parseInt(description.getIndexes());
                try {
                    property.insert(parseInt, description.getClazz().newInstance());
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
            if(property.getDim()==2)
            {
                int pos = Integer.parseInt(description.getIndexes().split(",")[0]);
                property.addRow();
                for(int i=0; i<((List)property.getData2d().get(0)).size(); i++)
                    try {
                        property.insert(pos, 0, description.getClazz().newInstance());
                    } catch (InstantiationException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }

            }
        }
        tableModel.refresh();
    }

    // Delete
    private void menuItemDeleteRowActionPerformed(ActionEvent e) {
        StructureMatrix property = getProperty();
        ObjectDetailDescription description = tableModel.getObjectDetailDescriptions().get(tableObjectDetails.getSelectedRow());
        if(property!=null)
        {
            if(property.getDim()==1)
            {
                int parseInt = Integer.parseInt(description.getIndexes());
                property.delete(parseInt);
            }
            if(property.getDim()==2)
            {
                int pos = Integer.parseInt(description.getIndexes().split(",")[0]);
                        property.delete(pos, 0);

            }
        }
        tableModel.refresh();
    }

    // Delete
    private void menuItemDeleteColActionPerformed(ActionEvent e) {
        StructureMatrix property = getProperty();
        ObjectDetailDescription description = tableModel.getObjectDetailDescriptions().get(tableObjectDetails.getSelectedRow());
        if(property!=null)
        {
            if(property.getDim()==1)
            {
                int parseInt = Integer.parseInt(description.getIndexes());
                property.delete(parseInt);
            }
            if(property.getDim()==2)
            {
                int pos = Integer.parseInt(description.getIndexes().split(",")[0]);
                property.delete(pos, 1);
            }
        }
        tableModel.refresh();
    }

    // Refresh
    private void menuItem1ActionPerformed(ActionEvent e) {
        refreshTable();
    }

    private void buttonPrevActionPerformed(ActionEvent e) {
        historyBack();
    }

    private void menuItemInsRowActionPerformed(ActionEvent e) {
        StructureMatrix property = getProperty();
        ObjectDetailDescription description = tableModel.getObjectDetailDescriptions().get(tableObjectDetails.getSelectedRow());
        if(property!=null)
        {
            if(property.getDim()==1)
            {
                int parseInt = Integer.parseInt(description.getIndexes());
                try {
                    property.insert(parseInt, description.getClazz().newInstance());
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
            if(property.getDim()==2)
            {
                int pos = Integer.parseInt(description.getIndexes().split(",")[0]);
                property.addRow();
                for(int i=0; i<((List)property.getData2d().get(0)).size(); i++)
                    try {
                        property.insert(pos, 0, description.getClazz().newInstance());
                    } catch (InstantiationException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }

            }
        }
        tableModel.refresh();
        }


    // Insert Col at
    private void menuItemInsColActionPerformed(ActionEvent e) {
        StructureMatrix property = getProperty();
        ObjectDetailDescription description = tableModel.getObjectDetailDescriptions().get(tableObjectDetails.getSelectedRow());
        if(property!=null)
        {
            if(property.getDim()==1)
            {
                int parseInt = Integer.parseInt(description.getIndexes());
                try {
                    property.insert(parseInt, description.getClazz().newInstance());
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
            if(property.getDim()==2)
            {
                int pos = Integer.parseInt(description.getIndexes().split(",")[0]);
                for(int i=0; i<((List)property.getData2d().get(0)).size(); i++)
                    try {
                        property.insert(pos, 1, description.getClazz().newInstance());
                    } catch (InstantiationException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }

            }
        }
        tableModel.refresh();
    }

    private void menuItemRefreshActionPerformed(ActionEvent e) {
        refreshTable();
    }

    //Insert Row after
    private void menuItemRowAfterActionPerformed(ActionEvent e) {
        StructureMatrix property = getProperty();
        ObjectDetailDescription description = tableModel.getObjectDetailDescriptions().get(tableObjectDetails.getSelectedRow());
        if(property!=null)
        {
            if(property.getDim()==1)
            {
                int parseInt = Integer.parseInt(description.getIndexes());
                try {
                    property.insert(parseInt+1, description.getClazz().newInstance());
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
            if(property.getDim()==2)
            {
                int pos = Integer.parseInt(description.getIndexes().split(",")[0]);
                property.addRow();
                for(int i=0; i<((List)property.getData2d().get(0)).size(); i++)
                    try {
                        property.insert(pos+1, 0, description.getClazz().newInstance());
                    } catch (InstantiationException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }

            }
        }
        tableModel.refresh();
    }
    // Insert col after
    private void menuItemColAfterActionPerformed(ActionEvent e) {
        StructureMatrix property = getProperty();
        ObjectDetailDescription description = tableModel.getObjectDetailDescriptions().get(tableObjectDetails.getSelectedRow());
        if(property!=null)
        {
            if(property.getDim()==1)
            {
                int parseInt = Integer.parseInt(description.getIndexes());
                try {
                    property.insert(parseInt+1, description.getClazz().newInstance());
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
            if(property.getDim()==2)
            {
                int pos = Integer.parseInt(description.getIndexes().split(",")[0]);
                for(int i=0; i<((List)property.getData2d().get(0)).size(); i++)
                    try {
                        property.insert(pos+1, 1, description.getClazz().newInstance());
                    } catch (InstantiationException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }

            }
        }
        tableModel.refresh();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.gui.gui");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        toolBar1 = new JToolBar();
        buttonPrev = new JButton();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        button4 = new JButton();
        button6 = new JButton();
        button7 = new JButton();
        buttonNext = new JButton();
        labelBreadCumbs = new JLabel();
        scrollPane2 = new JScrollPane();
        tableObjectDetails = new JTablePopUp();
        popupMenu1 = new JPopupMenu();
        menuItem1 = new JMenuItem();
        menuItemRowAt = new JMenuItem();
        menuItemColAt = new JMenuItem();
        menuItemRowAfter = new JMenuItem();
        menuItemColAfter = new JMenuItem();
        menuItemDelete = new JMenuItem();
        menuItem2 = new JMenuItem();

        //======== this ========
        setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "fill,insets dialog,hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));

                //======== scrollPane1 ========
                {

                    //======== toolBar1 ========
                    {

                        //---- buttonPrev ----
                        buttonPrev.setText("<<");
                        buttonPrev.addActionListener(e -> buttonBackActionPerformed(e));
                        toolBar1.add(buttonPrev);

                        //---- button1 ----
                        button1.setText("1");
                        button1.addActionListener(e -> button1ActionPerformed(e));
                        toolBar1.add(button1);

                        //---- button2 ----
                        button2.setText("2");
                        button2.addActionListener(e -> button2ActionPerformed(e));
                        toolBar1.add(button2);

                        //---- button3 ----
                        button3.setText("3");
                        button3.addActionListener(e -> button3ActionPerformed(e));
                        toolBar1.add(button3);

                        //---- button4 ----
                        button4.setText("4");
                        button4.addActionListener(e -> button4ActionPerformed(e));
                        toolBar1.add(button4);

                        //---- button6 ----
                        button6.setText("5");
                        button6.addActionListener(e -> button6ActionPerformed(e));
                        toolBar1.add(button6);

                        //---- button7 ----
                        button7.addActionListener(e -> button7ActionPerformed(e));
                        toolBar1.add(button7);

                        //---- buttonNext ----
                        buttonNext.setText(">>");
                        buttonNext.addActionListener(e -> buttonNextActionPerformed(e));
                        toolBar1.add(buttonNext);
                    }
                    scrollPane1.setViewportView(toolBar1);
                }
                contentPanel.add(scrollPane1, "cell 0 0 3 2");

                //---- labelBreadCumbs ----
                labelBreadCumbs.setText("Navigation history");
                contentPanel.add(labelBreadCumbs, "cell 0 0 3 2");

                //======== scrollPane2 ========
                {
                    scrollPane2.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            scrollPane2MouseClicked(e);
                        }
                    });

                    //---- tableObjectDetails ----
                    tableObjectDetails.setModel(new DefaultTableModel(
                        new Object[][] {
                            {null, null, null, null, null},
                            {null, null, null, null, null},
                        },
                        new String[] {
                            "Detail name", "Dim", "Indices", "objectType", "object"
                        }
                    ));
                    tableObjectDetails.setColumnSelectionAllowed(true);
                    tableObjectDetails.setComponentPopupMenu(popupMenu1);
                    tableObjectDetails.setSelectionForeground(Color.red);
                    tableObjectDetails.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            tableObjectDetailsMouseClicked(e);
                        }
                    });
                    scrollPane2.setViewportView(tableObjectDetails);
                }
                contentPanel.add(scrollPane2, "cell 0 2 3 3,dock center");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        add(dialogPane, BorderLayout.CENTER);

        //======== popupMenu1 ========
        {

            //---- menuItem1 ----
            menuItem1.setText("Refresh");
            menuItem1.addActionListener(e -> menuItemRefreshActionPerformed(e));
            popupMenu1.add(menuItem1);

            //---- menuItemRowAt ----
            menuItemRowAt.setText(bundle.getString("RPropertyList.menuItemRowAt.text"));
            menuItemRowAt.addActionListener(e -> menuItemInsRowActionPerformed(e));
            popupMenu1.add(menuItemRowAt);

            //---- menuItemColAt ----
            menuItemColAt.setText(bundle.getString("RPropertyList.menuItemColAt.text"));
            menuItemColAt.addActionListener(e -> menuItemInsColActionPerformed(e));
            popupMenu1.add(menuItemColAt);

            //---- menuItemRowAfter ----
            menuItemRowAfter.setText(bundle.getString("RPropertyList.menuItemRowAfter.text"));
            menuItemRowAfter.addActionListener(e -> menuItemRowAfterActionPerformed(e));
            popupMenu1.add(menuItemRowAfter);

            //---- menuItemColAfter ----
            menuItemColAfter.setText(bundle.getString("RPropertyList.menuItemColAfter.text"));
            menuItemColAfter.addActionListener(e -> menuItemColAfterActionPerformed(e));
            popupMenu1.add(menuItemColAfter);

            //---- menuItemDelete ----
            menuItemDelete.setText("Delete Row");
            menuItemDelete.addActionListener(e -> menuItemDeleteRowActionPerformed(e));
            popupMenu1.add(menuItemDelete);

            //---- menuItem2 ----
            menuItem2.setText("Delete column");
            menuItem2.addActionListener(e -> menuItemDeleteColActionPerformed(e));
            popupMenu1.add(menuItem2);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JScrollPane scrollPane1;
    private JToolBar toolBar1;
    private JButton buttonPrev;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button6;
    private JButton button7;
    private JButton buttonNext;
    private JLabel labelBreadCumbs;
    private JScrollPane scrollPane2;
    private JTablePopUp tableObjectDetails;
    private JPopupMenu popupMenu1;
    private JMenuItem menuItem1;
    private JMenuItem menuItemRowAt;
    private JMenuItem menuItemColAt;
    private JMenuItem menuItemRowAfter;
    private JMenuItem menuItemColAfter;
    private JMenuItem menuItemDelete;
    private JMenuItem menuItem2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    @Override
    public void initValues(Representable representable) {
        init(representable);
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public MatrixPropertiesObject getRepresentable() {
        return r;
    }

    public void setRepresentable(Representable r) {
        this.r = r;
    }

    }
