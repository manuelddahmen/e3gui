/*
 * Created by JFormDesigner on Wed Sep 18 13:19:59 CEST 2019
 */

package one.empty3.gui;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.*;
import one.empty3.library.Representable;

/**
 * @author Manuel Dahmen
 */
public class SaveFileToRepresentable extends JDialog {
    private Representable r;

    public SaveFileToRepresentable(Window owner) {
        super(owner);
        initComponents();
    }

    public void init(Representable r, Object fileS, String property, int dim, int row, int col) {
        this.r = r;
        if(fileS instanceof File)
        {
        }
        else if(fileS instanceof List)
        {
            List<File> files = ((List<File>) fileS);

        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.gui.gui");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "insets dialog,hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]"));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx right",
                    // columns
                    "[button,fill]" +
                    "[button,fill]",
                    // rows
                    null));

                //---- okButton ----
                okButton.setText(bundle.getString("SaveFileToRepresentable.okButton.text"));
                buttonBar.add(okButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText(bundle.getString("SaveFileToRepresentable.cancelButton.text"));
                buttonBar.add(cancelButton, "cell 1 0");
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
