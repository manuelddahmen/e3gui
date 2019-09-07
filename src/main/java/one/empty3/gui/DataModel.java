package one.empty3.gui;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import one.empty3.library.*;
import one.empty3.library.core.script.Loader;

import javax.imageio.ImageIO;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.function.BiConsumer;
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
    private ArrayList<ITexture> textures = new ArrayList();
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
        final FileInputStream[] fis = {new FileInputStream(file1)};
        final ZipEntry[] zipEntry = {new ZipEntry("scenes/" + file1.getName())};
        zipEntry[0].setComment("Text scene description");
        addFile(zipOut, fis[0], zipEntry[0]);
        fis[0].close();



        textures.forEach(iTexture ->

                {
                    fis[0] = null;
                    File file3 = null;File s =null;
                    String s2 = "";
                    try {
                        new File(getDirectory(false) + "/textures/").mkdirs();
                        s2 += "text_" + iTexture.getClass() ;
                        if (iTexture instanceof TextureImg)
                            try {
                            s2+= ".jpg";

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

    public void copy(File a, File b)
            throws IOException {

        Path copied = Paths.get(b.getCanonicalPath());
        Path originalPath = a.toPath();
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
    }
    public static DataModel load(File file) throws FileNotFoundException {
        XStream stream = new XStream();
        DataModel dataModel = new DataModel();
        dataModel.setScene((Scene)stream.fromXML(new FileInputStream(file)));

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

    public void addTexture(ITexture sel) {
        this.textures.add(sel);
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