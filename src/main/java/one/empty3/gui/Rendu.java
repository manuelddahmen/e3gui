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

import one.empty3.library.Representable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by manue on 26-11-19.
 */
public class Rendu extends JLabel
        implements ListCellRenderer {
    ImageIcon icon;
    ImageIcon selectIcon;
    Color selectCouleur = Color.RED;

    public Rendu() {
        //icon = new ImageIcon(getClass().getResource("img1.gif"));
        //selectIcon  = new ImageIcon(getClass().getResource("img2.gif"));
    }

    public Component getListCellRendererComponent(JList list,
                                                  Object value, // valeur à afficher
                                                  int index, // indice d'item
                                                  boolean isSelected, // l'item est-il sélectionné
                                                  boolean cellHasFocus) // La liste a-t-elle le focus
    {
        String s = value.toString();
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(selectCouleur);
            setText(s + "  " + index);
            //setIcon(selectIcon);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            setText(s);
            //setIcon(icon);
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}

