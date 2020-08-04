/*
 * Created by JFormDesigner on Sun Aug 02 12:12:22 CEST 2020
 */

package one.empty3.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * @author Manuel Dahmen
 */
public class TexturesDrawEditMapOnObjectPart extends JPanel {
    public TexturesDrawEditMapOnObjectPart() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        this.menuBar1 = new JMenuBar();
        this.menu1 = new JMenu();
        this.menuItem3 = new JMenuItem();
        this.menuItem2 = new JMenuItem();
        this.menuItem1 = new JMenuItem();
        this.menuItem4 = new JMenuItem();
        this.tabbedPane1 = new JTabbedPane();

        //======== this ========
        setName("this"); //NON-NLS
        setLayout(new MigLayout(
            "fill,hidemode 3", //NON-NLS
            // columns
            "[fill]" + //NON-NLS
            "[fill]" + //NON-NLS
            "[fill]" + //NON-NLS
            "[fill]", //NON-NLS
            // rows
            "[]" + //NON-NLS
            "[]" + //NON-NLS
            "[]" + //NON-NLS
            "[]" + //NON-NLS
            "[]")); //NON-NLS

        //======== menuBar1 ========
        {
            this.menuBar1.setName("menuBar1"); //NON-NLS

            //======== menu1 ========
            {
                this.menu1.setText("Media"); //NON-NLS
                this.menu1.setName("menu1"); //NON-NLS

                //---- menuItem3 ----
                this.menuItem3.setText("Load"); //NON-NLS
                this.menuItem3.setName("menuItem3"); //NON-NLS
                this.menu1.add(this.menuItem3);

                //---- menuItem2 ----
                this.menuItem2.setText("Draw"); //NON-NLS
                this.menuItem2.setName("menuItem2"); //NON-NLS
                this.menu1.add(this.menuItem2);

                //---- menuItem1 ----
                this.menuItem1.setText("Map"); //NON-NLS
                this.menuItem1.setName("menuItem1"); //NON-NLS
                this.menu1.add(this.menuItem1);

                //---- menuItem4 ----
                this.menuItem4.setText("MapTo"); //NON-NLS
                this.menuItem4.setName("menuItem4"); //NON-NLS
                this.menu1.add(this.menuItem4);
            }
            this.menuBar1.add(this.menu1);
        }
        add(this.menuBar1, "cell 0 0 4 1"); //NON-NLS

        //======== tabbedPane1 ========
        {
            this.tabbedPane1.setName("tabbedPane1"); //NON-NLS
        }
        add(this.tabbedPane1, "cell 0 1 4 4"); //NON-NLS
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void initComponentsI18n() {
        // JFormDesigner - Component i18n initialization - DO NOT MODIFY  //GEN-BEGIN:initI18n
        // JFormDesigner - End of component i18n initialization  //GEN-END:initI18n
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem3;
    private JMenuItem menuItem2;
    private JMenuItem menuItem1;
    private JMenuItem menuItem4;
    private JTabbedPane tabbedPane1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
