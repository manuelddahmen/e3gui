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

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
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
    public REditor()
    {}

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
                tableObjectDetails.setDefaultRenderer(String.class, new RETableRenderer(tableModel));
            } else if (re instanceof RPropertyDetailsRow) {
                labelBreadCumbs.setText(((RPropertyDetailsRow) re).getRepresentable().getClass().getSimpleName());
                RPropertyDetailsRow model = new RPropertyDetailsRow((RPropertyDetailsRow) re);

                this.tableModel = model;
                tableObjectDetails.setModel(model);
                labelBreadCumbs.setText(((RPropertyDetailsRow) re).getRepresentable().getClass().getSimpleName());
                firePropertyChange("representable", r, tableModel.getRepresentable());
                this.r  = tableModel.getRepresentable();
            }
            tableModel.setMain(main);
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
/*
    public String getToolTipText(MouseEvent e) {
        String tip = null;
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);

        try {
            tip = getValueAt(rowIndex, colIndex).toString();
        } catch (RuntimeException e1) {
            //catch null pointer exception if mouse is over an empty line
        }

        return tip;
    }
*/
    public void historyBack() {
        history.back();
        refreshTable();

    }

    public void historyNext() {
        history.next();
        refreshTable();

    }



    public void refreshTable() {
        Object h = history.getCurrentRow();
        if(h==null)
            ;
        else
            init(h);
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

    public JPanel getDialogPane() {
        return dialogPane;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JScrollPane getScrollPane1() {
        return scrollPane1;
    }

    public JToolBar getToolBar1() {
        return toolBar1;
    }

    public JButton getButtonPrev() {
        return buttonPrev;
    }

    public JButton getButton1() {
        return button1;
    }

    public JButton getButton2() {
        return button2;
    }

    public JButton getButton3() {
        return button3;
    }

    public JButton getButton4() {
        return button4;
    }

    public JButton getButton6() {
        return button6;
    }

    public JButton getButton7() {
        return button7;
    }

    public JButton getButtonNext() {
        return buttonNext;
    }

    public JLabel getLabelBreadCumbs() {
        return labelBreadCumbs;
    }

    public JScrollPane getScrollPane2() {
        return scrollPane2;
    }

    public JTablePopUp getTableObjectDetails() {
        return tableObjectDetails;
    }

    public JPopupMenu getPopupMenu1() {
        return popupMenu1;
    }

    public JMenuItem getMenuItem1() {
        return menuItem1;
    }

    public JMenuItem getMenuItemRowAt() {
        return menuItemRowAt;
    }

    public JMenuItem getMenuItemColAt() {
        return menuItemColAt;
    }

    public JMenuItem getMenuItemRowAfter() {
        return menuItemRowAfter;
    }

    public JMenuItem getMenuItemColAfter() {
        return menuItemColAfter;
    }

    public JMenuItem getMenuItemDelete() {
        return menuItemDelete;
    }

    public JMenuItem getMenuItem2() {
        return menuItem2;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
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
        setName("this"); //NON-NLS
        setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setName("dialogPane"); //NON-NLS
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setName("contentPanel"); //NON-NLS
                contentPanel.setLayout(new MigLayout(
                    new LC().fill().insets("dialog").hideMode(3), //NON-NLS
                    // columns
                    new AC()
                        .fill().gap()
                        .fill().gap()
                        .fill(),
                    // rows
                    new AC()
                        .gap()
                        .gap()
                        .gap()
                        .gap()
                        ));

                //======== scrollPane1 ========
                {
                    scrollPane1.setName("scrollPane1"); //NON-NLS

                    //======== toolBar1 ========
                    {
                        toolBar1.setName("toolBar1"); //NON-NLS

                        //---- buttonPrev ----
                        buttonPrev.setText("<<"); //NON-NLS
                        buttonPrev.setName("buttonPrev"); //NON-NLS
                        buttonPrev.addActionListener(e -> buttonBackActionPerformed(e));
                        toolBar1.add(buttonPrev);

                        //---- button1 ----
                        button1.setText("1"); //NON-NLS
                        button1.setName("button1"); //NON-NLS
                        button1.addActionListener(e -> button1ActionPerformed(e));
                        toolBar1.add(button1);

                        //---- button2 ----
                        button2.setText("2"); //NON-NLS
                        button2.setName("button2"); //NON-NLS
                        button2.addActionListener(e -> button2ActionPerformed(e));
                        toolBar1.add(button2);

                        //---- button3 ----
                        button3.setText("3"); //NON-NLS
                        button3.setName("button3"); //NON-NLS
                        button3.addActionListener(e -> button3ActionPerformed(e));
                        toolBar1.add(button3);

                        //---- button4 ----
                        button4.setText("4"); //NON-NLS
                        button4.setName("button4"); //NON-NLS
                        button4.addActionListener(e -> button4ActionPerformed(e));
                        toolBar1.add(button4);

                        //---- button6 ----
                        button6.setText("5"); //NON-NLS
                        button6.setName("button6"); //NON-NLS
                        button6.addActionListener(e -> button6ActionPerformed(e));
                        toolBar1.add(button6);

                        //---- button7 ----
                        button7.setName("button7"); //NON-NLS
                        button7.addActionListener(e -> button7ActionPerformed(e));
                        toolBar1.add(button7);

                        //---- buttonNext ----
                        buttonNext.setText(">>"); //NON-NLS
                        buttonNext.setName("buttonNext"); //NON-NLS
                        buttonNext.addActionListener(e -> buttonNextActionPerformed(e));
                        toolBar1.add(buttonNext);
                    }
                    scrollPane1.setViewportView(toolBar1);
                }
                contentPanel.add(scrollPane1, new CC().cell(0, 0, 3, 2));

                //---- labelBreadCumbs ----
                labelBreadCumbs.setText("Navigation history"); //NON-NLS
                labelBreadCumbs.setName("labelBreadCumbs"); //NON-NLS
                contentPanel.add(labelBreadCumbs, new CC().cell(0, 0, 3, 2));

                //======== scrollPane2 ========
                {
                    scrollPane2.setName("scrollPane2"); //NON-NLS
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
                            "Detail name", "Dim", "Indices", "objectType", "object" //NON-NLS
                        }
                    ));
                    tableObjectDetails.setColumnSelectionAllowed(true);
                    tableObjectDetails.setComponentPopupMenu(popupMenu1);
                    tableObjectDetails.setSelectionForeground(Color.red);
                    tableObjectDetails.setName("tableObjectDetails"); //NON-NLS
                    tableObjectDetails.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            tableObjectDetailsMouseClicked(e);
                        }
                    });
                    scrollPane2.setViewportView(tableObjectDetails);
                }
                contentPanel.add(scrollPane2, new CC().cell(0, 2, 3, 3).push().grow());
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        add(dialogPane, BorderLayout.CENTER);

        //======== popupMenu1 ========
        {
            popupMenu1.setName("popupMenu1"); //NON-NLS

            //---- menuItem1 ----
            menuItem1.setText("Refresh"); //NON-NLS
            menuItem1.setName("menuItem1"); //NON-NLS
            menuItem1.addActionListener(e -> menuItemRefreshActionPerformed(e));
            popupMenu1.add(menuItem1);

            //---- menuItemRowAt ----
            menuItemRowAt.setText("Insert row at"); //NON-NLS
            menuItemRowAt.setName("menuItemRowAt"); //NON-NLS
            menuItemRowAt.addActionListener(e -> menuItemInsRowActionPerformed(e));
            popupMenu1.add(menuItemRowAt);

            //---- menuItemColAt ----
            menuItemColAt.setText("Insert col at"); //NON-NLS
            menuItemColAt.setName("menuItemColAt"); //NON-NLS
            menuItemColAt.addActionListener(e -> menuItemInsColActionPerformed(e));
            popupMenu1.add(menuItemColAt);

            //---- menuItemRowAfter ----
            menuItemRowAfter.setText("Insert row after"); //NON-NLS
            menuItemRowAfter.setName("menuItemRowAfter"); //NON-NLS
            menuItemRowAfter.addActionListener(e -> menuItemRowAfterActionPerformed(e));
            popupMenu1.add(menuItemRowAfter);

            //---- menuItemColAfter ----
            menuItemColAfter.setText("Insert col after"); //NON-NLS
            menuItemColAfter.setName("menuItemColAfter"); //NON-NLS
            menuItemColAfter.addActionListener(e -> menuItemColAfterActionPerformed(e));
            popupMenu1.add(menuItemColAfter);

            //---- menuItemDelete ----
            menuItemDelete.setText("Delete Row"); //NON-NLS
            menuItemDelete.setName("menuItemDelete"); //NON-NLS
            menuItemDelete.addActionListener(e -> menuItemDeleteRowActionPerformed(e));
            popupMenu1.add(menuItemDelete);

            //---- menuItem2 ----
            menuItem2.setText("Delete column"); //NON-NLS
            menuItem2.setName("menuItem2"); //NON-NLS
            menuItem2.addActionListener(e -> menuItemDeleteColActionPerformed(e));
            popupMenu1.add(menuItem2);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void initComponentsI18n() {
        // JFormDesigner - Component i18n initialization - DO NOT MODIFY  //GEN-BEGIN:initI18n
        // JFormDesigner - End of component i18n initialization  //GEN-END:initI18n
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
