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

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * TODO: Fixing the only place where we use PX in the whole app requires this:
 * http://gwt-code-reviews.appspot.com/1244801/show
 *
 * @author Christian Bauer
 */
public class ResizableSplitLayoutPanel extends SplitLayoutPanel {

    public double getSplitPosition(Widget widget) {
        LayoutData layout = (LayoutData) widget.getLayoutData();
        return layout.size;
    }

    public double setSplitPosition(Widget widget, double size, boolean animate) {
        LayoutData layout = (LayoutData) widget.getLayoutData();
        layout.oldSize = layout.size;
        layout.size = size;
        if (animate)
            animate(100);
        else
            forceLayout();
        return layout.oldSize;
    }

}
