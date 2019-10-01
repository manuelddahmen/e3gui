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
