/*
 *
 *  *  This file is part of Empty3.
 *  *
 *  *     Empty3 is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
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

import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.library.Scene;
import one.empty3.library.TextureCol;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by manue on 02-10-19.
 */
public class ThreadGraphicalEditor extends Thread implements PropertyChangeListener {
    class Map {
        private int[] pixelsRepresentableInt;
        private HashMap<Integer, Representable> pixelsRepresentable = new HashMap<>();
        private List<ModelBrowser.Cell> editablePoints;
        private Scene scene;
        private Representable currentObject;
        private Point3D currentPoint;

        public Map(UpdateViewMain updateViewMain)
        {
        }
    }
    private static final int DRAW_POINTS = 1;
    private Main main;
    private Image image;
    private Map map;
    public ThreadGraphicalEditor(Main main)
    {
        this.main = main;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main zRunnerMain) {
        this.main = zRunnerMain;
    }

    @Override
    public void run() {
        while (true) {
            while (main == null || !main.isRunning()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (main == null || main.getUpdateView() == null || main.getUpdateView().getzRunner() == null && main.getUpdateView().getzRunner().getLastImage()==null)
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            while (main.getUpdateView().getzRunner().isGraphicalEditing() ) {
                image = main.getUpdateView().getzRunner().getLastImage();
                browseScene(DRAW_POINTS);

            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void browseScene(int drawType) {
        if(image!=null && drawType==DRAW_POINTS)
        {
            drawPoints(new ModelBrowser(main.getDataModel().getScene(), Point3D.class).getObjects());
        }
    }

    private void drawPoints(List<ModelBrowser.Cell> objects) {
        objects.forEach(cell -> {
            try {
                Point point = getMain().getUpdateView().getzRunner()
                        .getzBuffer().coordonneesPoint2D((Point3D)cell.o);

                for(int i=-1; i<=1; i++)
                    for(int j=-1; j<=1; j++)
                        ((BufferedImage)getMain().getUpdateView().getzRunner().getLastImage())
                                .setRGB((int)point.getX()+i, (int)point.getY()+j, Color.BLACK.getRGB());

            } catch (Exception ex) {

            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("renderedImageOK") &&evt.getNewValue().equals(Boolean.TRUE))
        {
            this.image =(Image) (evt.getNewValue() );

        }
    }


}
