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