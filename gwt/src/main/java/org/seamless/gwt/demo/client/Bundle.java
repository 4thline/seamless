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
package org.seamless.gwt.demo.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.GwtCreateResource;
import com.google.gwt.resources.client.ImageResource;
import org.seamless.gwt.theme.shared.client.ThemeBundle;

/**
 * @author Christian Bauer
 */
public interface Bundle extends ClientBundle {

    @GwtCreateResource.ClassType(
        org.seamless.gwt.theme.fourthline.client.ThemeBundleImpl.class
    )
    GwtCreateResource<ThemeBundle> themeBundle();

    Icon48 icon48();

    public interface Icon48 extends ClientBundle {

        @Source("icon48/report.png")
        ImageResource report();

        @Source("icon48/currency.png")
        ImageResource currency();

    }
}
