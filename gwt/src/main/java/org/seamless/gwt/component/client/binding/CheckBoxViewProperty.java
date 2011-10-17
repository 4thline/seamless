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

import com.google.gwt.user.client.ui.CheckBox;

/**
 * @author Christian Bauer
 */
public class CheckBoxViewProperty implements ViewProperty<Boolean> {

    protected CheckBox checkBox;

    public CheckBoxViewProperty(CheckBox checkBox) {
        this.checkBox = checkBox;
        reset();
    }

    @Override
    public void reset() {
        checkBox.setValue(false);
    }

    @Override
    public void set(Boolean value) {
        checkBox.setValue(value);
    }

    @Override
    public Boolean get() {
        return checkBox.getValue();
    }

}
