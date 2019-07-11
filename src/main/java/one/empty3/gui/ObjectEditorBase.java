/*
 * Created by JFormDesigner on Mon Jul 01 11:05:51 CEST 2019
 */

package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.Matrix33;
import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.library.core.raytracer.tree.AlgebraicFormulaSyntaxException;
import one.empty3.library.core.raytracer.tree.TreeNodeEvalException;
import one.empty3.library.core.script.InterpreteException;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

/**
 * @author Manuel Dahmen
 */
public class ObjectEditorBase extends JDialog {
    private boolean initValue;
    Representable r = null;
    private Point3D point3D = new Point3D();
    private Matrix33 matrix33 = new Matrix33(Matrix33.I);
    private Point3D scale = new Point3D();

    public ObjectEditorBase(Window owner,
                            Class<? extends Representable> classR) {
        super(owner);
        initComponents();
        initValue  =false;
        setVisible(true);
        initValues(classR);

    }
    public void initValues(Representable representable)
    {
        this.r = representable;
        // TODO init fields
        RepresentableClassList.setObjectFields(
                    point3D,
                    textAreaPoint3D,
                    new JTextField[]{textFieldX,textFieldY,textFieldZ},
                matrix33,
                textAreaMatrix33,
                    new JTextField[]{textField00, textField01, textField02,
                            textField10, textField11, textField12,
                            textField20, textField21, textField22},
                scale,
                textFieldScaleX,textFieldScaleY,textFieldScaleZ
            );
    }
    public void initValues(Class <? extends Representable> representable)
    {
        try {
            this.r = representable.newInstance();
            initValues(r);
            initValue = true;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void textFieldXYZActionPerformed(ActionEvent e) {
        if(initValue) {
            try {
                this.point3D = RepresentableClassList.pointParse(textFieldX.getText(), textFieldY.getText(), textFieldZ.getText());
                textAreaPoint3D.setText(point3D.toString());
            } catch (AlgebraicFormulaSyntaxException e1) {
                e1.printStackTrace();
            } catch (TreeNodeEvalException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void buttonOK1ActionPerformed(ActionEvent e) {
        if(initValue) {
            try {
                this.point3D = RepresentableClassList.pointParse(textAreaPoint3D.getText());
                // TODO COULEUR NOIRE textAreaPoint3D.getFont()
                textAreaPoint3D.setText(point3D.toString());
            } catch (InterpreteException e1) {
                //TODO COULEUR ROUGE
                e1.printStackTrace();
            }
        }
    }

    private void textFieldZActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void okButtonActionPerformed(ActionEvent e) {

    }

    private void textAreaMatrix33CaretUpdate(CaretEvent e) {
        if(initValue) {
            try {
                this.matrix33 = RepresentableClassList.loadMatrix(matrix33,
                        new JTextField[]{textField00, textField01, textField02,
                                textField10, textField11, textField12,
                                textField20, textField21, textField22},
                        textAreaMatrix33
                );
            } catch (AlgebraicFormulaSyntaxException e1) {
                e1.printStackTrace();
            } catch (TreeNodeEvalException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void textArea1Matrix33CaretUpdate(CaretEvent e) {
    }

    private void textFieldsMatrixActionPerformed(ActionEvent e) {
        if(initValue) {
            try {
                this.matrix33 = RepresentableClassList.matrixParse(textAreaMatrix33.getText());
                textAreaPoint3D.setText(matrix33.toString());
            } catch (InterpreteException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.gui.gui");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        layeredPane1 = new JLayeredPane();
        labelClass = new JLabel();
        scrollPane2 = new JScrollPane();
        scrollPane1 = new JScrollPane();
        textAreaPoint3D = new JTextArea(new Point3D().toString());
        label2 = new JLabel();
        textFieldX = new JTextField();
        label3 = new JLabel();
        textFieldY = new JTextField();
        label4 = new JLabel();
        textFieldZ = new JTextField();
        label1 = new JLabel();
        scrollPane3 = new JScrollPane();
        textAreaMatrix33 = new JTextArea();
        textField00 = new JTextField();
        textField01 = new JTextField();
        textField02 = new JTextField();
        textField10 = new JTextField();
        textField11 = new JTextField();
        textField12 = new JTextField();
        textField20 = new JTextField();
        textField21 = new JTextField();
        textField22 = new JTextField();
        label5 = new JLabel();
        textFieldScaleX = new JTextField();
        textFieldScaleY = new JTextField();
        textFieldScaleZ = new JTextField();
        labelDataPoints = new JLabel();
        buttonDataPoints = new JButton();
        labelDataDoubles = new JLabel();
        buttonDataDoubles = new JButton();
        label6 = new JLabel();
        button2 = new JButton();
        button4 = new JButton();
        button1 = new JButton();
        button3 = new JButton();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        helpButton = new JButton();

        //======== this ========
        setModal(true);
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
                contentPanel.add(layeredPane1, "cell 0 0");

                //---- labelClass ----
                labelClass.setText("Position");
                labelClass.setForeground(Color.white);
                labelClass.setBackground(new Color(51, 51, 255));
                labelClass.setOpaque(true);
                labelClass.setFont(new Font("Tahoma", Font.BOLD, 12));
                labelClass.setHorizontalAlignment(SwingConstants.CENTER);
                contentPanel.add(labelClass, "cell 0 1 3 1");

                //======== scrollPane2 ========
                {

                    //======== scrollPane1 ========
                    {

                        //---- textAreaPoint3D ----
                        textAreaPoint3D.setFont(new Font("Tahoma", Font.BOLD, 12));
                        scrollPane1.setViewportView(textAreaPoint3D);
                    }
                    scrollPane2.setViewportView(scrollPane1);
                }
                contentPanel.add(scrollPane2, "cell 0 2 3 1");

                //---- label2 ----
                label2.setText("X");
                label2.setLabelFor(textFieldX);
                contentPanel.add(label2, "cell 0 3");

                //---- textFieldX ----
                textFieldX.addActionListener(e -> textFieldXYZActionPerformed(e));
                contentPanel.add(textFieldX, "cell 0 3");

                //---- label3 ----
                label3.setText("Y");
                label3.setLabelFor(textFieldY);
                contentPanel.add(label3, "cell 1 3");

                //---- textFieldY ----
                textFieldY.addActionListener(e -> textFieldXYZActionPerformed(e));
                contentPanel.add(textFieldY, "cell 1 3");

                //---- label4 ----
                label4.setText(bundle.getString("ObjectEditorBase.label4.text"));
                label4.setLabelFor(textFieldZ);
                contentPanel.add(label4, "cell 2 3");

                //---- textFieldZ ----
                textFieldZ.addActionListener(e -> textFieldXYZActionPerformed(e));
                contentPanel.add(textFieldZ, "cell 2 3");

                //---- label1 ----
                label1.setText(bundle.getString("ObjectEditorBase.label1.text"));
                label1.setBackground(new Color(0, 51, 255));
                label1.setOpaque(true);
                label1.setForeground(Color.white);
                label1.setHorizontalAlignment(SwingConstants.CENTER);
                contentPanel.add(label1, "cell 0 4 3 1");

                //======== scrollPane3 ========
                {

                    //---- textAreaMatrix33 ----
                    textAreaMatrix33.setRows(3);
                    scrollPane3.setViewportView(textAreaMatrix33);
                }
                contentPanel.add(scrollPane3, "cell 0 5 3 3");

                //---- textField00 ----
                textField00.addCaretListener(e -> textAreaMatrix33CaretUpdate(e));
                textField00.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField00, "cell 0 8");

                //---- textField01 ----
                textField01.addCaretListener(e -> textAreaMatrix33CaretUpdate(e));
                textField01.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField01, "cell 1 8");

                //---- textField02 ----
                textField02.addCaretListener(e -> textAreaMatrix33CaretUpdate(e));
                textField02.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField02, "cell 2 8");

                //---- textField10 ----
                textField10.addCaretListener(e -> textAreaMatrix33CaretUpdate(e));
                textField10.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField10, "cell 0 9");

                //---- textField11 ----
                textField11.addCaretListener(e -> textAreaMatrix33CaretUpdate(e));
                textField11.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField11, "cell 1 9");

                //---- textField12 ----
                textField12.addCaretListener(e -> textAreaMatrix33CaretUpdate(e));
                textField12.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField12, "cell 2 9");

                //---- textField20 ----
                textField20.addCaretListener(e -> textAreaMatrix33CaretUpdate(e));
                textField20.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField20, "cell 0 10");

                //---- textField21 ----
                textField21.addCaretListener(e -> textAreaMatrix33CaretUpdate(e));
                textField21.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField21, "cell 1 10");

                //---- textField22 ----
                textField22.addCaretListener(e -> textAreaMatrix33CaretUpdate(e));
                textField22.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField22, "cell 2 10");

                //---- label5 ----
                label5.setText("Scale");
                label5.setBackground(new Color(0, 51, 255));
                label5.setOpaque(true);
                label5.setForeground(Color.white);
                label5.setHorizontalAlignment(SwingConstants.CENTER);
                contentPanel.add(label5, "cell 0 11 3 1");
                contentPanel.add(textFieldScaleX, "cell 0 12");
                contentPanel.add(textFieldScaleY, "cell 1 12");
                contentPanel.add(textFieldScaleZ, "cell 2 12");

                //---- labelDataPoints ----
                labelDataPoints.setText("Data Points");
                contentPanel.add(labelDataPoints, "cell 0 13");

                //---- buttonDataPoints ----
                buttonDataPoints.setText("Data Points");
                contentPanel.add(buttonDataPoints, "cell 0 13");

                //---- labelDataDoubles ----
                labelDataDoubles.setText("Data Doubles");
                contentPanel.add(labelDataDoubles, "cell 1 13");

                //---- buttonDataDoubles ----
                buttonDataDoubles.setText("Data Doubles");
                contentPanel.add(buttonDataDoubles, "cell 1 13");

                //---- label6 ----
                label6.setText("Edit Texture");
                contentPanel.add(label6, "cell 2 13");

                //---- button2 ----
                button2.setText("Texture");
                contentPanel.add(button2, "cell 2 13");

                //---- button4 ----
                button4.setText("Representable");
                contentPanel.add(button4, "cell 0 14");

                //---- button1 ----
                button1.setText("Double Array");
                contentPanel.add(button1, "cell 1 14");

                //---- button3 ----
                button3.setText("Double Matrix");
                contentPanel.add(button3, "cell 1 14");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx right",
                    // columns
                    "[button,fill]" +
                    "[button,fill]" +
                    "[button,fill]",
                    // rows
                    null));

                //---- okButton ----
                okButton.setText(bundle.getString("ObjectEditorBase.okButton.text"));
                okButton.addActionListener(e -> okButtonActionPerformed(e));
                buttonBar.add(okButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText(bundle.getString("ObjectEditorBase.cancelButton.text"));
                buttonBar.add(cancelButton, "cell 1 0");

                //---- helpButton ----
                helpButton.setText(bundle.getString("ObjectEditorBase.helpButton.text"));
                buttonBar.add(helpButton, "cell 2 0");
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
    private JLayeredPane layeredPane1;
    private JLabel labelClass;
    private JScrollPane scrollPane2;
    private JScrollPane scrollPane1;
    private JTextArea textAreaPoint3D;
    private JLabel label2;
    private JTextField textFieldX;
    private JLabel label3;
    private JTextField textFieldY;
    private JLabel label4;
    private JTextField textFieldZ;
    private JLabel label1;
    private JScrollPane scrollPane3;
    private JTextArea textAreaMatrix33;
    private JTextField textField00;
    private JTextField textField01;
    private JTextField textField02;
    private JTextField textField10;
    private JTextField textField11;
    private JTextField textField12;
    private JTextField textField20;
    private JTextField textField21;
    private JTextField textField22;
    private JLabel label5;
    private JTextField textFieldScaleX;
    private JTextField textFieldScaleY;
    private JTextField textFieldScaleZ;
    private JLabel labelDataPoints;
    private JButton buttonDataPoints;
    private JLabel labelDataDoubles;
    private JButton buttonDataDoubles;
    private JLabel label6;
    private JButton button2;
    private JButton button4;
    private JButton button1;
    private JButton button3;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    private JButton helpButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
