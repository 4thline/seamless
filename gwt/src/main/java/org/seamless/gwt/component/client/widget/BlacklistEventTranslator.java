 /*
 * Copyright (C) 2012 4th Line GmbH, Switzerland
 *
 * The contents of this file are subject to the terms of either the GNU
 * Lesser General Public License Version 2 or later ("LGPL") or the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.seamless.gwt.component.client.widget;

import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;

import java.util.HashSet;
import java.util.Set;

public class BlacklistEventTranslator<T> implements DefaultSelectionEventManager.EventTranslator<T> {

    private final Set<Integer> blacklist = new HashSet<Integer>();

    /**
     * Construct a new {@link BlacklistEventTranslator}.
     *
     * @param blacklistedColumns the columns to blacklist
     */
    public BlacklistEventTranslator(int... blacklistedColumns) {
        if (blacklistedColumns != null) {
            for (int i : blacklistedColumns) {
                setColumnBlacklisted(i, true);
            }
        }
    }

    /**
     * Clear all columns from the blacklist.
     */
    public void clearBlacklist() {
        blacklist.clear();
    }

    public boolean clearCurrentSelection(CellPreviewEvent<T> event) {
        return false;
    }

    /**
     * Check if the specified column is blacklisted.
     *
     * @param index the column index
     * @return true if blacklisted, false if not
     */
    public boolean isColumnBlacklisted(int index) {
        return blacklist.contains(index);
    }

    /**
     * Set whether or not the specified column in blacklisted.
     *
     * @param index         the column index
     * @param isBlacklisted true to blacklist, false to allow selection
     */
    public void setColumnBlacklisted(int index, boolean isBlacklisted) {
        if (isBlacklisted) {
            blacklist.add(index);
        } else {
            blacklist.remove(index);
        }
    }

    public DefaultSelectionEventManager.SelectAction translateSelectionEvent(CellPreviewEvent<T> event) {
        return isColumnBlacklisted(event.getColumn()) ? DefaultSelectionEventManager.SelectAction.IGNORE
                : DefaultSelectionEventManager.SelectAction.DEFAULT;
    }
}