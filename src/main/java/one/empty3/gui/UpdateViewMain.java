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

import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.library.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by manue on 26-06-19.
 */
public class UpdateViewMain extends JPanel implements RepresentableEditor {
    private Main main;
    private Scene scene;
    private Representable currentRepresentable;
    private int displayType;
    private List<ModelBrowser.Cell> cellList;


    public UpdateViewMain() {
        setView(new FunctionView());
        setzRunner(new ZRunnerMain());
        addMouseMotionListener(new MouseMotionListener() {
            public Point3D mousePoint3D;
            Point mousePoint = null;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (main.getUpdateView().getzRunner().isGraphicalEditing()) {

                    if (cellList != null)
                        cellList.forEach(cell -> {
                            Point point = getzRunner().getzBuffer().coordonneesPoint2D((Point3D) cell.o);
                            if (e.getX() - 1 > point.getX() && e.getX() < point.getY() && e.getY() - 1 > point.getY() && e.getY() < point.getY()) {
                                mousePoint = point;
                                mousePoint3D = (Point3D) cell.o;
                            }
                        });

                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (mousePoint3D != null) {
                    mousePoint3D.changeTo(getzRunner().getzBuffer().invert((int) e.getPoint().getX(), (int) e.getPoint().getY(), mousePoint3D.getZ()));
                }


            }
        });

    }


    public void setFF(Main ff) {
        this.main = ff;
        this.getzRunner().setMain(ff);
    }

    private FunctionView view;

    public FunctionView getView() {
        return view;
    }

    public void setView(FunctionView view) {
        FunctionView old = this.view;
        this.view = view;

        firePropertyChange("view", old, view);
    }

    public void afterSet() {

    }

    private ZRunnerMain zRunner;

    public ZRunnerMain getzRunner() {
        return zRunner;
    }

    public void setzRunner(ZRunnerMain zRunner) {
        ZRunnerMain old = this.zRunner;
        this.zRunner = zRunner;
        getView().addPropertyChangeListener(getzRunner());
        addPropertyChangeListener(getzRunner());
        firePropertyChange("zRunner", old, zRunner);
    }

    @Override
    public void initValues(Representable representable) {
        this.currentRepresentable = representable;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }


}
