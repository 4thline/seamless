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
