package one.empty3.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;
/*
 * Created by JFormDesigner on Thu Jun 27 11:40:57 CEST 2019
 */



/**
 * @author Manuel Dahmen
 */
public class FormFunctionGUI  {

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("gui");
        FormFunctionGUI = new JFrame();

        //======== FormFunctionGUI ========
        {
            FormFunctionGUI.setTitle(bundle.getString("FormFunctionGUI.title"));
            Container FormFunctionGUIContentPane = FormFunctionGUI.getContentPane();
            FormFunctionGUIContentPane.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]"));
            FormFunctionGUI.pack();
            FormFunctionGUI.setLocationRelativeTo(FormFunctionGUI.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JFrame FormFunctionGUI;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
