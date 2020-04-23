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

 */

package one.empty3.gui;

import com.google.gson.Gson;
//import com.sun.org.apache.regexp.internal.RE;
import com.thoughtworks.xstream.XStream;
//import nu.xom.*;
import one.empty3.library.*;
import one.empty3.library.core.script.Loader;

import javax.imageio.ImageIO;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by manue on 22-07-19.
 */
public class DataModel implements PropertyChangeListener {
    private ArrayList<ITexture> textures = new ArrayList();
    private String fileModel;
    private File newImageFile;
    private String sceneDirectory;

    public DataModel() {
        getDirectory(true);
        Scene scene = new Scene();
        setScene(scene);
        scene.cameraActive(new Camera());
        scene.cameraActive().calculerMatrice(null);
    }

    public DataModel(File inputEcXml) {
        if (!inputEcXml.isDirectory() && inputEcXml.exists()) {
            sceneDirectory = inputEcXml.getParent();
            try {
                Document build = new Builder().build(inputEcXml);
                Element rootElement = build.getRootElement();

                browser(rootElement, new Scene());
            } catch (ParsingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this);
        Logger.getAnonymousLogger().log(Level.INFO, "Model loaded");
    }

    private void browser(Element element, MatrixPropertiesObject representable) {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        Attribute aClass = element.getAttribute("class");
        try {
            Class<?> aClass1 = systemClassLoader.loadClass(aClass.getValue());
            if (aClass1.equals(Scene.class)) {
                this.scene = new Scene();
                representable = scene;
            }
            {
                if (representable instanceof Representable)
                    ((Representable) representable).declareProperties();
                // TODO TEST EQUALITY CLASSES
            }
            for (int i = 0; i < element.getChildElements().size(); i++) {
                Element element1 = element.getChildElements().get(i);
                String name = element1.getAttributeValue("name");
                /*if (name != null) {
                    if (name.contains("/"))
                        name = name.split("/")[0];
                StructureMatrix declaredProperty = representable.getDeclaredProperty(name);
                if (declaredProperty == null)
                    System.err.println("Element: " + element.toString() + "Element class: " + element.getAttributeValue("class") + "\nStructureMatrix : null \nelement1: name=" + name + "\nElement1= " + element1.toString());
                else
                    browser(element1, declaredProperty);
                //}
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassesNotEqualException e) {
            e.printStackTrace();
        }

    }

    private void browser(Element structureElement, StructureMatrix sm) throws ClassNotFoundException, ClassesNotEqualException {
        if (structureElement == null || sm == null)
            try {
                throw new ClassesNotEqualException(structureElement == null ? null : structureElement.getClass(), sm == null ? null : sm.getClass());
            } catch (ClassesNotEqualException e) {
                e.printStackTrace();
            }
        if (!structureElement.getAttributeValue("class").equals(StructureMatrix.class.getName()))
            throw new ClassesNotEqualException(Class.forName(structureElement.getAttributeValue("class")), StructureMatrix.class);

        String smClassName = structureElement.getAttributeValue("typeClass");
        try {
            Class<?> aClass = Class.forName(smClassName);
            Element data = structureElement.getFirstChildElement("Data");
            int dim = Integer.parseInt(structureElement.getAttributeValue("dim"));
            sm.init(dim, aClass);
            int size = data.getChildElements("Cell").size();
            for (int i = 0; i < size; i++) {
                int l, c;
                Element cell = data.getChildElements("Cell").get(i);
                l = Integer.parseInt(cell.getAttributeValue("l"));
                c = Integer.parseInt(cell.getAttributeValue("c"));
                if (cell.getChildElements().size() > 0) {

                    Object valueOf = null;
                    valueOf = valueOf(cell.getChildElements().get(0));
                    Object elemLc = null;
                    if (valueOf == null)
                        elemLc = browser(cell.getChildElements().get(0), sm, dim, l, c);
                    if (valueOf != null || elemLc != null) {

                        if(valueOf instanceof Representable && size>0 && size==3)
                        {
                            System.out.println(valueOf.toString());
                        }


                        switch (dim) {
                            case 0:
                                if (valueOf != null) {
                                    sm.setElem(valueOf);

                                } else {
                                    sm.setElem(elemLc);
                                }
                                break;
                            case 1:
                                if (valueOf != null) {
                                    sm.setElem(valueOf, c);

                                } else {
                                    sm.setElem(elemLc, c);
                                }
                                break;
                            case 2:
                                if (valueOf != null) {
                                    sm.setElem(valueOf, l, c);

                                } else {
                                    sm.setElem(elemLc, l, c);
                                }
                                break;
                        }
                    }
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Object valueOf(Element simple) {
        String localName = simple.getLocalName();
        String value = simple.getValue();
        switch (localName) {
            case "Double":
                return Double.parseDouble(value);
            case "Integer":
                return Integer.parseInt(value);
            case "String:":
                return value;
            case "Boolean":
                return Boolean.parseBoolean(value);
            case "File":
                return new File(value);
        }
        return null;

    }

    private Object browser(Element matrixContainedObjet, StructureMatrix sm, int dim, int l, int c) {

        try {

            Class<?> rClass = Class.forName(matrixContainedObjet.getAttributeValue("class"));
            Object o = rClass.newInstance();
            if (rClass.getMethod("getDeclaredProperty", String.class) != null) {
                if (o instanceof Representable) {
                    ((Representable) o).declareProperties();
                }
                switch (dim) {
                    case 0:
                        sm.setElem(o);
                        browser(matrixContainedObjet, (MatrixPropertiesObject) o);
                        break;
                    case 1:
                        sm.setElem(o, c);
                        browser(matrixContainedObjet, (MatrixPropertiesObject) o);
                        break;
                    case 2:
                        sm.setElem(o, l, c);
                        browser(matrixContainedObjet, (MatrixPropertiesObject) o);
                        break;
                }
            } else {
                throw new ClassesNotEqualException(rClass, (Class<? extends MatrixPropertiesObject>) o.getClass());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassesNotEqualException e) {
            e.printStackTrace();
        }
        return null;
    }
    
            switch (localName)
            {
                case "StructureMatrix":
                    break;
                case "Representable":
                    break;
                case "Data":
                    break;
                case "Double":
                    break;
                case "Integer":
                    break;
                case "String:" :
                    break;
                case "Boolean":
                    break;
                case "File":
                    break;
            }

     
*/
    private Scene scene;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {

        this.scene = scene;
    }

    public void saveAs(File file) throws IOException {
        this.fileModel = file.getAbsolutePath();
        save(fileModel);

    }

    public String getDirectory(boolean isNew) {
        if (isNew) {
            this.sceneDirectory = "./storage/scene-" + System.nanoTime();
            new File(sceneDirectory).mkdirs();
        }
        return sceneDirectory;
    }

    public String getDefaultFilename() {
        return getDirectory(false) + "/" + "scene-" + System.nanoTime() + "";
    }


    public void save(String fileModel) throws IOException {
        Logger.getAnonymousLogger().info("Save Data Model");


        try {
            Scene scene = getScene();
            StringBuilder stringBuilder = new StringBuilder();
            scene.xmlRepresentation(getDirectory(false), stringBuilder, (Representable) scene);

            File out = new File(getDefaultFilename() + ".xml");
            String xml = stringBuilder.toString();
            PrintWriter pw = new PrintWriter(out);
            pw.print(xml);
            pw.close();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

    }

    public void copy(File a, File b)
            throws IOException {

        Path copied = Paths.get(b.getCanonicalPath());
        Path originalPath = a.toPath();
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
    }

    public static DataModel load(File file) throws FileNotFoundException {
        XStream stream = new XStream();
        DataModel dataModel = new DataModel();
        dataModel.setScene((Scene) stream.fromXML(new FileInputStream(file)));

        return dataModel;

    }
/*
    private void addFile(ZipOutputStream zipOut, FileInputStream fis, ZipEntry zipEntry) throws IOException {
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
    }

    private static ZipEntry getEntry(ZipFile zipIn, String zipEntry) throws IOException {
        return zipIn.getEntry(zipEntry);
    }
*/
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {

        }
    }

    public void addTexture(ITexture sel) {
        this.textures.add(sel);
    }


    public String getNewImageFile() {
        return getDefaultFilename() + ".jpg";
    }

    public String getFileModel() {
        return getDefaultFilename() + ".moodz";
    }

    public String getNewStlFile() {

        return getDefaultFilename() + ".stl";
    }

    public String getNewObjFile() {
        return getDefaultFilename() + ".obj";
    }

/*
    class MoodModelCell {
        private String idContainer;
        private String propertyName;
        private String propertyDescription;
        private String FullObjectClassName;
        private boolean isListType;
        private String ListFullObjectClassName;
        private boolean isArrayType;
        private String ArrayFullObjectClassName;
        private String propertyDim;
        private String propertyIndices;
        private Object propertyValue;

        public String getIdContainer() {
            return idContainer;
        }

        public void setIdContainer(String idContainer) {
            this.idContainer = idContainer;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyDescription() {
            return propertyDescription;
        }

        public void setPropertyDescription(String propertyDescription) {
            this.propertyDescription = propertyDescription;
        }

        public String getFullObjectClassName() {
            return FullObjectClassName;
        }

        public void setFullObjectClassName(String fullObjectClassName) {
            FullObjectClassName = fullObjectClassName;
        }

        public boolean isListType() {
            return isListType;
        }

        public void setListType(boolean listType) {
            isListType = listType;
        }

        public String getListFullObjectClassName() {
            return ListFullObjectClassName;
        }

        public void setListFullObjectClassName(String listFullObjectClassName) {
            ListFullObjectClassName = listFullObjectClassName;
        }

        public boolean isArrayType() {
            return isArrayType;
        }

        public void setArrayType(boolean arrayType) {
            isArrayType = arrayType;
        }

        public String getArrayFullObjectClassName() {
            return ArrayFullObjectClassName;
        }

        public void setArrayFullObjectClassName(String arrayFullObjectClassName) {
            ArrayFullObjectClassName = arrayFullObjectClassName;
        }

        public String getPropertyDim() {
            return propertyDim;
        }

        public void setPropertyDim(String propertyDim) {
            this.propertyDim = propertyDim;
        }

        public String getPropertyIndices() {
            return propertyIndices;
        }

        public void setPropertyIndices(String propertyIndices) {
            this.propertyIndices = propertyIndices;
        }


        public void setPropertyValue(Object propertyValue) {
            this.propertyValue = propertyValue;
        }

        public Object getPropertyValue() {
            return propertyValue;
        }
    }

    public String getJson(MoodModelCell cell) {
        Gson gson = new Gson();
        String json = gson.toJson(cell);
        return json;
    }

    public MoodModelCell fromJson(String json) {
        Gson gson = new Gson();
        MoodModelCell cell = gson.fromJson(json, MoodModelCell.class);
        return cell;

    }
*/

    @Override
    public String toString() {
        Scene scene = getScene();
        StringBuilder stringBuilder = new StringBuilder();
        scene.xmlRepresentation(getDirectory(false),  stringBuilder, (Representable) scene);
        String xml = stringBuilder.toString();
        return xml;
    }
}
