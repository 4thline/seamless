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
package org.seamless.swing;


import javax.swing.table.AbstractTableModel;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A table model for Swing that works easily with rows of JavaBeans.
 *
 * @author Christian Bauer
 */
public class BeanTableModel<T> extends AbstractTableModel {

    private Class<T> beanClass;
    private List<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>();
    private List<T> rows;

    public BeanTableModel(Class<T> beanClass, Collection<T> rows) {
        this.beanClass = beanClass;
        this.rows = new ArrayList<T>(rows);
    }

    public String getColumnName(int column) {
        return properties.get(column).getDisplayName();
    }

    public int getColumnCount() {
        return properties.size();
    }

    public int getRowCount() {
        return rows.size();
    }

    public Object getValueAt(int row, int column) {
        Object value = null;
        T entityInstance = rows.get(row);
        if (entityInstance != null) {
            PropertyDescriptor property = properties.get(column);
            Method readMethod = property.getReadMethod();
            try {
                value = readMethod.invoke(entityInstance);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return value;
    }

    public void addColumn(String displayName, String propertyName) {
        try {
            PropertyDescriptor property =
                    new PropertyDescriptor(propertyName, beanClass, propertyName, null);
            property.setDisplayName(displayName);
            properties.add(property);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void resetColumns() {
        properties = new ArrayList<PropertyDescriptor>();
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(Collection<T> rows) {
        this.rows = new ArrayList<T>(rows);
        fireTableDataChanged();
    }

    public void addRow(T value) {
        rows.add(value);
        fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
    }
    
    public void removeRow(int row) {
        if (rows.size() > row && row != -1) {
            rows.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }

    public void setRow(int row, T entityInstance) {
        rows.remove(row);
        rows.add(row, entityInstance);
        fireTableDataChanged();
    }

}