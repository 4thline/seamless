/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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