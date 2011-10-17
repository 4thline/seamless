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

package org.seamless.swing.logging;

import java.util.Date;
import java.util.logging.Level;
import java.text.SimpleDateFormat;

/**
 *
 */
public class LogMessage {
    
    private Level level;
    private Long createdOn = new Date().getTime();
    private String thread = Thread.currentThread().getName();
    private String source;
    private String message;

    public LogMessage(String message) {
        this(Level.INFO, message);
    }

    public LogMessage(String source, String message) {
        this(Level.INFO, source, message);
    }

    public LogMessage(Level level, String message) {
        this(level, null, message);
    }

    public LogMessage(Level level, String source, String message) {
        this.level = level;
        this.source = source;
        this.message = message;
    }

    public Level getLevel() {
        return level;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public String getThread() {
        return thread;
    }

    public String getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
        return getLevel() + " - " +
                dateFormat.format(new Date(getCreatedOn())) + " - " +
                getThread() + " : " + 
                getSource() + " : " +
                getMessage();
    }
}