package one.empty3.gui;

import one.empty3.library.*;
import one.empty3.library.core.nurbs.CourbeParametriquePolynomiale;
import one.empty3.library.core.nurbs.ParametricSurface;
import one.empty3.library.core.nurbs.SurfaceParametriquePolynomialeBezier;
import one.empty3.library.core.tribase.Plan3D;

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
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MeshGEditorThread extends Thread implements PropertyChangeListener {


    private Main main;
    private final ArrayList<Point3D> pointsTranslate = new ArrayList<Point3D>();
    private Point3D p;
    private ParametricSurface surface;
    public MeshGEditorThread(Main main) {
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
                        // Select point or mark ready to move.
                        if (getMain().getUpdateView().getView().getMeshEditorBean().isSelection()) {
                            Point3D selectedPoint = getMain().getUpdateView().getzRunner().getzBuffer().clickAt(e.getX(), e.getY());
                            Representable selectedObject = getMain().getUpdateView().getzRunner().getzBuffer().getIme().getIME()
                                    .getrMap()[e.getX()][e.getY()];
                            if (selectedObject instanceof ParametricSurface) {
                                ParametricSurface ps = (ParametricSurface) selectedObject;
                                double u = getMain().getUpdateView().getzRunner().getzBuffer().getIme().getIME()
                                        .getuMap()[e.getX()][e.getY()];
                                double v = getMain().getUpdateView().getzRunner().getzBuffer().getIme().getIME()
                                        .getvMap()[e.getX()][e.getY()];

                                getMain().getUpdateView().setRuv(ps, u, v);

                            } else if(ZBufferImpl.INFINITY.equals(selectedPoint)) {
                                int meshType = getMain().getMeshEditorProps().getMeshType();
                                switch(meshType) {
                                    case MeshEditorBean.MESH_EDITOR_ParametricSurface:
                                        break;
                                    case MeshEditorBean.MESH_EDITOR_Sphere:
                                        surface = new Sphere();
                                        break;
                                    case MeshEditorBean.MESH_EDITOR_Cube:
                                        //surface = new Cube();
                                        break;
                                    case MeshEditorBean.MESH_EDITOR_Plane:
                                        surface = new SurfaceParametriquePolynomialeBezier();
                                        break;
                                }
                                if(selectedObject!=null)
                                    getMain().getMeshEditorProps().getInSelection().add(selectedPoint);//getMain().getDataModel().getScene().add(selectedObject);
                            }
                            //main.getDataModel().getScene().add(selectedPoint);
                            System.out.println("point added" + selectedPoint);
                        } else if (main.getMeshEditorProps().isTranslation()) {
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
                                    main.getGraphicalEdit2().getCurrentSelection().forEach(representable -> System.out.println("[selection from GraphicalEdit]" + representable));
                                });

                            } else {
                                System.out.println("cellList == null" + this.getClass());
                            }

                        }


                    }


                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (getMain().getMeshEditorProps().isTranslation()) {
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
                            modelBrowser.translateSelection(elem);
                            System.out.println(main.getGraphicalEdit2().getCurrentSelection());
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
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    private void afterDraw() {
        if (main.getMeshEditorProps().getInSelection().size() > 0)
            browseScene();
    }

    private void browseScene() {
        drawPoints(new ModelBrowser(getMain().getUpdateView().getzRunner().getzBuffer(), main.getDataModel().getScene(), Point3D.class).getObjects());
        drawSelection();
        if (getMain().getMeshEditorProps().isTranslation()) {
            showAxis();
        }
    }

    public void insertRow(StructureMatrix<Point3D> structureMatrix, Point3D p) {
        if (structureMatrix.getDim() == 1) {
            structureMatrix.setElem(p, structureMatrix.getData1d().size());
        }
        if (structureMatrix.getDim() == 2) {
            throw new UnsupportedOperationException("");

        }
    }
    public void insertDefault(StructureMatrix<Point3D> structureMatrix, int row, int col,
                              boolean isRow,
                                      Point3D p) {
        if(structureMatrix.getDim()==0)
            structureMatrix.setElem(p);


        if(structureMatrix.getDim()==1) {
            structureMatrix.getData1d().add(col, p);
        }

        if(structureMatrix.getDim()==2) {
            for(int i=0; i<structureMatrix.getData2d().size(); i++) {
                for(int j=0; j<structureMatrix.getData2d().size(); j++) {
                    if(j==row&&i==col) {
                        structureMatrix.getData2d().add(new ArrayList<>());
                    }
                    if(isRow)
                        if(i==col)
                            structureMatrix.getData2d().get(j).add(col, p);
                    else
                        if(j==row)
                            structureMatrix.getData2d().get(col).add(i, p);

                }
            }
            throw new UnsupportedOperationException("");
        }
    }

    private void showAxis() {

        LineSegment[] lsXYZ = new LineSegment[3];
        Point3D[] vects;
        Point3D p0;
        int i;
        for (Representable r : getMain().getMeshEditorProps().getInSelection()) {
            ZBufferImpl zBuffer = getMain().getUpdateView().getzRunner().getzBuffer();
            if (r instanceof Point3D) {
                Point3D p = (Point3D) r;
                Point p2d = zBuffer.camera().coordonneesPoint2D(p, zBuffer);
                int x = (int) (p2d.getX());
                int y = (int) (p2d.getY());
                Representable r1 = zBuffer.getIme().getIME().getrMap()[x][y];
                double u = zBuffer.getIme().getIME().getuMap()[x][y];
                double v = zBuffer.getIme().getIME().getvMap()[x][y];
                if (r1 instanceof ParametricSurface) {
                    ParametricSurface r11 = (ParametricSurface) (r1);
                    p0 = r11.calculerPoint3D(u, v);
                    Point3D vx = r11.calculerTangenteU(u, v);
                    Point3D vy = r11.calculerTangenteV(u, v);
                    Point3D vz = r11.calculerNormale3D(u, v);

                    vects = new Point3D[]{vx, vy, vz};
                    i = 0;
                    for (Point3D pv : vects) {
                        try {

                            lsXYZ[i] = new LineSegment(p0,
                                    pv.mult(10.0).plus(p0));
                            Point p1 = zBuffer.camera().coordonneesPoint2D(lsXYZ[i].getOrigine(), zBuffer);
                            Point p2 = zBuffer.camera().coordonneesPoint2D(lsXYZ[i].getExtremite(), zBuffer);

                            new ModelBrowser(getMain().getDataModel().getScene(), zBuffer).getObjects().forEach(
                                    cell -> {
                                        drawPoint(cell.getpRot(), Color.YELLOW);
                                        Graphics g = getMain().getUpdateView().getGraphics();
                                        Point point = zBuffer.camera().coordonneesPoint2D(cell.getpRot(), zBuffer);
                                        g.drawString("(r:" + cell.getRow() + "; c" + cell.getRow() + ")", (int) point.getX(), (int) point.getY());
                                    });
                            getMain().getMeshEditorProps().getInSelectionMoves().forEach((pMove, point3D) -> drawPoint(pMove.getPout(), Color.BLUE));
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
            } else {

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
        ArrayList<Point3D> list;
        list = main.getMeshEditorProps().getInSelection();
        if (list != null) {
            list.forEach(cell -> {
                try {
                    if (cell != null) {
                        if (getMain().getUpdateView().getzRunner().getLastImage() != null)
                            drawPoint((Point3D) cell, Color.RED);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
        drawPoint(getMain().getUpdateView().getRuv(), Color.YELLOW);
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

