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

package org.seamless.gwt.component.client;

import com.google.web.bindery.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Bauer
 */
public abstract class AbstractEventListeningPresenter implements EventListeningPresenter {

    final protected List<HandlerRegistration> registrations = new ArrayList();

    protected void addRegistration(HandlerRegistration registration) {
        registrations.add(registration);
    }

    public void stop() {
        for (HandlerRegistration registration : registrations) {
            registration.removeHandler();
        }
        registrations.clear();
    }
}
