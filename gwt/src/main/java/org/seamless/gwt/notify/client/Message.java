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

import java.util.logging.Level;

/**
 * @author Christian Bauer
 */
public class Message {

    private boolean modal;
    private Level level;
    private String title;
    private String detail;

    public Message(String title, String detail) {
        this(Level.INFO, title, detail);
    }

    public Message(Level level, String title, String detail) {
        this.level = level;
        this.title = title;
        this.detail = detail;
    }

    public Message(boolean modal, String title, String detail) {
        this(modal, Level.INFO, title, detail);
    }

    public Message(boolean modal, Level level, String title, String detail) {
        this.modal = modal;
        this.level = level;
        this.title = title;
        this.detail = detail;
    }

    public boolean isModal() {
        return modal;
    }

    public Level getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public String toString() {
        return "Level: " + getLevel()
                + ", Modal: " + isModal()
                + ", " + getTitle()
                + ", Detail: " + getDetail();
    }
}
