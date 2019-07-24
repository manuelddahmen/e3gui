package one.empty3.gui;

import one.empty3.library.Scene;
import one.empty3.library.core.script.Loader;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
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
        File file1 = new File("./tmp/tmp.mood");

        new Loader().saveTxt(file1, scene);
        FileInputStream fis = new FileInputStream(file1);
        ZipEntry zipEntry = new ZipEntry(file1.getName());
        zipEntry.setComment("Text scene description");
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        zipOut.close();
        fis.close();
    }
    public static DataModel load(File file)
    {
        throw new UnsupportedOperationException("Not implemented yet");

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
