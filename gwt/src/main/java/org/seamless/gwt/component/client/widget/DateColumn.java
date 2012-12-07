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
package org.seamless.gwt.component.client.widget;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.TextColumn;

import java.util.Date;

/**
 * @author Christian Bauer
 */
public abstract class DateColumn<T> extends TextColumn<T> {

    DateTimeFormat fmt = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM);

    public void setDateFormat(String pattern) {
        fmt = DateTimeFormat.getFormat(pattern);
    }

    @Override
    public void render(Cell.Context context, T object, SafeHtmlBuilder sb) {
        sb.appendHtmlConstant("<span style=\"white-space:nowrap;\">");
        super.render(context, object, sb);
        sb.appendHtmlConstant("</span>");
    }

    @Override
    public String getValue(T object) {
        return fmt.format(getDate(object));
    }

    abstract protected Date getDate(T object);

}
