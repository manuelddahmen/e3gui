package one.empty3.gui;

import javax.swing.*;

/**
 * Created by manue on 26-06-19.
 */
public class UpdateView extends JPanel {
    private FormFunction ff;

    public UpdateView() {
        setView(new FunctionView());
        setzRunner(new ZRunner());
    }
    public void setFF(FormFunction ff )
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
    private ZRunner zRunner;

    public ZRunner getzRunner() {
        return zRunner;
    }

    public void setzRunner(ZRunner zRunner) {
        ZRunner old = this.zRunner;
        this.zRunner = zRunner;
        getView().addPropertyChangeListener(getzRunner());
        addPropertyChangeListener(getzRunner());
        firePropertyChange("zRunner", old, zRunner);
    }

}
