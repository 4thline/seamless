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
