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

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Christian Bauer
 */
public class AbstractController<V extends Container> implements Controller<V> {

    private static Logger log = Logger.getLogger(AbstractController.class.getName());

    private V view;
    private Controller parentController;
    private java.util.List<Controller> subControllers = new ArrayList();
    private Map<String, DefaultAction> actions = new HashMap();
    private Map<Class, List<EventListener>> eventListeners = new HashMap();

    /**
     * Subclass is using the given view but is a root controller (e.g. Applet view)
     *
     * @param view
     */
    public AbstractController(V view) {
        this.view = view;
    }

    /**
     * Subclass wants to control own view and is root controller.
     */
    public AbstractController() {
    }

    /**
     * Subclass wants to control own view and is a subcontroller.
     *
     * @param parentController
     */
    public AbstractController(Controller parentController) {
        this(null, parentController);
    }

    /**
     * Subclass is completely dependend on the given view and is a subcontroller.
     *
     * @param view
     * @param parentController
     */
    public AbstractController(V view, Controller parentController) {
        this.view = view;

        // Check if this is a subcontroller or a root controller
        if (parentController != null) {
            this.parentController = parentController;
            parentController.getSubControllers().add(this);
        }
    }

    public V getView() {
        return view;
    }

    public Controller getParentController() {
        return parentController;
    }

    public java.util.List<Controller> getSubControllers() {
        return subControllers;
    }


    /**
     * This method disposes all subcontrollers first, recursively. Disposing a controller means it
     * will no longer be attached to its parent controller.
     */
    public void dispose() {
        log.fine("Disposing controller");
        Iterator<Controller> it = subControllers.iterator();
        while (it.hasNext()) {
            Controller subcontroller = it.next();
            subcontroller.dispose();
            it.remove();
        }
    }

    /**
     * Register an action that can be executed by this controller.
     *
     * @param source The prepared (== has action command assigned) action trigger source
     * @param action An actual action implementation.
     */
    public void registerAction(AbstractButton source, DefaultAction action) {
        source.removeActionListener(this);
        source.addActionListener(this);
        this.actions.put(source.getActionCommand(), action);
    }

    /**
     * Register an action that can be executed by this controller.
     *
     * @param source        The source component, this method sets action command and registers the controller as listener.
     * @param actionCommand The action command, used as a key when registering and executing actions.
     * @param action        An actual action implementation.
     */
    public void registerAction(AbstractButton source, String actionCommand, DefaultAction action) {
        source.setActionCommand(actionCommand);
        registerAction(source, action);
    }

    public void deregisterAction(String actionCommand) {
        this.actions.remove(actionCommand);
    }

    /**
     * Register an event listener that is being executed when an event is intercepted by this controller.
     *
     * @param eventClass    The actual event class this listeners is interested in.
     * @param eventListener The listener implementation.
     */
    public void registerEventListener(Class eventClass, EventListener eventListener) {
        log.fine("Registering listener: " + eventListener + " for event type: " + eventClass.getName());
        java.util.List<EventListener> listenersForEvent = eventListeners.get(eventClass);
        if (listenersForEvent == null) {
            listenersForEvent = new ArrayList<EventListener>();
        }
        listenersForEvent.add(eventListener);
        eventListeners.put(eventClass, listenersForEvent);
    }

    /**
     * Fire an event and pass it into the hierarchy of controllers.
     * <p/>
     * The event is propagated only to the controller instance and its subcontrollers, not upwards in the hierarchy.
     *
     * @param event The event to be propagated.
     */
    public void fireEvent(Event event) {
        fireEvent(event, false);
    }

    /**
     * Fire an event and pass it into the hierarchy of controllers.
     * <p/>
     * The event is propagated to the controller instance, its subcontrollers, and upwards into the controller
     * hierarchy. This operation effectively propagats the event to every controller in the whole hierarchy.
     *
     * @param event The event to be propagated.
     */
    public void fireEventGlobal(Event event) {
        fireEvent(event, true);
    }

    public void fireEvent(Event event, boolean global) {
        if (!event.alreadyFired(this)) {
            log.finest("Event has not been fired already");
            if (eventListeners.get(event.getClass()) != null) {
                log.finest("Have listeners for this type of event: " + eventListeners.get(event.getClass()));
                for (EventListener eventListener : eventListeners.get(event.getClass())) {
                    log.fine("Processing event: " + event.getClass().getName() + " with listener: " + eventListener.getClass().getName());
                    eventListener.handleEvent(event);
                }
            }
            event.addFiredInController(this);
            log.fine("Passing event: " + event.getClass().getName() + " DOWN in the controller hierarchy");
            for (Controller subController : subControllers) subController.fireEvent(event, global);
        } else {
            log.finest("Event already fired here, ignoring...");
        }
        if (getParentController() != null
                && !event.alreadyFired(getParentController())
                && global) {
            log.fine("Passing event: " + event.getClass().getName() + " UP in the controller hierarchy");
            getParentController().fireEvent(event, global);
        } else {
            log.finest("Event does not propagate up the tree from here");
        }
    }


    /**
     * Executes an action if it has been registered for this controller, otherwise passes it up the chain.
     * <p/>
     * This method extracts the source of the action (an <tt>AbstractButton</tt>) and gets the action
     * command. If the controller has this command registered, the registered action is executed. Otherwise
     * the action is passed upwards in the hierarchy of controllers.
     *
     * @param actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {

        try {
            AbstractButton button = (AbstractButton) actionEvent.getSource();
            String actionCommand = button.getActionCommand();
            DefaultAction action = actions.get(actionCommand);

            if (action != null) {
                // This controller can handle the action
                log.fine("Handling command: " + actionCommand + " with action: " + action.getClass());
                try {
                    preActionExecute();
                    log.fine("Dispatching to action for execution");
                    action.executeInController(this, actionEvent);
                    postActionExecute();
                } catch (RuntimeException ex) {
                    failedActionExecute();
                    throw ex;
                } catch (Exception ex) {
                    failedActionExecute();
                    throw new RuntimeException(ex);
                } finally {
                    finalActionExecute();
                }
            } else {
                // Let's try the parent controller in the hierarchy
                if (getParentController() != null) {
                    log.fine("Passing action on to parent controller");
                    parentController.actionPerformed(actionEvent);
                } else {
                    throw new RuntimeException("Nobody is responsible for action command: " + actionCommand);
                }
            }
        }
        catch (ClassCastException e) {
            throw new IllegalArgumentException("Action source is not an Abstractbutton: " + actionEvent);
        }
    }

    public void preActionExecute() {
    }

    public void postActionExecute() {
    }

    public void failedActionExecute() {
    }

    public void finalActionExecute() {
    }

    // If this controller is responsible for a JFrame, close it and all its children when the
    // window is closed.
    public void windowClosing(WindowEvent windowEvent) {
        dispose();
        ((JFrame)getView()).dispose();
    }

    public void windowOpened(WindowEvent windowEvent) {
    }

    public void windowClosed(WindowEvent windowEvent) {
    }

    public void windowIconified(WindowEvent windowEvent) {
    }

    public void windowDeiconified(WindowEvent windowEvent) {
    }

    public void windowActivated(WindowEvent windowEvent) {
    }

    public void windowDeactivated(WindowEvent windowEvent) {
    }

}
