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

import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;

/**
 * Created by manue on 19-11-19.
 */
public class TreeModelSelection implements TreeModel {
    public class MyTreeNode extends DefaultMutableTreeNode {
        private Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
    private MyTreeNode root = new MyTreeNode();
    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((MyTreeNode)parent).getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((MyTreeNode)parent).getChildCount();
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((MyTreeNode)node).getChildCount()==0;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        ((MyTreeNode)path.getLastPathComponent()).setValue(newValue);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((MyTreeNode)parent).getIndex((MyTreeNode)child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {

    }
}
