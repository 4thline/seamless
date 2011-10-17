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

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * http://groups.google.com/group/google-web-toolkit/browse_thread/thread/32813208b04af6e6/52afd90c87d6592b
 *
 * @author Christian Bauer
 */
public class SimpleLayoutPanel extends LayoutPanel implements AcceptsOneWidget {

    @Override
    public void setWidget(IsWidget w) {
        this.clear();
        Widget widget = Widget.asWidgetOrNull(w);
        if (widget != null) {
            this.add(widget);
        }
    }
}