package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.Representable;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
/*
 * Created by JFormDesigner on Thu Jun 20 22:09:55 CEST 2019
 */



/**
 * @author Manuel Dahmen
 */
public class ObjectsView extends JFrame {
	public ObjectsView()
    {
        initComponents();
        setVisible(true);

	}

    private void table1MouseClicked(MouseEvent e) {
        ObjectEditorBase objectEditorBase = new ObjectEditorBase(getOwner(),
                (Class<? extends Representable>) table1.getValueAt(table1.getSelectedRow(), 1));
        //listR.add
    }
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane3 = new JScrollPane();
        listR = RepresentableClassList.getScenes();
        scrollPane6 = new JScrollPane();
        table1 = new JTable();
        scrollPane4 = new JScrollPane();
        button1 = new JButton();
        button2 = new JButton();
        scrollPane2 = new JScrollPane();
        observableList1 = new RepresentableClassList().myList();

        //======== this ========
        setTitle("Object View");
        setBackground(new Color(204, 204, 0));
        setFont(new Font("Verdana", Font.PLAIN, 18));
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "fill,hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //======== scrollPane3 ========
        {

            //---- listR ----
            listR.setFont(new Font("Tahoma", Font.PLAIN, 16));
            scrollPane3.setViewportView(listR);
        }
        contentPane.add(scrollPane3, "cell 1 0 1 3,dock center");

        //======== scrollPane6 ========
        {

            //---- table1 ----
            table1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    table1MouseClicked(e);
                }
            });
            scrollPane6.setViewportView(table1);
        }
        contentPane.add(scrollPane6, "cell 0 0 1 2,dock center");
        contentPane.add(scrollPane4, "cell 1 1,dock center");

        //---- button1 ----
        button1.setText("Add");
        button1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        contentPane.add(button1, "cell 0 2,dock center");

        //---- button2 ----
        button2.setText("Edit");
        button2.setFont(new Font("Tahoma", Font.PLAIN, 16));
        contentPane.add(button2, "cell 0 2,dock center");
        contentPane.add(scrollPane2, "cell 1 2,dock center");
        pack();
        setLocationRelativeTo(getOwner());

        //---- bindings ----
        bindingGroup = new BindingGroup();
        {
            JTableBinding binding = SwingBindings.createJTableBinding(UpdateStrategy.READ_ONCE,
                observableList1, table1);
            binding.addColumnBinding(BeanProperty.create("name"))
                .setColumnName("name")
                .setColumnClass(String.class);
            binding.addColumnBinding(BeanProperty.create("r"))
                .setColumnName("R")
                .setColumnClass(Class.class);
            bindingGroup.addBinding(binding);
        }
        bindingGroup.bind();
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane3;
    private JTree listR;
    private JScrollPane scrollPane6;
    private JTable table1;
    private JScrollPane scrollPane4;
    private JButton button1;
    private JButton button2;
    private JScrollPane scrollPane2;
    private ArrayList<one.empty3.gui.ObjectDescription> observableList1;
    private BindingGroup bindingGroup;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
