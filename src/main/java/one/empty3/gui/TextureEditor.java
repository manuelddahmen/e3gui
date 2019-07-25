/*
 * Created by JFormDesigner on Thu Jul 25 04:19:30 CEST 2019
 */

package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.*;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * @author Manuel Dahmen
 */
public class TextureEditor extends JPanel {
    private MyObservableList<File> fileList = new MyObservableList<>();
    private Color currentColor;
    public TextureEditor() {
        initComponents();
    }

    private void comboBox1ActionPerformed(ActionEvent e) {
        if (comboBox1.getSelectedIndex() == 0) {
            TextureCol textureCol = new TextureCol(JColorChooser.showDialog(this, "Choose a Color", currentColor));
            myObservableList1.add(textureCol);

        } else {
            JFileChooser choix = new JFileChooser();
            choix.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    String extension = f.getName().substring(f.getName().lastIndexOf("."));
                    String[] extensions;
                    if (comboBox1.getSelectedIndex() == 1) {
                        extensions = new String[]{"jpg", "jpeg", "png", "gif", "tiff", "ppm"};//...
                    } else {
                        extensions = new String[]{"mpeg", "mpg", "avi", "qt", "mp4"};
                    }
                    return Arrays.stream(extensions).anyMatch(s -> s.equals(extension));
                }

                @Override
                public String getDescription() {
                    if (comboBox1.getSelectedIndex() == 1) {
                        return "Images";
                    } else {
                        return "Vidéos";
                    }
                }
            });
            int retour = choix.showOpenDialog(this);
            if (retour == JFileChooser.APPROVE_OPTION) {
                for (File sel : choix.getSelectedFiles()) {
                    if (comboBox1.getSelectedIndex() == 1) {
                        try {
                            myObservableList1.add(new TextureImg(new ECBufferedImage(ImageIO.read(sel))));
                            fileList.add(sel);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        myObservableList1.add(new TextureMov(sel.getAbsolutePath()));
                        fileList.add(sel);
                    }
                }
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.gui.gui");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        textFieldR = new JTextField();
        button2 = new JButton();
        button1 = new JButton();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        comboBox1 = new JComboBox<>();
        textFieldG = new JTextField();
        textFieldB = new JTextField();
        fileChooser1 = new JFileChooser();
        myObservableList1 = new MyObservableList<>();

        //======== this ========
        setLayout(new BorderLayout());

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
                    "[]"));

                //---- label1 ----
                label1.setText("Texture edtor");
                contentPanel.add(label1, "cell 0 1");

                //---- textFieldR ----
                textFieldR.setToolTipText("red");
                contentPanel.add(textFieldR, "cell 4 1");

                //---- button2 ----
                button2.setText("Apply");
                contentPanel.add(button2, "cell 5 1");

                //---- button1 ----
                button1.setText("Load");
                contentPanel.add(button1, "cell 5 1");

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(table1);
                }
                contentPanel.add(scrollPane1, "cell 6 1 4 5");

                //---- comboBox1 ----
                comboBox1.setDoubleBuffered(true);
                comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                    "Color",
                    "Image",
                    "Video"
                }));
                comboBox1.addActionListener(e -> {
			comboBox1ActionPerformed(e);
			comboBox1ActionPerformed(e);
		});
                contentPanel.add(comboBox1, "cell 0 2");

                //---- textFieldG ----
                textFieldG.setToolTipText("green");
                contentPanel.add(textFieldG, "cell 4 2");

                //---- textFieldB ----
                textFieldB.setToolTipText("blue");
                contentPanel.add(textFieldB, "cell 4 4");
                contentPanel.add(fileChooser1, "cell 0 5 5 1");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        add(dialogPane, BorderLayout.CENTER);

        //---- bindings ----
        bindingGroup = new BindingGroup();
        bindingGroup.addBinding(SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE,
            myObservableList1, table1));
        {
            JTableBinding binding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE,
                myObservableList1, table1);
            binding.addColumnBinding(BeanProperty.create("name"))
                .setColumnName(bundle.getString("LoadTexture.table1.columnName.1"))
                .setColumnClass(String.class);
            bindingGroup.addBinding(binding);
        }
        bindingGroup.bind();
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JTextField textFieldR;
    private JButton button2;
    private JButton button1;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JComboBox<String> comboBox1;
    private JTextField textFieldG;
    private JTextField textFieldB;
    private JFileChooser fileChooser1;
    private MyObservableList<ITexture> myObservableList1;
    private BindingGroup bindingGroup;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
