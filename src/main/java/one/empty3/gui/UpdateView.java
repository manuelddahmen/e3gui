package one.empty3.gui;

import javax.swing.*;

/**
 * Created by manue on 26-06-19.
 */
public class UpdateView extends JPanel {
    public UpdateView() {
        super();
        setView(new FunctionView());
        setzRunner(new ZRunner());
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
    private ZRunner zRunner;

    public ZRunner getzRunner() {
        return zRunner;
    }

    public void setzRunner(ZRunner zRunner) {
        ZRunner old = this.zRunner;
        this.zRunner = zRunner;
        zRunner.setUpdateView(this);
        getView().addPropertyChangeListener(getzRunner());
        addPropertyChangeListener(getzRunner());
        firePropertyChange("zRunner", old, zRunner);
    }

}
