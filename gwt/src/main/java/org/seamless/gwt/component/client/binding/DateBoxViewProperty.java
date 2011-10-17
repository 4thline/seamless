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
