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

import one.empty3.library.MatrixPropertiesObject;

/**
 * Created by manue on 06-10-19.
 */
public class ClassesNotEqualException extends Exception {
    public ClassesNotEqualException(Class<?> aClass1, Class<? extends MatrixPropertiesObject> aClass) {
        System.err.println(this.getClass().getName());
        System.err.println("Class A (element class attribute) : " + (aClass1==null?"NULL": aClass1.getCanonicalName()));
        System.err.println("Class B (MatrixPropertiesObject)  : " +( aClass==null?"NULL":aClass.getCanonicalName()));
    }
}
