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
