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
import com.google.gwt.user.client.ui.TextBoxBase;
import org.seamless.gwt.theme.shared.client.ThemeStyle;
import org.seamless.gwt.validation.shared.ValidationError;

/**
 * @author Christian Bauer
 */
public class TextBoxViewProperty extends FormViewProperty<String> {

    protected TextBoxBase textBox;
    protected String errorStyleName;

    public TextBoxViewProperty(TextBoxBase textBox) {
        this(null, textBox, ThemeStyle.FormErrorField());
    }

    public TextBoxViewProperty(HasWidgets errorPanel, TextBoxBase textBox) {
        this(errorPanel, textBox, ThemeStyle.FormErrorField());
    }

    public TextBoxViewProperty(TextBoxBase textBox, String errorStyleName) {
        this(null, textBox, errorStyleName);
    }

    public TextBoxViewProperty(HasWidgets errorPanel, TextBoxBase textBox, String errorStyleName) {
        super(errorPanel);
        this.textBox = textBox;
        this.errorStyleName = errorStyleName;
    }

    @Override
    public void reset() {
        set(null);
    }

    @Override
    public void set(String value) {
        clearValidationError();
        textBox.setValue(value);
    }

    @Override
    public String get() {
        return textBox.getValue();
    }

    @Override
    public void showValidationError(ValidationError error) {
        super.showValidationError(error);
        textBox.addStyleName(errorStyleName);
    }

    @Override
    public void clearValidationError() {
        super.clearValidationError();
        textBox.removeStyleName(errorStyleName);
    }

}
