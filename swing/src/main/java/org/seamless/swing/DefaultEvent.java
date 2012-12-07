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
package org.seamless.swing;

import java.util.HashSet;
import java.util.Set;

/**
 * An event with an Object payload.
 * <p>
 * Instantiate an event, put an optional payload into it fire it with the API of a controller.
 *
 * @author Christian Bauer
 */
public class DefaultEvent<PAYLOAD> implements Event {

    PAYLOAD payload;
    Set<Controller> firedInControllers = new HashSet();

    public DefaultEvent() {}

    public DefaultEvent(PAYLOAD payload) {
        this.payload = payload;
    }

    public PAYLOAD getPayload() {
        return payload;
    }

    public void setPayload(PAYLOAD payload) {
        this.payload = payload;
    }

    public void addFiredInController(Controller seenController) {
        firedInControllers.add(seenController);
    }

    public boolean alreadyFired(Controller controller) {
        return firedInControllers.contains(controller);
    }
}
