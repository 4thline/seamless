package com.google.gwt.user.client.ui;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * The GWT CellTree is unusable in 2.3 so we have to make Tree usable.
 * <p>
 * All glory to the <del>hypno toad</del>package visibility level!
 * </p>
 *
 * @author Christian Bauer
 */
public class UsableTree extends Tree {

    public UsableTree() {
        this(null);
    }

    public UsableTree(Resources resources) {
        this(resources, false);
        addSelectionHandler(new MultiSelectionHandler());
    }

    public UsableTree(Resources resources, boolean useLeafImages) {
        super(resources, useLeafImages);
        addSelectionHandler(new MultiSelectionHandler());
    }

    @Override
    void maybeUpdateSelection(TreeItem itemThatChangedState, boolean isItemOpening) {
        /** Bugfix
         *
         * http://code.google.com/p/google-web-toolkit/issues/detail?id=3660
         *
         * Don't mess with the selection if the item's state changed, avoid
         * double "click" for selection handlers on the tree. This is the
         * comment in TreeItem.java that calls this method:
         *
         * "We may need to update the tree's selection in response to a tree state change. For
         *  example, if the tree's currently selected item is a descendant of an item whose
         *  branch was just collapsed, then the item itself should become the newly-selected item."
         *
         * No, it shouldn't. How do you think items get expanded/collapsed? They get selected!
         *
         * If we just closed the item then let's de-select it.
         */
        if (!isItemOpening) {
            TreeItem tempItem = getSelectedItem();
            if (tempItem != null && tempItem.isSelected())
                tempItem.setSelected(false);
        }
    }

    /* ################################################################################## */
    // Feature: That is actually useful, reducing the pain from bad
    // design MultiSelectionModel vs. Tree - the only model-backed widgets are
    // really the new CellTable, CellTree, etc. This simulates a multi-selection
    // model - yes, I should really not force you to subclass and just delegate
    // to the already existing MultiSelectionModel but I had most of this before
    // I even found CellTree.

    public static class MultiSelectionHandler implements SelectionHandler<TreeItem> {
        @Override
        public void onSelection(SelectionEvent<TreeItem> event) {
            // Keep track of the multi selection state when "single" selection occurred
            if (event.getSelectedItem() instanceof MultiSelectableTreeItem) {
                ((MultiSelectableTreeItem) event.getSelectedItem()).flipMultiSelected();
            }
        }
    }

    public static class MultiSelectableTreeItem extends TreeItem {

        final boolean cascading;
        String multiSelectStyle;
        boolean multiSelected;

        public MultiSelectableTreeItem(Widget widget) {
            this(widget, false);
        }

        public MultiSelectableTreeItem(Widget widget, boolean cascading) {
            this.cascading = cascading;
            setWidget(widget);
        }

        public boolean isCascading() {
            return cascading;
        }

        public boolean isMultiSelected() {
            return multiSelected;
        }

        public void setMultiSelected(boolean selected) {
            this.multiSelected = selected;
            if (getMultiSelectStyle() == null) return; // Don't show on UI
            if (multiSelected)
                getWidget().addStyleName(getMultiSelectStyle());
            else
                getWidget().removeStyleName(getMultiSelectStyle());
        }

        public String getMultiSelectStyle() {
            return multiSelectStyle;
        }

        public void setMultiSelectStyle(String multiSelectStyle) {
            this.multiSelectStyle = multiSelectStyle;
        }

        @Override
        public MultiSelectableTreeItem getChild(int index) {
            return (MultiSelectableTreeItem) super.getChild(index);
        }

        public void flipMultiSelected() {
            boolean targetState = !multiSelected; // Flip
            this.flipMultiSelected(targetState);
        }

        protected void flipMultiSelected(boolean targetState) {
            setMultiSelected(targetState);
            if (isCascading()) {
                for (int i = 0; i < getChildCount(); i++) {
                    MultiSelectableTreeItem child = getChild(i);
                    child.flipMultiSelected(targetState);
                }
            }
        }

        public List<MultiSelectableTreeItem> getMultiSelected() {
            // Recursive get of all lists of tree
            List<MultiSelectableTreeItem> list = new ArrayList();
            addMultiSelected(list);
            return list;
        }

        protected void addMultiSelected(List items) {
            // Flatten the tree into the given list
            //
            // When the current state is queried, only add this item if it's selected
            // and if no subclass says it shouldn't be selected (e.g. transient visual
            // grouping nodes).
            if (isMultiSelected() && isAddedToMultiSelection())
                items.add(this);
            // But always do the recursive addition, other node's might want to
            // be added
            for (int i = 0; i < getChildCount(); i++) {
                MultiSelectableTreeItem child = getChild(i);
                child.addMultiSelected(items);
            }
        }

        protected boolean isAddedToMultiSelection() {
            return true;
        }
    }

    /* ################################################################################## */
    // Feature: Easy recursive access to nodes, unifying the inconsistent ItemCount/ChildCount API

    public static interface TreeItemVisitor<T extends TreeItem> {
        void visit(T treeItem);
    }

    public void accept(TreeItemVisitor visitor) {
        for (int i = 0; i < getItemCount(); i++) {
            accept(getItem(i), visitor);
        }
    }

    protected void accept(TreeItem treeItem, TreeItemVisitor visitor) {
        visitor.visit(treeItem);
        for (int i = 0; i < treeItem.getChildCount(); i++) {
            accept(treeItem.getChild(i), visitor);
        }
    }
}
