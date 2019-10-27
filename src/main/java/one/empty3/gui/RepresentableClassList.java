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

import one.empty3.library.*;
import one.empty3.library.Polygon;
import one.empty3.library.core.nurbs.*;
import one.empty3.library.core.raytracer.tree.AlgebraicFormulaSyntaxException;
import one.empty3.library.core.raytracer.tree.AlgebricTree;
import one.empty3.library.core.raytracer.tree.TreeNodeEvalException;
import one.empty3.library.core.script.InterpreteException;
import one.empty3.library.core.script.InterpretePoint3D;
import one.empty3.library.core.tribase.TRIEllipsoide;
import one.empty3.library.core.tribase.TRIExtrusionGeneralisee;
import one.empty3.library.core.tribase.Tubulaire3;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by manue on 27-06-19.
 */
public class RepresentableClassList {
    private static JTree scenes;
    private static boolean isInitScenes;
    static MyObservableList<ObjectDescription>
     listClasses = new MyObservableList<>();

    public static void add(String name, Class clazz) {
        ObjectDescription objectDescription = new ObjectDescription();
        objectDescription.setName(name);
        objectDescription.setR(clazz);
        listClasses.add(objectDescription);
    }

    public static MyObservableList<ObjectDescription> myList()
    {
        listClasses = new MyObservableList<>();
        add("point", Point3D.class);
        add("container (group)", RepresentableConteneur.class);
        add("line", LineSegment.class);
        add("bezier", CourbeParametriquePolynomialeBezier.class);
        add("bezier2", SurfaceParametriquePolynomialeBezier.class);
        add("triangle", TRI.class);
        add("polygon", Polygon.class);
        add("polyline", PolyLine.class);
        add("sphere", Sphere.class);
        add("tube", Tubulaire3.class);
        add("surface (P = f(u,v))", FunctionSurface.class);
        add("curve   (P = f(u))", FunctionCurve.class);
        add("ellipsoid", TRIEllipsoide.class);
        add("fct y = f(x)", FctXY.class);
        //add("heightSurfaceXYZ", HeightMapSurfaceXYZ.class);
        add("B-Spline", BSpline.class);
        add("LumierePonctuelle", LumierePonctuelle.class);
        add("extrusion", TRIExtrusionGeneralisee.class);
        //add("move", Move.class);
        //add("paramCurve", ParametricCurve.class);
        //add("paramSurface", ParametricSurface.class);

        return listClasses;
    }

    public static Point3D pointParse(String x, String y, String z) throws AlgebraicFormulaSyntaxException, TreeNodeEvalException {
            Map<String, Double> hashMap = new HashMap<String, Double>();

            AlgebricTree treeX = new AlgebricTree(x, hashMap);
            treeX.construct();
            AlgebricTree treeY = new AlgebricTree(y, hashMap);
            treeY.construct();
            AlgebricTree treeZ = new AlgebricTree(z, hashMap);
            treeZ.construct();

        Point3D point3D = new Point3D((Double) treeX.eval(), (Double) treeY.eval(), (Double) treeZ.eval());

        return point3D;
    }


    public static Point3D pointParse(String toStringRepresentation) throws InterpreteException {
        InterpretePoint3D interpretePoint3D = new InterpretePoint3D();
        return (Point3D) interpretePoint3D.interprete(toStringRepresentation, 0);
    }
/*
    public static Matrix33 matrixParse(String text) throws InterpreteException {
        InterpreteMatrix33 interpreteMatrix33 = new InterpreteMatrix33();
        return (Matrix33) interpreteMatrix33.interprete(text, 0);
    }
*/
    public static Matrix33 matrixParse(JTextField[] strings) throws AlgebraicFormulaSyntaxException, TreeNodeEvalException {
        Matrix33 matrix = new Matrix33();
        for(int i=0; i<strings.length; i++) {
            AlgebricTree treeI = new AlgebricTree(strings[i].getText());
            treeI.construct();
            matrix.set(i/3, i%3, (double)treeI.eval());
            strings[i].setText(""+matrix.get(i/3, i%3));
        }
        return matrix;
    }

    public static void setObjectFields(Representable r,Point3D point3D, JTextArea textAreaPoint3D, JTextField[] jTextFields, Matrix33 matrix33, JTextArea textAreaMatrix33, JTextField[] jTextFields1, Point3D scale, JTextField textFieldScaleX, JTextField textFieldScaleY, JTextField textFieldScaleZ) {

        textAreaMatrix33.setText(matrix33.toString());
        textAreaPoint3D.setText(point3D.toString());

        for(int i=0; i<jTextFields.length; i++)
            jTextFields[i].setText(""+String.valueOf(point3D.get(i)));
        for(int i=0; i<jTextFields1.length; i++)
            jTextFields1[i].setText(""+matrix33.get(i/3, i%3));
        textFieldScaleX.setText(""+r.getScale().get(0));
        textFieldScaleY.setText(""+r.getScale().get(1));
        textFieldScaleZ.setText(""+r.getScale().get(2));


    }

    public static void setObjectFieldsCamera(Camera camera, JTextField[] jTextFields) {
        for(int i=0; i<3; i++)
            jTextFields[i].setText(""+ camera.getEye().get(i));
        for(int i=3; i<6; i++)
            jTextFields[i].setText(""+ camera.getLookat().get(i-3));
        // Verticale
        for(int i=9; i<18; i++) jTextFields[i].setText("" + camera.getMatrice().get((i - 9) / 3, (i - 9) % 3));
        jTextFields[18].setText("" + camera.getAngleX());
        jTextFields[19].setText("" + camera.getAngleY());
        jTextFields[20].setText("1.0");
    }

    public static void initTextValues(ITexture texture, JComboBox<String> comboBox1, JFileChooser fileChooser1, JTextField[] jTextFields) {
        if(texture!=null)
        {
            if(texture instanceof TextureCol)
            {
                comboBox1.setSelectedIndex(0);
                float[] comps;
                Color color = new Color(((TextureCol) texture).color());
                jTextFields[0].setText(""+color.getRed());
                jTextFields[1].setText(""+color.getGreen());
                jTextFields[2].setText(""+color.getBlue());
                //jTextFields[3].setText(""+color.getAlpha());
            }
            else if(texture instanceof TextureMov)
            {

            }else if(texture instanceof TextureImg)
            {

            }
        }

    }
}
