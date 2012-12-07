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
package org.seamless.gwt.component.client.binding;

import com.google.gwt.user.client.ui.ListBox;
import org.seamless.util.time.DateFormat;

/**
 * @author Christian Bauer
 */
public class DateFormatViewProperty implements ViewProperty<DateFormat> {

    protected ListBox listBox;

    public DateFormatViewProperty(ListBox listBox) {
        this.listBox = listBox;
        reset();
    }

    @Override
    public void reset() {
        listBox.clear();
        for (DateFormat.Preset preset : DateFormat.Preset.values()) {
            listBox.addItem(preset.getDateFormat().getLabel());
        }
    }

    @Override
    public void set(DateFormat value) {
        for (int i = 0; i < DateFormat.Preset.values().length; i++) {
            DateFormat.Preset preset = DateFormat.Preset.values()[i];
            if (preset.getDateFormat().equals(value)) {
                listBox.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public DateFormat get() {
        int idx = DateFormatViewProperty.this.listBox.getSelectedIndex();
        return DateFormat.Preset.values()[idx].getDateFormat();
    }

}
