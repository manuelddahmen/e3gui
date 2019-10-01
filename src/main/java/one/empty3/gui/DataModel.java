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

import com.google.gson.Gson;
import com.sun.org.apache.regexp.internal.RE;
import com.thoughtworks.xstream.XStream;
import nu.xom.*;
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
    private TreeScene treeScene;
    private REditor rEditor;
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

                browseRoot(rootElement);
            } catch (ParsingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Logger.getAnonymousLogger().log(Level.INFO, "Model loaded");
    }

    private void browseRoot(Element element) {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        Attribute aClass = element.getAttribute("class");
        try {
            Class<?> aClass1 = systemClassLoader.loadClass(aClass.getValue());
            aClass1.newInstance();
            if (aClass1.isAssignableFrom(Scene.class)) {
                this.scene = new Scene();
                browser(element, scene);
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    private void browser(Element structureElement, StructureMatrix sm) {
        Element data = structureElement.getFirstChildElement("Data");
        for (int i = 0; i < data.getChildElements().size(); i++) {
            int l, c;
            Element child = data.getChildElements().get(i);
            if (child.getLocalName().equals("Cell")) {
                l = Integer.parseInt(child.getAttributeValue("l"));
                c = Integer.parseInt(child.getAttributeValue("c"));
                Object value = null;
                Object elemLc = browser(child.getChildElements().get(0), sm, l, c);
                Object valueOf = valueOf(child);
                /*if(valueOf!=null||elemLc!=null)
                {
                    switch (sm.getDim())
                    {
                        case 0:
                            if(valueOf!=null)
                            {
                                sm.setElem(valueOf);

                            }
                            else {
                                sm.setElem(elemLc);
                            }
                            break;
                        case 1:
                            if(valueOf!=null)
                            {
                                sm.setElem(valueOf, l);

                            }
                            else {
                                sm.setElem(elemLc, l);
                            }
                            break;
                        case 2:
                            if(valueOf!=null)
                            {
                                sm.setElem(valueOf, l, c);

                            }
                            else {
                                sm.setElem(elemLc, l, c);
                            }
                            break;
                    }
                }*/
            }

        }

    }

    private Object valueOf(Element cell) {
        String localName = cell.getLocalName();
        String value = cell.getValue();
        switch (localName) {
            case "StructureMatrix":
                break;
            case "Representable":

                break;
            case "Data":
                break;
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

    private Object browser(Element cell, StructureMatrix sm, int l, int c) {
        switch (cell.getChildElements().get(0).getAttributeValue("class")) {
            case "Representable":
                try {
                    Class<?> aClass = Class.forName(cell.getChildElements().get(0).getAttributeValue("class"));
                    Object o = aClass.newInstance();
                    switch (sm.getDim()) {
                        case 0:
                            sm.setElem(o);
                            browser(cell.getChildElements().get(0), (Representable) o);
                            break;
                        case 1:
                            sm.setElem(o, l);
                            browser(cell.getChildElements().get(0), (Representable) o);
                            break;
                        case 2:
                            sm.setElem(o, l, c);
                            browser(cell.getChildElements().get(0), (Representable) o);
                            break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

                break;
            case "ITexture":
                try {
                    return Class.forName(cell.getAttributeValue("class"));

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return null;
    }

    /*
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
    private void browser(Element element, Representable representable) {
        String localName = element.getLocalName();
        String classAttributeName = element.getAttributeValue("name");
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        String aClass = element.getAttributeValue("class");
        try {
            Class<?> aClass1 = systemClassLoader.loadClass(aClass);
            Class<?> aClass2 = null;
            aClass1.newInstance();
            if (aClass1.isAssignableFrom(StructureMatrix.class)) {
                StructureMatrix o = (StructureMatrix) aClass1.newInstance();
                Attribute type = element.getAttribute("typeClass");
                aClass2 = Class.forName(type.getLocalName());
                o.setClassType(aClass2);
                representable.setProperty(classAttributeName.split("/")[0], o);
                browser(element, (StructureMatrix) aClass2.newInstance());
                browser(element, o);

            } else if (aClass1.isAssignableFrom(ITexture.class)) {
                Attribute type = element.getAttribute("typeClass");
                aClass2 = Class.forName(type.getLocalName());

            } else if (aClass1.isAssignableFrom(Representable.class)) {
                Attribute type = element.getAttribute("typeClass");
                aClass2 = Class.forName(type.getLocalName());
                browser(element, (Representable) aClass2.newInstance());

            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

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
        if (fileModel == null)
            fileModel = getFileModel();

        FileOutputStream fos = new FileOutputStream(getFileModel());
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        File file1 = new File(getDefaultFilename() + ".mood");
        new Loader().saveTxt(file1, scene);
        final FileInputStream[] fis = {new FileInputStream(file1)};
        final ZipEntry[] zipEntry = {new ZipEntry("scenes/" + file1.getName())};
        zipEntry[0].setComment("Text scene description");
        addFile(zipOut, fis[0], zipEntry[0]);
        fis[0].close();


        textures.forEach(iTexture ->

        {
            fis[0] = null;
            File file3 = null;
            File s = null;
            String s2 = "";
            try {
                new File(getDirectory(false) + "/textures/").mkdirs();
                s2 += "text_" + iTexture.getClass();
                if (iTexture instanceof TextureImg)
                    try {
                        s2 += ".jpg";

                        ImageIO.write(((TextureImg) iTexture).getImage(), "jpg", new File(s2));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if (iTexture instanceof TextureMov) {
                    copy(file3, new File(getDirectory(false) + "/textures/" + file3.getName()));
                }
                fis[0] = new FileInputStream(file3);
                zipEntry[0] = new ZipEntry("textures/" + file3.getName());
                zipEntry[0].setComment("Texture { class: " + iTexture.getClass() + ", string : " + iTexture.toString());

                addFile(zipOut, fis[0], zipEntry[0]);
                fis[0].close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        File file2 = new File(getDefaultFilename() + ".txt");
        PrintWriter printWriter = new PrintWriter(file2);
        printWriter.println(getScene().toString());

        String fileSceneRaw = getDefaultFilename() + ".raw";
        /*XMLEncoder xmlEncoder = new XMLEncoder(new FileOutputStream(fileSceneRaw));
        xmlEncoder.writeObject(getScene());
        xmlEncoder.close();
        */
        BrowseScene browseScene = new BrowseScene(fileSceneRaw);

        browseScene.encode(getScene());

        XStream xStream = new XStream();
        String xml = xStream.toXML(scene);
        File file = new File(fileSceneRaw + "_xtream.xml");
        PrintWriter pw = new PrintWriter(file);
        pw.print(xml);
        pw.close();


        zipOut.close();
        fos.close();
/*
        Gson gson = new Gson();
        ArrayList<MoodModelCell> cellsFromRepresentable = createCellsFromRepresentable(scene);

        try {
         System.out.println(gson.toJson(cellsFromRepresentable));
        } catch (java.lang.StackOverflowError ex)
        { ex.printStackTrace();}
        try {
            //System.out.println(gson.toJson(scene));
        } catch (java.lang.StackOverflowError ex)
        { ex.printStackTrace();}
*/
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {

        }
    }

    public void addTexture(ITexture sel) {
        this.textures.add(sel);
    }


    public ArrayList<MoodModelCell> createCellsFromRepresentable(Representable representable) {
        ArrayList<MoodModelCell> cells = new ArrayList<>();
        representable.declareProperties();

        representable.declarations().forEach(new BiConsumer<String, Object>() {
            @Override
            public void accept(String s, Object o) {
                MoodModelCell moodModelCell = new MoodModelCell();
                if (o instanceof Double || o instanceof Integer || o instanceof Boolean || o instanceof Representable || o instanceof ITexture) {
                    moodModelCell.setArrayType(false);
                    moodModelCell.setListType(false);
                    moodModelCell.setPropertyDim("0");
                    moodModelCell.setFullObjectClassName(o.getClass().getCanonicalName());
                    moodModelCell.setPropertyName(s.split("/")[0]);
                    moodModelCell.setPropertyDescription(s.split("/")[1]);
                    moodModelCell.setPropertyValue(o);

                } else if (o instanceof Double[] || o instanceof Representable[] || o instanceof Integer[] || o instanceof Boolean[] || o instanceof ITexture[]) {
                    moodModelCell.setArrayType(true);
                    moodModelCell.setListType(false);
                    moodModelCell.setPropertyDim("1");
                    moodModelCell.setArrayFullObjectClassName(o.getClass().getCanonicalName());
                    moodModelCell.setListType(false);
                    moodModelCell.setFullObjectClassName(o.getClass().getCanonicalName());
                    moodModelCell.setPropertyName(s.split("/")[0]);
                    moodModelCell.setPropertyDescription(s.split("/")[1]);
                    moodModelCell.setPropertyValue(o);
                    cells.add(moodModelCell);

                    /*
                    Object[] t = ((Object[]) o);
                    Arrays.stream(t).forEach(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) {

                        }
                    });*/
                } else if (o instanceof Double[][] || o instanceof Representable[][] || o instanceof Integer[][] || o instanceof Boolean[][] || o instanceof ITexture[][]) {
                    moodModelCell.setArrayType(true);
                    moodModelCell.setListType(false);
                    moodModelCell.setPropertyDim("2");
                    moodModelCell.setArrayFullObjectClassName(o.getClass().getCanonicalName());
                    moodModelCell.setListType(false);
                    moodModelCell.setFullObjectClassName(o.getClass().getCanonicalName());
                    moodModelCell.setPropertyName(s.split("/")[0]);
                    moodModelCell.setPropertyDescription(s.split("/")[1]);
                    moodModelCell.setPropertyValue(o);
                    cells.add(moodModelCell);
                } else if (o instanceof ArrayList) {
                    moodModelCell.setArrayType(false);
                    moodModelCell.setListType(true);
                    moodModelCell.setListFullObjectClassName(o.getClass().getCanonicalName());
                    moodModelCell.setPropertyDim("1");
                    moodModelCell.setListType(true);
                    moodModelCell.setFullObjectClassName(o.getClass().getCanonicalName());
                    moodModelCell.setPropertyName(s.split("/")[0]);
                    moodModelCell.setPropertyDescription(s.split("/")[1]);
                    moodModelCell.setPropertyValue(o);
                    cells.add(moodModelCell);

                }
            }
        });
        {

        }
        return cells;
    }

    public Representable getRepresentableFromCells(ArrayList<MoodModelCell> cells) {
        return null;
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

}