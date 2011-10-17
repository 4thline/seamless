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
