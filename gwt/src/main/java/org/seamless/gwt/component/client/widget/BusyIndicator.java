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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import org.seamless.gwt.theme.shared.client.ThemeBundle;
import org.seamless.gwt.theme.shared.client.ThemeStyle;

import javax.inject.Inject;

public class BusyIndicator extends SimplePanel {

    public BusyIndicator() {
        this((ThemeBundle) GWT.create(ThemeBundle.class));
    }

    @Inject
    public BusyIndicator(ThemeBundle themeBundle) {
        setStyleName(ThemeStyle.BusyIndicator());
        add(new Image(themeBundle.iconMisc().busy()));
    }

}
