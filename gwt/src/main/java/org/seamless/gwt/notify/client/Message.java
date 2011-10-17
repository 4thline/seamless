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
