package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.Camera;
import one.empty3.library.Matrix33;
import one.empty3.library.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

/**
 * Created by manue on 04-07-19.
 */
public class Camera3dControl extends JDialog {
    FormFunction scriptPanel;

    Camera camera;
    public Camera3dControl(FormFunction scriptPanel, Camera c) {
        super();
        initComponents();
        this.scriptPanel = scriptPanel;
        this.camera  = c;
        if(camera==null)
            camera = new Camera();
        initValues();
        setVisible(true);
    }

    public void initValues() {
            // TODO init fields
            RepresentableClassList.setObjectFieldsCamera(
                    camera,
                    new JTextField[]{
                            textFieldE0, textFieldE1, textFieldE2,
                            textFieldL0, textFieldL1, textFieldL2,
                            textFieldV0, textFieldV1, textFieldV2,
                            textFieldMX0, textFieldMX1, textFieldMX2,
                            textFieldMY0, textFieldMY1, textFieldMY2,
                            textFieldMZ0, textFieldMZ1, textFieldMZ2,
                            textFieldAX, textFieldAY,
                            textFieldR
                    }
            );
    }
    public double getD(JTextField f)
    {
        return Double.parseDouble(f.getText());
    }
    private void okButtonActionPerformed(ActionEvent e) {
        try {
            Camera camera = new Camera();
            camera.setEye(new Point3D(getD(textFieldE0), getD(textFieldE1), getD(textFieldE2)));
            camera.setLookat(new Point3D(getD(textFieldL0), getD(textFieldL1), getD(textFieldL2)));
            camera.setMatrice(new Matrix33(new Double[]{
                    getD(textFieldMX0), getD(textFieldMX1), getD(textFieldMX2),
                    getD(textFieldMY0), getD(textFieldMY1), getD(textFieldMY2),
                    getD(textFieldMZ0), getD(textFieldMZ1), getD(textFieldMZ2)
            }));
            camera.angleXY(getD(textFieldAX), getD(textFieldAY));
            if(radioButtonCOMPMAT.isSelected())
            {
                if(textFieldV0.getText().equals("")||textFieldV1.getText().equals("")||textFieldV2.getText().equals(""))
                {
                    camera.calculerMatrice(new Point3D(getD(textFieldMY0), getD(textFieldMY1), getD(textFieldMY2)));
                }
                else
                {
                    camera.calculerMatrice(new Point3D(getD(textFieldV0), getD(textFieldV1), getD(textFieldV2)));
                }
            }
            else
            {
                camera.calculerMatrice(new Point3D(getD(textFieldMY0), getD(textFieldMY1), getD(textFieldMY2)));
            }
            scriptPanel.getPanelView3D().getView().setCamera(camera);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.gui.gui");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        textFieldE0 = new JTextField();
        textFieldE1 = new JTextField();
        textFieldE2 = new JTextField();
        label2 = new JLabel();
        textFieldL0 = new JTextField();
        textFieldL1 = new JTextField();
        textFieldL2 = new JTextField();
        label3 = new JLabel();
        textFieldV0 = new JTextField();
        textFieldV1 = new JTextField();
        textFieldV2 = new JTextField();
        label4 = new JLabel();
        textFieldAX = new JTextField();
        textFieldAY = new JTextField();
        label5 = new JLabel();
        textFieldR = new JTextField();
        label6 = new JLabel();
        textFieldMX0 = new JTextField();
        textFieldMX1 = new JTextField();
        textFieldMX2 = new JTextField();
        label10 = new JLabel();
        textFieldMY0 = new JTextField();
        textFieldMY1 = new JTextField();
        textFieldMY2 = new JTextField();
        label9 = new JLabel();
        textFieldMZ0 = new JTextField();
        textFieldMZ1 = new JTextField();
        textFieldMZ2 = new JTextField();
        label8 = new JLabel();
        radioButtonDEFMAT = new JRadioButton();
        label11 = new JLabel();
        radioButtonCOMPMAT = new JRadioButton();
        buttonBar = new JPanel();
        okButton = new JButton();
        helpButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "fill,insets dialog",
                    // columns
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
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));

                //---- label1 ----
                label1.setText("Eye");
                contentPanel.add(label1, "cell 0 0");
                contentPanel.add(textFieldE0, "cell 1 0");
                contentPanel.add(textFieldE1, "cell 2 0");
                contentPanel.add(textFieldE2, "cell 3 0");

                //---- label2 ----
                label2.setText("LookAt");
                contentPanel.add(label2, "cell 0 1");
                contentPanel.add(textFieldL0, "cell 1 1");
                contentPanel.add(textFieldL1, "cell 2 1");
                contentPanel.add(textFieldL2, "cell 3 1");

                //---- label3 ----
                label3.setText("Vertical (Y)");
                contentPanel.add(label3, "cell 0 2");
                contentPanel.add(textFieldV0, "cell 1 2");
                contentPanel.add(textFieldV1, "cell 2 2");
                contentPanel.add(textFieldV2, "cell 3 2");

                //---- label4 ----
                label4.setText("X Y angles");
                contentPanel.add(label4, "cell 0 3");
                contentPanel.add(textFieldAX, "cell 1 3");
                contentPanel.add(textFieldAY, "cell 2 3");

                //---- label5 ----
                label5.setText("ratio moveTo");
                contentPanel.add(label5, "cell 0 4");
                contentPanel.add(textFieldR, "cell 1 4");

                //---- label6 ----
                label6.setText("Camera Matrix X");
                contentPanel.add(label6, "cell 0 5");
                contentPanel.add(textFieldMX0, "cell 1 5");
                contentPanel.add(textFieldMX1, "cell 2 5");
                contentPanel.add(textFieldMX2, "cell 3 5");

                //---- label10 ----
                label10.setText(bundle.getString("Camera3dControl.label10.text"));
                contentPanel.add(label10, "cell 0 6");
                contentPanel.add(textFieldMY0, "cell 1 6");
                contentPanel.add(textFieldMY1, "cell 2 6");
                contentPanel.add(textFieldMY2, "cell 3 6");

                //---- label9 ----
                label9.setText("Z");
                contentPanel.add(label9, "cell 0 7");
                contentPanel.add(textFieldMZ0, "cell 1 7");
                contentPanel.add(textFieldMZ1, "cell 2 7");
                contentPanel.add(textFieldMZ2, "cell 3 7");

                //---- label8 ----
                label8.setText(bundle.getString("Camera3dControl.label8.text"));
                contentPanel.add(label8, "cell 0 8");

                //---- radioButtonDEFMAT ----
                radioButtonDEFMAT.setText(bundle.getString("Camera3dControl.radioButtonDEFMAT.text"));
                radioButtonDEFMAT.setSelected(true);
                contentPanel.add(radioButtonDEFMAT, "cell 1 8");

                //---- label11 ----
                label11.setText(bundle.getString("Camera3dControl.label11.text"));
                contentPanel.add(label11, "cell 2 8");

                //---- radioButtonCOMPMAT ----
                radioButtonCOMPMAT.setText(bundle.getString("Camera3dControl.radioButtonCOMPMAT.text"));
                contentPanel.add(radioButtonCOMPMAT, "cell 3 8");
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
                okButton.setText(bundle.getString("Camera3dControl.okButton.text"));
                okButton.addActionListener(e -> {
			okButtonActionPerformed(e);
			okButtonActionPerformed(e);
			okButtonActionPerformed(e);
			okButtonActionPerformed(e);
		});
                buttonBar.add(okButton, "cell 0 0");

                //---- helpButton ----
                helpButton.setText(bundle.getString("Camera3dControl.helpButton.text"));
                buttonBar.add(helpButton, "cell 1 0");
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(radioButtonDEFMAT);
        buttonGroup1.add(radioButtonCOMPMAT);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JTextField textFieldE0;
    private JTextField textFieldE1;
    private JTextField textFieldE2;
    private JLabel label2;
    private JTextField textFieldL0;
    private JTextField textFieldL1;
    private JTextField textFieldL2;
    private JLabel label3;
    private JTextField textFieldV0;
    private JTextField textFieldV1;
    private JTextField textFieldV2;
    private JLabel label4;
    private JTextField textFieldAX;
    private JTextField textFieldAY;
    private JLabel label5;
    private JTextField textFieldR;
    private JLabel label6;
    private JTextField textFieldMX0;
    private JTextField textFieldMX1;
    private JTextField textFieldMX2;
    private JLabel label10;
    private JTextField textFieldMY0;
    private JTextField textFieldMY1;
    private JTextField textFieldMY2;
    private JLabel label9;
    private JTextField textFieldMZ0;
    private JTextField textFieldMZ1;
    private JTextField textFieldMZ2;
    private JLabel label8;
    private JRadioButton radioButtonDEFMAT;
    private JLabel label11;
    private JRadioButton radioButtonCOMPMAT;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton helpButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
