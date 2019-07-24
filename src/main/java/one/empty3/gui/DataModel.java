package one.empty3.gui;

import one.empty3.library.Camera;
import one.empty3.library.Scene;
import one.empty3.library.core.script.ExtensionFichierIncorrecteException;
import one.empty3.library.core.script.Loader;
import one.empty3.library.core.script.VersionNonSupporteeException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by manue on 22-07-19.
 */
public class DataModel implements PropertyChangeListener{
    private File file;
    private TreeScene treeScene;
    private REditor rEditor;
    public DataModel()
    {
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
        this.file = file;

    }
    public void save() throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File file1 = new File("./tmp/scene.mood");
        file1.mkdirs();
        new Loader().saveTxt(file1, scene);
        FileInputStream fis = new FileInputStream(file1);
        ZipEntry zipEntry = new ZipEntry(file1.getName());
        zipEntry.setComment("Text scene description");
        addFile(zipOut, fis, zipEntry);
        fis.close();


        File file2 = new File("./tmp/scene.txt");
        file2.mkdirs();
        PrintWriter printWriter = new PrintWriter(file2);
        printWriter.println(getScene().toString());
    }


    public static DataModel load(File file) throws IOException, VersionNonSupporteeException, ExtensionFichierIncorrecteException {
        DataModel dataModel = new DataModel();
        ZipFile zipFile = new ZipFile(file);
        InputStream inputStream = zipFile.getInputStream(getEntry(zipFile, "scene.mood"));
        File file1 = new File("./tmp/scene.mood");
        file1.mkdirs();
        int read=0;
        FileOutputStream fileOutputStream = new FileOutputStream(file1);
        while((read=inputStream.read())>=0)
        {
            fileOutputStream.write(read);
        }
        new Loader().load(file1, dataModel.getScene());
        return dataModel;

    }

    private void addFile(ZipOutputStream zipOut, FileInputStream fis, ZipEntry zipEntry) throws IOException {
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        zipOut.close();
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
}
