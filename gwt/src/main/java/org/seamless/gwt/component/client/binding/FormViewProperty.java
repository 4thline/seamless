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

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import org.seamless.gwt.theme.shared.client.ThemeStyle;
import org.seamless.gwt.validation.shared.ValidationError;

/**
 * @author Christian Bauer
 */
public abstract class FormViewProperty<T> implements ValidatableViewProperty<T> {

    protected HasWidgets errorPanel;

    protected FormViewProperty() {
    }

    protected FormViewProperty(HasWidgets errorPanel) {
        this.errorPanel = errorPanel;
    }

    @Override
    public void showValidationError(ValidationError error) {
        if (errorPanel != null) {
            Label l = new Label(error.getMessage());
            l.setStyleName(ThemeStyle.ErrorMessage());
            errorPanel.add(l);
        }
    }

    @Override
    public void clearValidationError() {
        if (errorPanel != null) {
            errorPanel.clear();
        }
    }

}
