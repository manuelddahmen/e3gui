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

import one.empty3.library.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by manue on 02-10-19.
 */
public class ThreadGraphicalEditor extends Thread implements PropertyChangeListener {
    private boolean motionListener = false;

    public ArrayList<Point3D> getPointsTranslate() {
        return pointsTranslate;
    }

    class Map {
        private int[] pixelsRepresentableInt;
        private HashMap<Integer, Representable> pixelsRepresentable = new HashMap<>();
        private List<ModelBrowser.Cell> editablePoints;
        private Scene scene;
        private Representable currentObject;
        private Point3D currentPoint;

        public Map(UpdateViewMain updateViewMain) {
        }
    }


    private static final int DRAW_POINTS = 1;
    private Main main;
    private Image image;
    private Map map;
    private ArrayList<Point3D> pointsTranslate = new ArrayList<Point3D>();

    public ThreadGraphicalEditor(Main main) {
        this.main = main;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main zRunnerMain) {
        this.main = zRunnerMain;
    }

    boolean init;

    @Override
    public void run() {
        while (main == null || !main.isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (main.isRunning()) {
            while (main == null || main.getUpdateView() == null || main.getUpdateView().getzRunner() == null || main.getUpdateView().getzRunner().getLastImage() == null)
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            if (!init) {
                main.getUpdateView().addMouseListener(new MouseAdapter() {
                    Point3D point;
                    boolean move;
                    int axeNo = 0;

                    @Override
                    public void mouseClicked(MouseEvent e) {
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getButton() == 0) {
                            if (!move) {
                                point = main.getUpdateView().getzRunner().getzBuffer().invert(e.getX(), e.getY(),
                                        main.getUpdateView().getzRunner().getzBuffer().camera()).getP2().getElem();
                            }
                        } else {

                            axeNo = ((axeNo + 1) % 3);
                        }
                        move = true;
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        move = false;
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                    }

                    @Override
                    public void mouseWheelMoved(MouseWheelEvent e) {
                        super.mouseWheelMoved(e);
                    }

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        int x = e.getX();
                        int y = e.getY();
                        pointsTranslate.forEach(new Consumer<Point3D>() {
                            @Override
                            public void accept(Point3D p) {

                            }
                        });

                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {
                        while (move) {

                        }
                    }
                });
                init = true;
            }

            afterDraw();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void afterDraw() {
        if (main.getGraphicalEdit2().isActiveGraphicalEdit())
            browseScene();
    }

    public void browseScene() {
        drawPoints(new ModelBrowser(main.getUpdateView().getzRunner().getzBuffer(), main.getDataModel().getScene(), Point3D.class).getObjects());
        drawSelection();
        if (getMain().getGraphicalEdit2().getActionToPerform().equals(GraphicalEdit2.Action.TRANSLATE)) {
            showAxis();
            if (!motionListener) {
                motionListener = true;
                main.getUpdateView().addMouseMotionListener(new MouseMotionListener() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        pointsTranslate.forEach(new Consumer<Representable>() {
                            @Override
                            public void accept(Representable representable) {
                                ZBufferImpl zBuffer = getMain().getUpdateView().getzRunner().getzBuffer();
                                Axe invert = zBuffer.invert(e.getX(), e.getY(), zBuffer.camera());
                                Point3D elem = invert.getP1().getElem();

                                Point3D origin = null;
                                if (representable instanceof Point3D) {
                                    origin = (Point3D) representable;
                                } else {
                                    origin = representable.getRotation().getElem().getCentreRot().getElem();
                                }
                                origin.changeTo(elem);
                            }

                        });
                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {

                    }
                });
            }
        }
    }

    private void showAxis() {
        pointsTranslate.clear();
        LineSegment[] lsXYZ = new LineSegment[3];
        int i = 0;
        for (Representable r : getMain().getGraphicalEdit2().getCurrentSelection()) {

            Point3D[] vects = null;
            Point3D centre;
            if (r instanceof Point3D) {
                centre = (Point3D) r;
                vects = new Matrix33().getColVectors();
            } else {
                vects = r.getRotation().getElem().getRot().getElem().getColVectors();
                centre = r.getRotation().getElem().getCentreRot().getElem();
            }
            i = 0;
            if (vects != null && vects.length == 3)
                for (Point3D p : vects) {
                    try {
                        pointsTranslate.add(centre);
                        lsXYZ[i] = new LineSegment(p.mult(-10.0).plus(centre),
                                p.mult(10.0).plus(centre));
                        pointsTranslate.add(p);
                        Point p1 = main.getUpdateView().getzRunner().getzBuffer().camera().coordonneesPoint2D(lsXYZ[i].getOrigine(), main.getUpdateView().getzRunner().getzBuffer());
                        Point p2 = main.getUpdateView().getzRunner().getzBuffer().camera().coordonneesPoint2D(lsXYZ[i].getExtremite(), main.getUpdateView().getzRunner().getzBuffer());


                        if(p1!=null && p2!=null ) {
                            Graphics graphics = main.getUpdateView().getzRunner().getLastImage().getGraphics();
                            graphics.setColor(Color.BLACK);
                            graphics.drawLine(p1.x, p1.y, p2.x, p2.y);
                        }
                        i++;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
        }
    }


    private void drawPoints(List<ModelBrowser.Cell> objects) {
        objects.forEach(cell -> {
            try {
                ZBufferImpl zBuffer = getMain().getUpdateView().getzRunner()
                        .getzBuffer();
                Point point = getMain().getUpdateView().getzRunner()
                        .getzBuffer().camera().coordonneesPoint2D((Point3D) cell.pRot, zBuffer);

                for (int i = -2; i <= 2; i++)
                    for (int j = -2; j <= 2; j++)
                        ((BufferedImage) getMain().getUpdateView().getzRunner().getLastImage())
                                .setRGB((int) point.getX() + i, (int) point.getY() + j, Color.BLACK.getRGB());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void drawSelection() {
        List<Representable> list = null;
        list = (List<Representable>) main.getGraphicalEdit2().getCurrentSelection();
        if (list != null) {
            list.forEach(cell -> {
                try {
                    ZBufferImpl zBuffer = getMain().getUpdateView().getzRunner()
                            .getzBuffer();
                    if (cell instanceof Point3D) {
                        Point point = getMain().getUpdateView().getzRunner()
                                .getzBuffer().camera().coordonneesPoint2D((Point3D) cell, zBuffer);

                        for (int i = -2; i <= 2; i++)
                            for (int j = -2; j <= 2; j++)
                                if (getMain().getUpdateView().getzRunner().getLastImage() != null && point != null)
                                    ((BufferedImage) getMain().getUpdateView().getzRunner().getLastImage())
                                            .setRGB((int) point.getX() + i, (int) point.getY() + j, Color.RED.getRGB());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("renderedImageOK") && evt.getNewValue().equals(Boolean.TRUE)) {
            this.image = (Image) (evt.getNewValue());

        }
    }


}
