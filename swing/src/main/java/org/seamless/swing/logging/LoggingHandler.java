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
package org.seamless.swing.logging;

import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * @author Christian Bauer
 */
public abstract class LoggingHandler extends Handler {

    public int sourcePathElements = 3;

    public LoggingHandler() {
    }

    public LoggingHandler(int sourcePathElements) {
        this.sourcePathElements = sourcePathElements;
    }

    public void publish(LogRecord logRecord) {
        LogMessage logMessage = new LogMessage(
                logRecord.getLevel(),
                getSource(logRecord),
                logRecord.getMessage()
        );

        log(logMessage);
    }

    public void flush() {
    }

    public void close() throws SecurityException {
    }

    protected String getSource(LogRecord record) {
        StringBuilder sb = new StringBuilder(180);
        String[] split = record.getSourceClassName().split("\\.");
        if (split.length > sourcePathElements) {
            split = Arrays.copyOfRange(split, split.length-sourcePathElements, split.length);
        }
        for (String s : split) {
            sb.append(s).append(".");
        }
        sb.append(record.getSourceMethodName());
        return sb.toString();
    }

    protected abstract void log(LogMessage msg);

}
