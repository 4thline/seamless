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

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import org.seamless.gwt.notify.client.NotificationDisplay;
import org.seamless.gwt.notify.client.PopupNotificationDisplay;

public class DemoModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(EventBus.class)
            .to(SimpleEventBus.class)
            .in(Singleton.class);
    }

    @Provides
    @Singleton
    NotificationDisplay getNotificationDisplay(Bundle bundle) {
        return new PopupNotificationDisplay(bundle.themeBundle().create());
    }

}
