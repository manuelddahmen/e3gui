package one.empty3.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by manue on 07-09-19.
 */
public class JTablePopUp extends JTable {

    public String getToolTipText(MouseEvent e) {
        String tip = null;
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);

        try {
            tip = getValueAt(rowIndex, 5).toString();
            ObjectDetailDescription objectDetailDescription = ((RPropertyDetailsRow) this.getModel()).objectDetailDescriptions.get(rowIndex);
            tip += objectDetailDescription.toString();
        } catch (RuntimeException e1) {

        }

        return tip;
    }

    /**
     * @inherited <p>
     */
    @Override
    public JPopupMenu getComponentPopupMenu() {
        Point p = getMousePosition();
        // mouse over table and valid row
        if (p != null && rowAtPoint(p) >= 0) {
            this.changeSelection(rowAtPoint(p), 0, false, false);
            // condition for showing popup triggered by mouse
            return super.getComponentPopupMenu();
        }
        return super.getComponentPopupMenu();
    }
}
