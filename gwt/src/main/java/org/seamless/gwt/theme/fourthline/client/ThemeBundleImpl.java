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
package org.seamless.gwt.theme.fourthline.client;

import com.google.gwt.resources.client.GwtCreateResource;
import com.google.gwt.user.cellview.client.CellTable;
import org.seamless.gwt.theme.shared.client.ThemeBundle;

public interface ThemeBundleImpl extends ThemeBundle {

    @Override
    @GwtCreateResource.ClassType(CellTableResources.class)
    GwtCreateResource<CellTable.Resources> cellTableResources();

    /* This is how you override shared images in a theme:

    Icon16 icon16();

    public interface Icon16 extends ThemeBundle.Icon16 {

        @Source("override.png")
        ImageResource edit();

    }

    */
}
