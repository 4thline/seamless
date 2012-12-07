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
