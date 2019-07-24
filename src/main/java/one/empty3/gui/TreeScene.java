/*
 * Created by JFormDesigner on Tue Jul 23 13:07:01 CEST 2019
 */

package one.empty3.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Manuel Dahmen
 */
public class TreeScene extends JPanel implements PropertyChangeListener{
    public TreeScene() {
        initComponents();
        treeScene.addPropertyChangeListener(this);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        treeScene = new JTree();

        //======== this ========
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
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
            scrollPane1.setViewportView(treeScene);
        }
        add(scrollPane1, "cell 0 0 6 5,dock center");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private JTree treeScene;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
