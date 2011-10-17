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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.seamless.gwt.theme.shared.client.ThemeBundle;
import org.seamless.util.math.Point;
import org.seamless.util.math.Rectangle;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Shows messages in floating rectangles, smart positioning and overlap detection.
 *
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.sl-PopupNotification</dt>
 * <dd>the container of each message</dd>
 * <dt>.sl-PopupNotificationIconLevel</dt>
 * <dd>the icon shown for severity level</dd>
 * <dt>.sl-PopupNotificationIconBusy</dt>
 * <dd>the icon shown for busy status</dd>
 * <dt>.sl-PopupNotificationTitle</dt>
 * <dd>title text</dd>
 * <dt>.sl-PopupNotificationDetail</dt>
 * <dd>detail text</dd>
 * </dl>
 *
 * @author Christian Bauer
 */
public class PopupNotificationDisplay implements NotificationDisplay {

    public static final int MARGIN_BOTTOM_PIXEL = 10;
    public static final int MARGIN_RIGHT_PIXEL = 25;

    protected Map<Message, PopupMessagePanel> messagePanels = new HashMap();

    final protected ThemeBundle themeBundle;

    public PopupNotificationDisplay() {
        this.themeBundle = GWT.create(ThemeBundle.class);
    }

    @Inject
    public PopupNotificationDisplay(ThemeBundle themeBundle) {
        this.themeBundle = themeBundle;
    }

    @Override
    public void showMessage(final Message message) {

        final PopupMessagePanel messagePanel = new PopupMessagePanel(message);

        messagePanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                int originLeft = Window.getScrollLeft() + Window.getClientWidth() - offsetWidth - MARGIN_RIGHT_PIXEL;
                int originTop = Window.getScrollTop() + Window.getClientHeight() - offsetHeight - MARGIN_BOTTOM_PIXEL;
                setRelativePosition(
                    messagePanels.values(),
                    messagePanel,
                    originLeft, originTop, originTop,
                    offsetHeight, offsetWidth
                );
            }
        });

        messagePanels.put(message, messagePanel);
    }

    @Override
    public void removeMessage(Message message) {
        if (messagePanels.containsKey(message)) {
            messagePanels.get(message).hide();
            messagePanels.remove(message);
        }
    }

    class PopupMessagePanel extends DecoratedPopupPanel {

        VerticalPanel content = new VerticalPanel();
        Message message;

        PopupMessagePanel(final Message message) {
            super(false, message.isModal());

            setGlassEnabled(message.isModal());

            this.message = message;

            setWidget(content);
            content.addStyleName(NotifyStyle.PopupNotification());

            HorizontalPanel titlePanel = new HorizontalPanel();
            titlePanel.addStyleName(NotifyStyle.PopupNotificationTitlePanel());

            Image levelImage;
            if (message.getLevel().equals(Level.WARNING) ||
                message.getLevel().equals(Level.SEVERE)) {
                levelImage = new Image(themeBundle.icon16().warn());
            } else {
                levelImage = new Image(themeBundle.icon16().info());
            }

            if (message.isModal()) {
                Image busyImage = new Image(themeBundle.iconMisc().busy());
                busyImage.addStyleName(NotifyStyle.PopupNotificationIconBusy());
                titlePanel.add(busyImage);
            } else {
                PushButton levelButton = new PushButton(levelImage);
                levelButton.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        removeMessage(message);
                    }
                });
                levelButton.addStyleName(NotifyStyle.PopupNotificationIconLevel());
                titlePanel.add(levelButton);
            }

            Label titleLabel = new Label(message.getTitle());
            titleLabel.addStyleName(NotifyStyle.PopupNotificationTitle());
            titlePanel.add(titleLabel);
            titlePanel.setCellWidth(titleLabel, "100%");

            if (!message.isModal()) {
                Button closeButton = new Button("X");
                closeButton.addStyleName(NotifyStyle.PopupNotificationCloseButton());
                closeButton.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        removeMessage(message);
                    }
                });
                titlePanel.add(closeButton);
            }

            content.add(titlePanel);

            Label detailLabel = new Label(message.getDetail());
            detailLabel.addStyleName(NotifyStyle.PopupNotificationDetail());
            content.add(detailLabel);
        }

    }

    protected void setRelativePosition(Collection<PopupMessagePanel> messagePanels,
                                       PopupMessagePanel messagePanel,
                                       int desiredLeft, int desiredTop, int originTop,
                                       int offsetHeight, int offsetWidth) {

        for (PopupPanel existingPanel : messagePanels) {

            Rectangle existingRec =
                new Rectangle(
                    new Point(existingPanel.getPopupLeft(), existingPanel.getPopupTop()),
                    existingPanel.getOffsetWidth(), existingPanel.getOffsetHeight()
                );

            Rectangle newRec =
                new Rectangle(
                    new Point(desiredLeft, desiredTop),
                    offsetWidth, offsetHeight
                );

            // Detect collision with existing panel in grid
            if (existingRec.isOverlapping(newRec)) {

                // Calculate new grid position
                int newTop = desiredTop - offsetHeight - MARGIN_BOTTOM_PIXEL;
                if (newTop < 0) {
                    desiredTop = originTop;
                    desiredLeft = desiredLeft - offsetWidth - MARGIN_RIGHT_PIXEL;
                } else {
                    desiredTop = newTop;
                }
                // Recursive processing until a free slot in the grid is found
                setRelativePosition(
                    messagePanels,
                    messagePanel,
                    desiredLeft, desiredTop, originTop,
                    offsetHeight, offsetWidth
                );
                return;
            }
        }
        messagePanel.setPopupPosition(desiredLeft, desiredTop);
    }

}
