/*
 *
 *  *  This file is part of Empty3.
 *  *
 *  *     Empty3 is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
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

package one.empty3.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by manue on 27-12-19.
 */
public class RETableRenderer extends DefaultTableCellRenderer {
    String[] columnDescr = {"Nom de la forme 3D\nNom simple",
            "Description\nDescription de la propriété/de l'objet",
            "Entier entre 0 et 2 inclus\nDimension de l'objet: Scalar, Array1D, Array2D",
            "Indice(s) de l'objet dans le tableau\n" +
                    "Dim 0: =0" +
                    "Dim 1: =1 : indice i dans le tableau [1, size]" +
                    "Dim 2: = 2: indice i,j dans le tableau [2, size]"
    };


    private final RPropertyDetailsRow rPropertyRow;

    public RETableRenderer(RPropertyDetailsRow rows) {
        this.rPropertyRow = rows;
    }

    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        // This...
        String pathValue = column<columnDescr.length?columnDescr[column]:"null" + rPropertyRow.getValueAt(row, column); // Could be value.toString()
        c.setToolTipText(pathValue);
        // ...OR this probably works in your case:
        c.setToolTipText(c.getText());
        return c;
    }
}
