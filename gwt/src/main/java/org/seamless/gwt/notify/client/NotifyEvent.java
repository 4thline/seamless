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

package org.seamless.gwt.notify.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

/**
 * @author Christian Bauer
 */
public class NotifyEvent extends Event<NotifyEvent.Handler> {

    public interface Handler extends EventHandler {
        public void onShow(NotifyEvent event);
    }

    public static Type<Handler> TYPE = new Type<Handler>();

    Message message;

    public NotifyEvent() {
    }

    public NotifyEvent(String title, String detail) {
        this(new Message(title, detail));
    }

    public NotifyEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onShow(this);
    }

    @Override
    public String toString() {
        return "Show Message Event - " + getMessage();
    }
}
