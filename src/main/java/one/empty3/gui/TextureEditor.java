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
 * Created by JFormDesigner on Thu Jul 25 04:19:30 CEST 2019
 */

package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.soap.Text;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Manuel Dahmen
 */
public class TextureEditor extends JPanel {
    class TextureTransferHandler extends TransferHandler
    {
        public class MyTransferHandler extends TransferHandler{

            /**
             * Méthode permettant à l'objet de savoir si les données reçues
             * via un drop sont autorisées à être importées
             * @param info
             * @return boolean
             */
            public boolean canImport(TransferHandler.TransferSupport info) {
                //Nous contrôlons si les données reçues sont d'un type autorisé, ici String
                if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    return false;
                }
                return true;
            }
            /**
             * C'est ici que l'insertion des données dans notre composant est réalisée
             * @param support
             * @return boolean
             */
            public boolean importData(TransferHandler.TransferSupport support){
                //Nous contrôlons si les données reçues sont d'un type autorisé
                if(!canImport(support))
                    return false;

                //On récupère notre objet Transferable, celui qui contient les données en transit
                Transferable data = support.getTransferable();
                File f = null;

                try {
                    //Nous récupérons nos données en spécifiant ce que nous attendons
                    f = (File) data.getTransferData(DataFlavor.javaFileListFlavor);
                } catch (UnsupportedFlavorException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                boolean done = false;

                String[] extensionsImg = new String[]{"jpg", "jpeg", "png", "gif", "tiff", "ppm"};//...
                String[] extensionsMov = new String[]{"mpeg", "mpg", "avi", "qt", "mp4"};
                if(Arrays.stream(extensionsImg).anyMatch(s -> s.equals(extensionsImg)))
                {
                    try {
                        tableModelTexture.getLines().add(new TableModelTexture.ModelLine(f, new TextureImg(
                                new ECBufferedImage(ImageIO.read(f))), TextureImg.class));
                        done = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                if(Arrays.stream(extensionsMov).anyMatch(s -> s.equals(extensionsMov)))
                {
                        TextureMov textureMov = new TextureMov();
                        textureMov.setFile(f);
                        tableModelTexture.getLines().add(new TableModelTexture.ModelLine(f, textureMov,
                                TextureMov.class));
                        done = true;
                }

                return done;
            }
            /**
             * Cette méthode est invoquée à la fin de l'action DROP
             * Si des actions sont à faire ensuite, c'est ici qu'il faudra coder le comportement désiré
             * @param c
             * @param t
             * @param action
             */
            protected void exportDone(JComponent c, Transferable t, int action){}

            public int getSourceActions(JComponent c) {
                return COPY;
            }
        }

    }


    private MyObservableList<File> fileList = new MyObservableList<>();
    private Color currentColor;
    public int choiceTexType = 0;
    private TableModelTexture tableModelTexture;
    private Main main;
    private File currentDirectory = new File(System.getProperties().getProperty("user.home"));
    
    
    public TextureEditor() {
        initComponents();
        tableModelTexture = new TableModelTexture();
        tableModelTexture.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                refreshTable();
            }
        });
        table1.setDragEnabled(true);
        table1.setDropMode(DropMode.INSERT_ROWS);
        table1.setModel(tableModelTexture);
     }

    private void comboBox1ActionPerformed(ActionEvent e) {
        if (choiceTexType == 0) {
            TextureCol textureCol;
            currentColor = JColorChooser.showDialog(this, "Choose a Color", currentColor);
            textureCol = new TextureCol(currentColor);
            tableModelTexture.getLines().add(new TableModelTexture.ModelLine(null, textureCol, textureCol.getClass()));
            getMain().getDataModel().addTexture(textureCol);
        } else {
            JFileChooser choose = new JFileChooser();
            choose.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    String extension = f.getName().substring(f.getName().lastIndexOf(".")+1).toLowerCase();
                    String[] extensions;
                    if (choiceTexType == 1) {
                        extensions = new String[]{"jpg", "jpeg", "png", "gif", "tiff", "ppm"};//...
                    } else {
                        extensions = new String[]{"mpeg", "mpg", "avi", "qt", "mp4"};
                    }
                    return Arrays.stream(extensions).anyMatch(s -> s.equals(extension)||f.isDirectory());
                }

                @Override
                public String getDescription() {
                    if (choiceTexType == 1) {
                        return "Images";
                    } else {
                        return "Vidéos";
                    }
                }
            });
            choose.setCurrentDirectory(currentDirectory);
            int retour = choose.showOpenDialog(this);
            currentDirectory = choose.getCurrentDirectory();
            if (retour == JFileChooser.APPROVE_OPTION) {
                File sel = choose.getSelectedFile();
                {
                    if (choiceTexType == 1) {
                        try {
                            TextureImg textureImg = new TextureImg(new ECBufferedImage(ImageIO.read(sel)));
                            getMain().getDataModel().addTexture(textureImg);
                            tableModelTexture.getLines().add(new TableModelTexture.ModelLine(sel, textureImg, textureImg.getClass()));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        TextureMov textureMov = new TextureMov(sel.getAbsolutePath());
                        getMain().getDataModel().addTexture(textureMov);
                        tableModelTexture.getLines().add(new TableModelTexture.ModelLine(sel, textureMov, textureMov.getClass()));
    
                    }
                }
            }
        }
        tableModelTexture.initTable();
    }

    private void button3ActionPerformed(ActionEvent e) {
        String text = ((JButton) e.getSource()).getText();
        int index = -1;
        switch (text)
        {
            case "Color":
                choiceTexType = 0;
                break;
            case "Image":
                choiceTexType = 1;
                break;
            case "Vidéo":
                choiceTexType = 2;
                break;
        }
        comboBox1ActionPerformed(e);
    }

    private void buttonRefreshActionPerformed(ActionEvent e) {
    
    }

    private void button1ActionPerformed(ActionEvent e) {
        tableModelTexture.initTable();
    }

    private void button2ActionPerformed(ActionEvent e) {
        ((Representable) getMain().getEditor().getRepresentable()).setTexture(tableModelTexture.getLines().get(table1.getSelectedRow()).getiTexture());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        button3 = new JButton();
        button4 = new JButton();
        updateViewMain1 = new UpdateViewMain();
        button2 = new JButton();
        button1 = new JButton();
        button5 = new JButton();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
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
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));

                //---- label1 ----
                label1.setText("Texture edtor");
                contentPanel.add(label1, "cell 0 0");

                //---- button3 ----
                button3.setText("Color");
                button3.addActionListener(e -> button3ActionPerformed(e));
                contentPanel.add(button3, "cell 1 0");

                //---- button4 ----
                button4.setText("Image");
                button4.addActionListener(e -> button3ActionPerformed(e));
                contentPanel.add(button4, "cell 2 0");
                contentPanel.add(updateViewMain1, "cell 3 0 7 11");

                //---- button2 ----
                button2.setText("Apply");
                button2.addActionListener(e -> button2ActionPerformed(e));
                contentPanel.add(button2, "cell 0 1");

                //---- button1 ----
                button1.setText("Refresh");
                button1.addActionListener(e -> buttonRefreshActionPerformed(e));
                contentPanel.add(button1, "cell 1 1");

                //---- button5 ----
                button5.setText("Vid\u00e9o");
                button5.addActionListener(e -> button3ActionPerformed(e));
                contentPanel.add(button5, "cell 2 1");

                //======== scrollPane1 ========
                {
                    scrollPane1.setDoubleBuffered(true);

                    //---- table1 ----
                    table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    table1.setModel(new DefaultTableModel(
                        new Object[][] {
                        },
                        new String[] {
                            "Texture", "Text type"
                        }
                    ));
                    table1.setVerifyInputWhenFocusTarget(false);
                    table1.setUpdateSelectionOnSort(false);
                    table1.setAutoCreateRowSorter(true);
                    scrollPane1.setViewportView(table1);
                }
                contentPanel.add(scrollPane1, "cell 0 3 3 8,dock center");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        add(dialogPane, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JButton button3;
    private JButton button4;
    private UpdateViewMain updateViewMain1;
    private JButton button2;
    private JButton button1;
    private JButton button5;
    private JScrollPane scrollPane1;
    private JTable table1;
    private MyObservableList<ITexture> myObservableList1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public void refreshTable()
    {
        tableModelTexture.initTable();
    }
    
    public void setMain(Main main) {
        
        this.main = main;
    }
    
    
    
    public Main getMain() {
        return main;
    }
}
