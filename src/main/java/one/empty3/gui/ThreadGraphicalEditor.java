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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/***
 * Created by manue on 02-10-19.
 * Thread . Dessine les points de contrôles des objets de la scène.
 */
public class ThreadGraphicalEditor extends Thread implements PropertyChangeListener {



    private Main main;
    private ArrayList<Point3D> pointsTranslate = new ArrayList<Point3D>();

    ThreadGraphicalEditor(Main main) {
        this.main = main;
    }


    public Main getMain() {
        return main;
    }

    public void setMain(Main Main) {
        this.main = Main;
    }

    private boolean init;

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
            while (main == null || main.getUpdateView() == null || getMain().getUpdateView().getzRunner() == null || getMain().getUpdateView().getzRunner().getLastImage() == null)
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            if (!init) {
                main.getUpdateView().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (getMain().getGraphicalEdit2().getActionToPerform().equals(GraphicalEdit2.Action.SELECT)) {
                            if (main.getGraphicalEdit2().isSelectArbitraryPoints()) {
                                Point3D selectedPoint = getMain().getUpdateView().getzRunner().getzBuffer().clickAt(e.getX(), e.getY());
                                main.getGraphicalEdit2().add(selectedPoint);
                            } else if (main.getGraphicalEdit2().isSelectingMultipleObjects()) {
                                Representable multiple = getMain().getUpdateView().getzRunner().getzBuffer().representableAt(e.getX(), e.getY());
                                main.getGraphicalEdit2().add(multiple);
                            } else {
                                List<ModelBrowser.Cell> cellList;
                                cellList = new ModelBrowser(getMain().getUpdateView().getzRunner().getzBuffer(), main.getDataModel().getScene(), Point3D.class).getObjects();

                                if (cellList != null)
                                    cellList.forEach(cell -> {
                                        Point point = getMain().getUpdateView().getzRunner().getzBuffer().camera().coordonneesPoint2D(cell.pRot
                                                ,
                                                getMain().getUpdateView().getzRunner().getzBuffer());
                                        if (point != null &&
                                                e.getX() - 2 < point.getX() && e.getX() + 2 > point.getX()
                                                && e.getY() - 2 < point.getY() && e.getY() + 2 > point.getY()) {
                                            if (cell.o instanceof Point3D) {
                                                Point3D mousePoint3D = (Point3D) cell.o;
                                                if (pointsTranslate.contains(mousePoint3D)) {
                                                    pointsTranslate.remove(mousePoint3D);
                                                    if (getMain().getGraphicalEdit2().getCurrentSelection().contains(mousePoint3D))
                                                        getMain().getGraphicalEdit2().getCurrentSelection().remove(mousePoint3D);
                                                } else {
                                                    pointsTranslate.add(mousePoint3D);
                                                    getMain().getGraphicalEdit2().getCurrentSelection().add(mousePoint3D);
                                                }
                                            }
                                        }
                                    });
                                System.out.println(main.getGraphicalEdit2().getCurrentSelection());
                            }


                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (getMain().getGraphicalEdit2().getActionToPerform().equals(GraphicalEdit2.Action.TRANSLATE)) {
                            ZBufferImpl zBuffer = main.getUpdateView().getzRunner().getzBuffer();
                            Point location = MouseInfo.getPointerInfo().getLocation();
                            SwingUtilities.convertPointFromScreen(location, main.getUpdateView());
                            Axe invert = zBuffer.invert(e.getX(), e.getY(), Point3D.O0, main.getUpdateView().getzRunner().getzBuffer().camera() ) ;//TODO
                            Point3D elem = invert.getP2().getElem();
                            System.out.println("Inverted location " + elem);
                            ModelBrowser modelBrowser = new ModelBrowser(getMain().getGraphicalEdit2().getSelectionIn(), zBuffer);
                            if (getMain().getGraphicalEdit2().getActionToPerform().equals(GraphicalEdit2.Action.TRANSLATE)) {
                                modelBrowser.translateSelection(elem);
                                System.out.println(main.getGraphicalEdit2().getCurrentSelection());
                            }
                        }
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


                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {

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

    private void afterDraw() {
        if (main.getGraphicalEdit2().isActiveGraphicalEdit())
            browseScene();
    }

    private void browseScene() {
        drawPoints(new ModelBrowser(getMain().getUpdateView().getzRunner().getzBuffer(), main.getDataModel().getScene(), Point3D.class).getObjects());
        drawSelection();
        if (getMain().getGraphicalEdit2().getActionToPerform().equals(GraphicalEdit2.Action.TRANSLATE)) {
            showAxis();
        }
    }

    private void showAxis() {
        pointsTranslate.clear();
        LineSegment[] lsXYZ = new LineSegment[3];
        int i;
        for (Representable r : getMain().getGraphicalEdit2().getCurrentSelection()) {

            Point3D[] vects;
            Point3D centre;
            if (r instanceof Point3D) {
                centre = (Point3D) r;
                vects = new Matrix33().getColVectors();
                pointsTranslate.add(centre);

            } else {
                vects = r.getRotation().getElem().getRot().getElem().getColVectors();
                centre = r.getRotation().getElem().getCentreRot().getElem();
                pointsTranslate.add(centre);
            }
            i = 0;
            if (vects != null && vects.length == 3)
                for (Point3D p : vects) {
                    try {

                        lsXYZ[i] = new LineSegment(p.mult(-10.0).plus(centre),
                                p.mult(10.0).plus(centre));
                        Point p1 = getMain().getUpdateView().getzRunner().getzBuffer().camera().coordonneesPoint2D(lsXYZ[i].getOrigine(), getMain().getUpdateView().getzRunner().getzBuffer());
                        Point p2 = getMain().getUpdateView().getzRunner().getzBuffer().camera().coordonneesPoint2D(lsXYZ[i].getExtremite(), getMain().getUpdateView().getzRunner().getzBuffer());


                        if (p1 != null && p2 != null) {
                            Graphics graphics = getMain().getUpdateView().getzRunner().getLastImage().getGraphics();
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
                drawPoint((Point3D) cell.pRot, Color.BLACK);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void drawSelection() {
        List<Representable> list;
        list = main.getGraphicalEdit2().getCurrentSelection();
        if (list != null) {
            list.forEach(cell -> {
                try {
                    if (cell instanceof Point3D) {
                        if (getMain().getUpdateView().getzRunner().getLastImage() != null)
                            drawPoint((Point3D) cell, Color.RED);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    private void drawPoint(Point3D p, Color color) {
        ZBufferImpl zBuffer = getMain().getUpdateView().getzRunner()
                .getzBuffer();
        Point point = getMain().getUpdateView().getzRunner()
                .getzBuffer().camera().coordonneesPoint2D( p, zBuffer);

        for (int i = -2; i <= 2; i++)
            for (int j = -2; j <= 2; j++) {
                int x = (int) point.getX() + i;
                int y = (int) point.getY() + j;
                BufferedImage lastImage = (BufferedImage) getMain().getUpdateView().getzRunner().getLastImage();
                if ((x >= 0) && (x < lastImage.getWidth()) && (y >= 0) && (y < lastImage.getHeight())) {
                    lastImage.setRGB(x, y, color.getRGB());
                }
            }
    }
}
