package one.empty3.gui;

import javax.swing.*;

/**
 * Created by manue on 27-06-19.
 */
public class UpdateRepresentableClassList extends JTable
{
    public UpdateRepresentableClassList()
    {
        super();
    }
    private RepresentableClassList representableClassList;

    public RepresentableClassList getRepresentableClassList() {
        return representableClassList;
    }

    public void setRepresentableClassList(RepresentableClassList representableClassList) {
        firePropertyChange("classList", this.representableClassList, representableClassList);
        this.representableClassList = representableClassList;
    }
}
