/*
 * Created by JFormDesigner on Mon Jul 15 09:34:29 CEST 2019
 */

package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.ITexture;
import one.empty3.library.Representable;
import one.empty3.library.RepresentableConteneur;
import one.empty3.library.Scene;
import one.empty3.library.core.Arrays;

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

/**
 * @author Manuel Dahmen
 */
public class REditor extends JPanel implements PropertyChangeListener, RepresentableEditor {
    private  Representable r;
    History history = new History();

    private RPropertyDetailsRow tableModel;
    private DataModel dataModel ;


    public REditor(DataModel dataModel, Representable re) {
        super();
        this.dataModel = dataModel;
        initComponents();
        init(re);
        history.getHistory().add(0, tableModel);
        init(history.get(0));
        this.r = re;

    }

    public void init(Object re) {
        if(re!=null) {
            if (re instanceof Representable) {
                labelBreadCumbs.setText(re.getClass().getCanonicalName());
                this.tableModel = new RPropertyDetailsRow((Representable) re);
                tableObjectDetails.setModel(tableModel);
                firePropertyChange("representable", r, tableModel.getRepresentable());
                this.r  = tableModel.getRepresentable();

            } else if (re instanceof RPropertyDetailsRow) {
                labelBreadCumbs.setText(((RPropertyDetailsRow) re).getRepresentable().getClass().getCanonicalName());
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
                    getDataModel().save(null);
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
                if (tableModel.getItemList(selectedRow) instanceof Representable) {
                    boolean isNew = tableModel.getValueAt(selectedRow, 1) != null && tableModel.getValueAt(selectedRow, 1).toString().equals("NEW");
                    Representable newR = (Representable) tableModel.getItemList(selectedRow);
                    Representable oldR = (Representable) r;
                    init(tableModel.getItemList(selectedRow));
                    if (isNew) {
                        if (oldR instanceof Scene) {
                            ((Scene) oldR).add(newR);
                            System.out.print("Added to scene" + newR.toString());
                        }
                        if (oldR instanceof RepresentableConteneur) {
                            ((RepresentableConteneur) oldR).add(newR);
                            System.out.print("Added to scene" + newR.toString());
                        } else {
                            if (objectDetailDescription.getDim() == 1 && objectDetailDescription.getDescrition().equals("ADD NEW")) {
                                Object[] insert = new Arrays().insert((Object[]) objectDetailDescription.getValue(), Integer.parseInt(objectDetailDescription.getIndexes()));
                                try {
                                    newR.setProperty(objectDetailDescription.getName(), insert);
                                } catch (InvocationTargetException e1) {
                                    e1.printStackTrace();
                                } catch (IllegalAccessException e1) {
                                    e1.printStackTrace();
                                } catch (NoSuchMethodException e1) {
                                    e1.printStackTrace();
                                }
                            } else if (objectDetailDescription.getDim() == 2 && objectDetailDescription.getClazz().equals(Double[][].class)&& objectDetailDescription.getDescrition().equals("ADD NEW")) {
                                String[] split = objectDetailDescription.getIndexes().split(",");
                                int pos1 = Integer.parseInt(split[0]);
                                int pos2 = Integer.parseInt(split[1]);
                                Double[][] insert = new Arrays().insert(Double.class, (Double[][]) objectDetailDescription.getValue(),
                                        pos1, pos2, 0);// TODO
                                try {
                                    newR.setProperty(objectDetailDescription.getName(), insert);
                                } catch (InvocationTargetException e1) {
                                    e1.printStackTrace();
                                } catch (IllegalAccessException e1) {
                                    e1.printStackTrace();
                                } catch (NoSuchMethodException e1) {
                                    e1.printStackTrace();
                                }

                            } else if (objectDetailDescription.getDim() == 2 && objectDetailDescription.getClazz().isAssignableFrom(Representable[][].class)&& objectDetailDescription.getDescrition().equals("ADD NEW")) {
                                String[] split = objectDetailDescription.getIndexes().split(",");
                                int pos1 = Integer.parseInt(split[0]);
                                int pos2 = Integer.parseInt(split[1]);
                                Representable[][] insert = new Arrays().insert(Representable.class, (Representable[][]) objectDetailDescription.getValue(),
                                        pos1, pos2, 0);// TODO
                                try {
                                    newR.setProperty(objectDetailDescription.getName(), insert);
                                } catch (InvocationTargetException e1) {
                                    e1.printStackTrace();
                                } catch (IllegalAccessException e1) {
                                    e1.printStackTrace();
                                } catch (NoSuchMethodException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        }
                        history.addToHistory(tableModel);
                        System.out.println("add to history " + history.getCurrent());
                    } else if (tableObjectDetails.isCellEditable(selectedRow, 5))
                        tableObjectDetails.editCellAt(selectedRow, 5);
                } else {
                    MyObservableList<ObjectDescription> objectDescriptions = RepresentableClassList.myList();

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

    private void buttonPrevActionPerformed(ActionEvent e) {
        // TODO add your code here
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof ITexture) {
            try {
                tableModel.getRepresentable().setProperty(evt.getPropertyName(), evt.getNewValue());
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
        init(history.get(0));
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
        tableObjectDetails = new JTable() {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);

                try {
                    tip = getValueAt(rowIndex, 5).toString();
                    ObjectDetailDescription objectDetailDescription = ((RPropertyDetailsRow) this.getModel()).objectDetailDescriptions.get(rowIndex);
                    tip += objectDetailDescription.toString();
                } catch (RuntimeException e1) {

                }

                return tip;
            }
        };
        popupMenu1 = new JPopupMenu();
        menuItemDelete = new JMenuItem();
        menuItem1 = new JMenuItem();

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
                        buttonPrev.addActionListener(e -> {
			buttonPrevActionPerformed(e);
			buttonPrevActionPerformed(e);
			buttonPrevActionPerformed(e);
			buttonBackActionPerformed(e);
		});
                        toolBar1.add(buttonPrev);

                        //---- button1 ----
                        button1.setText("1");
                        button1.addActionListener(e -> {
			buttonRefreshActionPerformed(e);
			buttonPrevActionPerformed(e);
			button1ActionPerformed(e);
		});
                        toolBar1.add(button1);

                        //---- button2 ----
                        button2.setText("2");
                        button2.addActionListener(e -> {
			buttonPrevActionPerformed(e);
			button2ActionPerformed(e);
		});
                        toolBar1.add(button2);

                        //---- button3 ----
                        button3.setText("3");
                        button3.addActionListener(e -> {
			buttonPrevActionPerformed(e);
			button3ActionPerformed(e);
		});
                        toolBar1.add(button3);

                        //---- button4 ----
                        button4.setText("4");
                        button4.addActionListener(e -> {
			buttonPrevActionPerformed(e);
			button4ActionPerformed(e);
		});
                        toolBar1.add(button4);

                        //---- button6 ----
                        button6.setText("5");
                        button6.addActionListener(e -> {
			buttonPrevActionPerformed(e);
			button6ActionPerformed(e);
		});
                        toolBar1.add(button6);

                        //---- button7 ----
                        button7.addActionListener(e -> {
			buttonPrevActionPerformed(e);
			button7ActionPerformed(e);
		});
                        toolBar1.add(button7);

                        //---- buttonNext ----
                        buttonNext.setText(">>");
                        buttonNext.addActionListener(e -> {
			buttonPrevActionPerformed(e);
			buttonNextActionPerformed(e);
			buttonNextActionPerformed(e);
		});
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

            //---- menuItemDelete ----
            menuItemDelete.setText("Delete");
            popupMenu1.add(menuItemDelete);

            //---- menuItem1 ----
            menuItem1.setText("Refresh");
            popupMenu1.add(menuItem1);
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
    private JTable tableObjectDetails;
    private JPopupMenu popupMenu1;
    private JMenuItem menuItemDelete;
    private JMenuItem menuItem1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    @Override
    public void initValues(Representable representable) {
        init(representable);
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public Representable getRepresentable() {
        return r;
    }

    public void setRepresentable(Representable r) {
        this.r = r;
    }

    }
