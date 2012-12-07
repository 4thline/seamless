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

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import java.awt.event.ActionEvent;

/**
 * Well, it adds the "action command" to the constructor of JButton, that's all.
 *
 * @author Christian Bauer
 */
public class ActionButton extends JButton {

    public ActionButton(String actionCommand) {
        setActionCommand(actionCommand);
    }

    public ActionButton(Icon icon, String actionCommand) {
        super(icon);
        setActionCommand(actionCommand);
    }

    public ActionButton(String s, String actionCommand) {
        super(s);
        setActionCommand(actionCommand);
    }

    public ActionButton(Action action, String actionCommand) {
        super(action);
        setActionCommand(actionCommand);
    }

    public ActionButton(String s, Icon icon, String actionCommand) {
        super(s, icon);
        setActionCommand(actionCommand);
    }

    public ActionButton enableDefaultEvents(final Controller controller) {
        controller.registerAction(this,
                new DefaultAction() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        Event e;
                        if ((e = createDefaultEvent()) != null)
                            controller.fireEvent(e);
                        if ((e = createDefaultGlobalEvent()) != null)
                            controller.fireEventGlobal(e);
                    }
                }
        );
        return this;
    }

    public Event createDefaultEvent() {
        return null;
    }

    public Event createDefaultGlobalEvent() {
        return null;
    }

}
