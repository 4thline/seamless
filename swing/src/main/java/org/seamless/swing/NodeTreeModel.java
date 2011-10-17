/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.seamless.swing;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * A tree model for Swing that works easily with a tree of <tt>Node</tt> implementors.
 *
 * @see Node
 * @author Christian Bauer
 */
public class NodeTreeModel implements TreeModel {

    private Node rootNode;
    private Node selectedNode;

    public NodeTreeModel(Node rootNode) {
        this.rootNode = rootNode;
    }

    // #################### TreeModel ###############################

    public Object getRoot() {
        return rootNode;
    }

    public boolean isLeaf(Object object) {
        Node node = (Node)object;
        boolean isLeaf = node.getChildren().size() == 0;
        return isLeaf;
    }

    public int getChildCount(Object parent) {
        Node node = (Node)parent;
        return node.getChildren().size();
    }

    public Object getChild(Object parent, int i) {
        Node node = (Node)parent;
        Object child = node.getChildren().get(i);
        return child;
    }

    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null) return -1;
        Node node = (Node)parent;
        int index = node.getChildren().indexOf(child);
        return index;
    }

    // This method is invoked by the JTree only for editable trees.
    // This TreeModel does not allow editing, so we do not implement
    // this method.  The JTree editable property is false by default.
    public void valueForPathChanged(TreePath path, Object newvalue) {}

    // Since this is not an editable tree model, we never fire any events,
    // so we don't actually have to keep track of interested listeners
    public void addTreeModelListener(TreeModelListener l) {}
    public void removeTreeModelListener(TreeModelListener l) {}

}
