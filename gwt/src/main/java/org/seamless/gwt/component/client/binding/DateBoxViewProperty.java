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
import com.google.gwt.user.datepicker.client.DateBox;
import org.seamless.gwt.theme.shared.client.ThemeStyle;
import org.seamless.gwt.validation.shared.ValidationError;

import java.util.Date;

/**
 * @author Christian Bauer
 */
public class DateBoxViewProperty extends FormViewProperty<Date> {

    protected DateBox dateBox;
    protected String errorStyleName;

    public DateBoxViewProperty(DateBox dateBox) {
        this(null, dateBox, ThemeStyle.FormErrorField());
    }

    public DateBoxViewProperty(HasWidgets errorPanel, DateBox dateBox) {
        this(errorPanel, dateBox, ThemeStyle.FormErrorField());
    }

    public DateBoxViewProperty(DateBox dateBox, String errorStyleName) {
        this(null, dateBox, errorStyleName);
    }

    public DateBoxViewProperty(HasWidgets errorPanel, DateBox dateBox, String errorStyleName) {
        super(errorPanel);
        this.dateBox = dateBox;
        this.errorStyleName = errorStyleName;
    }

    @Override
    public void reset() {
        set(null);
    }

    @Override
    public void set(Date value) {
        clearValidationError();
        dateBox.setValue(value);
    }

    @Override
    public Date get() {
        return dateBox.getValue();
    }

    @Override
    public void showValidationError(ValidationError error) {
        super.showValidationError(error);
        dateBox.addStyleName(errorStyleName);
    }

    @Override
    public void clearValidationError() {
        super.clearValidationError();
        dateBox.removeStyleName(errorStyleName);
    }
}
