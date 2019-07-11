package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.ZBufferImpl;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.beans.PropertyChangeEvent;
/*
 * Created by JFormDesigner on Thu Jun 20 05:36:16 CEST 2019
 */


/**
 * @author Manuel Dahmen
 */
public class FormFunction  extends JFrame {
    public static void main(String ... args)
    {
        FormFunction ff = new FormFunction();
        ff.initAll();
    }

    public FormFunction()
    {
    }
    public void initAll()
    {
        initComponents();

    }
    private void CreateRepresentableObjectActionPerformed(ActionEvent e) {
        one.empty3.gui.ObjectsView objectsView = new one.empty3.gui.ObjectsView();
        objectsView.setVisible(true);

    }

    private void AddRepresentableObjectActionPerformed(ActionEvent e) {
        one.empty3.gui.ObjectsView objectsView = new one.empty3.gui.ObjectsView();
    }


    public one.empty3.gui.UpdateView getPanelView3D() {
        return this.panelView3D;
    }

    private void xyzLabelActionPerformed(ActionEvent e) {
        JTextField text = null;
        switch(((JButton)e.getSource()).getText())
        {
            case "x=":
                text = textFctX;
                break;
            case "y=":
                text = textFctY;
                break;
            case "z=":
                text = textFctZ;
                break;
            case "[u":
                text = uMin;
                break;
            case "u]":
                text = uMax;
                break;
            case "[v":
                text = vMin;
                break;
            case "v]":
                text = vMax;
                break;
            default:
                System.out.println("Personal Binding Error");
        }
        if(text!=null) {
            one.empty3.gui.Calculatrice calculatrice = new one.empty3.gui.Calculatrice(ScriptPanel, text);
            calculatrice.setVisible(true);
        }
    }

    private void textField1PropertyChange(PropertyChangeEvent e) {
        // TODO add your code here
    }

    private void button1ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void textField2PropertyChange(PropertyChangeEvent e) {
        // TODO add your code here
    }

    private void button2ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void textField3PropertyChange(PropertyChangeEvent e) {
        // TODO add your code here
    }

    private void buttonCancelActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void buttonOKActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void buttonRefreshActionPerformed(ActionEvent e) {

        getPanelView3D().firePropertyChange("refresh", false, true);
    }

    private void buttonAddTextureActionPerformed(ActionEvent e) {
        one.empty3.gui.LoadTexture loadTexture = new one.empty3.gui.LoadTexture(ScriptPanel, this);
        loadTexture.setVisible(true);

    }

    private void buttonCameraActionPerformed(ActionEvent e) {
        one.empty3.gui.Camera3dControl camera3dControl = new one.empty3.gui.Camera3dControl(this, getPanelView3D().getView().getCamera());
        //camera3dControl.add

    }

    private void comboBoxRendererSpeedActionPerformed(ActionEvent e) {
        int zbufferSpeed = 0;
        switch (comboBoxRendererSpeed.getSelectedIndex())
        {
            case 0:
                zbufferSpeed =  ZBufferImpl.DISPLAY_ALL;
                break;
            case 1:
                zbufferSpeed =  ZBufferImpl.DISPLAY_LINES;
                break;
            case 2:
                zbufferSpeed =  ZBufferImpl.DISPLAY_POINTS;
                break;
        }
        getPanelView3D().getView().setzDiplayType(zbufferSpeed);
    }

    private void textFieldZPropertyChange(PropertyChangeEvent e) {
        // TODO add your code here
    }

    private void textFctXActionPerformed(ActionEvent e) {
        getPanelView3D().getView().setxFormula(((JTextField)e.getSource()).getText());
    }
    private void textFctYActionPerformed(ActionEvent e) {
        getPanelView3D().getView().setyFormula(((JTextField)e.getSource()).getText());
    }
    private void textFctZActionPerformed(ActionEvent e) {
        getPanelView3D().getView().setzFormula(((JTextField)e.getSource()).getText());
    }

    private void ScriptPanelPropertyChange(PropertyChangeEvent e) {
        // TODO add your code here
    }

    private void textFctXPropertyChange(PropertyChangeEvent e) {
        // TODO add your code here
    }

    private void panelView3DAncestorResized(HierarchyEvent e) {
    }

    private void panelView3DComponentShown(ComponentEvent e) {
        //getPanelView3D().getView().setzBuffer((ZBufferImpl)ZBufferFactory.instance(e.getComponent().getWidth(), e.getComponent().getHeight()));
    }

    private void panelView3DComponentResized(ComponentEvent e) {
        //getPanelView3D().getView().setzBuffer((ZBufferImpl)ZBufferFactory.instance(e.getComponent().getWidth(), e.getComponent().getHeight()));

    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }



	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        this.ScriptPanel = new JFrame();
        this.menuBar1 = new JMenuBar();
        this.scrollPane1 = new JPanel();
        this.layeredPane1 = new JLayeredPane();
        this.panel1 = new JPanel();
        this.scrollPane3 = new JScrollPane();
        this.tree1 = new JTree();
        this.xLabel = new JButton();
        this.textFctX = new JTextField();
        this.yLabel = new JButton();
        this.textFctY = new JTextField();
        this.zLabel = new JButton();
        this.textFctZ = new JTextField();
        this.label1 = new JButton();
        this.uMin = new JTextField();
        this.panelView3D = new UpdateView();
        this.label2 = new JButton();
        this.uMax = new JTextField();
        this.label3 = new JButton();
        this.vMin = new JTextField();
        this.label4 = new JButton();
        this.vMax = new JTextField();
        this.buttonAddTexture = new JButton();
        this.buttonOK = new JButton();
        this.buttonRefresh = new JButton();
        this.AddRepresentableObject = new JButton();
        this.DeleteRepresentableObject = new JButton();
        this.buttonCamera = new JButton();
        this.scrollPane2 = new JScrollPane();
        this.radioButtonE3Render = new JRadioButton();
        this.radioButtonGL = new JRadioButton();
        this.comboBoxRendererSpeed = new JComboBox<>();
        this.buttonGroup1 = new ButtonGroup();

        //======== ScriptPanel ========
        {
            this.ScriptPanel.setTitle("Form Function");
            this.ScriptPanel.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.ScriptPanel.setBackground(new Color(204, 255, 255));
            this.ScriptPanel.setVisible(true);
            this.ScriptPanel.addPropertyChangeListener("fieldFunctionsPropertyChanged", e -> ScriptPanelPropertyChange(e));
            Container ScriptPanelContentPane = this.ScriptPanel.getContentPane();
            ScriptPanelContentPane.setLayout(new MigLayout(
                "fill,hidemode 3",
                // columns
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]"));
            ScriptPanelContentPane.add(this.menuBar1, "cell 1 0");

            //======== scrollPane1 ========
            {
                this.scrollPane1.setLayout(new MigLayout(
                    "fill,insets 0,hidemode 3",
                    // columns
                    "[fill]",
                    // rows
                    "[fill]"));

                //======== layeredPane1 ========
                {

                    //======== panel1 ========
                    {
                        this.panel1.setLayout(new MigLayout(
                            "fill,hidemode 3",
                            // columns
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
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]"));

                        //======== scrollPane3 ========
                        {
                            this.scrollPane3.setViewportView(this.tree1);
                        }
                        this.panel1.add(this.scrollPane3, "cell 0 0");

                        //---- xLabel ----
                        this.xLabel.setText("x=");
                        this.xLabel.addActionListener(e -> xyzLabelActionPerformed(e));
                        this.panel1.add(this.xLabel, "cell 0 1");

                        //---- textFctX ----
                        this.textFctX.addActionListener(e -> textFctXActionPerformed(e));
                        this.panel1.add(this.textFctX, "cell 0 1");

                        //---- yLabel ----
                        this.yLabel.setText("y=");
                        this.yLabel.addActionListener(e -> xyzLabelActionPerformed(e));
                        this.panel1.add(this.yLabel, "cell 0 2");

                        //---- textFctY ----
                        this.textFctY.addActionListener(e -> textFctYActionPerformed(e));
                        this.panel1.add(this.textFctY, "cell 0 2");

                        //---- zLabel ----
                        this.zLabel.setText("z=");
                        this.zLabel.addActionListener(e -> xyzLabelActionPerformed(e));
                        this.panel1.add(this.zLabel, "cell 0 3");

                        //---- textFctZ ----
                        this.textFctZ.addActionListener(e -> textFctZActionPerformed(e));
                        this.panel1.add(this.textFctZ, "cell 0 3");

                        //---- label1 ----
                        this.label1.setText("[u");
                        this.label1.addActionListener(e -> xyzLabelActionPerformed(e));
                        this.panel1.add(this.label1, "cell 0 4");
                        this.panel1.add(this.uMin, "cell 0 4");

                        //---- panelView3D ----
                        this.panelView3D.setBackground(Color.white);
                        this.panelView3D.addComponentListener(new ComponentAdapter() {
                            @Override
                            public void componentResized(ComponentEvent e) {
                                panelView3DComponentResized(e);
                            }
                            @Override
                            public void componentShown(ComponentEvent e) {
                                panelView3DComponentShown(e);
                            }
                        });
                        this.panel1.add(this.panelView3D, "cell 1 0 1 20,dock center,wmin 600,hmin 600");

                        //---- label2 ----
                        this.label2.setText("u]");
                        this.label2.addActionListener(e -> xyzLabelActionPerformed(e));
                        this.panel1.add(this.label2, "cell 0 5");
                        this.panel1.add(this.uMax, "cell 0 5");

                        //---- label3 ----
                        this.label3.setText("[v");
                        this.label3.addActionListener(e -> xyzLabelActionPerformed(e));
                        this.panel1.add(this.label3, "cell 0 6");
                        this.panel1.add(this.vMin, "cell 0 6");

                        //---- label4 ----
                        this.label4.setText("v]");
                        this.label4.addActionListener(e -> xyzLabelActionPerformed(e));
                        this.panel1.add(this.label4, "cell 0 7");
                        this.panel1.add(this.vMax, "cell 0 7");

                        //---- buttonAddTexture ----
                        this.buttonAddTexture.setText("Texture");
                        this.buttonAddTexture.addActionListener(e -> buttonAddTextureActionPerformed(e));
                        this.panel1.add(this.buttonAddTexture, "cell 0 8 1 1");

                        //---- buttonOK ----
                        this.buttonOK.setText("OK");
                        this.buttonOK.addActionListener(e -> buttonOKActionPerformed(e));
                        this.panel1.add(this.buttonOK, "cell 0 9");

                        //---- buttonRefresh ----
                        this.buttonRefresh.setText("Refresh");
                        this.buttonRefresh.addActionListener(e -> {
			buttonRefreshActionPerformed(e);
			buttonRefreshActionPerformed(e);
			buttonRefreshActionPerformed(e);
		});
                        this.panel1.add(this.buttonRefresh, "cell 0 10");

                        //---- AddRepresentableObject ----
                        this.AddRepresentableObject.setText("Add Representable Object");
                        this.AddRepresentableObject.addActionListener(e -> AddRepresentableObjectActionPerformed(e));
                        this.panel1.add(this.AddRepresentableObject, "cell 0 11");

                        //---- DeleteRepresentableObject ----
                        this.DeleteRepresentableObject.setText("Delete Representable Object");
                        this.panel1.add(this.DeleteRepresentableObject, "cell 0 12");

                        //---- buttonCamera ----
                        this.buttonCamera.setText("Camera 3D controls");
                        this.buttonCamera.addActionListener(e -> buttonCameraActionPerformed(e));
                        this.panel1.add(this.buttonCamera, "cell 0 13");
                        this.panel1.add(this.scrollPane2, "cell 0 13");

                        //---- radioButtonE3Render ----
                        this.radioButtonE3Render.setText("E3 Rendering");
                        this.radioButtonE3Render.setSelected(true);
                        this.panel1.add(this.radioButtonE3Render, "cell 0 14");

                        //---- radioButtonGL ----
                        this.radioButtonGL.setText("JoGL Rendering");
                        this.panel1.add(this.radioButtonGL, "cell 0 15");

                        //---- comboBoxRendererSpeed ----
                        this.comboBoxRendererSpeed.setEditable(true);
                        this.comboBoxRendererSpeed.setModel(new DefaultComboBoxModel<>(new String[] {
                            "All",
                            "Lines",
                            "Points"
                        }));
                        this.comboBoxRendererSpeed.addActionListener(e -> comboBoxRendererSpeedActionPerformed(e));
                        this.panel1.add(this.comboBoxRendererSpeed, "cell 0 17");
                    }
                    this.layeredPane1.add(this.panel1, JLayeredPane.DEFAULT_LAYER);
                    this.panel1.setBounds(0, 0, 1370, 785);
                }
                this.scrollPane1.add(this.layeredPane1, "cell 0 0");
            }
            ScriptPanelContentPane.add(this.scrollPane1, "cell 1 1,dock center,wmin 800,hmin 600");
            this.ScriptPanel.setSize(1415, 930);
            this.ScriptPanel.setLocationRelativeTo(this.ScriptPanel.getOwner());
        }

        //---- buttonGroup1 ----
        this.buttonGroup1.add(this.radioButtonE3Render);
        this.buttonGroup1.add(this.radioButtonGL);

        //---- bindings ----
        this.bindingGroup = new BindingGroup();
        this.bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
            this.panelView3D, BeanProperty.create("view.xFormula"),
            this.textFctX, BeanProperty.create("text")));
        this.bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
            this.panelView3D, BeanProperty.create("view.yFormula"),
            this.textFctY, BeanProperty.create("text")));
        this.bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
            this.panelView3D, BeanProperty.create("view.zFormula"),
            this.textFctZ, BeanProperty.create("text")));
        this.bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
            this.panelView3D, BeanProperty.create("view.rendererType"),
            this.radioButtonE3Render, BeanProperty.create("selected")));
        this.bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
            this.panelView3D, BeanProperty.create("view.rendererType"),
            this.radioButtonGL, BeanProperty.create("selected")));
        this.bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
            this.panelView3D, BeanProperty.create("view.ok"),
            this.buttonOK, BeanProperty.create("selected")));
        this.bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
            this.panelView3D, BeanProperty.create("view.refresh"),
            this.buttonRefresh, BeanProperty.create("selected")));
        this.bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
            this.panelView3D, BeanProperty.create("view.uMin"),
            this.uMin, BeanProperty.create("text")));
        this.bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
            this.panelView3D, BeanProperty.create("view.uMax"),
            this.uMax, BeanProperty.create("text")));
        this.bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
            this.panelView3D, BeanProperty.create("view.vMin"),
            this.vMin, BeanProperty.create("text")));
        this.bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
            this.panelView3D, BeanProperty.create("view.vMax"),
            this.vMax, BeanProperty.create("text")));
        this.bindingGroup.bind();
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JFrame ScriptPanel;
    private JMenuBar menuBar1;
    private JPanel scrollPane1;
    private JLayeredPane layeredPane1;
    private JPanel panel1;
    private JScrollPane scrollPane3;
    private JTree tree1;
    private JButton xLabel;
    private JTextField textFctX;
    private JButton yLabel;
    private JTextField textFctY;
    private JButton zLabel;
    private JTextField textFctZ;
    private JButton label1;
    private JTextField uMin;
    private UpdateView panelView3D;
    private JButton label2;
    private JTextField uMax;
    private JButton label3;
    private JTextField vMin;
    private JButton label4;
    private JTextField vMax;
    private JButton buttonAddTexture;
    private JButton buttonOK;
    private JButton buttonRefresh;
    private JButton AddRepresentableObject;
    private JButton DeleteRepresentableObject;
    private JButton buttonCamera;
    private JScrollPane scrollPane2;
    private JRadioButton radioButtonE3Render;
    private JRadioButton radioButtonGL;
    private JComboBox<String> comboBoxRendererSpeed;
    private ButtonGroup buttonGroup1;
    private BindingGroup bindingGroup;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
