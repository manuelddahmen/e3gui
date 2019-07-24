package one.empty3.gui;

import one.empty3.library.*;
import one.empty3.library.Polygon;
import one.empty3.library.core.nurbs.CourbeParametriquePolynomialeBezier;
import one.empty3.library.core.nurbs.SurfaceParametriquePolynomialeBezier;
import one.empty3.library.core.raytracer.tree.AlgebraicFormulaSyntaxException;
import one.empty3.library.core.raytracer.tree.AlgebricTree;
import one.empty3.library.core.raytracer.tree.TreeNodeEvalException;
import one.empty3.library.core.script.InterpreteException;
import one.empty3.library.core.script.InterpreteMatrix33;
import one.empty3.library.core.script.InterpretePoint3D;
import one.empty3.library.core.tribase.TubulaireN2cc;

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
        add("scene", Scene.class);
        add("p", Point3D.class);
        add("r", Representable.class);
        add("rContainer", RepresentableConteneur.class);
        add("line", LineSegment.class);
        add("bezier", CourbeParametriquePolynomialeBezier.class);
        add("bezier2", SurfaceParametriquePolynomialeBezier.class);
        add("t", TRI.class);
        add("polygon", Polygon.class);
        add("polyline", PolyLine.class);
        add("sphere", Sphere.class);
        add("tube", TubulaireN2cc.class);
        //add("paramCurve", ParametricCurve.class);
        //add("paramSurface", ParametricSurface.class);

        return listClasses;
    }
    public static void initScenes()
    {
        scenes = new JTree();
        scenes.add(new Scene().toString(), new JTextField(new Scene().toString()));
        isInitScenes = true;
    }
    public static JTree getScenes()
    {
        if(!isInitScenes)
            initScenes();
        return scenes;
    }

    public static Point3D pointParse(String x, String y, String z) throws AlgebraicFormulaSyntaxException, TreeNodeEvalException {
            Map<String, Double> hashMap = new HashMap<String, Double>();

            AlgebricTree treeX = new AlgebricTree(x, hashMap);
            treeX.construct();
            AlgebricTree treeY = new AlgebricTree(y, hashMap);
            treeY.construct();
            AlgebricTree treeZ = new AlgebricTree(z, hashMap);
            treeZ.construct();

            return new Point3D((Double)treeX.eval(), (Double)treeY.eval(), (Double)treeZ.eval());
    }
    public static Matrix33 loadMatrix(Matrix33 m, JTextField[] strings, JTextArea text) throws AlgebraicFormulaSyntaxException, TreeNodeEvalException {
        for(int i=0; i<strings.length; i++) {
            AlgebricTree treeI = new AlgebricTree(strings[i].getText());
            treeI.construct();
            m.set(i/3, i%3, (double)treeI.eval());
            strings[i].setText(""+m.get(i/3, i%3));
            text.setText(m.toString());
        }
        return m;
    }


    public static Point3D pointParse(String toStringRepresentation) throws InterpreteException {
        InterpretePoint3D interpretePoint3D = new InterpretePoint3D();
        return (Point3D) interpretePoint3D.interprete(toStringRepresentation, 0);
    }

    public static Matrix33 matrixParse(String text) throws InterpreteException {
        InterpreteMatrix33 interpreteMatrix33 = new InterpreteMatrix33();
        return (Matrix33) interpreteMatrix33.interprete(text, 0);
    }

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

    public static void setObjectFields(Point3D point3D, JTextArea textAreaPoint3D, JTextField[] jTextFields, Matrix33 matrix33, JTextArea textAreaMatrix33, JTextField[] jTextFields1, Point3D scale, JTextField textFieldScaleX, JTextField textFieldScaleY, JTextField textFieldScaleZ) {

        textAreaMatrix33.setText(matrix33.toString());
        textAreaPoint3D.setText(point3D.toString());

        for(int i=0; i<jTextFields.length; i++)
            jTextFields[i].setText(""+point3D.get(i));
        for(int i=0; i<jTextFields1.length; i++)
            jTextFields1[i].setText(""+matrix33.get(i/3, i%3));
        textFieldScaleX.setText(""+scale.get(0));
        textFieldScaleY.setText(""+scale.get(1));
        textFieldScaleZ.setText(""+scale.get(2));


    }

    public static void setObjectFieldsCamera(Camera camera, JTextField[] jTextFields) {
        for(int i=0; i<3; i++)
            jTextFields[i].setText(""+ camera.getEye().get(i));
        for(int i=3; i<6; i++)
            jTextFields[i].setText(""+ camera.getLookat().get(i-3));
        // Verticale
        for(int i=9; i<18; i++) jTextFields[i].setText("" + camera.getMatrice().get((i - 9) / 3, (i - 9) % 3));
        jTextFields[18].setText("" + camera.angleX());
        jTextFields[19].setText("" + camera.angleY());
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
