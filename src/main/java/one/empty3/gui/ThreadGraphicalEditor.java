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

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;

/**
 * Created by manue on 02-10-19.
 */
public class ThreadGraphicalEditor extends Thread implements PropertyChangeListener {
    class Map {
        private int[] pixelsRepresentableInt;
        private HashMap<Integer, Representable> pixelsRepresentable;
        private List<Point3D> editablePoints;
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

    public ThreadGraphicalEditor()
    {}

    public Main getMain() {
        return main;
    }

    public void setMain(Main zRunnerMain) {
        this.main = zRunnerMain;
    }

    @Override
    public void run() {
        while(main==null || main.getUpdateView()==null||main.getUpdateView().getzRunner()==null)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        Map map = new Map(main.getUpdateView());
        while( main.getUpdateView().getzRunner().isRunning())
            while(main.getUpdateView().getzRunner().isGraphicalEditing())
            {
                browseScene(DRAW_POINTS);
            }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void browseScene(int drawType) {
        Scene scene = main.getUpdateView().getzRunner().getzBuffer().scene();
        if(image!=null)
        {

        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("renderedImageOK") &&evt.getNewValue()!=null)
        {
            this.image =(Image) (evt.getNewValue() );

        }
    }
}
