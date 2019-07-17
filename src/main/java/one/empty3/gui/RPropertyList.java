/*
 * Created by JFormDesigner on Mon Jul 15 09:34:29 CEST 2019
 */

package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.Representable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * @author Manuel Dahmen
 */
public class RPropertyList extends JDialog {
    private final RPropertyDetailsRow tableModel;

    public RPropertyList(Window owner, Representable re) {
        super(owner);
        initComponents();
        this.tableModel = new RPropertyDetailsRow(re);
        tableObjectDetails.setModel(tableModel);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.gui.gui");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        labelBreadCumbs = new JLabel();
        scrollPane1 = new JScrollPane();
        toolBar1 = new JToolBar();
        buttonBack = new JButton();
        buttonNext = new JButton();
        buttonRefresh = new JButton();
        buttonHome = new JButton();
        scrollPane2 = new JScrollPane();
        tableObjectDetails = new JTable();
        buttonBar = new JPanel();
        okButton = new JButton();
        helpButton = new JButton();

        //======== this ========
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

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
                labelBreadCumbs.setText("BreadCumbs Scene");
                contentPanel.add(labelBreadCumbs, "cell 0 0 3 2");

                //======== scrollPane1 ========
                {

                    //======== toolBar1 ========
                    {

                        //---- buttonBack ----
                        buttonBack.setText(bundle.getString("RPropertyList.buttonBack.text"));
                        toolBar1.add(buttonBack);

                        //---- buttonNext ----
                        buttonNext.setText(bundle.getString("RPropertyList.buttonNext.text"));
                        toolBar1.add(buttonNext);

                        //---- buttonRefresh ----
                        buttonRefresh.setText(bundle.getString("RPropertyList.buttonRefresh.text"));
                        toolBar1.add(buttonRefresh);

                        //---- buttonHome ----
                        buttonHome.setText(bundle.getString("RPropertyList.buttonHome.text"));
                        toolBar1.add(buttonHome);
                    }
                    scrollPane1.setViewportView(toolBar1);
                }
                contentPanel.add(scrollPane1, "cell 0 0 3 2");

                //======== scrollPane2 ========
                {

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
                    scrollPane2.setViewportView(tableObjectDetails);
                }
                contentPanel.add(scrollPane2, "cell 0 2 3 1");
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
                okButton.setText(bundle.getString("RPropertyList.okButton.text"));
                buttonBar.add(okButton, "cell 0 0");

                //---- helpButton ----
                helpButton.setText(bundle.getString("RPropertyList.helpButton.text"));
                buttonBar.add(helpButton, "cell 1 0");
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
    private JLabel labelBreadCumbs;
    private JScrollPane scrollPane1;
    private JToolBar toolBar1;
    private JButton buttonBack;
    private JButton buttonNext;
    private JButton buttonRefresh;
    private JButton buttonHome;
    private JScrollPane scrollPane2;
    private JTable tableObjectDetails;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton helpButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
