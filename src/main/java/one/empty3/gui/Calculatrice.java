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
 * Created by JFormDesigner on Sun Jun 30 18:42:27 CEST 2019
 */

package one.empty3.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

/**
 * @author Manuel Dahmen
 */
public class Calculatrice extends JDialog {
    private final JTextField textField0;

    public Calculatrice(Window owner, JTextField text) {
        super(owner);
        initComponents();
        this.textField1.setText(text.getText());
        this.textField0 = text;
   }
    private void appendEventButtonActionPerformed(ActionEvent e) {
        boolean del = false;
        String buttonText = ((JButton) e.getSource()).getText();
        if(buttonText.equals("DELETE"))
        {
            del = true;
            buttonText = "";
        }
        if(textField1.getSelectedText()!=null)
            textField1.replaceSelection(del?"":buttonText);
        else {
            int caretPosition = this.textField1.getCaretPosition();
            String substring1 = this.textField1.getText().substring(0, caretPosition);
            if (del && substring1.length() > 0)
                substring1 = substring1.substring(0, substring1.length() - 1);

            String substring2 = this.textField1.getText().substring(caretPosition);
            textField1.setText(substring1 + buttonText + substring2);
        }

    }

    private void okButtonActionPerformed(ActionEvent e) {
        textField0.setText(textField1.getText());

        this.dispose();
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void buttonFunctionActionPerformed(ActionEvent e) {
        //TODO new MathFunction();
    }

    private void listFunctionMouseClicked(MouseEvent e) {
        this.textField1.setText(this.textField1.getText()+ listFunction.getSelectedValue().toString());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.gui.gui");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        textField1 = new JTextField();
        button2 = new JButton();
        button3 = new JButton();
        button1 = new JButton();
        button8 = new JButton();
        button7 = new JButton();
        button6 = new JButton();
        button9 = new JButton();
        button5 = new JButton();
        button4 = new JButton();
        button0 = new JButton();
        buttonPoint = new JButton();
        buttonDEL = new JButton();
        button13 = new JButton();
        button12 = new JButton();
        button11 = new JButton();
        button10 = new JButton();
        button14 = new JButton();
        button15 = new JButton();
        button16 = new JButton();
        button17 = new JButton();
        scrollPane1 = new JScrollPane();
        listFunction = new JList<>();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        helpButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new MigLayout(
                "insets 0,hidemode 3,gap 0 0",
                // columns
                "[grow,fill]",
                // rows
                "[grow,fill]" +
                "[fill]"));

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "fill,insets dialog,hidemode 3",
                    // columns
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
                    "[]"));
                contentPanel.add(textField1, "cell 0 0");

                //---- button2 ----
                button2.setText("1");
                button2.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button2, "cell 0 1");

                //---- button3 ----
                button3.setText("2");
                button3.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button3, "cell 0 1");

                //---- button1 ----
                button1.setText(bundle.getString("Calculatrice.button1.text"));
                button1.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button1, "cell 0 1");

                //---- button8 ----
                button8.setText(bundle.getString("Calculatrice.button8.text"));
                button8.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button8, "cell 0 2");

                //---- button7 ----
                button7.setText(bundle.getString("Calculatrice.button7.text"));
                button7.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button7, "cell 0 2");

                //---- button6 ----
                button6.setText(bundle.getString("Calculatrice.button6.text"));
                button6.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button6, "cell 0 2");

                //---- button9 ----
                button9.setText(bundle.getString("Calculatrice.button9.text"));
                button9.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button9, "cell 0 3");

                //---- button5 ----
                button5.setText(bundle.getString("Calculatrice.button5.text"));
                button5.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button5, "cell 0 3");

                //---- button4 ----
                button4.setText(bundle.getString("Calculatrice.button4.text"));
                button4.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button4, "cell 0 3");

                //---- button0 ----
                button0.setText(bundle.getString("Calculatrice.button0.text"));
                button0.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button0, "cell 0 4");

                //---- buttonPoint ----
                buttonPoint.setText(bundle.getString("Calculatrice.buttonPoint.text"));
                buttonPoint.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(buttonPoint, "cell 0 4");

                //---- buttonDEL ----
                buttonDEL.setText(bundle.getString("Calculatrice.buttonDEL.text"));
                buttonDEL.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(buttonDEL, "cell 0 4");

                //---- button13 ----
                button13.setText(bundle.getString("Calculatrice.button13.text"));
                button13.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button13, "cell 0 5");

                //---- button12 ----
                button12.setText(bundle.getString("Calculatrice.button12.text"));
                button12.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button12, "cell 0 5");

                //---- button11 ----
                button11.setText(bundle.getString("Calculatrice.button11.text"));
                button11.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button11, "cell 0 5");

                //---- button10 ----
                button10.setText(bundle.getString("Calculatrice.button10.text"));
                button10.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button10, "cell 0 5");

                //---- button14 ----
                button14.setText("u");
                button14.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button14, "cell 0 6");

                //---- button15 ----
                button15.setText("v");
                button15.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button15, "cell 0 6");

                //---- button16 ----
                button16.setText("(");
                button16.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button16, "cell 0 7");

                //---- button17 ----
                button17.setText(")");
                button17.addActionListener(e -> appendEventButtonActionPerformed(e));
                contentPanel.add(button17, "cell 0 7");

                //======== scrollPane1 ========
                {

                    //---- listFunction ----
                    listFunction.setModel(new AbstractListModel<String>() {
                        String[] values = {
                            "sin",
                            "cos",
                            "tan",
                            "atan",
                            "**",
                            "log10",
                            "log",
                            "random"
                        };
                        @Override
                        public int getSize() { return values.length; }
                        @Override
                        public String getElementAt(int i) { return values[i]; }
                    });
                    listFunction.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            listFunctionMouseClicked(e);
                        }
                    });
                    scrollPane1.setViewportView(listFunction);
                }
                contentPanel.add(scrollPane1, "cell 0 7");
            }
            dialogPane.add(contentPanel, "cell 0 0");

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
                okButton.setText(bundle.getString("Calculatrice.okButton.text"));
                okButton.addActionListener(e -> okButtonActionPerformed(e));
                buttonBar.add(okButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText(bundle.getString("Calculatrice.cancelButton.text"));
                cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
                buttonBar.add(cancelButton, "cell 1 0");

                //---- helpButton ----
                helpButton.setText(bundle.getString("Calculatrice.helpButton.text"));
                buttonBar.add(helpButton, "cell 2 0");
            }
            dialogPane.add(buttonBar, "cell 0 1");
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JTextField textField1;
    private JButton button2;
    private JButton button3;
    private JButton button1;
    private JButton button8;
    private JButton button7;
    private JButton button6;
    private JButton button9;
    private JButton button5;
    private JButton button4;
    private JButton button0;
    private JButton buttonPoint;
    private JButton buttonDEL;
    private JButton button13;
    private JButton button12;
    private JButton button11;
    private JButton button10;
    private JButton button14;
    private JButton button15;
    private JButton button16;
    private JButton button17;
    private JScrollPane scrollPane1;
    private JList<String> listFunction;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    private JButton helpButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
