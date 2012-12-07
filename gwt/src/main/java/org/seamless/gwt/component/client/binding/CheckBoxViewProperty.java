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
