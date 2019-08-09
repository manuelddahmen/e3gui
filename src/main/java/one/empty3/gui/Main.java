package one.empty3.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.library.Scene;
import one.empty3.library.ZBufferImpl;
import one.empty3.library.core.script.ExtensionFichierIncorrecteException;
import one.empty3.library.core.script.Loader;
import one.empty3.library.core.script.VersionNonSupporteeException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
/*
 * Created by JFormDesigner on Thu Jun 27 11:40:57 CEST 2019
 */


/**
 * @author Manuel Dahmen
 */
public class Main implements PropertyChangeListener {
    private boolean drawingPointCoords;
    private Point click;
    private String toDraw;

    public static void main(String [] args)
    {
        Main main = new Main();
    }

    public Main() {
        dataModel = new DataModel();
        initComponents();
        getUpdateView().setFF(this);
        getTextureEditor().setMain(this);
        getEditor().addPropertyChangeListener(this);
        getUpdateView().getzRunner().addPropertyChangeListener(this);
        ThreadDrawingCoords threadDrawingCoords = new ThreadDrawingCoords();
        threadDrawingCoords.start();
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

    private void MainWindowKeyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
        {
            getUpdateView().getzRunner().setStopCurrentRender(true);
        }
    }

    private void updateViewMouseClicked(MouseEvent e) {
            try {
                System.out.println(e.getX());
                System.out.println(e.getY());
                Point3D point3D = getUpdateView().getzRunner().getZBufferImpl().clickAt(
                        (int)e.getX(), (int)e.getY());

                if(point3D==null && point3D.equals(ZBufferImpl.INFINI)) {
                    toDraw = "background(texture)";
                }else{
            String[] ps = new String[3];
            for (int i = 0; i < 3; i++)
                ps[i] = String.format("%.3f", point3D.get(i));
            toDraw = "p3(" + ps[0] + ", " + ps[1] + ", " + ps[2] + ")";
        }
                click = e.getPoint();
                drawingPointCoords = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

    }
    class ThreadDrawingCoords  extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    if(drawingPointCoords) {
                        Graphics graphics = getUpdateView().getzRunner().getLastImage().getGraphics();
                        graphics.setColor(Color.BLACK);
                        graphics.drawBytes(toDraw.getBytes(), 0, toDraw.getBytes().length, click.x, click.y);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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
        this.objectEditorBase1 = new ObjectEditorBase();
        this.panel2 = new JPanel();
        this.labelX = new JLabel();
        this.textFieldXres = new JTextField();
        this.labelY = new JLabel();
        this.textFieldYres = new JTextField();
        this.label3 = new JLabel();
        this.comboBox1 = new JComboBox<>();
        this.buttonRender = new JButton();
        this.buttonSaveR = new JButton();
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
            this.MainWindow.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    MainWindowKeyPressed(e);
                }
            });
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
            MainWindowContentPane.add(this.menu1, "pad 5,cell 0 0 2 3,aligny top,growy 0,wmin 100,hmin 20");

            //---- editor ----
            this.editor.addPropertyChangeListener(e -> editorPropertyChange(e));
            MainWindowContentPane.add(this.editor, "cell 0 2 2 3,dock center");

            //---- updateViewMain ----
            this.updateViewMain.setBackground(new Color(204, 255, 204));
            this.updateViewMain.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    updateViewMouseClicked(e);
                    updateViewMouseClicked(e);
                }
            });
            MainWindowContentPane.add(this.updateViewMain, "cell 0 2 2 3,dock center");

            //======== tabbedPane1 ========
            {
                this.tabbedPane1.addTab("Textures", this.textureEditor1);

                //======== panel1 ========
                {
                    this.panel1.setLayout(new MigLayout(
                        "hidemode 3",
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
                        "[]" +
                        "[]"));
                    this.panel1.add(this.objectEditorBase1, "cell 0 0 6 6,dock center");
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
                        "[]" +
                        "[]"));

                    //---- labelX ----
                    this.labelX.setText("text");
                    this.panel2.add(this.labelX, "cell 0 0");
                    this.panel2.add(this.textFieldXres, "cell 1 0");

                    //---- labelY ----
                    this.labelY.setText("text");
                    this.panel2.add(this.labelY, "cell 0 1");
                    this.panel2.add(this.textFieldYres, "cell 1 1");

                    //---- label3 ----
                    this.label3.setText("Rendering type");
                    this.panel2.add(this.label3, "cell 0 2");

                    //---- comboBox1 ----
                    this.comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                        "All",
                        "Lines",
                        "Points"
                    }));
                    this.panel2.add(this.comboBox1, "cell 1 2");

                    //---- buttonRender ----
                    this.buttonRender.setText("Render");
                    this.buttonRender.setEnabled(false);
                    this.panel2.add(this.buttonRender, "cell 0 3");

                    //---- buttonSaveR ----
                    this.buttonSaveR.setText("Save");
                    this.buttonSaveR.setEnabled(false);
                    this.panel2.add(this.buttonSaveR, "cell 1 3");
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
            MainWindowContentPane.add(this.tabbedPane1, "cell 0 5 2 4,dock center");
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
    private ObjectEditorBase objectEditorBase1;
    private JPanel panel2;
    private JLabel labelX;
    private JTextField textFieldXres;
    private JLabel labelY;
    private JTextField textFieldYres;
    private JLabel label3;
    private JComboBox<String> comboBox1;
    private JButton buttonRender;
    private JButton buttonSaveR;
    private JPanel panel3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    
    
    public REditor getEditor() {
        return editor;
    }
    
    public void setEditor(REditor editor) {
        this.editor = editor;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Logger.getAnonymousLogger().info("Property main changed= "+evt.getPropertyName());
        if (evt.getPropertyName().equals("representable")) {
            Logger.getAnonymousLogger().info("representable changed");
            RepresentableEditor[] representableEditors = {getEditor(), getUpdateView(), getPositionEditor()};
            for (RepresentableEditor representableEditor : representableEditors) {
                if (!evt.getSource().equals(representableEditor)) {
                    representableEditor.initValues((Representable) evt.getNewValue());
                    Logger.getAnonymousLogger().info(representableEditor.getClass().getName()+".initValue()");
                }
            }
        } else if (evt.getPropertyName().equals("renderedImageOK")) {
            if ((boolean) evt.getNewValue()) {
            } else {
                this.MainWindow.setBackground(Color.BLACK);
                try {
                    this.getUpdateView().getzRunner().setLastImage(ImageIO.read(new File("resources/img/RESULT_FAILURE.png")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public RepresentableEditor getPositionEditor() {
        return objectEditorBase1;
    }
}
