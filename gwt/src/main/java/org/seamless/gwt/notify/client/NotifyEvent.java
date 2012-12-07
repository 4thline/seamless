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
