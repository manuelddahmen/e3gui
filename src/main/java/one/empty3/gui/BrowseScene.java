package one.empty3.gui;

import one.empty3.library.Representable;

import javax.lang.model.type.ArrayType;
import java.beans.XMLEncoder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by manue on 11-08-19.
 */
public class BrowseScene {
    private String fileBaseName;
    private XMLEncoder encoder;
    int i=0;
    public BrowseScene(String fileBaseName) throws FileNotFoundException {
        this.fileBaseName = fileBaseName;

    }
    public void encode(Object r) throws IOException {
        try {
            write(r);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(r instanceof Representable) {
            ((Representable)r).declarations().forEach(new BiConsumer<String, Object>() {
                @Override
                public void accept(String s, Object o) {
                    try {
                        write(s);
                        write(o);
                        if(o instanceof ArrayList)
                        {
                            for (Object o1 : ((ArrayList) o)) {
                                encode(o1);
                                write("NEXT LIST ELEMENT *");
                            }
                        }else if(o instanceof ArrayType) {
                            if(((ArrayType)o).getComponentType().getClass().isArray())
                            {
                                Arrays.stream((Object[][])o).forEach(new Consumer<Object[]>() {
                                    @Override
                                    public void accept(Object[] objects) {
                                        for(int i=0; i<objects.length; i++)
                                        {
                                            try {
                                                encode(objects[i]);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                            } else {
                                Arrays.stream((Object[])o).forEach(new Consumer<Object>() {
                                    @Override
                                    public void accept(Object objects) {
                                        try {
                                            encode(objects);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }

                        }
                        write("NEXT");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    public void write(Object o) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileBaseName + (i++) + ".xml");
        encoder = new XMLEncoder(fileOutputStream);
        encoder.writeObject(o);
        System.out.println(o);
        encoder.close();
        fileOutputStream.close();
    }
    public void close()
    {
        encoder.close();
    }
}
