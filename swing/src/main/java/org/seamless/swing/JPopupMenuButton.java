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

import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * A button that opens a JPopupMenu on click or on enter key.
 *
 * @author Christian Bauer
 */
public class JPopupMenuButton extends JButton {

    public JPopupMenu popup;

    public JPopupMenuButton(JPopupMenu m) {
        super();
        popup = m;
        enableEvents(AWTEvent.KEY_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    public JPopupMenuButton(String s, JPopupMenu m) {
        super(s);
        popup = m;
        enableEvents(AWTEvent.KEY_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    public JPopupMenuButton(String s, Icon icon, JPopupMenu popup) {
        super(s, icon);
        this.popup = popup;
        enableEvents(AWTEvent.KEY_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);

        setModel(new DefaultButtonModel() {
            @Override
            public void setPressed(boolean b) {
                // Ignore pressed state
            }
        });
    }

    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        int id = e.getID();
        if (id == MouseEvent.MOUSE_PRESSED) {
            if (popup != null) {
                popup.show(this, 0, 0);
            }
        } else if (id == MouseEvent.MOUSE_RELEASED) {
            if (popup != null) {
                popup.setVisible(false);
            }
        }
    }

    protected void processKeyEvent(KeyEvent e) {
        int id = e.getID();
        if (id == KeyEvent.KEY_PRESSED || id == KeyEvent.KEY_TYPED) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                popup.show(this, 0, 10);
            }
            super.processKeyEvent(e);
        }
    }
}
