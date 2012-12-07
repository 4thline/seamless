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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.seamless.gwt.notify.client.NotifyEvent;

public class DemoEntryPoint implements EntryPoint {

    private final DemoGinjector injector = GWT.create(DemoGinjector.class);

    public void onModuleLoad() {

        injector.getEventBus().addHandler(
            NotifyEvent.TYPE,
            injector.getNotifications()
        );

        injector.getEventBus().fireEvent(
            new NotifyEvent("Demo page", "This is a demo.")
        );
    }
}
