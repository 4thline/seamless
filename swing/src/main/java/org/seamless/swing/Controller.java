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

import javax.swing.AbstractButton;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

/**
 * Interface for building a hierarchical controller structure (HMVC).
 * <p/>
 * HMVC works with a tree of triads, these triads are a Model (usually several
 * JavaBeans and their binding models for the UI), a View (usually several Swing
 * UI components), and a Controller. This is a basic interface of a controller
 * that has a pointer to a parent controller (can be null if its the root of the
 * tree) and a collection of subcontrollers (can be empty, usually isn't empty).
 * <p/>
 * The hierarchy of controller supports propagation of action execution and
 * propagation of events.
 * <p/>
 * If a controllers view is a <tt>Frame</tt>, you should also register it as a
 * <tt>WindowListener</tt>, so that it can properly clean up its state when the
 * window is closed.
 *
 * @author Christian Bauer
 */
public interface Controller<V extends Container> extends ActionListener, WindowListener {

    public V getView();

    public Controller getParentController();

    public java.util.List<Controller> getSubControllers();

    public void dispose();

    public void registerEventListener(Class eventClass, EventListener eventListener);
    public void fireEvent(Event event);
    public void fireEventGlobal(Event event);
    public void fireEvent(Event event, boolean global);

    public void registerAction(AbstractButton source, DefaultAction action);
    public void registerAction(AbstractButton source, String actionCommand, DefaultAction action);
    void preActionExecute();
    void postActionExecute();
    void failedActionExecute();
    void finalActionExecute();
}

