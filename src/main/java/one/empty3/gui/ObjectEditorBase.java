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
        if(r!=null)
        {
            Rotation rotation = r.getRotation().getElem();
            if(rotation!=null) {
                Point3D centreRot = rotation.getCentreRot().getElem();
                if (centreRot != null) {
                    Matrix33 rot = rotation.getRot().getElem();
                    Point3D scale = r.getScale();
                    if (rot != null && scale != null) {
                        textAreaMatrix33.setText(rot.toString());
                        try {
                            for (int i = 0; i < 3; i++)
                                point3d[i].setText(
                                        "" + centreRot.get(i));
                            for (int i = 0; i < 9; i++)
                                jTextFieldMatrix[i].setText(String.valueOf(rot.get((i / 3), (i % 3))));
                            textAreaPoint3D.setText("" + centreRot.toString());
                            for (int i = 0; i < 3; i++)
                                jTextFieldsScale[i].setText("" + scale.get(i));
                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }

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

        r.getRotation().setElem(new Rotation(rot, centreRot));
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
        this.dialogPane = new JPanel();
        this.contentPanel = new JPanel();
        this.layeredPane1 = new JLayeredPane();
        this.labelClass = new JLabel();
        this.scrollPane2 = new JScrollPane();
        this.scrollPane1 = new JScrollPane();
        this.textAreaPoint3D = new JTextArea(new Point3D().toString());
        this.label2 = new JLabel();
        this.textFieldX = new JTextField();
        this.label3 = new JLabel();
        this.textFieldY = new JTextField();
        this.label4 = new JLabel();
        this.textFieldZ = new JTextField();
        this.label1 = new JLabel();
        this.scrollPane3 = new JScrollPane();
        this.textAreaMatrix33 = new JTextArea();
        this.textField00 = new JTextField();
        this.textField01 = new JTextField();
        this.textField02 = new JTextField();
        this.textField10 = new JTextField();
        this.textField11 = new JTextField();
        this.textField12 = new JTextField();
        this.textField20 = new JTextField();
        this.textField21 = new JTextField();
        this.textField22 = new JTextField();
        this.label5 = new JLabel();
        this.textFieldScaleX = new JTextField();
        this.textFieldScaleY = new JTextField();
        this.textFieldScaleZ = new JTextField();
        this.button1 = new JButton();

        //======== this ========
        setName("this"); //NON-NLS
        setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            this.dialogPane.setName("dialogPane"); //NON-NLS
            this.dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                this.contentPanel.setName("contentPanel"); //NON-NLS
                this.contentPanel.setLayout(new MigLayout(
                    "fillx,insets dialog,hidemode 3", //NON-NLS
                    // columns
                    "[fill]" + //NON-NLS
                    "[fill]" + //NON-NLS
                    "[fill]", //NON-NLS
                    // rows
                    "[]" + //NON-NLS
                    "[]" + //NON-NLS
                    "[]" + //NON-NLS
                    "[]" + //NON-NLS
                    "[]" + //NON-NLS
                    "[]" + //NON-NLS
                    "[]" + //NON-NLS
                    "[]" + //NON-NLS
                    "[]" + //NON-NLS
                    "[]" + //NON-NLS
                    "[]" + //NON-NLS
                    "[]" + //NON-NLS
                    "[]" + //NON-NLS
                    "[]" + //NON-NLS
                    "[]")); //NON-NLS

                //======== layeredPane1 ========
                {
                    this.layeredPane1.setAlignmentX(0.0F);
                    this.layeredPane1.setAlignmentY(0.0F);
                    this.layeredPane1.setName("layeredPane1"); //NON-NLS
                }
                this.contentPanel.add(this.layeredPane1, "cell 0 0"); //NON-NLS

                //---- labelClass ----
                this.labelClass.setText("Position"); //NON-NLS
                this.labelClass.setForeground(Color.white);
                this.labelClass.setBackground(new Color(51, 51, 255));
                this.labelClass.setOpaque(true);
                this.labelClass.setFont(new Font("Tahoma", Font.BOLD, 12)); //NON-NLS
                this.labelClass.setHorizontalAlignment(SwingConstants.CENTER);
                this.labelClass.setName("labelClass"); //NON-NLS
                this.contentPanel.add(this.labelClass, "cell 0 1 3 1"); //NON-NLS

                //======== scrollPane2 ========
                {
                    this.scrollPane2.setName("scrollPane2"); //NON-NLS

                    //======== scrollPane1 ========
                    {
                        this.scrollPane1.setName("scrollPane1"); //NON-NLS

                        //---- textAreaPoint3D ----
                        this.textAreaPoint3D.setFont(new Font("Tahoma", Font.BOLD, 12)); //NON-NLS
                        this.textAreaPoint3D.setEnabled(false);
                        this.textAreaPoint3D.setName("textAreaPoint3D"); //NON-NLS
                        this.textAreaPoint3D.addPropertyChangeListener(e -> textAreaPoint3DPropertyChange(e));
                        this.scrollPane1.setViewportView(this.textAreaPoint3D);
                    }
                    this.scrollPane2.setViewportView(this.scrollPane1);
                }
                this.contentPanel.add(this.scrollPane2, "cell 0 2 3 1"); //NON-NLS

                //---- label2 ----
                this.label2.setText("X"); //NON-NLS
                this.label2.setLabelFor(this.textFieldX);
                this.label2.setName("label2"); //NON-NLS
                this.contentPanel.add(this.label2, "cell 0 3"); //NON-NLS

                //---- textFieldX ----
                this.textFieldX.setName("textFieldX"); //NON-NLS
                this.textFieldX.addActionListener(e -> textFieldXYZActionPerformed(e));
                this.contentPanel.add(this.textFieldX, "cell 0 3"); //NON-NLS

                //---- label3 ----
                this.label3.setText("Y"); //NON-NLS
                this.label3.setLabelFor(this.textFieldY);
                this.label3.setName("label3"); //NON-NLS
                this.contentPanel.add(this.label3, "cell 1 3"); //NON-NLS

                //---- textFieldY ----
                this.textFieldY.setName("textFieldY"); //NON-NLS
                this.textFieldY.addActionListener(e -> textFieldXYZActionPerformed(e));
                this.contentPanel.add(this.textFieldY, "cell 1 3"); //NON-NLS

                //---- label4 ----
                this.label4.setText("Z"); //NON-NLS
                this.label4.setLabelFor(this.textFieldZ);
                this.label4.setName("label4"); //NON-NLS
                this.contentPanel.add(this.label4, "cell 2 3"); //NON-NLS

                //---- textFieldZ ----
                this.textFieldZ.setName("textFieldZ"); //NON-NLS
                this.textFieldZ.addActionListener(e -> textFieldXYZActionPerformed(e));
                this.contentPanel.add(this.textFieldZ, "cell 2 3"); //NON-NLS

                //---- label1 ----
                this.label1.setText("Rotation"); //NON-NLS
                this.label1.setBackground(new Color(0, 51, 255));
                this.label1.setOpaque(true);
                this.label1.setForeground(Color.white);
                this.label1.setHorizontalAlignment(SwingConstants.CENTER);
                this.label1.setName("label1"); //NON-NLS
                this.contentPanel.add(this.label1, "cell 0 4 3 1"); //NON-NLS

                //======== scrollPane3 ========
                {
                    this.scrollPane3.setName("scrollPane3"); //NON-NLS

                    //---- textAreaMatrix33 ----
                    this.textAreaMatrix33.setRows(3);
                    this.textAreaMatrix33.setEnabled(false);
                    this.textAreaMatrix33.setName("textAreaMatrix33"); //NON-NLS
                    this.scrollPane3.setViewportView(this.textAreaMatrix33);
                }
                this.contentPanel.add(this.scrollPane3, "cell 0 5 3 3"); //NON-NLS

                //---- textField00 ----
                this.textField00.setName("textField00"); //NON-NLS
                this.textField00.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                this.contentPanel.add(this.textField00, "cell 0 8"); //NON-NLS

                //---- textField01 ----
                this.textField01.setName("textField01"); //NON-NLS
                this.textField01.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                this.contentPanel.add(this.textField01, "cell 1 8"); //NON-NLS

                //---- textField02 ----
                this.textField02.setName("textField02"); //NON-NLS
                this.textField02.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                this.contentPanel.add(this.textField02, "cell 2 8"); //NON-NLS

                //---- textField10 ----
                this.textField10.setName("textField10"); //NON-NLS
                this.textField10.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                this.contentPanel.add(this.textField10, "cell 0 9"); //NON-NLS

                //---- textField11 ----
                this.textField11.setName("textField11"); //NON-NLS
                this.textField11.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                this.contentPanel.add(this.textField11, "cell 1 9"); //NON-NLS

                //---- textField12 ----
                this.textField12.setName("textField12"); //NON-NLS
                this.textField12.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                this.contentPanel.add(this.textField12, "cell 2 9"); //NON-NLS

                //---- textField20 ----
                this.textField20.setName("textField20"); //NON-NLS
                this.textField20.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                this.contentPanel.add(this.textField20, "cell 0 10"); //NON-NLS

                //---- textField21 ----
                this.textField21.setName("textField21"); //NON-NLS
                this.textField21.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                this.contentPanel.add(this.textField21, "cell 1 10"); //NON-NLS

                //---- textField22 ----
                this.textField22.setName("textField22"); //NON-NLS
                this.textField22.addActionListener(e -> textFieldsMatrixActionPerformed(e));
                this.contentPanel.add(this.textField22, "cell 2 10"); //NON-NLS

                //---- label5 ----
                this.label5.setText("Scale"); //NON-NLS
                this.label5.setBackground(new Color(0, 51, 255));
                this.label5.setOpaque(true);
                this.label5.setForeground(Color.white);
                this.label5.setHorizontalAlignment(SwingConstants.CENTER);
                this.label5.setName("label5"); //NON-NLS
                this.contentPanel.add(this.label5, "cell 0 11 3 1"); //NON-NLS

                //---- textFieldScaleX ----
                this.textFieldScaleX.setName("textFieldScaleX"); //NON-NLS
                this.contentPanel.add(this.textFieldScaleX, "cell 0 12"); //NON-NLS

                //---- textFieldScaleY ----
                this.textFieldScaleY.setName("textFieldScaleY"); //NON-NLS
                this.contentPanel.add(this.textFieldScaleY, "cell 1 12"); //NON-NLS

                //---- textFieldScaleZ ----
                this.textFieldScaleZ.setName("textFieldScaleZ"); //NON-NLS
                this.contentPanel.add(this.textFieldScaleZ, "cell 2 12"); //NON-NLS

                //---- button1 ----
                this.button1.setText("Ok"); //NON-NLS
                this.button1.setName("button1"); //NON-NLS
                this.button1.addActionListener(e -> {
			button1ActionPerformed(e);
			buttonOkActionPerformed(e);
		});
                this.contentPanel.add(this.button1, "cell 0 13"); //NON-NLS
            }
            this.dialogPane.add(this.contentPanel, BorderLayout.CENTER);
        }
        add(this.dialogPane, BorderLayout.CENTER);
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
