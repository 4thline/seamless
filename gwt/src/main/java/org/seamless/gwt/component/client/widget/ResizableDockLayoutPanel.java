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

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Christian Bauer
 */
public class ResizableDockLayoutPanel extends DockLayoutPanel {

    public ResizableDockLayoutPanel(Style.Unit unit) {
        super(unit);
    }

    public void setWidgetLayoutData(Widget widget, double size) {
        setWidgetLayoutData(widget, null, size);
    }

    public void setWidgetLayoutData(Widget widget, Direction direction, double size) {
        LayoutData data = (LayoutData) widget.getLayoutData();
        data.size = size;
        if (direction != null) {
            data.direction = direction;
        }
    }



}
