/*
 * Created by JFormDesigner on Mon Jul 01 11:05:51 CEST 2019
 */

package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.Matrix33;
import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.library.Rotation;
import one.empty3.library.core.raytracer.tree.AlgebraicFormulaSyntaxException;
import one.empty3.library.core.raytracer.tree.TreeNodeEvalException;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * @author Manuel Dahmen
 */
public class ObjectEditorBase extends JPanel implements RepresentableEditor {
    private boolean initValues;
    Representable r = null;

    public ObjectEditorBase(Representable r) {
        super();
        initComponents();
        initValues =false;
        initValues(r);
        setVisible(true);

    }
    public ObjectEditorBase() {
        super();
        initComponents();
        initValues =false;
        setVisible(true);

    }

    public void initValues(Representable representable)
    {
        if(r!=representable || r==null ) {
            this.r = representable;
            initValues(r,

                    new JTextField[]{
                            textFieldX, textFieldY, textFieldZ
                    },
                    new JTextField[]{
                            textField00, textField01, textField02,
                            textField10, textField11, textField12,
                            textField20, textField21, textField22},
                    textAreaPoint3D,
                    textAreaMatrix33,
                    new JTextField[]{
                            textFieldScaleX, textFieldScaleY, textFieldScaleZ
                    }
            );
            initValues = true;
        }
        Logger.getAnonymousLogger().info("initValues : " + r.toString());
    }

    private void initValues(Representable r, JTextField[] point3d, JTextField[] jTextFieldMatrix, JTextArea textAreaPoint3D,
                            JTextArea textAreaMatrix33,
                            JTextField[] jTextFieldsScale) {
        Rotation rotation = r.getRotation();
        Point3D centreRot = rotation.getCentreRot();
        Matrix33 rot = rotation.getRot();
        Point3D scale =r.getScale();
        textAreaMatrix33.setText(rot.toString());
        for(int i = 0; i<3; i++)
            point3d[i].setText(
                    ""+centreRot
                            .get(i));
        for(int i=0; i<9; i++)
            jTextFieldMatrix[i].setText(String.valueOf(rot.get((i/3), (i%3))));
        textAreaPoint3D.setText(""+centreRot.toString());
        for(int i = 0; i<3; i++)
            jTextFieldsScale[i].setText(""+scale.get(i));
    }
    private void saveValues(Representable r, JTextField[] point3d, JTextField[] jTextFieldMatrix, JTextArea textAreaPoint3D,
                            JTextArea textAreaMatrix33,
                            JTextField[] jTextFieldsScale) {
        Rotation rotation = new Rotation();
        Point3D centreRot = new Point3D();
        Matrix33 rot = new Matrix33();
        Point3D scale =  new Point3D();
        for(int i = 0; i<3; i++) {
            centreRot.set(i, Double.parseDouble(point3d[i].getText()));
        }
        for(int i=0; i<9; i++)
            rot.set((i/3), (i%3), Double.parseDouble(jTextFieldMatrix[i].getText()==""?"0.0":jTextFieldMatrix[i].getText()));
        textAreaPoint3D.setText(""+centreRot.toString());
        textAreaMatrix33.setText(rot.toString());
        for(int i = 0; i<3; i++)
            scale.set(i, Double.parseDouble(jTextFieldsScale[i].getText()));

        r.setRotation(new Rotation(rot, centreRot));
        r.setScale(scale);
    }

    private void textFieldXYZActionPerformed(ActionEvent e) {
    }

    private void buttonOK1ActionPerformed(ActionEvent e) {
    }

    private void textFieldZActionPerformed(ActionEvent e) {
    }

    private void okButtonActionPerformed(ActionEvent e) {
        saveValues(r);
        initValues(r);
        Logger.getAnonymousLogger().info("save then load "+r.getClass().getName());

    }

    private void changeText(JTextField f, String text) {

        Runnable doHighlight = new Runnable() {
            @Override
            public void run() {
                f.setText(text);
            }
        };
        SwingUtilities.invokeLater(doHighlight);
    }

    public Matrix33 loadMatrix(Matrix33 m, JTextField[] strings, JTextArea text) throws AlgebraicFormulaSyntaxException, TreeNodeEvalException {
        for(int i=0; i<strings.length; i++) {
            //AlgebricTree treeI = new AlgebricTree(strings[i].getText());
            //treeI.construct();
            m.set(i/3, i%3, ((double) Double.parseDouble(strings[i].getText())));

            changeText(strings[i], ""+m.get(i/3, i%3));
            text.setText(m.toString());
        }
        return m;
    }

    private void textAreaMatrix33CaretUpdate(CaretEvent e) {
        //saveValues();
    }

    private void saveValues(Representable r) {
        if(initValues) {
                saveValues(r,

                        new JTextField[]{
                                textFieldX, textFieldY, textFieldZ
                        },
                        new JTextField[]{
                                textField00, textField01, textField02,
                                textField10,textField11,textField12,
                                textField20,textField21,textField22},
                        textAreaPoint3D,
                        textAreaMatrix33,
                        new JTextField[]{
                                textFieldScaleX, textFieldScaleY, textFieldScaleZ
                        }
                );
        }

    }


    private void textArea1Matrix33CaretUpdate(CaretEvent e) {
    }

    private void textFieldsMatrixActionPerformed(ActionEvent e) {

    }

    private void textAreaPoint3DPropertyChange(PropertyChangeEvent e) {
    }

    private void button1ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void buttonOkActionPerformed(ActionEvent e) {
        saveValues(r);
        initValues(r);
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
        button1 = new JButton();

        //======== this ========
        setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "fillx,insets dialog,hidemode 3",
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

                //======== layeredPane1 ========
                {
                    layeredPane1.setAlignmentX(0.0F);
                    layeredPane1.setAlignmentY(0.0F);
                }
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
                        textAreaPoint3D.setEnabled(false);
                        textAreaPoint3D.addPropertyChangeListener(e -> textAreaPoint3DPropertyChange(e));
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
                    textAreaMatrix33.setEnabled(false);
                    scrollPane3.setViewportView(textAreaMatrix33);
                }
                contentPanel.add(scrollPane3, "cell 0 5 3 3");

                //---- textField00 ----
                textField00.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField00, "cell 0 8");

                //---- textField01 ----
                textField01.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField01, "cell 1 8");

                //---- textField02 ----
                textField02.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField02, "cell 2 8");

                //---- textField10 ----
                textField10.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField10, "cell 0 9");

                //---- textField11 ----
                textField11.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField11, "cell 1 9");

                //---- textField12 ----
                textField12.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField12, "cell 2 9");

                //---- textField20 ----
                textField20.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField20, "cell 0 10");

                //---- textField21 ----
                textField21.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                contentPanel.add(textField21, "cell 1 10");

                //---- textField22 ----
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

                //---- button1 ----
                button1.setText("Ok");
                button1.addActionListener(e -> {
			button1ActionPerformed(e);
			buttonOkActionPerformed(e);
		});
                contentPanel.add(button1, "cell 0 13");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        add(dialogPane, BorderLayout.CENTER);
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
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
