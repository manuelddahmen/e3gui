package one.empty3.gui;

import one.empty3.library.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MeshGEditorThread extends Thread implements PropertyChangeListener {


    private Main main;
    private ArrayList<Point3D> pointsTranslate = new ArrayList<Point3D>();

    MeshGEditorThread(Main main) {
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
                        System.out.println("Mouse clicked in " + this.getClass());
                        if (getMain().getGraphicalEdit2().getActionToPerform().equals(GraphicalEdit2.Action.SELECT)) {
                            if (main.getGraphicalEdit2().isSelectArbitraryPoints()) {
                                Point3D selectedPoint = getMain().getUpdateView().getzRunner().getzBuffer().clickAt(e.getX(), e.getY());
                                main.getGraphicalEdit2().add(selectedPoint);
                                System.out.println("point added" + selectedPoint);
                            } else if (main.getGraphicalEdit2().isSelectingMultipleObjects()) {
                                Representable multiple = getMain().getUpdateView().getzRunner().getzBuffer().representableAt(e.getX(), e.getY());
                                main.getGraphicalEdit2().add(multiple);
                                System.out.println("representable added" + multiple);
                            } else {
                                List<ModelBrowser.Cell> cellList;
                                cellList = new ModelBrowser(getMain().getUpdateView().getzRunner().getzBuffer(), main.getDataModel().getScene(), Point3D.class).getObjects();
                                System.out.println("Select point ADD/REMOVE from selected points list");

                                if (cellList != null) {
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
                                        main.getGraphicalEdit2().getCurrentSelection().forEach(new Consumer<Representable>() {
                                            @Override
                                            public void accept(Representable representable) {
                                                System.out.println("[selection from GraphicalEdit]" + representable);
                                            }

                                        });
                                    });

                                } else {
                                    System.out.println("cellList == null" + this.getClass());
                                }

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
                            Camera camera = main.getUpdateView().getzRunner().getzBuffer().camera();
                            /*Point3D invert = zBuffer.invert(new Point3D(location.getX(), location.getY(), 0d),
                                    main.getUpdateView().getzRunner().getzBuffer().camera());//TODO
                            */
                            Point2D point2D = new Point2D((int) location.getX(), (int) location.getY());

                            Point3D invert = zBuffer.invert(point2D, camera,
                                    camera.getLookat().moins(
                                            zBuffer.clickAt(
                                                    location.getX(), location.getY()
                                            )).norme());//TODO


                            Point3D elem = invert;
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
        if (zBuffer.camera() != null) {
            Point point = zBuffer.camera().coordonneesPoint2D(p, zBuffer);
            if (point != null)
                for (int i = -2; i <= 2; i++)
                    for (int j = -2; j <= 2; j++) {
                        int x = (int) point.getX() + i;
                        int y = (int) point.getY() + j;
                        BufferedImage lastImage = (BufferedImage) getMain().getUpdateView().getzRunner().getLastImage();
                        if ((x >= 0) && (x < lastImage.getWidth()) && (y >= 0) && (y < lastImage.getHeight())) {
                            lastImage.setRGB(x, y, color.getRGB());
                        }
                    }
        } else
            System.out.println("Cmaera Z");
    }
}

