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

import one.empty3.library.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by manue on 26-06-19.
 */
public class UpdateViewMain extends JPanel implements RepresentableEditor {
    private Main main;
    private Scene scene;
    private Representable currentRepresentable;


    public UpdateViewMain() {
        setView(new FunctionView());
        setzRunner(new ZRunnerMain());
        addMouseListener(new MouseAdapter() {
            public Representable representable;
            public ThreadDrawing threadDrawing;
            Point3D mousePoint3D;
            Point mousePoint = null;
            ArcBall2 arcBall;

            class ThreadDrawing extends Thread {
                boolean running;
                private boolean pause = false;

                public void run() {
                    running = true;
                         while (!isRunning()) {
                             while (isPause()) {
                                 try {
                                     Thread.sleep(100);
                                 } catch (InterruptedException e) {
                                     e.printStackTrace();
                                 }
                             }
                            Point location = MouseInfo.getPointerInfo().getLocation();
                            SwingUtilities.convertPointFromScreen(location, main.getUpdateView());
                            mousePoint = location;
                            try {
                                if (main.isSelectAndRotate()) {
                                    arcBall.moveTo((int) mousePoint.getX(), (int) mousePoint.getY());
                                    //System.out.println(arcBall.ab_curr);
                                } else
                                    drawPoint(mousePoint);
                            } catch (ArrayIndexOutOfBoundsException ex) {
                            }
                        }

                }

                private boolean isRunning() {
                    return running;
                }

                public void setRunning(boolean running) {
                    this.running = running;
                }

                public boolean isPause() {
                    return pause;
                }
            }


            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Mouse Pressed");
                if (main.isSelectAndRotate()) {
                    System.out.println("Mouse starts dragging rotating");
                    Point3D p  = zRunner.getzBuffer().clickAt(e.getX(), e.getY());
                    representable = zRunner.getzBuffer().representableAt(e.getX(), e.getY());
                    arcBall = new ArcBall2(getzRunner().getzBuffer().camera(), p, 10.0, getzRunner().getzBuffer());
                    if (threadDrawing == null) {
                        threadDrawing = new ThreadDrawing();
                        threadDrawing.start();
                    }
                } else if (main.getUpdateView().getzRunner().isGraphicalEditing()) {
                    List<ModelBrowser.Cell> cellList;
                    cellList = new ModelBrowser(main.getDataModel().getScene(), Point3D.class).getObjects();
                    if (cellList != null)
                        cellList.forEach(cell -> {
                            Point point = getzRunner().getzBuffer().camera().coordonneesPoint2D((Point3D) cell.o
                            ,
                                    getzRunner().getzBuffer());
                            if (e != null && point != null &&
                                    e.getX() - 2 < point.getX() && e.getX() + 2 > point.getX()
                                    && e.getY() - 2 < point.getY() && e.getY() + 2 > point.getY()) {
                                mousePoint = point;
                                mousePoint3D = (Point3D) cell.o;
                                if (threadDrawing == null) {
                                    threadDrawing = new ThreadDrawing();
                                    threadDrawing.start();
                                }
                            }
                        });

                }
            }


            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("Mouse Released");
                if (main.getUpdateView().getzRunner().isGraphicalEditing()) {
                    if (mousePoint3D != null) {
                        mousePoint3D.changeTo(getzRunner().getzBuffer().invert((int) e.getPoint().getX(), (int) e.getPoint().getY(), mousePoint3D, getzRunner().getzBuffer().camera()));
                    }
                    System.out.println(mousePoint3D);
                }
                if (threadDrawing != null) {
                    threadDrawing.setRunning(false);
                    threadDrawing = null;
                }
                mousePoint3D = null;
                mousePoint = null;
            }
        });
    }

    private void drawPoint(Point mousePoint) {
        for (int i = -2; i <= 2; i++)
            for (int j = -2; j <= 2; j++)
                ((BufferedImage) main.getUpdateView().getzRunner().getLastImage())
                        .setRGB((int) mousePoint.getX() + i, (int) mousePoint.getY() + j, Color.RED.getRGB());

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


}
