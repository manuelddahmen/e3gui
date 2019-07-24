/*
 * Created by JFormDesigner on Mon Jul 15 09:34:29 CEST 2019
 */

package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.ITexture;
import one.empty3.library.Representable;
import one.empty3.library.RepresentableConteneur;
import one.empty3.library.Scene;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Manuel Dahmen
 */
public class REditor extends JPanel implements PropertyChangeListener {
    private final Representable r;
    History history = new History();

    private RPropertyDetailsRow tableModel;


    public REditor(Representable re) {
        super();
        initComponents();
        init(re);
        history.addToHistory(tableModel);
        init(history.get(0));
        this.r = re;
    }

    public void init(Object re) {
        if(re!=null) {
            if (re instanceof Representable) {
                labelBreadCumbs.setText(re.getClass().getCanonicalName());
                this.tableModel = new RPropertyDetailsRow((Representable) re);
                tableObjectDetails.setModel(tableModel);

            } else if (re instanceof RPropertyDetailsRow) {
                RPropertyDetailsRow model = (RPropertyDetailsRow) re;
                this.tableModel = model;
                tableObjectDetails.setModel(model);
            }
        }
    }

    private void buttonRefreshActionPerformed(ActionEvent e) {
        tableModel.initTable();
    }

    private void scrollPane2MouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void tableObjectDetailsMouseClicked(MouseEvent e) {
        System.out.println(e.getButton());
        if(e.getButton()==1) {
            int selectedRow = tableObjectDetails.getSelectedRow();
            if (tableModel.getItemList(selectedRow) != null) {
                if (tableModel.getItemList(selectedRow) instanceof Representable) {
                    Representable newR = (Representable) tableModel.getItemList(selectedRow);
                    Representable oldR = (Representable) r;
                    init(tableModel.getItemList(selectedRow));
                    if(oldR instanceof Scene) {
                        ((Scene) oldR).add(newR);
                        System.out.print("Added to scene"+newR.toString());
                    }
                    if(oldR instanceof RepresentableConteneur) {
                        ((RepresentableConteneur) oldR).add(newR);
                        System.out.print("Added to scene" + newR.toString());
                    }
                    history.addToHistory(tableModel);
                    System.out.println("add to history "+history.getCurrent());
                } /*else if (tableModel.getItemList(selectedRow) instanceof ITexture) {
                    ITexture tex = ((ITexture) tableModel.getItemList(selectedRow));
                    LoadTexture loadTexture = new LoadTexture(this, tex);
                    loadTexture.addPropertyChangeListener(this);
                } */else if (tableObjectDetails.isCellEditable(selectedRow, 5))
                    tableObjectDetails.editCellAt(selectedRow, 5);
            } else {
                MyObservableList<ObjectDescription> objectDescriptions = RepresentableClassList.myList();

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

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        labelBreadCumbs = new JLabel();
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
        scrollPane2 = new JScrollPane();
        tableObjectDetails = new JTable();

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

                //---- labelBreadCumbs ----
                labelBreadCumbs.setText("Navigation history");
                contentPanel.add(labelBreadCumbs, "cell 0 0 3 2");

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
                        button7.setText("6");
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
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel labelBreadCumbs;
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
    private JScrollPane scrollPane2;
    private JTable tableObjectDetails;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
