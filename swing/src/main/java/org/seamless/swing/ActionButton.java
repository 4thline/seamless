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
