package one.empty3.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
/*
 * Created by JFormDesigner on Thu Jun 20 16:49:24 CEST 2019
 */



/**
 * @author Manuel Dahmen
 */
public class MainFrame extends JFrame {
	public MainFrame() {
		initComponents();
	}

	private void buttonObjectsWindowsActionPerformed(ActionEvent e) {
		// TODO add your code here
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        this.menuBar1 = new JMenuBar();
        this.menu1 = new JMenu();
        this.menu2 = new JMenu();
        this.menu3 = new JMenu();
        this.button1 = new JButton();
        this.button2 = new JButton();
        this.button3 = new JButton();
        this.button4 = new JButton();
        this.button5 = new JButton();
        this.button6 = new JButton();
        this.button7 = new JButton();
        this.button8 = new JButton();
        this.button9 = new JButton();
        this.button10 = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "fill,hidemode 3",
            // columns
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //======== menuBar1 ========
        {
            this.menuBar1.add(this.menu1);
            this.menuBar1.add(this.menu2);
            this.menuBar1.add(this.menu3);
        }
        setJMenuBar(this.menuBar1);

        //---- button1 ----
        this.button1.setText("OBJECTS");
        this.button1.setFont(new Font("Verdana", Font.PLAIN, 22));
        this.button1.addActionListener(e -> buttonObjectsWindowsActionPerformed(e));
        contentPane.add(this.button1, "cell 0 0");

        //---- button2 ----
        this.button2.setText("SCENES");
        this.button2.setFont(new Font("Verdana", Font.PLAIN, 22));
        contentPane.add(this.button2, "cell 1 0");

        //---- button3 ----
        this.button3.setText("LAYERS");
        this.button3.setFont(new Font("Verdana", Font.PLAIN, 22));
        contentPane.add(this.button3, "cell 0 1");

        //---- button4 ----
        this.button4.setText("FRAMES");
        this.button4.setFont(new Font("Verdana", Font.PLAIN, 22));
        contentPane.add(this.button4, "cell 1 1");

        //---- button5 ----
        this.button5.setText("TEXTURES");
        this.button5.setFont(new Font("Verdana", Font.PLAIN, 22));
        contentPane.add(this.button5, "cell 0 2");

        //---- button6 ----
        this.button6.setText("SOUNDS");
        this.button6.setFont(new Font("Verdana", Font.PLAIN, 22));
        contentPane.add(this.button6, "cell 1 2");

        //---- button7 ----
        this.button7.setText("PREVIEW");
        this.button7.setFont(new Font("Verdana", Font.PLAIN, 22));
        contentPane.add(this.button7, "cell 0 3");

        //---- button8 ----
        this.button8.setText("RENDERING");
        this.button8.setFont(new Font("Verdana", Font.PLAIN, 22));
        contentPane.add(this.button8, "cell 1 3");

        //---- button9 ----
        this.button9.setText("ENCODING");
        this.button9.setFont(new Font("Verdana", Font.PLAIN, 22));
        contentPane.add(this.button9, "cell 0 4");

        //---- button10 ----
        this.button10.setText("TIMELINE");
        this.button10.setFont(new Font("Verdana", Font.PLAIN, 22));
        contentPane.add(this.button10, "cell 1 4");

        initComponentsI18n();

        pack();
        setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

    private void initComponentsI18n() {
        // JFormDesigner - Component i18n initialization - DO NOT MODIFY  //GEN-BEGIN:initI18n
        ResourceBundle bundle = ResourceBundle.getBundle("gui");
        this.menu1.setText(bundle.getString("menu1.text"));
        this.menu2.setText(bundle.getString("menu2.text"));
        this.menu3.setText(bundle.getString("menu3.text"));
        // JFormDesigner - End of component i18n initialization  //GEN-END:initI18n
    }

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenu menu2;
    private JMenu menu3;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton button10;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
