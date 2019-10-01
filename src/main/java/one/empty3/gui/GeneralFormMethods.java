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

import one.empty3.library.ITexture;
import one.empty3.library.Point3D;
import one.empty3.library.TextureCol;

import javax.swing.*;
import java.awt.*;

/**
 * Created by manue on 11-07-19.
 */
public class GeneralFormMethods {
    public static double getD(JTextField f)
    {
        return Double.parseDouble(f.getText());
    }
    public static int getInt(JTextField f)
    {
        return Integer.parseInt(f.getText());
    }
    public static Point3D getP(JTextField [] f)
    {
        return new Point3D(getD(f[0]), getD(f[1]), getD(f[2]));
    }
    public static ITexture getTextColInt(JTextField [] f)
    {
        return new TextureCol(new Color(getInt(f[0]), getInt(f[0]), getInt(f[0])));
    }
    /*
    public static ITexture getTextImg(JTextField [] f)
    {

    }
    public static ITexture getTextMov(JTextField [] f)
    {

    }
    */
}
