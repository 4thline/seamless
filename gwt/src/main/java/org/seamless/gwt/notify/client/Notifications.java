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

import com.google.gwt.user.client.Timer;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Christian Bauer
 */
public class Notifications implements NotifyEvent.Handler {

    public static final int DISPLAY_TIME_MILLIS = 5000;

    final protected Queue<Message> queue = new LinkedList();
    final protected Timer displayTimer;

    protected NotificationDisplay display;

    @Inject
    public Notifications(NotificationDisplay display) {
        this.display = display;

        displayTimer = new Timer() {
            @Override
            public void run() {
                // If the head of the queue is a modal message, don't
                // remove it, it stays visible until the next message
                // arrives
                Message message = queue.peek();
                if (message != null && !message.isModal()) {
                    getDisplay().removeMessage(queue.poll());
                }

                if (queue.isEmpty()) cancel();
            }
        };
    }

    @Override
    public void onShow(NotifyEvent event) {
        pushMessage(event.getMessage());
    }

    public NotificationDisplay getDisplay() {
        return display;
    }

    public void pushMessage(Message message) {

        // If there is still a modal message visible, remove it now
        Iterator<Message> it = queue.iterator();
        while (it.hasNext()) {
            Message next = it.next();
            if (next.isModal()) {
                it.remove();
                getDisplay().removeMessage(next);
            }
        }

        // Start the timer but only if the queue was empty before the push
        // and the message is not modal
        if (message != null && !message.isModal() && queue.size() == 0) {
            displayTimer.cancel();
            displayTimer.scheduleRepeating(getDisplayTimeoutMillis());
        }

        if (message != null) {
            queue.add(message);
            getDisplay().showMessage(message);
        }
    }

    protected int getDisplayTimeoutMillis() {
        return DISPLAY_TIME_MILLIS;
    }
}
