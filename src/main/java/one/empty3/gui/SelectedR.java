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

/**
 * Created by manue on 12-10-19.
 */
public class SelectedR {
    public static final int CREATE = 0;
    public static final int SELECT = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;

    private Class selClass = null;
    private Class representableObject = null;
    private int level = -1;
    private Object value = null;

    public SelectedR(Representable representable)
    {

    }
}
