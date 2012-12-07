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
package org.seamless.mock.gwt;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;

import java.lang.Override;import java.lang.String;import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Bauer
 */
public class MockPlaceController extends PlaceController {

    static public class MockDelegate implements Delegate {
        public List<HandlerRegistration> registrations = new ArrayList();

        @Override
        public HandlerRegistration addWindowClosingHandler(Window.ClosingHandler handler) {
            HandlerRegistration reg = new HandlerRegistration() {
                @Override
                public void removeHandler() {
                    registrations.remove(this);
                }
            };
            registrations.add(reg);
            return reg;
        }

        @Override
        public boolean confirm(String message) {
            return true;
        }
    }

    public MockPlaceController(EventBus eventBus) {
        super(eventBus, new MockDelegate());
    }

    public MockPlaceController(EventBus eventBus, MockDelegate delegate) {
        super(eventBus, delegate);
    }
}
