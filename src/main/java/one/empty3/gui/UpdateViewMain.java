package one.empty3.gui;

import one.empty3.library.Representable;
import one.empty3.library.Scene;

import javax.swing.*;

/**
 * Created by manue on 26-06-19.
 */
public class UpdateViewMain extends JPanel implements RepresentableEditor {
    private Main ff;
    private Scene scene;
    private Representable currentRepresentable;
    private int displayType;

    public UpdateViewMain() {
        setView(new FunctionView());
        setzRunner(new ZRunnerMain());
    }
    public void setFF(Main ff )
    {
        this.ff = ff;
        this.getzRunner().setUpdateView(ff);
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
