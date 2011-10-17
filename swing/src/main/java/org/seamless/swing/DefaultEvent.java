/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
