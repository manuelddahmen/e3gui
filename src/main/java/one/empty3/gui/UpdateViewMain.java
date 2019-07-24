package one.empty3.gui;

import one.empty3.library.Scene;

import javax.swing.*;

/**
 * Created by manue on 26-06-19.
 */
public class UpdateViewMain extends JPanel {
    private Main ff;
    private Scene scene;

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

}
