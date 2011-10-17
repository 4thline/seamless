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

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TextBoxBase;
import org.seamless.gwt.theme.shared.client.ThemeStyle;
import org.seamless.gwt.validation.shared.ValidationError;

import java.math.BigDecimal;

/**
 * @author Christian Bauer
 */
public class BigDecimalViewProperty extends FormViewProperty<BigDecimal> {

    protected TextBoxBase textBox;
    protected String errorStyleName;

    public BigDecimalViewProperty(TextBoxBase textBox) {
        this(null, textBox, ThemeStyle.FormErrorField());
    }

    public BigDecimalViewProperty(HasWidgets errorPanel, TextBoxBase textBox) {
        this(errorPanel, textBox, ThemeStyle.FormErrorField());
    }

    public BigDecimalViewProperty(TextBoxBase textBox, String errorStyleName) {
        this(null, textBox, errorStyleName);
    }

    public BigDecimalViewProperty(HasWidgets errorPanel, TextBoxBase textBox, String errorStyleName) {
        super(errorPanel);
        this.textBox = textBox;
        this.errorStyleName = errorStyleName;
    }

    @Override
    public void reset() {
        set(null);
    }

    @Override
    public void set(BigDecimal value) {
        clearValidationError();
        textBox.setValue(value == null ? "0" : value.toString(), true);
    }

    @Override
    public BigDecimal get() {
        if (textBox.getValue().length() == 0) return null;
        try {
            return new BigDecimal(textBox.getValue());
        } catch (Exception ex) {
            // Well...
        }
        return null;
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
