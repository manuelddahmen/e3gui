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

package one.empty3.gui;

import javax.swing.*;

/**
 * Created by manue on 27-06-19.
 */
public class UpdateRepresentableClassList extends JTable
{
    public UpdateRepresentableClassList()
    {
        super();
    }
    private RepresentableClassList representableClassList;

    public RepresentableClassList getRepresentableClassList() {
        return representableClassList;
    }

    public void setRepresentableClassList(RepresentableClassList representableClassList) {
        firePropertyChange("classList", this.representableClassList, representableClassList);
        this.representableClassList = representableClassList;
    }
}
