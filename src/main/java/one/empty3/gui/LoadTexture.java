package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ResourceBundle;

/**
 * Created by manue on 04-07-19.
 */
public class LoadTexture extends JDialog {
    private final FormFunction form;
    private ITexture texture;

    public ITexture getTexture() {
        return texture;
    }

    public void setTexture(ITexture texture) {
        this.texture = texture;
    }

    public LoadTexture(Window owner, FormFunction parent)
    {
        super();
        initComponents();
        this.form = parent;
    }
   private void fileChooser1ActionPerformed(ActionEvent e) {
        ITexture texture0= texture;
        switch(comboBox1.getSelectedIndex()) {
            case 0:
                // Color
                try {
                    Color color = new Color(Integer.parseInt(textFieldR.getText()),
                            Integer.parseInt(textFieldG.getText()),
                            Integer.parseInt(textFieldB.getText()));
                    this.texture = new TextureCol(color);
                   this.setVisible(false);
                } catch (Exception ex)
                {ex.printStackTrace();
                return;}
                break;
            case 1:
                // Image
                try {
                    BufferedImage read = ImageIO.read(fileChooser1.getSelectedFile());
                    this.texture = new TextureImg(new ECBufferedImage(read));
                    this.setVisible(false);
                } catch (Exception ex)
                {ex.printStackTrace();return;}
                break;
            case 2:
                try {

                    this.texture = new TextureMov(fileChooser1.getSelectedFile().toURL());
                    this.setVisible(false);
                } catch (Exception ex)
                {ex.printStackTrace();return;}
                break;
        }
       form.getPanelView3D().getView().setTexture(texture);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.gui.gui");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        textFieldR = new JTextField();
        textFieldG = new JTextField();
        textFieldB = new JTextField();
        textField2 = new JTextField();
        textField3 = new JTextField();
        comboBox1 = new JComboBox<>();
        fileChooser1 = new JFileChooser();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
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
                    "insets dialog,hidemode 3",
                    // columns
                    "[fill]" +
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
                    "[]"));

                //---- label1 ----
                label1.setText(bundle.getString("LoadTexture.label1.text"));
                contentPanel.add(label1, "cell 1 1");

                //---- textFieldR ----
                textFieldR.setText("red");
                contentPanel.add(textFieldR, "cell 2 1");

                //---- textFieldG ----
                textFieldG.setToolTipText("green");
                contentPanel.add(textFieldG, "cell 2 1");

                //---- textFieldB ----
                textFieldB.setToolTipText("blue");
                contentPanel.add(textFieldB, "cell 2 1");
                contentPanel.add(textField2, "cell 4 1");
                contentPanel.add(textField3, "cell 5 1");

                //---- comboBox1 ----
                comboBox1.setDoubleBuffered(true);
                comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                    "Color",
                    "Image",
                    "Video"
                }));
                contentPanel.add(comboBox1, "cell 1 2");
                contentPanel.add(fileChooser1, "cell 2 2");
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
                okButton.setText(bundle.getString("LoadTexture.okButton.text"));
                okButton.addActionListener(e -> fileChooser1ActionPerformed(e));
                buttonBar.add(okButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText(bundle.getString("LoadTexture.cancelButton.text"));
                buttonBar.add(cancelButton, "cell 1 0");

                //---- helpButton ----
                helpButton.setText(bundle.getString("LoadTexture.helpButton.text"));
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
    private JLabel label1;
    private JTextField textFieldR;
    private JTextField textFieldG;
    private JTextField textFieldB;
    private JTextField textField2;
    private JTextField textField3;
    private JComboBox<String> comboBox1;
    private JFileChooser fileChooser1;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    private JButton helpButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private final PropertyChangeSupport updateViewSupport = new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
        updateViewSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
        updateViewSupport.addPropertyChangeListener(listener);
    }
}
