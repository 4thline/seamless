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
