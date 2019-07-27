package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.Scene;
import one.empty3.library.core.script.ExtensionFichierIncorrecteException;
import one.empty3.library.core.script.Loader;
import one.empty3.library.core.script.VersionNonSupporteeException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
/*
 * Created by JFormDesigner on Thu Jun 27 11:40:57 CEST 2019
 */


/**
 * @author Manuel Dahmen
 */
public class Main {
    public static void main(String [] args)
    {
        Main main = new Main();
    }

    public Main() {
        dataModel = new DataModel();
        initComponents();
        getUpdateView().setFF(this);
        getTextureEditor().setMain(this);
    }
    
    private TextureEditor getTextureEditor() {
        return textureEditor1;
    }
    
    public DataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public UpdateViewMain getUpdateView() {
        return updateViewMain;
    }

    public void setUpdateView(UpdateViewMain updateView) {
        this.updateViewMain = updateView;
    }

    private DataModel dataModel;


    private void menuItemNewActionPerformed(ActionEvent e) {
        dataModel = new DataModel();
        dataModel.setScene(new Scene());
    }

    private void ScriptPanelPropertyChange(PropertyChangeEvent e) {
        // TODO add your code here
    }

    private void menuItemLoadActionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser(".");
        jFileChooser.setDialogTitle("Open file");
        jFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        jFileChooser.showDialog(MainWindow, "Load");
        File selectedFile = jFileChooser.getSelectedFile();
        Scene scene = new Scene();
        try {
            new Loader().load(selectedFile, scene);
            dataModel = new DataModel();
            dataModel.setScene(scene);
        } catch (VersionNonSupporteeException e1) {
            e1.printStackTrace();
        } catch (ExtensionFichierIncorrecteException e1) {
            e1.printStackTrace();
        }
    }

    private void menuItemSaveActionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser(".");
        jFileChooser.setDialogTitle("Save file");
        jFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        jFileChooser.showDialog(MainWindow, "Save");
        File selectedFile = jFileChooser.getSelectedFile();
        Scene scene = new Scene();
        try {
            new Loader().saveTxt(selectedFile, scene);
            dataModel = new DataModel();
            dataModel.setScene(scene);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void editorPropertyChange(PropertyChangeEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        this.MainWindow = new JFrame();
        this.menu1 = new JMenu();
        this.menuItemNew = new JMenuItem();
        this.menuItemLoad = new JMenuItem();
        this.menuItemSave = new JMenuItem();
        this.editor = new REditor(dataModel.getScene());
        this.updateViewMain = new UpdateViewMain();
        this.tabbedPane1 = new JTabbedPane();
        this.textureEditor1 = new TextureEditor();
        this.panel1 = new JPanel();
        this.panel2 = new JPanel();
        this.panel3 = new JPanel();

        //======== MainWindow ========
        {
            this.MainWindow.setTitle("Main");
            this.MainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.MainWindow.setBackground(new Color(204, 255, 255));
            this.MainWindow.setVisible(true);
            this.MainWindow.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
            this.MainWindow.setMinimumSize(new Dimension(1000, 400));
            this.MainWindow.setIconImage(new ImageIcon(getClass().getResource("/one/empty3/library/mite.png")).getImage());
            this.MainWindow.addPropertyChangeListener("fieldFunctionsPropertyChanged", e -> ScriptPanelPropertyChange(e));
            Container MainWindowContentPane = this.MainWindow.getContentPane();
            MainWindowContentPane.setLayout(new MigLayout(
                "fill,hidemode 3",
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
                "[]"));

            //======== menu1 ========
            {
                this.menu1.setText("File");

                //---- menuItemNew ----
                this.menuItemNew.setText("New");
                this.menuItemNew.addActionListener(e -> {
			menuItemNewActionPerformed(e);
			menuItemNewActionPerformed(e);
		});
                this.menu1.add(this.menuItemNew);

                //---- menuItemLoad ----
                this.menuItemLoad.setText("Load");
                this.menuItemLoad.addActionListener(e -> menuItemLoadActionPerformed(e));
                this.menu1.add(this.menuItemLoad);

                //---- menuItemSave ----
                this.menuItemSave.setText("Save");
                this.menuItemSave.addActionListener(e -> menuItemSaveActionPerformed(e));
                this.menu1.add(this.menuItemSave);
            }
            MainWindowContentPane.add(this.menu1, "cell 0 0 2 4,aligny top,growy 0,wmin 100,hmin 20");

            //---- editor ----
            this.editor.addPropertyChangeListener(e -> editorPropertyChange(e));
            MainWindowContentPane.add(this.editor, "cell 0 1 1 3,dock center");

            //---- updateViewMain ----
            this.updateViewMain.setBackground(new Color(204, 255, 204));
            MainWindowContentPane.add(this.updateViewMain, "cell 0 1 1 3,dock center");

            //======== tabbedPane1 ========
            {
                this.tabbedPane1.addTab("Textures", this.textureEditor1);

                //======== panel1 ========
                {
                    this.panel1.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]"));
                }
                this.tabbedPane1.addTab("Position", this.panel1);

                //======== panel2 ========
                {
                    this.panel2.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]"));
                }
                this.tabbedPane1.addTab("Rendu", this.panel2);

                //======== panel3 ========
                {
                    this.panel3.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]"));
                }
                this.tabbedPane1.addTab("Draw Over 2D", this.panel3);
            }
            MainWindowContentPane.add(this.tabbedPane1, "cell 0 4 2 4,dock center");
            this.MainWindow.setSize(1620, 1085);
            this.MainWindow.setLocationRelativeTo(this.MainWindow.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JFrame MainWindow;
    private JMenu menu1;
    private JMenuItem menuItemNew;
    private JMenuItem menuItemLoad;
    private JMenuItem menuItemSave;
    private REditor editor;
    private UpdateViewMain updateViewMain;
    private JTabbedPane tabbedPane1;
    private TextureEditor textureEditor1;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    
    
    public REditor getEditor() {
        return editor;
    }
    
    public void setEditor(REditor editor) {
        this.editor = editor;
    }
}
