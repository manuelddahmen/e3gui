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
    private MyObservableList<Representable> translate = null;
    private boolean graphicalEditing;


    public UpdateViewMain() {
        setView(new FunctionView());
        setzRunner(new ZRunnerMain());
        addMouseListener(new MouseAdapter() {
            public Representable representable;
            public ThreadDrawing threadDrawing =null;
            public Point3D mousePoint3Dorig;
            Point3D mousePoint3D;
            Point mousePoint = null;
            ArcBall2 arcBall;

            class ThreadDrawing extends Thread {
                boolean running;
                private boolean pause = false;

                public void run() {
                    running = true;
                    while (isRunning()) {
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
                            if (main.getGraphicalEdit2().getActionToPerform().equals(GraphicalEdit2.Action.ROTATE)) {
                                arcBall.moveTo((int) mousePoint.getX(), (int) mousePoint.getY());
                                ZBufferImpl zBuffer = main.getUpdateView().getzRunner().getzBuffer();
                                //unit3t2
                                Point p1 =  zBuffer.camera().coordonneesPoint2D(mousePoint3D.plus(Point3D.random(1.0).norme1()), zBuffer);
                                Point p2 =  zBuffer.camera().coordonneesPoint2D(mousePoint3D.plus(Point3D.random(1.0).norme1()), zBuffer);

                                double sqrt = Math.sqrt(p1.getX() * p2.getX() + p2.getY() + p2.getY());



                                Graphics graphics = getGraphics();
                                graphics.drawOval((int)(location.getX()-sqrt/2), (int)(location.getY()-sqrt/2),
                                        (int)(location.getX()+sqrt/2),(int)(location.getY()+sqrt/2));
                                arcBall.setRadius(10*sqrt/getWidth());

                                  //
                                        //
                                        //
                                        //    arcBall.getRadius()), (int)location.getX()+unit3t2(arcBall.getRadius()));
////System.out.println("Mouse rotation moved");
                            } else if (main.getGraphicalEdit2().getActionToPerform().equals(GraphicalEdit2.Action.TRANSLATE)) {
                                main.getGraphicalEdit2().getCurrentSelection().forEach(new Consumer<Representable>() {
                                    @Override
                                    public void accept(Representable representable) {
                                        Point3D elem = null;
                                        if(representable instanceof Point3D)
                                            elem = (Point3D) representable;
                                        else {
                                            elem = representable.getRotation().getElem().getCentreRot().getElem();
                                            showAxisMove(representable.getRotation().getElem(), elem);
                                            System.out.println(representable.getRotation().getElem().toString() + " "+ elem.toString());
                                        }
                                        // TODO Manage Move Event
                                    }
                                });
                            }

                        } catch (ArrayIndexOutOfBoundsException ex) {
                            ex.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
                if (main.getGraphicalEdit2().getActionToPerform()!=null && main.getGraphicalEdit2().getActionToPerform().equals(GraphicalEdit2.Action.ROTATE)) {
                    System.out.println("Mouse Pressed");
                    System.out.println("Mouse starts dragging rotating");
                    mousePoint3D  = zRunner.getzBuffer().clickAt(e.getX(), e.getY());
                    representable = zRunner.getzBuffer().representableAt(e.getX(), e.getY());
                    arcBall = new ArcBall2(getzRunner().getzBuffer().camera(), mousePoint3D,
                            Math.atan(2.0*Math.PI*
                            e.getX()/getWidth()),

                                    getzRunner().getzBuffer());
                    arcBall.init(representable);
                    if (threadDrawing == null) {
                        threadDrawing = new ThreadDrawing();
                        threadDrawing.start();
                    }
                } else  {
                    System.out.println("Mouse Pressed");
                    if(main.getGraphicalEdit2().isSelectArbitraryPointsIn()) {
                        Point3D selectedPoint = zRunner.getzBuffer().clickAt(e.getX(), e.getY());
                        if(main.getGraphicalEdit2().isSelectArbitraryPointsIn())
                            main.getGraphicalEdit2().getSelectionIn().add(selectedPoint);
                    } else if(main.getGraphicalEdit2().isSelectingMultipleObjectsIn()) {
                        Representable multiple = zRunner.getzBuffer().representableAt(e.getX(), e.getY());
                        main.getGraphicalEdit2().getSelectionIn().add(multiple);
                    }


                    List<ModelBrowser.Cell> cellList;
                    cellList = new ModelBrowser(getzRunner().getzBuffer(), main.getDataModel().getScene(), Point3D.class).getObjects();
                    if (cellList != null)
                        cellList.forEach(cell -> {
                            Point point = getzRunner().getzBuffer().camera().coordonneesPoint2D((Point3D) cell.pRot
                            ,
                                    getzRunner().getzBuffer());
                            if (e != null && point != null &&
                                    e.getX() - 2 < point.getX() && e.getX() + 2 > point.getX()
                                    && e.getY() - 2 < point.getY() && e.getY() + 2 > point.getY()) {
                                mousePoint = point;
                                mousePoint3D= cell.pRot;
                                mousePoint3Dorig = (Point3D) cell.o;
                                if (threadDrawing == null) {
                                    threadDrawing = new ThreadDrawing();
                                    threadDrawing.start();
                                }


                                main.getGraphicalEdit2().getCurrentSelection().add(cell.pRot);
                            }
                        });

                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (main.getGraphicalEdit2().getSelection() ) {
                    if (mousePoint3D != null) {
                        System.out.println("Mouse Released");
                        mousePoint3D.changeTo(getzRunner().getzBuffer().invert((int) e.getPoint().getX(), (int) e.getPoint().getY(), mousePoint3D, getzRunner().getzBuffer().camera()));
                        System.out.println(mousePoint3D);
                        main.getGraphicalEdit2().add(mousePoint3D);
                    }
                } else if(main.getGraphicalEdit2().getActionToPerform().equals(GraphicalEdit2.Action.ROTATE)) {
                    if(arcBall.matrix()!=null) {
                        System.out.println("Mouse Released");
                        representable.getRotation().getElem().getRot().setElem(arcBall.matrix());
                        System.out.println("Matrix changed = " + representable.getRotation().getElem().getRot().getElem());
                        representable.getRotation().getElem().getCentreRot().setElem(mousePoint3D);
                        System.out.println("centreRot changed" + representable.getRotation().getElem().getCentreRot().getElem());
                        System.out.println("class re" + representable.getClass());
                    } else {
                        System.out.println("Matrix == null");
                    }
                }
                if (threadDrawing != null) {
                    threadDrawing.setRunning(false);
                    threadDrawing = null;
                }
                mousePoint3Dorig = null;
                mousePoint3D = null;
                mousePoint = null;
            }
        });
    }
    public void drawGraphicsEdit()
    {

    }


    private void showAxisMove(Rotation elem, Point3D elem1) {
        for (Point3D point3D : elem.getRot().getElem().getColVectors()) {
            LineSegment lsX =  new LineSegment(point3D.mult(10.0),elem1);
            LineSegment lsY =  new LineSegment(point3D.mult(10.0),elem1);
            LineSegment lsZ =  new LineSegment(point3D.mult(10.0),elem1);


        }
    }

    public void setTranslate(MyObservableList<Representable> translate) {
        this.translate = translate;
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


    public MyObservableList<Representable> getTranslate() {
        return translate;
    }

    public void setGraphicalEditing(boolean graphicalEditing) {
        this.graphicalEditing = graphicalEditing;

    }

    public boolean isGraphicalEditing() {
        return graphicalEditing;
    }
}
