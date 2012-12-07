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

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Christian Bauer
 */
public class ValidationError implements IsSerializable {

    // This is just some serializable option for attaching an error
    // to something (e.g. a particular entity instance or list item)
    private String id;

    private String entity;
    private EntityProperty property;
    private String message;

    public ValidationError() {
    }

    public ValidationError(String id, ValidationError template) {
        this(id, template.getEntity(), template.getProperty(), template.getMessage());
    }

    public ValidationError(String id, String entity, EntityProperty property, String message) {
        this.id = id;
        this.entity = entity;
        this.property = property;
        this.message = message;
    }

    public ValidationError(String entity, EntityProperty property, String message) {
        this(null, entity, property, message);
    }

    public String getId() {
        return id;
    }

    public String getEntity() {
        return entity;
    }

    public EntityProperty getProperty() {
        return property;
    }

    public String getMessage() {
        return message;
    }

    public static List<ValidationError> filterEntity(List<ValidationError> list, String entity) {
        List<ValidationError> result = new ArrayList();
        Iterator<ValidationError> it = list.iterator();
        while (it.hasNext()) {
            ValidationError next = it.next();
            if (next.getEntity() != null && next.getEntity().equals(entity)) {
                result.add(next);
                it.remove();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + ", Entity: " + getEntity() + ", Property: " + getProperty() + ", Message: " + getMessage();
    }

}
