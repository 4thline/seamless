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

import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 *
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
