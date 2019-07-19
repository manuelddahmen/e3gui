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
public class ObjectsView {
	public ObjectsView()
    {

        initComponents();
        ObjectsView.setVisible(true);
	}

    private void table1MouseClicked(MouseEvent e) {
        ObjectEditorBase objectEditorBase = new ObjectEditorBase(
                (Class<? extends Representable>) table1.getValueAt(table1.getSelectedRow(), 1));
   }
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ObjectsView = new JDialog();
        objectsView = new JPanel();
        scrollPane6 = new JScrollPane();
        table1 = new JTable();
        observableList1 = new RepresentableClassList().myList();

        //======== ObjectsView ========
        {
            ObjectsView.setTitle("Object choice");
            ObjectsView.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
            Container ObjectsViewContentPane = ObjectsView.getContentPane();
            ObjectsViewContentPane.setLayout(new MigLayout(
                "fill,hidemode 3",
                // columns
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]"));

            //======== objectsView ========
            {
                objectsView.setBackground(new Color(204, 204, 0));
                objectsView.setFont(new Font("Verdana", Font.PLAIN, 18));
                objectsView.setPreferredSize(new Dimension(500, 500));
                objectsView.setMinimumSize(new Dimension(500, 500));
                objectsView.setLayout(new MigLayout(
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
                objectsView.add(scrollPane6, "cell 0 0 3 4,dock center");
            }
            ObjectsViewContentPane.add(objectsView, "cell 0 0");
            ObjectsView.pack();
            ObjectsView.setLocationRelativeTo(ObjectsView.getOwner());
        }

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
    private JDialog ObjectsView;
    private JPanel objectsView;
    private JScrollPane scrollPane6;
    private JTable table1;
    private ArrayList<one.empty3.gui.ObjectDescription> observableList1;
    private BindingGroup bindingGroup;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
