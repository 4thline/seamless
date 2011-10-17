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

import org.seamless.gwt.validation.shared.ValidationError;

/**
 * @author Christian Bauer
 */
public class MockValidatableViewProperty<T> implements ValidatableViewProperty<T> {

    public T value;
    public boolean validationErrorVisible;


    @Override
    public void reset() {
        value = null;
    }

    @Override
    public void set(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void showValidationError(ValidationError error) {
        validationErrorVisible = true;
    }

    @Override
    public void clearValidationError() {
        validationErrorVisible = false;
    }
}
