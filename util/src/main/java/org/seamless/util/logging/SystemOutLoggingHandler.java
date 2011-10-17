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

package org.seamless.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import java.util.logging.Level;

/**
 * Clean one-line log output to <tt>System.out</tt>.
 * <p>
 * TODO: Make this pretty.
 * </p>
 * 
 * @author Christian Bauer
 */
public class SystemOutLoggingHandler extends StreamHandler {

    public SystemOutLoggingHandler() {
        super(System.out, new SimpleFormatter());
        setLevel(Level.ALL);
    }

    public void close() {
        flush();
    }

    public void publish(LogRecord record) {
        super.publish(record);
        flush();
    }

    public static class SimpleFormatter extends Formatter {
        public String format(LogRecord record) {
            StringBuffer buf = new StringBuffer(180);
            DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss,SS");

            buf.append("[").append(pad(Thread.currentThread().getName(), 32)).append("] ");
            buf.append(pad(record.getLevel().toString(), 12));
            buf.append(" - ");
            buf.append(pad(dateFormat.format(new Date(record.getMillis())), 24));
            buf.append(" - ");
            buf.append(toClassString(record.getSourceClassName(), 30));
            buf.append('#');
            buf.append(record.getSourceMethodName());
            buf.append(": ");
            buf.append(formatMessage(record));

            buf.append("\n");

            Throwable throwable = record.getThrown();
            if (throwable != null) {
                StringWriter sink = new StringWriter();
                throwable.printStackTrace(new PrintWriter(sink, true));
                buf.append(sink.toString());
            }

            return buf.toString();
        }

        public String pad(String s, int size) {
            if (s.length() < size) {
                for (int i = s.length(); i < size - s.length(); i++) {
                    s = s + " ";
                }
            }
            return s;
        }

        public String toClassString(String name, int maxLength) {
            return name.length() > maxLength ? name.substring(name.length() - maxLength) : name;
        }

    }

}
