package one.empty3.gui;

import javax.swing.tree.DefaultMutableTreeNode;

public class E3TreeNode extends DefaultMutableTreeNode
{
    public E3TreeNode(Object o)
    {
        super(o.toString());
        switch (o.getClass().getName())
        {

        }
    }
}