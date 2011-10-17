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

package org.seamless.gwt.validation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * Thrown if integrity rule violations are exceptional, encapsulating the errors.
 *
 * @author Christian Bauer
 */
public class ValidationException extends Exception implements IsSerializable {

    public List<ValidationError> errors;

    public ValidationException() {
    }

    public ValidationException(String s) {
        super(s);
    }

    public ValidationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ValidationException(String s, List<ValidationError> errors) {
        super(s);
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return errors != null && errors.size() > 0;
    }
}