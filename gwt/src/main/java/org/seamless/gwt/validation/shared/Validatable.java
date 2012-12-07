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
package org.seamless.gwt.validation.shared;

import java.lang.String;import java.util.List;

/**
 * @author Christian Bauer
 */
public interface Validatable {

    public static final String GROUP_CLIENT = "validationGroup-client";
    public static final String GROUP_SERVER = "validationGroup-server";

    /**
     * @return An empty <code>List</code> if all rules validated properly, otherwise, the detected errors.
     */
    public List<ValidationError> validate(String group);

}
