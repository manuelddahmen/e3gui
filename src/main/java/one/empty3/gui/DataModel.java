package one.empty3.gui;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import one.empty3.library.Camera;
import one.empty3.library.ITexture;
import one.empty3.library.Representable;
import one.empty3.library.Scene;
import one.empty3.library.core.script.Loader;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by manue on 22-07-19.
 */
public class DataModel implements PropertyChangeListener{
    private TreeScene treeScene;
    private REditor rEditor;
    private ArrayList<File> texturesFiles = new ArrayList();
    private String fileModel;
    private File newImageFile;
    private String sceneDirectory;

    public DataModel()
    {
        getDirectory(true);
        Scene scene = new Scene();
        setScene(scene);
        scene.cameraActive(new Camera());
        scene.cameraActive().calculerMatrice(null);
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
    public String getDirectory(boolean isNew)
    {
        if(isNew)
        {
        this.sceneDirectory = "./storage/scene-"+System.nanoTime();
        new File(sceneDirectory).mkdirs();
        }
        return sceneDirectory;
    }
    public String getDefaultFilename() {
        return getDirectory(false)+"/"+"scene-"+System.nanoTime()+"";
    }


    public void save(String fileModel) throws IOException {
        Logger.getAnonymousLogger().info("Save Data Model");
        if(fileModel==null)
            fileModel = getFileModel();

        FileOutputStream fos = new FileOutputStream(getFileModel());
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        File file1 = new File(getDefaultFilename()+".mood");
        new Loader().saveTxt(file1, scene);

        FileInputStream fis = new FileInputStream(file1);
        ZipEntry zipEntry = new ZipEntry("scenes/"+file1.getName());
        zipEntry.setComment("Text scene description");
        addFile(zipOut, fis, zipEntry);
        fis.close();



        texturesFiles.forEach(new Consumer<File>() {
            @Override
            public void accept(File file) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    ZipEntry zipEntry = new ZipEntry("textures/"+file.getName());
                    zipEntry.setComment("Texture");
                    addFile(zipOut, fis, zipEntry);
                    fis.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        File file2 = new File(getDefaultFilename()+".txt");
        PrintWriter printWriter = new PrintWriter(file2);
        printWriter.println(getScene().toString());

        String fileSceneRaw = getDefaultFilename()+".raw";
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


    public static DataModel load(File file) {
        DataModel dataModel = new DataModel();
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        /*
        InputStream inputStream = zipFile.getInputStream(getEntry(zipFile, "scene.mood"));
        File file1 = new File("./tmp/scene.mood");
        //file1.mkdirs();
        int read=0;
        FileOutputStream fileOutputStream = new FileOutputStream(file1);
        while((read=inputStream.read())>=0)
        {
            fileOutputStream.write(read);
        }
        new Loader().load(file1, dataModel.getScene());
        */
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while(entries.hasMoreElements())
        {
            ZipEntry zipEntry = entries.nextElement();
            if(zipEntry.getName().endsWith("xml")) {
                XStream xStream = new XStream();
                Object fromXML = null;
                try {
                    fromXML = xStream.fromXML(zipFile.getInputStream(zipEntry));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                if(fromXML instanceof Scene)
                {
                    DataModel dataModel1 = new DataModel();
                    dataModel1.setScene((Scene)fromXML);
                }
                else
                {
                    Logger.getAnonymousLogger().info("Error loading");
                }
            }
        }


        return dataModel;

    }

    private void addFile(ZipOutputStream zipOut, FileInputStream fis, ZipEntry zipEntry) throws IOException {
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
    }
    private static ZipEntry getEntry(ZipFile zipIn, String zipEntry) throws IOException {
        return zipIn.getEntry(zipEntry);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName())
        {

        }
    }

    public void addTextureFile(File sel) {
        this.texturesFiles.add(sel);
    }


    public ArrayList<MoodModelCell>  createCellsFromRepresentable(Representable representable)
    {
        ArrayList<MoodModelCell> cells = new ArrayList<>();
        representable.declareProperties();

        representable.declarations().forEach(new BiConsumer<String, Object>() {
            @Override
            public void accept(String s, Object o) {
                MoodModelCell moodModelCell = new MoodModelCell();
                if(o instanceof Double || o instanceof Integer || o instanceof Boolean || o instanceof Representable || o instanceof ITexture)
                {
                        moodModelCell.setArrayType(false);
                        moodModelCell.setListType(false);
                        moodModelCell.setPropertyDim("0");
                        moodModelCell.setFullObjectClassName(o.getClass().getCanonicalName());
                        moodModelCell.setPropertyName(s.split("/")[0]);
                        moodModelCell.setPropertyDescription(s.split("/")[1]);
                        moodModelCell.setPropertyValue(o);

                }
                else if(o instanceof Double[] || o instanceof Representable[] || o instanceof Integer[] || o instanceof Boolean[] || o instanceof ITexture[])  {
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
                }
                else if(o instanceof Double[][] || o instanceof Representable[][] || o instanceof Integer[][] || o instanceof Boolean[][] || o instanceof ITexture[][])  {
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
                }
                else if(o instanceof ArrayList)
                {
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

    public Representable getRepresentableFromCells(ArrayList<MoodModelCell> cells)
    {
        return null;
    }

    public String getNewImageFile() {
        return getDefaultFilename()+".jpg";
    }

    public String getFileModel() {
        return getDefaultFilename()+".moodz";
    }

    public String getNewStlFile() {

        return getDefaultFilename()+".stl";
    }
    public String getNewObjFile() {
        return getDefaultFilename()+".obj";
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

    public MoodModelCell fromJson(String json)
    {
        Gson gson = new Gson();
        MoodModelCell cell  = gson.fromJson(json, MoodModelCell.class);
        return cell;

    }

}
    /*
    public class ZipMultipleFiles {
            List<String> srcFiles = Arrays.asList("test1.txt", "test2.txt");
            FileOutputStream fos = new FileOutputStream("multiCompressed.zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (String srcFile : srcFiles) {
                File fileToZip = new File(srcFile);
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
            fos.close();
    }
*/

