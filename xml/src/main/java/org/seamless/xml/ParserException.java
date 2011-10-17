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

package org.seamless.xml;

import org.xml.sax.SAXParseException;

/**
 * Unified exception thrown by the <tt>DOMParser</tt> and <tt>SAXParser</tt>.
 *
 * @author Christian Bauer
 */
public class ParserException extends Exception {

    public ParserException() {
    }

    public ParserException(String s) {
        super(s);
    }

    public ParserException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ParserException(Throwable throwable) {
        super(throwable);
    }

    public ParserException(SAXParseException ex) {
        super("(Line/Column: " + ex.getLineNumber() + ":" + ex.getColumnNumber() + ") " + ex.getMessage());
    }
}
