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
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manue on 19-11-19.
 */
public class ListModelSelection extends DefaultListModel<Representable> {
    public ListModelSelection(ArrayList<Representable> selectionIn) {
        this.representables = selectionIn;
    }
    List<Representable> representables = new ArrayList<>();
    @Override
    public int getSize() {
        return representables.size();
    }

    @Override
    public Representable getElementAt(int index) {
        return representables.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        super.addListDataListener(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        super.removeListDataListener(l);

    }
    public void add(Representable representable)
    {
        representables.add(representable);
    }

    /**
     * @param o
     */
    @Override
    public void addElement(Representable o)
    {
        super.addElement(o);
        add(o);

    }
}
